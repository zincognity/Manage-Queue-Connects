package models;

import java.io.Serializable;

public class Message implements Serializable {
    protected String message;
    protected Object object;

    public Message(String message, Object object) {
        this.message = message;
        this.object = object;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObject() {
        return this.object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
