package types;

import java.io.Serializable;

public class AtentionAvaliable implements Serializable {
    protected Atention atention;
    protected Ticket ticket;
    
    public AtentionAvaliable(Atention atention, Ticket ticket) {
        this.atention = atention;
        this.ticket = ticket;
    }

    public Atention getAtention() {
        return this.atention;
    }

    public Ticket getTicket() {
        return this.ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
