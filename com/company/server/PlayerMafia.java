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

public class PlayerMafia {
    private Socket socket;
    private String name;
    private int port;
    private Role role;
    private BufferedReader reader;
    private PrintWriter writer;

    public PlayerMafia(int port, String name) {
        this.port = port;
        this.name = name;
    }

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
            Thread.sleep(20000);
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

    public String getName() {
        return this.name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public static void storeUserNames(String user) {
        try (FileWriter writer = new FileWriter("users.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            user += " ";
            bufferedWriter.write(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public void chatRoom() {
        PlayerWriteMessage writeMessage = new PlayerWriteMessage(socket, this, writer);
        PlayerReadMessage readMessage = new PlayerReadMessage(socket, this);
        if (role.isCanSpeak() == false) {
            System.out.println("You can only see messages");
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
                if (dieHard.getKillCounter() == 2) {
                    System.out.println("You have used the permissible number of your inquiries");
                } else {
                    answer = NightGame.dieHard();
                    writer.println(answer);
                }
                break;
            default:
                System.out.println("You have nothing to do at night. " +
                        "Wait for the night to end");
                break;
        }
    }

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

    public void askForExit() {
        Scanner scanner = new Scanner(System.in);
        String answer = null;
        String serverMessage = "";
        try {
            serverMessage = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (serverMessage.equals("ASK")) {
            while (true) {
                System.out.println("Do you want to exit?write [yes] or [no]");
                answer = scanner.nextLine();
                if (answer.equals("yes") || answer.equals("no"))
                    break;
            }
            writer.println(answer);
        }
        if (answer.equals("yes"))
            System.exit(0);
    }

    public void showHistory() {
        try (FileReader fileReader = new FileReader("messages.txt");
             BufferedReader reader = new BufferedReader(fileReader)) {
            int i;
            while ((i = reader.read()) != -1)
                System.out.print((char) i);
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
