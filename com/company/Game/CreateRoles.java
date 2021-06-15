package com.company.Game;

import com.company.characters.*;
import java.util.ArrayList;

/**
 * this class creates roles that exists in the game.
 * after creates roles we can get list of roles names or
 * list of roles.
 *
 * @author  mahmood-saneian
 * @since   2021-6-15
 * @version 15.0.2
 */
public class CreateRoles {
    //list of roles
    private ArrayList<Role> roles;
    //list of roles names
    private ArrayList<String> nameRoles;

    /**
     * this constructor allocates memory for lists and call
     * methods that create roles & names of roles.
     */
    public CreateRoles() {
        roles = new ArrayList<Role>();
        nameRoles = new ArrayList<String>();
        createRoles();
        addRolesNames();
    }

    /**
     * this method creates roles that exist in the game.
     */
    public void createRoles(){
        Role godFather       = new GodFather();
        Role doctorLector    = new DoctorLector();
        Role ordinaryMafia   = new OrdinaryMafia();
        Role cityDoctor      = new CityDoctor();
        Role detective       = new Detective();
        Role sniper          = new Sniper();
        Role ordinaryCitizen = new OrdinaryCitizen();
        Role mayor           = new Mayor();
        Role psychologist    = new Psychologist();
        Role dieHard         = new DieHard();

        roles.add(godFather);
        roles.add(doctorLector);
        roles.add(ordinaryMafia);
        roles.add(cityDoctor);
        roles.add(detective);
        roles.add(sniper);
        roles.add(ordinaryCitizen);
        roles.add(mayor);
        roles.add(psychologist);
        roles.add(dieHard);
    }

    /**
     * this method return list of roles that exist in the game.
     * @return the list of roles that exist in the game.
     */
    public ArrayList<Role> getRoles() {
        return roles;
    }

    /**
     * this method add name of roles to list.
     */
    public void addRolesNames(){
        nameRoles.add("godFather");
        nameRoles.add("Doctor lector");
        nameRoles.add("ordinary Mafia");
        nameRoles.add("City doctor");
        nameRoles.add("Detective");
        nameRoles.add("Sniper");
        nameRoles.add("Ordinary Citizen");
        nameRoles.add("Mayor");
        nameRoles.add("Psychologist");
        nameRoles.add("Die hard");
    }

    /**
     * return list of roles names.
     * @return the list of roles names.
     */
    public ArrayList<String> getNameRoles() {
        return nameRoles;
    }
}
