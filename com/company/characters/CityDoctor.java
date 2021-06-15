package com.company.characters;

/**
 * this class inherit [CitizenTeam] class & also can use methods that
 * exists in [CitizenTeam] class & [Role] class.
 * city doctor is a member of citizen team that can hill citizen's members.
 * also doctor just two times can save himself.
 */
public class CityDoctor extends CitizenTeam{
    //the number of times that doctor saved himself
    private int saveYourSelf;

    /**
     * this constructor call super class constructor & give some information about
     * city doctor such as [role description] & [name] to it.
     * and also init save himself.
     */
    public CityDoctor() {
        super("City doctor","You are the doctor of city "+
                "& you should save citizens from mafia team",false, true, true);
        saveYourSelf = 0;
    }

    /**
     * this method return number of times that doctor saved himself.
     * @return counter of saved himself.
     */
    public int getSaveYourSelf() {
        return saveYourSelf;
    }

    /**
     *increase number of times that doctor saved himself.
     */
    public void increament(){
        saveYourSelf++;
    }
}
