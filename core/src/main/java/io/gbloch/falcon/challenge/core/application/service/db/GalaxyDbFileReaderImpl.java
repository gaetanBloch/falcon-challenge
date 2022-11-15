package io.gbloch.falcon.challenge.core.application.service.db;

import com.google.common.collect.RowSortedTable;
import com.google.common.collect.TreeBasedTable;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import io.gbloch.falcon.challenge.core.common.ResourceFileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response.Status;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

@Slf4j
@ApplicationScoped
public final class GalaxyDbFileReaderImpl implements GalaxyDbFileReader {

    private static final String SQL_QUERY = "SELECT ORIGIN, DESTINATION, TRAVEL_TIME FROM ROUTES";
    private static final String JDBC_PREFIX = "jdbc:sqlite:";

    @Override
    public MutableValueGraph<String, Integer> readFile(String dbFilePath) {
        final MutableValueGraph<String, Integer> galaxy = ValueGraphBuilder.directed().build();
        String dbFileName = FilenameUtils.getName(dbFilePath);
        File file;
        try {
            file = ResourceFileUtils.createTempFileFromResource(dbFileName);
        } catch (IOException e) {
            throw new GalaxyDbException("Could not find Galaxy DB file", e);
        }
        String dbConnectionString = JDBC_PREFIX + file.getAbsolutePath();
        try (Connection connection = DriverManager.getConnection(dbConnectionString)) {
            RowSortedTable<String, String, Integer> routes = createRoutesFromDb(connection);
            routes.rowKeySet().forEach(
                (k) -> routes.row(k).forEach((k2, v) -> galaxy.putEdgeValue(k, k2, v))
            );
        } catch (SQLException e) {
            log.error("Error while reading database file", e);
            throw new GalaxyDbException("Error while reading database file", e);
        }
        return galaxy;
    }

    private RowSortedTable<String, String, Integer> createRoutesFromDb(Connection connection)
        throws SQLException {
        final RowSortedTable<String, String, Integer> routes = TreeBasedTable.create();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQL_QUERY);
            while (resultSet.next()) {
                String origin = resultSet.getString("origin");
                if(origin == null || origin.isBlank()){
                    throw new IllegalArgumentException("Origin cannot be null or empty");
                }
                String destination = resultSet.getString("destination");
                if(destination == null || destination.isBlank()){
                    throw new IllegalArgumentException("Destination cannot be null or empty");
                }
                int travelTime = resultSet.getInt("travel_time");
                if(travelTime <= 0){
                    throw new IllegalArgumentException("Travel time must be greater than 0");
                }

                routes.put(origin, destination, travelTime);
            }
        }
        return routes;
    }
}
