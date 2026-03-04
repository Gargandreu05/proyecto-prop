package edu.upc.prop.cluster422.Presentacio.Vistes.VistesPrincipals;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import edu.upc.prop.cluster422.Presentacio.Controladors.CtrlPresentacio;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Vista que mostra la pantalla de crèdits. Mostra la informació dels autors i
 * permet tornar a la pantalla principal.
 * 
 * @author Andreu Puerto
 * @version 15/12/2025
 */
public class VistaCredits extends JPanel {

    /**
     * Controlador de la presentacio
     */
    private final CtrlPresentacio ctrlPresentacio;

    /**
     * Etiqueta per al titol de la vista
     */
    private JLabel labelCredits;

    /**
     * Area de text per a mostrar la informacio dels autors
     */
    private JTextArea textAreaCredits;

    /**
     * Botó per a tornar enrere
     */
    private JButton botoTornarEnrere;

    /**
     * Botó per a tancar l'aplicacio
     */
    private JButton botoSortir;

    // Colors Corporatius
    private final Color COLOR_BLAU = new Color(0, 106, 176);
    private final Color COLOR_BLANC = Color.WHITE;

    /**
     * Constructor de la vista.
     * 
     * @param ctrlPresentacio Controlador de la presentacio.
     */
    public VistaCredits(CtrlPresentacio ctrlPresentacio) {
        this.ctrlPresentacio = ctrlPresentacio;
        inicialitzarComponent();
    }

    /**
     * Inicialitza els components de la vista.
     */
    private void inicialitzarComponent() {
        this.setLayout(new GridBagLayout());
        this.setBackground(COLOR_BLAU);

        JPanel panelCredits = new JPanel(new GridBagLayout());
        panelCredits.setBackground(COLOR_BLANC);
        panelCredits.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 80, 150), 1),
                new EmptyBorder(40, 60, 40, 60)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        labelCredits = new JLabel("CRÈDITS", SwingConstants.CENTER);
        labelCredits.setFont(new Font("Arial", Font.BOLD, 24));
        labelCredits.setForeground(COLOR_BLAU);

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        panelCredits.add(labelCredits, gbc);

        ImageIcon iconTeam = ctrlPresentacio.carregarIcona("team", 96, 64);
        if (iconTeam != null) {
            JLabel lblIcona = new JLabel(iconTeam);
            lblIcona.setHorizontalAlignment(SwingConstants.CENTER);
            gbc.gridy = 1;
            gbc.insets = new Insets(0, 0, 20, 0);
            panelCredits.add(lblIcona, gbc);
        }

        textAreaCredits = new JTextArea();
        textAreaCredits.setEditable(false);
        textAreaCredits.setLineWrap(true);
        textAreaCredits.setWrapStyleWord(true);
        textAreaCredits.setOpaque(false);
        textAreaCredits.setBorder(null);
        textAreaCredits.setFont(new Font("Arial", Font.PLAIN, 14));
        textAreaCredits.setForeground(Color.DARK_GRAY);

        textAreaCredits.setText(
                "Aquest projecte ha estat realitzat pel grup 42.2 de PROP.\n\n" +
                        "Els integrants d'aquest grup són:\n\n" +
                        "   • Andreu Puerto\n" +
                        "   • Roger Guinovart\n" +
                        "   • Ramon Sanchez\n" +
                        "   • Guillem Revuelta\n" +
                        "   • Enzo Miquel\n");

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        panelCredits.add(textAreaCredits, gbc);

        botoTornarEnrere = crearBotoBlanc("Tornar");
        botoTornarEnrere.setFont(new Font("Arial", Font.BOLD, 14));

        botoSortir = crearBotoBlanc("Tancar Aplicació");

        gbc.gridy = 3;
        gbc.insets = new Insets(5, 0, 10, 0);
        panelCredits.add(botoTornarEnrere, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 0, 0);
        panelCredits.add(botoSortir, gbc);

        this.add(panelCredits);

        botoTornarEnrere.addActionListener(e -> ctrlPresentacio.mostrarVista("benvinguda"));
        botoSortir.addActionListener(e -> System.exit(0));
    }

    /**
     * Funció auxiliar per crear botons estilitzats.
     * 
     * @param text Text del botó.
     * @return Botó estilitzat.
     */
    private JButton crearBotoBlanc(String text) {
        JButton boto = new JButton(text);
        boto.setFont(new Font("Arial", Font.PLAIN, 14));
        boto.setBackground(Color.WHITE);
        boto.setForeground(COLOR_BLAU);
        boto.setFocusPainted(false);
        boto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BLAU, 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        boto.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boto.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                boto.setBackground(new Color(240, 248, 255));
            }

            public void mouseExited(MouseEvent e) {
                boto.setBackground(Color.WHITE);
            }
        });
        return boto;
    }
}