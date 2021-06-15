package com.company.characters;

/**
 * this class inherit [MafiaTeam] class & can use methods that exist in the
 * [MafiaTeam] class & also [Role] class.
 * ordinary mafia is a member of mafia team that don't do anything special.
 * ordinary mafia just misleads other citizens & vote at night is about
 * who to be killed.
 *
 * @author  mahmood-saneian
 * @since   2021-6-15
 * @version 15.0.2
 */
public class OrdinaryMafia extends MafiaTeam {

    /**
     * this constructor call super class constructor & give some information about
     * ordinary mafia such as [role description[ & [name] & ... to it.
     */
    public OrdinaryMafia() {
        super("ordinary Mafia","You are member of the mafia team & " +
                "you should try to mislead the citizens",true, true, true);
    }

}
