package com.company.characters;


import java.util.ArrayList;
import java.util.Scanner;

public class RolesAct {
    private static ArrayList<Role> roles;
//    private static ArrayList<Player> players;
    private static Scanner scanner = new Scanner(System.in);


    public RolesAct(ArrayList<Role> roles) {
        this.roles = roles;
    }

//    public static String doctorLectorAct(){
//        String save = "";
//        outer:
//        while (true){
//            System.out.println("Who do you want to save?");
//            String save = scanner.nextLine();
//            for (Role role : roles){
//                if (role instanceof DoctorLector)
//                    if ()
//            }
//        }
//        return save;
//    }

    public static String cityDoctorAct(){
        //find city doctor
        CityDoctor cityDoctor = null;
        for (Role role : roles){
            if (role instanceof CityDoctor){
                cityDoctor = (CityDoctor) role;
                break;
            }
        }

        String save = "";
        while (true) {
            System.out.println("Who do you want to save?");
            if (cityDoctor.getSaveYourSelf() == 1)
                System.out.println("You can't save your self");
            save = scanner.nextLine();
            if (!(save.equals(cityDoctor.getName())))
                break;
            else
                System.out.println("wrong input please try again");
        }
        return save;
    }

}
