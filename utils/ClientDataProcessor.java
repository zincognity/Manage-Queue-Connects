package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import types.Client;

public class ClientDataProcessor {
    public List<Client> loadClients(String filePath){
        List<Client> clients = new ArrayList<Client>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                String dniStr = values[0];
                String name = values[1];
                String lastname = values[2];
                String age_born = values[3];

                if(!dniStr.isEmpty() && !name.isEmpty() && !lastname.isEmpty() && !age_born.isEmpty()){
                    Client client = new Client(Integer.parseInt(dniStr), name, lastname, age_born);
                    clients.add(client);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clients;
    }
}
