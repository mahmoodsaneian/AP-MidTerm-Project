package com.company.server;

import com.company.Game.CreateRoles;
import com.company.Game.GameLoop;
import com.company.Game.ManageData;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private Set<String> userNames;
    private Set<PlayerHandler> userThreads;
    private int counterClient;
    private ArrayList<String> roles;
    private HashMap<String,String> game;

    /**
     *
     */
    public Server() {
        userNames = new HashSet<String>();
        userThreads = new HashSet<PlayerHandler>();
        counterClient = 0;
        roles = new CreateRoles().getNameRoles();
        game = new HashMap<String, String>();
    }

    /**
     *
     */
    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(6000)) {

            System.out.println("game is start on port : 6000 \n" +
                    " Wait for all players to connect");

            //Accept players
            while (counterClient < 3) {
                Socket socket = serverSocket.accept();
                System.out.println("New player connected");

                counterClient++;
                System.out.println("number of player in game : " + counterClient);

                //create a thread for player
                PlayerHandler newUser = new PlayerHandler(socket, this);
                userThreads.add(newUser);
                newUser.start();
                newUser.join();
            }
            sendMessageToAll("Capacity was completed. The game started.");
            sendMessageToAll("finish");
            //send role to player
            for (PlayerHandler handler : userThreads) {
                String role = getRandomRole();
                handler.sendMessage(role);
                game.put(role,handler.getUserName());
            }
            GameLoop gameLoop = new GameLoop(this, game);
//            gameLoop.print();
            gameLoop.firstNight();
            sendMessageToAll("finish first night");
        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        } catch (InterruptedException i) {
            i.printStackTrace();
        }
    }

    //For chatroom
    public void broadcast(String message, PlayerHandler excludeUser) {
        for (PlayerHandler aUser : userThreads) {
            if (aUser != excludeUser)
                aUser.sendMessage(message);
        }
    }

    public void addUserName(String userName) {
        userNames.add(userName);
        ManageData.addUsername(userName);
    }

    public void removeUser(String userName, PlayerHandler aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(aUser);
            System.out.println("The player " + userName + " quitted");
        }
    }

    public Set<String> getUserNames() {
        return this.userNames;
    }

    public boolean hasUsers() {
        return !this.userNames.isEmpty();
    }

    public void sendMessageToAll(String message) {
        for (PlayerHandler playerThread : userThreads) {
            playerThread.sendMessage(message);
        }
    }

    public String getRandomRole() {
        Random random = new Random();
        String role = roles.get(random.nextInt(roles.size()));
        roles.remove(role);
        return role;
    }


    public void sendMessageToSpecifiecPlayer(String playerName, String message){
        for (PlayerHandler playerHandler : userThreads){
            if (playerHandler.getUserName().equals(playerName)){
                playerHandler.sendMessage(message);
                break;
            }
        }
    }

    public static void main(String args[]) {
        Server server = new Server();
        server.execute();
    }
}

