package control;

import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.*;

import control.model.Atention;
import control.model.Client;
import control.model.ClientAtention;
import control.model.Queue;
import control.model.Ticket;
import control.utils.ClientDataProcessor;
import control.utils.ConfigLoader;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class JFrameC extends JFrame{
    private static final String DATA_FILE_PATH = "control/data/clients.csv";
    private static ClientDataProcessor dataProcessor = new ClientDataProcessor();
    private static ConfigLoader config = new ConfigLoader("control/config/config.properties");
    private static int length = Integer.parseInt(config.getProperty("queue.size"));
    private static int port = Integer.parseInt(config.getProperty("control.port"));
    private String ip;

    private static Queue queue = new Queue(length);

    private List<Atention> atention_connects = new ArrayList<Atention>();
    private List<Ticket> ticket_connects = new ArrayList<Ticket>();
    private List<Client> clients = dataProcessor.loadClients(DATA_FILE_PATH);
    private int countAtention = 0;

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
            labelQueueCountResponse = new JLabel(Integer.toString(queue.getSize()));
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

            add(panel);

         /* CONFIGS */
            setTitle("Tickets");
            setSize(400, 800);
            setMinimumSize(new Dimension(300, 200));
            setLocationRelativeTo(null);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setVisible(true);

            while(true) {
                Socket socket = server.accept();
                new Thread(new Server(socket, queue, this)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int addAtention(String name){
        atention_connects.add(new Atention(ip, name, atention_connects.size(), null));
        labelAtentionsResponse.setText(Integer.toString(atention_connects.size()));
        return atention_connects.size() - 1;
    }

    public String getAtentions() {
        StringBuilder atentions = new StringBuilder();
        for (int i = 0; i < atention_connects.size(); i++) {
            Atention atention = atention_connects.get(i);
            atentions.append(atention.getName() + ":" + atention.getIndex() + ":" + atention.getAtending());
            if (i < atention_connects.size() - 1) {
                atentions.append(",");
            }
        }
        System.out.println(atentions.toString());
        return atentions.toString();
    }

    public String getQueue(){
        StringBuilder queues = new StringBuilder();

        for (Object obj : queue.getObjects()) {
            if (obj instanceof ClientAtention) {
                ClientAtention cl = (ClientAtention) obj;
                queues.append(cl.getName() + ":" + cl.getAtentionIndex());
                if (cl.getAtentionIndex() < atention_connects.size() - 1) {
                    queues.append(",");
                }
            }
        }

        return queues.toString();
    }

    public void deleteAtention(int index){
        atention_connects.remove(index);
        labelAtentionsResponse.setText(Integer.toString(atention_connects.size()));
    }

    public int addTicket(String name){
        ticket_connects.add(new Ticket(ip, name, ticket_connects.size()));
        labelTicketsResponse.setText(Integer.toString(ticket_connects.size()));
        return ticket_connects.size() - 1;
    }

    public void deleteTicket(int index){
        ticket_connects.remove(index);
        labelTicketsResponse.setText(Integer.toString(ticket_connects.size()));
    }

    public String addClientToQueue(String dni){
        int index = 0;
        int found = -1;
        for (Client client : clients) {
            if(client.getDNI() == ((Integer.parseInt(dni)))){
                found = index;
                break;
            }
            index++;
        }
        if(found == -1){
            return "No encontrado";
        }
        Client client = clients.get(found);

        for (Object obj : queue.getObjects()) {
            if (obj instanceof ClientAtention) {
                ClientAtention cl = (ClientAtention) obj;
                if(client.getDNI() == cl.getDNI()){
                    return "Ya tienes un ticket:" + atention_connects.get(cl.getAtentionIndex()).getAtending();
                }
            }
        }

        String ticket = "AT-" + countAtention;

        for (Atention atention : atention_connects) {
            if(atention.getAtending() == "Disponible"){
                atention.setAtending(ticket);
                return "Su ticket es:" + ticket;
            }
        }

        ClientAtention clientAtention = new ClientAtention(client.getDNI(), client.getName(), client.getLastName(), client.getAgeBorn(), -1);
        countAtention++;
        boolean response = queue.addObject(clientAtention);
        if(!response) return "Valor maximo alcanzado";
        labelQueueCountResponse.setText(Integer.toString(queue.getSize()));
        return "Agregado exitósamente, espere su turno";
    }
}
