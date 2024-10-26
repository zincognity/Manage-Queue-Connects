package control.utils;

import types.Tickets;
import java.util.ArrayList;

import control.views.ServerData;

public class TicketManage {
    private ArrayList<Tickets> ticket_connects;
    private ServerData update;

    public TicketManage(ServerData update) {
        ticket_connects = new ArrayList<Tickets>();
        this.update = update;
    }

    public ArrayList<Tickets> getTicketConnects() {
        return this.ticket_connects;
    }

    public int addTicket(Tickets ticket) {
        for (Tickets tkt : ticket_connects) {
            if(tkt.getIP().equals(ticket.getIP()) && tkt.getName().equals(ticket.getName())) {
                return 0;
            }
        }
        ticket_connects.add(ticket);
        update.getLabelTickets().setText(Integer.toString(ticket_connects.size()));
        return 1;
    }

    public int removeTicket(Tickets ticket) {
        for (Tickets tkt : ticket_connects) {
            if(tkt.getIP().equals(ticket.getIP()) && tkt.getName().equals(ticket.getName())) {
                ticket_connects.remove(tkt);
                update.getLabelTickets().setText(Integer.toString(ticket_connects.size()));
            }
        }
        return ticket_connects.size();
    }
}
