package com.company.characters;

public class DieHard extends CitizenTeam{
    int counterInquiry;

    public DieHard() {
        super("Die hard","you are a die hard & "+
                "You can inquire twice from the narrator",false, true,true);
        counterInquiry = 0;
    }

    public void increament(){
        counterInquiry++;
    }

    public int getCounterInquiry() {
        return counterInquiry;
    }
}
