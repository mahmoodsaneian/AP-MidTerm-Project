package com.company.characters;

import com.company.Game.ManageData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public  class Role implements Serializable {
    private String name;
    private String roleDescription;
    private boolean inquiry;
    private boolean canSpeak;
    private boolean alive;

    public Role(String name, String roleDescription, boolean inquiry
            , boolean canSpeak, boolean alive) {

        this.name = name;
        this.roleDescription = roleDescription;
        this.inquiry = inquiry;
        this.canSpeak = canSpeak;
        this.alive = alive;
    }

    public String getVote(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Now is the time to vote. You have 30 seconds to vote");

        long start = System.currentTimeMillis();
        long end = start + 30*1000;
        String delete = null;
        ArrayList<String> usernames = ManageData.getUsernames();
        System.out.println("players in the game : "+usernames);
        boolean truth = false;

        outer:
        while (System.currentTimeMillis() < end){
            while (true) {
                System.out.println("Who do you want to delete?");
                delete = scanner.nextLine();
                for (String user : usernames){
                    if (user.equals(delete)){
                        truth = true;
                        break;
                    }
                }
                if (truth)
                    break outer;
                else
                    System.out.println("invalid input.please try again");
            }
        }
        if (delete == null)
            delete = "invalid vote";
        return delete;
    }

    public String getName() {
        return name;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public boolean isInquiry() {
        return inquiry;
    }

    public boolean isCanSpeak() {
        return canSpeak;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setCanSpeak(boolean canSpeak) {
        this.canSpeak = canSpeak;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
