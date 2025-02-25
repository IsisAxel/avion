package app.model;

import java.util.Date;


public class Avion {
    private int idAvion;

    private String compagnie;

    private String modele;

    private Date dateFabrication;

    private int nombreMaxPassager;

    public Avion(int idAvion, String compagnie, String modele, Date dateFabrication, int nombreMaxPassager) {
        this.idAvion = idAvion;
        this.compagnie = compagnie;
        this.modele = modele;
        this.dateFabrication = dateFabrication;
        this.nombreMaxPassager = nombreMaxPassager;
    }

    public Avion(int idAvion) {
        this.idAvion = idAvion;
    }

    public Avion() {
    }

    public int getIdAvion() {
        return idAvion;
    }

    public void setIdAvion(int idAvion) {
        this.idAvion = idAvion;
    }

    public String getCompagnie() {
        return compagnie;
    }

    public void setCompagnie(String compagnie) {
        this.compagnie = compagnie;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public Date getDateFabrication() {
        return dateFabrication;
    }

    public void setDateFabrication(Date dateFabrication) {
        this.dateFabrication = dateFabrication;
    }

    public int getNombreMaxPassager() {
        return nombreMaxPassager;
    }

    public void setNombreMaxPassager(int nombreMaxPassager) {
        this.nombreMaxPassager = nombreMaxPassager;
    }
}