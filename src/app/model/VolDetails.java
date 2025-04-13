package app.model;

public class VolDetails {
    private int idVolDetails;

    private TypeSiege typeSiege;

    private int placeDispo;
    private int placeRestante;

    public int getPlaceRestante() {
        return placeRestante;
    }

    public void setPlaceRestante(int placeRestante) {
        this.placeRestante = placeRestante;
    }

    private double prix;

    public int getIdVolDetails() {
        return idVolDetails;
    }

    public void setIdVolDetails(int idVolDetails) {
        this.idVolDetails = idVolDetails;
    }

    public TypeSiege getTypeSiege() {
        return typeSiege;
    }

    public void setTypeSiege(TypeSiege typeSiege) {
        this.typeSiege = typeSiege;
    }

    public int getPlaceDispo() {
        return placeDispo;
    }

    public void setPlaceDispo(int placeDispo) {
        this.placeDispo = placeDispo;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

}