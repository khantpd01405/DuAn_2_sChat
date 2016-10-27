package com.object.contain.khanguyen.simchat;

/**
 * Created by kha on 26/10/2016.
 */

public class Room {
    private String roomName;
    private int numUser;

    public Room() {
    }

    public Room(String roomName) {
        this.roomName = roomName;
    }

    public Room(String roomName, int numUser) {
        this.roomName = roomName;
        this.numUser = numUser;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getNumUser() {
        return numUser;
    }

    public void setNumUser(int numUser) {
        this.numUser = numUser;
    }
}
