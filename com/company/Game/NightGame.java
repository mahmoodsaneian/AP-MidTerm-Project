package com.company.Game;

import com.company.characters.DieHard;
import com.company.characters.Role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class NightGame {

    private static ArrayList<String> mafiavotes = new ArrayList<String>();
    private static Scanner scanner = new Scanner(System.in);
    private static HashMap<Role, String> rolesAndNames = ManageData.getRolesAndNames();

    public static void lectorAndOrdinaryMafia() {
        String kill = "";
        boolean truth = false;
        while (true) {
            System.out.println("Who are you going to kill?");
            kill = scanner.nextLine();
            truth = checkCitizenTeam(kill);
            if (truth == true)
                break;
            else
                System.out.println("unvalid input.please try again.");
        }
        mafiavotes.add(kill);
    }

    public static void godFather() {
        String kill = "";
        boolean truth = false;
        while (true) {
            System.out.println("votes of your teammates : " + mafiavotes + "\n" +
                    "Who are you going to kill?");
            kill = scanner.nextLine();
            truth = checkCitizenTeam(kill);
            if (truth == true)
                break;
            else
                System.out.println("unvalid input.please try again.");
        }
        //find player
        Role role = null;
        Set<Role> roles = rolesAndNames.keySet();
        for (Role role1 : roles) {
            if (rolesAndNames.get(role1).equals(kill)) {
                role = role1;
                break;
            }
        }
        //kill player[citizen]
        role.setAlive(false);
    }

    public static void doctor(String teamName) {
        String save = "";
        boolean truth = false;
        while (true) {
            System.out.println("Which of your teammates do you want to save?");
            save = scanner.nextLine();
            if (teamName.equals("Mafia")) {
                truth = checkMafiaTeam(save);
                if (truth == true)
                    break;
                else
                    System.out.println("unvalid input.please try again.");
            } else if (teamName.equals("Citizen")) {
                truth = checkCitizenTeam(save);
                if (truth == true)
                    break;
                else
                    System.out.println("unvalid input.please try again.");
            }
        }
        //find player
        Role role = null;
        Set<Role> roles = rolesAndNames.keySet();
        for (Role role1 : roles) {
            if (rolesAndNames.get(role1).equals(save)) {
                role = role1;
                break;
            }
        }
        if (!(role.isAlive()))
            role.setAlive(true);
    }

    public static void detective() {
        String inquiry = "";
        boolean truth = false;
        while (true) {
            System.out.println("Who do you want to inquire about?");
            inquiry = scanner.nextLine();
            truth = checkCitizenTeam(inquiry);
            if (truth == true)
                break;
            else
                System.out.println("unvalid input.please try again.");
            truth = checkMafiaTeam(inquiry);
            if (truth == true)
                break;
            else
                System.out.println("unvalid input.please try again.");
        }

        Role role = null;
        Set<Role> roles = rolesAndNames.keySet();
        for (Role role1 : roles) {
            if (rolesAndNames.get(role1).equals(inquiry)) {
                role = role1;
                break;
            }
        }
        if (role.isInquiry())
            System.out.println("Yes");
        else if (!role.isInquiry())
            System.out.println("No");
    }

    public static void sniper() {
        String kill = "";
        String shoot = "";
        System.out.println("Do you want to use your role?");
        String answer = scanner.nextLine();
        if (answer.equals("yes")) {
            System.out.println("Who are you going to kill?");
            kill = scanner.nextLine();
            boolean truth = checkMafiaTeam(kill);
            if (truth)
                shoot = "shoot correctly";
            else
                shoot = "wrong shooting";
        } else {
            shoot = "not shoot";
        }

        Role role = null;
        Set<Role> roles = rolesAndNames.keySet();

        if (shoot.equals("shoot correctly")) {
            for (Role role1 : roles) {
                if (rolesAndNames.get(role1).equals(kill)) {
                    role = role1;
                    break;
                }
            }
        } else if (shoot.equals("wrong shooting")) {
            for (Role role1 : roles) {
                if (role1.getName().equals("Sniper")) {
                    role = role1;
                    break;
                }
            }
        }
        role.setAlive(false);
    }

    public static void psychologist() {
        System.out.println("Do you want to use your role?.write yes or no");
        String answer = scanner.nextLine();
        if (answer.equals("yes")) {
            String silence = "";
            boolean truth = false;
            while (true) {
                System.out.println("Who do you want to silence?");
                silence = scanner.nextLine();
                truth = checkCitizenTeam(silence);
                if (truth == true)
                    break;
                else
                    System.out.println("unvalid input.please try again.");
                truth = checkMafiaTeam(silence);
                if (truth == true)
                    break;
                else
                    System.out.println("unvalid input.please try again.");
            }
            //find player
            Role role = null;
            Set<Role> roles = rolesAndNames.keySet();
            for (Role role1 : roles) {
                if (rolesAndNames.get(role1).equals(silence)) {
                    role = role1;
                    break;
                }
            }
            //silence player
            role.setCanSpeak(false);
        }
    }

    public static String dieHard() {
        System.out.println("Do you want to use your role?.write yes or no");
        String answer = scanner.nextLine();
        DieHard dieHard = (DieHard) ManageData.getRole("Die hard");
        if (answer.equals("yes")) {
            int counter = dieHard.getCounterInquiry();
            if (counter == 2) {
                System.out.println("You can no longer query.");
                answer = "No";
            } else {
                dieHard.increament();
            }
        }
        return answer;
    }

    private static boolean checkMafiaTeam(String name) {
        Role godfather = ManageData.getRole("godFather");
        Role doctorLector = ManageData.getRole("Doctor lector");
        Role ordinaryMafia = ManageData.getRole("ordinary Mafia");

        String godName = rolesAndNames.get(godfather);
        String doctorName = rolesAndNames.get(doctorLector);
        String ordinaryName = rolesAndNames.get(ordinaryMafia);

        if (godName.equals(name) || doctorName.equals(name) || ordinaryName.equals(name))
            return true;
        return false;
    }

    private static boolean checkCitizenTeam(String name) {
        Role mayor = ManageData.getRole("Mayor");
        Role sniper = ManageData.getRole("Sniper");
        Role psychologist = ManageData.getRole("Psychologist");
        Role ordinaryCitizen = ManageData.getRole("Ordinary Citizen");
        Role dieHard = ManageData.getRole("Die hard");
        Role detective = ManageData.getRole("Detective");
        Role doctor = ManageData.getRole("City doctor");

        String mayorName = rolesAndNames.get(mayor);
        String sniperName = rolesAndNames.get(sniper);
        String psychologistName = rolesAndNames.get(psychologist);
        String ordinaryName = rolesAndNames.get(ordinaryCitizen);
        String dieHardName = rolesAndNames.get(dieHard);
        String detectiveName = rolesAndNames.get(detective);
        String doctorName = rolesAndNames.get(doctor);

        if (mayorName.equals(name) || sniperName.equals(name) || psychologistName.equals(name))
            return true;
        if (ordinaryName.equals(name) || dieHardName.equals(name))
            return true;
        if (detectiveName.equals(name) || doctorName.equals(name))
            return true;
        return false;
    }

    public static HashMap<Role, String> updateGame() {
        HashMap<Role, String> deads = new HashMap<Role, String>();

        Set<Role> roles = rolesAndNames.keySet();

        for (Role role : roles){
            if (!(role.isAlive())){
                String name = rolesAndNames.get(role);
                deads.put(role, name);
                rolesAndNames.remove(role, name);
            }
        }

        return deads;
    }
}
