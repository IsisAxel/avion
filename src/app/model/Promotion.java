package app.model;


public class Promotion {
    private int idPromotion;

    private Vol vol;

    private TypeSiege typeSiege;

    private double pourcentage;

    private int nombreMaxPassager;

    public int getIdPromotion() {
        return idPromotion;
    }

    public void setIdPromotion(int idPromotion) {
        this.idPromotion = idPromotion;
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

    public double getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(double pourcentage) {
        this.pourcentage = pourcentage;
    }

    public int getNombreMaxPassager() {
        return nombreMaxPassager;
    }

    public void setNombreMaxPassager(int nombreMaxPassager) {
        this.nombreMaxPassager = nombreMaxPassager;
    }
}