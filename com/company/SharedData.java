package com.company;

import com.company.characters.Role;

import java.util.ArrayList;

public class SharedData {
    private ArrayList<String> usernames;
    private ArrayList<Role>   roles;
    private static SharedData sharedData = null;

    private SharedData() {
        usernames = new ArrayList<String>();
        roles     = new ArrayList<Role>();
    }

    public void addUsername(String user){
        usernames.add(user);
    }

    public void addRole(Role role){
        roles.add(role);
    }

    public boolean checkUsernameRepeatition(String user){
        for (String usename : usernames){
            if (usename.equals(user))
                return false;
        }
        return true;
    }

    public static SharedData getSharedData(){
        if (sharedData == null)
            sharedData = new SharedData();
        return sharedData;
    }

    public void printusername(){
        System.out.println(usernames);
    }
}
