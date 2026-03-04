package edu.upc.prop.cluster422.Presentacio.Vistes.VistesEnquesta;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import edu.upc.prop.cluster422.Presentacio.Controladors.CtrlPresentacioEnquestes;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Vista per respondre a una enquesta. Mostra una llista de preguntes i permet
 * respondre a elles. No deixa guardar-les fins que no es compleixi tot el
 * formulari.
 * 
 * @author Andreu Puerto
 * @version 15/12/2025
 */
public class VistaEnquestaRespondre extends JPanel {

    /**
     * Controlador de la presentació d'enquestes
     */
    private CtrlPresentacioEnquestes ctrlPresentacioEnquestes;

    /**
     * Etiqueta per a la visualització del titol de l'enquesta
     */
    private JLabel labelTitol;

    /**
     * Panel per a la visualització del contingut de l'enquesta
     */
    private JPanel panelContingut;

    /**
     * Botó per a enviar les respostes
     */
    private JButton botoEnviar;

    /**
     * Botó per a tornar a la vista principal
     */
    private JButton botoTornar;

    /**
     * Titol de l'enquesta
     */
    private String titolEnquesta;

    /**
     * Tipus de les preguntes
     */
    private ArrayList<String> tipusPreguntes;

    /**
     * Components del formulari
     */
    private ArrayList<JComponent> componentsFormulari;

    private final Color COLOR_BLAU = new Color(0, 106, 176);
    private final Color COLOR_FONS_COS = new Color(240, 240, 240);
    private final Color COLOR_TARGETA = Color.WHITE;

    /**
     * Constructor de la vista per respondre a una enquesta.
     * 
     * @param ctrlPresentacioEnquestes
     */
    public VistaEnquestaRespondre(CtrlPresentacioEnquestes ctrlPresentacioEnquestes) {
        this.ctrlPresentacioEnquestes = ctrlPresentacioEnquestes;
        this.componentsFormulari = new ArrayList<>();
        this.tipusPreguntes = new ArrayList<>();
        initComponents();
    }

    /**
     * Inicialitza els components de la vista.
     */
    private void initComponents() {
        this.setLayout(new BorderLayout());

        // HEADER
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_BLAU);
        panelHeader.setPreferredSize(new Dimension(800, 60));
        panelHeader.setBorder(new EmptyBorder(0, 20, 0, 20));

        labelTitol = new JLabel("RESPONDRE ENQUESTA");
        labelTitol.setFont(new Font("Arial", Font.BOLD, 20));
        labelTitol.setForeground(Color.WHITE);

        ImageIcon iconEdit = ctrlPresentacioEnquestes.carregarIcona("edit", 24, 24);
        if (iconEdit != null)
            labelTitol.setIcon(iconEdit);

        panelHeader.add(labelTitol, BorderLayout.WEST);
        this.add(panelHeader, BorderLayout.NORTH);

        // COS
        panelContingut = new JPanel();
        panelContingut.setLayout(new BoxLayout(panelContingut, BoxLayout.Y_AXIS));
        panelContingut.setBackground(COLOR_FONS_COS);
        panelContingut.setBorder(new EmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(panelContingut);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        this.add(scrollPane, BorderLayout.CENTER);

        JPanel panelPeu = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panelPeu.setBackground(Color.WHITE);
        panelPeu.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        botoTornar = new JButton("Cancel·lar");
        botoTornar.setBackground(Color.WHITE);

        botoEnviar = new JButton("Enviar Respostes");
        botoEnviar.setBackground(COLOR_BLAU);
        botoEnviar.setForeground(Color.WHITE);
        botoEnviar.setFont(new Font("Arial", Font.BOLD, 14));

        panelPeu.add(botoTornar);
        panelPeu.add(botoEnviar);
        this.add(panelPeu, BorderLayout.SOUTH);

        botoTornar.addActionListener(e -> ctrlPresentacioEnquestes.mostrarVistaEnquestes("menuenquesta"));
        botoEnviar.addActionListener(e -> enviarResposta());
    }

    /**
     * Configura la vista per respondre a una enquesta.
     * 
     * @param titol     Enquesta a la que es respondrà.
     * @param enunciats Enunciats de la enquesta.
     * @param tipus     Tipus de les preguntes.
     * @param opcions   Opcions de les preguntes.
     */
    public void configEnquesta(String titol, ArrayList<String> enunciats, ArrayList<String> tipus,
            ArrayList<ArrayList<String>> opcions) {

        this.titolEnquesta = titol;
        this.tipusPreguntes = tipus;

        labelTitol.setText("RESPONDRE: " + titol.toUpperCase());

        panelContingut.removeAll();
        componentsFormulari.clear();

        for (int i = 0; i < enunciats.size(); i++) {
            afegirComponentFormulari(i + 1, enunciats.get(i), tipus.get(i), opcions.get(i));
            panelContingut.add(Box.createVerticalStrut(15));
        }

        panelContingut.add(Box.createVerticalGlue());
        panelContingut.revalidate();
        panelContingut.repaint();
    }

    /**
     * Afegeix un component al formulari (una pregunta a respondre).
     * 
     * @param num      Número de la pregunta.
     * @param enunciat Enunciat de la pregunta.
     * @param tipus    Tipus de la pregunta.
     * @param opcions  Opcions de la pregunta.
     */
    private void afegirComponentFormulari(int num, String enunciat, String tipus, ArrayList<String> opcions) {
        JPanel panelPregunta = new JPanel(new BorderLayout(10, 10));
        panelPregunta.setBackground(COLOR_TARGETA);
        panelPregunta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(15, 20, 15, 20)));
        panelPregunta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        panelPregunta.setMinimumSize(new Dimension(300, 100));

        JLabel lblEnunciat = new JLabel(
                "<html><font color='#006AB0'><b>" + num + ".</b></font> " + enunciat + "</html>");
        lblEnunciat.setFont(new Font("Arial", Font.PLAIN, 14));
        panelPregunta.add(lblEnunciat, BorderLayout.NORTH);

        JComponent inputComponent = null;

        switch (tipus) {
            case "LLIURE":
            case "NUMERIC":
                JTextField txt = new JTextField();
                txt.setPreferredSize(new Dimension(200, 30));
                inputComponent = txt;
                break;

            case "UNIC":
                JComboBox<String> combo = new JComboBox<>(new Vector<>(opcions));
                combo.setBackground(Color.WHITE);
                combo.setSelectedIndex(-1);
                inputComponent = combo;
                break;

            case "MULTIPLE":
                JPanel pCheck = new JPanel();
                pCheck.setLayout(new BoxLayout(pCheck, BoxLayout.Y_AXIS));
                pCheck.setOpaque(false);
                for (String op : opcions) {
                    JCheckBox cb = new JCheckBox(op);
                    cb.setOpaque(false);
                    pCheck.add(cb);
                }
                JScrollPane sc = new JScrollPane(pCheck);
                sc.setPreferredSize(new Dimension(100, 100));
                sc.setBorder(null);

                inputComponent = pCheck;
                panelPregunta.add(sc, BorderLayout.CENTER);
                break;
        }

        if (!tipus.equals("MULTIPLE") && inputComponent != null) {
            panelPregunta.add(inputComponent, BorderLayout.CENTER);
        }

        if (inputComponent != null) {
            componentsFormulari.add(inputComponent);
        }

        panelContingut.add(panelPregunta);
    }

    /**
     * Envia les respostes del formulari i les guarda a l'aplicació.
     */
    private void enviarResposta() {
        ArrayList<Object> respostes = new ArrayList<>();

        try {
            for (int i = 0; i < componentsFormulari.size(); i++) {
                JComponent input = componentsFormulari.get(i);
                String tipus = tipusPreguntes.get(i);
                Object valor = null;

                if (input instanceof JTextField) {
                    String text = ((JTextField) input).getText();
                    if (text == null || text.trim().isEmpty())
                        throw new Exception("Falta respondre la pregunta " + (i + 1));
                    if (tipus.equals("NUMERIC")) {
                        try {
                            valor = Integer.parseInt(text.trim());
                        } catch (NumberFormatException e) {
                            throw new Exception("La pregunta " + (i + 1) + " ha de ser numèrica.");
                        }
                    } else {
                        valor = text;
                    }
                } else if (input instanceof JComboBox) {
                    int index = ((JComboBox<?>) input).getSelectedIndex();
                    if (index == -1)
                        throw new Exception("Selecciona una opció a la pregunta " + (i + 1));
                    valor = index;
                } else if (input instanceof JPanel) {
                    JPanel pCheck = (JPanel) input;
                    ArrayList<Integer> indexos = new ArrayList<>();
                    for (int k = 0; k < pCheck.getComponentCount(); k++) {
                        if (pCheck.getComponent(k) instanceof JCheckBox cb && cb.isSelected()) {
                            indexos.add(k);
                        }
                    }
                    valor = indexos;
                }
                respostes.add(valor);
            }

            ctrlPresentacioEnquestes.enviarResposta(titolEnquesta, respostes);

            JOptionPane.showMessageDialog(this, "Resposta guardada correctament!", "Èxit",
                    JOptionPane.INFORMATION_MESSAGE);
            ctrlPresentacioEnquestes.mostrarVistaEnquestes("menuenquesta");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Falten Dades", JOptionPane.WARNING_MESSAGE);
        }
    }
}