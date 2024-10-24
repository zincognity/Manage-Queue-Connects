package types;

import java.io.Serializable;

public class AtentionAvaliable implements Serializable {
    protected Atention atention;
    protected String ticket;
    
    public AtentionAvaliable(Atention atention) {
        this.atention = atention;
        this.ticket = "Disponible";
    }

    public Atention getAtention() {
        return this.atention;
    }

    public String getTicket() {
        return this.ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
