package atention;

import javax.swing.*;

import models.Message;
import utils.ConfigLoader;
import types.Atention;
import types.Ticket;

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

    private static JPanel panel = new JPanel(new GridBagLayout());
    private static GridBagConstraints gbc = new GridBagConstraints();

    private static Atention client;
    private ArrayList<Ticket> tickets = new ArrayList<Ticket>();

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

    public void updateTickets() {
        JButton buttonMessage;
        JLabel labelMessage;
        panel.removeAll();
        getTickets();
        int index = 0;
        for (Ticket ticket : tickets) {
            labelMessage = new JLabel(ticket.getName() != null ? ticket.getName() : "NO EXISTE");
            gbc.gridx = 0;
            gbc.gridy = index;
            buttonMessage = new JButton("Atender Ticket");
            buttonMessage.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onSubmit(ticket);
                }
            });
            panel.add(labelMessage, gbc);
            gbc.gridx = 1;
            panel.add(buttonMessage, gbc);
            index++;
        }
        gbc.gridx = 0;
        gbc.gridy = index;
        gbc.anchor = GridBagConstraints.LINE_START;
        buttonMessage = new JButton("Actualizar");
        buttonMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTickets();
            }
        });
        panel.add(buttonMessage, gbc);
        add(panel);
        revalidate();
        repaint();
    }

    public void onSubmit(Ticket ticket) {
        sendMessage("ClaimTicket", ticket);
    }

    // public void formAtended() {
    // JFrame form = new JFrame();

    // }

    public void initialize() {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            client = new Atention(ip, config.getProperty("atention.name"), new Message(null, null));

            sendMessage("Initialize", null);

            int res = (int) input.readObject();
            if (res == 0)
                return;

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

    private void listener() {
        new Thread(() -> {
            while (true) {
                updateTickets();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendMessage(String message, Object object) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
