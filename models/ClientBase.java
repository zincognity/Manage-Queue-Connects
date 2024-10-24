package models;

import java.io.Serializable;

public class ClientBase implements Serializable{
    protected String type;
    protected String ip;
    protected String name;
    protected String message;

    public ClientBase(String type, String ip, String name, String message){
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

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
