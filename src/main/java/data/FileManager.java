package data;

import model.Experimento;
import java.io.*;

public class FileManager {
    public static final String DIRECTORY = "experimentos/";

    public static void saveExperiment(Experimento experimento, String filename) {
        File dir = new File(DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DIRECTORY + filename))) {
            oos.writeObject(experimento);
            System.out.println("Experimento guardado exitosamente en: " + filename);
        } catch (IOException e) {
            System.err.println("Error al guardar el experimento: " + e.getMessage());
        }
    }

    public static Experimento loadExperiment(String filename) {
        Experimento experimento = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DIRECTORY + filename))) {
            experimento = (Experimento) ois.readObject();
            System.out.println("Experimento cargado exitosamente desde: " + filename);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar el experimento: " + e.getMessage());
        }
        return experimento;
    }
}

