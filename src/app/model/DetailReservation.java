package app.model;

import java.math.BigDecimal;
import java.util.Date;

public class DetailReservation {
    private int idDetail;

    private Reservation reservation;

    private String nomComplet;

    private Date dateNaissance;

    private String numeroPassport;

    private TypeSiege typeSiege;

    private BigDecimal prix;

    public int getIdDetail() {
        return idDetail;
    }

    public void setIdDetail(int idDetail) {
        this.idDetail = idDetail;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getNumeroPassport() {
        return numeroPassport;
    }

    public void setNumeroPassport(String numeroPassport) {
        this.numeroPassport = numeroPassport;
    }

    public TypeSiege getTypeSiege() {
        return typeSiege;
    }

    public void setTypeSiege(TypeSiege typeSiege) {
        this.typeSiege = typeSiege;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

}