package com.company.characters;

import java.util.Scanner;

public class CityDoctor extends CitizenTeam{
    private int saveYourSelf;

    public CityDoctor() {
        super("City doctor","You are the doctor of city "+
                "& you should save citizens from mafia team",false, true, true);
        saveYourSelf = 0;
    }

    public int getSaveYourSelf() {
        return saveYourSelf;
    }

}
