package de.fhdortmund.seelab.tadoac.Model;

/**
 * @author Jonas Fleck
 */
public class TadoACSetting {
    private String type = "AIR_CONDITIONING";
    private TadoACPower power;
    private TadoACMode mode;
    private TadoACFanSpeed fanSpeed;
    private TadoACTemperature temperature;

    public TadoACSetting build() {
        TadoACSetting s = new TadoACSetting();
        s.power = this.power;
        if(this.power == TadoACPower.ON) {
            s.mode = this.mode;
            s.fanSpeed = this.fanSpeed;
            if(this.mode == TadoACMode.COOL) {
                s.temperature = this.temperature;
            }
        }
        return s;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TadoACPower getPower() {
        return power;
    }

    public void setPower(TadoACPower power) {
        this.power = power;
    }

    public TadoACMode getMode() {
        return mode;
    }

    public void setMode(TadoACMode mode) {
        this.mode = mode;
    }

    public TadoACFanSpeed getFanSpeed() {
        return fanSpeed;
    }

    public void setFanSpeed(TadoACFanSpeed fanSpeed) {
        this.fanSpeed = fanSpeed;
    }

    public TadoACTemperature getTemperature() {
        return temperature;
    }

    public void setTemperature(TadoACTemperature temperature) {
        this.temperature = temperature;
    }
}
