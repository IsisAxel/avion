package app.model;

public class SiegeAvion {
    private int idSiegeAvion;

    private TypeSiege typeSiege;

    private Avion avion;

    private int nombrePlace;

    public int getIdSiegeAvion() {
        return idSiegeAvion;
    }

    public void setIdSiegeAvion(int idSiegeAvion) {
        this.idSiegeAvion = idSiegeAvion;
    }

    public TypeSiege getTypeSiege() {
        return typeSiege;
    }

    public void setTypeSiege(TypeSiege typeSiege) {
        this.typeSiege = typeSiege;
    }

    public Avion getAvion() {
        return avion;
    }

    public void setAvion(Avion avion) {
        this.avion = avion;
    }

    public int getNombrePlace() {
        return nombrePlace;
    }

    public void setNombrePlace(int nombrePlace) {
        this.nombrePlace = nombrePlace;
    }

}