package control.utils;

import types.Atention;
import types.Client;
import types.Ticket;
import types.Tickets;

import java.util.ArrayList;
import java.util.List;

import control.views.ServerData;

import java.time.Duration;
import java.time.LocalDateTime;

public class TicketsManage {
    private ArrayList<Ticket> tickets;
    private ServerData update;
    private List<Client> clients;

    public TicketsManage(ServerData update, List<Client> clients) {
        tickets = new ArrayList<Ticket>();
        this.update = update;
        this.clients = clients;
    }

    public Client reqClient(int dni) {
        Client clientRes = null;
        for (Client clt : clients) {
            if(clt.getDNI() == dni) clientRes = clt;
        }
        return clientRes;
    }

    public Ticket createTicket(int dni, Tickets ticket, int queueMax) {
        Client clientResponse = reqClient(dni);
        Ticket newTicket = new Ticket(ticket, new Atention(null, null, null), null, null);

        if (clientResponse == null) {
            newTicket.getTickets().getMessage().setMessage("EL CLIENTE NO EXISTE");
            return newTicket;
        }
        int enableTickets = 0;
        for (Ticket tkt : tickets) {
            if(tkt.getAtention() == null) enableTickets++;
            if (tkt.getClient().getDNI() == dni) {
                LocalDateTime lastTicketTime = tkt.getCreateAt();
                Duration duration = Duration.between(lastTicketTime, LocalDateTime.now());
    
                if (duration.toMinutes() < 5) {
                    newTicket.getTickets().getMessage().setMessage("YA TIENES UN TICKET, DEBES ESPERAR AL MENOS 5 MINUTOS PARA SACAR OTRO");
                    return newTicket;
                }
            }
        }

        if(enableTickets >= queueMax) {
            newTicket.getTickets().getMessage().setMessage("EL LÍMITE DE PERSONAS EN COLA HA SIDO SUPERADO, POR FAVOR ESPERE UNOS MINUTOS");
            return newTicket;
        }

        String name = "T-" + tickets.size();
        newTicket = new Ticket(ticket, null, clientResponse, name);
        tickets.add(newTicket);
        update.getLabelQueueSize().setText(Integer.toString(getTickets().size()));
        return newTicket;
    }

    public ArrayList<Ticket> getTickets() {
        ArrayList<Ticket> filterTickets = new ArrayList<Ticket>();
        for (Ticket ticket : tickets) {
            if(ticket.getAtention() == null) {
                filterTickets.add(ticket);
            }
        }
        return filterTickets;
    }

    public ArrayList<Ticket> getAllTickets() {
        return tickets;
    }

    public int claimTicket(Atention atention, Ticket ticket) {
        for (Ticket tkt : tickets) {
            if(tkt.getName().equals(ticket.getName())) {
                tkt.setAtention(atention);
                return 1;
            }
        }
        update.getLabelQueueSize().setText(Integer.toString(getTickets().size()));
        return 0;
    }
}
