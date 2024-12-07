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
        ArrayList<Tickets> serializableTickets = ticket_connects;
        serializableTickets.forEach(ticket -> {
            ticket.setOutput(null);
        });
        return serializableTickets;
    }

    public int addTicket(Tickets ticket) {
        for (Tickets tkt : ticket_connects) {
            if (tkt.getIP().equals(ticket.getIP()) && tkt.getName().equals(ticket.getName())) {
                return 0;
            }
        }
        ticket_connects.add(ticket);
        update.getLabelTickets().setText(Integer.toString(ticket_connects.size()));
        return 1;
    }

    public void removeTicket(Tickets ticket) {
        synchronized (ticket_connects) {
            ticket_connects.removeIf(currentTicket -> currentTicket.getIP().equals(ticket.getIP())
                    && currentTicket.getName().equals(ticket.getName()));
            update.getLabelAtentions().setText(Integer.toString(ticket_connects.size()));
        }
    }
}
