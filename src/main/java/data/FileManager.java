package data;

import model.Experimento;
import java.io.*;

public class FileManager {
    public static final String DIRECTORY = "experimentos/";  // Ensure this directory is correct and relative to the project or execution directory

    // Save an experiment to a file
    public static void saveExperiment(Experimento experimento, String filename) {
        try {
            File dir = new File(DIRECTORY);
            if (!dir.exists()) {
                dir.mkdirs();  // Create directory if it does not exist
            }
            File file = new File(dir, getFileName(filename));  // Ensuring correct file name usage
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(experimento);  // Serialize the Experimento object to file
                System.out.println("Experimento guardado exitosamente en: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar el experimento: " + e.getMessage());
        }
    }

    // Load an experiment from a file
    public static Experimento loadExperiment(String filename) {
        try {
            File file = new File(DIRECTORY, getFileName(filename));  // Ensure correct file name usage
            if (!file.exists()) {
                System.err.println("File does not exist: " + file.getAbsolutePath());
                return null;
            }
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (Experimento) ois.readObject();  // Deserialize the file into an Experimento object
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar el experimento: " + e.getMessage());
            return null;
        }
    }

    // Helper method to extract filename from path if necessary
    private static String getFileName(String path) {
        File f = new File(path);
        return f.getName();  // This will strip out any preceding path and return just the filename
    }
}



