package com.company.Game;

import com.company.characters.CityDoctor;
import com.company.characters.DieHard;
import com.company.characters.DoctorLector;
import com.company.characters.Role;
import com.company.server.Server;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * this class include some methods that execute main parts of game
 * such as first night, night, day, voting, check end condition.
 * also include some private methods that execute act of each role.
 * methods of this class call from server class.
 * also this class include a hashmap that specifies each player has which role.
 *
 * @author  mahmood-saneian
 * @since   2021-6-15
 * @version 15.0.2
 */
public class GameLoop {
    //the server of game
    private Server server;
    //specifies each player has which role
    private HashMap<Role, String> rolesAndUsernames;
    //contains players that died at game night
    private HashMap<Role, String> deads;
    //specifies that die hard wants get inquiry about roles that leave from game or no
    private boolean dieHardAct;
    //contains name of roles that died at game night
    private ArrayList<String> outs;

    /**
     * this constructor get server of game and allocate memory for
     * lists & hashmaps.also init boolean of diehard act.
     * @param server the server of game.
     */
    public GameLoop(Server server) {
        this.server = server;
        rolesAndUsernames = new HashMap<Role, String>();
        dieHardAct = false;
        deads = new HashMap<Role, String>();
        outs = new ArrayList<String>();
    }

    /**
     * this method get a boolean and set to die hard act.
     * @param dieHardAct is tru or false.
     */
    public void setDieHardAct(boolean dieHardAct) {
        this.dieHardAct = dieHardAct;
    }

    /**
     * this method get a hashmap that include name of players & name of roles
     * in the game.then create a new hashmap that include name of player & player's
     * role in the game.
     * @param nameRolesAndUsernames the hashmap that include name of player and name of exists role in the game.
     */
    public void initHashMap(HashMap<String, String> nameRolesAndUsernames) {
        Set<String> nameRoles = nameRolesAndUsernames.keySet();

        for (String name : nameRoles) {
            Role role = ManageData.getRole(name);
            String roleOwner = nameRolesAndUsernames.get(name);
            rolesAndUsernames.put(role, roleOwner);
        }

        ManageData.setRolesAndNames(rolesAndUsernames);
    }

    /**
     * this method get a hashmap that include information about
     * each player's role & set to rolesAndUsernames.
     * @param r the hashmap that include information about each player's role.
     */
    public void setRolesAndUsernames(HashMap<String, String> r){
        initHashMap(r);
    }

    /**
     * this method clear hashmap of deads players.
     * use after game night.
     */
    public void clearDeads() {
        deads.clear();
    }

    /**
     * this method manages first night of game.
     * sends suitable message to each player.
     * at first night of game , mafia's members know each other
     * and also mayor knows city of doctor.
     * then sends message to other players.
     */
    public void firstNight() {
        String name    = null;
        String message = null;

        //Introducing Mafia members to each other
        //Find Mafia Members
        Role godFather     = ManageData.getRole("godFather");
        Role ordinaryMafia = ManageData.getRole("ordinary Mafia");
        Role doctorlector  = ManageData.getRole("Doctor lector");

        //Introduction to god father
        name    = rolesAndUsernames.get(godFather);
        message = "your teammates in game are : " + rolesAndUsernames.get(doctorlector)
                  +" - " + rolesAndUsernames.get(ordinaryMafia) + "\n"
                  + rolesAndUsernames.get(doctorlector) + " is a doctor lector\n"
                  + rolesAndUsernames.get(ordinaryMafia) + " is an ordinary mafia"
                  +"You have to work together to defeat the citizens team members";
        if (name != null) {
            server.sendMessageToSpecifiecPlayer(name, message);
            name = null;
        }

        //Introduction to doctor lector
        name    = rolesAndUsernames.get(doctorlector);
        message = "your teammates in game are : " + rolesAndUsernames.get(godFather)
                  +"-" + rolesAndUsernames.get(ordinaryMafia) + "\n"
                  + rolesAndUsernames.get(godFather) + " is a god father \n"
                  + rolesAndUsernames.get(ordinaryMafia) + " is an ordinary mafia"
                  +"You have to work together to defeat the citizens team members";
        if (name != null) {
            server.sendMessageToSpecifiecPlayer(name, message);
            name = null;
        }

        //Introduction to ordinary mafia
        name    = rolesAndUsernames.get(ordinaryMafia);
        message = "your teammates in game are : " + rolesAndUsernames.get(godFather)
                + " - " + rolesAndUsernames.get(doctorlector) + "\n"
                + rolesAndUsernames.get(godFather) + " is a god father \n"
                + rolesAndUsernames.get(doctorlector) + " is a doctor lector" + "\n"
                +"You have to work together to defeat the citizens team members";
        if (name != null) {
            server.sendMessageToSpecifiecPlayer(name, message);
            name = null;
        }

        //Introducing city doctor to mayor
        //Find city doctor & mayor
        Role cityDoctor = ManageData.getRole("City doctor");
        Role mayor      = ManageData.getRole("Mayor");
        //Send message to Mayor
        name    = rolesAndUsernames.get(mayor);
        message = "city doctor is : " + rolesAndUsernames.get(cityDoctor);
        if (name != null) {
            server.sendMessageToSpecifiecPlayer(name, message);
            name = null;
        }

        //Send message to other roles
        //Find other Citizen's Members
        Role psychologist    = ManageData.getRole("Psychologist");
        Role sniper          = ManageData.getRole("Sniper");
        Role ordinaryCitizen = ManageData.getRole("Ordinary Citizen");
        Role diehard         = ManageData.getRole("Die hard");
        Role detective       = ManageData.getRole("Detective");
        message = "You and the rest of the citizens' team must "
                 +"try to identify the Mafia and defeat them";

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

        //Send [End message] to players
        server.sendMessageToAll("finish first night");
    }

    /**
     * this method manages game night.
     * in order, sends messages to players & receives messages from then.
     * first sends message to mayor & ordinary citizen.
     * then , in order sends messages to :
     * ordinary mafia, doctor lector, godfather, city doctor, detective, sniper, psychologist, die hard.
     * then send messages to persons who killed or saved during the night.
     */
    public void nightGame() {
        //Declare variables
        boolean condition = false;
        String  answer    = "";
        ArrayList<String> mafiaVotes = new ArrayList<String>();

        String lectorHill   = null;
        String mafiaKill    = null;
        String doctorHill   = null;
        String sniperKill   = null;
        String silentPerson = null;

        //Send message to players [ Start Night]
        server.sendMessageToAll("The night has started and the game server will move forward. " +
                "Wait until the night is completely over");

        //print exist players to player
        server.sendMessageToAll("exist player in the game : " + server.getUserNames());

        //Those who do not do anything special at night.[Mayor & ordinaryCitizen]
        Role mayor           = ManageData.getRole("Mayor");
        Role ordinaryCitizen = ManageData.getRole("Ordinary Citizen");

        String mayorName   = rolesAndUsernames.get(mayor);
        String citizenName = rolesAndUsernames.get(ordinaryCitizen);

        //Send message to [Mayor]
        if (mayorName != null) {
            server.sendMessageToSpecifiecPlayer(mayorName, "start your act");
        }

        //Send message to [Ordinary citizen]
        if (citizenName != null) {
            server.sendMessageToSpecifiecPlayer(citizenName, "start your act");
        }

        //start acts of Mafia team
        //Find mafia team
        Role godFather     = ManageData.getRole("godFather");
        Role ordinaryMafia = ManageData.getRole("ordinary Mafia");
        Role doctorLector  = ManageData.getRole("Doctor lector");

        String godName      = rolesAndUsernames.get(godFather);
        String doctorName   = rolesAndUsernames.get(doctorLector);
        String ordinaryName = rolesAndUsernames.get(ordinaryMafia);

        //Send message to [Ordinary Mafia]
        if (ordinaryName != null) {
            server.sendMessageToSpecifiecPlayer(ordinaryName, "start your act");
            while (true) {
                //Get answer from player
                answer = server.getMessageFromSpecifiecPlayer(ordinaryName);
                //Check validity
                condition = checkCitizenTeam(answer);
                if (condition == true) {
                    if ((godName == null) && (doctorName == null)) {
                        mafiaAct(answer);
                        mafiaKill = answer;
                    } else{
                        mafiaVotes.add(answer);
                    }
                    server.sendMessageToSpecifiecPlayer(ordinaryName, "Ok");
                    condition = false;
                    break;
                } else if (condition == false) {
                    server.sendMessageToSpecifiecPlayer(ordinaryName, "unalid input");
                }
            }
        }
        server.sendMessageToAll("The ordinary mafia did its job.");

        //Send message to [Doctor Lector]
        if (doctorName != null) {
            server.sendMessageToSpecifiecPlayer(doctorName, "start your act");
            //KILL
            while (true) {
                //Get answer from player
                answer = server.getMessageFromSpecifiecPlayer(doctorName);
                //Check validity
                condition = checkCitizenTeam(answer);
                if (condition == true) {
                    if (godName == null) {
                        mafiaAct(answer);
                        mafiaKill = answer;
                    } else{
                        mafiaVotes.add(answer);
                    }
                    server.sendMessageToSpecifiecPlayer(doctorName, "Ok");
                    condition = false;
                    break;
                }
                if (condition == false) {
                    server.sendMessageToSpecifiecPlayer(doctorName, "unvalid input");
                }
            }
            //HILL
            while (true) {
                //Get answer from player
                answer = server.getMessageFromSpecifiecPlayer(doctorName);
                //Check validity
                condition = checkMafiaTeam(answer);
                if (condition == true) {
                    lectorHill = answer;
                    server.sendMessageToSpecifiecPlayer(doctorName, "Ok");
                    condition = false;
                    break;
                } else if (condition == false) {
                    server.sendMessageToSpecifiecPlayer(doctorName, "unvalid input");
                }
            }
        }
        server.sendMessageToAll("The doctor lector did its job");

        //Send message to [God Father]
        if (godName != null) {
            server.sendMessageToSpecifiecPlayer(godName, "start your act");
            //Send votes of [ordinary mafia & doctor lector] to god father.
            server.sendMessageToSpecifiecPlayer(godName, "votes of your team mates : " + mafiaVotes);
            while (true) {
                //Get answer from player
                answer = server.getMessageFromSpecifiecPlayer(godName);
                //Check validity
                condition = checkCitizenTeam(answer);
                if (condition == true) {
                    mafiaKill = answer;
                    mafiaAct(answer);
                    server.sendMessageToSpecifiecPlayer(godName, "Ok");
                    condition = false;
                    break;
                } else if (condition == false) {
                    server.sendMessageToSpecifiecPlayer(godName, "unvalid input");
                }
            }
        }
        server.sendMessageToAll("The god father did its job");

        //Start acts of Citizen team
        //Find members of Citizen Team
        Role doctor       = ManageData.getRole("City doctor");
        Role detective    = ManageData.getRole("Detective");
        Role sniper       = ManageData.getRole("Sniper");
        Role psychologist = ManageData.getRole("Psychologist");
        Role dieHard      = ManageData.getRole("Die hard");

        String cityDoctor       = rolesAndUsernames.get(doctor);
        String detectiveName    = rolesAndUsernames.get(detective);
        String sniperName       = rolesAndUsernames.get(sniper);
        String psychologistName = rolesAndUsernames.get(psychologist);
        String dieHardName      = rolesAndUsernames.get(dieHard);

        //Send message to [City Doctor]
        if (cityDoctor != null) {
            server.sendMessageToSpecifiecPlayer(cityDoctor, "start your act");
            //Hill
            while (true) {
                //Get answer from player
                answer = server.getMessageFromSpecifiecPlayer(cityDoctor);
                //Check validity
                condition = checkCitizenTeam(answer);
                if (condition == true) {
                    doctorAct(answer);
                    doctorHill = answer;
                    server.sendMessageToSpecifiecPlayer(cityDoctor, "Ok");
                    condition  = false;
                    break;
                }
                //Check validity
                condition = checkMafiaTeam(answer);
                if (condition == true) {
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

        //Send message to [Detective]
        if (detectiveName != null) {
            server.sendMessageToSpecifiecPlayer(detectiveName, "start your act");
            while (true) {
                //Get answer from player
                answer = server.getMessageFromSpecifiecPlayer(detectiveName);
                //Check validity
                condition = checkCitizenTeam(answer);
                if (condition == true) {
                    server.sendMessageToSpecifiecPlayer(detectiveName, "Ok");
                    condition = false;
                    break;
                }
                //Check validity
                condition = checkMafiaTeam(answer);
                if (condition == true) {
                    server.sendMessageToSpecifiecPlayer(detectiveName, "Ok");
                    condition = false;
                    break;
                }
                if (condition == false)
                    server.sendMessageToSpecifiecPlayer(detectiveName, "unvalid input");
            }
            server.sendMessageToSpecifiecPlayer(detectiveName, answer + " : " + detectiveAct(answer));
        }
        server.sendMessageToAll("The detective did its job");

        //send message to [Sniper]
        if (sniperName != null) {
            server.sendMessageToSpecifiecPlayer(sniperName, "start your act");
            while (true) {
                //Get answer from player
                answer    = server.getMessageFromSpecifiecPlayer(sniperName);
                //Check validity
                condition = checkCitizenTeam(answer);
                if (condition == true) {
                    sniperKill = sniperName;
                    sniperAct(sniperName);
                    server.sendMessageToSpecifiecPlayer(sniperName, "Ok");
                    condition = false;
                    break;
                }
                //Check validity
                condition = checkMafiaTeam(answer);
                if ((condition == true)) {
                    sniperKill = answer;
                    sniperAct(answer);
                    if (lectorHill != null)
                        lectorAct(lectorHill);
                    server.sendMessageToSpecifiecPlayer(sniperName, "Ok");
                    condition = false;
                    break;
                }
                if (answer.equals("didn't want")) {
                    server.sendMessageToSpecifiecPlayer(sniperName, "Ok");
                    condition = false;
                    break;
                }
                server.sendMessageToSpecifiecPlayer(sniperName, "unvalid input");
            }
        }
        server.sendMessageToAll("The sniper did its job");

        //Send message to [Psychologist]
        if (psychologistName != null) {
            server.sendMessageToSpecifiecPlayer(psychologistName, "start your act");
            while (true) {
                //Get answer from player
                answer = server.getMessageFromSpecifiecPlayer(psychologistName);
                if (answer.equals("didn't want")) {
                    server.sendMessageToSpecifiecPlayer(psychologistName, "Ok");
                    condition = false;
                    break;
                }
                //Check validity
                condition = checkMafiaTeam(answer);
                if (condition == true) {
                    silentPerson = answer;
                    psychologistAct(answer);
                    server.sendMessageToSpecifiecPlayer(psychologistName, "Ok");
                    condition = false;
                    break;
                }
                //Check validity
                condition = checkCitizenTeam(answer);
                if (condition == true) {
                    silentPerson = answer;
                    psychologistAct(answer);
                    server.sendMessageToSpecifiecPlayer(psychologistName, "Ok");
                    condition = false;
                    break;
                }
                server.sendMessageToSpecifiecPlayer(psychologistName, "unvalid input");
            }
        }
        server.sendMessageToAll("The psychologist did its job");

        //Send message to [Die Hard]
        if (dieHardName != null) {
            server.sendMessageToSpecifiecPlayer(dieHardName, "start your act");
            //Get answer from player
            answer = server.getMessageFromSpecifiecPlayer(dieHardName);
            if (answer.equals("yes"))
                setDieHardAct(true);
        }
        server.sendMessageToAll("The die hard did its job");

        //Send message to deads and saved pesrons
        if (sniperKill   != null)
            server.sendMessageToSpecifiecPlayer(sniperKill  , "kill");
        if (mafiaKill    != null)
            server.sendMessageToSpecifiecPlayer(mafiaKill   , "kill");
        if (lectorHill   != null)
            server.sendMessageToSpecifiecPlayer(lectorHill  , "hill");
        if (doctorHill   != null)
            server.sendMessageToSpecifiecPlayer(doctorHill  , "hill");
        if (silentPerson != null)
            server.sendMessageToSpecifiecPlayer(silentPerson, "silent");

        //Send message to players
        server.sendMessageToAll("night finished");
    }

    /**
     * this method manages game day.
     * at first, prints name of players that exist in the game.
     * then , prints information about last game night.
     * then , if die hard wants, print name of roles that exit from game.
     * then, start chat room for 5 minutes.
     */
    public void day() {
        //send message of start day phase.
        server.sendMessageToAll("Game day has just begun");

        //send name of exist players to each player.
        String serverMessage = "exist player in the game : " + server.getUserNames();
        server.sendMessageToAll(serverMessage);

        //Send name of players who dead last night.
        Set<Role> roleSet = deads.keySet();
        String lastNightMessage;
        if (roleSet.size() == 0) {
            lastNightMessage = "No one left the game last night";
        } else {
            lastNightMessage = "Those who left the game last night : ";
            for (Role role : roleSet) {
                lastNightMessage += deads.get(role) + " - ";
            }
        }
        server.sendMessageToAll(lastNightMessage);

        //Send roles that are out of the game if die hard wants.
        if (dieHardAct == true) {
            serverMessage = "Roles that are out of the game : " + outs;
            server.sendMessageToAll(serverMessage);
            dieHardAct = false;
        }

        server.sendMessageToAll("start chat");
        server.chatRoom();
        try {
            Thread.sleep(300000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        server.storeMessages();
        clearDeads();
    }

    /**
     * this method manages voting of game.
     * at first , get valid vote from each player.
     * then , ask from mayor for cancel voting or no.
     * then , if mayor doesn't cancel voting , counts votes &
     * kill a person that has most votes.
     */
    public void voting() {
        Set<Role> roleSet       = rolesAndUsernames.keySet();
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
                //Refrained votes.If the player does not vote after 30 seconds.
                if (vote.equals("Refrained")) {
                    server.sendMessageToSpecifiecPlayer(name, "Ok");
                    break;
                }
                server.sendMessageToSpecifiecPlayer(name, "unvalid input");
            }
            //Show player's vote to other players
            if (!vote.equals("Refrained"))
                server.sendMessageToAll(name + " voted for " + vote);
            else
                server.sendMessageToAll(name + " abstained");
        }
        //finish ask vote from players
        server.sendMessageToAll("finish voting");

        //Find mayor
        Role    mayor       = ManageData.getRole("Mayor");
        String  mayorName   = rolesAndUsernames.get(mayor);
        boolean cancel      = false;

        //Get answer from [Mayor]
        if (mayorName != null) {
            String response = server.getMessageFromSpecifiecPlayer(mayorName);
            if (response.equals("yes"))
                cancel = true;
        }

        //if mayor didn't cancel voting
        if (cancel == false) {
            //Find how many votes each person has
            int[] votesCounter = new int[votes.size()];

            //Counting the votes
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
            for (int i = 0; i < votesCounter.length; i++) {
                int target = votesCounter[i];
                for (int j = 0; j < votesCounter.length; j++) {
                    if ((votesCounter[j] == target) && (!votes.get(j).equals(votes.get(i)))) {
                        equality = true;
                        break outer;
                    }
                }
            }

            if (equality == false) {
                //Find largest number of votes
                int largest = votesCounter[0];
                int index = 0;
                for (int i = 1; i < votesCounter.length; i++) {
                    if (votesCounter[i] > largest) {
                        largest = votesCounter[i];
                        index = i;
                    }
                }
                //Kill player
                server.sendMessageToAll(votes.get(index) + " is killed");
                server.sendMessageToSpecifiecPlayer(votes.get(index), "kill");
                server.sendMessageToSpecifiecPlayer(votes.get(index),"finish voting");
                voteKill(votes.get(index));
            } else if (equality == true) {
                server.sendMessageToAll("No one will be removed due to an equal vote.");
            }
            //If mayor canceled voting
        } else if (cancel == true) {
            server.sendMessageToAll("The mayor canceled the vote");
        }

        //Send [End message] to players
        server.sendMessageToAll("finish voting");
        System.out.println("after voting. exist players : "+ server.getUserNames());
    }

    /*
     * this is a private method that get a name and
     * search between citizen's members.
     * if this name exists in the citizen team return true
     * otherwise return false.
     */
    private boolean checkCitizenTeam(String name) {
        //Find roles
        Role mayor           = ManageData.getRole("Mayor");
        Role sniper          = ManageData.getRole("Sniper");
        Role psychologist    = ManageData.getRole("Psychologist");
        Role ordinaryCitizen = ManageData.getRole("Ordinary Citizen");
        Role dieHard         = ManageData.getRole("Die hard");
        Role detective       = ManageData.getRole("Detective");
        Role doctor          = ManageData.getRole("City doctor");

        //Find roles owners
        String mayorName        = rolesAndUsernames.get(mayor);
        String sniperName       = rolesAndUsernames.get(sniper);
        String psychologistName = rolesAndUsernames.get(psychologist);
        String ordinaryName     = rolesAndUsernames.get(ordinaryCitizen);
        String dieHardName      = rolesAndUsernames.get(dieHard);
        String detectiveName    = rolesAndUsernames.get(detective);
        String doctorName       = rolesAndUsernames.get(doctor);

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

    /*
     * this is a private method that get a name and
     * search between mafia's members.
     * if this name exists in the mafia team return true
     * otherwise return false.
     */
    private boolean checkMafiaTeam(String name) {
        //Find roles
        Role godfather     = ManageData.getRole("godFather");
        Role doctorLector  = ManageData.getRole("Doctor lector");
        Role ordinaryMafia = ManageData.getRole("ordinary Mafia");

        //Find roles owners
        String godName      = rolesAndUsernames.get(godfather);
        String doctorName   = rolesAndUsernames.get(doctorLector);
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

    /*
     * this is a private method that do mafia act.
     * get a name and kill it.
     */
    private void mafiaAct(String kill) {
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
                if (dieHard.getKillCounter() >= 2) {
                    dieHard.setAlive(false);
                } else {
                    dieHard.increamentKillCounter();
                }
            } else {
                role.setAlive(false);
            }
        }
    }

    /*
     * this is a private method that do doctor lector act
     * get a name and save it.
     */
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

    /*
     * this is a private method that do city doctor act
     * get a name and save it.
     */
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

    /*
     * this is a private method that do detective act.
     * get a name and return response of it's inquiry.
     */
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

    /*
     * this is a private method that do sniper act.
     * get a name and kill it.
     */
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

    /*
     * this is a private method that do psychologist act.
     * get a name and silent it.
     */
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

    /*
     * this is a private method that use after voting.
     * get a name [that achieved most votes] and kill it.
     */
    private void voteKill(String kill) {
        Set<Role> roleSet = rolesAndUsernames.keySet();
        Role killvote = null;

        //Find role of player
        for (Role role : roleSet) {
            if (rolesAndUsernames.get(role).equals(kill)) {
                killvote = role;
                break;
            }
        }

        if (killvote != null) {
            System.out.println(kill + " is killed");
            rolesAndUsernames.remove(killvote, kill);
            server.removeUser(kill);
            outs.add(killvote.getName());
        }
    }

    /**
     * this method update game.
     * remove players who died during night from main hashmap of game
     * and add them into deads hashmap.
     */
    public void updateGame() {
        Set<Role> roleSet = rolesAndUsernames.keySet();
        //Find died players
        for (Role role : roleSet) {
            if (role.isAlive() == false) {
                deads.put(role, rolesAndUsernames.get(role));
                outs.add(role.getName());
            }
        }

        //Remove died players from game
        Set<Role> setDeads = deads.keySet();
        for (Role role : setDeads) {
            rolesAndUsernames.remove(role, deads.get(role));
            server.removeUser(deads.get(role));
        }
    }

    /**
     * this method gets a name and remove it from main hashmap of game.
     * @param name the name of player who we want remove it.
     */
    public void removePlayer(String name) {
        Role role = null;
        Set<Role> roleSet = rolesAndUsernames.keySet();

        //Find role of player
        for (Role role1 : roleSet) {
            if (rolesAndUsernames.get(role1).equals(name)) {
                role = role1;
                break;
            }
        }

        //Remove player
        if (role != null){
            rolesAndUsernames.remove(role, name);
            outs.add(role.getName());
        }

    }

    /**
     * this method checks condition of end game.
     * @return true[If game must be end] or false
     */
    public boolean checkEndCondition(){
        int mafiaCounter = 0;
        int citizenCounter = 0;

        Set<Role> roleSet = rolesAndUsernames.keySet();
        //Find number of mafia team & citizen team
        for (Role role : roleSet){
            if ((role.getName().equals("City doctor")) || (role.getName().equals("Detective"))
                    || (role.getName().equals("Sniper")) || (role.getName().equals("Ordinary Citizen"))
                    || (role.getName().equals("Mayor")) || (role.getName().equals("Psychologist")) || (role.getName().equals("Die hard"))){
                citizenCounter++;
            }
            if ((role.getName().equals("godFather")) || (role.getName().equals("Doctor lector")) || (role.getName().equals("ordinary Mafia"))){
                mafiaCounter++;
            }
        }

        if (mafiaCounter == 0){
            System.out.println("Citizen team Win");
            return true;
        }

        if (mafiaCounter >= citizenCounter){
            System.out.println("Mafia team win");
            return true;
        }
        return false;
    }
}
