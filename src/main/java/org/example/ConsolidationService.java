package org.example;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConsolidationService {

    public static HashMap<String,Integer> parseCsv(List<String> appNames, String file) {
        boolean primeraLinea = true;
        var cuenta = new HashMap<String,Integer>();

        try {
            List<String> lineas = Files.readAllLines( Path.of(file) );

            for(String linea : lineas){
                if(primeraLinea){
                    primeraLinea=false;
                }else {
                    /* Solo me interesa el nombre de la App */
                    String[] items = (linea.trim()).split(";");

                    String Appname = items[1];

                    if( cuenta.containsKey(Appname) ){
                        cuenta.put(Appname, cuenta.get(Appname) + 1);
                    } else{
                        cuenta.put(Appname, 1);
                        if(!appNames.contains(Appname)) appNames.add(Appname);
                    }
                }
            };
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return cuenta;
    }

    public static List<AppStats> generateAppStats(List<String> appNames, HashMap<String, Integer> cuentaCrashes, HashMap<String, Integer> cuentaEventos) {

        List<AppStats> appStatsList = new ArrayList<>(); // Estadisticas de todas las apps

        for( String app : appNames){
            Integer crashes =  cuentaCrashes.get(app);
            Integer eventos =  cuentaEventos.get(app);
            Double ratio = Double.valueOf(crashes)/ Double.valueOf(eventos) * 100;
            appStatsList.add( new AppStats(app,eventos,crashes,ratio) );
        }
        return appStatsList;
    }

    public static void toJsonFile(List<AppStats> appStatsList, String file) {
        ObjectMapper om = new ObjectMapper();
        ObjectWriter w = om.writerWithDefaultPrettyPrinter();
        w.writeValue(Path.of(file), appStatsList);
    }
}
