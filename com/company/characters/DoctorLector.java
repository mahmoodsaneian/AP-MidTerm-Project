package com.company.characters;

/**
 * this class inherit [MafiaTeam] class & can use methods that exist in the
 * [MafiaTeam] class & also [Role] class.
 * doctor lector is a member of mafia team & can save mafia members from sniper.
 * also doctor lector give a vote at night that who killed.
 * doctor lector just 2 times can save himself from sniper.
 * His inquiry is also positive.
 *
 * @author  mahmood-saneian
 * @since   2021-6-15
 * @version 15.0.2
 */
public class DoctorLector extends MafiaTeam {
    //number of times that doctor save himself
    private int saveYourSelf;

    /**
     * this constructor call super class constructor and give some information
     * about doctor lector such as [role description] & [name] &... to it.
     * also give initial number to [Save your self] variable.
     */
    public DoctorLector() {
        super("Doctor lector","you are the doctor of the mafia team"+
                "And you must try to save your teammates who are killed by snipers"
                ,true, true, true);
        saveYourSelf = 0;
    }

    /**
     * this method return number of times that doctor save himself.
     * @return save himself counter
     */
    public int getSaveYourSelf() {
        return saveYourSelf;
    }

    /**
     * this method increase number of times that doctor save himself from sniper.
     */
    public void increament(){
        saveYourSelf++;
    }
}
