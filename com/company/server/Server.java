package com.company.server;

import com.company.Game.CreateRoles;
import com.company.SharedData;
import com.company.characters.Role;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private Set<String> userNames;
    private Set<PlayerThread> userThreads;
    private int counterClient;

    /**
     *
     */
    public Server() {
        userNames = new HashSet<String>();
        userThreads = new HashSet<PlayerThread>();
        counterClient = 0;
    }

    /**
     *
     */
    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(6000)) {

            System.out.println("game is start on port : 6000 \n" +
                    " Wait for all players to connect");

            //create roles of game
            CreateRoles createRoles = new CreateRoles();
            ArrayList<Role> roles = createRoles.getRoles();

            //Accept players
            while (counterClient < 3) {
                Socket socket = serverSocket.accept();
                System.out.println("New player connected");

                counterClient++;
                System.out.println("number of player in game : "+counterClient);
                //create a thread for player
                PlayerThread newUser = new PlayerThread(socket, this);
                userThreads.add(newUser);
                newUser.start();
            }
            sendMessageToAll("let's to start game");
            Random random = new Random();
            for (PlayerThread playerThread : userThreads){
                Role role = roles.get(random.nextInt(roles.size()));
                roles.remove(role);
                playerThread.sendRoleToPlayer(role);
            }
        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     *
     * @param message
     * @param excludeUser
     */
    public void broadcast(String message, PlayerThread excludeUser) {
        for (PlayerThread aUser : userThreads) {
            if (aUser != excludeUser)
                aUser.sendMessage(message);
        }
    }

    /**
     *
     * @param userName
     */
    public void addUserName(String userName) {
        userNames.add(userName);
    }

    /**
     *
     * @param userName
     * @param aUser
     */
    public void removeUser(String userName, PlayerThread aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(aUser);
            System.out.println("The player " + userName + " quitted");
        }
    }

    /**
     *
     * @return
     */
    public Set<String> getUserNames() {
        return this.userNames;
    }

    /**
     *
     * @return
     */
    public boolean hasUsers() {
        return !this.userNames.isEmpty();
    }

    /**
     *
     * @param message
     */
    public void sendMessageToAll(String message){
        for (PlayerThread playerThread : userThreads){
            playerThread.sendMessage(message);
        }
    }


    public static void main(String args[]) {
        Server server = new Server();
        server.execute();
    }
}

