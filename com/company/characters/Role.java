package com.company.characters;

import java.util.Scanner;

/**
 * this class include information about a role.
 * such as name of role, a short description about role,a boolean that specifies
 * role can speak or no,a boolean that specifies role alive or no.
 * also include some methods such as [getVote] that get vote from player.
 *
 * @author  mahmood-saneian
 * @since   2021-6-15
 * @version 15.0.2
 */
public class Role  {
    //the name of role
    private String name;
    //the short description about role
    private String roleDescription;
    //specifies response of role inquiry
    private boolean inquiry;
    //specifies player's that has this role can speak or no
    private boolean canSpeak;
    //specifies role is died or alive
    private boolean alive;

    /**
     * this constructor get some information about role and assign to fields
     * @param name the name of role
     * @param roleDescription the short description about role.
     * @param inquiry the inquiry of role
     * @param canSpeak specifies that role can speak or no
     * @param alive specifies that role is alive or died
     */
    public Role(String name, String roleDescription, boolean inquiry
            , boolean canSpeak, boolean alive) {

        this.name = name;
        this.roleDescription = roleDescription;
        this.inquiry = inquiry;
        this.canSpeak = canSpeak;
        this.alive = alive;
    }

    /**
     * this method get a vote from player.
     * @return the player's vote.
     */
    public String getVote(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Who do you want to delete?");
        String delete = scanner.nextLine();
        return delete;
    }

    /**
     * this method return name of role.
     * @return name of role.
     */
    public String getName() {
        return name;
    }

    /**
     * this method return description of role.
     * @return the description of role.
     */
    public String getRoleDescription() {
        return roleDescription;
    }

    /**
     * this method return response of role's inquiry.
     * @return response of role's inquiry.
     */
    public boolean isInquiry() {
        return inquiry;
    }

    /**
     * this method return response of player is can speak or no.
     * @return the response of player is can speak or no.
     */
    public boolean isCanSpeak() {
        return canSpeak;
    }

    /**
     * this method return response of player is died or alive.
     * @return the response of player is died or alive.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Set talk permission
     * @param canSpeak is true or false
     */
    public void setCanSpeak(boolean canSpeak) {
        this.canSpeak = canSpeak;
    }

    /**
     * set condition of live
     * @param alive is true or false
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
