package com.company.Game;

import java.util.Scanner;

/**
 * this class include some static methods that call
 * in the game night from [PlayerMafia] class & concerned
 * act of each roles.
 *
 * @author  mahmood-saneian
 * @since   2021-6-15
 * @version 15.0.2
 */
public class NightGame {
    //For get input from player via console
    private static Scanner scanner = new Scanner(System.in);

    /**
     * this method ask from mafia's members about who want kill
     * at game night.
     * @return name of person who they want to be kill.
     */
    public static String mafiaAct() {
       System.out.println("Who do you want to kill?");
       String kill = scanner.nextLine();
       return kill;
    }

    /**
     * this method ask from doctor[lector or city doctor] about person
     * who s/he wants to save.
     * @return name of person who s/he wants to save.
     */
    public static String doctor() {
        System.out.println("Which of your teammates do you want to save?");
        String hill = scanner.nextLine();
        return hill;
    }

    /**
     * this method ask from detective about a person who
     * s/he wants about it.
     * @return name of person who detective ask about it.
     */
    public static String detective() {
        System.out.println("Who do you want to inquire about?");
        String inquiry = scanner.nextLine();
        return inquiry;
    }

    /**
     * this method ask from sniper about person who
     * s/he wants to kill.
     * @return name of person who sniper wants to kill.
     */
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

    /**
     * this method ask from psychologist about person who
     * s/he wants to silent it.
     * @return name of person who psychologist wants to silent it.
     */
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

    /**
     * this method ask from die hard that does s/he want use for his role or no?
     * @return yes or no.
     */
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
}
