package com.company.Game;

import com.company.characters.Role;
import com.company.server.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class GameLoop {
    private Server server;
    private HashMap<Role, String> rolesAndUsernames;

    public GameLoop(Server server,HashMap<String,String> nameRolesAndUsernames) {
        this.server = server;
        rolesAndUsernames = new HashMap<Role, String>();
        initHashMap(nameRolesAndUsernames);
    }

    public void initHashMap(HashMap<String, String> nameRolesAndUsernames){
        ArrayList<Role> roles = new CreateRoles().getRoles();
        Set<String> nameRoles = nameRolesAndUsernames.keySet();

        for (String name : nameRoles){
            Role   role = ManageData.getRole(name);
            String roleOwner = nameRolesAndUsernames.get(name);
            rolesAndUsernames.put(role, roleOwner);
        }
        ManageData.setRolesAndNames(rolesAndUsernames);
    }

    public HashMap<Role, String> getRolesAndUsernames() {
        return rolesAndUsernames;
    }

    public void firstNight(){
        String name = null;
        String message = null;
        //Introducing Mafia members to each other
        Role godFather = ManageData.getRole("godFather");
        Role ordinaryMafia = ManageData.getRole("ordinary Mafia");
        Role doctorlector  = ManageData.getRole("Doctor lector");
        //Introduction to god father
        name = rolesAndUsernames.get(godFather);
        message = "your teammates in game are : "+rolesAndUsernames.get(doctorlector) +
                " - "+rolesAndUsernames.get(ordinaryMafia) + "\n"
                +rolesAndUsernames.get(doctorlector) + " is a doctor lector\n"
                +rolesAndUsernames.get(ordinaryMafia)+ " is an ordinary mafia"+
                "You have to work together to defeat the citizens team members";
        server.sendMessageToSpecifiecPlayer(name, message);
        //Introduction to doctor lector
        name = rolesAndUsernames.get(doctorlector);
        message = "your teammates in game are : "+rolesAndUsernames.get(godFather)+
            "-"+rolesAndUsernames.get(ordinaryMafia) + "\n"
            +rolesAndUsernames.get(godFather) + " is a god father \n"
            +rolesAndUsernames.get(ordinaryMafia) + " is an ordinary mafia"+
            "You have to work together to defeat the citizens team members";
        server.sendMessageToSpecifiecPlayer(name, message);
        //Introduction to ordinary mafia
        name = rolesAndUsernames.get(ordinaryMafia);
        message = "your teammates in game are : "+rolesAndUsernames.get(godFather)
                +" - "+rolesAndUsernames.get(doctorlector)+"\n"
                +rolesAndUsernames.get(godFather)+" is a god father \n"
                +rolesAndUsernames.get(doctorlector)+" is a doctor lector"+"\n"+
                "You have to work together to defeat the citizens team members";
        server.sendMessageToSpecifiecPlayer(name, message);
        //Introducing city doctor to mayor
        Role cityDoctor = ManageData.getRole("City doctor");
        Role mayor = ManageData.getRole("Mayor");
        name = rolesAndUsernames.get(mayor);
        message = "city doctor is : "+rolesAndUsernames.get(cityDoctor);
        server.sendMessageToSpecifiecPlayer(name, message);
        //Send message to other roles
        Role psychologist = ManageData.getRole("Psychologist");
        Role sniper = ManageData.getRole("Sniper");
        Role ordinaryCitizen = ManageData.getRole("Ordinary Citizen");
        Role diehard = ManageData.getRole("Die hard");
        Role detective = ManageData.getRole("Detective");
        message = "You and the rest of the citizens' team must " +
                "try to identify the Mafia and defeat them";
        server.sendMessageToSpecifiecPlayer(rolesAndUsernames.get(psychologist),message);
        server.sendMessageToSpecifiecPlayer(rolesAndUsernames.get(sniper),message);
        server.sendMessageToSpecifiecPlayer(rolesAndUsernames.get(ordinaryCitizen),message);
        server.sendMessageToSpecifiecPlayer(rolesAndUsernames.get(diehard),message);
        server.sendMessageToSpecifiecPlayer(rolesAndUsernames.get(cityDoctor),message);
        server.sendMessageToSpecifiecPlayer(rolesAndUsernames.get(detective),message);
    }

//    public void print(){
//        Set<Role> roleSet = rolesAndUsernames.keySet();
//        for (Role role : roleSet){
//            System.out.println(rolesAndUsernames.get(role) + " : "+role.getName());
//        }
//    }

    public void nightGame(){
        String name = null;
        //Mafia team
        Role godFather     = ManageData.getRole("godFather");
        Role ordinaryMafia = ManageData.getRole("ordinary Mafia");
        Role doctorLector  = ManageData.getRole("Doctor lector");

    }
}
