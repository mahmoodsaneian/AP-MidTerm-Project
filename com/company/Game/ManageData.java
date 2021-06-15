package com.company.Game;

import com.company.characters.Role;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * this class include some static methods that
 * can call from each section of game.
 * also include some information about game's data such as
 * roles & name of players who get each roles.
 *
 * @author  mahmood-saneian
 * @since   2021-6-15
 * @version 15.0.2
 */
public class ManageData {
    //the list of roles in the game
    private static ArrayList<Role>   roles = new CreateRoles().getRoles();
    //specifies each player gets which role
    private static HashMap<Role, String> rolesAndNames;

    /**
     * this method get name of role & return instance of role class.
     * @param name the name of role
     * @return the role's that player wants it.
     */
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

    /**
     * this method set a hashmap that include information about
     * roles and usernames.
     * @param r roles and usernames in the game
     */
    public static void setRolesAndNames(HashMap<Role, String> r){
        rolesAndNames = r;
    }

    /**
     * this method return roles and usernames that exists in the game.
     * @return roles and usernames.
     */
    public static HashMap<Role, String> getRolesAndNames() {
        return rolesAndNames;
    }
}
