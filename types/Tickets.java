package types;

import models.ClientBase;

public class Tickets extends ClientBase {
    public Tickets(String ip, String name, String message) {
        super("TICKETS", ip, name, message);
    }
}
