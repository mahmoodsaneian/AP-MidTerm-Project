package com.company.server;

import com.company.Game.ManageData;
import com.company.Game.NightGame;
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
            while (!answer.equals("ready")){
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
            System.out.println("the first night of the game started\n"+
                    "your role in the game : "+role.getName()+"\n"+
                    "your role description : "+role.getRoleDescription());

            while (!serverMessage.equals("finish first night")){
                serverMessage = reader.readLine();
                if (serverMessage.equals("finish first night"))
                    break;
                System.out.println(serverMessage);
            }
            //end first night
            System.out.println("The first night of the game is over");
            //night of game
            System.out.println("night started");
            while (!serverMessage.equals("night finished")){
                serverMessage = reader.readLine();
                if (serverMessage.equals("night finished")){
                    System.out.println(serverMessage);
                    break;
                }
            }
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
            ex.printStackTrace();
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
        PlayerReadMessage readMessage = new PlayerReadMessage(socket, this, reader);

        writeMessage.start();
        readMessage.start();
    }

    public void manageNightGame(){
        String answer = "";
        switch (role.getName()){
            case "godFather":
                NightGame.godFather();
                break;
            case "Doctor lector":
                NightGame.lectorAndOrdinaryMafia();
                NightGame.doctor("Mafia");
                break;
            case "ordinary Mafia":
                NightGame.lectorAndOrdinaryMafia();
                break;
            case "City doctor":
                NightGame.doctor("Citizen");
                break;
            case "Detective":
                NightGame.detective();
                break;
            case "Sniper":
                NightGame.sniper();
                break;
            case "Psychologist":
                NightGame.psychologist();
                break;
            case "Die hard":
                NightGame.dieHard();
                break;
        }
    }
}
