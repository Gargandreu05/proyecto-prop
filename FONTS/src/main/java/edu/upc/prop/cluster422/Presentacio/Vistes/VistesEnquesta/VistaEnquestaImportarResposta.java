package edu.upc.prop.cluster422.Presentacio.Vistes.VistesEnquesta;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import edu.upc.prop.cluster422.Presentacio.Controladors.CtrlPresentacioEnquestes;

/**
 * Vista per a importar respostes d'una enquesta. La enquesta ja ve seleccionada
 * prèviament.
 * 
 * @author Andreu Puerto
 * @version 15/12/2025
 */
public class VistaEnquestaImportarResposta extends JPanel {

    /**
     * Controlador de la presentacio d'enquestes
     */
    private CtrlPresentacioEnquestes ctrlPresentacioEnquestes;

    /**
     * Camp per a la introduccio del nom del fitxer
     */
    private JTextField campNomFitxer;

    // Colors Corporatius
    private final Color COLOR_BLAU = new Color(0, 106, 176);
    private final Color COLOR_FONS_COS = new Color(240, 240, 240);
    private final Color COLOR_TARGETA = Color.WHITE;

    /**
     * Constructor de la vista per a importar respostes d'una enquesta.
     * 
     * @param ctrlPresentacioEnquestes
     */
    public VistaEnquestaImportarResposta(CtrlPresentacioEnquestes ctrlPresentacioEnquestes) {
        this.ctrlPresentacioEnquestes = ctrlPresentacioEnquestes;
        inicialitzarComponent();
    }

    /**
     * Inicialitza els components de la vista.
     */
    private void inicialitzarComponent() {
        this.setLayout(new BorderLayout());

        // Carregar Icones
        ImageIcon iconFolder = ctrlPresentacioEnquestes.carregarIcona("add", 16, 16); // Icona per examinar
        ImageIcon iconImport = ctrlPresentacioEnquestes.carregarIcona("down", 24, 24); // Icona header (importar)
        ImageIcon iconBack = ctrlPresentacioEnquestes.carregarIcona("goback", 16, 16);

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_BLAU);
        panelHeader.setPreferredSize(new Dimension(800, 60));
        panelHeader.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel labelTitol = new JLabel("IMPORTAR RESPOSTES");
        labelTitol.setFont(new Font("Arial", Font.BOLD, 22));
        labelTitol.setForeground(Color.WHITE);
        if (iconImport != null)
            labelTitol.setIcon(iconImport);

        panelHeader.add(labelTitol, BorderLayout.WEST);
        this.add(panelHeader, BorderLayout.NORTH);

        JPanel panelCos = new JPanel(new GridBagLayout());
        panelCos.setBackground(COLOR_FONS_COS);

        JPanel panelTargeta = new JPanel(new GridBagLayout());
        panelTargeta.setBackground(COLOR_TARGETA);
        panelTargeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(40, 50, 40, 50)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        JLabel labelInstruccions = new JLabel(
                "Selecciona un fitxer de respostes (.txt o .csv) per afegir a l'enquesta actual.");
        labelInstruccions.setFont(new Font("Arial", Font.PLAIN, 14));
        labelInstruccions.setForeground(Color.DARK_GRAY);

        gbc.gridy = 0;
        gbc.gridwidth = 3;
        panelTargeta.add(labelInstruccions, gbc);

        JLabel labelFitxer = new JLabel("Fitxer:");
        labelFitxer.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        panelTargeta.add(labelFitxer, gbc);

        campNomFitxer = new JTextField(25);
        campNomFitxer.setEditable(false);
        campNomFitxer.setPreferredSize(new Dimension(300, 30));
        campNomFitxer.setBackground(Color.WHITE);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panelTargeta.add(campNomFitxer, gbc);

        JButton botoSeleccionar = new JButton("Examinar...");
        if (iconFolder != null)
            botoSeleccionar.setIcon(iconFolder);
        gbc.gridx = 2;
        gbc.weightx = 0;
        panelTargeta.add(botoSeleccionar, gbc);

        // Botons d'Acció (Peu de la targeta)
        JPanel panelBotons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotons.setOpaque(false);

        JButton botoEnrere = new JButton("Cancel·lar");
        if (iconBack != null)
            botoEnrere.setIcon(iconBack);

        JButton botoImportar = new JButton("Importar Respostes");
        botoImportar.setBackground(new Color(0, 106, 176)); // Blau UPC
        botoImportar.setForeground(Color.WHITE);
        botoImportar.setFont(new Font("Arial", Font.BOLD, 12));
        // Si tens icona 'submit' o 'check', pots posar-la aquí
        // if (iconSubmit != null) botoImportar.setIcon(iconSubmit);

        panelBotons.add(botoEnrere);
        panelBotons.add(botoImportar);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(30, 0, 0, 0);
        panelTargeta.add(panelBotons, gbc);

        panelCos.add(panelTargeta);
        this.add(panelCos, BorderLayout.CENTER);

        // ==========================================
        // LISTENERS
        // ==========================================
        botoEnrere.addActionListener(e -> {
            campNomFitxer.setText("");
            ctrlPresentacioEnquestes.mostrarVistaEnquestes("menuenquesta");
        });

        botoSeleccionar.addActionListener(e -> obrirExploradorFitxers());

        botoImportar.addActionListener(e -> executarImportacio());
    }

    /**
     * Obre un explorador de fitxers per seleccionar un fitxer de respostes.
     */
    private void obrirExploradorFitxers() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona el fitxer de Respostes");

        try {
            Path currentPath = Paths.get(System.getProperty("user.dir"));
            File carpetaExe = currentPath.getParent().resolve("EXE").toFile();
            if (carpetaExe.exists()) {
                fileChooser.setCurrentDirectory(carpetaExe);
            }
        } catch (Exception e) {

        }

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Fitxers de respostes (*.txt)", "txt");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToImport = fileChooser.getSelectedFile();
            campNomFitxer.setText(fileToImport.getName());
        }
    }

    /**
     * Executa la importació del fitxer seleccionat i guarda la enquesta a
     * l'aplicació.
     */
    private void executarImportacio() {
        String nomFitxer = campNomFitxer.getText().trim();

        if (nomFitxer.isEmpty()) {
            ImageIcon icon = ctrlPresentacioEnquestes.carregarIcona("warning", 32, 32);
            JOptionPane.showMessageDialog(this, "Has de seleccionar un fitxer primer.", "Atenció",
                    JOptionPane.WARNING_MESSAGE, icon);
            return;
        }

        try {
            ctrlPresentacioEnquestes.importarResposta(ctrlPresentacioEnquestes.getNomEnquesta(), nomFitxer);

            ImageIcon icon = ctrlPresentacioEnquestes.carregarIcona("success", 32, 32);
            JOptionPane.showMessageDialog(this, "Respostes importades correctament!", "Èxit",
                    JOptionPane.INFORMATION_MESSAGE, icon);

            campNomFitxer.setText("");
            ctrlPresentacioEnquestes.mostrarVistaEnquestes("menuenquesta");

        } catch (Exception e) {
            ImageIcon icon = ctrlPresentacioEnquestes.carregarIcona("error", 32, 32);
            JOptionPane.showMessageDialog(this, "Error en importar:\n" + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE, icon);
        }
    }
}