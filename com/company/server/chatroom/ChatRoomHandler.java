package com.company.server.chatroom;

import com.company.server.Server;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * this class use during chatroom.
 * get message from player and
 * sends to other players.
 *
 * @author  mahmood-saneian
 * @since   2021-6-15
 * @version 15.0.2
 */
public class ChatRoomHandler extends Thread {
    //The server of game
    private Server server;
    //The socket of player
    private Socket socket;
    //the name of player
    private String username;
    //For send message to players
    private PrintWriter writer;
    //For get message from player
    private BufferedReader reader;
    //Messages that player send during chatroom
    private ArrayList<String> messages;

    /**
     * this constructor get some information from [Server] class and assigns to fields.
     * also allocates memory to message's list.
     * @param server the server of game.
     * @param socket the socket that player connected to server with it.
     * @param username the name of player.
     * @param writer for send message to player.
     * @param reader for get message from player.
     */
    public ChatRoomHandler(Server server, Socket socket, String username,
                           PrintWriter writer, BufferedReader reader) {
        this.server = server;
        this.socket = socket;
        this.username = username;
        this.writer = writer;
        this.reader = reader;
        messages = new ArrayList<String>();
    }

    /**
     * this method for 5 minutes get message
     * from player and server sends this
     * message to other players.
     */
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

    /**
     * this method sends message to other players
     * @param message the message that we want send it.
     */
    public void sendMessage(String message) {
        writer.println(message);
    }

    /**
     * this method return all messages that player send during chatroom.
     * @return all player's message.
     */
    public ArrayList<String> getMessages() {
        return messages;
    }
}
