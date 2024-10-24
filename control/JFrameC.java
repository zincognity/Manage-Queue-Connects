package control;

import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.*;

import models.Message;
import types.Atention;
import types.Manage;
import utils.ClientDataProcessor;
import utils.ConfigLoader;
import types.Tickets;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import types.Ticket;
import types.Client;
import java.time.Duration;
import java.time.LocalDateTime;

public class JFrameC extends JFrame{
    /* CONFIG */
    private static final String DATA_FILE_PATH = "control/data/clients.csv";
    private static ClientDataProcessor dataProcessor = new ClientDataProcessor();
    private static ConfigLoader config = new ConfigLoader("control/config/config.properties");
    private static int length = Integer.parseInt(config.getProperty("queue.size"));
    private static int port = Integer.parseInt(config.getProperty("control.port"));
    
    private String ip;

    private ArrayList<Atention> atention_connects = new ArrayList<Atention>();
    private ArrayList<Tickets> ticket_connects = new ArrayList<Tickets>();
    private ArrayList<Manage> manage_connects = new ArrayList<Manage>();

    private List<types.Client> clients = dataProcessor.loadClients(DATA_FILE_PATH);

    private ArrayList<Ticket> tickets = new ArrayList<Ticket>();

    private JLabel labelManageResponse;
    private JLabel labelTicketsResponse;
    private JLabel labelAtentionsResponse;
    private JLabel labelQueueCountResponse;

    public void initialize() {
        try (ServerSocket server = new ServerSocket(port)){
            /* PANEL */
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);

            ip = InetAddress.getLocalHost().getHostAddress();

            /* LABELS AND FIELDS */
            JLabel labelIP = new JLabel("DIRECCIÓN IP:");
            JLabel labelIPResponse = new JLabel(ip);
            JLabel labelPort = new JLabel("PUERTO:");
            JLabel labelPortResponse = new JLabel(Integer.toString(port));
            JLabel labelMax = new JLabel("TAMAÑO MÁXIMO DE LA COLA:");
            JLabel labelMaxResponse = new JLabel(Integer.toString(length));
            JLabel labelCount = new JLabel("PERSONAS EN LA COLA:");
            labelQueueCountResponse = new JLabel("1");
            JLabel labelManage = new JLabel("PANTALLAS TRABAJANDO");
            labelManageResponse = new JLabel(Integer.toString(manage_connects.size()));
            JLabel labelTickets = new JLabel("TICKETS TRABAJANDO:");
            labelTicketsResponse = new JLabel(Integer.toString(ticket_connects.size()));
            JLabel labelAtentions = new JLabel("ATENCIONES TRABAJANDO:");
            labelAtentionsResponse = new JLabel(Integer.toString(atention_connects.size()));

            /* BUTTONS */

            /* ACTIONS */

            /* ADDS */
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.LINE_START;
            panel.add(labelIP, gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.LINE_START;
            panel.add(labelIPResponse, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.LINE_START;
            panel.add(labelPort, gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.LINE_START;
            panel.add(labelPortResponse, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.anchor = GridBagConstraints.LINE_START;
            panel.add(labelMax, gbc);

            gbc.gridx = 1;
            gbc.gridy = 2;
            gbc.anchor = GridBagConstraints.LINE_START;
            panel.add(labelMaxResponse, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.anchor = GridBagConstraints.LINE_START;
            panel.add(labelCount, gbc);

            gbc.gridx = 1;
            gbc.gridy = 3;
            gbc.anchor = GridBagConstraints.LINE_START;
            panel.add(labelQueueCountResponse, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.anchor = GridBagConstraints.LINE_START;
            panel.add(labelAtentions, gbc);

            gbc.gridx = 1;
            gbc.gridy = 4;
            gbc.anchor = GridBagConstraints.LINE_START;
            panel.add(labelAtentionsResponse, gbc);

            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.anchor = GridBagConstraints.LINE_START;
            panel.add(labelTickets, gbc);

            gbc.gridx = 1;
            gbc.gridy = 5;
            gbc.anchor = GridBagConstraints.LINE_START;
            panel.add(labelTicketsResponse, gbc);

            gbc.gridx = 0;
            gbc.gridy = 6;
            gbc.anchor = GridBagConstraints.LINE_START;
            panel.add(labelManage, gbc);

            gbc.gridx = 1;
            gbc.gridy = 6;
            gbc.anchor = GridBagConstraints.LINE_START;
            panel.add(labelManageResponse, gbc);

            add(panel);

            /* CONFIGS */
            setTitle("Control");
            setSize(400, 800);
            setMinimumSize(new Dimension(300, 200));
            setLocationRelativeTo(null);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setVisible(true);

            while(true) {
                Socket socket = server.accept();
                new Thread(new Server(socket, this)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int addManage(Manage manage) {
        for (Manage mng : manage_connects) {
            if(mng.getIP().equals(manage.getIP()) && mng.getName().equals(manage.getName())) {
                return 0;
            }
        }
        manage_connects.add(manage);
        labelManageResponse.setText(Integer.toString(manage_connects.size()));
        return 1;
    }

    public int removeManage(Manage manage) {
        for (Manage mng : manage_connects) {
            if(mng.getIP().equals(manage.getIP()) && mng.getName().equals(manage.getName())) {
                manage_connects.remove(mng);
                labelManageResponse.setText(Integer.toString(manage_connects.size()));
            }
        }
        return manage_connects.size();
    }

    public int addAtention(Atention atention){
        for (Atention atn : atention_connects) {
            if(atn.getIP().equals(atention.getIP()) && atn.getName().equals(atention.getName())) {
                return 0;
            }
        }
        atention_connects.add(atention);
        labelAtentionsResponse.setText(Integer.toString(atention_connects.size()));
        return 1;
    }

    public int removeAtention(Atention atention){
        for (Atention atn : atention_connects) {
            if(atn.getIP().equals(atention.getIP()) && atn.getName().equals(atention.getName())) {
                atention_connects.remove(atn);
                labelAtentionsResponse.setText(Integer.toString(atention_connects.size()));
            }
        }
        return atention_connects.size();
    }

    public int addTicket(Tickets ticket) {
        for (Tickets tkt : ticket_connects) {
            if(tkt.getIP().equals(ticket.getIP()) && tkt.getName().equals(ticket.getName())) {
                return 0;
            }
        }
        ticket_connects.add(ticket);
        labelTicketsResponse.setText(Integer.toString(ticket_connects.size()));
        return 1;
    }

    public int removeTicket(Tickets ticket) {
        for (Tickets tkt : ticket_connects) {
            if(tkt.getIP().equals(ticket.getIP()) && tkt.getName().equals(ticket.getName())) {
                ticket_connects.remove(tkt);
                labelTicketsResponse.setText(Integer.toString(ticket_connects.size()));
            }
        }
        return ticket_connects.size();
    }

    public Client reqClient(int dni) {
        Client clientRes = null;
        for (Client clt : clients) {
            if(clt.getDNI() == dni) clientRes = clt;
        }
        return clientRes;
    }

    public Ticket createTicket(int dni, Tickets ticket) {
        Client clientResponse = reqClient(dni);
        Ticket newTicket = new Ticket(new Tickets(null, null, new Message(null, null)), null, null, null);

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

        if(enableTickets >= Integer.parseInt(config.getProperty("queue.size"))) {
            newTicket.getTickets().getMessage().setMessage("EL LÍMITE DE PERSONAS EN COLA HA SIDO SUPERADO, POR FAVOR ESPERE UNOS MINUTOS");
            return newTicket;
        }

        String name = "T-" + tickets.size();
        newTicket = new Ticket(ticket, null, clientResponse, name);
        tickets.add(newTicket);
    
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

    public int claimTicket(Atention atention, Ticket ticket) {
        for (Ticket tkt : tickets) {
            if(tkt.getName().equals(ticket.getName())) {
                tkt.setAtention(atention);
                tkt.setName(tkt.getName() + " ATENDIDO POR: " + atention.getName());
                return 1;
            }
        }
        return 0;
    }
}
