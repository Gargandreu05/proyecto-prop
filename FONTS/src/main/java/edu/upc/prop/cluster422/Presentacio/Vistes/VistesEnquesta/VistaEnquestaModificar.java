package edu.upc.prop.cluster422.Presentacio.Vistes.VistesEnquesta;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import edu.upc.prop.cluster422.Presentacio.Controladors.CtrlPresentacioEnquestes;

/**
 * Vista per a la modificacio d'una enquesta.
 * 
 * @author Andreu Puerto
 * @version 15/12/2025
 */
public class VistaEnquestaModificar extends JPanel {

    /**
     * Controlador de la presentacio d'enquestes
     */
    private CtrlPresentacioEnquestes ctrlPresentacioEnquestes;

    /**
     * Camp per a la introduccio del titol de l'enquesta
     */
    private JTextField campTitolEnquesta;

    /**
     * Model de la llista de preguntes
     */
    private DefaultListModel<String> modelLlistaPreguntes;

    /**
     * Llista visual de les preguntes
     */
    private JList<String> llistaVisualPreguntes;

    /**
     * Titol original de l'enquesta
     */
    private String titolOriginal;

    /**
     * Llista temporal d'enunciats
     */
    private ArrayList<String> enunciatsTemp;

    /**
     * Llista temporal de tipus
     */
    private ArrayList<String> tipusTemp;

    /**
     * Llista temporal d'opcions
     */
    private ArrayList<List<String>> opcionsTemp;

    private final Color COLOR_BLAU = new Color(0, 106, 176);
    private final Color COLOR_FONS_COS = new Color(240, 240, 240);
    private final Color COLOR_TARGETA = Color.WHITE;

    /**
     * Constructor de la vista.
     * 
     * @param ctrlPresentacioEnquestes Controlador de la presentacio d'enquestes.
     */
    public VistaEnquestaModificar(CtrlPresentacioEnquestes ctrlPresentacioEnquestes) {
        this.ctrlPresentacioEnquestes = ctrlPresentacioEnquestes;
        enunciatsTemp = new ArrayList<>();
        tipusTemp = new ArrayList<>();
        opcionsTemp = new ArrayList<>();

        initComponents();
    }

    /**
     * Inicialitza els components de la vista.
     */
    private void initComponents() {
        this.setLayout(new BorderLayout());

        // Carregar Icones
        ImageIcon iconAdd = ctrlPresentacioEnquestes.carregarIcona("add", 20, 20);
        ImageIcon iconEdit = ctrlPresentacioEnquestes.carregarIcona("edit", 20, 20);
        ImageIcon iconDelete = ctrlPresentacioEnquestes.carregarIcona("delete", 20, 20);
        ImageIcon iconUp = ctrlPresentacioEnquestes.carregarIcona("up", 20, 20);
        ImageIcon iconDown = ctrlPresentacioEnquestes.carregarIcona("down", 20, 20);
        ImageIcon iconSave = ctrlPresentacioEnquestes.carregarIcona("submit", 20, 20);
        ImageIcon iconBack = ctrlPresentacioEnquestes.carregarIcona("goback", 16, 16);

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_BLAU);
        panelHeader.setPreferredSize(new Dimension(800, 60));
        panelHeader.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel lblTitol = new JLabel("MODIFICAR ENQUESTA");
        lblTitol.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitol.setForeground(Color.WHITE);
        if (iconEdit != null)
            lblTitol.setIcon(new ImageIcon(iconEdit.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH)));

        panelHeader.add(lblTitol, BorderLayout.WEST);
        this.add(panelHeader, BorderLayout.NORTH);

        JPanel panelCos = new JPanel(new GridBagLayout());
        panelCos.setBackground(COLOR_FONS_COS);

        JPanel panelTargeta = new JPanel(new BorderLayout(10, 10));
        panelTargeta.setBackground(COLOR_TARGETA);
        panelTargeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(20, 30, 20, 30)));

        JPanel panelTitolInput = new JPanel(new BorderLayout(10, 0));
        panelTitolInput.setBackground(Color.WHITE);
        panelTitolInput.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel lblCampTitol = new JLabel("Títol de l'Enquesta:");
        lblCampTitol.setFont(new Font("Arial", Font.BOLD, 14));

        campTitolEnquesta = new JTextField();
        campTitolEnquesta.setPreferredSize(new Dimension(300, 30));
        campTitolEnquesta.setFont(new Font("Arial", Font.PLAIN, 14));

        panelTitolInput.add(lblCampTitol, BorderLayout.WEST);
        panelTitolInput.add(campTitolEnquesta, BorderLayout.CENTER);

        panelTargeta.add(panelTitolInput, BorderLayout.NORTH);

        modelLlistaPreguntes = new DefaultListModel<>();
        llistaVisualPreguntes = new JList<>(modelLlistaPreguntes);
        llistaVisualPreguntes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        llistaVisualPreguntes.setFixedCellHeight(30);
        llistaVisualPreguntes.setFont(new Font("Arial", Font.PLAIN, 13));

        JScrollPane scrollLlista = new JScrollPane(llistaVisualPreguntes);
        scrollLlista.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Preguntes Actuals"));

        panelTargeta.add(scrollLlista, BorderLayout.CENTER);

        JPanel panelBotonsLat = new JPanel(new GridLayout(6, 1, 5, 8));
        panelBotonsLat.setOpaque(false);
        panelBotonsLat.setBorder(new EmptyBorder(10, 10, 0, 0));

        JButton botoAfegir = new JButton("Afegir");
        if (iconAdd != null)
            botoAfegir.setIcon(iconAdd);

        JButton botoModificar = new JButton("Modificar");
        if (iconEdit != null)
            botoModificar.setIcon(iconEdit);

        JButton botoPujar = new JButton("Pujar");
        if (iconUp != null)
            botoPujar.setIcon(iconUp);

        JButton botoBaixar = new JButton("Baixar");
        if (iconDown != null)
            botoBaixar.setIcon(iconDown);

        JButton botoEsborrar = new JButton("Esborrar");
        if (iconDelete != null)
            botoEsborrar.setIcon(iconDelete);
        botoEsborrar.setForeground(Color.RED);

        panelBotonsLat.add(botoAfegir);
        panelBotonsLat.add(botoModificar);
        panelBotonsLat.add(new JSeparator());
        panelBotonsLat.add(botoPujar);
        panelBotonsLat.add(botoBaixar);
        panelBotonsLat.add(botoEsborrar);

        JPanel panelBotonsWrapper = new JPanel(new BorderLayout());
        panelBotonsWrapper.setOpaque(false);
        panelBotonsWrapper.add(panelBotonsLat, BorderLayout.NORTH);

        panelTargeta.add(panelBotonsWrapper, BorderLayout.EAST);

        JPanel panelPeuTargeta = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelPeuTargeta.setOpaque(false);
        panelPeuTargeta.setBorder(new EmptyBorder(15, 0, 0, 0));

        JButton botoCancel = new JButton("Cancel·lar");
        if (iconBack != null)
            botoCancel.setIcon(iconBack);

        JButton botoGuardar = new JButton("Guardar Canvis");
        botoGuardar.setBackground(new Color(0, 106, 176));
        botoGuardar.setForeground(Color.WHITE);
        botoGuardar.setFont(new Font("Arial", Font.BOLD, 12));
        if (iconSave != null)
            botoGuardar.setIcon(iconSave);

        panelPeuTargeta.add(botoCancel);
        panelPeuTargeta.add(botoGuardar);

        panelTargeta.add(panelPeuTargeta, BorderLayout.SOUTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(20, 20, 20, 20);
        panelCos.add(panelTargeta, gbc);

        this.add(panelCos, BorderLayout.CENTER);

        botoCancel.addActionListener(e -> ctrlPresentacioEnquestes.mostrarVistaEnquestes("menuenquesta"));

        botoAfegir.addActionListener(e -> obrirDialegPregunta(-1));
        botoModificar.addActionListener(e -> accioModificar());
        botoPujar.addActionListener(e -> mourePregunta(-1));
        botoBaixar.addActionListener(e -> mourePregunta(1));
        botoEsborrar.addActionListener(e -> accioEsborrar());

        botoGuardar.addActionListener(e -> accioGuardarCanvis());
    }

    /**
     * Carrega les dades de l'enquesta actual.
     */
    public void carregarDadesEnquestaActual() {
        enunciatsTemp.clear();
        tipusTemp.clear();
        opcionsTemp.clear();
        modelLlistaPreguntes.clear();

        String nomEnquesta = ctrlPresentacioEnquestes.getNomEnquesta();
        if (nomEnquesta == null)
            return;

        this.titolOriginal = nomEnquesta;
        campTitolEnquesta.setText(nomEnquesta);

        try {
            ArrayList<String> enunciats = ctrlPresentacioEnquestes.getPreguntesEnquesta(nomEnquesta);
            for (int i = 0; i < enunciats.size(); i++) {
                String enunc = enunciats.get(i);
                String format = ctrlPresentacioEnquestes.getFormatPregunta(nomEnquesta, i);
                List<String> opcions = ctrlPresentacioEnquestes.getOpcionsPregunta(nomEnquesta, i);

                enunciatsTemp.add(enunc);
                tipusTemp.add(format);
                opcionsTemp.add(opcions);
                modelLlistaPreguntes.addElement((i + 1) + ". [" + format + "] " + enunc);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error carregant dades: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Accio per guardar els canvis de l'enquesta.
     */
    private void accioGuardarCanvis() {
        String nouTitol = campTitolEnquesta.getText().trim();
        if (nouTitol.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El títol no pot ser buit", "Atenció", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (enunciatsTemp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "L'enquesta ha de tenir almenys una pregunta.", "Atenció",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            ctrlPresentacioEnquestes.modificarEnquestaCompleta(titolOriginal, nouTitol, enunciatsTemp, tipusTemp,
                    opcionsTemp);
            JOptionPane.showMessageDialog(this, "Canvis guardats correctament!", "Èxit",
                    JOptionPane.INFORMATION_MESSAGE);

            ctrlPresentacioEnquestes.seleccionarEnquesta(nouTitol);
            ctrlPresentacioEnquestes.mostrarVistaEnquestes("menuenquesta");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No s'han pogut guardar els canvis:\n" + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Moure una pregunta cap a dalt o cap a baix.
     * 
     * @param direccio 1 per moure cap a baix, -1 per moure cap a dalt.
     */
    private void mourePregunta(int direccio) {
        int index = llistaVisualPreguntes.getSelectedIndex();
        if (index == -1)
            return;
        int nouIndex = index + direccio;
        if (nouIndex < 0 || nouIndex >= enunciatsTemp.size())
            return;

        Collections.swap(enunciatsTemp, index, nouIndex);
        Collections.swap(tipusTemp, index, nouIndex);
        Collections.swap(opcionsTemp, index, nouIndex);

        refrescarLlista();
        llistaVisualPreguntes.setSelectedIndex(nouIndex);
    }

    /**
     * Accio per esborrar una pregunta.
     */
    private void accioEsborrar() {
        int index = llistaVisualPreguntes.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una pregunta per esborrar.", "Atenció",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        enunciatsTemp.remove(index);
        tipusTemp.remove(index);
        opcionsTemp.remove(index);
        refrescarLlista();
    }

    /**
     * Accio per modificar una pregunta. Obre una finestra per editar la pregunta
     * seleccionada.
     */
    private void accioModificar() {
        int index = llistaVisualPreguntes.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una pregunta per modificar.", "Atenció",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        obrirDialegPregunta(index);
    }

    /**
     * Refresca la llista de preguntes.
     */
    private void refrescarLlista() {
        modelLlistaPreguntes.clear();
        for (int i = 0; i < enunciatsTemp.size(); i++) {
            modelLlistaPreguntes.addElement((i + 1) + ". [" + tipusTemp.get(i) + "] " + enunciatsTemp.get(i));
        }
    }

    /**
     * Obrir un dialeg per editar una pregunta.
     * 
     * @param indexEdit Index de la pregunta a editar.
     */
    private void obrirDialegPregunta(int indexEdit) {
        JPanel panelFormulari = new JPanel(new GridLayout(0, 1, 5, 5));
        JTextField fieldEnunciat = new JTextField();
        JComboBox<String> comboTipus = new JComboBox<>(new String[] { "LLIURE", "NUMERIC", "UNIC", "MULTIPLE" });
        JTextArea areaOpcions = new JTextArea(5, 20);
        JScrollPane scrollOpcions = new JScrollPane(areaOpcions);

        if (indexEdit != -1) {
            fieldEnunciat.setText(enunciatsTemp.get(indexEdit));
            comboTipus.setSelectedItem(tipusTemp.get(indexEdit));
            List<String> ops = opcionsTemp.get(indexEdit);
            StringBuilder sb = new StringBuilder();
            for (String op : ops)
                sb.append(op).append("\n");
            areaOpcions.setText(sb.toString());
        }

        panelFormulari.add(new JLabel("Enunciat:"));
        panelFormulari.add(fieldEnunciat);
        panelFormulari.add(new JLabel("Tipus:"));
        panelFormulari.add(comboTipus);
        panelFormulari.add(new JLabel("Opcions (per UNIC/MULTIPLE):"));
        panelFormulari.add(scrollOpcions);

        String titol = (indexEdit == -1) ? "Nova Pregunta" : "Editar Pregunta";
        int result = JOptionPane.showConfirmDialog(this, panelFormulari, titol, JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            guardarDadesPregunta(indexEdit, fieldEnunciat.getText(), (String) comboTipus.getSelectedItem(),
                    areaOpcions.getText());
        }
    }

    /**
     * Guarda les dades d'una pregunta.
     * 
     * @param index       Index de la pregunta a editar.
     * @param enunciat    Enunciat de la pregunta.
     * @param tipus       Tipus de la pregunta.
     * @param textOpcions Text amb les opcions de la pregunta.
     */
    private void guardarDadesPregunta(int index, String enunciat, String tipus, String textOpcions) {
        if (enunciat.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "L'enunciat és obligatori.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<String> llistaOpcions = new ArrayList<>();
        if (tipus.equals("UNIC") || tipus.equals("MULTIPLE")) {
            for (String l : textOpcions.split("\\n")) {
                if (!l.trim().isEmpty())
                    llistaOpcions.add(l.trim());
            }
            if (llistaOpcions.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Falten opcions!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (index == -1) {
            enunciatsTemp.add(enunciat);
            tipusTemp.add(tipus);
            opcionsTemp.add(llistaOpcions);
        } else {
            enunciatsTemp.set(index, enunciat);
            tipusTemp.set(index, tipus);
            opcionsTemp.set(index, llistaOpcions);
        }
        refrescarLlista();
    }

}