package com.company.server;

import com.company.Game.ManageData;
import com.company.Game.NightGame;
import com.company.characters.DieHard;
import com.company.characters.Role;
import com.company.server.chatroom.PlayerReadMessage;
import com.company.server.chatroom.PlayerWriteMessage;

import java.net.*;
import java.io.*;
import java.util.IllegalFormatCodePointException;
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
            //We ask the players if they are ready to start the game or not?
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

//            //start day
//            long start = System.currentTimeMillis();
//            long end = start + 300 * 1000;

//            while (true) {
//                if (System.currentTimeMillis() == end)
//                    break;
//                serverMessage = reader.readLine();
////                System.out.println(serverMessage);
//                if (serverMessage.equals("start of the day phase")){
//                    chatRoom();
//                    Thread.sleep(300000);
//                }
//            }
//            System.out.println("end day");
            //night of game
//            while (true) {
//                serverMessage = reader.readLine();
//                if (serverMessage.equals("start your act")) {
//                    manageNightGame();
//                }
//                if (serverMessage.equals("night finished")) {
//                    System.out.println(serverMessage + " wait 8 seconds");
//                    Thread.sleep(8000 );
//                    break;
//                }
//                if (!serverMessage.equals("start your act"))
//                    System.out.println(serverMessage);
//            }

            //start voting
            while (true){
                serverMessage = reader.readLine();
                if(serverMessage.equals("start voting")){
                    System.out.println("Now is the time to vote. You have 30 seconds to vote");
                    voting();
                }
                if (!serverMessage.equals("start voting"))
                    System.out.println(serverMessage);
                if (serverMessage.equals("finish voting"))
                    break;
            }
            //Ask from [Mayor]
            if (role.getName().equals("Mayor")){
                while (true){
                    System.out.println("Do you want cancel voting or no?write [yes] or [no]");
                    String cancel = scanner.nextLine();
                    if (cancel.equals("no") || cancel.equals("yes")){
                        writer.println(cancel);
                        break;
                    }else{
                        System.out.println("Unvalid input please try again");
                    }
                }
            }
            serverMessage = reader.readLine();
            System.out.println(serverMessage);
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

    public void setName(String name) {
        this.name = name;
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


    public void setRole(Role role) {
        this.role = role;
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

    public void chatRoom() {
        PlayerWriteMessage writeMessage = new PlayerWriteMessage(socket, this, writer);
        PlayerReadMessage readMessage = new PlayerReadMessage(socket, this);
        writeMessage.start();
        readMessage.start();
        long start = System.currentTimeMillis();
        long end = start + 300 * 1000;
        if (System.currentTimeMillis() == end) {
            writeMessage.stop();
            readMessage.stop();
            System.out.println("bastam");
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
                    while (true){
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
                }catch (IOException e){
                    e.printStackTrace();
                }
                break;
            case "Doctor lector":
                try {
                    //KILL
                    while (true){
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
                    while (true){
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
                }catch (IOException e){
                    e.printStackTrace();
                }
                break;
            case "ordinary Mafia":
                try {
                    while (true){
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
                }catch (IOException e){
                    e.printStackTrace();
                }
                break;
            case "City doctor":
                try {
                    while (true){
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
                }catch (IOException e){
                    e.printStackTrace();
                }
                break;
            case "Detective":
                try {
                    while (true){
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
                }catch (IOException e){
                    e.printStackTrace();
                }
                break;
            case "Sniper":
                try {
                    while (true){
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
                }catch (IOException e){
                    e.printStackTrace();
                }
                break;
            case "Psychologist":
                try {
                    while (true){
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
                }catch (IOException e){
                    e.printStackTrace();
                }
                break;
            case "Die hard":
                DieHard dieHard = (DieHard) role;
                if (dieHard.getKillCounter() == 2){
                    System.out.println("You have used the permissible number of your inquiries");
                }else {
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

    public void voting(){
        try {
            long start = System.currentTimeMillis();
            long end = start + 30 * 1000;
            String delete = null;

            while (System.currentTimeMillis() < end){
                if (System.currentTimeMillis() >= end){
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
                if (serverMessage.equals("Ok")){
                    System.out.println("Your vote has been registered");
                    break;
                }
                System.out.println(serverMessage);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
