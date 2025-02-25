package app.model;

import java.time.LocalDateTime;
import java.util.List;

public class Vol {
    private int idVol;

    private Avion avion;

    private Ville villeDepart;

    private Ville villeDestination;

    private LocalDateTime dateDepart;

    private LocalDateTime dateArrive;

    private List<VolDetails> volDetails;

    private RegleVol regleVol;


    public RegleVol getRegleVol() {
        return regleVol;
    }


    public void setRegleVol(RegleVol regleVol) {
        this.regleVol = regleVol;
    }


    public List<VolDetails> getVolDetails() {
        return volDetails;
    }


    public void setVolDetails(List<VolDetails> volDetails) {
        this.volDetails = volDetails;
    }


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


    public LocalDateTime getDateDepart() {
        return dateDepart;
    }


    public void setDateDepart(LocalDateTime dateDepart) {
        this.dateDepart = dateDepart;
    }


    public LocalDateTime getDateArrive() {
        return dateArrive;
    }


    public void setDateArrive(LocalDateTime dateArrive) {
        this.dateArrive = dateArrive;
    }

    public VolDetails getVolDetailsByIdTypeSiege(int idTypeSiege) {
        for (VolDetails vd : volDetails) {
            if (vd.getTypeSiege().getIdTypeSiege() == idTypeSiege) {
                return vd;
            }
        }
        return null;
    }
}