package atention;

import javax.swing.*;

import atention.views.Attending;
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
import atention.views.MenuBar;
import atention.views.TicketsData;

public class JFrameA extends JFrame {
    private static ConfigLoader config = new ConfigLoader("atention/config/config.properties");

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private static JPanel panel = new JPanel(new GridBagLayout());
    private GridBagConstraints gbc = new GridBagConstraints();

    private Attending attending;
    private Atention client;
    private ArrayList<Ticket> tickets = new ArrayList<Ticket>();

    private String ip;
    private String port;
    private static MenuBar menuBar = new MenuBar();
    private TicketsData ticketsData = new TicketsData();

    private boolean isAttending;

    private void printMenu() {
        menuBar.getConnectServer().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connectServer();
            }
        });
        menuBar.getDisconnectServer().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                disconnetServer();
            }
        });
        menuBar.getChangePort().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String result;
                result = JOptionPane.showInputDialog("Ingresa el puerto del servidor:");
                try {
                    Integer.parseInt(result);
                    port = result;
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresa un número entero válido.");
                }
            }
        });
        menuBar.getActiveAutomatic().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                return;
            }
        });
        menuBar.getHelp().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                return;
            }
        });
        setJMenuBar(menuBar);
    }

    private void printMessage(String message) {
        if (attending != null)
            remove(attending);
        panel.removeAll();
        JLabel labelIP = new JLabel(message);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(labelIP, gbc);

        add(panel);
        revalidate();
        repaint();
    }

    private void getTickets() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTickets() {
        getTickets();
        if (attending != null) {
            remove(attending);
            gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
        }
        panel.removeAll();
        if (ticketsData != null)
            ticketsData.updateData(tickets);
        if (ticketsData.getTicketButton() != null) {
            for (int i = 0; i < ticketsData.getTicketButton().size(); i++) {
                Ticket currentTicket = tickets.get(i);
                JButton currentButton = ticketsData.getTicketButton().get(i);
                currentButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        onAttend(currentTicket);
                    }
                });
            }
        } else {
            gbc.gridx = 0;
            gbc.gridy = 3;
            JLabel labelTicket = new JLabel("Sin tickets por atender");
            panel.add(labelTicket, gbc);
        }
        ticketsData.getTicketsQueue().setText(Integer.toString(tickets.size()));
        panel.add(ticketsData);

        add(panel);
        revalidate();
        repaint();
    }

    private void onAttend(Ticket ticket) {
        this.isAttending = true;
        remove(panel);
        setJMenuBar(null);
        attending = new Attending(gbc);
        attending.getLabelTicketName().setText(ticket.getName());
        attending.getLabelClientDNI().setText(Integer.toString(ticket.getClient().getDNI()));
        attending.getLabelClientNames().setText(ticket.getClient().getName() + " " + ticket.getClient().getLastName());
        attending.getSubmit().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!attending.getReason().getText().isEmpty() && !attending.getResponse().getText().isEmpty()) {
                    ticket.setReason(attending.getReason().getText());
                    ticket.setResponse(attending.getResponse().getText());
                    sendMessage("Attended", ticket);
                    Attended();
                }
            }
        });

        add(attending);
        revalidate();
        repaint();
        sendMessage("ClaimTicket", ticket);
        setSize(840, 330);
        setMinimumSize(new Dimension(840, 330));
        setMaximumSize(new Dimension(840, 330));
    }

    public void Attended() {
        setJMenuBar(menuBar);
        this.isAttending = false;
        attending.getLabelTicketName().setText("");
        attending.getLabelClientDNI().setText("");
        attending.getLabelClientNames().setText("");
        attending.getCountup().stopTimer();
        attending.getReason().setText("");
        attending.getResponse().setText("");
        setSize(550, 880);
        setMinimumSize(new Dimension(550, 880));
        setMaximumSize(new Dimension(550, 880));
    }

    public void connectServer() {
        new Thread(() -> {
            try {
                sendMessage("Initialize", null);
                int res = (int) input.readObject();
                if (res == 0) {
                    printMessage("Ya hay una aplicación conectada en tu escritorio.");
                    return;
                }
                this.isAttending = false;
                updateTickets();
                menuBar.getConnectServer().setEnabled(false);
                menuBar.getDisconnectServer().setEnabled(true);
                menuBar.getChangePort().setEnabled(false);
                listener();
            } catch (Exception e) {
                printMessage("Hubo un problema al conectar con el servidor.");
                e.printStackTrace();
            }
        }).start();
    }

    public void disconnetServer() {
        sendMessage("Close", null);
        closeResources();
        menuBar.getConnectServer().setEnabled(true);
        menuBar.getDisconnectServer().setEnabled(false);
        menuBar.getChangePort().setEnabled(true);
        printMessage("No está conectado a algún servidor.");
    }

    public void initialize() {
        try {
            panel = new JPanel(new GridBagLayout());
            gbc.insets = new Insets(10, 10, 10, 10);

            ip = InetAddress.getLocalHost().getHostAddress();
            port = config.getProperty("control.port");
            menuBar.getDisconnectServer().setEnabled(false);
            menuBar.getChangePort().setEnabled(true);

            printMessage("No estás conectado a algún servidor.");
            printMenu();

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.LINE_START;
            add(panel);

            client = new Atention(ip, config.getProperty("atention.name"), new Message(null, null));

            setTitle("ATENCION " + config.getProperty("atention.name"));
            setSize(550, 880);
            setMinimumSize(new Dimension(550, 880));
            setMaximumSize(new Dimension(550, 880));
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listener() {
        new Thread(() -> {
            while (!socket.isClosed()) {
                try {
                    Object response = input.readObject();
                    if (response != null)
                        if (response.equals("UPDATE"))
                            if (!isAttending) {
                                updateTickets();
                            }
                } catch (Exception e) {
                    continue;
                }
            }
        }).start();
    }

    private void sendMessage(String message, Object object) {
        try {
            socket = new Socket(config.getProperty("control.server"),
                    Integer.parseInt(
                            config.getProperty("control.port").equals(port) ? config.getProperty("control.port")
                                    : port));
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
            if (input != null)
                input.close();
            if (output != null)
                output.close();
            if (socket != null || !socket.isClosed())
                socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
