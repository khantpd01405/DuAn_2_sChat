package com.object.contain.khanguyen.simchat;

import java.util.ArrayList;

/**
 * Created by kha on 19/11/2016.
 */

public class Globals {
    private static Globals instance;

    // Global variable
    private ArrayList<ArrayList<Messaging>> data;

    // Restrict the constructor from being instantiated
    private Globals(){}

    public void setData(ArrayList<ArrayList<Messaging>> d){
        this.data=d;
    }
    public ArrayList<ArrayList<Messaging>> getData(){
        return this.data;
    }

    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }
}
