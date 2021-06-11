package com.company.Game;

import java.util.Scanner;

public class NightGame {

    private static Scanner scanner = new Scanner(System.in);

    public static String mafiaAct() {
       System.out.println("Who do you want to kill?");
       String kill = scanner.nextLine();
       return kill;
    }

    public static String doctor() {
        System.out.println("Which of your teammates do you want to save?");
        String hill = scanner.nextLine();
        return hill;
    }

    public static String detective() {
        System.out.println("Who do you want to inquire about?");
        String inquiry = scanner.nextLine();
        return inquiry;
    }

    public static String sniper() {
        System.out.println("Do you want to use your role?");
        String answer = scanner.nextLine();
        String kill = "";
        if (answer.equals("yes")){
            System.out.println("Who do you want to kill?");
            kill = scanner.nextLine();
        }else if (answer.equals("no")){
            kill = "didn't want";
        }
        return kill;
    }

    public static String psychologist() {
        System.out.println("Do you want to use your role?");
        String answer = scanner.nextLine();
        String silent = "";
        if (answer.equals("yes")){
            System.out.println("who do you want to silent it?");
            silent = scanner.nextLine();
        }else if (answer.equals("no")){
            silent = "didn't want";
        }
        return silent;
    }

    public static String dieHard() {
        String answer;
        while (true){
            System.out.println("Do you want to use your role?");
            answer = scanner.nextLine();
            if (answer.equals("yes") || answer.equals("no"))
                break;
        }
        return answer;
    }

//    public HashMap<Role, String> updateGame() {
//        HashMap<Role, String> deads = new HashMap<Role, String>();
//
//        Set<Role> roles = rolesAndNames.keySet();
//
//        for (Role role : roles) {
//            if (!(role.isAlive())) {
//                String name = rolesAndNames.get(role);
//                deads.put(role, name);
//                rolesAndNames.remove(role, name);
//            }
//        }
//        return deads;
//    }

//    public HashMap<Role, String> getRolesAndNames() {
//        return rolesAndNames;
//    }
}
