package tickets;

import javax.swing.*;

import models.ClientBase;
import models.Message;
import types.Ticket;
import types.Tickets;
import utils.ConfigLoader;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.net.InetAddress;

public class JFrameT extends JFrame {
    private static ConfigLoader config = new ConfigLoader("tickets/config/config.properties");
    private final JTextField dni = new JTextField(8);

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private JLabel labelMessage;

    private ClientBase client;

    public void initialize() {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            client = new Tickets(ip, config.getProperty("ticket.name"), new Message(null, null));
            sendMessage("Initialize", null);

            int res = (int) input.readObject();
            if(res == 0) return;

            /* PANEL */
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);

            /* LABELS AND FIELDS */
            JLabel label = new JLabel("DNI:");
            dni.setPreferredSize(new Dimension(200, 30));
            labelMessage = new JLabel();

            /* BUTTONS */
            JButton button = new JButton("Pedir Ticket");

            /* ACTIONS */
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onSubmit(dni);
                }
            });

            /* ADDS */
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.LINE_END;
            panel.add(label, gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.LINE_START;
            panel.add(dni, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(button, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(labelMessage, gbc);

            add(panel);

            /* CONFIGS */
            setTitle("TICKETS " + config.getProperty("ticket.name"));
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
                }
            });
            setVisible(true);
        } catch (EOFException e) {
            System.err.println("Server closed the connection unexpectedly.");
            closeResources();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onSubmit(JTextField field) {
        if(field.getText().length() < 8){
            labelMessage.setText("Ingresa un DNI válido");
            return;
        }

        try {
            createTicket(field.getText());
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
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTicket(String dni) {
        try {
            sendMessage("GetDNI", Integer.parseInt(dni));
            Ticket res = (Ticket) input.readObject();
            if(res.getClient() != null) {
                labelMessage.setText("BIENVENID@ " + res.getClient().getName() + " TU TICKET ES " + res.getName());
            } else {
                labelMessage.setText((String) res.getTickets().getMessage().getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeResources() {
        try {
            if (output != null) output.close();
            if (input != null) input.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
