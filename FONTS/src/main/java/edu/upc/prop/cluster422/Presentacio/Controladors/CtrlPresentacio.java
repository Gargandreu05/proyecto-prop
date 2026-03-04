package edu.upc.prop.cluster422.Presentacio.Controladors;

import edu.upc.prop.cluster422.Domini.Controladors.CtrlDomini;
import edu.upc.prop.cluster422.Presentacio.Vistes.VistesPrincipals.VistaPrincipal;
import edu.upc.prop.cluster422.Presentacio.Vistes.VistesUsuaris.VistaAdmin;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import javax.swing.*;

/**
 * Controlador de presentacio. Actua com a intermediari entre el domini i les
 * vistes, delegant la feina als sub-controladors.
 * * @author Andreu Puerto
 * 
 * @version 15/12/2025
 */
public class CtrlPresentacio {
    /**
     * Singleton de Presentacio
     */
    private static CtrlPresentacio instance = null;

    /**
     * Controlador de domini
     */
    private CtrlDomini ctrlDomini;

    /**
     * Controlador de presentacio d'enquestes
     */
    private CtrlPresentacioEnquestes ctrlEnquestes;

    /**
     * Controlador de presentacio d'usuaris
     */
    private CtrlPresentacioUsuaris ctrlUsuaris;

    /**
     * Vista principal
     */
    private VistaPrincipal vistaPrincipal;

    /**
     * Vista d'administrador
     */
    private VistaAdmin vistaAdmin;

    /**
     * Constructor privat per a implementar el Singleton.
     */
    private CtrlPresentacio() throws IOException {
        // Inicialitzem Singleton de Domini
        ctrlDomini = CtrlDomini.getInstance();

        // Inicialitzem sub-controladors
        // Passem 'this' perquè ells puguin cridar a mostrarVista()
        this.ctrlEnquestes = new CtrlPresentacioEnquestes(this);
        this.ctrlUsuaris = new CtrlPresentacioUsuaris(this);

        // Inicialitzem vistes principals
        this.vistaAdmin = new VistaAdmin(ctrlUsuaris);
        this.vistaPrincipal = new VistaPrincipal(this);
    }

    /**
     * Retorna l'instancia del controlador de presentacio.
     * * @return L'instancia del controlador de presentacio.
     */
    public static CtrlPresentacio getInstance() throws IOException {
        if (instance == null) {
            instance = new CtrlPresentacio();
        }
        return instance;
    }

    /**
     * Inicia la presentacio mostrant la vista principal.
     */
    public void iniciarPresentacio() {
        vistaPrincipal.setVisible(true);
    }

    // --- GETTERS CONTROLADORS I VISTES ---

    /**
     * Retorna el controlador de presentacio d'enquestes.
     * * @return El controlador de presentacio d'enquestes.
     */
    public CtrlPresentacioEnquestes getCtrlEnquestes() {
        return ctrlEnquestes;
    }

    /**
     * Retorna el controlador de presentacio d'usuaris.
     * * @return El controlador de presentacio d'usuaris.
     */
    public CtrlPresentacioUsuaris getCtrlUsuaris() {
        return ctrlUsuaris;
    }

    /**
     * Retorna la vista principal.
     * * @return La vista principal.
     */
    public VistaPrincipal getVistaPrincipal() {
        return vistaPrincipal;
    }

    /**
     * Retorna la vista d'administrador.
     * * @return La vista d'administrador.
     */
    public VistaAdmin getVistaAdmin() {
        return vistaAdmin;
    }

    // --- NAVEGACIÓ (Centralitzada) ---

    /**
     * Mostra una vista principal.
     * * @param nomVista El nom de la vista a mostrar.
     */
    public void mostrarVista(String nomVista) {
        vistaPrincipal.mostrarVista(nomVista);
    }

    /**
     * Mostra una vista d'enquestes.
     * * @param nomVista El nom de la vista a mostrar.
     */
    public void mostrarVistaEnquestes(String nomVista) {
        mostrarVista("enquestes");
        ctrlEnquestes.mostrarVistaEnquestes(nomVista);
    }

    /**
     * Mostra una vista d'usuaris.
     * * @param nomVista El nom de la vista a mostrar.
     */
    public void mostrarVistaUsuari(String nomVista) {
        mostrarVista("usuaris");
        ctrlUsuaris.mostrarVistaUsuari(nomVista);
    }

    // --- UTILITATS (Delegacions a sub-controladors o compartides) ---

    /**
     * Tanca la sessió actual.
     */
    public void tancaSessio() {
        ctrlUsuaris.tancaSessio();
    }

    // --- GESTIÓ DE RECURSOS (Icones) ---
    /**
     * Carrega una icona a partir d'un nom i la redimensiona a les dimensions
     * especificades.
     * * @param nom El nom de la icona (sense extensió).
     * 
     * @param w La amplada de la icona.
     * @param h La alçada de la icona.
     * @return La icona redimensionada.
     */
    public ImageIcon carregarIcona(String nom, int w, int h) {
        URL url = getClass().getResource("/images/" + nom + ".png");

        if (url != null) {
            ImageIcon icon = new ImageIcon(url);
            Image img = icon.getImage();
            Image newImg = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(newImg);
        } else {
            // Opcional: Mostrar error només en mode debug per no molestar
            System.err.println("No s'ha trobat la icona: " + nom);
            return null;
        }
    }

}