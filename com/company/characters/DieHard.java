package com.company.characters;

/**
 * this class inherit [CitizenTeam] class & also can use methods that
 * exists in [CitizenTeam] class & [Role] class.
 * die hard is a member of citizen team that can wants inquiry from
 * server about roles that leave from game.
 * also mafia's members must two times shoot to die hard.
 *
 * @author  mahmood-saneian
 * @since   2021-6-15
 * @version 15.0.2
 */
public class DieHard extends CitizenTeam{
    //the number of times that die hard died
    int killCounter;
    //the number of times that die hard wanted inquiry
    int inquiryCounter;

    /**
     * this constructor call super class constructor & give some information about
     * die hard such as [role description] & [name] to it.
     * and also init kill counter & inquiry counter.
     */
    public DieHard() {
        super("Die hard","you are a die hard & "+
                "You can inquire twice from the narrator",false, true,true);
        killCounter = 0;
        inquiryCounter = 0;
    }

    /**
     * this method increase times that die hard killed.
     */
    public void increamentKillCounter(){
        killCounter++;
    }

    /**
     * this method return number of times that die hard killed.
     * @return counter of kill.
     */
    public int getKillCounter() {
        return killCounter;
    }

    /**
     * this method increase times that die hard wanted inquiry.
     */
    public void increamentInquiryCounter(){
        inquiryCounter++;
    }

    /**
     * this method return number of times that die hard wanted inquiry.
     * @return counter of inquiry.
     */
    public int getInquiryCounter() {
        return inquiryCounter;
    }
}
