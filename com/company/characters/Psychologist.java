package com.company.characters;

/**
 * this class inherit [CitizenTeam] class & also can use methods that
 * exists in [CitizenTeam] class & [Role] class.
 * psychologist is a member of citizen team that can silent a player
 * at night.
 *
 * @author  mahmood-saneian
 * @since   2021-6-15
 * @version 15.0.2
 */
public class Psychologist extends CitizenTeam{

    /**
     * this constructor call super class constructor & give some information about
     * psychologist such as [role description] & [name] to it.
     */
    public Psychologist() {
        super("Psychologist","you are a psychologist & "+
                "you can silent a person",false, true, true);
    }
}
