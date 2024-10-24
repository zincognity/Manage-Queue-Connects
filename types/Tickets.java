package types;

import models.ClientBase;
import models.Message;

public class Tickets extends ClientBase {
    public Tickets(String ip, String name, Message message) {
        super("TICKETS", ip, name, message);
    }
}
