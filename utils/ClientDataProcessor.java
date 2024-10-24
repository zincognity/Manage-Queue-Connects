package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientDataProcessor {
    public List<types.Client> loadClients(String filePath){
        List<types.Client> clients = new ArrayList<types.Client>();
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
                    types.Client client = new types.Client(Integer.parseInt(dniStr), name, lastname, age_born);
                    clients.add(client);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clients;
    }
}
