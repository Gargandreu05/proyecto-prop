package edu.upc.prop.cluster422.Presentacio.Vistes.VistesUsuaris;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import edu.upc.prop.cluster422.Presentacio.Controladors.CtrlPresentacioUsuaris;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Vista del panell d'administrador.
 * Permet gestionar usuaris (ban, veure dades), crear admins i eliminar
 * enquestes globalment.
 * 
 * @author Guillem Revuelta
 * 
 * @version 15/12/2025
 */
public class VistaAdmin extends JPanel {

    /**
     * Controlador de la presentacio d'usuaris
     */
    private CtrlPresentacioUsuaris ctrlPresentacioUsuaris;

    /**
     * Camp de text per seleccionar usuaris
     */
    private JTextField selectorUsuaris;

    /**
     * Botó per a veure les dades privades
     */
    private JButton botoVeurePrivat;

    /**
     * Botó per a banear un usuari
     */
    private JButton botoBanUser;

    /**
     * Botó per a registrar un administrador
     */
    private JButton botoRegistrarAdmin;

    /**
     * Botó per a eliminar una enquesta
     */
    private JButton botoEliminarEnquesta;

    /**
     * Botó per a tornar enrere
     */
    private JButton botoEnrere;

    private final Color COLOR_BLAU = new Color(0, 106, 176);
    private final Color COLOR_FONS_COS = new Color(240, 240, 240);
    private final Color COLOR_TARGETA = Color.WHITE;

    /**
     * Botó per a anar a la vista d'usuari
     */
    private JButton botoVistaUsuari;

    /**
     * Constructor de la vista d'administrador.
     * 
     * @param ctrlPresentacioUsuaris Controlador de presentació d'usuaris.
     */
    public VistaAdmin(CtrlPresentacioUsuaris ctrlPresentacioUsuaris) {
        this.ctrlPresentacioUsuaris = ctrlPresentacioUsuaris;
        inicialitzarComponent();
    }

    /**
     * Inicialitza els components de la vista.
     */
    private void inicialitzarComponent() {
        this.setLayout(new BorderLayout());
        initHeader();
        initCosCentral();
        assignarListeners();
    }

    /**
     * Inicialitza el header de la vista.
     */
    private void initHeader() {
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_BLAU);
        panelHeader.setPreferredSize(new Dimension(800, 70));
        panelHeader.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel labelTitol = new JLabel("PANELL D'ADMINISTRADOR");
        labelTitol.setFont(new Font("Arial", Font.BOLD, 22));
        labelTitol.setForeground(Color.WHITE);

        ImageIcon iconSettings = ctrlPresentacioUsuaris.carregarIcona("setting", 32, 32);
        if (iconSettings != null)
            labelTitol.setIcon(iconSettings);

        botoEnrere = new JButton("Tornar al Menú");
        estilitzarBotoHeader(botoEnrere);

        JPanel panelDreta = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelDreta.setOpaque(false);
        panelDreta.add(botoEnrere);

        botoVistaUsuari = new JButton("Vista Usuari");
        estilitzarBotoHeader(botoVistaUsuari);
        panelDreta.add(botoVistaUsuari);

        panelHeader.add(labelTitol, BorderLayout.WEST);
        panelHeader.add(panelDreta, BorderLayout.EAST);
        this.add(panelHeader, BorderLayout.NORTH);
    }

    /**
     * Inicialitza el cos central de la vista.
     */
    private void initCosCentral() {
        JPanel panelFons = new JPanel(new GridBagLayout());
        panelFons.setBackground(COLOR_FONS_COS);

        JPanel panelTargeta = new JPanel(new GridBagLayout());
        panelTargeta.setBackground(COLOR_TARGETA);
        panelTargeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(30, 40, 30, 40)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        JLabel lblUser = new JLabel("Gestió d'Usuaris (Ban / Info)");
        lblUser.setFont(new Font("Arial", Font.BOLD, 16));
        lblUser.setForeground(COLOR_BLAU);
        gbc.gridy = 0;
        panelTargeta.add(lblUser, gbc);

        JPanel panelInput = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelInput.setOpaque(false);
        panelInput.add(new JLabel("Seleccionar Usuari (Nom Exacte):"));

        selectorUsuaris = new JTextField();
        selectorUsuaris.setPreferredSize(new Dimension(250, 35));
        selectorUsuaris.setFont(new Font("Arial", Font.PLAIN, 14));
        selectorUsuaris.setBackground(Color.WHITE);

        panelInput.add(selectorUsuaris);

        gbc.gridy = 1;
        panelTargeta.add(panelInput, gbc);

        JPanel panelBotonsUser = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBotonsUser.setOpaque(false);

        botoVeurePrivat = crearBotoAccio("Veure Dades Privades", Color.WHITE, COLOR_BLAU);
        botoBanUser = crearBotoAccio("Eliminar Usuari (Ban)", new Color(220, 50, 50), Color.WHITE);

        panelBotonsUser.add(botoVeurePrivat);
        panelBotonsUser.add(botoBanUser);

        gbc.gridy = 2;
        gbc.insets = new Insets(5, 10, 20, 10);
        panelTargeta.add(panelBotonsUser, gbc);

        gbc.gridy = 3;
        panelTargeta.add(new JSeparator(), gbc);

        JLabel lblGlobal = new JLabel("Accions Globals");
        lblGlobal.setFont(new Font("Arial", Font.BOLD, 16));
        lblGlobal.setForeground(COLOR_BLAU);
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 10, 10, 10);
        panelTargeta.add(lblGlobal, gbc);

        JPanel panelBotonsGlobal = new JPanel(new GridLayout(2, 1, 0, 10));
        panelBotonsGlobal.setOpaque(false);

        botoRegistrarAdmin = crearBotoAccio("Registrar Nou Administrador", Color.WHITE, Color.DARK_GRAY);
        botoEliminarEnquesta = crearBotoAccio("Eliminar Enquesta per Títol", Color.WHITE, Color.RED);

        panelBotonsGlobal.add(botoRegistrarAdmin);
        panelBotonsGlobal.add(botoEliminarEnquesta);

        gbc.gridy = 5;
        panelTargeta.add(panelBotonsGlobal, gbc);

        panelFons.add(panelTargeta);
        this.add(panelFons, BorderLayout.CENTER);
    }

    /**
     * Assigna els listeners als botons.
     */
    private void assignarListeners() {
        botoEnrere.addActionListener(e -> ctrlPresentacioUsuaris.mostrarVistaPrincipal("menu"));
        botoVistaUsuari.addActionListener(e -> ctrlPresentacioUsuaris.mostrarVistaUsuari("usuaris_no_redirect"));

        botoVeurePrivat.addActionListener(e -> {
            String usuariSeleccionat = selectorUsuaris.getText().trim();
            if (usuariSeleccionat.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Escriu un nom d'usuari primer.");
                return;
            }
            try {
                String[] d = ctrlPresentacioUsuaris.adminVeureDadesPrivades(usuariSeleccionat);
                if (d != null) {
                    JOptionPane.showMessageDialog(this,
                            "Dades Privades de " + d[0] + ":\n\nPass: " + d[1] + "\nEmail: " + d[2] + "\nEdat: " + d[3],
                            "Informació Privada", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Usuari no trobat o error recuperant dades.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        botoBanUser.addActionListener(e -> {
            String usuariSeleccionat = selectorUsuaris.getText().trim();
            if (usuariSeleccionat.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Escriu un nom d'usuari primer.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Segur que vols eliminar l'usuari " + usuariSeleccionat
                            + "?\nS'esborraran totes les seves dades i enquestes.",
                    "Confirmar Ban", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    ctrlPresentacioUsuaris.adminEsborrarUsuari(usuariSeleccionat);
                    JOptionPane.showMessageDialog(this, "Usuari eliminat correctament.");
                    sincronitzar();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        botoRegistrarAdmin.addActionListener(e -> {
            JPanel p = new JPanel(new GridLayout(2, 2, 5, 5));
            JTextField u = new JTextField();
            JPasswordField pa = new JPasswordField();
            p.add(new JLabel("Nom Admin:"));
            p.add(u);
            p.add(new JLabel("Password:"));
            p.add(pa);

            int res = JOptionPane.showConfirmDialog(this, p, "Nou Admin", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                try {
                    ctrlPresentacioUsuaris.registrarAdmin(u.getText(), new String(pa.getPassword()));
                    JOptionPane.showMessageDialog(this, "Administrador creat correctament.");
                    sincronitzar();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });

        botoEliminarEnquesta.addActionListener(e -> {
            String titol = JOptionPane.showInputDialog(this, "Introdueix el títol exacte de l'enquesta a eliminar:");
            if (titol != null && !titol.trim().isEmpty()) {
                try {
                    ctrlPresentacioUsuaris.adminEliminarEnquesta(titol);
                    JOptionPane.showMessageDialog(this, "Enquesta eliminada.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });
    }

    /**
     * Sincronitza la llista d'usuaris del ComboBox amb les dades del sistema.
     */
    public void sincronitzar() {
        if (selectorUsuaris != null) {
            selectorUsuaris.setText("");
        }
    }

    /**
     * Estiliza un boto per al header.
     * 
     * @param btn Botó a estilitzar.
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
     * Crea un boto per a les accions.
     * 
     * @param text Text del boto.
     * @param bg   Color de fons del boto.
     * @param fg   Color del text del boto.
     * 
     * @return Botó creat.
     */
    private JButton crearBotoAccio(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (bg.equals(Color.WHITE)) {
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(fg.equals(Color.WHITE) ? Color.GRAY : fg, 1),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        } else {
            btn.setBorder(BorderFactory.createEmptyBorder(9, 16, 9, 16));
        }
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (bg.equals(Color.WHITE))
                    btn.setBackground(new Color(245, 245, 245));
                else
                    btn.setBackground(bg.brighter());
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }
}