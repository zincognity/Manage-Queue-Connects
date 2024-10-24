package atention;

import javax.swing.*;

import utils.ConfigLoader;
import types.Atention;
import types.Ticket;
import types.Tickets;

import java.net.InetAddress;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class JFrameA extends JFrame {
    private static ConfigLoader config = new ConfigLoader("atention/config/config.properties");
    
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private JLabel labelMessage;
    private JButton buttonMessage;
    private static JPanel panel = new JPanel(new GridBagLayout());
    private static GridBagConstraints gbc = new GridBagConstraints();

    private static Atention client;
    private ArrayList<Ticket> tickets = new ArrayList<Ticket>();

    public ArrayList<Ticket> getTickets() {
        try {
            sendMessage("GetTickets");
            Object readObject = input.readObject();

            if (readObject instanceof ArrayList<?>) {
                ArrayList<?> rawList = (ArrayList<?>) readObject;
                ArrayList<Ticket> safeTickets = new ArrayList<>();
                for (Object item : rawList) if (item instanceof Ticket) safeTickets.add((Ticket) item);
                tickets = safeTickets;
            }
            if (tickets.isEmpty()) {
                ArrayList<Ticket> defaultTicket = new ArrayList<>();
                defaultTicket.add(new Ticket(new Tickets("19", "si", "nose"), client, null, "None"));
                return defaultTicket;
            }
            System.out.println(tickets.size());
            return tickets;
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found during deserialization: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O error during deserialization: " + e.getMessage());
        }
        return tickets;
    }

    private void updateTickets() {
        panel.removeAll();
        getTickets();
        int index = 0;
        for (Ticket ticket : tickets) {
            labelMessage = new JLabel(ticket.getName() != null ? ticket.getName() : "NO EXISTE");
            gbc.gridx = 0;
            gbc.gridy = index;
            buttonMessage = new JButton("Atender Ticket");
            panel.add(labelMessage, gbc);
            gbc.gridx = 1;
            panel.add(buttonMessage, gbc);
            index++;
        }
        gbc.gridx = 0;
        gbc.gridy = index;
        gbc.anchor = GridBagConstraints.LINE_START;
        buttonMessage = new JButton("Actualizar");
        panel.add(buttonMessage, gbc);
        add(panel);
        revalidate();
        repaint();
    }

    public void initialize() {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            client = new Atention(ip, config.getProperty("atention.name"), null);
            sendMessage("Initialize");

            int res = (int) input.readObject();
            if(res == 0) return;

            /* PANEL */
            gbc.insets = new Insets(10, 10, 10, 10);

            /* LABELS AND FIELDS */

            /* BUTTONS */

            /* ACTIONS */

            /* ADDS */
            updateTickets();

            /* CONFIGS */
            setTitle("ATENCION " + config.getProperty("atention.name"));
            setSize(400, 200);
            setMinimumSize(new Dimension(300, 200));
            setLocationRelativeTo(null);
            setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                        try {
                            sendMessage("Close");
                        } catch (Exception e1) {
                            System.err.println("Error al enviar el mensaje: " + e1.getMessage());
                            e1.printStackTrace();
                        } finally {
                            closeResources();
                            dispose();
                        }
                        dispose();
                }
            });
            setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message){
        try {
            socket = new Socket(config.getProperty("control.server"),Integer.parseInt(config.getProperty("control.port")));
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            client.setMessage(message);
            output.writeObject(client);
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeResources() {
        try {
            if (output != null) output.close();
            if (input != null) input.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
