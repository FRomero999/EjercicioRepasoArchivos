package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
class AppStats implements Serializable {
    private String app;
    private Integer totalEvents;
    private Integer totalCrashes;
    private Double errorRatio;
}


public class Main {

    public final static String CRASHES_FILE = "crashes.csv";
    public final static String EVENTS_FILE = "events.csv";
    public final static String OUTPUT_FILE = "analysis_report.json";

    public static void main(String[] args) {

        List<AppStats> appStatsList = new ArrayList<>(); // Estadisticas de todas las apps
        List<String> appNames = new ArrayList<>(); // listado de los nombres de las apps
        HashMap<String,Integer> cuentaCrashes = new HashMap<String,Integer>(); // Mapa con el nombre de la App y el total de crashes
        HashMap<String,Integer> cuentaEventos = new HashMap<String,Integer>(); // Mapa con el nombre de la App y el total de eventos

        /* Leo y proceso crashes, solo lo que me interesa */

        boolean primeraLinea = true;

        try {
            List<String> lineas = Files.readAllLines( Path.of(CRASHES_FILE) );
            for(String linea : lineas){
                if(primeraLinea){
                    primeraLinea=false;
                }else {
                    /* Solo me interesa el nombre de la App */
                    String[] items = (linea.trim()).split(";");
                    String Appname = items[1];

                    if( cuentaCrashes.containsKey(Appname) ){
                        cuentaCrashes.put(Appname, cuentaCrashes.get(Appname) + 1);
                    } else{
                        cuentaCrashes.put(Appname, 1);
                        if(!appNames.contains(Appname)) appNames.add(Appname);
                    }
                }
            };
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /* Leo y proceso events, solo lo que me interesa */

        primeraLinea=true;

        try {
            List<String> lineas = Files.readAllLines( Path.of(EVENTS_FILE) );
            for(String linea : lineas){
                if(primeraLinea){
                    primeraLinea=false;
                }else {
                    String[] items = (linea.trim()).split(";");
                    String Appname = items[1];

                    if( cuentaEventos.containsKey(Appname) ){
                        cuentaEventos.put(Appname, cuentaEventos.get(Appname) + 1);
                    } else{
                        cuentaEventos.put(Appname, 1);
                        if(!appNames.contains(Appname)) appNames.add(Appname);
                    }
                }
            };
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /* Calculo de estadisticas */

        for( String app : appNames ){
            Integer crashes =  cuentaCrashes.get(app);
            Integer eventos =  cuentaEventos.get(app);
            Double ratio = Double.valueOf(crashes)/ Double.valueOf(eventos) * 100;
            appStatsList.add( new AppStats(app,eventos,crashes,ratio) );
        }

        /* Volcado de estadísticas */

        ObjectMapper om = new ObjectMapper();
        ObjectWriter w = om.writerWithDefaultPrettyPrinter();
        w.writeValue(Path.of(OUTPUT_FILE), appStatsList);

    }
}