package com.company.Game;

import com.company.characters.CityDoctor;
import com.company.characters.DieHard;
import com.company.characters.DoctorLector;
import com.company.characters.Role;
import com.company.server.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class GameLoop {
    private Server server;
    private HashMap<Role, String> rolesAndUsernames;
    private HashMap<Role, String> deads;
    boolean dieHardAct;

    public GameLoop(Server server, HashMap<String, String> nameRolesAndUsernames) {
        this.server = server;
        rolesAndUsernames = new HashMap<Role, String>();
        dieHardAct = false;
        deads = new HashMap<Role, String>();
        initHashMap(nameRolesAndUsernames);
    }

    public void setDieHardAct(boolean dieHardAct) {
        this.dieHardAct = dieHardAct;
    }

    public void initHashMap(HashMap<String, String> nameRolesAndUsernames) {
        Set<String> nameRoles = nameRolesAndUsernames.keySet();

        for (String name : nameRoles) {
            Role role = ManageData.getRole(name);
            String roleOwner = nameRolesAndUsernames.get(name);
            rolesAndUsernames.put(role, roleOwner);
        }

        ManageData.setRolesAndNames(rolesAndUsernames);
    }

    public HashMap<Role, String> getRolesAndUsernames() {
        return rolesAndUsernames;
    }

    public void firstNight() {
        String name = null;
        String message = null;

        //Introducing Mafia members to each other
        //Find Mafia Members
        Role godFather = ManageData.getRole("godFather");
        Role ordinaryMafia = ManageData.getRole("ordinary Mafia");
        Role doctorlector = ManageData.getRole("Doctor lector");

        //Introduction to god father
        name = rolesAndUsernames.get(godFather);
        message = "your teammates in game are : " + rolesAndUsernames.get(doctorlector) +
                " - " + rolesAndUsernames.get(ordinaryMafia) + "\n"
                + rolesAndUsernames.get(doctorlector) + " is a doctor lector\n"
                + rolesAndUsernames.get(ordinaryMafia) + " is an ordinary mafia" +
                "You have to work together to defeat the citizens team members";
        if (name != null) {
            server.sendMessageToSpecifiecPlayer(name, message);
        }

        //Introduction to doctor lector
        name = rolesAndUsernames.get(doctorlector);
        message = "your teammates in game are : " + rolesAndUsernames.get(godFather) +
                "-" + rolesAndUsernames.get(ordinaryMafia) + "\n"
                + rolesAndUsernames.get(godFather) + " is a god father \n"
                + rolesAndUsernames.get(ordinaryMafia) + " is an ordinary mafia" +
                "You have to work together to defeat the citizens team members";
        if (name != null) {
            server.sendMessageToSpecifiecPlayer(name, message);
        }

        //Introduction to ordinary mafia
        name = rolesAndUsernames.get(ordinaryMafia);
        message = "your teammates in game are : " + rolesAndUsernames.get(godFather)
                + " - " + rolesAndUsernames.get(doctorlector) + "\n"
                + rolesAndUsernames.get(godFather) + " is a god father \n"
                + rolesAndUsernames.get(doctorlector) + " is a doctor lector" + "\n" +
                "You have to work together to defeat the citizens team members";
        if (name != null) {
            server.sendMessageToSpecifiecPlayer(name, message);
        }

        //Introducing city doctor to mayor
        //Find city doctor & mayor
        Role cityDoctor = ManageData.getRole("City doctor");
        Role mayor = ManageData.getRole("Mayor");
        //Send message to Mayor
        name = rolesAndUsernames.get(mayor);
        message = "city doctor is : " + rolesAndUsernames.get(cityDoctor);
        if (name != null) {
            server.sendMessageToSpecifiecPlayer(name, message);
        }

        //Send message to other roles
        //Find other Citizen Members
        Role psychologist = ManageData.getRole("Psychologist");
        Role sniper = ManageData.getRole("Sniper");
        Role ordinaryCitizen = ManageData.getRole("Ordinary Citizen");
        Role diehard = ManageData.getRole("Die hard");
        Role detective = ManageData.getRole("Detective");
        message = "You and the rest of the citizens' team must " +
                "try to identify the Mafia and defeat them";
        //Send message to [Psychologist]
        if (rolesAndUsernames.get(psychologist) != null) {
            server.sendMessageToSpecifiecPlayer(rolesAndUsernames.get(psychologist), message);
        }
        //Send message to [Sniper]
        if (rolesAndUsernames.get(sniper) != null) {
            server.sendMessageToSpecifiecPlayer(rolesAndUsernames.get(sniper), message);
        }
        //Send message to [Ordinary Citizen]
        if (rolesAndUsernames.get(ordinaryCitizen) != null) {
            server.sendMessageToSpecifiecPlayer(rolesAndUsernames.get(ordinaryCitizen), message);
        }
        //Send message to [Die Hard]
        if (rolesAndUsernames.get(diehard) != null) {
            server.sendMessageToSpecifiecPlayer(rolesAndUsernames.get(diehard), message);
        }
        //Send message to [City Doctor]
        if (rolesAndUsernames.get(cityDoctor) != null) {
            server.sendMessageToSpecifiecPlayer(rolesAndUsernames.get(cityDoctor), message);
        }
        //Send message to [Detective]
        if (rolesAndUsernames.get(detective) != null) {
            server.sendMessageToSpecifiecPlayer(rolesAndUsernames.get(detective), message);
        }
    }

    public void nightGame() {
        boolean condition = false;
        String answer = "";
        ArrayList<String> mafiaVotes = new ArrayList<String>();

        //print exist players to player
        server.sendMessageToAll("exist player in the game : " + server.getUserNames());

        //Those who do not do anything special at night
        Role mayor = ManageData.getRole("Mayor");
        Role ordinaryCitizen = ManageData.getRole("Ordinary Citizen");

        String mayorName = rolesAndUsernames.get(mayor);
        String citizenName = rolesAndUsernames.get(ordinaryCitizen);

        //send message to mayor
        if (mayorName != null) {
            server.sendMessageToSpecifiecPlayer(mayorName, "start your act");
        }

        //send message to ordinary citizen
        if (citizenName != null) {
            server.sendMessageToSpecifiecPlayer(citizenName, "start your act");
        }

        //start acts of Mafia team
        //Find mafia team
        Role godFather = ManageData.getRole("godFather");
        Role ordinaryMafia = ManageData.getRole("ordinary Mafia");
        Role doctorLector = ManageData.getRole("Doctor lector");

        String godName = rolesAndUsernames.get(godFather);
        String doctorName = rolesAndUsernames.get(doctorLector);
        String ordinaryName = rolesAndUsernames.get(ordinaryMafia);

        //send message to Ordinary Mafia.
        if (ordinaryName != null) {
            server.sendMessageToSpecifiecPlayer(ordinaryName, "start your act");
            while (true) {
                answer = server.getMessageFromSpecifiecPlayer(ordinaryName);
                condition = checkCitizenTeam(answer);
                if (condition == true) {
                    if (godName == null && doctorName == null)
                        godFatherAct(answer);
                    else
                        mafiaVotes.add(answer);
                    server.sendMessageToSpecifiecPlayer(ordinaryName, "Ok");
                    condition = false;
                    break;
                } else if (condition == false) {
                    server.sendMessageToSpecifiecPlayer(ordinaryName, "unvalid input");
                }
            }
        }
        server.sendMessageToAll("The ordinary mafia did its job.");

        //send message to Doctor Lector.
        if (doctorName != null) {
            server.sendMessageToSpecifiecPlayer(doctorName, "start your act");
            //KILL
            while (true) {
                answer = server.getMessageFromSpecifiecPlayer(doctorName);
                condition = checkCitizenTeam(answer);
                if (condition == true) {
                    server.sendMessageToSpecifiecPlayer(doctorName, "Ok");
                    mafiaVotes.add(answer);
                    condition = false;
                    break;
                } else if (condition == false) {
                    server.sendMessageToSpecifiecPlayer(doctorName, "unvalid input");
                }
            }
            //HILL
            while (true) {
                answer = server.getMessageFromSpecifiecPlayer(doctorName);
                condition = checkMafiaTeam(answer);
                if (condition == true) {
                    if (godName == null)
                        godFatherAct(answer);
                    else
                        lectorAct(answer);
                    server.sendMessageToSpecifiecPlayer(doctorName, "Ok");
                    condition = false;
                    break;
                } else if (condition == false) {
                    server.sendMessageToSpecifiecPlayer(doctorName, "unvalid input");
                }
            }
        }
        server.sendMessageToAll("The doctor lector did its job");

        //send message to God Father.
        if (godName != null) {
            server.sendMessageToSpecifiecPlayer(godName, "start your act");
            server.sendMessageToSpecifiecPlayer(godName, "votes of your team mates : " + mafiaVotes);
            while (true) {
                answer = server.getMessageFromSpecifiecPlayer(godName);
                condition = checkCitizenTeam(answer);
                if (condition == true) {
                    godFatherAct(answer);
                    server.sendMessageToSpecifiecPlayer(godName, "Ok");
                    condition = false;
                    break;
                } else if (condition == false) {
                    server.sendMessageToSpecifiecPlayer(godName, "unvalid input");
                }
            }
        }
        server.sendMessageToAll("The god father did its job");

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
            //Hill
            while (true) {
                answer = server.getMessageFromSpecifiecPlayer(cityDoctor);
                condition = checkCitizenTeam(answer);
                if (condition == true) {
                    doctorAct(answer);
                    server.sendMessageToSpecifiecPlayer(cityDoctor, "Ok");
                    condition = false;
                    break;
                }
                condition = checkMafiaTeam(answer);
                if (condition == true) {
                    doctorAct(answer);
                    server.sendMessageToSpecifiecPlayer(cityDoctor, "Ok");
                    condition = false;
                    break;
                }
                if (condition == false) {
                    server.sendMessageToSpecifiecPlayer(cityDoctor, "unvalid input");
                }
            }
        }
        server.sendMessageToAll("The city doctor did its job");

        //send message to Detective
        if (detectiveName != null) {
            server.sendMessageToSpecifiecPlayer(detectiveName, "start your act");
            while (true) {
                answer = server.getMessageFromSpecifiecPlayer(detectiveName);
                condition = checkCitizenTeam(answer);
                if (condition == true) {
                    server.sendMessageToSpecifiecPlayer(detectiveName, "Ok");
                    condition = false;
                    break;
                }
                condition = checkMafiaTeam(answer);
                if (condition == true) {
                    server.sendMessageToSpecifiecPlayer(detectiveName, "Ok");
                    condition = false;
                    break;
                }
                if (condition == false)
                    server.sendMessageToSpecifiecPlayer(detectiveName, "Unvalid input");
            }
        }
        server.sendMessageToSpecifiecPlayer(detectiveName, answer + " : " + detectiveAct(answer));
        server.sendMessageToAll("The detective did its job");

        //send message to Sniper
        if (sniperName != null) {
            server.sendMessageToSpecifiecPlayer(sniperName, "start your act");
            while (true) {
                answer = server.getMessageFromSpecifiecPlayer(sniperName);
                condition = checkCitizenTeam(answer);
                if ((condition == true) || answer.equals("didn't want")) {
                    server.sendMessageToSpecifiecPlayer(sniperName, "Ok");
                    condition = false;
                    break;
                }
                condition = checkMafiaTeam(answer);
                if ((condition == true) || answer.equals(sniperName)) {
                    sniperAct(answer);
                    server.sendMessageToSpecifiecPlayer(sniperName, "Ok");
                    condition = false;
                    break;
                }
                server.sendMessageToSpecifiecPlayer(sniperName, "unvalid input");
            }
        }
        server.sendMessageToAll("The sniper did its job");

        //send message to Psychologist.
        if (psychologistName != null) {
            server.sendMessageToSpecifiecPlayer(psychologistName, "start your act");
            while (true) {
                answer = server.getMessageFromSpecifiecPlayer(psychologistName);
                if (answer.equals("didn't want")) {
                    server.sendMessageToSpecifiecPlayer(psychologistName, "Ok");
                    condition = false;
                    break;
                }
                condition = checkMafiaTeam(answer);
                if (condition == true) {
                    psychologistAct(answer);
                    server.sendMessageToSpecifiecPlayer(psychologistName, "Ok");
                    condition = false;
                    break;
                }
                condition = checkCitizenTeam(answer);
                if (condition == true) {
                    psychologistAct(answer);
                    server.sendMessageToSpecifiecPlayer(psychologistName, "Ok");
                    condition = false;
                    break;
                }
                server.sendMessageToSpecifiecPlayer(psychologistName, "Unvalid input");
            }
        }
        server.sendMessageToAll("The psychologist did its job");

        //send message to Die Hard.
        if (dieHardName != null) {
            server.sendMessageToSpecifiecPlayer(dieHardName, "start your act");
            answer = server.getMessageFromSpecifiecPlayer(dieHardName);
            if (answer.equals("yes"))
                setDieHardAct(true);
        }
        server.sendMessageToAll("The die hard did its job");
    }


    public void voting() {
        Set<Role> roleSet = rolesAndUsernames.keySet();
        ArrayList<String> votes = new ArrayList<String>();
        String vote = null;

        //Get valid vote from each player
        for (Role role : roleSet) {
            String name = rolesAndUsernames.get(role);
            server.sendMessageToSpecifiecPlayer(name, "player exist in the game : " + server.getUserNames());
            server.sendMessageToSpecifiecPlayer(name, "start voting");

            long start = System.currentTimeMillis();
            long end = start + 30 * 1000;

            while (System.currentTimeMillis() < end) {
                if (System.currentTimeMillis() >= end) {
                    vote = "Refrained";
                    break;
                }
                //Get vote from player
                vote = server.getMessageFromSpecifiecPlayer(name);
                //Check validity
                boolean condition = checkCitizenTeam(vote);
                if (condition == true) {
                    votes.add(vote);
                    server.sendMessageToSpecifiecPlayer(name, "Ok");
                    break;
                }
                //Check validity
                condition = checkMafiaTeam(vote);
                if (condition == true) {
                    votes.add(vote);
                    server.sendMessageToSpecifiecPlayer(name, "Ok");
                    break;
                }
                //Refrained votes.If after 30 seconds player
                if (vote.equals("Refrained")) {
                    server.sendMessageToSpecifiecPlayer(name, "Ok");
                    break;
                }
                server.sendMessageToSpecifiecPlayer(name, "unvalid input");
            }
            if (!vote.equals("Refrained"))
                server.sendMessageToAll(name + " voted for " + vote);
            else
                server.sendMessageToAll(name + " abstained");
        }
        //finish ask vote from players
        server.sendMessageToAll("finish voting");

        //Find mayor
        Role mayor = ManageData.getRole("Mayor");
        String mayorName = rolesAndUsernames.get(mayor);
        boolean cancel = false;

        //Get answer from [Mayor]
        if (mayorName != null) {
            String response = server.getMessageFromSpecifiecPlayer(mayorName);
            if (response.equals("yes"))
                cancel = true;
        }

        //if mayor didn't cancel voting
        if (cancel == false) {
            //Find how many votes each person has
            String kill = null;
            int[] votesCounter = new int[votes.size()];

            for (int i = 0; i < votes.size(); i++) {
                String target = votes.get(i);
                int counter = 0;
                for (int j = 0; j < votes.size(); j++) {
                    if (votes.get(j).equals(target))
                        counter++;
                }
                votesCounter[i] = counter;
            }

            //Check equals number
            boolean equality = false;
            outer:
            for (int i = 0;i < votesCounter.length;i++){
                int target = votesCounter[i];
                for (int j = 0;j < votesCounter.length;j++){
                    if ((votesCounter[j] == target) && (!votes.get(j).equals(votes.get(i)))){
                        equality = true;
                        break outer;
                    }
                }
            }

            if (equality == false){
                //Find largest number of votes
                int largest = votesCounter[0];
                int index = 0;
                for (int i = 1;i < votesCounter.length;i++){
                    if (votesCounter[i] > largest){
                        largest = votesCounter[i];
                        index = i;
                    }
                }
                server.sendMessageToAll(votes.get(index) + " is killed");
                voteKill(votes.get(index));
            }else if (equality == true){
                server.sendMessageToAll("No one will be removed due to an equal vote.");
            }
            //If mayor canceled voting
        } else if (cancel == true) {
            server.sendMessageToAll("The mayor canceled the vote");
        }
    }

    private boolean checkCitizenTeam(String name) {
        //Find roles
        Role mayor = ManageData.getRole("Mayor");
        Role sniper = ManageData.getRole("Sniper");
        Role psychologist = ManageData.getRole("Psychologist");
        Role ordinaryCitizen = ManageData.getRole("Ordinary Citizen");
        Role dieHard = ManageData.getRole("Die hard");
        Role detective = ManageData.getRole("Detective");
        Role doctor = ManageData.getRole("City doctor");

        //Find roles owners
        String mayorName = rolesAndUsernames.get(mayor);
        String sniperName = rolesAndUsernames.get(sniper);
        String psychologistName = rolesAndUsernames.get(psychologist);
        String ordinaryName = rolesAndUsernames.get(ordinaryCitizen);
        String dieHardName = rolesAndUsernames.get(dieHard);
        String detectiveName = rolesAndUsernames.get(detective);
        String doctorName = rolesAndUsernames.get(doctor);

        if (mayorName != null)
            if (mayorName.equals(name))
                return true;
        if (sniperName != null)
            if (sniperName.equals(name))
                return true;
        if (psychologistName != null)
            if (psychologistName.equals(name))
                return true;
        if (ordinaryName != null)
            if (ordinaryName.equals(name))
                return true;
        if (detectiveName != null)
            if (detectiveName.equals(name))
                return true;
        if (dieHardName != null)
            if (dieHardName.equals(name))
                return true;
        if (doctorName != null)
            if (doctorName.equals(name))
                return true;

        return false;
    }

    private boolean checkMafiaTeam(String name) {
        //Find roles
        Role godfather = ManageData.getRole("godFather");
        Role doctorLector = ManageData.getRole("Doctor lector");
        Role ordinaryMafia = ManageData.getRole("ordinary Mafia");

        //Find roles owners
        String godName = rolesAndUsernames.get(godfather);
        String doctorName = rolesAndUsernames.get(doctorLector);
        String ordinaryName = rolesAndUsernames.get(ordinaryMafia);

        if (godName != null)
            if (godName.equals(name))
                return true;
        if (doctorName != null)
            if (doctorName.equals(name))
                return true;
        if (ordinaryName != null)
            if (ordinaryName.equals(name))
                return true;

        return false;
    }

    private void godFatherAct(String kill) {
        Role role = null;
        Set<Role> roleSet = rolesAndUsernames.keySet();

        //Find role of player
        for (Role role1 : roleSet) {
            if (rolesAndUsernames.get(role1).equals(kill)) {
                role = role1;
                break;
            }
        }

        //Kill player
        if (role != null) {
            if (role.getName().equals("Die hard")) {
                DieHard dieHard = (DieHard) role;
                if (dieHard.getKillCounter() == 2) {
                    dieHard.setAlive(false);
                } else {
                    dieHard.increament();
                }
            } else {
                role.setAlive(false);
            }
        }
    }

    private void lectorAct(String hill) {
        Role role = null;
        Set<Role> roleSet = rolesAndUsernames.keySet();

        //Find role of player
        for (Role role1 : roleSet) {
            if (rolesAndUsernames.get(role1).equals(hill)) {
                role = role1;
                break;
            }
        }

        //Save player
        if (role != null) {
            if (role.getName().equals("Doctor lector")) {
                DoctorLector doctorLector = (DoctorLector) role;
                if (doctorLector.getSaveYourSelf() < 1) {
                    if (doctorLector.isAlive() == false) {
                        doctorLector.increament();
                        doctorLector.setAlive(true);
                    }
                }
            } else {
                if (role.isAlive() == false) {
                    role.setAlive(true);
                }
            }
        }
    }

    private void doctorAct(String hill) {
        Role role = null;
        Set<Role> roleSet = rolesAndUsernames.keySet();

        //Find role of player
        for (Role role1 : roleSet) {
            if (rolesAndUsernames.get(role1).equals(hill)) {
                role = role1;
                break;
            }
        }

        //Save player
        if (role != null) {
            if (role.getName().equals("City doctor")) {
                CityDoctor cityDoctor = (CityDoctor) role;
                if (cityDoctor.getSaveYourSelf() < 1) {
                    if (cityDoctor.isAlive() == false) {
                        cityDoctor.increament();
                        cityDoctor.setAlive(true);
                    }
                }
            } else {
                if (role.isAlive() == false) {
                    role.setAlive(true);
                }
            }
        }
    }

    private boolean detectiveAct(String name) {
        Role role = null;
        Set<Role> roleSet = rolesAndUsernames.keySet();
        boolean b;

        //Find role of player
        for (Role role1 : roleSet) {
            if (rolesAndUsernames.get(role1).equals(name)) {
                role = role1;
                break;
            }
        }

        b = role.isInquiry();
        return b;
    }

    private void sniperAct(String kill) {
        Role role = null;
        Set<Role> roleSet = rolesAndUsernames.keySet();

        //Find role of player
        for (Role role1 : roleSet) {
            if (rolesAndUsernames.get(role1).equals(kill)) {
                role = role1;
                break;
            }
        }

        //Kill player
        if (role != null) {
            role.setAlive(false);
        }
    }

    private void psychologistAct(String name) {
        Role role = null;
        Set<Role> roleSet = rolesAndUsernames.keySet();

        //Find role of player
        for (Role role1 : roleSet) {
            if (rolesAndUsernames.get(role1).equals(name)) {
                role = role1;
                break;
            }
        }

        if (role != null) {
            role.setCanSpeak(false);
        }
    }

    public void updateGame() {
        Set<Role> roleSet = rolesAndUsernames.keySet();
        Iterator<Role> it = roleSet.iterator();

        while (it.hasNext()) {
            Role role = it.next();
            if (role.isAlive() == false) {
                String name = rolesAndUsernames.get(role);
                rolesAndUsernames.remove(role, name);
                deads.put(role, name);
            }
        }

        Set<Role> roles = rolesAndUsernames.keySet();
        System.out.println("roles and usernames : ");
        for (Role role : roles) {
            System.out.println(role.getName() + " : " + rolesAndUsernames.get(role));
        }
    }

    public void voteKill(String kill){
        Set<Role> roleSet = rolesAndUsernames.keySet();
        Role killvote = null;

        //Find role of player
        for (Role role : roleSet){
            if (rolesAndUsernames.get(role).equals(kill)){
                killvote = role;
                break;
            }
        }

        if (killvote != null){
            System.out.println(kill + " killed");
            rolesAndUsernames.remove(killvote, kill);
        }

        System.out.println(rolesAndUsernames);
    }

    public HashMap<Role, String> getDeads() {
        return deads;
    }


    public void clearDeads() {
        deads.clear();
    }

}
