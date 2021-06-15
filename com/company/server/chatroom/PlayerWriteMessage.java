package com.company.server.chatroom;

import com.company.server.PlayerMafia;
import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * this class inherit from [Thread] class.
 * this class use during chatroom.
 * get input from player and send to server.
 *
 * @author  mahmood-saneian
 * @since   2021-6-15
 * @version 15.0.2
 */
public class PlayerWriteMessage extends Thread {
    //for send message to server
    private PrintWriter writer;
    //socket of player
    private Socket socket;
    //player
    private PlayerMafia palyer;

    /**
     * this constructor get some information about [playerMafia] class and assigns to fields.
     * @param socket the socket that player connected to server with it.
     * @param player the player of game.
     * @param writer for send message to server
     */
    public PlayerWriteMessage(Socket socket, PlayerMafia player,PrintWriter writer) {
        this.socket = socket;
        this.palyer = player;
        this.writer = writer;
    }

    /**
     * this method for 5 minutes get input from
     * player and send it to server.
     */
    public void run() {
        Scanner scanner =  new Scanner(System.in);
        String text;
        System.out.println("Chat room opened");

        long start = System.currentTimeMillis();
        long end = start + 300 *1000;

        while (System.currentTimeMillis() < end) {
            System.out.print("[ you ] : ");
            text = scanner.nextLine();
            text = "[ " + palyer.getName() + " ] :" + text ;
            if (System.currentTimeMillis() >= end) {
                writer.println("end#");
                break;
            }
            writer.println(text);
        }
    }
}

