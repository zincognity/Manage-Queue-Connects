package manage.model;

public class Atention {
    private String ip;
    private String name;
    private int index;

    public Atention(String ip, String name, int index){
        this.ip = ip;
        this.name = name;
        this.index = index;
    }

    public int getIndex(){
        return this.index;
    }

    public String getName(){
        return this.name;
    }
}
