package com.company.Game;

import com.company.characters.*;

import java.util.ArrayList;

public class CreateRoles {
    private ArrayList<Role> roles;
    private ArrayList<String> nameRoles;

    public CreateRoles() {
        roles = new ArrayList<Role>();
        nameRoles = new ArrayList<String>();
        createRoles();
        addRolesNames();
    }

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

    public ArrayList<Role> getRoles() {
        return roles;
    }

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

    public ArrayList<String> getNameRoles() {
        return nameRoles;
    }
}
