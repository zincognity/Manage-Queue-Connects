package control;

import java.net.Socket;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
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
    private static Map<String, ArrayList<ClientBase>> clients = new HashMap<String, ArrayList<ClientBase>>();

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
            this.output = new ObjectOutputStream(socket.getOutputStream());
            this.input = new ObjectInputStream(socket.getInputStream());
            handleClient(socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendObject(Object object) {
        try {
            this.output.writeObject(object);
            this.output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) throws ClassNotFoundException, IOException {
        ClientBase newClient = null;
        this.window.updateData();

        try {
            newClient = (ClientBase) input.readObject();
            System.out.println(newClient.getType() + " " + newClient.getIP() + " " + newClient.getMessage());
            handleMessage(newClient);

            synchronized (clients) {
                clients.putIfAbsent(newClient.getType(), new ArrayList<>());

                if (isUniqueClient(newClient)) {
                    newClient.setOutput(output);
                    clients.get(newClient.getType()).add(newClient);
                } else {
                    ClientBase tempNewClient = newClient;
                    clients.get(newClient.getType()).forEach(client -> {
                        if (client.getType().equals(tempNewClient.getType()) &&
                                client.getIP().equals(tempNewClient.getIP()) &&
                                client.getName().equals(tempNewClient.getName())) {
                            try {
                                client.getOutput().close();
                                client.setOutput(output);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                System.out.println("Cliente de tipo '" + newClient.getType() + "/" + newClient.getIP() + "/"
                        + newClient.getName() + "' conectado. Total en este tipo: "
                        + clients.get(newClient.getType()).size());
                printClientCounts();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean isUniqueClient(ClientBase newClient) {
        return clients.get(newClient.getType()).stream()
                .noneMatch(client -> client.getType().equals(newClient.getType()) &&
                        client.getIP().equals(newClient.getIP()) &&
                        client.getName().equals(newClient.getName()));
    }

    private void sendUpdateToMonitor() {
        synchronized (clients) {
            for (Map.Entry<String, ArrayList<ClientBase>> entry : clients.entrySet()) {
                if (entry.getKey().equals("MANAGE"))
                    for (ClientBase client : entry.getValue()) {
                        try {
                            client.getOutput().writeObject("UPDATE");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            }
        }
    }

    private void sendUpdateToAtention() {
        synchronized (clients) {
            for (Map.Entry<String, ArrayList<ClientBase>> entry : clients.entrySet()) {
                if (entry.getKey().equals("ATENTION"))
                    for (ClientBase client : entry.getValue()) {
                        try {
                            client.getOutput().writeObject("UPDATE");
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
                        sendObject(window.getAtentionManage().addAtention((Atention) object));
                        sendUpdateToMonitor();
                        break;
                    case "GetTickets":
                        sendObject(window.getTicketsManage().getTickets());
                        break;
                    case "ClaimTicket":
                        sendObject(window.getTicketsManage().claimTicket((Atention) object,
                                (Ticket) object.getMessage().getObject()));
                        sendUpdateToMonitor();
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
                        sendUpdateToAtention();
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
            for (Map.Entry<String, ArrayList<ClientBase>> entry : clients.entrySet()) {
                System.out.println("Tipo " + entry.getKey() + ": " + entry.getValue().size() + " conectado(s)");
            }
        }
    }
}
