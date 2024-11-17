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

public class Server implements Runnable {

    private Socket socket;
    private JFrameC window;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private static Map<String, List<ClientBase>> clients = new HashMap<String, List<ClientBase>>();
    private ArrayList<ObjectOutput> outputs = new ArrayList<ObjectOutput>();

    public Server(Socket socket, JFrameC window) {
        this.socket = socket;
        this.window = window;
    }

    public void serSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            outputs.add(output);
            input = new ObjectInputStream(socket.getInputStream());
            handleClient(socket);
        } catch (Exception e) {
            e.printStackTrace();
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

    private void handleClient(Socket clientSocket) throws ClassNotFoundException, IOException {
        ClientBase newClient = null;
        window.updateData();
        try {
            newClient = (ClientBase) input.readObject();
            System.out.println(newClient.getType() + newClient.getIP() + newClient.getMessage());
            handleMessage(newClient);

            synchronized (clients) {
                clients.putIfAbsent(newClient.getType() + "/" + newClient.getIP() + "/" + newClient.getName(),
                        new ArrayList<>());
                if (clients.get(newClient.getType() + "/" + newClient.getIP() + "/" + newClient.getName()).size() < 1) {
                    clients.get(newClient.getType() + "/" + newClient.getIP() + "/" + newClient.getName())
                            .add(newClient);
                }

                System.out.println("Cliente de tipo '" + newClient.getType() + "/" + newClient.getIP() + "/"
                        + newClient.getName() + "' conectado. Total en este tipo: " + clients
                                .get(newClient.getType() + "/" + newClient.getIP() + "/" + newClient.getName()).size());
                printClientCounts();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void asd() {
        if (outputs instanceof ArrayList<?>) {
            ArrayList<?> rawList = (ArrayList<?>) outputs;
            for (int i = 0; i < rawList.size(); i++) {
                if (rawList.get(i) instanceof Manage) {
                    try {
                        outputs.get(i).writeObject("res");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void handleMessage(ClientBase object) {
        switch ((String) object.getType()) {
            case "ATENTION":
                switch ((String) object.getMessage().getMessage()) {
                    case "Initialize":
                        asd();
                        sendObject(window.getAtentionManage().addAtention((Atention) object));
                        break;
                    case "GetTickets":
                        sendObject(window.getTicketsManage().getTickets());
                        break;
                    case "ClaimTicket":
                        sendObject(window.getTicketsManage().claimTicket((Atention) object,
                                (Ticket) object.getMessage().getObject()));
                        break;
                    case "Close":
                        sendObject(Integer.toString(window.getAtentionManage().removeAtention((Atention) object)));
                        break;

                    default:
                        break;
                }
                break;
            case "MANAGE":
                switch ((String) object.getMessage().getMessage()) {
                    case "Initialize":
                        sendObject(window.getManageManage().addManage((Manage) object));
                        break;
                    case "GetAtentions":
                        sendObject(window.getAtentionManage().getAtentionConnects());
                        break;
                    case "GetTickets":
                        sendObject(window.getTicketsManage().getAllTickets());
                        break;
                    case "Close":
                        sendObject(Integer.toString(window.getManageManage().removeManage((Manage) object)));
                        break;
                    default:
                        break;
                }
                break;
            case "TICKETS":
                switch ((String) object.getMessage().getMessage()) {
                    case "Initialize":
                        sendObject(window.getTicketManage().addTicket((Tickets) object));
                        break;
                    case "GetDNI":
                        sendObject(window.getTicketsManage().createTicket((int) object.getMessage().getObject(),
                                (Tickets) object, window.getQueueMax()));
                        break;
                    case "Close":
                        sendObject(window.getTicketManage().removeTicket((Tickets) object));
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
