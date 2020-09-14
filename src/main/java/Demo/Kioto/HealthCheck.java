package Demo.Kioto;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Date;

public final class HealthCheck {
    private String event;
    private String factory;
    private String serialNumber;
    private String type;
    private String status;
    private Date lastStartAt;
    private float temperature;
    private String ipAddress;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastStartAt() {
        return lastStartAt;
    }

    public void setLastStartAt(Date lastStartAt) {
        this.lastStartAt = lastStartAt;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public HealthCheck(String event, String factory, String serialNumber, String type, String status, Date lastStartAt, float temperature, String ipAddress) {
        this.event = event;
        this.factory = factory;
        this.serialNumber = serialNumber;
        this.type = type;
        this.status = status;
        this.lastStartAt = lastStartAt;
        this.temperature = temperature;
        this.ipAddress = ipAddress;
    }

    public HealthCheck(String json) throws JsonProcessingException {

        HealthCheck temp = Constants.getJsonMapper().readValue(json,HealthCheck.class);

        this.event = temp.event;
        this.factory = temp.factory;
        this.serialNumber = temp.serialNumber;
        this.type = temp.type;
        this.status = temp.status;
        this.lastStartAt = temp.lastStartAt;
        this.temperature = temp.temperature;
        this.ipAddress = temp.ipAddress;

    }

    public HealthCheck(){

    }

}
