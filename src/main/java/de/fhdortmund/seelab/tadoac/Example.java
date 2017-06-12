package de.fhdortmund.seelab.tadoac;

import de.fhdortmund.seelab.tadoac.Model.*;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author Jonas Fleck
 */
public class Example {

    public static void main (String[] args) {
        try {
            TadoACConnector ac = new TadoACConnector("foo@bar.tld", "foobar");
            Arrays.asList(ac.getHomes()).stream().forEach((l) -> System.out.println(l.getName()));

            TadoACSetting s = new TadoACSetting();
            System.out.println(s.getPower());
            s.setPower(TadoACPower.OFF);
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
}
