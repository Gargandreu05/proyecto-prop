package edu.upc.prop.cluster422.Presentacio.Vistes.VistesEnquesta;

import javax.swing.*;
import edu.upc.prop.cluster422.Presentacio.Controladors.CtrlPresentacioEnquestes;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Vista per crear una nova enquesta.
 * Aquesta vista permet crear una nova enquesta amb les seves preguntes i
 * opcions.
 * 
 * @author Andreu Puerto
 * 
 * @version 15/12/2025
 */
public class VistaCrearEnquesta extends JPanel {
    /**
     * Controlador de la presentacio d'enquestes
     */
    private CtrlPresentacioEnquestes ctrlPresentacioEnquestes;

    /**
     * Camp de text per a l'identificador de l'enquesta
     */
    private JTextField campTitolEnquesta;

    /**
     * Model de la llista de preguntes
     */
    private DefaultListModel<String> modelLlistaPreguntes;

    /**
     * Llista de preguntes
     */
    private JList<String> llistaVisualPreguntes;

    /**
     * Botó per afegir una pregunta
     */
    private JButton botoAfegirPregunta;

    /**
     * Botó per esborrar una pregunta
     */
    private JButton botoEsborrarPregunta;

    /**
     * Botó per modificar una pregunta
     */
    private JButton botoModificarPregunta;

    /**
     * Botó per pujar una pregunta
     */
    private JButton botoPujar;

    /**
     * Botó per baixar una pregunta
     */
    private JButton botoBaixar;

    /**
     * Botó per guardar l'enquesta
     */
    private JButton botoGuardar;

    /**
     * Botó per anar enrere
     */
    private JButton botoEnrere;

    /**
     * Enunciats temporals per a la creacio d'una enquesta
     */
    private ArrayList<String> enunciatsTemp;

    /**
     * Tipus temporals per a la creacio d'una enquesta
     */
    private ArrayList<String> tipusTemp;

    /**
     * Opcions temporals per a la creacio d'una enquesta
     */
    private ArrayList<List<String>> opcionsTemp;

    /**
     * Constructor de la vista de crear enquesta.
     * 
     * @param ctrlPresentacioEnquestes Controlador de la presentacio d'enquestes.
     */
    public VistaCrearEnquesta(CtrlPresentacioEnquestes ctrlPresentacioEnquestes) {
        this.ctrlPresentacioEnquestes = ctrlPresentacioEnquestes;
        enunciatsTemp = new ArrayList<>();
        tipusTemp = new ArrayList<>();
        opcionsTemp = new ArrayList<>();
        inicialitzarComponent();
    }

    /**
     * Inicialitza els components de la vista.
     */
    private void inicialitzarComponent() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        ImageIcon iconAfegir = ctrlPresentacioEnquestes.carregarIcona("add", 20, 20);
        ImageIcon iconModificar = ctrlPresentacioEnquestes.carregarIcona("edit", 20, 20);
        ImageIcon iconEsborrar = ctrlPresentacioEnquestes.carregarIcona("delete", 20, 20);
        ImageIcon iconPujar = ctrlPresentacioEnquestes.carregarIcona("up", 20, 20);
        ImageIcon iconBaixar = ctrlPresentacioEnquestes.carregarIcona("down", 20, 20);
        ImageIcon iconGuardar = ctrlPresentacioEnquestes.carregarIcona("submit", 20, 20);
        ImageIcon iconEnrere = ctrlPresentacioEnquestes.carregarIcona("goback", 20, 20);

        JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));
        JLabel lblTitol = new JLabel("CREAR NOVA ENQUESTA", SwingConstants.CENTER);
        lblTitol.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitol.setForeground(new Color(0, 106, 176));

        JPanel panelCampTitol = new JPanel(new BorderLayout());
        panelCampTitol.add(new JLabel("Títol de l'Enquesta: "), BorderLayout.WEST);
        campTitolEnquesta = new JTextField();
        panelCampTitol.add(campTitolEnquesta, BorderLayout.CENTER);

        panelSuperior.add(lblTitol, BorderLayout.NORTH);
        panelSuperior.add(panelCampTitol, BorderLayout.SOUTH);
        this.add(panelSuperior, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));

        modelLlistaPreguntes = new DefaultListModel<>();
        llistaVisualPreguntes = new JList<>(modelLlistaPreguntes);
        llistaVisualPreguntes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        llistaVisualPreguntes.setFixedCellHeight(25);

        JScrollPane scrollLlista = new JScrollPane(llistaVisualPreguntes);
        scrollLlista.setBorder(BorderFactory.createTitledBorder("Preguntes Afegides"));

        JPanel panelAccionsLlista = new JPanel(new GridLayout(6, 1, 5, 5));
        botoAfegirPregunta = new JButton("Afegir Nova");
        if (iconAfegir != null)
            botoAfegirPregunta.setIcon(iconAfegir);

        botoModificarPregunta = new JButton("Modificar");
        if (iconModificar != null)
            botoModificarPregunta.setIcon(iconModificar);

        botoEsborrarPregunta = new JButton("Esborrar");
        if (iconEsborrar != null)
            botoEsborrarPregunta.setIcon(iconEsborrar);
        botoEsborrarPregunta.setForeground(Color.RED);

        botoPujar = new JButton("Pujar");
        if (iconPujar != null)
            botoPujar.setIcon(iconPujar);

        botoBaixar = new JButton("Baixar");
        if (iconBaixar != null)
            botoBaixar.setIcon(iconBaixar);

        panelAccionsLlista.add(botoAfegirPregunta);
        panelAccionsLlista.add(botoModificarPregunta);
        panelAccionsLlista.add(new JSeparator());
        panelAccionsLlista.add(botoPujar);
        panelAccionsLlista.add(botoBaixar);
        panelAccionsLlista.add(botoEsborrarPregunta);

        panelCentral.add(scrollLlista, BorderLayout.CENTER);

        JPanel containerBotons = new JPanel(new BorderLayout());
        containerBotons.add(panelAccionsLlista, BorderLayout.NORTH);
        panelCentral.add(containerBotons, BorderLayout.EAST);

        this.add(panelCentral, BorderLayout.CENTER);

        JPanel panelBotons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoGuardar = new JButton("Guardar Enquesta");
        botoGuardar.setBackground(new Color(200, 230, 255));
        if (iconGuardar != null)
            botoGuardar.setIcon(iconGuardar);

        botoEnrere = new JButton("Cancel·lar");
        if (iconEnrere != null)
            botoEnrere.setIcon(iconEnrere);

        panelBotons.add(botoEnrere);
        panelBotons.add(botoGuardar);
        this.add(panelBotons, BorderLayout.SOUTH);

        botoEnrere.addActionListener(e -> {
            sortirSenseGuardar();
            ctrlPresentacioEnquestes.mostrarVistaEnquestes("gestioenquestes");
        });

        botoAfegirPregunta.addActionListener(e -> obrirDialegPregunta(-1));
        botoModificarPregunta.addActionListener(e -> accioModificar());
        botoEsborrarPregunta.addActionListener(e -> accioEsborrar());
        botoPujar.addActionListener(e -> mourePregunta(-1));
        botoBaixar.addActionListener(e -> mourePregunta(1));

        botoGuardar.addActionListener(e -> {
            guardarEnquesta();
        });
    }

    /**
     * Moure una pregunta cap a la posició seleccionada.
     * Només permet moure una pregunta cap a la posició anterior o següent si la
     * posició seleccionada és vàlida.
     * 
     * @param direccio -1 per moure cap a la posició anterior, 1 per moure cap a la
     *                 posició següent.
     * 
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

        refrescarLlistaVisual();
        llistaVisualPreguntes.setSelectedIndex(nouIndex);
    }

    /**
     * Esborra la pregunta seleccionada.
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
        refrescarLlistaVisual();
    }

    /**
     * Modifica la pregunta seleccionada.
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
     * Obrir un dialeg per editar una pregunta.
     * 
     * @param indexEdit L'índex de la pregunta a editar. Si és -1, s'obrirà un
     *                  dialeg per afegir una nova pregunta.
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
        panelFormulari.add(new JLabel("Opcions (una per línia, per UNIC/MULTIPLE):"));
        panelFormulari.add(scrollOpcions);

        String titolDialog = (indexEdit == -1) ? "Nova Pregunta" : "Editar Pregunta";
        int result = JOptionPane.showConfirmDialog(this, panelFormulari, titolDialog, JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            guardarDadesPregunta(indexEdit, fieldEnunciat.getText(), (String) comboTipus.getSelectedItem(),
                    areaOpcions.getText());
        }
    }

    /**
     * Guarda les dades d'una pregunta. Deixa de ser temporal i passa a ser
     * permanent.
     * 
     * @param index       Index de la pregunta.
     * @param enunciat    Enunciat de la pregunta.
     * @param tipus       Tipus de la pregunta.
     * @param textOpcions Text de les opcions de la pregunta.
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
                JOptionPane.showMessageDialog(this, "Aquest tipus requereix opcions!", "Error",
                        JOptionPane.ERROR_MESSAGE);
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
        refrescarLlistaVisual();
    }

    /**
     * Refresca la llista visual de preguntes quant es modifica una pregunta.
     */
    private void refrescarLlistaVisual() {
        modelLlistaPreguntes.clear();
        for (int i = 0; i < enunciatsTemp.size(); i++) {
            String resum = (i + 1) + ". [" + tipusTemp.get(i) + "] " + enunciatsTemp.get(i);
            modelLlistaPreguntes.addElement(resum);
        }
    }

    /**
     * Guarda l'enquesta a l'aplicacio.
     */
    private void guardarEnquesta() {
        String titol = campTitolEnquesta.getText().trim();
        if (titol.isEmpty() || enunciatsTemp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Títol o preguntes buides!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            ctrlPresentacioEnquestes.crearEnquestaCompleta(titol, enunciatsTemp, tipusTemp, opcionsTemp);
            JOptionPane.showMessageDialog(this, "Enquesta guardada correctament!", "Èxit",
                    JOptionPane.INFORMATION_MESSAGE);
            sortirSenseGuardar();
            ctrlPresentacioEnquestes.mostrarVistaEnquestes("gestioenquestes");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Sortir sense guardar les modificacions. Fa reset a les dades temporals.
     */
    private void sortirSenseGuardar() {
        campTitolEnquesta.setText("");
        enunciatsTemp.clear();
        tipusTemp.clear();
        opcionsTemp.clear();
        modelLlistaPreguntes.clear();
    }
}