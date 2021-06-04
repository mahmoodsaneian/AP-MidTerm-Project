package com.company.Game;

import com.company.characters.*;

import java.util.ArrayList;

public class CreateRoles {
    private ArrayList<Role> roles;

    public CreateRoles() {
        roles = new ArrayList<Role>();
        createRoles();
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
}
