package control.model;

public class Atention {
    private String ip;
    private String name;
    private int index;
    private String atending;

    public Atention(String ip, String name, int index, String atending){
        this.ip = ip;
        this.name = name;
        this.index = index;
        this.atending = "Disponible";
    }

    public int getIndex(){
        return this.index;
    }

    public String getName(){
        return this.name;
    }

    public String getAtending(){
        return this.atending;
    }

    public void setAtending(String atending){
        this.atending = atending;
    }
}
