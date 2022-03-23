package org.miage.biblio.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Hotel implements Serializable {

    @Id
    private Long id;
    private String nom;
    private String ville;
    private String etat;

    public Hotel(){

    }

    public Hotel(Long id, String nom, String ville, String etat) {
        super();
        this.id = id;
        this.nom = nom;
        this.ville = ville;
        this.etat = etat;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEtat() {
        return etat;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getVille() {
        return ville;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", ville='" + ville + '\'' +
                ", etat='" + etat + '\'' +
                '}';
    }
}
