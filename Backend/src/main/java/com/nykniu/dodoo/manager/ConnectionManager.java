package com.nykniu.dodoo.manager;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nykniu.dodoo.GlobalVars;
import com.nykniu.dodoo.poco.ProviderConnection;

public class ConnectionManager {
    private YamlManager yamlManager;

    /**
     * Constructor que declara el administrador del archivo de configuración
     */
    public ConnectionManager() {
        yamlManager = new YamlManager();
    }

    /**
     * Obtiene los datos del ticket del proveedor
     * 
     * @param providerCode Código del proveedor
     * @param ticket       Folio del ticket
     * @return Objeto con los datos obtenidos
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws FileNotFoundException
     */
    public JsonObject getData(int providerCode, String ticket)
            throws ClassNotFoundException, SQLException, FileNotFoundException {
        JsonObject result = new JsonObject();
        HashMap<Integer, ProviderConnection> providerConnections = yamlManager.getYamlConfig();

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
                        .replace("${limitName}", provider.getTicket().getFilterName()));
        query.append(" AS `ticket`");
        query.append(
                "(SELECT ${column}) FROM ${table} WHERE ${limitName} = ?)"
                        .replace("${column}", provider.getDate().getColumnName())
                        .replace("${table}", provider.getDate().getTableName())
                        .replace("${limitName}", provider.getDate().getFilterName()));
        query.append(" AS `date`");
        query.append(
                "(SELECT ${column}) FROM ${table} WHERE ${limitName} = ?)"
                        .replace("${column}", provider.getAmmount().getColumnName())
                        .replace("${table}", provider.getAmmount().getTableName())
                        .replace("${limitName}", provider.getAmmount().getFilterName()));
        query.append(" AS `ammount`;");

        Class.forName(GlobalVars.managers.get(provider.getJDBC()));
        String connectionUri = "jdbc:${jdbc}://${hostname}/${database}"
                .replace("${jdbc}", provider.getJDBC())
                .replace("${hostname}", provider.getHost())
                .replace("${database}", provider.getDatabase());
        Connection conn = DriverManager.getConnection(connectionUri, provider.getUsername(), provider.getPassword());

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

    /**
     * Obtener la lista de proveedores registrados en el archivo de configuración
     * 
     * @return Lista de proveedores
     * @throws FileNotFoundException 
     */
    public JsonArray getProvidersList() throws FileNotFoundException {
        return yamlManager.getProvidersList();
    }
}
