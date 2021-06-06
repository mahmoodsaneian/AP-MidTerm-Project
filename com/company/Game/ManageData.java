package com.company.Game;

import com.company.characters.Role;

import java.util.ArrayList;

public class ManageData {
    private static ArrayList<String> usernames = new ArrayList<String>();
    private static ArrayList<Role>   roles = new CreateRoles().getRoles();

    public static void addUsername(String user){
        usernames.add(user);
    }

    public static Role getRole(String name){
        Role role = null;
        for (Role role1 : roles){
            if (role1.getName().equals(name)){
                role = role1;
                break;
            }
        }
        return role;
    }

    public static void printUsernames(){
        System.out.println(usernames);
    }
}
