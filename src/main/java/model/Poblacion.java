package model;

import java.time.LocalDate;

public class Poblacion {
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int numBacterias;
    private double temperatura;
    private String luminosidad;
    private int comidaInicial;
    private int diaIncremento;
    private int comidaMaxima;
    private int comidaFinal;
    private int[] planAlimentacion;

    public Poblacion(String nombre, LocalDate fechaInicio, LocalDate fechaFin, int numBacterias,
                     double temperatura, String luminosidad, int comidaInicial, int diaIncremento,
                     int comidaMaxima, int comidaFinal) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.numBacterias = numBacterias;
        this.temperatura = temperatura;
        this.luminosidad = luminosidad;
        this.comidaInicial = comidaInicial;
        this.diaIncremento = diaIncremento;
        this.comidaMaxima = comidaMaxima;
        this.comidaFinal = comidaFinal;
        this.planAlimentacion = new int[30];  // Asumiendo que cada experimento dura 30 días
        calcularPlanAlimentacion();
    }

    private void calcularPlanAlimentacion() {
        double incrementoDiario = (comidaMaxima - comidaInicial) / (double) diaIncremento;
        double decrementoDiario = (comidaMaxima - comidaFinal) / (double) (30 - diaIncremento);

        for (int i = 0; i < 30; i++) {
            if (i < diaIncremento) {
                planAlimentacion[i] = (int) (comidaInicial + i * incrementoDiario);
            } else {
                planAlimentacion[i] = (int) (comidaMaxima - (i - diaIncremento) * decrementoDiario);
            }
        }
    }

    // Getters y Setters para acceder y modificar los atributos de la clase
    // Aquí incluiría métodos como getNombre(), getFechaInicio(), etc.

    public int[] getPlanAlimentacion() {
        return planAlimentacion;
    }

    @Override
    public String toString() {
        return "Poblacion{" +
                "nombre='" + nombre + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", numBacterias=" + numBacterias +
                ", temperatura=" + temperatura +
                ", luminosidad='" + luminosidad + '\'' +
                ", planAlimentacion=" + Arrays.toString(planAlimentacion) +
                '}';
    }

}
