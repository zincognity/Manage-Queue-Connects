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
import types.Tickets;

public class Server implements Runnable{

    private Socket socket;
    private JFrameC window;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    //private Queue queue;
    private static Map<String, List<ClientBase>> clients = new HashMap<String, List<ClientBase>>();
    
    public Server(Socket socket, JFrameC window) {
        this.socket = socket;
        //this.queue = queue;
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
            handleMessage(newClient.getType(), newClient.getMessage(), newClient);

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
            //if (newClient != null) {
            //    synchronized (clients) {
            //        clients.get(newClient.getType()).remove(newClient);
            //        System.out.println("Cliente de tipo '" + newClient.getType() + "' desconectado. Total en este tipo: " + clients.get(newClient.getType()).size());
            //        printClientCounts();
            //    }
            //}
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Error al cerrar el socket: " + e.getMessage());
            }
        }
    }

    private void handleMessage(String type, String message, ClientBase object) {
        switch (type) {
            case "ATENTION":
                switch (message) {
                    case "Initialize":
                        sendObject(window.addAtention((Atention) object));
                        break;
                    case "GetTickets":
                        sendObject(window.getTickets());
                        break;
                    case "Close":
                        sendMessage(Integer.toString(window.removeAtention((Atention) object)));
                        break;
                
                    default:
                        break;
                }
                break;
            case "MANAGE":
                switch (message) {
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
                int dni = 0;
                if (message.startsWith("GetDNI")) {
                    String[] parts = message.split("/");
                    message = "GetDNI";
                    if (parts.length > 1) {
                        dni = Integer.parseInt(parts[1]);
                    }
                }
                switch (message) {
                    case "Initialize":
                        sendObject(window.addTicket((Tickets) object));
                        break;
                    case "GetDNI":
                        System.out.println("PASO DNI");
                        sendObject(window.createTicket(dni, (Tickets) object));
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
