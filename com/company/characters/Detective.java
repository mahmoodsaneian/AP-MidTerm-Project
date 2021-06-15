package com.company.characters;

/**
 * this class inherit [CitizenTeam] class & also can use methods that
 * exists in [CitizenTeam] class & [Role] class.
 * detective is a member of citizen team that can wants inquiry
 * from server about mafia's members.
 *
 * @author  mahmood-saneian
 * @since   2021-6-15
 * @version 15.0.2
 */
public class Detective extends CitizenTeam{

    /**
     * this constructor call super class constructor & give some information about
     * detective such as [role description] & [name] to it.
     */
    public Detective() {
        super("Detective","you are a detective & "+
                "you can get inquiry from godfather about mafia",false, true, true);
    }
}
