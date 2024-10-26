package manage;

import javax.swing.*;

import models.Message;
import types.Atention;
import types.Manage;
import types.Ticket;
import utils.ConfigLoader;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import java.net.InetAddress;

public class JFrameM extends JFrame {
    private static ConfigLoader config = new ConfigLoader("manage/config/config.properties");
    
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private JLabel labelMessage;
    private JButton buttonMessage;
    private static JPanel panel = new JPanel(new GridBagLayout());
    private static GridBagConstraints gbc = new GridBagConstraints();

    private Manage client;
    private ArrayList<Ticket> tickets = new ArrayList<Ticket>();
    private ArrayList<Atention> atentions = new ArrayList<Atention>();

    public ArrayList<Ticket> getTickets() {
        try {
            sendMessage("GetTickets", null);
            Object readObject = input.readObject();

            if (readObject instanceof ArrayList<?>) {
                ArrayList<?> rawList = (ArrayList<?>) readObject;
                ArrayList<Ticket> safeTickets = new ArrayList<Ticket>();
                for (Object item : rawList) if (item instanceof Ticket) safeTickets.add((Ticket) item);
                tickets = safeTickets;
            }
            return tickets;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public ArrayList<Atention> getAtentions() {
        try {
            sendMessage("GetAtentions", null);
            Object readObject = input.readObject();

            if (readObject instanceof ArrayList<?>) {
                ArrayList<?> rawList = (ArrayList<?>) readObject;
                ArrayList<Atention> safeAtentions = new ArrayList<Atention>();
                for (Object item : rawList) if (item instanceof Atention) safeAtentions.add((Atention) item);
                atentions = safeAtentions;
            }
            return atentions;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return atentions;
    }

    private void updateData() {
        panel.removeAll();
        getTickets();
        getAtentions();
        int index = 0;
        for (Atention atention : atentions) {
            labelMessage = new JLabel(atention.getName());
            gbc.gridx = 0;
            gbc.gridy = index;
            panel.add(labelMessage, gbc);
            labelMessage = new JLabel(getAtention(atention));
            gbc.gridx = 1;
            panel.add(labelMessage, gbc);
            index++;
        }
        gbc.gridx = 0;
        gbc.gridy = index;
        gbc.anchor = GridBagConstraints.LINE_START;
        buttonMessage = new JButton("Actualizar");
        buttonMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateData();
            } 
        });
        panel.add(buttonMessage, gbc);
        add(panel);
        revalidate();
        repaint();
    }

    private void listener() throws InterruptedException {
        while (true) {
            updateData();
            Thread.sleep(1000);
        }
    }

    private String getAtention(Atention atention) {
        for (Ticket tkt : tickets) {
            if(tkt.getAtention() != null) {
                if(tkt.getAtention().getName().equals(atention.getName())) return tkt.getName();
            }
        }
        return "Disponible";
    }

    public void initialize() {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            client = new Manage(ip, config.getProperty("manage.name"), new Message(null, null));
            sendMessage("Initialize", null);

            int res = (int) input.readObject();
            if(res == 0) return;

         /* PANEL */
            gbc.insets = new Insets(10, 10, 10, 10);

         /* LABELS AND FIELDS */

         /* BUTTONS */

         /* ACTIONS */

         /* ADDS */
            updateData();

         /* CONFIGS */
            setTitle("MANAGE " + config.getProperty("manage.name"));
            setSize(400, 200);
            setMinimumSize(new Dimension(300, 200));
            setLocationRelativeTo(null);
            setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                        try {
                            sendMessage("Close", null);
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
            listener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message, Object object) {
        try {
            socket = new Socket(config.getProperty("control.server"),Integer.parseInt(config.getProperty("control.port")));
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            client.getMessage().setMessage(message);
            client.getMessage().setObject(object);
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
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
