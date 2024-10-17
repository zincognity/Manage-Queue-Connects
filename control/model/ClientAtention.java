package control.model;

public class ClientAtention extends Client {
    private int atentionIndex;

    public ClientAtention(int dni, String name, String lastname, String age_born, int atentionIndex) {
        super(dni, name, lastname, age_born);
        this.atentionIndex = atentionIndex;
    }
    
    public int getAtentionIndex(){
        return this.atentionIndex;
    }

}
