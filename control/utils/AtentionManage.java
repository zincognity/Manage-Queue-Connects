package control.utils;

import types.Atention;
import java.util.ArrayList;

import control.views.ServerData;

public class AtentionManage {
    private ArrayList<Atention> atention_connects;
    private ServerData update;

    public AtentionManage(ServerData update) {
        this.atention_connects = new ArrayList<Atention>();
        this.update = update;
    }

    public ArrayList<Atention> getAtentionConnects() {
        ArrayList<Atention> serializableAtentions = new ArrayList<Atention>();

        for (Atention atention : atention_connects) {
            Atention clonedAtention = new Atention(atention);
            clonedAtention.setOutput(null);
            serializableAtentions.add(clonedAtention);
        }

        return serializableAtentions;
    }

    public int addAtention(Atention atention) {
        for (Atention atn : atention_connects) {
            if (atn.getIP().equals(atention.getIP()) && atn.getName().equals(atention.getName())) {
                return 0;
            }
        }
        atention_connects.add(atention);
        update.getLabelAtentions().setText(Integer.toString(atention_connects.size()));
        return 1;
    }

    public void removeAtention(Atention atention) {
        synchronized (atention_connects) {
            atention_connects.removeIf(currentAtention -> currentAtention.getIP().equals(atention.getIP())
                    && currentAtention.getName().equals(atention.getName()));
            update.getLabelAtentions().setText(Integer.toString(atention_connects.size()));
        }
    }
}
