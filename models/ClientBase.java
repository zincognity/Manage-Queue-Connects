package models;

import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ClientBase implements Serializable {
    protected String type;
    protected String ip;
    protected String name;
    protected Message message;
    protected ObjectOutputStream output;

    public ClientBase(String type, String ip, String name, Message message) {
        this.type = type;
        this.ip = ip;
        this.name = name;
        this.message = message;
    }

    public void print() {
        System.out.println("Tipo: " + type + "\nIP: " + ip + "\nNombre: " + name);
    };

    public String getType() {
        return this.type;
    }

    public String getIP() {
        return this.ip;
    }

    public String getName() {
        return this.name;
    }

    public Message getMessage() {
        return this.message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public ObjectOutputStream getOutput() {
        return this.output;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }
}
