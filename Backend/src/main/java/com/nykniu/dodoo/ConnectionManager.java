package com.nykniu.dodoo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.yaml.snakeyaml.Yaml;

import com.google.gson.JsonObject;
import com.nykniu.dodoo.poco.ProviderConnection;

public class ConnectionManager {
    private HashMap<Integer, ProviderConnection> providerConnections;
    private HashMap<String, String> managers;

    /**
     * Constructor que declara los JDBCs
     */
    public ConnectionManager() {
        managers = new HashMap<>();
        managers.put("mariadb", "org.mariadb.jdbc.Driver");
        managers.put("mysql", "com.mysql.cj.jdbc.Driver");

        providerConnections = new HashMap<>(); 
    }

    /**
     * Lee el archivo de configuración y genera las conexiones necesarias
     */
    public void setupConnections() {
        try {
            InputStream in = new FileInputStream("./rsc/connections.yml");
            ArrayList connections = (ArrayList) ((HashMap) new Yaml().load(in)).get("connections");

            for (int i = 0; i < connections.size(); ++i) {
                HashMap<String, Object> connectionData = (HashMap<String, Object>) connections.get(i);

                // Validación de los campos
                if (
                    !connectionData.containsKey("name") ||
                    !connectionData.containsKey("host") ||
                    !connectionData.containsKey("port") ||
                    !connectionData.containsKey("JDBC") ||
                    !connectionData.containsKey("database") ||
                    !connectionData.containsKey("fields")
                ) {
                    throw new NoSuchFieldError("Malformed YAML configuration!");
                }

                System.out.println(connectionData.toString());
                if (!managers.containsKey(connectionData.get("JDBC"))) {
                    throw new IllegalArgumentException("Unrecognized JDBC!");
                }

                providerConnections.put((Integer) connectionData.get("code"), new ProviderConnection(connectionData));
            }
        } catch (FileNotFoundException e) {
            System.err.println("Archivo de configuración de conexiones no encontrado");
            e.printStackTrace();
        }
    }

    /**
     * Obtiene los datos del ticket del proveedor
     * 
     * @param providerCode código del proveedor
     * @param ticket       folio del ticket
     * @return Booleano de error
     * @throws ClassNotFoundException 
     * @throws SQLException 
     */
    public JsonObject getData(int providerCode, String ticket) throws ClassNotFoundException, SQLException {
        JsonObject result = new JsonObject();
        if (!providerConnections.containsKey(providerCode)) {
            result.addProperty("error", 2);
            return result;
        }

        ProviderConnection provider = providerConnections.get(providerCode);

        StringBuilder query = new StringBuilder();
        query.append("SELECT");
        query.append(
            "(SELECT ${column}) FROM ${table} WHERE ${limitName} = ?)"
                .replace("${column}", provider.getTicket().getColumnName())
                .replace("${table}", provider.getTicket().getTableName())
                .replace("${limitName}", provider.getTicket().getFilterName())
        );
        query.append(" AS `ticket`");
        query.append(
            "(SELECT ${column}) FROM ${table} WHERE ${limitName} = ?)"
                .replace("${column}", provider.getDate().getColumnName())
                .replace("${table}", provider.getDate().getTableName())
                .replace("${limitName}", provider.getDate().getFilterName())
        );
        query.append(" AS `date`");
        query.append(
            "(SELECT ${column}) FROM ${table} WHERE ${limitName} = ?)"
                .replace("${column}", provider.getAmmount().getColumnName())
                .replace("${table}", provider.getAmmount().getTableName())
                .replace("${limitName}", provider.getAmmount().getFilterName())
        );
        query.append(" AS `ammount`;");

        Class.forName(managers.get(provider.getJDBC()));
        Connection conn = DriverManager.getConnection("jdbc:${jdbc}://${host}:${port}/${database}", provider.getUsername(), provider.getPassword());
        PreparedStatement stmt = conn.prepareStatement(query.toString());
        stmt.setString(0, ticket);
        stmt.setString(1, ticket);
        stmt.setString(2, ticket);

        ResultSet res = stmt.executeQuery();

        if (!res.next()) {
            result.addProperty("error", 1);
            return result;
        }

        result.addProperty("folio", res.getString("ticket"));
        result.addProperty("fecha", res.getString("date"));
        result.addProperty("monto", res.getString("ammount"));
        result.addProperty("error", 0);
        return result;
    }
}
