package com.company.characters;

/**
 * this class inherit [CitizenTeam] class & also can use methods that
 * exists in [CitizenTeam] class & [Role] class.
 * sniper is a member of citizen team that can kill mafia's members.
 * if sniper s/he fires wrong , s/he kills herself.
 *
 * @author  mahmood-saneian
 * @since   2021-6-15
 * @version 15.0.2
 */
public class Sniper extends CitizenTeam{

    /**
     * this constructor call super class constructor & give some information about
     * sniper such as [role description] & [name] to it.
     */
    public Sniper() {
        super("Sniper","you are sniper & you can kill a mafia every night"
                ,false, true, true);
    }
}
