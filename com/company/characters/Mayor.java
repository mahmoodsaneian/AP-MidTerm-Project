package com.company.characters;

/**
 * this class inherit [CitizenTeam] class & also can use methods that
 * exists in [CitizenTeam] class & [Role] class.
 * mayor is a member of citizen team that can cancel voting
 * and also knows city doctor.
 *
 * @author  mahmood-saneian
 * @since   2021-6-15
 * @version 15.0.2
 */
public class Mayor extends CitizenTeam{

    /**
     * this constructor call super class constructor & give some information about
     * mayor such as [role description] & [name] to it.
     */
    public Mayor() {
        super("Mayor","you are a mayor & you can cancel a vote"
                ,false, true, true);
    }
}
