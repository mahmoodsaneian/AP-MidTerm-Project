package com.company.server;

import com.company.SharedData;
import com.company.characters.Role;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class PlayerMafia {
    //name of player
    private String name;
    //port of game
    private int port;
    //role of player
    private Role role;

    /**
     *
     * @param port
     * @param name
     */
    public PlayerMafia(int port, String name) {
        this.port = port;
        this.name = name;
    }

    /**
     *
     */
    public void execute() {
        try {
            Socket socket = new Socket("127.0.0.1", port);
            System.out.println("welcome. you entered the game");

            PlayerWriteMessage writeMessage = new PlayerWriteMessage(socket, this);
            PlayerReadMessage  readMessage  = new PlayerReadMessage(socket, this);

            writeMessage.start();
            readMessage.start();
            try {
                readMessage.join();
            }catch (InterruptedException i){
                i.printStackTrace();
            }
            readMessage.getRoleFromServer();
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
            ex.printStackTrace();
        }

    }

    /**
     *
     * @return
     */
    String getName() {
        return this.name;
    }

    public Role getRole() {
        return role;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }


    public static void storeUserNames(String user){
       try (FileWriter writer = new FileWriter("users.txt",true);
       BufferedWriter bufferedWriter = new BufferedWriter(writer)){
           user += " ";
           bufferedWriter.write(user);
       }catch (IOException e){
           e.printStackTrace();
       }
    }


    public static boolean checkUserName(String user){
        try (FileReader fileReader = new FileReader("users.txt");
        BufferedReader reader = new BufferedReader(fileReader)){
            int i;
            String names = "";
            while ((i = reader.read()) != -1)
                names += (char) i;
            if (names.contains(user))
                return false;
        }catch (FileNotFoundException f){
            f.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return true;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        //get port of game from user
        int portNumber;
        while (true){
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
        while (!condition){
            System.out.println("enter user name");
            user = scanner.nextLine();
            condition = checkUserName(user);
            if(condition == true)
                break;
        }
        storeUserNames(user);

//        create an object for player
        PlayerMafia playerMafia = new PlayerMafia(portNumber, user);
        playerMafia.execute();
    }

}
