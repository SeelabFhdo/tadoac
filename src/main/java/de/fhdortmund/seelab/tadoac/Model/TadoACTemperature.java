package de.fhdortmund.seelab.tadoac.Model;

/**
 * @author Jonas Fleck
 */
public class TadoACTemperature {
    private float celsius;

    public float getCelsius() {
        return celsius;
    }

    public void setCelsius(float celsius) {
        this.celsius = celsius;
    }

    public float getFahrenheit() {
        return 32 + (celsius * 9 / 5);
    }

    public void setFahrenheit(float fahrenheit) {
       celsius =  ((fahrenheit - 32)*5)/9;
    }
}
