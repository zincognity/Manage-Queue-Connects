package types;

import models.ClientBase;
import models.Message;

public class Atention extends ClientBase {
    public Atention(String ip, String name, Message message){
        super("ATENTION", ip, name, message);
    }

    @Override
    public void print(){
        //asd.updateTickets();
    }
}
