package control.model;

public class Client {
    private int dni;
    private String name;
    private String lastname;
    private String age_born;

    public Client (int dni, String name, String lastname, String age_born) {
        this.dni = dni;
        this.name = name;
        this.lastname = lastname;
        this.age_born = age_born;
    }

    public int getDNI(){
        return this.dni;
    }
}
