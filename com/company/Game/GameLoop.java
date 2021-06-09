package com.company.Game;

import com.company.characters.Role;
import com.company.server.Server;

import java.lang.reflect.Array;
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
        if (name != null){
            server.sendMessageToSpecifiecPlayer(name, message);
        }
        //Introduction to doctor lector
        name = rolesAndUsernames.get(doctorlector);
        message = "your teammates in game are : "+rolesAndUsernames.get(godFather)+
            "-"+rolesAndUsernames.get(ordinaryMafia) + "\n"
            +rolesAndUsernames.get(godFather) + " is a god father \n"
            +rolesAndUsernames.get(ordinaryMafia) + " is an ordinary mafia"+
            "You have to work together to defeat the citizens team members";
        if (name != null){
            server.sendMessageToSpecifiecPlayer(name, message);
        }
        //Introduction to ordinary mafia
        name = rolesAndUsernames.get(ordinaryMafia);
        message = "your teammates in game are : "+rolesAndUsernames.get(godFather)
                +" - "+rolesAndUsernames.get(doctorlector)+"\n"
                +rolesAndUsernames.get(godFather)+" is a god father \n"
                +rolesAndUsernames.get(doctorlector)+" is a doctor lector"+"\n"+
                "You have to work together to defeat the citizens team members";
        if (name != null){
            server.sendMessageToSpecifiecPlayer(name, message);
        }
        //Introducing city doctor to mayor
        Role cityDoctor = ManageData.getRole("City doctor");
        Role mayor = ManageData.getRole("Mayor");
        name = rolesAndUsernames.get(mayor);
        message = "city doctor is : "+rolesAndUsernames.get(cityDoctor);
        if (name != null){
            server.sendMessageToSpecifiecPlayer(name, message);
        }
        //Send message to other roles
        Role psychologist = ManageData.getRole("Psychologist");
        Role sniper = ManageData.getRole("Sniper");
        Role ordinaryCitizen = ManageData.getRole("Ordinary Citizen");
        Role diehard = ManageData.getRole("Die hard");
        Role detective = ManageData.getRole("Detective");
        message = "You and the rest of the citizens' team must " +
                "try to identify the Mafia and defeat them";
        if (rolesAndUsernames.get(psychologist) != null){
            server.sendMessageToSpecifiecPlayer(rolesAndUsernames.get(psychologist),message);
        }
        if (rolesAndUsernames.get(sniper) != null){
            server.sendMessageToSpecifiecPlayer(rolesAndUsernames.get(sniper),message);
        }
        if (rolesAndUsernames.get(ordinaryCitizen) != null){
            server.sendMessageToSpecifiecPlayer(rolesAndUsernames.get(ordinaryCitizen),message);
        }
        if (rolesAndUsernames.get(diehard) != null){
            server.sendMessageToSpecifiecPlayer(rolesAndUsernames.get(diehard),message);
        }
        if (rolesAndUsernames.get(cityDoctor) != null){
            server.sendMessageToSpecifiecPlayer(rolesAndUsernames.get(cityDoctor),message);
        }
        if (rolesAndUsernames.get(detective) != null){
            server.sendMessageToSpecifiecPlayer(rolesAndUsernames.get(detective),message);
        }
    }

//    public void print(){
//        Set<Role> roleSet = rolesAndUsernames.keySet();
//        for (Role role : roleSet){
//            System.out.println(rolesAndUsernames.get(role) + " : "+role.getName());
//        }
//    }

    public void nightGame(){
        //
//        NightGame nightGame = new NightGame(ManageData.getRolesAndNames());
        //
        server.sendMessageToAll("exist player in the game : "+ManageData.getUsernames());
        //Those who do not do anything special at night
        Role mayor = ManageData.getRole("Mayor");
        Role ordinaryCitizen = ManageData.getRole("Ordinary Citizen");

        String mayorName = rolesAndUsernames.get(mayor);
        String citizenName = rolesAndUsernames.get(ordinaryCitizen);
        //send message to mayor
        if (mayorName != null) {
            server.sendMessageToSpecifiecPlayer(mayorName, "start your act");
            server.joinThread(mayorName);
        }
        //send message to ordinary citizen
        if (citizenName != null) {
            server.sendMessageToSpecifiecPlayer(citizenName, "start your act");
            server.joinThread(citizenName);
        }
        //start acts of Mafia team
        //Find mafia team
        Role godFather     = ManageData.getRole("godFather");
        Role ordinaryMafia = ManageData.getRole("ordinary Mafia");
        Role doctorLector  = ManageData.getRole("Doctor lector");
        String godName = rolesAndUsernames.get(godFather);
        String doctorName = rolesAndUsernames.get(doctorLector);
        String ordinaryName = rolesAndUsernames.get(ordinaryMafia);
        //send message to Ordinary Mafia.
        if (ordinaryName != null) {
            server.sendMessageToSpecifiecPlayer(ordinaryName, "start your act");
            server.joinThread(ordinaryName);
        }
        //send message to Doctor Lector.
        if (doctorName != null) {
            server.sendMessageToSpecifiecPlayer(doctorName, "start your act");
            server.joinThread(doctorName);
        }
        //send message to God Father.
        if (godName != null) {
            server.sendMessageToSpecifiecPlayer(godName, "start your act");
            server.joinThread(godName);
        }
        //start acts of Citizen team
        //Find members of Citizen Team
        Role doctor = ManageData.getRole("City doctor");
        Role detective = ManageData.getRole("Detective");
        Role sniper = ManageData.getRole("Sniper");
        Role psychologist = ManageData.getRole("Psychologist");
        Role dieHard = ManageData.getRole("Die hard");

        String cityDoctor = rolesAndUsernames.get(doctor);
        String detectiveName = rolesAndUsernames.get(detective);
        String sniperName = rolesAndUsernames.get(sniper);
        String psychologistName = rolesAndUsernames.get(psychologist);
        String dieHardName = rolesAndUsernames.get(dieHard);
        //send message to City Doctor.
        if (cityDoctor != null) {
            server.sendMessageToSpecifiecPlayer(cityDoctor, "start your act");
            server.joinThread(cityDoctor);
        }
        //send message to Detective.
        if (detectiveName != null) {
            server.sendMessageToSpecifiecPlayer(detectiveName, "start your act");
            server.joinThread(detectiveName);
        }
        //send message to Sniper.
        if (sniperName != null) {
            server.sendMessageToSpecifiecPlayer(sniperName, "start your act");
            server.joinThread(sniperName);
        }
        //send message to Psychologist.
        if (psychologistName != null) {
            server.sendMessageToSpecifiecPlayer(psychologistName, "start your act");
            server.joinThread(psychologistName);
        }
        //send message to Die Hard.
        if (dieHardName != null) {
            server.sendMessageToSpecifiecPlayer(dieHardName, "start your act");
            server.joinThread(dieHardName);
        }
    }

//    public void Voting(){
//        ArrayList<String> votes = new ArrayList<String>();
//        Set<Role> roles = rolesAndUsernames.keySet();
//        String name = null;
//        String vote = "";
//
//        for (Role role : roles){
//            name = rolesAndUsernames.get(role);
//            if (name != null) {
//                server.joinThread(name);
//                vote = server.getMessageFromSpecifiecPlayer(name);
//                votes.add(vote);
//                name = null;
//            }
//        }
//
//        String message = null;
//        String mayorName = null;
//        String answer = null;
//        Role mayor = ManageData.getRole("Mayor");
//        mayorName = rolesAndUsernames.get(mayor);
//        if (mayorName != null){
//            message = "votes are : "+votes;
//            server.sendMessageToSpecifiecPlayer(mayorName,message);
//            server.joinThread(mayorName);
//            answer = server.getMessageFromSpecifiecPlayer(mayorName);
//        }
//
//        if (answer.equals("No") || answer.equals("no")){
//            int[] counterVotes = new int[votes.size()];
//
//            for (int i = 0;i < votes.size();i++){
//                String user = votes.get(i);
//                int counter = 0;
//                for (int j = 0;j < votes.size();j++){
//                    if (i != j){
//                        if (user.equals(votes.get(j)))
//                            counter++;
//                    }
//                }
//            counterVotes[i] = counter;
//            }
//
//            int index = 0;
//            for (int i = 0;i < counterVotes.length;i++){
//                if (counterVotes[i] > counterVotes[index]){
//                    index = i;
//                }
//            }
//
//            String delete = votes.get(index);
//        }
//    }
}
