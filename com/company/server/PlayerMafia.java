package com.company.server;

import java.net.*;
import java.io.*;

public class PlayerMafia {
    private String Name;

    public PlayerMafia() {
    }

    public void execute() {
        try {
            Socket socket = new Socket("127.0.0.1", 6000);

            System.out.println("welcome. you entered the game");

            PlayerReadMessage readMessage   = new PlayerReadMessage(socket, this);
            PlayerWriteMessage writeMessage = new PlayerWriteMessage(socket, this);
            writeMessage.sendUserNameToServer();
            writeMessage.start();
            readMessage.start();
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
            ex.printStackTrace();
        }

    }

    void setName(String name) {
        this.Name = name;
    }

    String getName() {
        return this.Name;
    }


    public static void main(String args[]) {

        PlayerMafia playerMafia = new PlayerMafia();
        playerMafia.execute();
    }
}
