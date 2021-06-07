package com.company.characters;

import java.io.Serializable;
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

        System.out.println("Who do you want to delete?");
        String delete = scanner.nextLine();
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
