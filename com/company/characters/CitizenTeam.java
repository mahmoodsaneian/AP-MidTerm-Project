package com.company.characters;

/**
 * this class inherit [Role] class & can use methods that exists
 * in the [Role] class.
 * citizen team has some members who fight with mafia team.
 * city doctor, ordinary citizen, detective, sniper,
 * die hard, psychologist, mayor are citizen's members.
 *
 * @author  mahmood-saneian
 * @since   2021-6-15
 * @version 15.0.2
 */
public class CitizenTeam extends Role {

     /**
     * this constructor get some information about each member of citizen team & give to superclass constructor.
     * @param name the name of role
     * @param roleDescription the short description about role
     * @param inquiry the inquiry of role
     * @param canSpeak specifies that role can speak or no
     * @param alive specifies that role is alive or died
     */
    public CitizenTeam(String name, String roleDescription, boolean inquiry
            , boolean canSpeak, boolean alive) {
        super(name, roleDescription, inquiry, canSpeak, alive);
    }


}
