package database;


import beans.Point;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DatabaseManager {
    private static Connection connection;

    public DatabaseManager() {
        try {
            String DATABASE_URL = "jdbc:postgresql://pg:5432/studs";
            Scanner scanner = new Scanner(new File("/home/studs/s373330/.pgpass"));
            String line = scanner.nextLine();
            String[] data = line.split(":");
            connection = DriverManager.getConnection(DATABASE_URL, data[3], data[4]);
        } catch (SQLException | IOException e) {

            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void createTable() throws SQLException, IOException {
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS HITS"
                + "(X DOUBLE PRECISION, "
                + "Y DOUBLE PRECISION, "
                + "R DOUBLE PRECISION,"
                + "HIT BOOLEAN, "
                + "ATTEMPT_TIME BIGINT,"
                + "EXECUTION_TIME DOUBLE PRECISION);");
    }

    public List<Point> getDataFromTable() throws SQLException, IOException {
        List<Point> results = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM HITS");
        while (rs.next()) {
            results.add(new Point(rs.getDouble("X"), rs.getDouble("Y"),
                    rs.getDouble("R"), rs.getBoolean("HIT"), rs.getLong("ATTEMPT_TIME"),
                    rs.getLong("EXECUTION_TIME")));
        }
        return results;
    }

    public void addPoint(Point row) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO HITS "
                    + "(X, Y, R, HIT, ATTEMPT_TIME, EXECUTION_TIME) VALUES (?, ?, ?, ?, ?, ?)");
            statement.setDouble(1, row.getX());
            statement.setDouble(2, row.getHiddenY());
            statement.setDouble(3, row.getR());
            statement.setBoolean(4, row.isHit());
            statement.setLong(5, row.getAttemptTime());
            statement.setDouble(6, row.getExecutionTime());
            statement.execute();
        } catch (SQLException e) {
        }
    }

    public void clear() {
        try {
            Statement statement = connection.createStatement();
            statement.execute("TRUNCATE TABLE HITS");
        } catch (SQLException e) {
        }
    }
}

