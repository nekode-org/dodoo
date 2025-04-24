package com.nykniu.dodoo.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.yaml.snakeyaml.Yaml;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nykniu.dodoo.GlobalVars;
import com.nykniu.dodoo.poco.ProviderConnection;

public class YamlManager {
    private long lastModifided;
    private HashMap<Integer, ProviderConnection> yamlConfig;

    /**
     * Refresca el config si el archivo es más nuevo, no lo cambia en caso contrario
     * 
     * @throws FileNotFoundException
     */
    private void refreshConfigFile() throws FileNotFoundException {
        long modified = new File("./rsc/connections.yml").lastModified();
        if (modified > lastModifided) {
            InputStream in = new FileInputStream("./rsc/connections.yml");
            ArrayList rawConfig = (ArrayList) ((HashMap) new Yaml().load(in)).get("connections");

            for (int i = 0; i < rawConfig.size(); ++i) {
                HashMap<String, Object> connectionData = (HashMap<String, Object>) rawConfig.get(i);

                // Validación de los campos
                if (!connectionData.containsKey("name") ||
                        !connectionData.containsKey("host") ||
                        !connectionData.containsKey("JDBC") ||
                        !connectionData.containsKey("database") ||
                        !connectionData.containsKey("fields") ||
                        !connectionData.containsKey("username")) {
                    throw new NoSuchFieldError("Malformed YAML configuration!");
                }

                System.out.println(connectionData.toString());
                if (!GlobalVars.managers.containsKey(connectionData.get("JDBC"))) {
                    throw new IllegalArgumentException("Unrecognized JDBC!");
                }

                yamlConfig.put((Integer) connectionData.get("code"), new ProviderConnection(connectionData));
            }
        }
    }

    /**
     * Refresca la versión de configuración y la retorna
     * 
     * @return La lista nueva o de caché
     * @throws FileNotFoundException
     */
    public HashMap<Integer, ProviderConnection> getYamlConfig() throws FileNotFoundException {
        refreshConfigFile();
        return yamlConfig;
    }

    /**
     * Obtiene la lista de proveedores registrados
     * 
     * @return Lista de proveedores
     * @throws FileNotFoundException
     */
    public JsonArray getProvidersList() throws FileNotFoundException {
        refreshConfigFile();

        JsonArray providersList = new JsonArray();
        for (Integer key : yamlConfig.keySet()) {
            JsonObject provider = new JsonObject();
            provider.addProperty("code", key);
            provider.addProperty("name", yamlConfig.get(key).getProviderName());
        }

        return providersList;
    }
}
