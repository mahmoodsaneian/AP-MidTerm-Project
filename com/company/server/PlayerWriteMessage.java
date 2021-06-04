package com.company.server;

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class PlayerWriteMessage extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private PlayerMafia palyer;


    public PlayerWriteMessage(Socket socket, PlayerMafia player) {
        this.socket = socket;
        this.palyer = player;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        //send username to server
        writer.println(palyer.getName());
        System.out.println("Wait for all the players to express themselves and then start the game");
    }


    public void chatRoom(){
        Scanner scanner =  new Scanner(System.in);

        String text;

        do {
            System.out.print("[ "+palyer.getName()+" ] : ");
            text = scanner.nextLine();
            String clientMessage = "[ " + palyer.getName() + " ] :" + text;
            writer.println(clientMessage);
        } while (!(text.equals("bye")));

        try {
            socket.close();
        } catch (IOException ex) {

            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
}

