package types;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Ticket implements Serializable {
    protected Tickets ticket;
    protected Atention atention;
    protected Client client;
    protected String name;
    protected LocalDateTime createAt;

    public Ticket(Tickets ticket, Atention atention, Client client, String name) {
        this.ticket = ticket;
        this.atention = atention;
        this.client = client;
        this.name = name;
        this.createAt = LocalDateTime.now();
    }

    public Tickets getTickets() {
        return this.ticket;
    }

    public Atention getAtention() {
        return this.atention;
    }

    public void setAtention(Atention atention) {
        this.atention = atention;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreateAt() {
        return this.createAt;
    }
}
