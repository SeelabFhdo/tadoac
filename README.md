# Tado AC Control Java Wrapper
This is a simple Java wrapper for the basic functions of the Tado AC control API 

This wrapper uses the same API as the original Tado Web App. Please notice that the API isn't official for third party developers yet.

## Quickstart

We need to find out two parameters: Your home ID and your zone ID.

First we need to login into the webinterface (https://my.tado.com). Then navigate to your zone that you want to control and look at your 
URL-bar. 
In our example chrome shows us this URL:


`https://my.tado.com/webapp/#/home/zone/1`

The last number is your zone id. Finding out the home id is a little bit tricky. We need to sniffer the API calls. We recommend the Google
Chrome developer tools. Look for a call looking like this:

`https://my.tado.com/api/v2/homes/1234/zones/1/state`

In this case `1234` is your home ID

## example

`public class Example {

    public static void main (String[] args) {
        try {
            TadoACConnector ac = new TadoACConnector("foo@bar.tld", "foobar");
            Arrays.asList(ac.getHomes()).stream().forEach((l) -> System.out.println(l.getName()));

            TadoACSetting s = new TadoACSetting();
            System.out.println(s.getPower());
            s.setPower(TadoACPower.ON);
            TadoACTemperature t = new TadoACTemperature();
            t.setCelsius(20);
            s.setTemperature(t);
            s.setMode(TadoACMode.COOL);
            s.setFanSpeed(TadoACFanSpeed.MIDDLE);
            ac.setSetting(1234, 1, s);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}`
