package types;

import java.util.Objects;

import models.ClientBase;
import models.Message;

public class Atention extends ClientBase {
    public Atention(String ip, String name, Message message) {
        super("ATENTION", ip, name, message);
    }

    public Atention(Atention atention) {
        super(atention.getType(), atention.getIP(), atention.getName(), atention.getMessage());
    }

    @Override
    public String toString() {
        return "\nTIPO: " + this.type + "\nNOMBRE: " + this.name + "\nIP:" + this.ip;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Atention other = (Atention) obj;

        return Objects.equals(this.name, other.name) &&
                Objects.equals(this.ip, other.ip);
    }
}
