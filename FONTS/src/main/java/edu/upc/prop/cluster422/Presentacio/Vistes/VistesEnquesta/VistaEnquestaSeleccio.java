package edu.upc.prop.cluster422.Presentacio.Vistes.VistesEnquesta;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import edu.upc.prop.cluster422.Presentacio.Controladors.CtrlPresentacioEnquestes;

/**
 * Vista per seleccionar una enquesta. Mostra una llista d'enquestes i permet
 * seleccionar una per gestionar-la.
 * 
 * @author Andreu Puerto
 * @version 15/12/2025
 */
public class VistaEnquestaSeleccio extends JPanel {

    /**
     * Controlador de la presentació d'enquestes
     */
    private CtrlPresentacioEnquestes ctrlPresentacioEnquestes;

    /**
     * Llista d'enquestes
     */
    private JList<String> llistaEnquestes;

    /**
     * Model de la llista d'enquestes
     */
    private DefaultListModel<String> listModel;

    // Colors Corporatius
    private final Color COLOR_BLAU = new Color(0, 106, 176);
    private final Color COLOR_FONS_COS = new Color(240, 240, 240);
    private final Color COLOR_TARGETA = Color.WHITE;

    /**
     * Constructor de la vista per seleccionar una enquesta.
     * 
     * @param ctrlPresentacioEnquestes Controlador de la vista.
     */
    public VistaEnquestaSeleccio(CtrlPresentacioEnquestes ctrlPresentacioEnquestes) {
        this.ctrlPresentacioEnquestes = ctrlPresentacioEnquestes;
        inicialitzarComponent();
    }

    /**
     * Inicialitza els components de la vista.
     */
    private void inicialitzarComponent() {
        this.setLayout(new BorderLayout());

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_BLAU);
        panelHeader.setPreferredSize(new Dimension(800, 60));
        panelHeader.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel labelTitol = new JLabel("SELECCIONAR ENQUESTA");
        labelTitol.setFont(new Font("Arial", Font.BOLD, 22));
        labelTitol.setForeground(Color.WHITE);

        ImageIcon iconSearch = ctrlPresentacioEnquestes.carregarIcona("info", 24, 24);
        if (iconSearch != null)
            labelTitol.setIcon(iconSearch);

        panelHeader.add(labelTitol, BorderLayout.WEST);
        this.add(panelHeader, BorderLayout.NORTH);

        JPanel panelCos = new JPanel(new GridBagLayout());
        panelCos.setBackground(COLOR_FONS_COS);

        JPanel panelTargeta = new JPanel(new BorderLayout(0, 15));
        panelTargeta.setBackground(COLOR_TARGETA);
        panelTargeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(30, 40, 30, 40)));
        panelTargeta.setPreferredSize(new Dimension(500, 400)); // Mida fixa maca

        JLabel labelInstruccio = new JLabel("Tria l'enquesta que vols gestionar:");
        labelInstruccio.setFont(new Font("Arial", Font.PLAIN, 16));
        labelInstruccio.setForeground(Color.DARK_GRAY);
        panelTargeta.add(labelInstruccio, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        llistaEnquestes = new JList<>(listModel);
        llistaEnquestes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        llistaEnquestes.setFont(new Font("Arial", Font.PLAIN, 14));
        llistaEnquestes.setFixedCellHeight(30);

        JScrollPane scrollPane = new JScrollPane(llistaEnquestes);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panelTargeta.add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotons.setOpaque(false);

        JButton botoEnrere = new JButton("Cancel·lar");
        ImageIcon iconBack = ctrlPresentacioEnquestes.carregarIcona("goback", 16, 16);
        if (iconBack != null)
            botoEnrere.setIcon(iconBack);

        JButton botoSeleccionar = new JButton("Seleccionar");
        botoSeleccionar.setBackground(COLOR_BLAU);
        botoSeleccionar.setForeground(Color.WHITE);
        botoSeleccionar.setFont(new Font("Arial", Font.BOLD, 12));
        ImageIcon iconCheck = ctrlPresentacioEnquestes.carregarIcona("success", 16, 16);
        if (iconCheck != null)
            botoSeleccionar.setIcon(iconCheck);

        panelBotons.add(botoEnrere);
        panelBotons.add(botoSeleccionar);
        panelTargeta.add(panelBotons, BorderLayout.SOUTH);

        panelCos.add(panelTargeta);
        this.add(panelCos, BorderLayout.CENTER);

        botoEnrere.addActionListener(e -> ctrlPresentacioEnquestes.mostrarVistaEnquestes("gestioenquestes"));

        botoSeleccionar.addActionListener(e -> {
            String seleccionada = llistaEnquestes.getSelectedValue();
            if (seleccionada != null) {
                ctrlPresentacioEnquestes.seleccionarEnquesta(seleccionada);
                ctrlPresentacioEnquestes.mostrarVistaEnquestes("menuenquesta");
            } else {
                JOptionPane.showMessageDialog(this, "Has de seleccionar una enquesta.", "Atenció",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    /**
     * Refresca la llista d'enquestes.
     */
    public void refrescar() {
        listModel.clear();
        ArrayList<String> enquestes = ctrlPresentacioEnquestes.getLlistaEnquestes();
        for (String e : enquestes) {
            listModel.addElement(e);
        }
    }
}