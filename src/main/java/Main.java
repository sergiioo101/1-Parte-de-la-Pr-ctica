import data.FileManager;
import model.Experimento;
import model.Poblacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

public class Main {
    private static Experimento currentExperiment;
    private static JFrame frame;
    private static JList<String> listPoblaciones;
    private static DefaultListModel<String> listModel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame("Gestión de Experimentos de Cultivo de Bacterias");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel experimentPanel = createExperimentPanel();
        tabbedPane.addTab("Experimentos", experimentPanel);

        JPanel populationPanel = createPopulationPanel();
        tabbedPane.addTab("Poblaciones", populationPanel);

        JPanel detailsPanel = createDetailsPanel();
        tabbedPane.addTab("Detalles", detailsPanel);

        frame.add(tabbedPane);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JPanel createExperimentPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
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
        JFileChooser fileChooser = new JFileChooser(FileManager.DIRECTORY);
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            currentExperiment = FileManager.loadExperiment(filename);
            if (currentExperiment != null) {
                JOptionPane.showMessageDialog(frame, "Experimento cargado correctamente.", "Cargar", JOptionPane.INFORMATION_MESSAGE);
                updatePoblacionesList();
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
        JFileChooser fileChooser = new JFileChooser(FileManager.DIRECTORY);
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
            FileManager.saveExperiment(currentExperiment, filename);
            JOptionPane.showMessageDialog(frame, "Nuevo experimento creado y guardado.", "Nuevo Experimento", JOptionPane.INFORMATION_MESSAGE);
            updatePoblacionesList();
        }
    }

    private static JPanel createPopulationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        listModel = new DefaultListModel<>();
        listPoblaciones = new JList<>(listModel);
        listPoblaciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(listPoblaciones);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        JButton btnAdd = new JButton("Añadir Población");
        JButton btnRemove = new JButton("Eliminar Población");

        btnAdd.addActionListener(e -> addPopulation());
        btnRemove.addActionListener(e -> removePopulation());

        buttonsPanel.add(btnAdd);
        buttonsPanel.add(btnRemove);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private static void addPopulation() {
        if (currentExperiment == null) {
            JOptionPane.showMessageDialog(frame, "No hay un experimento activo. Por favor, crea o carga un experimento primero.");
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        try {
            String nombre = JOptionPane.showInputDialog(frame, "Nombre de la Población:");
            LocalDate fechaInicio = LocalDate.parse(JOptionPane.showInputDialog(frame, "Fecha de Inicio (YYYY-M-D):"), formatter);
            LocalDate fechaFin = LocalDate.parse(JOptionPane.showInputDialog(frame, "Fecha de Fin(YYYY-M-D):"), formatter);
            int numBacterias = Integer.parseInt(JOptionPane.showInputDialog(frame, "Número de Bacterias Iniciales:"));
            double temperatura = Double.parseDouble(JOptionPane.showInputDialog(frame, "Temperatura:"));
            String luminosidad = JOptionPane.showInputDialog(frame, "Luminosidad (Alta, Media, Baja):");
            int comidaInicial = Integer.parseInt(JOptionPane.showInputDialog(frame, "Comida Inicial:"));
            int diaIncremento = Integer.parseInt(JOptionPane.showInputDialog(frame, "Día de Incremento Máximo:"));
            int comidaMaxima = Integer.parseInt(JOptionPane.showInputDialog(frame, "Comida Máxima en el Día de Incremento:"));
            int comidaFinal = Integer.parseInt(JOptionPane.showInputDialog(frame, "Comida Final en Día 30:"));

            Poblacion nuevaPoblacion = new Poblacion(nombre, fechaInicio, fechaFin, numBacterias, temperatura, luminosidad, comidaInicial, diaIncremento, comidaMaxima, comidaFinal);
            currentExperiment.addPoblacion(nuevaPoblacion);
            updatePoblacionesList();
            JOptionPane.showMessageDialog(frame, "Población añadida correctamente.");
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(frame, "La fecha ingresada no es válida. Por favor, use el formato YYYY-M-D.");
        }
    }

    private static void removePopulation() {
        String selected = listPoblaciones.getSelectedValue();
        if (selected != null && currentExperiment.removePoblacion(selected)) {
            updatePoblacionesList();
            JOptionPane.showMessageDialog(frame, "Población eliminada correctamente.");
        } else {
            JOptionPane.showMessageDialog(frame, "Seleccione una población para eliminar.");
        }
    }

    private static void updatePoblacionesList() {
        listModel.clear();
        if (currentExperiment != null) {
            for (Poblacion p : currentExperiment.getPoblaciones()) {
                listModel.addElement(p.getNombre());
            }
        }
    }

    private static JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea detailsArea = new JTextArea(10, 50);
        detailsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(detailsArea);

        JButton btnShowDetails = new JButton("Mostrar Detalles");
        btnShowDetails.addActionListener(e -> {
            Poblacion p = currentExperiment.getPoblacion(listPoblaciones.getSelectedValue());
            if (p != null) {
                detailsArea.setText(p.toString());
            } else {
                detailsArea.setText("Seleccione una población para ver detalles.");
            }
        });

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnShowDetails, BorderLayout.SOUTH);

        return panel;
    }
}




