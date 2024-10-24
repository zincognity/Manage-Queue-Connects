package manage;

import javax.swing.*;

import types.Atention;
import types.AtentionAvaliable;
import types.Manage;
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

    private Manage client;

    @SuppressWarnings("unchecked")
    public ArrayList<AtentionAvaliable> getData(){
        try {
            sendMessage("GetQueue");
            ObjectInputStream objectInput= new ObjectInputStream(socket.getInputStream());
            ArrayList<AtentionAvaliable> atentions = (ArrayList<AtentionAvaliable>) objectInput.readObject();

            if(atentions.size() < 1){
                ArrayList<AtentionAvaliable> atention = new ArrayList<AtentionAvaliable>();
                atention.add(new AtentionAvaliable(new Atention("NO EXISTE", "NO EXISTE", "NO EXISTE")));
                return atention;
            }
            return atentions;
        } catch (Exception e) {
            ArrayList<AtentionAvaliable> atention = new ArrayList<AtentionAvaliable>();
            atention.add(new AtentionAvaliable(new Atention("ERROR", "ERROR", "ERROR")));
            return atention;
        }
    }

    public void initialize() {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            client = new Manage(ip, config.getProperty("manage.name"), null);
            sendMessage("Initialize");

            int res = (int) input.readObject();
            if(res == 0) return;

         /* PANEL */
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);

         /* LABELS AND FIELDS */

         /* BUTTONS */

         /* ACTIONS */

         /* ADDS */
            ArrayList<AtentionAvaliable> queue = getData();
            int index = 0;
            for (AtentionAvaliable string : queue) {
                labelMessage = new JLabel(string.getAtention().getName() + ":" + string.getTicket());
                gbc.gridx = 0;
                gbc.gridy = index;
                gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                panel.add(labelMessage, gbc);
                index++;
            }
            add(panel);

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

    private void sendMessage(String message) {
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
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
