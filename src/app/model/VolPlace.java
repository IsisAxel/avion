package app.model;

public class VolPlace {
    private int idVolPlace;

    private Vol vol;

    private TypeSiege typeSiege;

    private int placeDispo;

    public int getIdVolPlace() {
        return idVolPlace;
    }

    public void setIdVolPlace(int idVolPlace) {
        this.idVolPlace = idVolPlace;
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

    public int getPlaceDispo() {
        return placeDispo;
    }

    public void setPlaceDispo(int placeDispo) {
        this.placeDispo = placeDispo;
    }

}