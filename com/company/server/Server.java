package com.company.server;

import com.company.Game.CreateRoles;
import com.company.Game.GameLoop;
import com.company.Game.ManageData;
import com.company.characters.Role;
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
    private GameLoop gameLoop;

    public Server() {
        userNames         = new HashSet<String>();
        userThreads       = new ArrayList<PlayerHandler>();
        roles             = new CreateRoles().getNameRoles();
        rolesAndUsernames = new HashMap<String, String>();
        chatRoomHandlers  = new ArrayList<ChatRoomHandler>();
        gameLoop = new GameLoop(this);
        counterClient     = 0;
    }

    public void execute() {
        //Create server socket
        try (ServerSocket serverSocket = new ServerSocket(6000)) {

            System.out.println("game is start on port : 6000 \n" +
                    " Wait for all players to connect");

            //Accept players
            while (counterClient < 5) {
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
            System.out.println("All players join to the game.The game started");

            //Declaration of readiness.get answer from players.
            System.out.println("We ask all the players if they are ready to start the game or not");
            sendMessageToAll("Are you ready to start the game?.please type [ready]");
            ArrayList<String> answers = new ArrayList<String>();
            for (PlayerHandler handler : userThreads){
                String playerAnswer = handler.getMessageFromPlayer();
                answers.add(playerAnswer);
            }

            //send role to player
            System.out.println("Send role to each player");
            for (PlayerHandler handler : userThreads) {
                String role = getRandomRole();
                handler.sendMessage(role);
                rolesAndUsernames.put(role,handler.getUserName());
            }

            //Give roles and user names to [GameLoop] class
            gameLoop.setRolesAndUsernames(rolesAndUsernames);

            //start first night.
            System.out.println("The first night of the game started");
            gameLoop.firstNight();
            //finish first night
            System.out.println("End first night of game.Wait 8 seconds");
            Thread.sleep(8000);

            //Ask for exit
            System.out.println("Ask for exit");
            askForExit();
            if (userNames.size() == 0)
                System.exit(0);

            //start night game
            System.out.println("Start game night");
            gameLoop.nightGame();
            //finish nigh game
            System.out.println("End night of game.Wait 8 seconds");
            Thread.sleep(8000);

            //Update game
            gameLoop.updateGame();

            //Ask for exit
            System.out.println("Ask for exit");
            askForExit();
            if (userNames.size() == 0)
                System.exit(0);

            //start day
            System.out.println("Start game day");
            gameLoop.day();
            //finish day
            System.out.println("Finish game day.Wait 20 seconds");
            Thread.sleep(20000);
            sendMessageToAll("finish chat");

            //Ask for exit
            System.out.println("Ask for exit");
            askForExit();
            if (userNames.size() == 0)
                System.exit(0);

            //start voting
            System.out.println("Start voting");
            gameLoop.voting();
            //Finish voting
            System.out.println("Finish voting.Wait 8 seconds");
            Thread.sleep(8000);

        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        } catch (InterruptedException i) {
            i.printStackTrace();
        }
    }


    public void addUserName(String userName) {
        userNames.add(userName);
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

    public String getRandomRole() {
        Random random = new Random();
        String role = roles.get(random.nextInt(roles.size()));
        roles.remove(role);
        return role;
    }

    public void broadcast(String message, ChatRoomHandler handler){
        for (ChatRoomHandler roomHandler : chatRoomHandlers){
            if (roomHandler != handler){
                roomHandler.sendMessage(message);
            }
        }
    }

    public void broadcast(String message, PlayerHandler excludeUser) {
        for (PlayerHandler aUser : userThreads) {
            if (aUser != excludeUser)
                aUser.sendMessage(message);
        }
    }

    public void sendMessageToAll(String message) {
        for (PlayerHandler playerThread : userThreads) {
            playerThread.sendMessage(message);
        }
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

    public void chatRoom(){
        ArrayList<ChatRoomHandler> roomHandlers = new ArrayList<ChatRoomHandler>();

        for (PlayerHandler handler : userThreads){
            //Create an object from [ChatRoomHandler] class to player
            ChatRoomHandler chatRoomHandler = new ChatRoomHandler(this,handler.getSocket(),
                    handler.getUserName(),handler.getWriter(),handler.getReader());
            //Start thread of chat
            chatRoomHandler.start();
            //Add to list
            roomHandlers.add(chatRoomHandler);
        }
        setChatRoomHandlers(roomHandlers);
    }

    public void setChatRoomHandlers(ArrayList<ChatRoomHandler> chatRoomHandlers) {
        this.chatRoomHandlers = chatRoomHandlers;
    }

    public void storeMessages(){
        ArrayList<String> messages = new ArrayList<String>();
        for (ChatRoomHandler handler : chatRoomHandlers){
            ArrayList<String> tmp = handler.getMessages();
            for (String s : tmp){
                messages.add(s);
            }
        }
        try (FileWriter fileWriter = new FileWriter("messages.txt",true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)){
            for (String s : messages){
                s += " ";
                bufferedWriter.write(s);
            }
            System.out.println("Finish store messages in the file");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void askForExit(){
        ArrayList<String> removes = new ArrayList<String>();
        for (PlayerHandler handler : userThreads){
            handler.sendMessage("ASK");
            String answer = handler.getMessageFromPlayer();
            if (answer.equals("yes")){
                removes.add(handler.getUserName());
                gameLoop.removePlayer(handler.getUserName());
            }
        }

        for (String s : removes)
            removeUser(s);
    }

    public static void main(String args[]) {
        Server server = new Server();
        server.execute();
    }

}

