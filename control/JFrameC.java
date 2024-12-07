package control;

import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import javax.swing.*;

import control.utils.AtentionManage;
import control.utils.ManageManage;
import control.utils.TicketManage;
import control.utils.TicketsManage;
import control.views.MenuBar;
import control.views.ServerData;
import control.views.TicketTableViewer;
import utils.ClientDataProcessor;
import utils.ConfigLoader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import types.Client;
import java.io.*;

public class JFrameC extends JFrame {
    /* CONFIG */
    private JFrameC window = this;
    private static final String DATA_FILE_PATH = "control/data/clients.csv";
    private static ConfigLoader config = new ConfigLoader("control/config/config.properties");

    private int port = Integer.parseInt(config.getProperty("control.port"));
    private int queueMax = Integer.parseInt(config.getProperty("queue.size"));

    private String ip;

    private ServerSocket server;
    private boolean isRunning = false;

    private GridBagConstraints gbc = new GridBagConstraints();
    private ServerData serverData = new ServerData(gbc);
    private List<Client> clients = new ClientDataProcessor().loadClients(DATA_FILE_PATH);

    private AtentionManage atentions_connects = new AtentionManage(serverData);
    private TicketManage ticket_connects = new TicketManage(serverData);
    private ManageManage manage_connects = new ManageManage(serverData);
    private TicketsManage tickets = new TicketsManage(serverData, clients);

    private TicketTableViewer ticketTableViewer;
    private JPanel panel;
    protected JButton buttonUpdate;
    private MenuBar menuBar = new MenuBar();

    public List<Client> getClients() {
        return this.clients;
    }

    public TicketTableViewer getTicketTableViewer() {
        return this.ticketTableViewer;
    }

    public AtentionManage getAtentionManage() {
        return this.atentions_connects;
    }

    public TicketManage getTicketManage() {
        return this.ticket_connects;
    }

    public ManageManage getManageManage() {
        return this.manage_connects;
    }

    public TicketsManage getTicketsManage() {
        return this.tickets;
    }

    public int getQueueMax() {
        return this.queueMax;
    }

    private void printMenu() {
        menuBar.getConfigMenu().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showConfig();
            }
        });
        menuBar.getStartServer().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startServer();
            }
        });
        menuBar.getRestartServer().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restartServer();
            }
        });
        menuBar.getStopServer().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopServer();
            }
        });
        menuBar.getHistory().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ticketTableViewer = new TicketTableViewer(window);
            }
        });
        setJMenuBar(menuBar);
    }

    public void updateData() {
        serverData.getLabelIP().setText(ip);
        serverData.getLabelPort().setText(Integer.toString(port));
        serverData.getLabelQueueSizeMax().setText(Integer.toString(queueMax));
        if (panel != null)
            remove(panel);
        if (serverData != null)
            remove(serverData);
        add(serverData);
        revalidate();
        repaint();
    }

    private void showConfig() {
        String result;
        do {
            result = JOptionPane.showInputDialog("Ingresa el puerto del servidor:");
            try {
                port = Integer.parseInt(result);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor, ingresa un número entero válido.");
            }
        } while (port == -1);
    }

    private void startServer() {
        new Thread(() -> {
            try {
                if (server == null || server.isClosed()) {
                    server = new ServerSocket(port);
                    isRunning = true;
                    updateData();
                    menuBar.getStartServer().setEnabled(false);
                    menuBar.getRestartServer().setEnabled(true);
                    menuBar.getStopServer().setEnabled(true);

                    while (isRunning) {
                        try {
                            Socket socket = server.accept();
                            new Thread(new Server(socket, this)).start();
                        } catch (SocketException e) {
                            if (!isRunning) {
                                printMessage("El servidor no iniciado");
                            } else {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void restartServer() {
        new Thread(() -> {
            stopServer();
            try {
                Thread.sleep(3000);
                startServer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void stopServer() {
        if (server != null && !server.isClosed()) {
            try {
                server.close();
                menuBar.getStartServer().setEnabled(true);
                menuBar.getRestartServer().setEnabled(false);
                menuBar.getStopServer().setEnabled(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        isRunning = false;
    }

    private void printMessage(String message) {
        if (serverData != null)
            remove(serverData);
        if (panel != null)
            remove(panel);

        panel = new JPanel(new GridBagLayout());
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel labelIP = new JLabel(message);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(labelIP, gbc);

        add(panel);
        revalidate();
        repaint();
    }

    public void initialize() {
        try {
            window = this;
            panel = new JPanel(new GridBagLayout());
            gbc.insets = new Insets(10, 10, 10, 10);

            ip = InetAddress.getLocalHost().getHostAddress();
            menuBar.getStopServer().setEnabled(false);
            menuBar.getRestartServer().setEnabled(false);

            JLabel labelIP = new JLabel("El servidor no ha iniciado.");

            printMenu();

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.LINE_START;
            panel.add(labelIP, gbc);
            add(panel);

            setTitle("Control");
            setSize(550, 880);
            setMaximumSize(new Dimension(550, 880));
            setMinimumSize(new Dimension(550, 880));
            setLocationRelativeTo(null);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}