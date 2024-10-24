package types;

import models.ClientBase;

public class Atention extends ClientBase {

    public Atention(String ip, String name, String message){
        super("ATENTION", ip, name, message);
    }
    
    @Override
    public void print(){
        System.out.println("");
    }
}
