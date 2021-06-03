package com.company.server;

import java.io.*;
import java.net.*;

public class PlayerThread extends Thread {
    private Socket socket;
    private Server server;
    private PrintWriter writer;

    public PlayerThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            printUsers();

            String userName = reader.readLine();
            server.addUserName(userName);

            String serverMessage = "\n New player connected: " + userName;
            server.broadcast(serverMessage, this);

            String clientMessage;

            do {
                clientMessage = reader.readLine();
                serverMessage = clientMessage;
                server.broadcast(serverMessage, this);

            } while (!(clientMessage.endsWith("bye")));

            server.removeUser(userName, this);
            socket.close();

            serverMessage = userName + " has quitted.";
            server.broadcast(serverMessage, this);

        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    void printUsers() {
        if (server.hasUsers()) {
            writer.println("exist players: " + server.getUserNames());
        } else {
            writer.println("No other players connected");
        }
    }

    void sendMessage(String message) {
        writer.println(message);
    }
}