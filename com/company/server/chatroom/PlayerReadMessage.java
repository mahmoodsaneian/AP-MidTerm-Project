package com.company.server.chatroom;

import com.company.characters.Role;
import com.company.server.PlayerMafia;

import java.io.*;
import java.net.*;

public class PlayerReadMessage extends Thread {
    private BufferedReader reader;
    private Socket socket;
    private PlayerMafia palyer;

    public PlayerReadMessage(Socket socket, PlayerMafia player, BufferedReader reader) {
        this.socket = socket;
        this.palyer = player;
        this.reader = reader;
//        try {
//            InputStream input = socket.getInputStream();
//            reader = new BufferedReader(new InputStreamReader(input));
//        } catch (IOException ex) {
//            System.out.println("Error getting input stream: " + ex.getMessage());
//            ex.printStackTrace();
//        }
    }

    public void run() {
        long start = System.currentTimeMillis();
        long end = start + 300 * 1000;

        while (System.currentTimeMillis() < end) {
            try {
                if (System.currentTimeMillis() == end)
                    break;
                String response = reader.readLine();
                System.out.println(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
