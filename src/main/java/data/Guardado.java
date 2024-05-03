package data;

import model.Experimento;
import java.io.*;

public class Guardado {
    public static void saveExperiment(Experimento experimento, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(experimento);
            System.out.println("Experimento guardado exitosamente en: " + filename);
        } catch (IOException e) {
            System.err.println("Error al guardar el experimento: " + e.getMessage());
        }
    }

    public static Experimento loadExperiment(String filename) {
        Experimento experimento = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            experimento = (Experimento) ois.readObject();
            System.out.println("Experimento cargado exitosamente desde: " + filename);
        } catch (IOException e) {
            System.err.println("Error al cargar el experimento: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Clase no encontrada al cargar el experimento: " + e.getMessage());
        }
        return experimento;
    }
}
