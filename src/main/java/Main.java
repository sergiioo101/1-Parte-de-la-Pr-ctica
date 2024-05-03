import data.FileManager;
import model.Experimento;
import model.Poblacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.ArrayList;

public class Main {

    private static Experimento currentExperiment;
    private static JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        // Establecer el look and feel del sistema para la interfaz de usuario
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Crear y configurar la ventana principal
        frame = new JFrame("Gestión de Experimentos de Cultivo de Bacterias");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Crear el panel de pestañas
        JTabbedPane tabbedPane = new JTabbedPane();

        // Pestaña para abrir o crear experimentos
        JPanel experimentPanel = createExperimentPanel();
        tabbedPane.addTab("Experimentos", experimentPanel);

        // Pestaña para gestionar poblaciones
        JPanel populationPanel = createPopulationPanel();
        tabbedPane.addTab("Poblaciones", populationPanel);

        // Pestaña para visualización de detalles
        JPanel detailsPanel = createDetailsPanel();
        tabbedPane.addTab("Detalles", detailsPanel);

        // Agregar el panel de pestañas al frame
        frame.add(tabbedPane);
        frame.setLocationRelativeTo(null);  // Centrar ventana
        frame.setVisible(true);
    }

    private static JPanel createExperimentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton btnLoad = new JButton("Cargar Experimento");
        JButton btnSave = new JButton("Guardar Experimento");
        JButton btnNew = new JButton("Nuevo Experimento");

        btnLoad.addActionListener(e -> loadExperiment());
        btnSave.addActionListener(e -> saveExperiment());
        btnNew.addActionListener(e -> createNewExperiment());

        panel.add(btnLoad);
        panel.add(btnSave);
        panel.add(btnNew);

        return panel;
    }

    private static void loadExperiment() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            currentExperiment = FileManager.loadExperiment(filename);
            if (currentExperiment != null) {
                JOptionPane.showMessageDialog(frame, "Experimento cargado correctamente.", "Cargar", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Error al cargar el experimento.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void saveExperiment() {
        if (currentExperiment == null) {
            JOptionPane.showMessageDialog(frame, "No hay experimento activo para guardar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            FileManager.saveExperiment(currentExperiment, filename);
            JOptionPane.showMessageDialog(frame, "Experimento guardado correctamente.", "Guardar", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void createNewExperiment() {
        String filename = JOptionPane.showInputDialog(frame, "Ingrese el nombre del archivo para el nuevo experimento:");
        if (filename != null && !filename.isEmpty()) {
            currentExperiment = new Experimento(filename);
            JOptionPane.showMessageDialog(frame, "Nuevo experimento creado.", "Nuevo Experimento", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static JPanel createPopulationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea populationDetails = new JTextArea(10, 50);
        populationDetails.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(populationDetails);

        JButton btnAdd = new JButton("Añadir Población");
        JButton btnRemove = new JButton("Eliminar Población");

        btnAdd.addActionListener(e -> addPopulation());
        btnRemove.addActionListener(e -> removePopulation(populationDetails));

        panel.add(scrollPane, BorderLayout.CENTER);
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(btnAdd);
        buttonsPanel.add(btnRemove);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private static void addPopulation() {
        // Aquí añadiríamos una forma de recopilar datos y crear una nueva población, similar a lo que hemos descrito antes
        JOptionPane.showMessageDialog(frame, "Funcionalidad para añadir población no implementada.", "En construcción", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void removePopulation(JTextArea textArea) {
        // Aquí implementaríamos la lógica para eliminar una población
        JOptionPane.showMessageDialog(frame, "Funcionalidad para eliminar población no implementada.", "En construcción", JOptionPane.INFORMATION_MESSAGE);
    }

    private static JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea detailsArea = new JTextArea(10, 50);
        detailsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(detailsArea);

        JButton btnShowDetails = new JButton("Mostrar Detalles");
        btnShowDetails.addActionListener(e -> showPopulationDetails(detailsArea));

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnShowDetails, BorderLayout.SOUTH);

        return panel;
    }

    private static void showPopulationDetails(JTextArea textArea) {
        if (currentExperiment != null && !currentExperiment.getPoblaciones().isEmpty()) {
            StringBuilder details = new StringBuilder();
            for (Poblacion p : currentExperiment.getPoblaciones()) {
                details.append(p.toString()).append("\n");
            }
            textArea.setText(details.toString());
        } else {
            textArea.setText("No hay poblaciones para mostrar o experimento no cargado.");
        }
    }
}

