package com.company.server;

import com.company.characters.Role;

import java.io.*;
import java.net.*;

public class PlayerHandler extends Thread {
    private Socket socket;
    private Server server;
    private PrintWriter writer;
    private BufferedReader reader;
    private String userName;

    public PlayerHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            printUsers();

            userName = reader.readLine();
            server.addUserName(userName);

            String serverMessage = "\n New player connected: " + userName;
            server.broadcast(serverMessage, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void printUsers() {
        if (server.hasUsers()) {
            writer.println("\n exist players: " + server.getUserNames());
        } else {
            writer.println("\n No other players connected");
        }
    }

    void sendMessage(String message) {
        writer.println(message);
    }

    public void chatRoom() {
        try {
            String serverMessage;
            String clientMessage;
            do {
                clientMessage = reader.readLine();
                serverMessage = clientMessage;
                server.broadcast(serverMessage, this);
            } while (!(clientMessage.endsWith("bye")));
        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public String getUserName() {
        return userName;
    }
}