package edu.upc.prop.cluster422.Domini.Controladors;

import javax.security.auth.login.CredentialException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Aquesta clase és el controlador que s'encarrega de tots els tramits
 * relacionats amb el domini.
 * 
 * @author Ramon Sánchez
 * @version 14/12/2025/1
 */

public class CtrlDomini {

    /**
     * Instància única del controlador de domini per complir amb el patro singleton.
     */
    private static CtrlDomini instancia;
    /**
     * Instància del controlador d'usuaris.
     */
    private final CtrlUsuaris ctrlUsuaris;
    /**
     * Instància del controlador d'enquestes.
     */
    private final CtrlEnquesta ctrlEnquesta;

    /**
     * Creadora privada de la classe CtrlDomini per complir amb el patro singleton.
     * 
     * @throws IOException Si no es poden crear els directoris
     */
    private CtrlDomini() throws IOException {
        ctrlUsuaris = CtrlUsuaris.getInstance();
        ctrlEnquesta = CtrlEnquesta.getInstance();
    }

    /**
     * Getter de la instància del controlador de domini.
     * 
     * @return La instància única del controlador de domini.
     * @throws IOException Si no es poden crear els directoris
     */
    public static CtrlDomini getInstance() throws IOException {
        if (instancia == null) {
            instancia = new CtrlDomini();
        }
        return instancia;
    }

    /**
     * Registra un usuari nou al sistema.
     * 
     * @param nomusuari   Nom d'usuari del nou usuari.
     * @param contrasenya Contrasenya del nou usuari.
     * @throws CustomException          Si hi ha algun error durant el registre.
     * @throws IllegalArgumentException Si els arguments proporcionats no són
     *                                  vàlids.
     * @throws IOException              Si hi ha algun error de lectura de fitxers
     */
    public void registrarUsuari(String nomusuari, String contrasenya)
            throws CustomException, IllegalArgumentException, IOException {
        ctrlUsuaris.RegistrarUsuari(nomusuari, contrasenya);
    }

    /**
     * Registra un administrador nou al sistema.
     * 
     * @param nomusuari   Nom d'usuari del nou administrador.
     * @param contrasenya Contrasenya del nou administrador.
     * @throws SecurityException        Si l'usuari actual no té permisos per
     *                                  registrar un administrador.
     * @throws CustomException          Si hi ha algun error durant el registre.
     * @throws IllegalArgumentException Si els arguments proporcionats no són
     *                                  vàlids.
     * @throws IOException              Si hi ha algun error de lectura de fitxers
     */
    public void RegistrarAdministrador(String nomusuari, String contrasenya)
            throws SecurityException, CustomException, IllegalArgumentException, IOException {
        ctrlUsuaris.RegistrarAdministrador(nomusuari, contrasenya);
    }

    /**
     * Inicia sessió amb les credencials proporcionades.
     * 
     * @param nomusuari   Nom d'usuari.
     * @param contrasenya Contrasenya.
     * @throws SecurityException      Si ja s'ha iniciat sessio.
     * @throws NoSuchElementException Si l'usuari no existeix.
     * @throws CredentialException    Si la contrasenya és incorrecta.
     * @throws IOException            Si hi ha algun error de lectura de fitxers
     */
    public void iniciarSessio(String nomusuari, String contrasenya)
            throws NoSuchElementException, SecurityException, CredentialException, IOException {
        ctrlUsuaris.IniciarSessio(nomusuari, contrasenya);
    }

    /**
     * Tanca la sessió de l'usuari actual.
     * 
     * @return Missatge indicant l'èxit de l'operació.
     * @throws CustomException Si no s'ha iniciat sessio per tancar.
     *                         IOException Si hi ha algun error de lectura de
     *                         fitxers
     */
    public String TancaSessio() throws CustomException, IOException {
        return ctrlUsuaris.TancaSessio();
    }

    /**
     * Obté les dades públiques d'un usuari.
     * 
     * @param nomusuari Nom d'usuari de l'usuari del qual es volen obtenir les
     *                  dades.
     * @return Array de Strings amb les dades públiques de l'usuari.
     * @throws NoSuchElementException Si l'usuari no existeix.
     * @throws IOException            Si hi ha algun error de lectura de fitxers
     */
    public String[] getDadesUsuaripublic(String nomusuari) throws NoSuchElementException, IOException {
        return ctrlUsuaris.getDadesUsuaripublic(nomusuari);
    }

    /**
     * Obté les dades privades d'un usuari.
     * 
     * @param nomusuari Nom d'usuari de l'usuari del qual es volen obtenir les
     *                  dades.
     * @return Array de Strings amb les dades privades de l'usuari.
     * @throws NoSuchElementException Si l'usuari no existeix.
     * @throws SecurityException      Si l'usuari actual no té permisos per veure
     *                                les dades privades de l'usuari sol·licitat.
     * @throws IOException            Si hi ha algun error de lectura de fitxers
     */
    public String[] getDadesUsuariprivat(String nomusuari)
            throws NoSuchElementException, SecurityException, IOException {
        return ctrlUsuaris.getDadesUsuariprivat(nomusuari);
    }

    /**
     * Obté les dades de l'usuari actual.
     * 
     * @return Array de Strings amb les dades de l'usuari actual.
     * @throws CustomException Si no hi ha cap usuari amb sessió iniciada.
     */
    public String[] getDadesUsuariActual() throws CustomException {
        return ctrlUsuaris.getDadesUsuariActual();
    }

    /**
     * Modifica el nom d'usuari de l'usuari actual.
     *
     * @param nom Nou nom d'usuari.
     * @throws CustomException          Si no hi ha cap sessio iniciada.
     * @throws IOException              Si hi ha algun error de lectura de fitxers
     * @throws IllegalArgumentException Si el nou nom d'usuari no és vàlid o ja
     *                                  existeix.
     */
    public void modificarUsuari_nom(String nom) throws CustomException, IOException, IllegalArgumentException {
        ctrlUsuaris.modificarUsuari_nom(nom);
    }

    /**
     * Modifica la contrasenya de l'usuari actual.
     * 
     * @param antigacontrasenya Contrasenya antiga.
     * @param novacontrasenya   Nova contrasenya.
     * @throws CustomException          Si no hi ha cap sessio iniciada o si la
     *                                  contrasenya actual és incorrecta.
     * @throws IllegalArgumentException Si la nova contrasenya no és vàlida o és
     *                                  igual a l'actual.
     */
    public void modificarUsuari_contrasenya(String antigacontrasenya, String novacontrasenya)
            throws CustomException, IllegalArgumentException, IOException {
        ctrlUsuaris.modificarUsuari_contrasenya(antigacontrasenya, novacontrasenya);
    }

    /**
     * Modifica l'edat de l'usuari actual.
     * 
     * @param novaedat Nova edat.
     * @throws CustomException          Si no hi ha cap sessio iniciada.
     * @throws IllegalArgumentException Si la nova edat no és vàlida.
     */
    public void modificarUsuari_edat(int novaedat) throws CustomException, IllegalArgumentException, IOException {
        ctrlUsuaris.modificarUsuari_edat(novaedat);
    }

    /**
     * Modifica l'email de l'usuari actual.
     * 
     * @param nouemail Nou email.
     * @throws CustomException          Si no hi ha cap sessio iniciada.
     * @throws IllegalArgumentException Si el nou email no és vàlid.
     */
    public void modificarUsuari_email(String nouemail) throws CustomException, IllegalArgumentException, IOException {
        ctrlUsuaris.modificarUsuari_email(nouemail);
    }

    /**
     * Obté les enquestes creades per un usuari.
     * 
     * @param nomusuari Nom d'usuari de l'usuari del qual es volen obtenir les
     *                  enquestes.
     * @return Array de Strings amb els títols de les enquestes creades per
     *         l'usuari.
     * @throws NoSuchElementException Si l'usuari no existeix.
     * @throws IOException            Si hi ha algun error de lectura de fitxers
     */
    public String[] getEnquestesUsuari(String nomusuari) throws NoSuchElementException, IOException, CustomException {
        Integer[] ids = ctrlUsuaris.getEnquestesUsuari(nomusuari);
        String[] titols = new String[ids.length];
        for (int i = 0; i < ids.length; i++) {
            titols[i] = ctrlEnquesta.getEnquesta(ids[i]).getTitol();
        }
        return titols;
    }

    /**
     * Obté les respostes creades per un usuari.
     * 
     * @param nomusuari Nom d'usuari de l'usuari del qual es volen obtenir les
     *                  respostes.
     * @return Array de Strings amb els títols de les respostes creades per
     *         l'usuari.
     * @throws NoSuchElementException Si l'usuari no existeix.
     * @throws SecurityException      Si l'usuari actual no té permisos per veure
     *                                les respostes de l'usuari sol·licitat.
     * @throws IOException            Si hi ha algun error de lectura de fitxers
     */
    public String[] getRespostesUsuari(String nomusuari)
            throws NoSuchElementException, SecurityException, IOException, CustomException {
        Integer[] ids = ctrlUsuaris.getRespostesUsuari(nomusuari);
        String[] titols = new String[ids.length];
        for (int i = 0; i < ids.length; i++) {
            titols[i] = ctrlEnquesta.getEnquesta(ids[i]).getTitol();
        }
        return titols;
    }

    /**
     * Esborra l'usuari actual del sistema.
     * 
     * @return El nom de l'usuari eliminat.
     * @throws CustomException        Si no hi ha cap sessio iniciada.
     * @throws NoSuchElementException Si hi ha algun problema en esborrar les
     *                                enquestes o respostes de l'usuari..
     * @throws IOException            Si hi ha algun error de lectura de fitxers
     */
    public String esborrarUsuariActual() throws CustomException, NoSuchElementException, IOException {
        return ctrlUsuaris.esborrarUsuariActual();
    }

    /**
     * Esborra un usuari del sistema.
     * 
     * @param nomusuari Nom d'usuari de l'usuari a esborrar.
     * @throws SecurityException        Si l'usuari actual no té permisos per
     *                                  esborrar l'usuari sol·licitat.
     * @throws NoSuchElementException   Si hi ha algun problema en esborrar les
     *                                  enquestes o respostes de l'usuari..
     * @throws IllegalArgumentException Si l'usuari que es vol eliminar no existeix.
     * @throws IOException              Si hi ha algun error de lectura de fitxers
     * @throws CustomException          Si hi ha algun problema en esborrar les
     *                                  enquestes o respostes de l'usuari.
     */
    public void esborrarUsuari(String nomusuari)
            throws SecurityException, NoSuchElementException, IllegalArgumentException, IOException, CustomException {
        ctrlUsuaris.esborrarUsuari(nomusuari);
    }

    /**
     * Comprova si hi ha una sessió iniciada.
     * 
     * @return Cert si hi ha una sessió iniciada, fals en cas contrari.
     */
    public boolean sessioIniciada() {
        return ctrlUsuaris.sessioIniciada();
    }

    /**
     * Comprova si un usuari existeix al sistema.
     * 
     * @param nomusuari Nom d'usuari a comprovar.
     * @return Cert si l'usuari existeix, fals en cas contrari.
     */
    public boolean existeixUsuari(String nomusuari) {
        return ctrlUsuaris.existeixUsuari(nomusuari);
    }

    /**
     * Obté el nom d'usuari de l'usuari actual.
     * 
     * @return Nom d'usuari de l'usuari actual.
     * @throws NoSuchElementException Si no hi ha cap usuari amb sessió iniciada.
     */
    public String getNomUsuari() throws NoSuchElementException {
        return ctrlUsuaris.getUsuariActual().getUsername();
    }

    /**
     * Crea una enquesta nova amb el títol proporcionat.
     * 
     * @param titol Títol de l'enquesta a crear.
     * @throws CustomException Si hi ha algun error durant la creació de l'enquesta.
     */
    public void crearEnquesta(String titol) throws CustomException, IOException {
        ctrlEnquesta.crearEnquesta(ctrlUsuaris.getUsuariActual(), titol);
    }

    /**
     * Afegeix una pregunta a una enquesta existent.
     * 
     * @param titolEnquesta Títol de l'enquesta a la qual s'afegirà la pregunta.
     * @param titolPregunta Títol de la pregunta a afegir.
     * @param opcions       Opcions de resposta per a la pregunta (si escau).
     * @param format        Format de la pregunta (UNIC, MULTIPLE, NUMERIC, LLIURE).
     * @throws CustomException Si hi ha algun error durant l'afegiment de la
     *                         pregunta.
     */
    public void afegirPregunta(String titolEnquesta, String titolPregunta, List<String> opcions, String format)
            throws CustomException, IOException {
        ctrlEnquesta.afegirPregunta(titolEnquesta, titolPregunta, opcions, format);
    }

    /**
     * Obté les enquestes creades per l'usuari actual.
     * 
     * @return Array de Strings amb els títols de les enquestes creades per l'usuari
     *         actual.
     * @throws NoSuchElementException Si no hi ha cap usuari amb sessió iniciada.
     * @throws IOException            Si hi ha algun error de lectura de fitxers
     */
    public String[] getEnquestesCreades() throws NoSuchElementException, IOException, CustomException {
        return getEnquestesUsuari(ctrlUsuaris.getUsuariActual().getUsername());
    }

    /**
     * Obté la llista de totes les enquestes del sistema.
     * 
     * @return ArrayList de Strings amb els títols de totes les enquestes.
     */
    public ArrayList<String> getEnquestes() {
        return ctrlEnquesta.getLlistaEnquestes();
    }

    /**
     * Modifica el títol d'una enquesta existent.
     * 
     * @param antigtitol Títol actual de l'enquesta.
     * @param noutitol   Nou títol per a l'enquesta.
     * @throws CustomException Si hi ha algun error durant la modificació del títol.
     */
    public void setTitolEnquesta(String antigtitol, String noutitol) throws CustomException, IOException {
        ctrlEnquesta.setTitolEnquesta(antigtitol, noutitol);
    }

    /**
     * Crea una resposta a una enquesta per part de l'usuari actual.
     * 
     * @param titolEnquesta      Títol de l'enquesta a la qual es respon.
     * @param respostesPreguntes Llista d'objectes representant les respostes a les
     *                           preguntes de l'enquesta.
     * @throws CustomException        Si hi ha algun error durant la creació de la
     *                                resposta.
     * @throws NoSuchElementException Si no hi ha cap usuari amb sessió iniciada.
     */
    public void crearRespostaEnquesta(String titolEnquesta, List<Object> respostesPreguntes)
            throws CustomException, NoSuchElementException, IOException {
        ctrlEnquesta.crearRespostaEnquesta(titolEnquesta, ctrlUsuaris.getUsuariActual(), respostesPreguntes);
    }

    /**
     * Modifica la resposta a una pregunta específica d'una enquesta per part de
     * l'usuari actual.
     * 
     * @param titol       Títol de l'enquesta.
     * @param numpregunta Número de la pregunta a modificar.
     * @param resposta    Nova resposta per a la pregunta.
     * @throws NoSuchElementException    Si no hi ha cap usuari amb sessió iniciada.
     * @throws IndexOutOfBoundsException Si el número de pregunta és invàlid.
     * @throws CustomException           Si hi ha algun error durant la modificació
     *                                   de la resposta.
     */
    public void modificarRespostaPregunta(String titol, int numpregunta, Object resposta)
            throws CustomException, IndexOutOfBoundsException, NoSuchElementException, IOException {
        ctrlEnquesta.modificarRespostaPregunta(titol, ctrlUsuaris.getUsuariActual(), numpregunta, resposta);
    }

    /**
     * Elimina una pregunta d'una enquesta.
     * 
     * @param titolEnquesta Títol de l'enquesta.
     * @param numPregunta   Número de la pregunta a eliminar.
     */
    public void eliminarPregunta(String titolEnquesta, int numPregunta) throws IOException, CustomException {
        ctrlEnquesta.eliminarPregunta(titolEnquesta, numPregunta);
    }

    /**
     * Elimina la resposta a una enquesta per part de l'usuari actual.
     * 
     * @param titolEnquesta Títol de l'enquesta.
     */
    public void eliminarRespostaEnquesta(String titolEnquesta)
            throws NoSuchElementException, IOException, CustomException {
        ctrlEnquesta.eliminarRespostaEnquesta(titolEnquesta, ctrlUsuaris.getUsuariActual());
    }

    /**
     * Elimina una enquesta del sistema.
     * 
     * @param titolEnquesta Títol de l'enquesta a eliminar.
     */
    public void eliminarEnquesta(String titolEnquesta) throws IOException, CustomException {
        ctrlEnquesta.eliminarEnquesta(titolEnquesta);
    }

    /**
     * Obté la llista de preguntes d'una enquesta específica.
     * 
     * @param titolEnquesta Títol de l'enquesta.
     * @return Llista de Strings amb les preguntes de l'enquesta.
     * @throws NoSuchElementException Si l'enquesta no existeix.
     */
    public List<String> getPreguntesEnquesta(String titolEnquesta)
            throws NoSuchElementException, IOException, CustomException {
        return ctrlEnquesta.getPreguntesEnquesta(titolEnquesta);
    }

    /**
     * Importa enquesta al sistema des d'una font externa, s'ha de trobar a la
     * carpeta EXE.
     * 
     * @param fitxer Argument que especifica la font de les dades a importar.
     * @throws CustomException          si no s'ha iniciat sessio.
     * @throws NoSuchFileException      si el fitxer no existeix.
     * @throws IllegalArgumentException Si el format no és correcte.
     */
    public void importarEnquesta(String fitxer) throws CustomException, IllegalArgumentException, IOException {
        if (!sessioIniciada()) {
            throw new CustomException("S'ha de iniciar sessio per importar una enquesta.");
        }
        ctrlEnquesta.importarEnquesta(fitxer, ctrlUsuaris.getUsuariActual());
    }

    /**
     * Importa resposta al sistema des d'una font externa.
     * 
     * @param fitxer Argument que especifica la font de les dades a importar.
     * @throws NoSuchFileException si el fitxer no existeix.
     * @throws CustomException     Si el format no és correcte.
     */
    public void importarResposta(String enq, String fitxer) throws IOException, CustomException {
        if (!sessioIniciada()) {
            throw new CustomException("S'ha de iniciar sessió per importar una enquesta.");
        }
        ctrlEnquesta.importarResposta(enq, ctrlUsuaris.getUsuariActual(), fitxer);
    }

    /**
     * Obté el format d'una pregunta específica dins d'una enquesta.
     * 
     * @param titolEnquesta Títol de l'enquesta.
     * @param numPregunta   Número de la pregunta dins de l'enquesta.
     * @return String representant el format de la pregunta.
     * @throws IndexOutOfBoundsException Si el número de pregunta no és vàlid.
     */
    public String getFormatPregunta(String titolEnquesta, int numPregunta)
            throws IndexOutOfBoundsException, IOException, CustomException {
        return ctrlEnquesta.getFormatPregunta(titolEnquesta, numPregunta);
    }

    /**
     * Aplica un algoritme de clustering a les respostes d'una enquesta específica.
     * 
     * @param titolEnquesta Títol de l'enquesta.
     * @param k             Nombre de clústers a crear.
     * @param clusterMethod Mètode de clustering a utilitzar.
     * @throws CustomException Si hi ha algun error durant l'aplicació de
     *                         l'algoritme.
     */
    public Map<Integer, List<String>> clusterAlgorithm(String titolEnquesta, int k, int clusterMethod)
            throws CustomException, IOException {
        return ctrlEnquesta.clusterAlgorithm(titolEnquesta, k, clusterMethod);
    }

    /**
     * Retorna el coeficient de Silhouette del últim clustering realitzat.
     * 
     * @return Coeficient de Silhouette (entre -1 i +1)
     */
    public double getSilhouetteUltimClustering() {
        return ctrlEnquesta.getUltimSilhouette();
    }

    /**
     * Obté les opcions d'una pregunta específica dins d'una enquesta.
     * 
     * @param titolEnquesta Títol de l'enquesta.
     * @param numPregunta   Número de la pregunta dins de l'enquesta.
     * @return Llista de Strings representant les opcions de la pregunta.
     * @throws IndexOutOfBoundsException Si el número de pregunta no és vàlid.
     */
    public List<String> getOpcionsPregunta(String titolEnquesta, int numPregunta)
            throws IndexOutOfBoundsException, IOException, CustomException {
        return ctrlEnquesta.getOpcionsPregunta(titolEnquesta, numPregunta);
    }

    /**
     * Obté els representants dels clústers de l'últim clustering realitzat.
     * 
     * @return Llista de Strings amb els representants dels clústers.
     * @throws CustomException Si no s'ha realitzat cap clustering prèviament.
     * @throws IOException     Si hi ha algun error de lectura de fitxers
     */
    public List<String> getRepresentantsCluster() throws CustomException, IOException {
        return ctrlEnquesta.getRepresentantsCluster();
    }

    /**
     * Comprova si l'usuari actual ha respost una enquesta específica.
     * 
     * @param titolenquesta Títol de l'enquesta a comprovar.
     * @return Cert si l'usuari ha respost l'enquesta, fals en cas contrari.
     * @throws CustomException      Si no hi ha cap usuari amb sessió iniciada.
     * @throws NullPointerException Si el titol no existeix
     */
    public boolean haRespostEnquesta(String titolenquesta) throws CustomException, NullPointerException {

        return ctrlUsuaris.haRespostEnquesta(ctrlEnquesta.getIdEnquesta(titolenquesta));
    }

}