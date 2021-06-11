package com.company.server.chatroom;

import com.company.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatRoomHandler extends Thread{
    private Server server;
    private Socket socket;
    private String username;
    private PrintWriter writer;
    private BufferedReader reader;

    public ChatRoomHandler(Server server, Socket socket, String username,
                           PrintWriter writer,BufferedReader reader) {
        this.server = server;
        this.socket = socket;
        this.username = username;
        this.writer = writer;
        this.reader = reader;
    }

    @Override
    public void run() {
        try {

            long start = System.currentTimeMillis();
            long end = start + 300 * 1000;

            while (System.currentTimeMillis() < end){
                if (System.currentTimeMillis() == end)
                    break;
                String clientMessage = reader.readLine();
                server.broadcast(clientMessage, this);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        writer.println(message);
    }

    public String getUsername() {
        return username;
    }
}
