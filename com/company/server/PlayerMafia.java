package com.company.server;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class PlayerMafia {
    private String name;
    private int port;

    public PlayerMafia(int port, String name) {
        this.port = port;
        this.name = name;
    }

    public void execute() {
        try {
            Socket socket = new Socket("127.0.0.1", port);
            System.out.println("welcome. you entered the game");

            PlayerReadMessage readMessage   = new PlayerReadMessage(socket, this);
            PlayerWriteMessage writeMessage = new PlayerWriteMessage(socket, this);

            writeMessage.setUserName(name);
            writeMessage.start();
            readMessage.start();
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
            ex.printStackTrace();
        }

    }

    String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("enter port");
        int portNumber = scanner.nextInt();
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
        PlayerMafia playerMafia = new PlayerMafia(portNumber, user);
        playerMafia.execute();
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
}
