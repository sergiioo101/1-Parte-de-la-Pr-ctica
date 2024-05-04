import data.FileManager;
import model.Experimento;
import model.Poblacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeParseException;

public class Main {
    private static Experimento currentExperiment;
    private static JFrame frame;
    private static JList<String> listPoblaciones; // Lista para mostrar nombres de poblaciones
    private static DefaultListModel<String> listModel; // Modelo de datos para la lista

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        frame = new JFrame("Gestión de Experimentos de Cultivo de Bacterias");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Experimentos", createExperimentPanel());
        tabbedPane.addTab("Poblaciones", createPopulationPanel());
        tabbedPane.addTab("Detalles", createDetailsPanel());

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
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField nameField = new JTextField();
        JTextField startDateField = new JTextField();
        JTextField endDateField = new JTextField();
        JTextField numBacteriasField = new JTextField();
        JTextField temperaturaField = new JTextField();
        JComboBox<String> luminosidadField = new JComboBox<>(new String[]{"Alta", "Media", "Baja"});
        JTextField comidaInicialField = new JTextField();
        JTextField diaIncrementoField = new JTextField();
        JTextField comidaMaximaField = new JTextField();
        JTextField comidaFinalField = new JTextField();

        panel.add(new JLabel("Nombre:"));
        panel.add(nameField);
        panel.add(new JLabel("Fecha de Inicio (YYYY-MM-DD):"));
        panel.add(startDateField);
        panel.add(new JLabel("Fecha de Fin (YYYY-MM-DD):"));
        panel.add(endDateField);
        panel.add(new JLabel("Número de Bacterias:"));
        panel.add(numBacteriasField);
        panel.add(new JLabel("Temperatura:"));
        panel.add(temperaturaField);
        panel.add(new JLabel("Luminosidad:"));
        panel.add(luminosidadField);
        panel.add(new JLabel("Comida Inicial:"));
        panel.add(comidaInicialField);
        panel.add(new JLabel("Día de Incremento Máximo:"));
        panel.add(diaIncrementoField);
        panel.add(new JLabel("Comida Máxima en el Día de Incremento:"));
        panel.add(comidaMaximaField);
        panel.add(new JLabel("Comida Final en Día 30:"));
        panel.add(comidaFinalField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Añadir Población", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String nombre = nameField.getText();
                LocalDate fechaInicio = LocalDate.parse(startDateField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDate fechaFin = LocalDate.parse(endDateField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                int numBacterias = Integer.parseInt(numBacteriasField.getText());
                double temperatura = Double.parseDouble(temperaturaField.getText());
                String luminosidad = luminosidadField.getSelectedItem().toString();
                int comidaInicial = Integer.parseInt(comidaInicialField.getText());
                int diaIncremento = Integer.parseInt(diaIncrementoField.getText());
                int comidaMaxima = Integer.parseInt(comidaMaximaField.getText());
                int comidaFinal = Integer.parseInt(comidaFinalField.getText());

                Poblacion nuevaPoblacion = new Poblacion(nombre, fechaInicio, fechaFin, numBacterias, temperatura, luminosidad, comidaInicial, diaIncremento, comidaMaxima, comidaFinal);
                currentExperiment.addPoblacion(nuevaPoblacion);
                updatePoblacionesList();
                JOptionPane.showMessageDialog(frame, "Población añadida correctamente.");
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Error en las fechas. Asegúrese de que están en el formato correcto (YYYY-MM-DD).", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Por favor, introduzca números válidos en los campos numéricos.", "Error de Número", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void removePopulation() {
        String selected = listPoblaciones.getSelectedValue();
        if (selected != null) {
            currentExperiment.removePoblacion(selected);
            updatePoblacionesList();
            JOptionPane.showMessageDialog(frame, "Población eliminada correctamente.");
        } else {
            JOptionPane.showMessageDialog(frame, "Seleccione una población para eliminar.");
        }
    }

    private static void updatePoblacionesList() {
        listModel.clear();
        if (currentExperiment != null && currentExperiment.getPoblaciones() != null) {
            for (Poblacion p : currentExperiment.getPoblaciones()) {
                listModel.addElement(p.getNombre());
            }
        }
    }

    private static JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(detailsArea);

        JButton btnShowDetails = new JButton("Mostrar Detalles");
        btnShowDetails.addActionListener(e -> {
            StringBuilder details = new StringBuilder();
            if (currentExperiment != null && currentExperiment.getPoblaciones() != null) {
                for (Poblacion poblacion : currentExperiment.getPoblaciones()) {
                    details.append("Población: ").append(poblacion.getNombre()).append("\n");
                    List<String> simulationResults = simulate(poblacion);
                    for (int i = 0; i < simulationResults.size(); i++) {
                        details.append("Día ").append(i + 1).append(": ").append(simulationResults.get(i)).append("\n");
                    }
                    details.append("\n");
                }
            }
            detailsArea.setText(details.toString());
        });

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnShowDetails, BorderLayout.SOUTH);

        return panel;
    }

    private static List<String> simulate(Poblacion poblacion) {
        List<String> results = new ArrayList<>();
        LocalDate currentDate = poblacion.getFechaInicio();
        LocalDate endDate = poblacion.getFechaFin();
        while (!currentDate.isAfter(endDate)) {
            // Simulate a day
            String result = "Fecha: " + currentDate + ", Número de Bacterias: " + poblacion.getNumBacterias() + ", Temperatura: " + poblacion.getTemperatura() + ", Luminosidad: " + poblacion.getLuminosidad() + ", Comida Inicial: " + poblacion.getComidaInicial() + ", Día de Incremento Máximo: " + poblacion.getDiaIncremento() + ", Comida Máxima en el Día de Incremento: " + poblacion.getComidaMaxima() + ", Comida Final en Día 30: " + poblacion.getComidaFinal();
            results.add(result);
            currentDate = currentDate.plusDays(1);
        }
        return results;
    }
}









