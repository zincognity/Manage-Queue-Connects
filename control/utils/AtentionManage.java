package control.utils;

import types.Atention;
import java.util.ArrayList;

import control.views.ServerData;

public class AtentionManage {
    private ArrayList<Atention> atention_connects;
    private ServerData update;

    public AtentionManage(ServerData update) {
        atention_connects = new ArrayList<Atention>();
        this.update = update;
    }

    public ArrayList<Atention> getAtentionConnects() {
        return this.atention_connects;
    }

    public int addAtention(Atention atention){
        for (Atention atn : atention_connects) {
            if(atn.getIP().equals(atention.getIP()) && atn.getName().equals(atention.getName())) {
                return 0;
            }
        }
        atention_connects.add(atention);
        update.getLabelAtentions().setText(Integer.toString(atention_connects.size()));
        return 1;
    }

    public int removeAtention(Atention atention){
        for (Atention atn : atention_connects) {
            if(atn.getIP().equals(atention.getIP()) && atn.getName().equals(atention.getName())) {
                atention_connects.remove(atn);
                update.getLabelAtentions().setText(Integer.toString(atention_connects.size()));
            }
        }
        return atention_connects.size();
    }
}
