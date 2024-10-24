package control;

import java.net.Socket;

import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;
import java.util.List;

import java.io.*;

import models.ClientBase;
import types.Atention;
import types.Manage;
import types.Ticket;
import types.Tickets;

public class Server implements Runnable{

    private Socket socket;
    private JFrameC window;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private static Map<String, List<ClientBase>> clients = new HashMap<String, List<ClientBase>>();
    
    public Server(Socket socket, JFrameC window) {
        this.socket = socket;
        this.window = window;
    }

    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            handleClient(socket);
        } catch (Exception e) {
            e.printStackTrace();
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

    public void sendObject(Object object) {
        try {
            output.writeObject(object);
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket){
        ClientBase newClient = null;
        try {
            newClient = (ClientBase) input.readObject();
            System.out.println(newClient.getType() + newClient.getIP() + newClient.getMessage());
            handleMessage(newClient);

            synchronized (clients) {
                clients.putIfAbsent(newClient.getType() + "/" + newClient.getIP() + "/" + newClient.getName(), new ArrayList<>());
                if(clients.get(newClient.getType() + "/" + newClient.getIP() + "/" + newClient.getName()).size() < 1){
                    clients.get(newClient.getType() + "/" + newClient.getIP() + "/" + newClient.getName()).add(newClient);
                }
                
                System.out.println("Cliente de tipo '" + newClient.getType() + "/" + newClient.getIP() + "/" + newClient.getName() + "' conectado. Total en este tipo: " + clients.get(newClient.getType() + "/" + newClient.getIP() + "/" + newClient.getName()).size());
                printClientCounts();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al manejar cliente: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Error al cerrar el socket: " + e.getMessage());
            }
        }
    }

    private void handleMessage(ClientBase object) {
        switch ((String) object.getType()) {
            case "ATENTION":
                switch ((String) object.getMessage().getMessage()) {
                    case "Initialize":
                        sendObject(window.addAtention((Atention) object));
                        break;
                    case "GetTickets":
                        sendObject(window.getTickets());
                        break;
                    case "ClaimTicket":
                        sendObject(window.claimTicket((Atention) object, (Ticket) object.getMessage().getObject()));
                        break;
                    case "Close":
                        sendMessage(Integer.toString(window.removeAtention((Atention) object)));
                        break;
                
                    default:
                        break;
                }
                break;
            case "MANAGE":
                switch ((String) object.getMessage().getMessage()) {
                    case "Initialize":
                        sendObject(window.addManage((Manage) object));
                        break;
                    case "GetQueue":
                        //resObject = window.getAtentions();
                        //sendObject(resObject);
                        break;
                    case "Close":
                        sendMessage(Integer.toString(window.removeManage((Manage) object)));
                        break;
                    default:
                        break;
                }
                break;
            case "TICKETS":
                switch ((String) object.getMessage().getMessage()) {
                    case "Initialize":
                        sendObject(window.addTicket((Tickets) object));
                        break;
                    case "GetDNI":
                        sendObject(window.createTicket((int) object.getMessage().getObject(), (Tickets) object));
                        break;
                    case "Close":
                        sendObject(window.removeTicket((Tickets) object));
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    private static void printClientCounts() {
        System.out.println("Clientes conectados por tipo:");
        synchronized (clients) {
            for (Map.Entry<String, List<ClientBase>> entry : clients.entrySet()) {
                System.out.println("Tipo " + entry.getKey() + ": " + entry.getValue().size() + " conectado(s)");
            }
        }
    }
}
