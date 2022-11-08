package io.gbloch.falcon.challenge.core.application.service.db;

import com.google.common.collect.RowSortedTable;
import com.google.common.collect.TreeBasedTable;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
final class GalaxyDbFileReaderImpl implements GalaxyDbFileReader {

    private static final String SQL_QUERY = "SELECT ORIGIN, DESTINATION, TRAVEL_TIME FROM ROUTES";
    private static final String JDBC_PREFIX = "jdbc:sqlite:";

    @Override
    public MutableValueGraph<String, Integer> readFile(String dbFilePath)
        throws GalaxyDbException {
        MutableValueGraph<String, Integer> galaxy = ValueGraphBuilder.directed().build();
        String dbConnectionString = JDBC_PREFIX + dbFilePath;
        try (Connection connection = DriverManager.getConnection(dbConnectionString)) {
            RowSortedTable<String, String, Integer> routes = createRoutesFromDb(connection);
            routes.rowKeySet().forEach(
                (k) -> routes.row(k).forEach((k2, v) -> galaxy.putEdgeValue(k, k2, v))
            );
        } catch (SQLException e) {
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
                String destination = resultSet.getString("destination");
                int travelTime = resultSet.getInt("travel_time");
                routes.put(origin, destination, travelTime);
            }
        }
        return routes;
    }
}
