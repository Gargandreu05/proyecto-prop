package edu.upc.prop.cluster422.Presentacio.Vistes.VistesUsuaris;

import edu.upc.prop.cluster422.Presentacio.Controladors.CtrlPresentacioUsuaris;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Vista per a l'inici de sessió.
 * 
 * @author Andreu Puerto
 * @version 15/12/2025
 */
public class VistaLogin extends JPanel {

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
    private JPasswordField campPassword;

    private final Color COLOR_BLAU = new Color(0, 106, 176);
    private final Color COLOR_BLANC = Color.WHITE;

    /**
     * Constructor de la vista de login.
     * 
     * @param ctrlPresentacioUsuaris Controlador de presentació d'usuaris.
     */
    public VistaLogin(CtrlPresentacioUsuaris ctrlPresentacioUsuaris) {
        this.ctrlPresentacioUsuaris = ctrlPresentacioUsuaris;
        initComponents();
    }

    /**
     * Inicialitza els components de la vista.
     */
    private void initComponents() {
        this.setLayout(new GridBagLayout());
        this.setBackground(COLOR_BLAU);

        JPanel panelCard = new JPanel(new GridBagLayout());
        panelCard.setBackground(COLOR_BLANC);
        panelCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 80, 150), 1),
                new EmptyBorder(40, 50, 40, 50)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;

        JLabel labelLogo = new JLabel("LOGIN - PROP", SwingConstants.CENTER);
        labelLogo.setFont(new Font("Arial", Font.BOLD, 24));
        labelLogo.setForeground(COLOR_BLAU);

        JLabel labelSubtitol = new JLabel("Servei d'Autenticació", SwingConstants.CENTER);
        labelSubtitol.setFont(new Font("Arial", Font.PLAIN, 14));
        labelSubtitol.setForeground(Color.GRAY);

        gbc.gridy = 0;
        panelCard.add(labelLogo, gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 30, 0);
        panelCard.add(labelSubtitol, gbc);

        gbc.insets = new Insets(5, 0, 5, 0);

        JLabel lblUser = new JLabel("Nom d'usuari");
        lblUser.setFont(new Font("Arial", Font.BOLD, 12));
        lblUser.setForeground(COLOR_BLAU);
        campUsuari = new JTextField(20);
        estilitzarCamp(campUsuari);

        gbc.gridy = 2;
        panelCard.add(lblUser, gbc);
        gbc.gridy = 3;
        panelCard.add(campUsuari, gbc);

        JLabel lblPass = new JLabel("Contrasenya");
        lblPass.setFont(new Font("Arial", Font.BOLD, 12));
        lblPass.setForeground(COLOR_BLAU);
        campPassword = new JPasswordField(20);
        estilitzarCamp(campPassword);

        gbc.gridy = 4;
        panelCard.add(lblPass, gbc);
        gbc.gridy = 5;
        gbc.insets = new Insets(5, 0, 30, 0);
        panelCard.add(campPassword, gbc);

        JButton botoLogin = crearBotoBlanc("Iniciar Sessió");
        botoLogin.setFont(new Font("Arial", Font.BOLD, 14));
        JButton botoAnarARegistre = crearBotoBlanc("No tens compte? Registra't");
        JButton botoEnrere = crearBotoBlanc("Tornar");

        gbc.gridy = 6;
        gbc.insets = new Insets(5, 0, 5, 0);
        panelCard.add(botoLogin, gbc);
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 5, 0);
        panelCard.add(botoAnarARegistre, gbc);
        gbc.gridy = 8;
        panelCard.add(botoEnrere, gbc);

        this.add(panelCard);

        botoLogin.addActionListener(e -> accioLogin());

        botoEnrere.addActionListener(e -> {
            netejarCamps();
            ctrlPresentacioUsuaris.mostrarVistaPrincipal("benvinguda");
        });
        botoAnarARegistre.addActionListener(e -> {
            netejarCamps();
            ctrlPresentacioUsuaris.mostrarVistaUsuari("registre");
        });
        campPassword.addActionListener(e -> accioLogin());
    }

    /**
     * Accio que s'executa quan es fa clic al boto de login. Això fa login al
     * usuari.
     */
    private void accioLogin() {
        String user = campUsuari.getText().trim();
        String pass = new String(campPassword.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Omple tots els camps", "Atenció", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            netejarCamps();
            ctrlPresentacioUsuaris.login(user, pass.toCharArray());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error d'accés: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Neteja els camps de text.
     */
    private void netejarCamps() {
        campUsuari.setText("");
        campPassword.setText("");
    }

    /**
     * Funció auxiliar per crear botons estilitzats.
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