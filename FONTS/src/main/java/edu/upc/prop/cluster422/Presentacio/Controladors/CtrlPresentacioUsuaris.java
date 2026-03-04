package edu.upc.prop.cluster422.Presentacio.Controladors;

import edu.upc.prop.cluster422.Domini.Controladors.CtrlDomini;
import edu.upc.prop.cluster422.Presentacio.Vistes.VistesUsuaris.VistaUsuarisPrincipal;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador de la vista usuaris, aquesta classe s'encarrega de gestionar les
 * operacions de les vistes d'usuaris.
 * * @author Andreu Puerto i Guillem Revuelta
 * 
 * @version 15/12/2025
 */
public class CtrlPresentacioUsuaris {

    /**
     * Controlador de presentacio
     */
    private CtrlPresentacio ctrlPresentacio;

    /**
     * Controlador de domini
     */
    private CtrlDomini ctrlDomini;

    /**
     * Vista d'usuaris principal
     */
    private VistaUsuarisPrincipal vistaUsuarisPrincipal;

    /**
     * Constructor del controlador de presentació d'usuaris.
     * 
     * @param ctrlPresentacio Controlador principal de presentació.
     */
    public CtrlPresentacioUsuaris(CtrlPresentacio ctrlPresentacio) throws IOException {
        this.ctrlPresentacio = ctrlPresentacio;
        this.ctrlDomini = CtrlDomini.getInstance();
        this.vistaUsuarisPrincipal = new VistaUsuarisPrincipal(this);
    }

    /**
     * Obté la vista principal d'usuaris.
     * 
     * @return La instància de VistaUsuarisPrincipal.
     */
    public VistaUsuarisPrincipal getVistaUsuarisPrincipal() {
        return vistaUsuarisPrincipal;
    }

    /**
     * Mostra una sub-vista específica dins de la secció d'usuaris.
     * Si la vista és "admin", sincronitza la llista d'usuaris.
     * 
     * @param nomVista Clau de la vista a mostrar.
     */
    public void mostrarVistaUsuari(String nomVista) {
        if (nomVista.equals("admin")) {
            if (vistaUsuarisPrincipal.getVistaAdmin() != null) {
                vistaUsuarisPrincipal.getVistaAdmin().sincronitzar();
            }
        } else if (nomVista.equals("usuaris_no_redirect")) {
            nomVista = "usuaris";
        } else if (nomVista.equals("usuaris")) {
            // Comprovar si es admin i redirigir
            try {
                if (ctrlDomini.getDadesUsuariActual().length == 5) {
                    nomVista = "admin";
                    if (vistaUsuarisPrincipal.getVistaAdmin() != null) {
                        vistaUsuarisPrincipal.getVistaAdmin().sincronitzar();
                    }
                } else {
                    if (vistaUsuarisPrincipal.getVistaGestioUsuaris() != null) {
                        vistaUsuarisPrincipal.getVistaGestioUsuaris().sincronitzar();
                    }
                }
            } catch (Exception e) {
                // Ignorar error, mantenir vista original
            }
        }

        // Cas explicit usuaris (usuaris_no_redirect)
        if (nomVista.equals("usuaris")) {
            if (vistaUsuarisPrincipal.getVistaGestioUsuaris() != null) {
                vistaUsuarisPrincipal.getVistaGestioUsuaris().sincronitzar();
            }
        }

        vistaUsuarisPrincipal.mostrarVista(nomVista);
    }

    /**
     * Navega a una vista global de l'aplicació (fora de la secció usuaris).
     * 
     * @param nomVista Clau de la vista a mostrar.
     */
    public void mostrarVistaPrincipal(String nomVista) {
        ctrlPresentacio.mostrarVista(nomVista);
    }

    /**
     * Realitza el procés de login. Si l'usuari és administrador, redirigeix al
     * panell d'admin.
     * 
     * @param usuari   Nom d'usuari.
     * @param password Contrasenya.
     * @throws Exception Si hi ha errors en l'autenticació.
     */
    public void login(String usuari, char[] password) throws Exception {
        ctrlDomini.iniciarSessio(usuari, new String(password));

        ctrlPresentacio.getVistaPrincipal().setNomUsuari(usuari);

        String[] dadesUsuari = ctrlDomini.getDadesUsuariActual();

        if (dadesUsuari.length == 5) {
            ctrlPresentacio.mostrarVistaUsuari("admin");
        } else {
            ctrlPresentacio.mostrarVista("menu");
        }
    }

    /**
     * Registra un nou usuari i inicia sessió automàticament.
     * 
     * @param usuari   Nom d'usuari.
     * @param password Contrasenya.
     * @throws Exception Si hi ha errors durant el registre.
     */
    public void registre(String usuari, char[] password) throws Exception {
        ctrlDomini.registrarUsuari(usuari, new String(password));
        ctrlDomini.iniciarSessio(usuari, new String(password));
        ctrlPresentacio.getVistaPrincipal().setNomUsuari(usuari);
        ctrlPresentacio.mostrarVista("menu");
    }

    /**
     * Tanca la sessió actual i torna a la pantalla de benvinguda.
     */
    public void tancaSessio() {
        try {
            ctrlDomini.TancaSessio();
            ctrlPresentacio.mostrarVista("benvinguda");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Modifica les dades de l'usuari actual.
     * 
     * @param nouNom    Nou nom d'usuari (opcional).
     * @param passVella Contrasenya actual (necessària per canviar pass).
     * @param passNova  Nova contrasenya (opcional).
     * @param edat      Nova edat (negatiu per no canviar).
     * @param email     Nou email (opcional).
     * @throws Exception Si hi ha errors en la modificació.
     */
    public void modificarUsuari(String nouNom, String passVella, String passNova, int edat, String email)
            throws Exception {
        if (nouNom != null && !nouNom.isEmpty())
            ctrlDomini.modificarUsuari_nom(nouNom);
        if (edat >= 0)
            ctrlDomini.modificarUsuari_edat(edat);
        if (email != null && !email.isEmpty())
            ctrlDomini.modificarUsuari_email(email);
        if (passVella != null && !passVella.isEmpty()) {
            ctrlDomini.modificarUsuari_contrasenya(passVella, passNova);
        }
        ctrlPresentacio.getVistaPrincipal().setNomUsuari(nouNom);
    }

    /**
     * Esborra el compte de l'usuari actual.
     */
    public void esborrarUsuariActual() {
        try {
            ctrlDomini.esborrarUsuariActual();
            JOptionPane.showMessageDialog(null, "Compte eliminat correctament.");
            ctrlPresentacio.mostrarVista("benvinguda");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Obre la vista de detall per veure les respostes d'un usuari a una enquesta.
     * 
     * @param titolEnquesta Títol de l'enquesta.
     * @param nomUsuari     Nom de l'usuari.
     */
    public void veureDetallEnquestaUsuari(String titolEnquesta, String nomUsuari) {
        try {
            var vista = vistaUsuarisPrincipal.getVistaEnquestaUsuari();

            List<String[]> dadesSimulades = new ArrayList<>();

            if (titolEnquesta.contains("Satisfacció")) {
                dadesSimulades.add(new String[] { "Com valoraries el servei?", "8" });
                dadesSimulades.add(new String[] { "Recomanaries l'aplicació?", "Sí" });
                dadesSimulades.add(new String[] { "Comentaris addicionals", "Molt bona interfície." });
            } else if (titolEnquesta.contains("Producte")) {
                dadesSimulades.add(new String[] { "Quin producte has comprat?", "Portàtil X200" });
                dadesSimulades.add(new String[] { "Preu just?", "No, massa car" });
            } else {
                dadesSimulades.add(new String[] { "Pregunta 1", "Resposta A" });
                dadesSimulades.add(new String[] { "Pregunta 2", "Resposta B" });
                dadesSimulades.add(new String[] { "Pregunta 3", "Resposta C" });
            }

            vista.carregarDades(titolEnquesta, nomUsuari, dadesSimulades);

            mostrarVistaUsuari("veure_resposta");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error obrint detall: " + e.getMessage());
        }
    }

    /**
     * Obté una llista simulada d'usuaris.
     * 
     * @return Llista de noms d'usuaris.
     */
    public List<String> getLlistaUsuaris() {
        List<String> mock = new ArrayList<>();
        mock.add("UsuariExemple1");
        mock.add("UsuariExemple2");
        return mock;
    }

    /**
     * Carrega una icona a través del controlador principal.
     */
    public ImageIcon carregarIcona(String nom, int w, int h) {
        return ctrlPresentacio.carregarIcona(nom, w, h);
    }

    /**
     * Registra un nou administrador (funció només per admins).
     * 
     * @param nom  Nom del nou admin.
     * @param pass Contrasenya.
     * @throws Exception Si hi ha error en el registre.
     */
    public void registrarAdmin(String nom, String pass) throws Exception {
        ctrlDomini.RegistrarAdministrador(nom, pass);
    }

    /**
     * Elimina un usuari del sistema (funció només per admins).
     * 
     * @param nom Nom de l'usuari a eliminar.
     * @throws Exception Si hi ha error en l'eliminació.
     */
    public void adminEsborrarUsuari(String nom) throws Exception {
        ctrlDomini.esborrarUsuari(nom);
    }

    /**
     * Obté les dades privades d'un usuari (funció només per admins).
     * 
     * @param nom Nom de l'usuari.
     * @return Array amb les dades privades.
     * @throws Exception Si hi ha error o falta de permisos.
     */
    public String[] adminVeureDadesPrivades(String nom) throws Exception {
        return ctrlDomini.getDadesUsuariprivat(nom);
    }

    /**
     * Obté les dades públiques d'un usuari (funció només per admins).
     *
     * @param nom Nom de l'usuari.
     * @return Array amb les dades públiques.
     * @throws Exception Si hi ha error o falta de permisos.
     */
    public String[] veureDadesPubliquesUsuari(String nom) throws Exception {
        return ctrlDomini.getDadesUsuaripublic(nom);
    }

    /**
     * Obté les enquestes creades per un usuari (funció només per admins).
     *
     * @param nom Nom de l'usuari.
     * @return Array amb els títols de les enquestes.
     * @throws Exception Si hi ha error o falta de permisos.
     */
    public String[] getEnquestesCreadesUsuari(String nom) throws Exception {
        return ctrlDomini.getEnquestesUsuari(nom);
    }

    /**
     * Elimina una enquesta pel títol (funció global d'admin).
     * 
     * @param titol Títol de l'enquesta.
     * @throws Exception Si hi ha error.
     */
    public void adminEliminarEnquesta(String titol) throws Exception {
        ctrlDomini.eliminarEnquesta(titol);
    }
}