package edu.upc.prop.cluster422.Presentacio.Vistes.VistesUsuaris;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import edu.upc.prop.cluster422.Presentacio.Controladors.CtrlPresentacioUsuaris;
import java.awt.*;
import java.util.List;

/**
 * Vista de detall d'una enquesta resposta per un usuari.
 * Mostra una taula amb les preguntes i les respostes donades.
 * 
 * @author Guillem Revuelta
 * @version 15/12/2025
 */
public class VistaEnquestaUsuari extends JPanel {

    /**
     * Controlador de la presentacio d'usuaris
     */
    private CtrlPresentacioUsuaris ctrlPresentacioUsuaris;

    /**
     * Etiqueta per al titol de la vista
     */
    private JLabel labelTitol;

    /**
     * Etiqueta per al subtitol de la vista
     */
    private JLabel labelSubtitol;

    /**
     * Taula per a mostrar les respostes
     */
    private JTable taulaRespostes;

    /**
     * Model de la taula
     */
    private DefaultTableModel modelTaula;

    private final Color COLOR_BLAU = new Color(0, 106, 176);
    private final Color COLOR_FONS_COS = new Color(240, 240, 240);
    private final Color COLOR_TARGETA = Color.WHITE;

    /**
     * Constructor de la vista de detall d'enquesta d'usuari.
     * 
     * @param ctrlPresentacioUsuaris Controlador de presentació d'usuaris.
     */
    public VistaEnquestaUsuari(CtrlPresentacioUsuaris ctrlPresentacioUsuaris) {
        this.ctrlPresentacioUsuaris = ctrlPresentacioUsuaris;
        inicialitzarComponent();
    }

    /**
     * Inicialitza els components de la vista.
     */
    private void inicialitzarComponent() {
        this.setLayout(new BorderLayout());

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_BLAU);
        panelHeader.setPreferredSize(new Dimension(800, 70));
        panelHeader.setBorder(new EmptyBorder(0, 20, 0, 20));

        labelTitol = new JLabel("DETALL DE RESPOSTA");
        labelTitol.setFont(new Font("Arial", Font.BOLD, 22));
        labelTitol.setForeground(Color.WHITE);

        ImageIcon iconInfo = ctrlPresentacioUsuaris.carregarIcona("info", 32, 32);
        if (iconInfo != null)
            labelTitol.setIcon(iconInfo);

        JButton botoTornar = new JButton("Tornar");
        estilitzarBotoHeader(botoTornar);
        ImageIcon iconBack = ctrlPresentacioUsuaris.carregarIcona("goback", 16, 16);
        if (iconBack != null)
            botoTornar.setIcon(iconBack);

        botoTornar.addActionListener(e -> ctrlPresentacioUsuaris.mostrarVistaUsuari("usuaris"));

        JPanel panelDreta = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelDreta.setOpaque(false);
        panelDreta.add(botoTornar);

        panelHeader.add(labelTitol, BorderLayout.WEST);
        panelHeader.add(panelDreta, BorderLayout.EAST);
        this.add(panelHeader, BorderLayout.NORTH);

        JPanel panelFons = new JPanel(new GridBagLayout());
        panelFons.setBackground(COLOR_FONS_COS);

        JPanel panelTargeta = new JPanel(new BorderLayout(0, 20));
        panelTargeta.setBackground(COLOR_TARGETA);
        panelTargeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(30, 40, 30, 40)));

        labelSubtitol = new JLabel("Carregant dades...", SwingConstants.LEFT);
        labelSubtitol.setFont(new Font("Arial", Font.PLAIN, 16));
        labelSubtitol.setForeground(Color.DARK_GRAY);
        panelTargeta.add(labelSubtitol, BorderLayout.NORTH);

        String[] columnNames = { "Pregunta", "Resposta Donada" };
        modelTaula = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        taulaRespostes = new JTable(modelTaula);
        estilitzarTaula(taulaRespostes);

        JScrollPane scrollPane = new JScrollPane(taulaRespostes);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        scrollPane.getViewport().setBackground(Color.WHITE);

        panelTargeta.add(scrollPane, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 20, 20, 20);
        panelFons.add(panelTargeta, gbc);

        this.add(panelFons, BorderLayout.CENTER);
    }

    /**
     * Carrega les dades de l'enquesta i respostes a la taula visual.
     * 
     * @param titolEnquesta  Títol de l'enquesta.
     * @param nomUsuari      Nom de l'usuari que ha respost.
     * @param dadesRespostes Llista d'arrays de Strings on cada array és {Pregunta,
     *                       Resposta}.
     */
    public void carregarDades(String titolEnquesta, String nomUsuari, List<String[]> dadesRespostes) {
        labelTitol.setText("ENQUESTA: " + titolEnquesta.toUpperCase());
        labelSubtitol.setText("<html>Respostes de l'usuari: <b>" + nomUsuari + "</b></html>");

        modelTaula.setRowCount(0);
        for (String[] fila : dadesRespostes) {
            modelTaula.addRow(fila);
        }
    }

    /**
     * Estiliza un boto per a la capçalera de la vista.
     * 
     * @param btn Boto a estilitzar.
     */
    private void estilitzarBotoHeader(JButton btn) {
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(0, 80, 150));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Arial", Font.PLAIN, 12));
    }

    /**
     * Estiliza una taula per a la capçalera de la vista.
     * 
     * @param table Taula a estilitzar.
     */
    private void estilitzarTaula(JTable table) {
        table.setRowHeight(35);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        table.getColumnModel().getColumn(0).setPreferredWidth(400);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(245, 245, 245));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Arial", Font.BOLD, 12));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
    }
}