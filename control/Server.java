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

    @Override
    public void run() {
        try {
            this.output = new ObjectOutputStream(socket.getOutputStream());
            this.input = new ObjectInputStream(socket.getInputStream());
            handleClient();
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

    private void handleClient() throws ClassNotFoundException, IOException {
        ClientBase newClient = null;
        this.window.updateData();

        try {
            newClient = (ClientBase) input.readObject();
            System.out
                    .println(newClient.getType() + " " + newClient.getIP() + " " + newClient.getMessage().getMessage());
            handleMessage(newClient);

            clients.putIfAbsent(newClient.getType(), new ArrayList<ClientBase>());

            if (isUniqueClient(newClient)) {
                newClient.setOutput(this.output);
                System.out.println("Se ha establecido el output de " + newClient.getType()
                        + newClient.getName() + ":" + this.output + "verificando: " + newClient.getOutput());
                clients.get(newClient.getType()).add(newClient);
            } else {
                ClientBase tempNewClient = newClient;
                clients.get(newClient.getType()).forEach(client -> {
                    if (client.getType().equals(tempNewClient.getType()) &&
                            client.getIP().equals(tempNewClient.getIP()) &&
                            client.getName().equals(tempNewClient.getName())) {
                        try {
                            if (client.getOutput() != null) {
                                client.getOutput().close();
                                System.out.println("El output de " + client.getType() + client.getName() + " se cerró: "
                                        + client.getOutput());
                            }
                            client.setOutput(this.output);
                            System.out.println("Se ha establecido el nuevo output de " + client.getType()
                                    + client.getName() + ":" + output + "verificando: " + client.getOutput());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            for (String a : clients.keySet()) {
                clients.get(a).forEach(c -> {
                    System.out.println("VERIFICANDO " + a + c.getOutput());

                });
            }

            printClientCounts();
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

    private void sendMessageToClients(String name, String message) {
        if (clients.get(name) != null) {
            clients.get(name).forEach(client -> {
                System.out.println(client.getType() + client.getName() + client.getOutput());
                try {
                    client.getOutput().writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void handleMessage(ClientBase object) {
        switch ((String) object.getType()) {
            case "ATENTION":
                switch ((String) object.getMessage().getMessage()) {
                    case "Initialize":
                        sendObject(window.getAtentionManage().addAtention((Atention) object));
                        sendMessageToClients("MANAGE", "UPDATE");
                        break;
                    case "GetTickets":
                        sendObject(window.getTicketsManage().getTickets());
                        break;
                    case "ClaimTicket":
                        sendObject(window.getTicketsManage().claimTicket((Atention) object,
                                (Ticket) object.getMessage().getObject()));
                        window.getTicketsManage().getUpdate().getLabelQueueSize()
                                .setText(Integer.toString(window.getTicketsManage().getTickets().size()));
                        sendMessageToClients("MANAGE", "UPDATE");
                        sendMessageToClients("ATENTION", "UPDATE");
                        break;
                    case "Attended":
                        window.getTicketsManage().attendedTicket((Ticket) object.getMessage().getObject());
                        if (window.getTicketTableViewer() != null)
                            window.getTicketTableViewer().updateData(null);
                        sendMessageToClients("MANAGE", "UPDATE");
                        sendMessageToClients("ATENTION", "UPDATE");
                        break;
                    case "Close":
                        window.getAtentionManage().removeAtention((Atention) object);
                        sendMessageToClients("MANAGE", "UPDATE");
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
                        window.getTicketsManage().getUpdate().getLabelQueueSize()
                                .setText(Integer.toString(window.getTicketsManage().getTickets().size()));
                        sendMessageToClients("ATENTION", "UPDATE");
                        break;
                    case "Close":
                        window.getTicketManage().removeTicket((Tickets) object);
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
