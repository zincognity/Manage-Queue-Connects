package manage;

import javax.swing.*;

import manage.views.AtentionStatus;
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

    private static Socket socket;
    private static ObjectOutputStream output;
    private static ObjectInputStream input;

    private static JPanel panel = new JPanel(new GridBagLayout());
    private static GridBagConstraints gbc = new GridBagConstraints();

    private Manage client;
    private AtentionStatus atentionStatus = new AtentionStatus();
    private ArrayList<Ticket> tickets = new ArrayList<Ticket>();
    private ArrayList<Atention> atentions = new ArrayList<Atention>();

    public ArrayList<Ticket> getTickets() {
        try {
            sendMessage("GetTickets", null);
            Object readObject = input.readObject();

            if (readObject instanceof ArrayList<?>) {
                ArrayList<?> rawList = (ArrayList<?>) readObject;
                ArrayList<Ticket> safeTickets = new ArrayList<Ticket>();
                for (Object item : rawList)
                    if (item instanceof Ticket)
                        safeTickets.add((Ticket) item);
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
                for (Object item : rawList)
                    if (item instanceof Atention)
                        safeAtentions.add((Atention) item);
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
        getTickets();
        getAtentions();
        panel.removeAll();
        if (atentionStatus != null)
            atentionStatus.updateData(atentions, tickets);
        else {
            gbc.gridx = 0;
            gbc.gridy = 3;
            JLabel labelTicket = new JLabel("NO HAY ATENCIONES DISPONIBLES");
            panel.add(labelTicket, gbc);
        }

        panel.add(atentionStatus, gbc);
        add(panel);
        revalidate();
        repaint();
    }

    private void listener() {
        new Thread(() -> {
            while (true) {
                try {
                    Object response = input.readObject();
                    if (response != null)
                        if (response.equals("UPDATE")) {
                            updateData();
                        }
                } catch (EOFException e) {
                    System.out.println("El servidor cerró la conexión antes de enviar datos.");
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void initialize() {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            client = new Manage(ip, config.getProperty("manage.name"), new Message(null, null));
            sendMessage("Initialize", null);

            int res = (int) input.readObject();
            if (res == 0)
                return;

            gbc.insets = new Insets(10, 10, 10, 10);

            updateData();

            setTitle("MANAGE " + config.getProperty("manage.name"));
            setSize(500, 800);
            setMinimumSize(new Dimension(500, 800));
            setMaximumSize(new Dimension(500, 800));
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

    public void sendMessage(String message, Object object) {
        try {
            socket = new Socket(config.getProperty("control.server"),
                    Integer.parseInt(config.getProperty("control.port")));
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
            if (output != null)
                output.close();
            if (input != null)
                input.close();
            if (socket != null && !socket.isClosed())
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
