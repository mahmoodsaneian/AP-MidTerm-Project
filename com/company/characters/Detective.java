package com.company.characters;

import java.util.Scanner;

public class Detective extends CitizenTeam{

    public Detective() {
        super("Detective","you are a detective & "+
                "you can get inquiry from godfather about mafia",false, true, true);
    }

//    @Override
//    public String act() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Who do you want to inquire about?");
//        String input = scanner.nextLine();
//        return input;
//    }
}
