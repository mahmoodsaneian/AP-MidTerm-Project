package com.company.server;

import com.company.Game.CreateRoles;
import com.company.Game.GameLoop;
import com.company.Game.ManageData;
import com.company.server.chatroom.ChatRoomHandler;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private Set<String> userNames;
    private ArrayList<PlayerHandler> userThreads;
    private ArrayList<String> roles;
    private HashMap<String,String> rolesAndUsernames;
    private ArrayList<ChatRoomHandler> chatRoomHandlers;
    private int counterClient;

    public Server() {
        userNames         = new HashSet<String>();
        userThreads       = new ArrayList<PlayerHandler>();
        roles             = new CreateRoles().getNameRoles();
        rolesAndUsernames = new HashMap<String, String>();
        chatRoomHandlers  = new ArrayList<ChatRoomHandler>();
        counterClient     = 0;
    }

    public void execute() {
        //create server socket
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
            Thread.sleep(8000);
//            //start night game
//            sendMessageToAll("The night has started and the game server will move forward. " +
//                    "Wait until the night is completely over");
//            gameLoop.nightGame();
//            //finish nigh game
//            sendMessageToAll("night finished");
//            //start voting
//            sendMessageToAll("vote");
//            gameLoop.Voting();
//            //finish voting
//            sendMessageToAll("finish voting");
            //start day
            //start day
            sendMessageToAll("start of the day phase");
            chatRoom();
            Thread.sleep(300000);
        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        } catch (InterruptedException i) {
            i.printStackTrace();
        }
    }


    public void broadcast(String message, PlayerHandler excludeUser) {
        for (PlayerHandler aUser : userThreads) {
            if (aUser != excludeUser)
                aUser.sendMessage(message);
        }
    }

    //For chatroom
    public void broadcast(String message, ChatRoomHandler handler){
        for (ChatRoomHandler roomHandler : chatRoomHandlers){
            if (roomHandler != handler){
                roomHandler.sendMessage(message);
            }
        }
    }

    public void addUserName(String userName) {
        userNames.add(userName);
        ManageData.addUsername(userName);
    }

    public void removeUser(String userName) {
        //Find thread of player
        PlayerHandler handler = null;
        for (PlayerHandler playerHandler : userThreads){
            if (playerHandler.getUserName().equals(userName)){
                handler = playerHandler;
                break;
            }
        }
        //Remove user name
        boolean removed = userNames.remove(userName);
        //Remove thread of player
        if (removed) {
            userThreads.remove(handler);
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

    public void joinThread(String playerName) {
        for (PlayerHandler handler : userThreads) {
            if (handler.getUserName().equals(playerName)) {
                try {
                    handler.join();
                    break;
                } catch (InterruptedException i) {
                    i.printStackTrace();
                }
            }
        }
    }

    public void chatRoom(){
        ArrayList<ChatRoomHandler> roomHandlers = new ArrayList<ChatRoomHandler>();
        for (PlayerHandler handler : userThreads){
            ChatRoomHandler chatRoomHandler = new ChatRoomHandler(this,handler.getSocket(),
                    handler.getUserName(),handler.getWriter(),handler.getReader());
            chatRoomHandler.start();
            roomHandlers.add(chatRoomHandler);
        }
        setChatRoomHandlers(roomHandlers);
    }

    public void setChatRoomHandlers(ArrayList<ChatRoomHandler> chatRoomHandlers) {
        this.chatRoomHandlers = chatRoomHandlers;
    }

    public static void main(String args[]) {
        Server server = new Server();
        server.execute();
    }

}

