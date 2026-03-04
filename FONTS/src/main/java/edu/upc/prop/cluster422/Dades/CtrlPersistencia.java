package edu.upc.prop.cluster422.Dades;

import edu.upc.prop.cluster422.Domini.Classes.Usuari;
import edu.upc.prop.cluster422.Domini.Classes.RespostaEnquesta;
import edu.upc.prop.cluster422.Domini.Classes.Enquesta;
import edu.upc.prop.cluster422.Domini.Classes.Pregunta;
import edu.upc.prop.cluster422.Domini.Controladors.CustomException;

import javax.security.auth.login.CredentialException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Aquesta classe és el controlador que s'encarrega de tots els tramits
 * relacionats amb la persistència de les dades.
 *
 * @author Ramon Sánchez
 * @version 14/12/2025/1
 */
public class CtrlPersistencia {

    /**
     * Instància única del controlador de persistencia per complir amb el patro
     * singleton.
     */
    private static CtrlPersistencia instance;

    /**
     * Instància del gestor d'usuaris.
     */
    private static GestorUsuaris gestorUsuaris;

    /**
     * Instància del gestor d'enquestes.
     */
    private static GestorEnquestes gestorEnquestes;

    /**
     * Instància del gestor de respostes.
     */
    private static GestorRespostes gestorRespostes;

    /**
     * Creadora privada de la classe CtrlPersisitencia per complir amb el patro
     * singleton.
     * 
     * @throws IOException Si no es poden crear els directoris
     */
    private CtrlPersistencia() throws IOException {
        Path path_data = Paths.get("../DATA");
        Files.createDirectories(path_data);
        gestorUsuaris = GestorUsuaris.getInstance(path_data);
        gestorEnquestes = GestorEnquestes.getInstance(path_data);
        gestorRespostes = GestorRespostes.getInstance(path_data);
    }

    /**
     * getter de l'unica instancia del CtrlPersistencia
     * 
     * @return l'unica instancia del CtrlPersistencia, si cal la crea
     * @throws IOException Si no es poden crear els directoris
     */
    public static CtrlPersistencia getInstance() throws IOException {
        if (instance == null) {
            instance = new CtrlPersistencia();
        }
        return instance;
    }

    // Metodes d'usuari

    /**
     * Crea un usuari nou
     * 
     * @param user L'usuari a crear
     * @throws IOException     Si hi ha un error d'escriptura/lectura de fitxers
     * @throws CustomException Si el nom d'usuari ja existeix
     */
    public void crearUsuari(Usuari user) throws IOException, CustomException {
        gestorUsuaris.crearUsuari(user);
    }

    /**
     * Retorna l'usuari amb totes les dades amb el nom d'usuari donat
     * 
     * @param nomusuari El nom d'usuari de l'usuari a retornar
     * @return L'usuari privat amb el nom d'usuari donat
     * @throws IOException            Si hi ha un error d'escriptura/lectura de
     *                                fitxers
     * @throws NoSuchElementException Si no existeix l'usuari amb el nom donat
     */
    public Usuari getPrivateUsuari(String nomusuari) throws IOException, NoSuchElementException {
        return gestorUsuaris.getPrivateUsuari(nomusuari);
    }
    /**
     * Retorna l'usuari amb totes les dades amb l'id d'usuari donat
     *
     * @param idusuari L'id d'usuari de l'usuari a retornar
     * @return L'usuari privat amb l'id d'usuari donat
     * @throws IOException            Si hi ha un error d'escriptura/lectura de
     *                                fitxers
     * @throws NoSuchElementException Si no existeix l'usuari amb l'id donat
     */
    public Usuari getPrivateUsuariById(int idusuari) throws IOException, NoSuchElementException {
        return gestorUsuaris.getPrivateUsuariById(idusuari);
    }

    /**
     * Retorna l'usuari amb les dades publiques amb el nom d'usuari donat
     * 
     * @param nomusuari El nom d'usuari de l'usuari a retornar
     * @return L'usuari public amb el nom d'usuari donat
     * @throws IOException            Si hi ha un error d'escriptura/lectura de
     *                                fitxers
     * @throws NoSuchElementException Si no existeix l'usuari amb el nom donat
     */
    public Usuari getPublicUsuari(String nomusuari) throws IOException, NoSuchElementException {
        return gestorUsuaris.getPublicUsuari(nomusuari);
    }

    /**
     * Realitza el log in d'un usuari
     * 
     * @param nom         El nom d'usuari
     * @param contrasenya La contrasenya de l'usuari
     * @return L'usuari privat amb el nom i contrasenya donats
     * @throws IOException            Si hi ha un error d'escriptura/lectura de
     *                                fitxers
     * @throws NoSuchElementException Si no existeix l'usuari amb el nom donat
     * @throws CredentialException    Si la contrasenya no coincideix
     */
    public Usuari logIn(String nom, String contrasenya)
            throws IOException, NoSuchElementException, CredentialException {
        return gestorUsuaris.logIn(nom, contrasenya);
    }

    /**
     * Actualitza les dades d'un usuari
     * 
     * @param user L'usuari amb les dades actualitzades
     * @throws IOException     Si hi ha un error d'escriptura/lectura de fitxers
     * @throws CustomException Si el nom d'usuari ja existeix
     */
    public void updateUsuari(Usuari user) throws IOException, CustomException {
        gestorUsuaris.updateUsuari(user);
    }

    /**
     * Elimina un usuari
     * @param nom el nom de l'usuari a eliminar
     * @throws IOException Si hi ha un error d'escriptura/lectura de fitxers
     * @throws NoSuchElementException Si no existeix l'usuari amb el nom donat
     */
    public void deleteUsuari(String nom) throws IOException, CustomException {
        try{
            Usuari traidor =  gestorUsuaris.getPrivateUsuari(nom);
            Integer[] enquestesRespostes = traidor.getRespostesCreadesTitol();
            int id = traidor.getId();
            for (int r : enquestesRespostes) {
                deleteResposta(r, id);
            }
            Integer[] enquestesCreades = traidor.getEnquestesCreadesTitol();
            for (int e : enquestesCreades) {
                deleteEnquesta(gestorEnquestes.getEnquestaById(e).getTitol());
            }
            gestorUsuaris.deleteUsuari(id);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("L'usuari amb nom " + nom + " no existeix.");
        } catch (Exception e) {
            throw new IOException("Error en la concurrencia dels fitxers.");
        }
    }

    /**
     * Retorna una llista amb els noms d'usuari corresponents als ids donats
     * 
     * @param ids Array d'ids d'usuaris
     * @return Llista amb els noms d'usuari corresponents als ids donats
     * @throws IOException Si hi ha un error d'escriptura/lectura de fitxers
     */
    public ArrayList<String> getnomsUsuarus(List<Integer> ids) throws IOException {
        return gestorUsuaris.getnomsUsuarus(ids);
    }

    /**
     * Esborra una enquesta creada de la llista de enquestes creades d'un usuari
     * 
     * @param idusuari   id de l'usuari
     * @param idenquesta id de l'enquesta
     * @throws IOException            si hi ha un error d'escriptura/lectura de
     *                                fitxers
     * @throws NoSuchElementException si no existeix l'usuari o l'enquesta
     */
    public void deleteEnquestaUsuari(int idusuari, int idenquesta) throws IOException, NoSuchElementException {
        gestorUsuaris.deleteEnquestaUsuari(idusuari, idenquesta);
    }

    /**
     * Esborra una resposta creada de la llista de respostes creades d'un usuari
     * 
     * @param idusuari   id de l'usuari
     * @param idresposta id de l'enquesta resposta
     * @throws IOException            si hi ha un error d'escriptura/lectura de
     *                                fitxers
     * @throws NoSuchElementException si no existeix l'usuari o la resposta
     */
    public void deleteRespostaUsuari(int idusuari, int idresposta) throws IOException, NoSuchElementException {
        gestorUsuaris.deleteRespostaUsuari(idusuari, idresposta);
    }

    // Metodes d' enquesta

    /**
     * Crea una enquesta nova
     *
     * @param e L'enquesta a crear
     * @return id otorgat a l'enquesta creada
     * @throws IOException Si hi ha un error d'escriptura/lectura de fitxers
     */
    public int crearEnquesta(Enquesta e) throws IOException {
        return gestorEnquestes.crearEnquesta(e);
    }

    /**
     * Retorna l'enquesta del títol donat
     * 
     * @param titolEnquesta El titol de l'enquesta a retornar
     * @return L'enquesta del titol donat
     * @throws IOException     Si hi ha un error d'escriptura/lectura de fitxers
     * @throws CustomException Si no existeix l'enquesta amb el titol donat
     */
    public Enquesta getEnquesta(String titolEnquesta) throws IOException, CustomException {
        return gestorEnquestes.getEnquesta(titolEnquesta);
    }

    /**
     * Retorna l'enquesta amb l'identificador donat.
     * 
     * @param idEnquesta Identificador de l'enquesta a obtenir.
     * @return Enquesta corresponent a l'identificador
     * @throws IOException     Si hi ha un error d'escriptura/lectura de fitxers
     * @throws CustomException Si no existeix una enquesta amb l'identificador donat
     */
    public Enquesta getEnquestaById(int idEnquesta) throws IOException, CustomException {
        return gestorEnquestes.getEnquestaById(idEnquesta);
    }

    /**
     * Elimina l'enquesta amb el titol donat
     * 
     * @param titolEnquesta El titol de l'enquesta a eliminar
     * @throws IOException     Si hi ha un error d'escriptura/lectura de fitxers
     * @throws CustomException Si no existeix l'enquesta amb el titol donat
     */
    public void deleteEnquesta(String titolEnquesta) throws IOException, CustomException {
        Map<Integer, RespostaEnquesta> respostes = gestorRespostes.getRespostesEnquesta(gestorEnquestes.getEnquesta(titolEnquesta).getId());
        for(RespostaEnquesta r : respostes.values()) {
            deleteRespostaUsuari(r.getUsuari(), r.getEnquesta());
        }
        try {
            deleteEnquestaUsuari(gestorEnquestes.getEnquesta(titolEnquesta).getCreador(), gestorEnquestes.getEnquesta(titolEnquesta).getId());
        } catch (NoSuchElementException e) {
            // si l'usuari acaba de crear l'enquesta encara no sha guardat en les dades
        }
        gestorEnquestes.deleteEnquesta(titolEnquesta);
    }


    /**
     * Retorna una llista amb els titols de totes les enquestes
     * 
     * @return Llista amb els titols de totes les enquestes
     * @throws IOException Si hi ha un error d'escriptura/lectura de fitxers
     */
    public Map<String, Integer> getLlistatEnquestes() throws IOException {
        return gestorEnquestes.getLlistatEnquestes();
    }

    /**
     * Afegeix una pregunta a l'enquesta amb el titol donat
     * 
     * @param titolEnquesta El titol de l'enquesta a la que s'afegeix la pregunta
     * @param p             La pregunta a afegir
     * @throws IOException Si hi ha un error d'escriptura/lectura de fitxers
     * @throws CustomException Si no existeix l'enquesta amb el titol donat
     */
    public void addQuestionToEnquestaJSON(String titolEnquesta, Pregunta p) throws IOException, CustomException {
        gestorEnquestes.addQuestionToEnquestaJSON(titolEnquesta, p);
    }

    /**
     * Elimina una pregunta de l'enquesta amb el titol donat
     * 
     * @param titolEnquesta El titol de l'enquesta de la que s'elimina la pregunta
     * @param numPregunta   El numero de la pregunta a eliminar
     * @throws IOException Si hi ha un error d'escriptura/lectura de fitxers
     * @throws CustomException Si no existeix l'enquesta amb el titol donat
     */
    public void deleteQuestionFromEnquestaJSON(String titolEnquesta, Integer numPregunta) throws IOException, CustomException {
        gestorEnquestes.deleteQuestionFromEnquestaJSON(titolEnquesta, numPregunta);
    }

    /**
     * Actualitza el titol de l'enquesta amb el titol donat
     * 
     * @param titolEnquesta El titol de l'enquesta a actualitzar
     * @param nouTitol      El nou titol de l'enquesta
     * @throws IOException Si hi ha un error d'escriptura/lectura de fitxers
     * @throws CustomException Si no existeix l'enquesta amb el titol donat
     */
    public void setTitleEnquesta(String titolEnquesta, String nouTitol) throws IOException, CustomException {
        gestorEnquestes.setTitleEnquesta(titolEnquesta, nouTitol);
    }

    // Metodes de resposta

    /**
     * Crea una resposta per una enquesta i la persisteix
     * 
     * @param r          RespostaEnquesta a crear
     * @param idUsuari   Id de l'usuari que crea la resposta
     * @param idEnquesta Id de l'enquesta a la que pertany la resposta
     * @throws IOException Si hi ha un error d'escriptura/lectura de fitxers
     */
    public void crearResposta(RespostaEnquesta r, int idUsuari, int idEnquesta) throws IOException {
        gestorRespostes.crearResposta(r, idUsuari, idEnquesta);
    }

    /**
     * Obté totes les respostes d'una enquesta
     * 
     * @param idEnquesta Id de l'enquesta
     * @return Mapa de RespostaEnquesta per l'enquesta indicada
     * @throws IOException     Si hi ha un error d'escriptura/lectura de fitxers
     * @throws CustomException Si hi ha errors en el processament de respostes
     */
    public Map<Integer,RespostaEnquesta> getRespostesEnquesta(int idEnquesta)
            throws IOException, CustomException {
        return gestorRespostes.getRespostesEnquesta(idEnquesta);
    }

    /**
     * Obté la resposta d'un usuari per una enquesta concreta
     * 
     * @param idEnquesta Id de l'enquesta
     * @param idUsuari   Id de l'usuari
     * @return RespostaEnquesta de l'usuari per l'enquesta
     * @throws IOException     Si hi ha un error d'escriptura/lectura de fitxers
     * @throws CustomException Si no existeix la resposta o hi ha errors en el
     *                         processament
     */
    public RespostaEnquesta getRespostaUsuari(int idEnquesta, int idUsuari)
            throws IOException, CustomException {
        return gestorRespostes.getRespostaUsuari(idEnquesta, idUsuari);
    }

    /**
     * Esborra la resposta d'un usuari per una enquesta
     * 
     * @param idEnquesta Id de l'enquesta
     * @param idUsuari   Id de l'usuari
     * @throws IOException Si hi ha un error d'escriptura/lectura de fitxers
     */
    public void deleteResposta(int idEnquesta, int idUsuari) throws IOException {
        gestorRespostes.deleteResposta(idEnquesta, idUsuari);
        try {
            deleteRespostaUsuari(idUsuari, idEnquesta);
        } catch (NoSuchElementException e) {
            // si la resposta s'acaba de crear encara no s'ha guardat a les dades
        }
    }

    /**
     * Actualitza una resposta existent
     * 
     * @param r          Resposta actualitzada
     * @param idUsuari   Id de l'usuari
     * @param idEnquesta Id de l'enquesta
     * @throws IOException Si hi ha un error d'escriptura/lectura de fitxers
     */
    public void updateResposta(RespostaEnquesta r, int idUsuari, int idEnquesta) throws IOException {
        gestorRespostes.updateResposta(r, idUsuari, idEnquesta);
    }

    /**
     * Comprova si existeix una resposta d'un usuari per una enquesta
     * 
     * @param idEnquesta Id de l'enquesta
     * @param idUsuari   Id de l'usuari
     * @return true si existeix la resposta, false en cas contrari
     * @throws IOException Si hi ha un error d'escriptura/lectura de fitxers
     */
    public boolean existsResposta(int idEnquesta, int idUsuari) throws IOException {
        return gestorRespostes.existsResposta(idEnquesta, idUsuari);
    }

}
