package com.company.server.chatroom;

import com.company.server.Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ChatRoomHandler extends Thread {
    private Server server;
    private Socket socket;
    private String username;
    private PrintWriter writer;
    private BufferedReader reader;
    private ArrayList<String> messages;

    public ChatRoomHandler(Server server, Socket socket, String username,
                           PrintWriter writer, BufferedReader reader) {
        this.server = server;
        this.socket = socket;
        this.username = username;
        this.writer = writer;
        this.reader = reader;
        messages = new ArrayList<String>();
    }

    @Override
    public void run() {
        try {

            long start = System.currentTimeMillis();
            long end = start + 300 * 1000;

            while (System.currentTimeMillis() < end) {
                if (System.currentTimeMillis() >= end)
                    break;
                String clientMessage = reader.readLine();
                server.broadcast(clientMessage, this);
                messages.add(clientMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }
}
