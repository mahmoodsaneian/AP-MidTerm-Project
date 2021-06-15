package com.company.server;

import com.company.Game.CreateRoles;
import com.company.Game.GameLoop;
import com.company.server.chatroom.ChatRoomHandler;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * this class is server of class & manages game & players.
 * All events in this game are managed in this class.
 * at first, creates a server socket and accepts players.
 * then , Asks players if they are ready to start the game.
 * then, send role to each player.
 * then , starts first nigh of the game .
 * then , Progresses the game according to the plan
 * and removes it from the game whenever a player wants to leave the game.
 * also checks the ending condition of the game after
 * each night and finishes the game if the condition is met.
 * The main brain of the game is this class.
 *
 * @author  mahmood-saneian
 * @since   2021-6-15
 * @version 15.0.2
 */
public class Server {
    //list of players names
    private Set<String> userNames;
    //list of players handlers
    private ArrayList<PlayerHandler> playerHandlers;
    //list of roles names
    private ArrayList<String> roles;
    //specifies each player has which role
    private HashMap<String, String> rolesAndUsernames;
    //list of chat room handlers
    private ArrayList<ChatRoomHandler> chatRoomHandlers;
    //number of players in the game
    private int counterPlayer;
    //an object from [Game loop] class
    private GameLoop gameLoop;

    /**
     * this constructor allocates memory for lists and hashmaps & game loop object
     * also initialize counter of player.
     */
    public Server() {
        userNames = new HashSet<String>();
        playerHandlers = new ArrayList<PlayerHandler>();
        roles = new CreateRoles().getNameRoles();
        rolesAndUsernames = new HashMap<String, String>();
        chatRoomHandlers = new ArrayList<ChatRoomHandler>();
        gameLoop = new GameLoop(this);
        counterPlayer = 0;
    }

    /**
     * this method , it's main method of this class.
     * create a server socket & accepts all players.
     * At each stage, it sends the appropriate message to
     * the player and continues the game until the game is over.
     * at each stage call suitable method from [Game loop] class.
     */
    public void execute() {
        //Create server socket
        try (ServerSocket serverSocket = new ServerSocket(6000)) {

            System.out.println("game is start on port : 6000 \n" +
                    " Wait for all players to connect");

            //Accept players
            while (counterPlayer < 7) {
                Socket socket = serverSocket.accept();
                System.out.println("New player connected");

                counterPlayer++;
                System.out.println("number of player in game : " + counterPlayer);

                //create a thread for player
                PlayerHandler newUser = new PlayerHandler(socket, this);
                playerHandlers.add(newUser);
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
            for (PlayerHandler handler : playerHandlers) {
                String playerAnswer = handler.getMessageFromPlayer();
                answers.add(playerAnswer);
            }

            //send role to player
            System.out.println("Send role to each player");
            for (PlayerHandler handler : playerHandlers) {
                String role = getRandomRole();
                handler.sendMessage(role);
                rolesAndUsernames.put(role, handler.getUserName());
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

            //start day
            System.out.println("Start game day");
            gameLoop.day();
            //finish day
            System.out.println("Finish game day.Wait 1 minute");
            Thread.sleep(60000);
            sendMessageToAll("finish chat");

            //Ask for exit
            System.out.println("Ask for exit");
            askForExit();
            if (userNames.size() == 0)
                System.exit(0);

            //Game loop
            while (true) {
                //start night game
                System.out.println("Start game night");
                gameLoop.nightGame();
                //finish nigh game
                System.out.println("End night of game.Wait 8 seconds");
                Thread.sleep(8000);

                //Update game
                gameLoop.updateGame();

                //Check end condition
                boolean end = gameLoop.checkEndCondition();
                if (end == true) {
                    sendMessageToAll("finish game");
                    System.exit(0);
                } else
                    sendMessageToAll("don't finish game");

                //Ask for exit
                System.out.println("Ask for exit");
                askForExit();
                if (userNames.size() == 0)
                    System.exit(0);

                //start day
                System.out.println("Start game day");
                gameLoop.day();
                //finish day
                System.out.println("Finish game day.Wait 1 minute");
                Thread.sleep(60000);
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
            }
        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        } catch (InterruptedException i) {
            i.printStackTrace();
        }
    }

    /**
     * this method add user name to list of usernames
     * @param userName the new username
     */
    public void addUserName(String userName) {
        userNames.add(userName);
    }

    /**
     * this method remove a player from game.
     * remove it's name & it's player handler.
     * @param userName
     */
    public void removeUser(String userName) {
        //Find thread of player
        PlayerHandler handler = null;
        for (PlayerHandler playerHandler : playerHandlers) {
            if (playerHandler.getUserName().equals(userName)) {
                handler = playerHandler;
                break;
            }
        }
        //Remove user name
        boolean removed = userNames.remove(userName);
        //Remove thread of player
        if (removed) {
            playerHandlers.remove(handler);
        }
    }

    /**
     * this method return set of players names.
     * @return players names.
     */
    public Set<String> getUserNames() {
        return this.userNames;
    }

    /**
     * this method return true[if no player exists in the game]
     * or false [if a player exists in the game]
     * @return true or false.
     */
    public boolean hasUsers() {
        return !this.userNames.isEmpty();
    }

    /**
     * this method return random role's name.
     * @return random role's name
     */
    public String getRandomRole() {
        Random random = new Random();
        String role = roles.get(random.nextInt(roles.size()));
        roles.remove(role);
        return role;
    }

    /**
     * this method use during chatroom.
     * when a player send message in the chat room ,
     * server sends this message to other players.
     * @param message the player message.
     * @param handler the player that send message.
     */
    public void broadcast(String message, ChatRoomHandler handler) {
        for (ChatRoomHandler roomHandler : chatRoomHandlers) {
            if (roomHandler != handler) {
                roomHandler.sendMessage(message);
            }
        }
    }

    /**
     * this method use at first of game.when a new player
     * joined to the game server notify other exist players/
     * @param message the message of server.
     * @param excludeUser the new player object
     */
    public void broadcast(String message, PlayerHandler excludeUser) {
        for (PlayerHandler aUser : playerHandlers) {
            if (aUser != excludeUser)
                aUser.sendMessage(message);
        }
    }

    /**
     * this method sends a message to all players.
     * @param message the our message.
     */
    public void sendMessageToAll(String message) {
        for (PlayerHandler playerThread : playerHandlers) {
            playerThread.sendMessage(message);
        }
    }

    /**
     * this method sends message to specifiec player.
     * @param playerName the player who we want send message to it.
     * @param message the our message.
     */
    public void sendMessageToSpecifiecPlayer(String playerName, String message) {
        for (PlayerHandler playerHandler : playerHandlers) {
            if (playerHandler.getUserName().equals(playerName)) {
                playerHandler.sendMessage(message);
                break;
            }
        }
    }

    /**
     * this method gets message from specifiec player.
     * @param playerName the name of player who we want get message from it.
     * @return the message of player
     */
    public String getMessageFromSpecifiecPlayer(String playerName) {
        String message = "";
        for (PlayerHandler handler : playerHandlers) {
            if (handler.getUserName().equals(playerName)) {
                message = handler.getMessageFromPlayer();
                break;
            }
        }
        return message;
    }

    /**
     * this method create an object for
     * each player from [Chat room handler] class.
     * use during chatroom.
     */
    public void chatRoom() {
        ArrayList<ChatRoomHandler> roomHandlers = new ArrayList<ChatRoomHandler>();

        for (PlayerHandler handler : playerHandlers) {
            //Create an object from [ChatRoomHandler] class to player
            ChatRoomHandler chatRoomHandler = new ChatRoomHandler(this, handler.getSocket(),
                    handler.getUserName(), handler.getWriter(), handler.getReader());
            //Start thread of chat
            chatRoomHandler.start();
            //Add to list
            roomHandlers.add(chatRoomHandler);
        }
        setChatRoomHandlers(roomHandlers);
    }

    /**
     * this method get list of chat room handlers & send to field.
     * @param chatRoomHandlers list of chat room handlers.
     */
    public void setChatRoomHandlers(ArrayList<ChatRoomHandler> chatRoomHandlers) {
        this.chatRoomHandlers = chatRoomHandlers;
    }

    /**
     * this method after chat room store all messages
     * in the file.
     */
    public void storeMessages() {
        ArrayList<String> messages = new ArrayList<String>();
        for (ChatRoomHandler handler : chatRoomHandlers) {
            ArrayList<String> tmp = handler.getMessages();
            for (String s : tmp) {
                messages.add(s);
            }
        }
        try (FileWriter fileWriter = new FileWriter("messages.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            for (String s : messages) {
                s += " ";
                bufferedWriter.write(s);
            }
            System.out.println("Finish store messages in the file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method ask for all players that
     * they want leave from game or no.
     * if each player wants leave from game
     * server remove ir from list of names & player handlers.
     */
    public void askForExit() {
        ArrayList<String> removes = new ArrayList<String>();
        for (PlayerHandler handler : playerHandlers) {
            handler.sendMessage("ASK");
            String answer = handler.getMessageFromPlayer();
            if (answer.equals("yes")) {
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

