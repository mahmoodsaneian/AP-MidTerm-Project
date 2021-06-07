package com.company.characters;

import java.util.Scanner;

public class DoctorLector extends MafiaTeam {
    private int saveYourSelf;

    public DoctorLector() {
        super("Doctor lector","you are the doctor of the mafia team"+
                "And you must try to save your teammates who are killed by snipers"
                ,true, true, true);
        saveYourSelf = 0;
    }

    public int getSaveYourSelf() {
        return saveYourSelf;
    }
}
