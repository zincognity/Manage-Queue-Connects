package types;

import models.ClientBase;

public class Manage extends ClientBase {
    public Manage(String ip, String name, String message) {
        super("MANAGE", ip, name, message);
    }
}
