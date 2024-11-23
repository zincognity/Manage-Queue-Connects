package types;

import models.ClientBase;
import models.Message;

public class Manage extends ClientBase {

    public Manage(String ip, String name, Message message) {
        super("MANAGE", ip, name, message);
    }

}
