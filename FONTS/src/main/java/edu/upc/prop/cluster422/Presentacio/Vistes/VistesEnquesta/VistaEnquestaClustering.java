package edu.upc.prop.cluster422.Presentacio.Vistes.VistesEnquesta;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import edu.upc.prop.cluster422.Presentacio.Controladors.CtrlPresentacioEnquestes;
import java.awt.*;
import java.util.Map;
import java.util.List;

/**
 * Vista per a executar i visualitzar l'algorisme de clustering sobre una
 * enquesta.
 * * @author Andreu Puerto
 * 
 * @version 12/12/2025
 */
public class VistaEnquestaClustering extends JPanel {

    /**
     * Controlador de la presentació d'enquestes
     */
    private CtrlPresentacioEnquestes ctrlPresentacioEnquestes;

    /**
     * ComboBox per a la selecció de l'algorisme
     */
    private JComboBox<String> comboAlgoritme;

    /**
     * Radio buttons per a la selecció de k
     */
    private JRadioButton radioManual, radioOptima, radioAleatoria;

    /**
     * Spinner per a la selecció de k
     */
    private JSpinner spinnerK;

    /**
     * Grup de radio buttons per a la selecció de k
     */
    private ButtonGroup grupK;

    /**
     * Area de text per a la visualització dels resultats
     */
    private JTextArea areaResultats;

    /**
     * Etiqueta per a la visualització de la qualitat del clustering
     */
    private JLabel labelSilhouette;

    /**
     * Etiqueta per a la visualització del temps d'execució
     */
    private JLabel labelTempsExecucio;

    /**
     * Vista del gràfic
     */
    private VistaGrafic vistaGrafic;

    /**
     * Pestanyes per a la selecció de la vista
     */
    private JTabbedPane pestanyes;

    /**
     * Llista de representants del clustering
     */
    private List<String> representants;

    private final Color COLOR_BLAU = new Color(0, 106, 176);
    private final Color COLOR_FONS_COS = new Color(240, 240, 240);
    private final Color COLOR_TARGETA = Color.WHITE;

    /**
     * Constructor de la vista de clustering.
     * 
     * @param ctrlPresentacioEnquestes
     */
    public VistaEnquestaClustering(CtrlPresentacioEnquestes ctrlPresentacioEnquestes) {
        this.ctrlPresentacioEnquestes = ctrlPresentacioEnquestes;
        initComponents();
    }

    /**
     * Inicialitza els components de la vista.
     * Inicialitza la vista del gràfic també.
     */
    private void initComponents() {
        this.setLayout(new BorderLayout());

        ImageIcon iconCluster = ctrlPresentacioEnquestes.carregarIcona("cluster", 24, 24);
        ImageIcon iconRun = ctrlPresentacioEnquestes.carregarIcona("submit", 16, 16);
        ImageIcon iconBack = ctrlPresentacioEnquestes.carregarIcona("goback", 16, 16);

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_BLAU);
        panelHeader.setPreferredSize(new Dimension(800, 60));
        panelHeader.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel labelTitol = new JLabel("ANÀLISI DE DADES (CLUSTERING)");
        labelTitol.setFont(new Font("Arial", Font.BOLD, 20));
        labelTitol.setForeground(Color.WHITE);
        if (iconCluster != null)
            labelTitol.setIcon(iconCluster);

        JButton botoTornar = new JButton("Tornar al Menú");
        estilitzarBotoHeader(botoTornar);
        if (iconBack != null)
            botoTornar.setIcon(iconBack);

        panelHeader.add(labelTitol, BorderLayout.WEST);

        JPanel pRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pRight.setOpaque(false);
        pRight.add(botoTornar);
        panelHeader.add(pRight, BorderLayout.EAST);

        this.add(panelHeader, BorderLayout.NORTH);

        JPanel panelCos = new JPanel(new GridBagLayout());
        panelCos.setBackground(COLOR_FONS_COS);

        JPanel panelTargeta = new JPanel(new BorderLayout(20, 20));
        panelTargeta.setBackground(COLOR_TARGETA);
        panelTargeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(30, 30, 30, 30)));

        JPanel panelConfig = new JPanel(new GridBagLayout());
        panelConfig.setBackground(Color.WHITE);
        panelConfig.setBorder(createTitledBorder("Configuració de l'Algorisme"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelConfig.add(new JLabel("Algorisme:"), gbc);

        String[] algoritmes = { "K-Means", "K-Means++", "K-Medoids", "Jeràrquic Aglomeratiu" };
        comboAlgoritme = new JComboBox<>(algoritmes);
        comboAlgoritme.setBackground(Color.WHITE);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panelConfig.add(comboAlgoritme, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panelConfig.add(new JLabel("Nombre de Clústers (k):"), gbc);

        JPanel panelRadios = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelRadios.setOpaque(false);

        radioManual = new JRadioButton("Manual");
        radioManual.setBackground(Color.WHITE);
        radioManual.setSelected(true);

        spinnerK = new JSpinner(new SpinnerNumberModel(2, 2, 50, 1));
        spinnerK.setPreferredSize(new Dimension(60, 25));

        radioOptima = new JRadioButton("Calculada (K Òptima)");
        radioOptima.setBackground(Color.WHITE);
        radioOptima.setToolTipText("El sistema provarà diferents k i triarà la millor qualitat");

        radioAleatoria = new JRadioButton("Aleatòria");
        radioAleatoria.setBackground(Color.WHITE);

        grupK = new ButtonGroup();
        grupK.add(radioManual);
        grupK.add(radioOptima);
        grupK.add(radioAleatoria);

        panelRadios.add(radioManual);
        panelRadios.add(spinnerK);
        panelRadios.add(radioOptima);
        panelRadios.add(radioAleatoria);

        gbc.gridx = 1;
        panelConfig.add(panelRadios, gbc);

        JButton botoExecutar = new JButton("EXECUTAR ANÀLISI");
        botoExecutar.setBackground(new Color(0, 150, 100));
        botoExecutar.setForeground(Color.WHITE);
        botoExecutar.setFont(new Font("Arial", Font.BOLD, 14));
        botoExecutar.setFocusPainted(false);
        if (iconRun != null)
            botoExecutar.setIcon(iconRun);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 10, 0);
        panelConfig.add(botoExecutar, gbc);

        panelTargeta.add(panelConfig, BorderLayout.NORTH);

        JPanel panelResultatsBase = new JPanel(new BorderLayout(10, 10));
        panelResultatsBase.setBackground(Color.WHITE);
        panelResultatsBase.setBorder(createTitledBorder("Resultats de l'Anàlisi"));
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 10));
        panelInfo.setBackground(new Color(245, 250, 255));
        panelInfo.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        labelSilhouette = new JLabel("Qualitat (Silhouette): -");
        labelSilhouette.setFont(new Font("Arial", Font.BOLD, 14));
        labelSilhouette.setForeground(Color.DARK_GRAY);

        labelTempsExecucio = new JLabel("Temps: - ms");
        labelTempsExecucio.setFont(new Font("Arial", Font.PLAIN, 12));

        panelInfo.add(labelSilhouette);
        panelInfo.add(labelTempsExecucio);
        panelResultatsBase.add(panelInfo, BorderLayout.NORTH);

        pestanyes = new JTabbedPane();

        areaResultats = new JTextArea();
        areaResultats.setEditable(false);
        areaResultats.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaResultats.setText("Prem 'Executar' per veure els resultats...");
        JScrollPane scrollResultats = new JScrollPane(areaResultats);

        vistaGrafic = new VistaGrafic();

        pestanyes.addTab("Informe Textual", scrollResultats);
        pestanyes.addTab("Visualització Gràfica", vistaGrafic);

        panelResultatsBase.add(pestanyes, BorderLayout.CENTER);
        panelTargeta.add(panelResultatsBase, BorderLayout.CENTER);

        GridBagConstraints gbcCos = new GridBagConstraints();
        gbcCos.fill = GridBagConstraints.BOTH;
        gbcCos.weightx = 1.0;
        gbcCos.weighty = 1.0;
        gbcCos.insets = new Insets(20, 20, 20, 20);
        panelCos.add(panelTargeta, gbcCos);

        this.add(panelCos, BorderLayout.CENTER);

        java.awt.event.ActionListener radioListener = e -> spinnerK.setEnabled(radioManual.isSelected());
        radioManual.addActionListener(radioListener);
        radioOptima.addActionListener(radioListener);
        radioAleatoria.addActionListener(radioListener);

        botoTornar.addActionListener(e -> {
            areaResultats.setText("Prem 'Executar' per veure els resultats...");
            vistaGrafic.actualitzarDades(null);
            labelSilhouette.setText("Qualitat (Silhouette): -");
            labelSilhouette.setForeground(Color.DARK_GRAY);
            labelTempsExecucio.setText("Temps: - ms");
            representants = null;
            ctrlPresentacioEnquestes.mostrarVistaEnquestes("menuenquesta");
        });
        botoExecutar.addActionListener(e -> executarClustering());
    }

    /**
     * Executa l'algorisme de clustering seleccionat i mostra els resultats.
     */
    private void executarClustering() {
        int selectedAlgIndex = comboAlgoritme.getSelectedIndex() + 1;
        int kValue = 0;

        if (radioManual.isSelected()) {
            kValue = (Integer) spinnerK.getValue();
        } else if (radioOptima.isSelected()) {
            kValue = 0;
        } else {
            kValue = -1;
        }
        System.out.println("Executant clustering amb algoritme " + selectedAlgIndex + " i k = " + kValue);

        areaResultats.setText("Processant algorisme...\nSi us plau espera.");

        areaResultats.paintImmediately(areaResultats.getVisibleRect());

        try {
            long startTime = System.currentTimeMillis();

            Map<Integer, List<String>> resultatMap = ctrlPresentacioEnquestes.executarClustering(selectedAlgIndex,
                    kValue);
            double silhouette = ctrlPresentacioEnquestes.getUltimaSilhouette();

            long endTime = System.currentTimeMillis();
            representants = ctrlPresentacioEnquestes.getRepresentantsCluster();
            String reportText = generarReportText(resultatMap, silhouette);

            areaResultats.setText(reportText);
            areaResultats.setCaretPosition(0);
            Map<Integer, List<String>> resultatMapAux = resultatMap;
            if (representants != null && resultatMapAux != null) {
                for (int i = 0; i < representants.size(); i++) {
                    String nomRep = representants.get(i);
                    if (resultatMapAux.containsKey(i)) {
                        List<String> membres = resultatMapAux.get(i);
                        if (!membres.contains(nomRep)) {
                            membres.add(0, nomRep);
                        }
                    }
                }
            }

            vistaGrafic.actualitzarDades(resultatMapAux);
            labelSilhouette.setText(String.format("Qualitat (Silhouette): %.4f", silhouette));

            if (silhouette > 0.7)
                labelSilhouette.setForeground(new Color(0, 150, 0));
            else if (silhouette > 0.4)
                labelSilhouette.setForeground(new Color(200, 150, 0));
            else
                labelSilhouette.setForeground(Color.RED);

            labelTempsExecucio.setText("Temps: " + (endTime - startTime) + " ms");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error durant el clustering:\n" + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            areaResultats.setText("Error en l'execució.");
        }
    }

    /**
     * Funció auxiliar que serveix per a estilitzar els botons.
     * 
     * @param boto Botó a estilitzar
     */
    private void estilitzarBotoHeader(JButton boto) {
        boto.setForeground(Color.WHITE);
        boto.setBackground(new Color(0, 80, 150));
        boto.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        boto.setFocusPainted(false);
        boto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boto.setFont(new Font("Arial", Font.PLAIN, 12));

        boto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boto.setBackground(new Color(0, 100, 180));
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                boto.setBackground(new Color(0, 80, 150));
            }
        });
    }

    /**
     * Funció auxiliar que serveix per a crear un TitledBorder.
     * 
     * @param title Títol del TitledBorder.
     * @return TitledBorder
     */
    private TitledBorder createTitledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), title);
        border.setTitleFont(new Font("Arial", Font.BOLD, 14));
        border.setTitleColor(COLOR_BLAU);
        return border;
    }

    /**
     * Genera un report text amb el resum del clustering. Després de generar el
     * report text, es mostra en el JTextArea i es mostra el gràfic.
     * 
     * @param map        Mapa amb les dades del clustering.
     * @param silhouette Silhouette del clustering.
     * @return String amb el report text.
     */
    private String generarReportText(Map<Integer, List<String>> map, double silhouette) {
        StringBuilder sb = new StringBuilder();

        sb.append("=========================================\n");
        sb.append("       INFORME DE CLUSTERING\n");
        sb.append("=========================================\n\n");

        sb.append("RESUM GENERAL:\n");
        if (map != null) {
            sb.append(" - Total Clústers: ").append(map.size()).append("\n");
        }
        sb.append(String.format(" - Qualitat (Silhouette): %.4f\n", silhouette));

        if (silhouette > 0.7)
            sb.append("   -> Interpretació: Excel·lent separació.\n");
        else if (silhouette > 0.5)
            sb.append("   -> Interpretació: Bona estructura.\n");
        else if (silhouette > 0.25)
            sb.append("   -> Interpretació: Estructura feble.\n");
        else
            sb.append("   -> Interpretació: Sense estructura clara.\n");

        sb.append("\n-----------------------------------------\n");
        sb.append("DETALL PER GRUPS:\n");
        sb.append("-----------------------------------------\n");

        if (map != null) {
            List<Integer> keys = new java.util.ArrayList<>(map.keySet());
            java.util.Collections.sort(keys);

            for (Integer clusterId : keys) {

                List<String> usuaris = map.get(clusterId);
                sb.append("\n[CLÚSTER ").append(clusterId).append("] -> ").append(usuaris.size()).append(" usuaris\n");

                if (representants != null && clusterId < representants.size()) {
                    sb.append(" * MEDOIDE (Representant): ").append(representants.get(clusterId)).append("\n");
                }
                sb.append(" Membres: ");

                if (usuaris.isEmpty()) {
                    sb.append("(Buit)");
                } else {
                    if (usuaris.size() > 50) {
                        sb.append(String.join(", ", usuaris.subList(0, 50))).append("... (+")
                                .append(usuaris.size() - 50).append(" més)");
                    } else {
                        sb.append(String.join(", ", usuaris));
                    }
                }
                sb.append("\n");
            }
        }
        sb.append("\n=========================================\n");
        sb.append("Fi de l'informe.");
        return sb.toString();
    }
}