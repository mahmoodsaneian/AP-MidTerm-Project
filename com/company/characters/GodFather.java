package com.company.characters;

/**
 * this class inherit [MafiaTeam] class & can use methods that exist in the
 * [MafiaTeam] class & also [Role] class.
 * god father is a member of mafia team
 * that the final decision is about who to be killed.
 * also his inquiry is negative.
 *
 * @author  mahmood-saneian
 * @since   2021-6-15
 * @version 15.0.2
 */
public class GodFather extends MafiaTeam {

    /**
     * this constructor call super class constructor & give some information about
     * god father such as [role description] & [name] & ... to it.
     */
    public GodFather() {
        super("godFather","You are the leader of the mafia team "+
                "And you have to be the final decision maker about the citizen who wants to be killed"
                ,false, true, true);
    }

}
