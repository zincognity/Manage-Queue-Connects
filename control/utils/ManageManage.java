package control.utils;

import types.Manage;
import java.util.ArrayList;

import control.views.ServerData;

public class ManageManage {
    private ArrayList<Manage> manage_connects;
    private ServerData update;

    public ManageManage(ServerData update) {
        manage_connects = new ArrayList<Manage>();
        this.update = update;
    }

    public int addManage(Manage manage) {
        for (Manage mng : manage_connects) {
            if(mng.getIP().equals(manage.getIP()) && mng.getName().equals(manage.getName())) {
                return 0;
            }
        }
        manage_connects.add(manage);
        update.getLabelManages().setText(Integer.toString(manage_connects.size()));
        return 1;
    }

    public int removeManage(Manage manage) {
        for (Manage mng : manage_connects) {
            if(mng.getIP().equals(manage.getIP()) && mng.getName().equals(manage.getName())) {
                manage_connects.remove(mng);
                update.getLabelManages().setText(Integer.toString(manage_connects.size()));
            }
        }
        return manage_connects.size();
    }
}
