package com.company.server;

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class PlayerWriteMessage extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private PlayerMafia palyer;
    private Scanner scanner;

    public PlayerWriteMessage(Socket socket, PlayerMafia player) {
        this.socket = socket;
        this.palyer = player;
        scanner = new Scanner(System.in);

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {

        String text;

        System.out.println("so , you can send messages to other users. please write [bye] to quit");
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

    public void setUserName(String userName){
        writer.println(userName);
    }

}

