package app.model;

public class VolPrix {
    private int idVolPrix;
    
    private Vol vol;

    private TypeSiege typeSiege;

    private double prix;

    public int getIdVolPrix() {
        return idVolPrix;
    }

    public void setIdVolPrix(int idVolPrix) {
        this.idVolPrix = idVolPrix;
    }

    public Vol getVol() {
        return vol;
    }

    public void setVol(Vol vol) {
        this.vol = vol;
    }

    public TypeSiege getTypeSiege() {
        return typeSiege;
    }

    public void setTypeSiege(TypeSiege typeSiege) {
        this.typeSiege = typeSiege;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

}