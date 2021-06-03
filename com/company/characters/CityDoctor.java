package com.company.characters;

import java.util.Scanner;

public class CityDoctor extends CitizenTeam{
    private int saveYourSelf;

    public CityDoctor() {
        super("City doctor","You are the doctor of city "+
                "& you should save citizens from mafia team",false, true, true);
        saveYourSelf = 0;
    }

    public int getSaveYourSelf() {
        return saveYourSelf;
    }

//    @Override
//    public String act() {
//        Scanner scanner = new Scanner(System.in);
//        String save = "";
//        while (true) {
//            System.out.println("Who do you want to save?");
//            if (saveYourSelf == 1)
//                System.out.println("You can't save your self");
//            save = scanner.nextLine();
//            if (!(save.equals(this.getName())))
//                break;
//            else
//                System.out.println("wrong input please try again");
//        }
//        return save;
//    }
}
