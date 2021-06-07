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
    private ArrayList<String> roles;
    private HashMap<String,String> rolesAndUsernames;
    private int counterClient;

    public Server() {
        userNames         = new HashSet<String>();
        userThreads       = new HashSet<PlayerHandler>();
        roles             = new CreateRoles().getNameRoles();
        rolesAndUsernames = new HashMap<String, String>();
        counterClient     = 0;
    }

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
            //All players are connected. We start the game
            sendMessageToAll("Capacity was completed. The game started.");
            sendMessageToAll("finish");
            //Declaration of readiness.get answer from user.
            sendMessageToAll("Are you ready to start the game?.please type [ready]");
            ArrayList<String> answers = new ArrayList<String>();
            for (PlayerHandler handler : userThreads){
                String playerAnswer = handler.getMessageFromPlayer();
                answers.add(playerAnswer);
            }
            //Check Answers
            for (String playerAnswer : answers){
                if (!playerAnswer.equals("ready"))
                    System.out.println("wait.a palyer not ready");
            }
            //send role to player
            for (PlayerHandler handler : userThreads) {
                String role = getRandomRole();
                handler.sendMessage(role);
                rolesAndUsernames.put(role,handler.getUserName());
            }
            //start game.
            GameLoop gameLoop = new GameLoop(this, rolesAndUsernames);
            //start first night.
            gameLoop.firstNight();
            //finish first night
            sendMessageToAll("finish first night");
            //start night game
            gameLoop.nightGame();
            //finish nigh game
            sendMessageToAll("night finished");
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

    public String getMessageFromSpecifiecPlayer(String playerName){
        String message = "";
        for (PlayerHandler handler : userThreads){
            if (handler.getUserName().equals(playerName)){
                message = handler.getMessageFromPlayer();
                break;
            }
        }
        return message;
    }

    public static void main(String args[]) {
        Server server = new Server();
        server.execute();
    }
}

