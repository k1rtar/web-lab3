package beans;

import com.google.gson.Gson;
import database.DatabaseManager;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class ResultTable implements Serializable {
    private static final long serialVersionUID = 1L;
    @Getter
    @Setter
    private List<Point> results;
    private DatabaseManager dbManager;
    private final Lock lock = new ReentrantLock();

    @PostConstruct
    public void init() throws SQLException, IOException {
        dbManager = new DatabaseManager();
        dbManager.createTable();
        results = dbManager.getDataFromTable();
    }

    public void addPoint(Point row) {
        dbManager.addPoint(row);
        try {
            lock.lock();
            results.add(row);
        } finally {
            lock.unlock();
        }
    }

    public void clear() {
        dbManager.clear();
        try {
            lock.lock();
            results.clear();
        } finally {
            lock.unlock();
        }
    }

    public String getX() {
        return new Gson().toJson(getResults().stream().map(Point::getX).collect(Collectors.toList()));
    }

    public String getY() {
        return new Gson().toJson(getResults().stream().map(Point::getY).collect(Collectors.toList()));
    }

    public String getR() {
        return new Gson().toJson(getResults().stream().map(Point::getR).collect(Collectors.toList()));
    }

    public String getHit() {
        return new Gson().toJson(getResults().stream().map(Point::isHit).collect(Collectors.toList()));
    }
}
