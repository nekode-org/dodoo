package com.nykniu.dodoo.poco;

import java.util.ArrayList;
import java.util.HashMap;

public class ProviderConnection {
    private String clientName;
    private String host;
    private int port;
    private String JDBC;
    private String database;
    private String username;
    private String password;
    private ProviderField ticket;
    private ProviderField date;
    private ProviderField ammount;

    /*
     * class Connection
     * Clase que contiene los datos de conexión de cada cliente
     */
    public ProviderConnection(HashMap<String, Object> data) {
        this.clientName = (String) data.get("name");
        this.host = (String) data.get("host");
        this.port = (int) data.get("port");
        this.JDBC = (String) data.get("JDBC");
        this.database = (String) data.get("database");
        this.username = (String) data.get("username");
        this.password = (String) data.get("password");

        ArrayList<HashMap> fields = (ArrayList<HashMap>) data.get("fields");

        for (int i = 0; i < fields.size(); i++) {
            HashMap field = fields.get(i);
            if ("folio".equals(field.get("name"))) {
                this.ticket = new ProviderField((String) field.get("table"), (String) field.get("column"), (String) field.get("filter"));
            } else if ("fecha".equals(field.get("name"))) {
                this.ticket = new ProviderField((String) field.get("table"), (String) field.get("column"), (String) field.get("filter"));
            } else if ("monto".equals(field.get("name"))) {
                this.ticket = new ProviderField((String) field.get("table"), (String) field.get("column"), (String) field.get("filter"));
            }
        }
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getJDBC() {
        return JDBC;
    }

    public void setJDBC(String JDBC) {
        this.JDBC = JDBC;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ProviderField getTicket() {
        return ticket;
    }

    public void setTicket(ProviderField ticket) {
        this.ticket = ticket;
    }

    public ProviderField getDate() {
        return date;
    }

    public void setDate(ProviderField date) {
        this.date = date;
    }

    public ProviderField getAmmount() {
        return ammount;
    }

    public void setAmmount(ProviderField ammount) {
        this.ammount = ammount;
    }
}
