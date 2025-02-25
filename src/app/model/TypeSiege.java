package app.model;

public class TypeSiege {
    private int idTypeSiege;

    private String type;

    public TypeSiege(int idTypeSiege) {
        this.idTypeSiege = idTypeSiege;
    }

    public TypeSiege(int idTypeSiege, String type) {
        this.idTypeSiege = idTypeSiege;
        this.type = type;
    }

    public TypeSiege() {
    }

    public int getIdTypeSiege() {
        return idTypeSiege;
    }

    public void setIdTypeSiege(int idTypeSiege) {
        this.idTypeSiege = idTypeSiege;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}