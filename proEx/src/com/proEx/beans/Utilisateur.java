package com.proEx.beans;

public class Utilisateur {
    private String nom;
    private String email;
    private String motDePass;

    public String getNom() {
        return nom;
    }

    public void setNom( String nom ) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getMotDePass() {
        return motDePass;
    }

    public void setMotDePass( String motDePass ) {
        this.motDePass = motDePass;
    }

}
