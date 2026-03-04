package edu.upc.prop.cluster422.Presentacio.Vistes.VistesPrincipals;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import edu.upc.prop.cluster422.Presentacio.Controladors.CtrlPresentacio;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Vista del menu principal.
 * Quan s'inicia sessió es mostra aquesta vista i permet gestionar les enquestes
 * o els usuaris.
 * 
 * @author Andreu Puerto
 * 
 * @version 11/12/2025
 */
public class VistaMenu extends JPanel {

    /**
     * Controlador de la presentacio
     */
    private CtrlPresentacio ctrlPresentacio;

    /**
     * Etiqueta per al nom d'usuari
     */
    private JLabel labelNomUsuari;

    private final Color COLOR_BLAU = new Color(0, 106, 176);
    private final Color COLOR_FONS_COS = new Color(240, 240, 240);
    private final Color COLOR_TARGETA = Color.WHITE;

    /**
     * Constructor de la vista del menu principal.
     * 
     * @param ctrlPresentacio Controlador de la presentacio.
     */
    public VistaMenu(CtrlPresentacio ctrlPresentacio) {
        this.ctrlPresentacio = ctrlPresentacio;
        inicialitzarComponent();
    }

    /**
     * Inicialitza els components de la vista.
     */
    private void inicialitzarComponent() {
        this.setLayout(new BorderLayout());

        ImageIcon iconLogo = ctrlPresentacio.carregarIcona("upc", 40, 40);
        ImageIcon iconUser = ctrlPresentacio.carregarIcona("user", 20, 20);
        ImageIcon iconTeam = ctrlPresentacio.carregarIcona("team", 32, 32);
        ImageIcon iconEdit = ctrlPresentacio.carregarIcona("edit", 32, 32);
        ImageIcon iconBack = ctrlPresentacio.carregarIcona("goback", 16, 16);

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_BLAU);
        panelHeader.setPreferredSize(new Dimension(800, 70));
        panelHeader.setBorder(new EmptyBorder(0, 20, 0, 20));

        JPanel panelLogo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 12));
        panelLogo.setOpaque(false);

        JLabel labelImgLogo = new JLabel();
        if (iconLogo != null)
            labelImgLogo.setIcon(iconLogo);

        JLabel labelTextLogo = new JLabel("MENÚ PRINCIPAL");
        labelTextLogo.setFont(new Font("Arial", Font.BOLD, 22));
        labelTextLogo.setForeground(Color.WHITE);

        panelLogo.add(labelImgLogo);
        panelLogo.add(labelTextLogo);

        JPanel panelUsuari = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        panelUsuari.setOpaque(false);

        labelNomUsuari = new JLabel("Usuari", SwingConstants.RIGHT);
        labelNomUsuari.setFont(new Font("Arial", Font.BOLD, 14));
        labelNomUsuari.setForeground(Color.WHITE);
        if (iconUser != null)
            labelNomUsuari.setIcon(iconUser);

        JButton botoLogout = new JButton("Tancar sessió");
        estilitzarBotoHeader(botoLogout);
        if (iconBack != null)
            botoLogout.setIcon(iconBack);

        panelUsuari.add(labelNomUsuari);
        panelUsuari.add(botoLogout);

        panelHeader.add(panelLogo, BorderLayout.WEST);
        panelHeader.add(panelUsuari, BorderLayout.EAST);

        this.add(panelHeader, BorderLayout.NORTH);

        // ==========================================
        // 2. COS CENTRAL (MENU)
        // ==========================================
        JPanel panelCos = new JPanel(new GridBagLayout());
        panelCos.setBackground(COLOR_FONS_COS);

        JPanel panelTargeta = new JPanel(new GridBagLayout());
        panelTargeta.setBackground(COLOR_TARGETA);
        panelTargeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(30, 50, 30, 50)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        // Títol de la secció
        JLabel labelSeccio = new JLabel("Els meus mòduls", SwingConstants.LEFT);
        labelSeccio.setFont(new Font("Arial", Font.PLAIN, 18));
        labelSeccio.setForeground(new Color(80, 80, 80));
        labelSeccio.setBorder(new EmptyBorder(0, 0, 15, 0));

        gbc.gridy = 0;
        panelTargeta.add(labelSeccio, gbc);

        JButton botoEnquestes = crearBotoModul("Gestió d'Enquestes", "Crear, editar i respondre enquestes", iconEdit);
        JButton botoUsuaris = crearBotoModul("Gestió d'Usuaris", "Administrar usuaris del sistema", iconTeam);

        gbc.gridy = 1;
        panelTargeta.add(botoEnquestes, gbc);

        gbc.gridy = 2;
        panelTargeta.add(botoUsuaris, gbc);

        panelCos.add(panelTargeta);
        this.add(panelCos, BorderLayout.CENTER);

        // ==========================================
        // 3. PEU (BARRA INFERIOR)
        // ==========================================
        JPanel panelPeu = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelPeu.setBackground(Color.WHITE);
        JButton botoSortirApp = new JButton("Sortir de l'Aplicació");
        estilitzarBotoSimple(botoSortirApp);

        panelPeu.add(botoSortirApp);
        this.add(panelPeu, BorderLayout.SOUTH);

        // ==========================================
        // LISTENERS
        // ==========================================
        botoLogout.addActionListener(e -> ctrlPresentacio.tancaSessio());
        botoEnquestes.addActionListener(e -> ctrlPresentacio.mostrarVista("enquestes"));
        botoUsuaris.addActionListener(e -> ctrlPresentacio.mostrarVistaUsuari("usuaris"));
        botoSortirApp.addActionListener(e -> {
            ctrlPresentacio.tancaSessio();
            System.exit(0);
        });
    }

    /**
     * Crea un botó estilitzat per la vista del menu.
     * 
     * @param titol      Títol del botó.
     * @param descripcio Descripció del botó.
     * @param icona      Icona del botó.
     * @return Botó estilitzat.
     */
    private JButton crearBotoModul(String titol, String descripcio, ImageIcon icona) {
        JButton boto = new JButton();
        boto.setLayout(new BorderLayout(15, 0)); // 15px separació horitzontal

        // 1. Icona a l'esquerra
        if (icona != null) {
            JLabel labelIcona = new JLabel(icona);
            boto.add(labelIcona, BorderLayout.WEST);
        }

        // 2. Text al centre (Títol a dalt, Descripció a sota)
        JPanel panelText = new JPanel(new GridLayout(2, 1, 0, 2));
        panelText.setOpaque(false); // Transparent per veure el hover

        JLabel labelTitol = new JLabel(titol);
        labelTitol.setFont(new Font("Arial", Font.BOLD, 14));
        labelTitol.setForeground(COLOR_BLAU);

        JLabel labelDesc = new JLabel(descripcio);
        labelDesc.setFont(new Font("Arial", Font.PLAIN, 11));
        labelDesc.setForeground(Color.GRAY);

        panelText.add(labelTitol);
        panelText.add(labelDesc);

        boto.add(panelText, BorderLayout.CENTER);

        // 3. Estils del Botó
        boto.setPreferredSize(new Dimension(400, 75));
        boto.setBackground(Color.WHITE);
        boto.setFocusPainted(false);
        boto.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Vora composta (Linia grisa + Marge intern)
        boto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        // 4. Efecte Hover (Ratolí per sobre)
        boto.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                boto.setBackground(new Color(245, 250, 255)); // Blauet clar
                boto.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_BLAU, 1), // Vora blava
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)));
            }

            public void mouseExited(MouseEvent e) {
                boto.setBackground(Color.WHITE);
                boto.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)));
            }
        });

        return boto;
    }

    /**
     * Estilitza un boto per la capçalera.
     * 
     * @param boto Botó a estilitzar.
     */
    private void estilitzarBotoHeader(JButton boto) {
        boto.setForeground(Color.WHITE);
        boto.setBackground(new Color(0, 80, 150));
        boto.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        boto.setFocusPainted(false);
        boto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boto.setFont(new Font("Arial", Font.PLAIN, 12));
    }

    /**
     * Estilitza un boto simple.
     * 
     * @param boto Botó a estilitzar.
     */
    private void estilitzarBotoSimple(JButton boto) {
        boto.setBackground(Color.WHITE);
        boto.setForeground(Color.DARK_GRAY);
        boto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        boto.setPreferredSize(new Dimension(150, 30));
    }

    /**
     * Actualitza el text de benvinguda del usuari.
     * 
     * @param nom Nom de l'usuari.
     */
    public void setNomUsuari(String nom) {
        if (labelNomUsuari != null) {
            labelNomUsuari.setText("Benvingut/da, " + nom);
        }
    }
}