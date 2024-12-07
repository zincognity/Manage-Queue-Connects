package tickets;

import javax.swing.*;

import models.ClientBase;
import models.Message;
import tickets.utils.ButtonCustom;
import tickets.utils.Countdown;
import tickets.utils.LabelCustom;
import tickets.utils.TextFieldCustom;
import types.Ticket;
import types.Tickets;
import utils.ConfigLoader;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.net.InetAddress;

public class JFrameT extends JFrame {
    private static ConfigLoader config = new ConfigLoader("tickets/config/config.properties");
    private JTextField textFieldDNI;

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private JLabel labelMessage;
    private JLabel labelTicketDetail;
    private JLabel labelTimeDetail;

    private ClientBase client;

    public void initialize() {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            client = new Tickets(ip, config.getProperty("ticket.name"), new Message(null, null));
            sendMessage("Initialize", null);

            int res = (int) input.readObject();
            if (res == 0)
                return;

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            Font defaultFont = new Font("SansSerif", Font.BOLD, 14);

            JLabel labelTicket = new JLabel("TICKET");
            labelTicket.setFont(defaultFont);

            labelTicketDetail = new LabelCustom("", defaultFont);

            JLabel labelTime = new JLabel("TIEMPO");
            labelTime.setFont(defaultFont);

            labelTimeDetail = new LabelCustom("00:00:00", defaultFont);

            JLabel labelDNI = new JLabel("DNI");
            labelDNI.setFont(defaultFont);
            textFieldDNI = new TextFieldCustom(defaultFont);
            labelMessage = new JLabel();
            labelMessage.setFont(defaultFont);

            JButton button = new ButtonCustom("PEDIR TICKET", defaultFont);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onSubmit(textFieldDNI);
                    textFieldDNI.setText("");
                }
            });

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(labelTicket, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(labelTicketDetail, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(labelTime, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(labelTimeDetail, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(labelDNI, gbc);

            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(textFieldDNI, gbc);

            gbc.gridx = 0;
            gbc.gridy = 6;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(labelMessage, gbc);

            gbc.gridx = 0;
            gbc.gridy = 7;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(button, gbc);

            add(panel);

            setTitle("TICKETS " + config.getProperty("ticket.name"));
            setSize(680, 440);
            setMinimumSize(new Dimension(680, 440));
            setMaximumSize(new Dimension(680, 440));
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
            closeResources();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onSubmit(JTextField field) {
        Timer timer;
        if (field.getText().length() < 8) {
            labelMessage.setText("INGRESA UN DNI VÁLIDO");
            timer = new Timer(5000, e -> labelMessage.setText(""));
            timer.setRepeats(false);
            timer.start();
            return;
        }

        try {
            int dni = Integer.parseInt(field.getText());
            createTicket(dni);
        } catch (Exception e) {
            labelMessage.setText("INGRESA UN DNI VÁLIDO");
            timer = new Timer(5000, e2 -> labelMessage.setText(""));
            timer.setRepeats(false);
            timer.start();
        }
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
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTicket(int dni) {
        textFieldDNI.setEnabled(false);
        Timer timer;
        try {
            sendMessage("GetDNI", dni);
            Ticket res = (Ticket) input.readObject();
            if (res.getClient() != null) {
                labelTicketDetail.setText("#" + res.getName());
                new Countdown(labelTimeDetail, LocalDateTime.now());
                timer = new Timer(5000, e -> labelTicketDetail.setText(""));
                timer.setRepeats(false);
                timer.start();
                labelMessage.setText(
                        "BIENVENID@ " + res.getClient().getName() + ", PUEDES CREAR OTRO TICKET EN 5 MINUTOS. ");
                timer = new Timer(5000, e -> labelMessage.setText(""));
                timer.setRepeats(false);
                timer.start();
            } else {
                new Countdown(labelTimeDetail, res.getCreateAt());
                labelMessage.setText((String) res.getTickets().getMessage().getMessage());
                timer = new Timer(5000, e -> labelMessage.setText(""));
                timer.setRepeats(false);
                timer.start();
            }
            timer = new Timer(5000, e -> textFieldDNI.setEnabled(true));
            timer.setRepeats(false);
            timer.start();
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
            if (socket != null && !socket.isClosed())
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
