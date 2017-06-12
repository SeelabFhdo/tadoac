package de.fhdortmund.seelab.tadoac;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.fhdortmund.seelab.tadoac.Exception.TadoApiException;
import de.fhdortmund.seelab.tadoac.Exception.TadoNotFoundException;
import de.fhdortmund.seelab.tadoac.Model.TadoHome;
import de.fhdortmund.seelab.tadoac.Model.TadoACSetting;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the base class for interacting with Tado AC Controllers.
 * @author Jonas Fleck
 */
public class TadoACConnector {

    private static final String REST_URL = "https://my.tado.com/api/v2";

    private String username;
    private String password;

    static {
        Logger.getLogger("org.apache.http").setLevel(Level.OFF);
    }

    /**
     *
     * @param username username of the tado account
     * @param password password of the tado account
     */
    public TadoACConnector(String username, String password) {
     this.username = username;
     this.password = password;
    }

    /**
     *
     * @return The list of homes to which the user has access.
     * @throws IOException
     */
    public TadoHome[] getHomes() throws IOException {
        HttpGet request = new HttpGet(REST_URL + "/me" + generateAuthString());
        JsonObject response = executeRequest(request);
        TadoHome[] homes = new Gson().fromJson(response.get("homes"), TadoHome[].class);
        return  homes;

    }


    /**
     *
     * @param homeid The id of the home
     * @param zone The id of the zone
     * @return The current setting of the zone. Returns null if there is no setting or the AC is not in manual mode.
     * @throws IOException
     */
    public TadoACSetting getSetting(int homeid, int zone) throws IOException {
        HttpGet request = new HttpGet(REST_URL + "/homes/" + homeid + "/zones/" + zone + "/overlay"
                + generateAuthString());
        try {
            JsonObject response = executeRequest(request);
            if(response.get("setting") == null) {
                return null;
            } else {
                return new Gson().fromJson(response.get("setting"), TadoACSetting.class);
            }
        } catch (TadoNotFoundException e) {
            return null;
        }

    }

    /**
     *
     * @param homeid The id of the home
     * @param zone The id of the zone
     * @param setting The setting which should be sent to the AC
     * @throws IOException
     */
    public void setSetting(int homeid, int zone, TadoACSetting setting) throws IOException {
        HttpPut request =  new HttpPut(REST_URL + "/homes/" + homeid + "/zones/" + zone + "/overlay"
                + generateAuthString());
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("setting", gson.toJsonTree(setting.build()));
        JsonObject termination = new JsonObject();
        termination.addProperty("type", "MANUAL");
        jsonObject.add("termination", termination);

        StringEntity entity = new StringEntity(gson.toJson(jsonObject), "UTF-8");
        entity.setContentType("application/json");
        request.setEntity(entity);
        executeRequest(request);
    }


    private String generateAuthString() {
        try {
            return String.format("?username=%s&password=%s", URLEncoder.encode(username, "UTF-8"),
                    URLEncoder.encode(password, "UTF-8"));
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private JsonObject executeRequest(HttpUriRequest request) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader
                (new InputStreamReader(
                        response.getEntity().getContent()));

        String body = "";
        String line;
        while ((line = rd.readLine()) != null) {
            body += line;
        }
        Gson gson = new Gson();
        JsonObject obj = gson.fromJson(body, JsonElement.class).getAsJsonObject();
        JsonArray errors = obj.getAsJsonArray("errors");
        if(errors != null && errors.size() > 0) {
            String errosStrings = "Following errors occured while connecting the API:";
            for(JsonElement error : errors) {
                if(error.getAsJsonObject().get("code") != null &&
                        error.getAsJsonObject().get("code").getAsString().equals("notFound")) {
                    throw  new TadoNotFoundException();
                }
                errosStrings += "\n" + error.getAsJsonObject().get("title").getAsString();
            }
            throw new TadoApiException(errosStrings);
        }
        return obj;
    }
}
