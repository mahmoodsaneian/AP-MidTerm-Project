package com.company.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private Set<String> userNames;
    private Set<PlayerThread> userThreads;

    public Server() {
        userNames = new HashSet<String>();
        userThreads = new HashSet<PlayerThread>();
    }

    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(6000)) {

            System.out.println("game is start on port : 6000");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New player connected");

                PlayerThread newUser = new PlayerThread(socket, this);
                userThreads.add(newUser);
                newUser.start();

            }

        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    void broadcast(String message, PlayerThread excludeUser) {
        for (PlayerThread aUser : userThreads) {
            if (aUser != excludeUser)
                aUser.sendMessage(message);
        }
    }


    void addUserName(String userName) {
        userNames.add(userName);
    }


    void removeUser(String userName, PlayerThread aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(aUser);
            System.out.println("The player " + userName + " quitted");
        }
    }

    Set<String> getUserNames() {
        return this.userNames;
    }

    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }

    public static void main(String args[]) {
        Server server = new Server();
        server.execute();
    }
}

