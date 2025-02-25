package app.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/avion";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "passwd";

    public static Connection getConnexion() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}