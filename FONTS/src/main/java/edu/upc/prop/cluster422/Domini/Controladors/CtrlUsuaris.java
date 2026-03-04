package edu.upc.prop.cluster422.Domini.Controladors;

import java.io.IOException;
import java.util.NoSuchElementException;

import edu.upc.prop.cluster422.Dades.CtrlPersistencia;
import edu.upc.prop.cluster422.Domini.Classes.Usuari;
import edu.upc.prop.cluster422.Domini.Classes.Administrador;

import javax.security.auth.login.CredentialException;

/**
 * Aquesta classe és el controlador que s'encarrega de tots els tramits
 * relacionats amb els usuaris.
 * 
 * @author: Ramon Sánchez
 * @version: 14/12/2025/2
 */

public class CtrlUsuaris {

    // Atributs
    /**
     * Instància unica de la classe CtrlUsuaris per complir amb el patró singleton.
     */
    private static CtrlUsuaris instance;
    /**
     * Usuari actual que ha iniciat sessio. Si no n'hi ha cap, es null.
     */
    private Usuari usuariactual;
    /**
     * Dades d'un Usuari temporal que ha buscat l'usuari actual.
     */
    private Usuari usuaribuscat;
    /** Instància del controlador de persistència */
    private final CtrlPersistencia ctrlPersistencia;

    // Constructora
    /**
     * Constructora privada de la classe CtrlUsuaris, on es crea l'usuari
     * administrador per defecte.
     * 
     * @throws IOException Si hi ha algun problema en carregar les dades de l'usuari
     *                     des de la persistència.
     */
    private CtrlUsuaris() throws IOException {
        usuariactual = null;
        usuaribuscat = null;
        ctrlPersistencia = CtrlPersistencia.getInstance();
    }

    // Getters
    /**
     * Getter de l'única instància de la classe CtrlUsuaris per complir amb el patró
     * singleton.
     * 
     * @return Retorna la instància unica de la classe CtrlUsuaris o la crea si no
     *         existeix.
     * @throws IOException Si hi ha algun problema en carregar les dades de l'usuari
     *                     des de la persistència.
     */
    public static CtrlUsuaris getInstance() throws IOException {
        if (instance == null)
            instance = new CtrlUsuaris();
        return instance;
    }

    /**
     * Getter de l'Usuari actual.
     * 
     * @return Retorna l'usuari que té la sessio iniciada.
     * @throws NoSuchElementException Si no hi ha cap usuari amb la sessio iniciada.
     */
    public Usuari getUsuariActual() throws NoSuchElementException {
        if (!sessioIniciada()) {
            throw new NoSuchElementException("No hi ha cap usuari amb la sessio iniciada.");
        }
        return usuariactual;
    }

    /**
     * Getter per obtenir els titols de les enquestes creades per un usuari.
     * 
     * @param nomusuari L'usuari del qual es vol mostrar els titols de les enquestes
     *                  creades.
     * @return Retorna un Array de Integers amb els ids de les enquestes creades per
     *         l'usuari.
     * @throws NoSuchElementException Si l'usuari no existeix.
     * @throws IOException            Si hi ha algun problema en carregar les dades
     *                                de l'usuari des de la persistència.
     */
    public Integer[] getEnquestesUsuari(String nomusuari) throws IOException, NoSuchElementException {
        if (usuariactual.getUsername().equals(nomusuari)) {
            return usuariactual.getEnquestesCreadesTitol();
        } else {
            if (usuaribuscat == null || !usuaribuscat.getUsername().equals(nomusuari)) {
                usuaribuscat = ctrlPersistencia.getPublicUsuari(nomusuari);
            }
            return usuaribuscat.getEnquestesCreadesTitol();
        }
    }

    /**
     * Getter per obtenir el titol de les enquestes respostes per l'usuari.
     * 
     * @param nomusuari L'usuari del qual es vol mostrar els titols de les enquestes
     *                  respostes.
     * @return Retorna un Array de Strings amb els titols de les enquestes respostes
     *         per l'usuari.
     * @throws NoSuchElementException Si l'usuari no existeix.
     * @throws SecurityException      Si no hi ha cap sessio iniciada o si l'usuari
     *                                actual no té permisos per veure les dades
     *                                privades de l'usuari sol·licitat.
     * @throws IOException            Si hi ha algun problema en carregar les dades
     *                                de l'usuari des de la persistència.
     */
    public Integer[] getRespostesUsuari(String nomusuari)
            throws NoSuchElementException, SecurityException, IOException {
        if (usuariactual == null)
            throw new SecurityException("Has d'inicar sessio per veure les dades privades de l'usuari.");
        if (usuariactual.getUsername().equals(nomusuari)) {
            return usuariactual.getRespostesCreadesTitol();
        } else {
            if (!(usuariactual instanceof Administrador)) {
                throw new SecurityException("No es poden mostrar les dades privades d'un altre usuari.");
            } else if (usuaribuscat == null || !usuaribuscat.getUsername().equals(nomusuari)
                    || usuaribuscat.getRespostesCreadesTitol() == null) {
                usuaribuscat = ctrlPersistencia.getPrivateUsuari(nomusuari);
            }
            return usuaribuscat.getRespostesCreadesTitol();
        }
    }

    // Mètodes
    /**
     * Metode per registrar un nou usuari.
     * 
     * @param nomusuari   És el nomusuari del nou Usuari que es vol crear.
     * @param contrasenya És la contrasenya del nou Usuari que es vol crear.
     * @throws IllegalArgumentException Si ja hi ha una sessio iniciada o si algun
     *                                  dels atributs no és vàlid per crear el nou
     *                                  usuari.
     * @throws CustomException          Si el nom d'usuari ja està en ús.
     * @throws IOException              Si hi ha algun problema en desar les dades
     *                                  de l'usuari a la persistència.
     */
    public void RegistrarUsuari(String nomusuari, String contrasenya)
            throws IllegalArgumentException, CustomException, IOException {
        if (sessioIniciada())
            throw new IllegalArgumentException("Ja s'ha iniciat sessio amb un altre usuari.");
        Usuari nouusuari = new Usuari(nomusuari, contrasenya, 0); // id temporalment 0
        ctrlPersistencia.crearUsuari(nouusuari);
    }

    /**
     * Metode per registrar un nou administrador. Aquest metode només pot ser cridat
     * per un altre administrador.
     * 
     * @param nomusuari   És el nomusuari del nou Usuari que es vol crear.
     * @param contrasenya És la contrasenya del nou Usuari que es vol crear.
     * @throws SecurityException        Si no hi ha cap sessio iniciada o si
     *                                  l'usuari actual no es un administrador.
     * @throws IllegalArgumentException Si algun dels atributs no és vàlid per crear
     *                                  el nou usuari.
     * @throws CustomException          Si el nom d'usuari ja està en ús
     * @throws IOException              Si hi ha algun problema en desar les dades
     *                                  de l'usuari a la persistència.
     */
    public void RegistrarAdministrador(String nomusuari, String contrasenya)
            throws SecurityException, IllegalArgumentException, CustomException, IOException {
        if (!sessioIniciada() || !(usuariactual instanceof Administrador)) {
            throw new SecurityException("No tens els permisos necesaris.");
        }
        String acreditador = usuariactual.getUsername();
        Usuari nouusuari = new Administrador(nomusuari, contrasenya, acreditador, 0);
        ctrlPersistencia.crearUsuari(nouusuari);
    }

    /**
     * Metode per iniciar sessio d'un usuari.
     * 
     * @param nomusuari   És el nom d'usuari de l'usuari que vol iniciar sessio.
     * @param contrasenya És la contrasenya de l'usuari que vol iniciar sessio.
     * @throws SecurityException      Si ja hi ha una sessio iniciada.
     * @throws IOException            Si hi ha algun problema en carregar les dades
     *                                de l'usuari des de la persistència.
     * @throws CredentialException    Si les credencials de l'usuari son
     *                                incorrectes.
     * @throws NoSuchElementException Si l'usuari no existeix.
     */
    public void IniciarSessio(String nomusuari, String contrasenya)
            throws SecurityException, IOException, CredentialException, NoSuchElementException {
        if (sessioIniciada())
            throw new SecurityException("Ja s'ha iniciat sessio amb un altre usuari.");
        usuariactual = ctrlPersistencia.logIn(nomusuari, contrasenya);
    }

    /**
     * Metode per tancar la sessio actual d'un usuari.
     * 
     * @return Retorna el nom d'usuari de l'usuari que ha tancat la sessio.
     * @throws CustomException Si no hi ha cap sessio iniciada.
     * @throws IOException     Si hi ha algun problema en desar les dades de
     *                         l'usuari a la persistència.
     */
    public String TancaSessio() throws CustomException, IOException {
        if (!sessioIniciada())
            throw new CustomException("No s'ha iniciat ninguna sessio.");
        String nomusuari = usuariactual.getUsername();
        ctrlPersistencia.updateUsuari(usuariactual);
        usuariactual = null;
        usuaribuscat = null;
        return nomusuari;
    }

    /**
     * Metode per obtenir les dades publiques dun usuari
     * 
     * @param nomusuari És el nom d'usuari de l'usuari del qual es volen mostrar les
     *                  dades.
     * @return Retorna un Array de Strings amb les dades públiques de l'usuari.
     * @throws NoSuchElementException Si l'usuari no existeix.
     */
    public String[] getDadesUsuaripublic(String nomusuari) throws NoSuchElementException, IOException {
        if (usuariactual != null && usuariactual.getUsername().equals(nomusuari)) {
            return usuariactual.getDadesPubliques();
        } else {
            if (usuaribuscat == null || !usuaribuscat.getUsername().equals(nomusuari)) {
                usuaribuscat = ctrlPersistencia.getPublicUsuari(nomusuari);
            }
            return usuaribuscat.getDadesPubliques();
        }
    }

    /**
     * Metode per obtenir les dades privades d'un usuari.
     * 
     * @param nomusuari És el nom d'usuari de l'usuari del qual es volen mostrar les
     *                  dades privades.
     * @return Retorna un Array de Strings amb les dades privades de l'usuari.
     * @throws NoSuchElementException Si l'usuari no existeix.
     * @throws IOException            Si hi ha un problema en carregar les dades de
     *                                l'usuari des de la persistència.
     * @throws SecurityException      Si no hi ha cap sessio iniciada o si l'usuari
     *                                actual no té permisos per veure les dades
     *                                privades de l'usuari sol·licitat.
     */
    public String[] getDadesUsuariprivat(String nomusuari)
            throws NoSuchElementException, SecurityException, IOException {
        if (usuariactual == null)
            throw new SecurityException("Has d'inicar sessio per veure les dades privades de l'usuari.");
        if (usuariactual.getUsername().equals(nomusuari)) {
            return usuariactual.getDadesPrivades();
        } else if (!(usuariactual instanceof Administrador)) {
            throw new SecurityException("No es poden mostrar les dades privades d'un altre usuari.");
        } else {
            if (usuaribuscat == null || !usuaribuscat.getUsername().equals(nomusuari)) {
                usuaribuscat = ctrlPersistencia.getPrivateUsuari(nomusuari);
            }
            return usuaribuscat.getDadesPrivades();
        }
    }

    /**
     * Metode per obtenir les dades privades de l'usuari actual.
     * 
     * @return Retorna un Array de Strings amb les dades privades de l'usuari
     *         actual.
     * @throws CustomException Si no hi ha cap usuari amb la sessio iniciada.
     */
    public String[] getDadesUsuariActual() throws CustomException {
        if (!sessioIniciada()) {
            throw new CustomException("No hi ha cap usuari amb la sessio iniciada.");
        }
        return usuariactual.getDadesPrivades();
    }

    /**
     * Metode per modificra la contrasenya de l'usuari.
     * 
     * @param antigacontrasenya La contrasenya actual de l'usuari.
     * @param novacontrasenya   La nova contrasenya de l'usuari.
     * @throws CustomException          Si no hi ha cap sessio iniciada o si la
     *                                  contrasenya actual és incorrecta.
     * @throws IllegalArgumentException Si la nova contrasenya no és vàlida o és
     *                                  igual a l'actual.
     */
    public void modificarUsuari_contrasenya(String antigacontrasenya, String novacontrasenya)
            throws CustomException, IllegalArgumentException, IOException {
        if (!sessioIniciada())
            throw new CustomException("No s'ha iniciat ninguna sessio per poder modificarla.");
        if (!usuariactual.getContrasenya().equals(antigacontrasenya))
            throw new CustomException("Contrasenya actual incorrecte.");
        if (usuariactual.getContrasenya().equals(novacontrasenya))
            throw new IllegalArgumentException("La nova contrasenya no pot ser igual a l'actual.");
        usuariactual.changeContrasenya(novacontrasenya);
        ctrlPersistencia.updateUsuari(usuariactual);
    }

    /**
     * Metode per modificar l'edat de l'usuari.
     * 
     * @param novaedat És la nova edat de l'usuari.
     * @throws CustomException          Si no hi ha cap sessio iniciada.
     * @throws IllegalArgumentException Si la nova edat no és vàlida.
     */
    public void modificarUsuari_edat(int novaedat) throws CustomException, IllegalArgumentException, IOException {
        if (!sessioIniciada())
            throw new CustomException("No s'ha iniciat ninguna sessio per poder modificarla.");
        usuariactual.setEdat(novaedat);
        ctrlPersistencia.updateUsuari(usuariactual);
    }

    /**
     * Metode per modificar el nom de l'usuari
     * 
     * @param nom Es el nou nom d'usuari.
     * @throws CustomException          Si no s'ha iniciat sessió per poder
     *                                  modificar el nom d'usuari.
     * @throws IllegalArgumentException Si el nou nom d'usuari no es valid.
     * @throws IOException              Si hi ha algun problema en desar les dades
     *                                  de l'usuari a la persistència.
     */
    public void modificarUsuari_nom(String nom) throws CustomException, IOException, IllegalArgumentException {
        if (!sessioIniciada())
            throw new CustomException("No s'ha iniciat ninguna sessio per poder modificarla");
        usuariactual.changeNom(nom);
        try {
            ctrlPersistencia.updateUsuari(usuariactual);
        } catch (CustomException e) {
            throw new IllegalArgumentException("El nom ja esta en us.");
        }
    }

    /**
     * Metode per modificra l'email de l'usuari.
     * 
     * @param nouemail És el nou mail de l'usuari.
     * @throws CustomException          Si no hi ha cap sessio iniciada.
     * @throws IllegalArgumentException Si el nou email no és vàlid.
     */
    public void modificarUsuari_email(String nouemail) throws IllegalArgumentException, CustomException, IOException {
        if (!sessioIniciada())
            throw new CustomException("No s'ha iniciat ninguna sessio per poder modificarla.");
        usuariactual.setEmail(nouemail);
        ctrlPersistencia.updateUsuari(usuariactual);
    }

    /**
     * Metode per eliminar l'usuari actual.
     * 
     * @return Retorna el nom d'usuari de l'usuari que ha estat eliminat.
     * @throws CustomException        Si no hi ha cap sessio iniciada.
     * @throws NoSuchElementException Si hi ha algun problema en esborrar les
     *                                enquestes o respostes de l'usuari.
     * @throws IOException            Si hi ha algun problema en esborrar les dades
     *                                de l'usuari a la persistència.
     */
    public String esborrarUsuariActual() throws NoSuchElementException, CustomException, IOException {
        if (!sessioIniciada())
            throw new CustomException("No s'ha iniciat ninguna sessio per poder esborrar-la.");
        String nomusuari = usuariactual.getUsername();
        ctrlPersistencia.deleteUsuari(usuariactual.getUsername());
        CtrlEnquesta.getInstance().eliminarTotesEnquestesUsuari(usuariactual.getEnquestesCreadesTitol());
        usuariactual = null;
        return nomusuari;
    }

    /**
     * Metode per administradors per eliminar un usuari.
     * 
     * @param nomusuari L'usuari que es vol eliminar.
     * @throws NoSuchElementException   Si hi ha algun problema en esborrar les
     *                                  enquestes o respostes de l'usuari.
     * @throws SecurityException        Si no hi ha cap sessio iniciada o si
     *                                  l'usuari actual no es un administrador.
     * @throws IllegalArgumentException Si l'usuari que es vol eliminar no existeix.
     * @throws IOException              Si hi ha algun problema en esborrar les
     *                                  dades de l'usuari a la persistència.
     * @throws CustomException          Si hi ha algun problema en esborrar les
     *                                  enquestes o respostes de l'usuari.
     */
    public void esborrarUsuari(String nomusuari)
            throws NoSuchElementException, SecurityException, IllegalArgumentException, IOException, CustomException {
        if (!sessioIniciada() || !(usuariactual instanceof Administrador)) {
            throw new SecurityException("No tens els permisos per fer aixo.");
        }
        if (usuaribuscat == null || !usuaribuscat.getUsername().equals(nomusuari)) {
            usuaribuscat = ctrlPersistencia.getPrivateUsuari(nomusuari);
        }
        CtrlEnquesta.getInstance().eliminarTotesEnquestesUsuari(usuaribuscat.getEnquestesCreadesTitol());
        ctrlPersistencia.deleteUsuari(nomusuari);
        usuaribuscat = null;
        if (usuariactual.getUsername().equals(nomusuari)) {
            usuariactual = null;
        }
    }

    /**
     * Metode per comprovar si s'ha iniciat sessio.
     * 
     * @return Retorna true si hi ha un usuari actual, false en cas contrari
     */
    public boolean sessioIniciada() {
        return usuariactual != null;
    }

    /**
     * Metode per comprovar si un usuari existeix.
     * 
     * @param nomusuari És el nom d'usuari que es vol comprovar si existeix.
     * @return Retorna true si l'usuari existeix, falsi en cas contrari
     */
    public boolean existeixUsuari(String nomusuari) {
        try {
            ctrlPersistencia.getPublicUsuari(nomusuari);
            return true;
        } catch (IOException | NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Comprova si l'usuari actual ha respost una enquesta específica.
     * 
     * @param idenquesta id de l'enquesta a comprovar.
     * @return Cert si l'usuari ha respost l'enquesta, fals en cas contrari.
     * @throws CustomException Si no hi ha cap usuari amb sessió iniciada.
     */
    public boolean haRespostEnquesta(int idenquesta) throws CustomException {
        if (!sessioIniciada()) {
            throw new CustomException("No hi ha cap usuari amb la sessió iniciada.");
        }
        return usuariactual.haRespostEnquesta(idenquesta);
    }

}