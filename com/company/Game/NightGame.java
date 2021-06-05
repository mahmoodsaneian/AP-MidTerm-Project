package com.company.Game;

import java.util.ArrayList;
import java.util.Scanner;

public class NightGame {
    private static ArrayList<String> mafiavotes;
    private static Scanner scanner;

    public NightGame() {
        mafiavotes = new ArrayList<String>();
        scanner = new Scanner(System.in);
    }

    public static String lectorAndOrdinaryMafia(){
        System.out.println("Who are you going to kill?");
        String kill = scanner.nextLine();
        mafiavotes.add(kill);
        return kill;
    }

    public static String godFather(){
        System.out.println("votes of your teammates : "+mafiavotes+"\n"+
                "Who are you going to kill?");
        String kill = scanner.nextLine();
        return kill;
    }

    public static String doctor(){
        System.out.println("Which of your teammates do you want to save?");
        String save = scanner.nextLine();
        return save;
    }

    public static String detective(){
        System.out.println("Who do you want to inquire about?");
        String inquiry = scanner.nextLine();
        return inquiry;
    }

    public static String sniper(){
        String kill = "";
        System.out.println("Do you want to use your role?");
        String answer = scanner.nextLine();
        if (answer.equals("yes")){
            System.out.println("Who are you going to kill?");
            kill = scanner.nextLine();
        }
        return kill;
    }

    public static String psychologist(){
        System.out.println("Who do you want to silence?");
        String silence = scanner.nextLine();
        return silence;
    }

    public static String dieHard(){
        System.out.println("Do you want to use your role?.write yes or no");
        String answer = scanner.nextLine();
        return answer;
    }

}
