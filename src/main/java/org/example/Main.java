package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

    public final static String CRASHES_FILE = "crashes.csv";
    public final static String EVENTS_FILE = "events.csv";
    public final static String OUTPUT_FILE = "analysis_report.json";

    public static void main(String[] args) {

        List<String> appNames = new ArrayList<>(); // listado de los nombres de las apps

        /* Leo y proceso crashes, solo lo que me interesa */
        HashMap<String,Integer>  cuentaCrashes = ConsolidationService.parseCsv(appNames,CRASHES_FILE);
        
        /* Leo y proceso events, solo lo que me interesa */
        HashMap<String,Integer>  cuentaEventos = ConsolidationService.parseCsv(appNames,EVENTS_FILE);

        /* Calculo de estadisticas */
        List<AppStats> appStatsList = ConsolidationService.generateAppStats(appNames, cuentaCrashes, cuentaEventos);

        /* Volcado de estadísticas */
        ConsolidationService.toJsonFile(appStatsList,OUTPUT_FILE);

    }




}