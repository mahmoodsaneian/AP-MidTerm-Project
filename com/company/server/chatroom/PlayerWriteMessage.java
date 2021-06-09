package com.company.server.chatroom;

import com.company.server.PlayerMafia;

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class PlayerWriteMessage extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private PlayerMafia palyer;


    public PlayerWriteMessage(Socket socket, PlayerMafia player,PrintWriter writer) {
        this.socket = socket;
        this.palyer = player;
        this.writer = writer;
//        try {
//            OutputStream output = socket.getOutputStream();
//            writer = new PrintWriter(output, true);
//        } catch (IOException ex) {
//            System.out.println("Error getting output stream: " + ex.getMessage());
//            ex.printStackTrace();
//        }
    }

    public void run() {
        Scanner scanner =  new Scanner(System.in);
        String text;
        System.out.println("Chat room opened");

        long start = System.currentTimeMillis();
        long end = start + 300 *1000;

        while (System.currentTimeMillis() < end) {
            if (System.currentTimeMillis() == end)
                break;
            System.out.print("[ you ] : ");
            text = scanner.nextLine();
            text = "[ " + palyer.getName() + " ] :" + text;
            writer.println(text);
        }
    }
}

