package com.nykniu.dodoo.controller;

import java.sql.SQLException;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nykniu.dodoo.GlobalVars;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/providers")
public class ProvidersController {

    /**
     * Obtener información del rfc y de los tickets según los proveedores
     * 
     * @params
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(value = "/getInfo", method = RequestMethod.POST, produces = "application/json")
    public String getInfo(
            ModelMap model, HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "email", required = true) String email,
            @RequestParam(value = "rfc", required = true) String rfc,
            @RequestParam(value = "rfcPdf", required = true) String rfcPdf,
            @RequestParam(value = "tickets", required = true) JsonArray ticketsArray)
            throws ClassNotFoundException, SQLException {

        // TODO Aquí se usa la API de SAT
        JsonArray ticketResults = new JsonArray();
        for (int i = 0; i < ticketsArray.size(); i++) {
            ticketResults.add(
                GlobalVars.connectionManager.getData(
                    ticketsArray.get(i).getAsJsonObject().get("providerCode").getAsInt(),
                    ticketsArray.get(i).getAsJsonObject().get("ticketCode").getAsString()
                )
            );
        }

        JsonObject result = new JsonObject();
        result.add("ticketResults", ticketResults);

        return result.toString();
    }
}
