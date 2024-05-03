package data;

import model.Experimento;
import java.io.*;

public class FileManager {
    public static final String DIRECTORY = "experimentos/";  // Directory where experiments are stored

    // Method to save an experiment to a file
    public static void saveExperiment(Experimento experimento, String filename) {
        File dir = new File(DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();  // Create directory if it does not exist
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(dir, filename)))) {
            oos.writeObject(experimento);  // Serialize the Experimento object to file
            System.out.println("Experimento guardado exitosamente en: " + filename);
        } catch (IOException e) {
            System.err.println("Error al guardar el experimento: " + e.getMessage());
        }
    }

    // Method to load an experiment from a file
    public static Experimento loadExperiment(String filename) {
        File file = new File(DIRECTORY, filename);
        if (!file.exists()) {
            System.err.println("File does not exist: " + file.getAbsolutePath());
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Experimento) ois.readObject();  // Deserialize the file into an Experimento object
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar el experimento: " + e.getMessage());
            return null;
        }
    }
}


