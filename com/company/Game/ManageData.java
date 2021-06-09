package com.company.Game;

import com.company.characters.Role;

import java.util.ArrayList;
import java.util.HashMap;

public class ManageData {
    private static ArrayList<String> usernames = new ArrayList<String>();
    private static ArrayList<Role>   roles = new CreateRoles().getRoles();
    private static HashMap<Role, String> rolesAndNames;

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

    public static void setRolesAndNames(HashMap<Role, String> r){
        rolesAndNames = r;
    }

    public static HashMap<Role, String> getRolesAndNames() {
        return rolesAndNames;
    }

    public static ArrayList<String> getUsernames() {
        return usernames;
    }


}
