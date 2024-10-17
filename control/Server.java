package control;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import control.model.Queue;

public class Server implements Runnable {

    private Socket socket;
    private JFrameC window;
    private DataInputStream input;
    private DataOutputStream output;
    private Queue queue;
    
    public Server(Socket socket, Queue queue, JFrameC window) {
        this.socket = socket;
        this.queue = queue;
        this.window = window;
    }
    
    @Override
    public void run() {
        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            String message;
            
            while((message = input.readUTF()) != null) {

                String[] messageParts = message.split("/");
                String command = messageParts[0];

                switch (command) {
                    case "NEW_ATENTION_PLATFORM":
                        try {
                            int res = window.addAtention(messageParts[1]);
                            sendMessage(Integer.toString(res));
                        } catch (Exception e) {
                            sendMessage("401");
                        } 
                        break;
                    case "GET_ATENTIONS_PLATFORM":
                        try {
                            String res = window.getAtentions();
                            sendMessage(res);
                        } catch (Exception e) {
                            sendMessage("401");
                        } 
                        break;
                    case "CLOSE_ATENTION_PLATFORM":
                        try {
                            window.deleteAtention(Integer.parseInt(messageParts[1]));
                            sendMessage("201");
                        } catch (Exception e) {
                            sendMessage("401");
                        }
                        break;
                    case "NEW_TICKET_PLATFORM":
                        try {
                            int res = window.addTicket(messageParts[1]);
                            sendMessage(Integer.toString(res));
                        } catch (Exception e) {
                            sendMessage("401");
                        }
                        break;
                    case "CLOSE_TICKET_PLATFORM":
                        try {
                            window.deleteTicket(Integer.parseInt(messageParts[1]));
                            sendMessage("201");
                        } catch (Exception e) {
                            sendMessage("401");
                        }
                        break;
                    case "GET_QUEUE":
                        try {
                            String res = window.getQueue();
                            sendMessage(res);
                        } catch (Exception e) {
                            sendMessage("401");
                        }
                        break;
                    case "DNI_REQUEST":
                        try {
                            String res = window.addClientToQueue(messageParts[1]);
                            sendMessage(res);
                        } catch (Exception e) {
                            sendMessage("401");
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
        }
    }

    public void sendMessage(String message) {
        try {
            if (output != null) {
                output.writeUTF(message);
                output.flush();
            }
        } catch (IOException e) {
            System.err.println("Error al enviar mensaje: " + e.getMessage());
        }
    }
}
