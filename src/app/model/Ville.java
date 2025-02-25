package app.model;

public class Ville {
    private int idVille;

    private String nom;

    private String pays;

    private String imageUrl;

    public Ville(int idVille, String nom, String pays, String imageUrl) {
        this.idVille = idVille;
        this.nom = nom;
        this.pays = pays;
        this.imageUrl = imageUrl;
    }

    public Ville(int idVille) {
        this.idVille = idVille;
    }

    public Ville() {
    }

    public int getIdVille() {
        return idVille;
    }

    public void setIdVille(int idVille) {
        this.idVille = idVille;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}