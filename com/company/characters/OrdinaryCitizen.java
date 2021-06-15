package com.company.characters;

/**
 * this class inherit [CitizenTeam] class & also can use methods that
 * exists in [CitizenTeam] class & [Role] class.
 * ordinary citizen is a member of citizen team that don't do anything
 * special during the game.
 *
 * @author  mahmood-saneian
 * @since   2021-6-15
 * @version 15.0.2
 */
public class OrdinaryCitizen extends CitizenTeam {

    /**
     * this constructor call super class constructor & give some information about
     * ordinary citizen such as [role description] & [name] to it.
     */
    public OrdinaryCitizen() {
        super("Ordinary Citizen","you are an ordinary citizen & "+
                "you must try to find the mafia",false, true, true);
    }
}
