package app.model;

import java.time.LocalDateTime;
import java.util.List;

public class Reservation {
    private int idReservation;

    private int idVol;

    public int getIdVol() {
        return idVol;
    }

    public void setIdVol(int idVol) {
        this.idVol = idVol;
    }

    private int nombrePersonnes;

    private LocalDateTime dateReservation;

    private double montantTotal;

    private List<DetailReservation> detailReservations;

    public List<DetailReservation> getDetailReservations() {
        return detailReservations;
    }

    public void setDetailReservations(List<DetailReservation> detailReservations) {
        this.detailReservations = detailReservations;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getNombrePersonnes() {
        return nombrePersonnes;
    }

    public void setNombrePersonnes(int nombrePersonnes) {
        this.nombrePersonnes = nombrePersonnes;
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDateTime dateReservation) {
        this.dateReservation = dateReservation;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

}