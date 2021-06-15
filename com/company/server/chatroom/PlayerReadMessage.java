package com.company.server.chatroom;

import com.company.characters.Role;
import com.company.server.PlayerMafia;

import java.io.*;
import java.net.*;

/**
 * this class inherit from [Thread] class.
 * this class use during chatroom.
 * get server message and prints to player.
 *
 * @author  mahmood-saneian
 * @since   2021-6-15
 * @version 15.0.2
 */
public class PlayerReadMessage extends Thread {
    //for get server message
    private BufferedReader reader;
    //socket of player
    private Socket socket;
    //player
    private PlayerMafia palyer;

    /**
     * this constructor get some information about [playerMafia] class and assigns to fields.
     * also open reader on the socket.
     * @param socket the socket that player connected to server with it.
     * @param player the player of game.
     */
    public PlayerReadMessage(Socket socket, PlayerMafia player) {
        this.socket = socket;
        this.palyer = player;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * this method from 5 minutes get
     * server message and prints to player.
     */
    public void run() {
        long start = System.currentTimeMillis();
        long end = start + 300 * 1000;

        while (System.currentTimeMillis() < end) {
            try {
                if (System.currentTimeMillis() >= end)
                    break;
                String response = reader.readLine();
                if (!response.equals("end#"))
                System.out.println("\n"+response );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
