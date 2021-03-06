package com.energyxxer.craftr.global;

/**
 * Created by User on 12/15/2016.
 */
public class Status {
    private String message = "";
    private String type = INFO;

    public static final String INFO = "INFO";
    public static final String WARNING = "WARNING";
    public static final String ERROR = "ERROR";

    public Status() {}

    public Status(String message) {
        this.message = message;
    }

    public Status(String message, String type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
