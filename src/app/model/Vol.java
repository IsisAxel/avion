package app.model;


import java.sql.Timestamp;

public class Vol {
    private int idVol;

    private Avion avion;

    private Ville villeDepart;

    private Ville villeDestination;

    private Timestamp dateDepart;

    private Timestamp dateArrive;


    public int getIdVol() {
        return idVol;
    }


    public void setIdVol(int idVol) {
        this.idVol = idVol;
    }


    public Avion getAvion() {
        return avion;
    }


    public void setAvion(Avion avion) {
        this.avion = avion;
    }


    public Ville getVilleDepart() {
        return villeDepart;
    }


    public void setVilleDepart(Ville villeDepart) {
        this.villeDepart = villeDepart;
    }


    public Ville getVilleDestination() {
        return villeDestination;
    }


    public void setVilleDestination(Ville villeDestination) {
        this.villeDestination = villeDestination;
    }


    public Timestamp getDateDepart() {
        return dateDepart;
    }


    public void setDateDepart(Timestamp dateDepart) {
        this.dateDepart = dateDepart;
    }


    public Timestamp getDateArrive() {
        return dateArrive;
    }


    public void setDateArrive(Timestamp dateArrive) {
        this.dateArrive = dateArrive;
    }

}