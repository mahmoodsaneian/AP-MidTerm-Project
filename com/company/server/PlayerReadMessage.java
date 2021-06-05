package com.company.server;

import com.company.characters.Role;

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
       try {
           while (true) {
               String response = reader.readLine();
               if (response.equals("let's to start game")) {
                   System.out.println("Capacity was completed. The game started");
                   break;
               }
               System.out.println(response);
           }
       }catch (IOException e){
           e.printStackTrace();
       }
       getRoleFromServer();
    }

    public void chatRoom(String message) {
        System.out.println(message);
    }

    public void getRoleFromServer() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Role role = (Role) objectInputStream.readObject();
            palyer.setRole(role);
            System.out.println(palyer.getRole().getRoleDescription());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
    }
}
