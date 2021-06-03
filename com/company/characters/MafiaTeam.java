package com.company.characters;

import java.util.Scanner;

public  class MafiaTeam extends Role {

    public MafiaTeam(String name, String roleDescription, boolean inquiry
            , boolean canSpeak, boolean alive) {
        super(name, roleDescription, inquiry, canSpeak, alive);
    }

    public String killCitizen(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Which citizen do you want to kill?");
        String kill = scanner.nextLine();
        return kill;
    }
}
