package edu.upc.prop.cluster422.Presentacio.Vistes.VistesUsuaris;

import edu.upc.prop.cluster422.Presentacio.Controladors.CtrlPresentacioUsuaris;
import javax.swing.*;
import java.awt.*;

/**
 * Vista Principal d'Usuaris. Actua com a gestor de les vistes d'usuari.
 * 
 * @author Andreu Puerto
 * @version 15/12/2025
 */
public class VistaUsuarisPrincipal extends JPanel {

    /**
     * Controlador de la presentacio d'usuaris
     */
    private final CtrlPresentacioUsuaris ctrlPresentacioUsuaris;

    /**
     * Layout per a la gestio de les vistes
     */
    private CardLayout cardLayout;

    /**
     * Panel on es posaran les vistes.
     */
    private JPanel panelUsuaris;

    /**
     * Vista de gestio d'usuaris
     */
    private VistaUsuaris vistaGestioUsuaris;

    /**
     * Vista de registre d'usuaris
     */
    private VistaRegistre vistaRegistre;

    /**
     * Vista de login d'usuaris
     */
    private VistaLogin vistaLogin;

    /**
     * Vista d'administrador
     */
    private VistaAdmin vistaAdmin;

    /**
     * Vista de detall d'enquesta d'usuari
     */
    private VistaEnquestaUsuari vistaEnquestaUsuari;

    /**
     * Constructor de la vista principal d'usuaris.
     * 
     * @param ctrlPresentacioUsuaris Controlador de presentació d'usuaris.
     */
    public VistaUsuarisPrincipal(CtrlPresentacioUsuaris ctrlPresentacioUsuaris) {
        this.ctrlPresentacioUsuaris = ctrlPresentacioUsuaris;
        inicialitzarComponents();
        inicialitzarVistes();
        mostrarVista("usuaris");
    }

    /**
     * Inicialitza els components de la vista.
     */
    private void inicialitzarComponents() {
        this.setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        panelUsuaris = new JPanel(cardLayout);
        this.add(panelUsuaris, BorderLayout.CENTER);
    }

    /**
     * Inicialitza totes les vistes de la vista principal d'usuaris.
     */
    private void inicialitzarVistes() {
        vistaGestioUsuaris = new VistaUsuaris(ctrlPresentacioUsuaris);
        panelUsuaris.add(vistaGestioUsuaris, "usuaris");
        vistaRegistre = new VistaRegistre(ctrlPresentacioUsuaris);
        panelUsuaris.add(vistaRegistre, "registre");
        vistaLogin = new VistaLogin(ctrlPresentacioUsuaris);
        panelUsuaris.add(vistaLogin, "login");
        vistaAdmin = new VistaAdmin(ctrlPresentacioUsuaris);
        panelUsuaris.add(vistaAdmin, "admin");
        vistaEnquestaUsuari = new VistaEnquestaUsuari(ctrlPresentacioUsuaris);
        panelUsuaris.add(vistaEnquestaUsuari, "veure_resposta");
    }

    /**
     * Canvia la sub-vista visible dins de la secció d'usuaris.
     * * @param nomVista La clau de la vista ("usuaris", "editar", etc.)
     */
    public void mostrarVista(String nomVista) {
        cardLayout.show(panelUsuaris, nomVista);
    }

    /**
     * Obté la vista de detall d'enquesta d'usuari.
     * 
     * @return La instància de VistaEnquestaUsuari.
     */
    public VistaEnquestaUsuari getVistaEnquestaUsuari() {
        return vistaEnquestaUsuari;
    }

    /**
     * Obté la vista d'administrador.
     * 
     * @return La instància de VistaAdmin.
     */
    public VistaAdmin getVistaAdmin() {
        return vistaAdmin;
    }

    /**
     * Obté la vista de gestió d'usuaris.
     * 
     * @return La instància de VistaUsuaris.
     */
    public VistaUsuaris getVistaGestioUsuaris() {
        return vistaGestioUsuaris;
    }
}