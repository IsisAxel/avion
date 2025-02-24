package app.model;

public class RegleVol {
    private int idRegleVol;

    private Vol vol;

    private int heureMaxReservation;

    private int heureMaxAnnulation;

    public int getIdRegleVol() {
        return idRegleVol;
    }

    public void setIdRegleVol(int idRegleVol) {
        this.idRegleVol = idRegleVol;
    }

    public Vol getVol() {
        return vol;
    }

    public void setVol(Vol vol) {
        this.vol = vol;
    }

    public int getHeureMaxReservation() {
        return heureMaxReservation;
    }

    public void setHeureMaxReservation(int heureMaxReservation) {
        this.heureMaxReservation = heureMaxReservation;
    }

    public int getHeureMaxAnnulation() {
        return heureMaxAnnulation;
    }

    public void setHeureMaxAnnulation(int heureMaxAnnulation) {
        this.heureMaxAnnulation = heureMaxAnnulation;
    }

}