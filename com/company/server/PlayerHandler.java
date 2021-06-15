package com.company.server;

import java.io.*;
import java.net.*;

/**
 * this class use from server side.
 * each player in the game has an object from this class.
 * this class can gets or sends message to player during game.
 *
 * @author  mahmood-saneian
 * @since   2021-6-15
 * @version 15.0.2
 */
public class PlayerHandler extends Thread {
    //The socket of player
    private Socket socket;
    //The server of game
    private Server server;
    //For send message
    private PrintWriter writer;
    //For get message
    private BufferedReader reader;
    //Name of player
    private String userName;

    /**
     * this constructor gets some information from [Server] class and assigns ti fields.
     * also open reader & writer on the socket.
     * @param socket the socket of player.
     * @param server the server of game.
     */
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

    /**
     * this method get username from player
     * and if new player connect to the game
     * notify to player.
     */
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

    /**
     * this method send exist players in the game to player.
     */
    void printUsers() {
        if (server.hasUsers()) {
            writer.println("\n exist players: " + server.getUserNames());
        } else {
            writer.println("\n No other players connected");
        }
    }

    /**
     * this method sends message to player.
     * @param message the message that we want to send it.
     */
    void sendMessage(String message) {
        writer.println(message);
    }

    /**
     * this method return name of player.
     * @return the name of player.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * this method get message from player.
     * @return the message.
     */
    public String getMessageFromPlayer(){
        String clientMessage = "";
        try {
            clientMessage = reader.readLine();
        }catch (IOException e){
            e.printStackTrace();
        }
        return clientMessage;
    }

    /**
     * this method return socket of player.
     * @return socket of player.
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * this method return writer that open on the  player's socket.
     * @return writer that open on the  player's socket.
     */
    public PrintWriter getWriter() {
        return writer;
    }

    /**
     * this method return reader that open on the  player's socket.
     * @return the reader that open on the  player's socket.
     */
    public BufferedReader getReader() {
        return reader;
    }
}