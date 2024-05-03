import model.Experimento;
import model.Poblacion;
import data.FileManager;


import java.time.LocalDate;
import java.util.Scanner;

public class main {
    private static Experimento currentExperiment = null;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int option;
        do {
            System.out.println("\n*** Gestión de Experimentos de Cultivo de Bacterias ***");
            System.out.println("1. Abrir experimento existente");
            System.out.println("2. Crear nuevo experimento");
            System.out.println("3. Añadir población al experimento");
            System.out.println("4. Eliminar población del experimento");
            System.out.println("5. Mostrar información de una población");
            System.out.println("6. Guardar experimento");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción: ");
            option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (option) {
                case 1:
                    openExperiment();
                    break;
                case 2:
                    createExperiment();
                    break;
                case 3:
                    addPopulation();
                    break;
                case 4:
                    removePopulation();
                    break;
                case 5:
                    showPopulationDetails();
                    break;
                case 6:
                    saveExperiment();
                    break;
                case 7:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor intente de nuevo.");
            }
        } while (option != 7);
    }

    private static void openExperiment() {
        System.out.print("Ingrese el nombre del archivo del experimento: ");
        String filename = scanner.nextLine();
        currentExperiment = FileManager.loadExperiment(filename);
        if (currentExperiment == null) {
            System.out.println("No se pudo cargar el experimento.");
        } else {
            System.out.println("Experimento cargado exitosamente.");
        }
    }

    private static void createExperiment() {
        System.out.print("Ingrese el nombre del archivo para el nuevo experimento: ");
        String filename = scanner.nextLine();
        currentExperiment = new Experimento(filename);
        System.out.println("Nuevo experimento creado.");
    }

    private static void addPopulation() {
        if (currentExperiment == null) {
            System.out.println("No hay experimento abierto. Por favor, abra o cree un experimento primero.");
            return;
        }
        System.out.print("Ingrese nombre de la población: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese la fecha de inicio (AAAA-MM-DD): ");
        LocalDate fechaInicio = LocalDate.parse(scanner.nextLine());
        System.out.print("Ingrese la fecha de fin (AAAA-MM-DD): ");
        LocalDate fechaFin = LocalDate.parse(scanner.nextLine());
        System.out.print("Ingrese el número inicial de bacterias: ");
        int numBacterias = scanner.nextInt();
        System.out.print("Ingrese la temperatura: ");
        double temperatura = scanner.nextDouble();
        scanner.nextLine(); // consume newline
        System.out.print("Ingrese la luminosidad (Alta, Media, Baja): ");
        String luminosidad = scanner.nextLine();
        System.out.print("Ingrese la comida inicial: ");
        int comidaInicial = scanner.nextInt();
        System.out.print("Ingrese el día hasta el cual incrementar la comida: ");
        int diaIncremento = scanner.nextInt();
        System.out.print("Ingrese la comida máxima en el día de incremento: ");
        int comidaMaxima = scanner.nextInt();
        System.out.print("Ingrese la comida final en el día 30: ");
        int comidaFinal = scanner.nextInt();
        scanner.nextLine(); // consume newline

        Poblacion nuevaPoblacion = new Poblacion(nombre, fechaInicio, fechaFin, numBacterias, temperatura, luminosidad,
                comidaInicial, diaIncremento, comidaMaxima, comidaFinal);
        currentExperiment.addPoblacion(nuevaPoblacion);
        System.out.println("Población añadida exitosamente al experimento.");
    }

    private static void removePopulation() {
        if (currentExperiment == null) {
            System.out.println("No hay experimento abierto. Por favor, abra o cree un experimento primero.");
            return;
        }
        System.out.print("Ingrese el nombre de la población que desea eliminar: ");
        String nombre = scanner.nextLine();
        currentExperiment.removePoblacion(nombre);
        System.out.println("Población eliminada exitosamente.");
    }

    private static void showPopulationDetails() {
        if (currentExperiment == null) {
            System.out.println("No hay experimento abierto. Por favor, abra o cree un experimento primero.");
            return;
        }
        System.out.print("Ingrese el nombre de la población cuyos detalles quiere ver: ");
        String nombre = scanner.nextLine();
        Poblacion poblacion = currentExperiment.getPoblacion(nombre);
        if (poblacion == null) {
            System.out.println("No se encontró la población especificada.");
        } else {
            System.out.println(poblacion);
        }
    }

    private static void saveExperiment() {
        if (currentExperiment == null) {
            System.out.println("No hay experimento abierto. Por favor, abra o cree un experimento primero.");
            return;
        }
        FileManager.saveExperiment(currentExperiment, currentExperiment.getArchivo());
    }
}
