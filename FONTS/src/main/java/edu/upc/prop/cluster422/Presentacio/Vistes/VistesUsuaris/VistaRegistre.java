package edu.upc.prop.cluster422.Presentacio.Vistes.VistesUsuaris;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import edu.upc.prop.cluster422.Presentacio.Controladors.CtrlPresentacioUsuaris;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Vista per al registre de nous usuaris. Una vegada completats els camps,
 * es crea un compte i es fa login automàticament.
 * 
 * @author Andreu Puerto
 * @version 15/12/2025
 */
public class VistaRegistre extends JPanel {

    /**
     * Controlador de la presentacio d'usuaris
     */
    private final CtrlPresentacioUsuaris ctrlPresentacioUsuaris;

    /**
     * Camp per a la introduccio del nom d'usuari
     */
    private JTextField campUsuari;

    /**
     * Camp per a la introduccio de la contrasenya
     */
    private JPasswordField campContrasenya;

    private final Color COLOR_BLAU = new Color(0, 106, 176);
    private final Color COLOR_BLANC = Color.WHITE;

    /**
     * Constructor de la vista de registre.
     * 
     * @param ctrlPresentacioUsuaris Controlador de presentació d'usuaris.
     */
    public VistaRegistre(CtrlPresentacioUsuaris ctrlPresentacioUsuaris) {
        this.ctrlPresentacioUsuaris = ctrlPresentacioUsuaris;
        inicialitzarComponent();
    }

    /**
     * Inicialitza els components de la vista.
     */
    private void inicialitzarComponent() {
        this.setLayout(new GridBagLayout());
        this.setBackground(COLOR_BLAU);

        JPanel panelCard = new JPanel(new GridBagLayout());
        panelCard.setBackground(COLOR_BLANC);
        panelCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 80, 150), 1),
                new EmptyBorder(40, 50, 40, 50)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel labelTitol = new JLabel("REGISTRE - PROP", SwingConstants.CENTER);
        labelTitol.setFont(new Font("Arial", Font.BOLD, 24));
        labelTitol.setForeground(COLOR_BLAU);

        JLabel labelSubtitol = new JLabel("Crea un compte per accedir", SwingConstants.CENTER);
        labelSubtitol.setFont(new Font("Arial", Font.PLAIN, 14));
        labelSubtitol.setForeground(Color.GRAY);

        gbc.gridy = 0;
        panelCard.add(labelTitol, gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 30, 0);
        panelCard.add(labelSubtitol, gbc);

        gbc.insets = new Insets(5, 0, 5, 0);

        JLabel labelUser = new JLabel("Nom d'usuari");
        labelUser.setFont(new Font("Arial", Font.BOLD, 12));
        labelUser.setForeground(COLOR_BLAU);
        campUsuari = new JTextField(20);
        estilitzarCamp(campUsuari);

        gbc.gridy = 2;
        panelCard.add(labelUser, gbc);
        gbc.gridy = 3;
        panelCard.add(campUsuari, gbc);

        JLabel labelPass = new JLabel("Contrasenya");
        labelPass.setFont(new Font("Arial", Font.BOLD, 12));
        labelPass.setForeground(COLOR_BLAU);
        campContrasenya = new JPasswordField(20);
        estilitzarCamp(campContrasenya);

        gbc.gridy = 4;
        panelCard.add(labelPass, gbc);
        gbc.gridy = 5;
        gbc.insets = new Insets(5, 0, 30, 0);
        panelCard.add(campContrasenya, gbc);

        JButton botoRegistre = crearBotoBlanc("Crear Compte");
        botoRegistre.setFont(new Font("Arial", Font.BOLD, 14));
        JButton botoAnarALogin = crearBotoBlanc("Ja tens compte? Inicia sessió");
        JButton botoTornar = crearBotoBlanc("Cancel·lar");

        gbc.gridy = 6;
        gbc.insets = new Insets(5, 0, 10, 0);
        panelCard.add(botoRegistre, gbc);
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 5, 0);
        panelCard.add(botoAnarALogin, gbc);
        gbc.gridy = 8;
        panelCard.add(botoTornar, gbc);

        this.add(panelCard);

        botoRegistre.addActionListener(e -> accioRegistre());
        botoAnarALogin.addActionListener(e -> {
            netejarCamps();
            ctrlPresentacioUsuaris.mostrarVistaUsuari("login");
        });
        botoTornar.addActionListener(e -> {
            netejarCamps();
            ctrlPresentacioUsuaris.mostrarVistaPrincipal("benvinguda");
        });
        campContrasenya.addActionListener(e -> accioRegistre());
    }

    /**
     * Accio que s'executa quan es fa clic al boto de registre. Això fa registre al
     * usuari.
     */
    private void accioRegistre() {
        String user = campUsuari.getText().trim();
        char[] pass = campContrasenya.getPassword();

        if (user.isEmpty() || pass.length == 0) {
            JOptionPane.showMessageDialog(this, "Omple tots els camps.", "Atenció", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            netejarCamps();
            ctrlPresentacioUsuaris.registre(user, pass);
            ;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Registre", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Neteja els camps de text quan es canvia de vista.
     */
    private void netejarCamps() {
        campUsuari.setText("");
        campContrasenya.setText("");
    }

    /**
     * Funció auxiliar per crear un boto amb el text especificat.
     * 
     * @param text Text del boto.
     * @return Boto creat.
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

    /**
     * Funció auxiliar per estilitzar els camps de text.
     * 
     * @param camp Camp de text a estilitzar.
     */
    private void estilitzarCamp(JTextField camp) {
        camp.setPreferredSize(new Dimension(250, 35));
        camp.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        camp.setFont(new Font("Arial", Font.PLAIN, 14));
    }
}