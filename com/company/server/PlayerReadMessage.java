package com.company.server;
import java.io.*;
import java.net.*;

public class PlayerReadMessage extends Thread {
    private BufferedReader reader;
    private Socket socket;
    private PlayerMafia palyer;

    public PlayerReadMessage(Socket socket, PlayerMafia player) {
        this.socket = socket;
        this.palyer = player;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                String response = reader.readLine();
                System.out.println( response);
            } catch (IOException ex) {
                System.out.println("connected end");
                break;
            }
        }
    }
}
