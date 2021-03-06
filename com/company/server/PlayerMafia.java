package com.company.server;

import com.company.Game.ManageData;
import com.company.Game.NightGame;
import com.company.characters.DieHard;
import com.company.characters.Role;
import com.company.server.chatroom.PlayerReadMessage;
import com.company.server.chatroom.PlayerWriteMessage;
import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 * This class is the most important game class after the server class.
 * get a port & valid username from player & connecto to the server.
 * It works according to the messages it receives from the server.
 * also include some information about a player
 * such as name of player, role of player & ....
 * also when player wants to see previous messages ,
 * show previous messages to player.
 *
 *
 * @author  mahmood-saneian
 * @since   2021-6-15
 * @version 15.0.2
 */
public class PlayerMafia {
    //The socket of player
    private Socket socket;
    //The name of player
    private String name;
    //The port of game
    private int port;
    //The role of player
    private Role role;
    //For read message from server
    private BufferedReader reader;
    //For write message to server
    private PrintWriter writer;

    /**
     * this constructor get name of player & port of game then set to fields.
     * @param port the port of game.
     * @param name the name of player.
     */
    public PlayerMafia(int port, String name) {
        this.port = port;
        this.name = name;
    }

    /**
     * This method is the main method of this class.
     * Manages the game on the player side
     * It also behaves according to the messages received from the server.
     * If the player dies during the game, the program closes.
     */
    public void execute() {
        try {
            //connect to the server
            socket = new Socket("127.0.0.1", port);

            System.out.println("welcome. you entered the game.\n" +
                    "Wait for all the players to express themselves and then start the game");

            //open reader & writer on the socket
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            //create an object from "Scanner" class to get input from player
            Scanner scanner = new Scanner(System.in);

            //this string ,it is message that server send to player.
            String serverMessage = "";

            //send username to server
            writer.println(name);

            //before start game.The player will be notified if a new player enters the game.
            while (!serverMessage.equals("finish")) {
                serverMessage = reader.readLine();
                if (serverMessage.equals("finish"))
                    break;
                System.out.println(serverMessage);
            }

            //All players connected to the game.
            //We ask the player if s/he is ready to start the game or not?
            serverMessage = reader.readLine();
            String answer = "";
            while (!answer.equals("ready")) {
                System.out.println(serverMessage);
                answer = scanner.nextLine();
                if (answer.equals("ready"))
                    break;
            }
            writer.println(answer);

            //get role from server
            String role1 = reader.readLine();
            role = ManageData.getRole(role1);

            //first night of game started & print role description.
            System.out.println("the first night of the game started\n" +
                    "your role in the game : " + role.getName() + "\n" +
                    "your role description : " + role.getRoleDescription());

            while (true) {
                serverMessage = reader.readLine();
                if (serverMessage.equals("finish first night")) {
                    System.out.println("The first night of the game is over.wait 8 seconds");
                    Thread.sleep(8000);
                    break;
                }
                System.out.println(serverMessage);
            }
            //end first night

            //Ask for [Exit]
            System.out.println("Ask for exit");
            askForExit();

            //start day
            long start = System.currentTimeMillis();
            long end = start + 300 * 1000;

            while (true) {
                //If the time is up
                if (System.currentTimeMillis() >= end)
                    break;
                //Get message from server
                serverMessage = reader.readLine();
                //If server say : start chatroom
                if (serverMessage.equals("start chat")) {
                    chatRoom();
                }
                //print other message except chatroom
                if (!serverMessage.equals("start chat"))
                    System.out.println(serverMessage);
            }
            //Print message that day is ended
            System.out.println("end day.you can't write message.chat room closed.OK? [write ok]");
            Thread.sleep(60000);
            //Finish chatroom
            while (true) {
                serverMessage = reader.readLine();
                if (serverMessage.equals("finish chat"))
                    break;
            }
            //Finish day

            //Ask for [Exit]
            System.out.println("Ask for exit");
            askForExit();

            //Game loop
            while (true) {
                //Start night game
                while (true) {
                    //Get message from server
                    serverMessage = reader.readLine();
                    //If player must start her/his act
                    if (serverMessage.equals("start your act")) {
                        manageNightGame();
                    }
                    //If night finished
                    if (serverMessage.equals("night finished")) {
                        System.out.println(serverMessage + " wait 8 seconds");
                        Thread.sleep(8000);
                        break;
                    }
                    //print server message
                    if ((!serverMessage.equals("start your act")) && (!serverMessage.equals("kill"))
                            && (!serverMessage.equals("hill")) && (!serverMessage.equals("silent")))
                        System.out.println(serverMessage);
                    //If player must be killed
                    if (serverMessage.equals("kill"))
                        role.setAlive(false);
                    //If player must be saved
                    if (serverMessage.equals("hill"))
                        role.setAlive(true);
                    //If player must be saved
                    if (serverMessage.equals("silent"))
                        role.setCanSpeak(false);
                }
                //Finish night game

                //If player died
                if (!role.isAlive()) {
                    System.out.println("you are killed");
                    System.exit(0);
                }

                //Check end condition
                serverMessage = reader.readLine();
                if (serverMessage.equals("finish game")) {
                    System.out.println("finish game");
                    System.exit(0);
                }

                //Ask for [Exit]
                System.out.println("Ask for exit");
                askForExit();

                //start day
                start = System.currentTimeMillis();
                end = start + 300 * 1000;

                while (true) {
                    //If the time is up
                    if (System.currentTimeMillis() >= end)
                        break;
                    //Get message from server
                    serverMessage = reader.readLine();
                    //If server say : start chatroom
                    if (serverMessage.equals("start chat")) {
                        chatRoom();
                    }
                    //print other message except chatroom
                    if (!serverMessage.equals("start chat"))
                        System.out.println(serverMessage);
                }
                //Print message that day is ended
                System.out.println("end day.you can't write message.chat room closed.OK? [write ok]");
                Thread.sleep(60000);
                //Finish chatroom
                while (true) {
                    serverMessage = reader.readLine();
                    if (serverMessage.equals("finish chat"))
                        break;
                }
                //Finish day

               //Ask for [Exit]
                System.out.println("Ask for exit");
                askForExit();

                //Start voting
                while (true) {
                    //Get message from server
                    serverMessage = reader.readLine();
                    //If voting started
                    if (serverMessage.equals("start voting")) {
                        System.out.println("Now is the time to vote. You have 30 seconds to vote");
                        voting();
                    }
                    //print other messages
                    if (!serverMessage.equals("start voting"))
                        System.out.println(serverMessage);
                    //If voting finished
                    if (serverMessage.equals("finish voting"))
                        break;
                }

                //Ask from [Mayor]
                if (role.getName().equals("Mayor")) {
                    while (true) {
                        System.out.println("Do you want cancel voting or no?write [yes] or [no]");
                        String cancel = scanner.nextLine();
                        if (cancel.equals("no") || cancel.equals("yes")) {
                            writer.println(cancel);
                            break;
                        } else {
                            System.out.println("Unvalid input please try again");
                        }
                    }
                }
                //get messages from server
                while (true) {
                    serverMessage = reader.readLine();
                    //If player died
                    if (serverMessage.equals("kill"))
                        role.setAlive(false);
                    //Finish voting
                    if (serverMessage.equals("finish voting"))
                        break;
                    //Print other messages
                    if (!serverMessage.equals("kill"))
                        System.out.println(serverMessage);
                }
                Thread.sleep(8000);
                //End of voting

                //If player died
                if (!role.isAlive()) {
                    System.out.println("You are killed");
                    System.exit(0);
                }

                //History
                System.out.println("Do you want see previous messages?write [yes] or [no]");
                answer = scanner.nextLine();
                if (answer.equals("yes"))
                    showHistory();
            }
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
            ex.printStackTrace();
        } catch (InterruptedException w) {
            w.printStackTrace();
        }

    }

    /**
     * this method return name of player.
     * @return the name of player.
     */
    public String getName() {
        return this.name;
    }

    /**
     * this method return role of player.
     * @return the role of player.
     */
    public Role getRole() {
        return role;
    }

    /**
     * this method set role of player.
     * @param role the role of player.
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * this method stores usernames in the file.
     * @param user new user name.
     */
    public static void storeUserNames(String user) {
        try (FileWriter writer = new FileWriter("users.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            user += " ";
            bufferedWriter.write(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method get username & checks duplication.
     * @param user the usename.
     * @return true or false.
     */
    public static boolean checkUserName(String user) {
        try (FileReader fileReader = new FileReader("users.txt");
             BufferedReader reader = new BufferedReader(fileReader)) {
            int i;
            String names = "";
            while ((i = reader.read()) != -1)
                names += (char) i;
            if (names.contains(user))
                return false;
        } catch (FileNotFoundException f) {
            f.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * this method use during chatroom.
     * create objects from [PlayerReadMessage] & [PlayerWriteMessage].
     */
    public void chatRoom() {
        PlayerWriteMessage writeMessage = new PlayerWriteMessage(socket, this, writer);
        PlayerReadMessage readMessage = new PlayerReadMessage(socket, this);

        if (role.isCanSpeak() == false) {
            System.out.println("You can only see messages");
            role.setCanSpeak(true);
        } else {
            writeMessage.start();
        }
        readMessage.start();
        try {
            Thread.sleep(300000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Depending on the role of the player,
     * this method calls the appropriate method from the night Game class.
     * It then sends the answer to the server and checks its correctness.
     * It then sends the answer to the server and checks its correctness.
     */
    public void manageNightGame() {
        String roleName = role.getName();
        String answer = null;
        String serverMessage = null;

        switch (roleName) {
            case "godFather":
                try {
                    serverMessage = reader.readLine();
                    System.out.println(serverMessage);
                    while (true) {
                        //Get input from user
                        answer = NightGame.mafiaAct();
                        //Send to server
                        writer.println(answer);
                        //Get answer from server
                        serverMessage = reader.readLine();
                        //Check condition of break
                        if (serverMessage.equals("Ok"))
                            break;
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Doctor lector":
                try {
                    //KILL
                    while (true) {
                        //Get input from user
                        answer = NightGame.mafiaAct();
                        //Send to server
                        writer.println(answer);
                        //Get answer from server
                        serverMessage = reader.readLine();
                        //Check condition of break
                        if (serverMessage.equals("Ok"))
                            break;
                        System.out.println(serverMessage);
                    }
                    //HILL
                    while (true) {
                        //Get input from user
                        answer = NightGame.doctor();
                        //Send to server
                        writer.println(answer);
                        //Get answer from server
                        serverMessage = reader.readLine();
                        //Check condition of break
                        if (serverMessage.equals("Ok"))
                            break;
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "ordinary Mafia":
                try {
                    while (true) {
                        //Get input from user
                        answer = NightGame.mafiaAct();
                        //Send to server
                        writer.println(answer);
                        //Get answer from server
                        serverMessage = reader.readLine();
                        //Check condition of break
                        if (serverMessage.equals("Ok"))
                            break;
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "City doctor":
                try {
                    while (true) {
                        //Get input from user
                        answer = NightGame.doctor();
                        //Send to server
                        writer.println(answer);
                        //Get answer from server
                        serverMessage = reader.readLine();
                        //Check condition of break
                        if (serverMessage.equals("Ok"))
                            break;
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Detective":
                try {
                    while (true) {
                        //Get input from user
                        answer = NightGame.detective();
                        //Send to server
                        writer.println(answer);
                        //Get answer from server
                        serverMessage = reader.readLine();
                        //Check condition of break
                        if (serverMessage.equals("Ok"))
                            break;
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Sniper":
                try {
                    while (true) {
                        //Get input from user
                        answer = NightGame.sniper();
                        //Send to server
                        writer.println(answer);
                        //Get answer from server
                        serverMessage = reader.readLine();
                        //Check condition of break
                        if (serverMessage.equals("Ok"))
                            break;
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Psychologist":
                try {
                    while (true) {
                        //Get input from user
                        answer = NightGame.psychologist();
                        //Send to server
                        writer.println(answer);
                        //Get answer from server
                        serverMessage = reader.readLine();
                        //Check condition of break
                        if (serverMessage.equals("Ok"))
                            break;
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Die hard":
                DieHard dieHard = (DieHard) role;
                if (dieHard.getInquiryCounter() >= 2) {
                    System.out.println("You have used the permissible number of your inquiries");
                } else {
                    answer = NightGame.dieHard();
                    writer.println(answer);
                    if (answer.equals("yes"))
                        dieHard.increamentInquiryCounter();
                }
                break;
            default:
                System.out.println("You have nothing to do at night. " +
                        "Wait for the night to end");
                break;
        }
    }

    /**
     * this method use for voting.
     * It takes a vote from the player and then sends it to the server.
     * It will end if the server confirms it correctly.
     */
    public void voting() {
        try {
            long start = System.currentTimeMillis();
            long end = start + 30 * 1000;
            String delete = null;

            while (System.currentTimeMillis() < end) {
                if (System.currentTimeMillis() >= end) {
                    System.out.println("Your time is up");
                    delete = "Refrained";
                    writer.println(delete);
                    break;
                }
                //Get vote from player
                delete = role.getVote();
                //Send to server
                writer.println(delete);
                //Get response from server
                String serverMessage = reader.readLine();
                if (serverMessage.equals("Ok")) {
                    System.out.println("Your vote has been registered");
                    break;
                }
                System.out.println(serverMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method ask from player that
     * does s/he want to exit from game or no?
     * sends player's answer to server.
     * if s/he wants exit from game . close the console.
     * also server remove it.
     */
    public void askForExit() {
        Scanner scanner      = new Scanner(System.in);
        String answer        = null;
        String serverMessage = "";

        //Read server message
        try {
            serverMessage = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Ask from player
        if (serverMessage.equals("ASK")) {
            while (true) {
                System.out.println("Do you want to exit?write [yes] or [no]");
                answer = scanner.nextLine();
                if (answer.equals("yes") || answer.equals("no"))
                    break;
            }
            //Send valid answer to server
            writer.println(answer);
        }
        if (answer.equals("yes"))
            System.exit(0);
    }

    /**
     * this method shows all previous chatroom's messages
     * to player.
     */
    public void showHistory() {
        try (FileReader fileReader = new FileReader("messages.txt");
             BufferedReader reader = new BufferedReader(fileReader)) {
            int i;
            while ((i = reader.read()) != -1)
                System.out.print((char) i);
            System.out.println();
        } catch (FileNotFoundException f) {
            f.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        //get port of game from user
        int portNumber;
        while (true) {
            System.out.println("enter port of game that you want connected to it");
            portNumber = scanner.nextInt();
            if (portNumber == 6000)
                break;
            else
                System.out.println("invalid port number");
        }

        //We check that the username is not duplicate
        boolean condition = false;
        String user = "";
        while (!condition) {
            System.out.println("enter user name");
            user = scanner.nextLine();
            condition = checkUserName(user);
            if (condition == true)
                break;
        }
        storeUserNames(user);

        //create an object for player
        PlayerMafia playerMafia = new PlayerMafia(portNumber, user);
        playerMafia.execute();
    }
}
