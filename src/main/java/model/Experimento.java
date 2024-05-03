package model;

import java.util.ArrayList;
import java.util.List;

public class Experimento {
    private String archivo;
    private List<Poblacion> poblaciones;

    public Experimento(String archivo) {
        this.archivo = archivo;
        this.poblaciones = new ArrayList<>();
    }

    public void addPoblacion(Poblacion poblacion) {
        poblaciones.add(poblacion);
    }

    public void removePoblacion(String nombrePoblacion) {
        poblaciones.removeIf(p -> p.getNombre().equals(nombrePoblacion));
    }

    public Poblacion getPoblacion(String nombrePoblacion) {
        return poblaciones.stream()
                .filter(p -> p.getNombre().equals(nombrePoblacion))
                .findFirst()
                .orElse(null);
    }

    public List<Poblacion> getPoblaciones() {
        return poblaciones;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }
}
