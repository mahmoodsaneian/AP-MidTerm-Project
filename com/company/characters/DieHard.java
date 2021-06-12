package com.company.characters;

public class DieHard extends CitizenTeam{
    int killCounter;

    public DieHard() {
        super("Die hard","you are a die hard & "+
                "You can inquire twice from the narrator",false, true,true);
        killCounter = 0;
    }

    public void increament(){
        killCounter++;
    }

    public int getKillCounter() {
        return killCounter;
    }
}
