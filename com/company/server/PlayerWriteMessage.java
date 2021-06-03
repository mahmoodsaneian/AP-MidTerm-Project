package com.company.server;

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class PlayerWriteMessage extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private PlayerMafia palyer;
    private Scanner scanner;
    private String userName;

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
            System.out.print("[ "+userName+" ] : ");
            text = scanner.nextLine();
            String clientMessage = "[ " + userName + " ] :" + text;
            writer.println(clientMessage);
        } while (!(text.equals("bye")));

        try {
            socket.close();
        } catch (IOException ex) {

            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }

    public void sendUserNameToServer(){
        System.out.println("please enter username to join game");
        userName = scanner.nextLine();
        palyer.setName(userName);
        writer.println(userName);
    }
}

