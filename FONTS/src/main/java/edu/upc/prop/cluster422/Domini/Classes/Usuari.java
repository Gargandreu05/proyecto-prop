package edu.upc.prop.cluster422.Domini.Classes;

import java.util.*;

/**
 * Aquesta clase representa un Usuari.
 * 
 * @author: Ramon Sanchez
 * @version: 14/12/2025/1
 */

public class Usuari {

    // Atributos
    private final Integer id;
    /** El nom d'usuari de l'Usuari */
    private String nomusuari;
    /** La contrasenya de l'Usuari */
    private String contrasenya;
    /** L'edat de l'Usuari */
    private int edat;
    /** L'email de l'Usuari */
    private String email;

    // Relaciones
    /** Set de ids d'enquestes creades per l'Usuari */
    private TreeSet<Integer> enquestescreades = new TreeSet<>();
    /** Set de ids de respostes a enquestes respostes per l'Usuari */
    private TreeSet<Integer> respostescreades = new TreeSet<>();

    // Constructora

    /**
     * Constructora de la clase Usuari
     * 
     * @param nomusuari   El paràmetre nomusuari defineix el nom de l'usuari
     * @param contrasenya El paràmetre contrasenya defineix la contrasenya de
     *                    l'usuari
     * @throws IllegalArgumentException Si el nom d'usuari o la contrasenya no
     *                                  compleixen els requisits
     */
    public Usuari(String nomusuari, String contrasenya, Integer id) throws IllegalArgumentException {
        if (nomusuari == null)
            throw new IllegalArgumentException("El nom no pot estar buit.");
        if (nomusuari.length() < 4)
            throw new IllegalArgumentException("El nom ha de contenir mes de 3 caracters.");
        if (contrasenya == null)
            throw new IllegalArgumentException("La contrasenya no pot estar buida.");
        if (contrasenya.length() < 4)
            throw new IllegalArgumentException("La contrasenya ha de contenir mes de 3 caracters.");
        if (nomusuari.contains("\"") || contrasenya.contains("\""))
            throw new IllegalArgumentException(
                    "El nom d'usuari i la contrasenya no poden contenir els seguents caracters: \".");
        this.id = id;
        this.nomusuari = nomusuari;
        this.contrasenya = contrasenya;
        this.edat = -1;
        this.email = "";
    }

    // Setters

    /**
     * Setter del nom d'usuari
     * 
     * @param nom El paràmetre nom defineix el nou nom d'usuari
     * @throws IllegalArgumentException Si el nom no compleix els requisits
     */
    public void changeNom(String nom) throws IllegalArgumentException {
        if (nomusuari == null)
            throw new IllegalArgumentException("El nom no pot estar buit.");
        if (nomusuari.length() < 4)
            throw new IllegalArgumentException("El nom ha de contenir mes de 3 caracters.");
        if (nomusuari.contains("\"") || contrasenya.contains("\""))
            throw new IllegalArgumentException(
                    "El nom d'usuari i la contrasenya no poden contenir els seguents caracters: \".");
        this.nomusuari = nom;

    }

    /**
     * Setter de l'edat de l'usuari
     * 
     * @param edat El paràmetre edat defineix la nova edat de l'usuari
     * @throws IllegalArgumentException Si l'edat no es valida
     */
    public void setEdat(int edat) throws IllegalArgumentException {
        if (edat < -1 || edat > 120)
            throw new IllegalArgumentException("Edat no valida, ha de ser un numero entre -1 i 120.");
        else
            this.edat = edat;
    }

    /**
     * Setter de l'email de l'usuari
     * 
     * @param email El paràmetre email defineix el nou email de l'usuari
     * @throws IllegalArgumentException Si l'email no es valid
     */
    public void setEmail(String email) throws IllegalArgumentException {
        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("Aquest email no es valid.");
        if (email.contains("\""))
            throw new IllegalArgumentException("L'email no pot contenir cometes: \".");
        else
            this.email = email;
    }

    /**
     * Setter del llistat d'ids d'enquestes creades
     * 
     * @param ids El paràmetre ids defineix el llistat d'ids
     */
    public void setEnquestescreades(Set<Integer> ids) {
        enquestescreades.clear();
        if (!ids.isEmpty())
            enquestescreades.addAll(ids);
    }

    /**
     * Setter del llistat d'ids de enquestes repostes
     * 
     * @param ids El paràmetre ids defineix el llistat d'ids
     */
    public void setRespostescreades(Set<Integer> ids) {
        respostescreades.clear();
        if (!ids.isEmpty())
            respostescreades.addAll(ids);
    }

    // Metodes

    /**
     * Metode per cambiar la contrasenya de l'usuari
     * 
     * @param novacontrasenya La nova contrasenya de l'usuari
     * @throws IllegalArgumentException Si la nova contrasenya no compleix els
     *                                  requisits
     */
    public void changeContrasenya(String novacontrasenya) throws IllegalArgumentException {
        if (novacontrasenya == null)
            throw new IllegalArgumentException("La nova contrasenya no pot estar buida.");
        if (novacontrasenya.length() < 4)
            throw new IllegalArgumentException("La contrasenya ha de contenir mes de 3 caracters.");
        if (novacontrasenya.contains("\""))
            throw new IllegalArgumentException("La contrasenya no pot contenir cometes dobles (\").");

        this.contrasenya = novacontrasenya;
    }

    /**
     * Metode per afegir una enquesta creada per l'usuari al seu llistat d'enquestes
     * creades
     * 
     * @param eid L'id de l'enquesta creada per l'usuari que s'ha d'afegir al seu
     *            llistat d'enquestes creades
     * @throws IllegalArgumentException Si l'enquesta ja existeix entre les
     *                                  enquestes creades per l'usuari
     */
    public void addEnquestaCreada(int eid) throws IllegalArgumentException {
        if (enquestescreades.contains(eid))
            throw new IllegalArgumentException(
                    "Aquesta enquesta ja existeix entre les enquestes creades per aquest Usuari.");
        this.enquestescreades.add(eid);
    }

    /**
     * Metode per afegir la resposta de l'usuari a una enquesta al seu llistat
     * d'enquestes respostes
     * 
     * @param rid L'id de la resposta de l'usuari a una enquesta que s'ha d'afegir
     *            al seu llistat de respostes creades
     * @throws IllegalArgumentException Si la resposta ja existeix entre les
     *                                  respostes creades per l'usuari
     */
    public void addRespostacreada(int rid) throws IllegalArgumentException {
        if (respostescreades.contains(rid))
            throw new IllegalArgumentException(
                    "Aquesta resposta ja existeix entre les respostes creades per aquest Usuari.");
        this.respostescreades.add(rid);
    }

    /**
     * Metode per eliminar una enquesta creada per l'usuari del seu llistat
     * d'enquestes creades
     * 
     * @param eid L'id de l'enquesta creada per l'usuari que s'ha d'eliminar del seu
     *            llistat d'enquestes creades
     * @throws NoSuchElementException Si l'enquesta no existeix entre les enquestes
     *                                creades per l'usuari
     */
    public void deleteEnquestaCreada(int eid) throws NoSuchElementException {
        if (!this.enquestescreades.remove(eid)) {
            throw new NoSuchElementException(
                    "Aquesta enquesta no existeix entre les enquestes creades per aquest Usuari.");
        }
    }

    /**
     * Metode per eliminar una resposta de l'usuari a una enquesta del seu llistat
     * d'enquestes respostes
     * 
     * @param rid L'id de la resposta de l'usuari a una enquesta que s'ha d'eliminar
     *            del seu llistat de respostes creades
     * @throws NoSuchElementException Si la resposta no existeix entre les respostes
     *                                creades per l'usuari
     */
    public void deleteRespostacreada(int rid) throws NoSuchElementException {
        if (!this.respostescreades.remove(rid)) {
            throw new NoSuchElementException(
                    "Aquesta resposta no existeix entre les respostes creades per aquest Usuari.");
        }
    }

    // Getters
    public Integer getId() {
        return this.id;
    }

    /**
     * Comprova si l'usuari actual ha respost una enquesta específica.
     * 
     * @param idenquesta Id de l'enquesta a comprovar.
     * @return Cert si l'usuari ha respost l'enquesta, fals en cas contrari.
     */
    public boolean haRespostEnquesta(int idenquesta) {
        return respostescreades.contains(idenquesta);
    }

    /**
     * Getter del nom d'usuari
     * 
     * @return Retorna el nomusuari de l'usuari
     */
    public String getUsername() {
        return this.nomusuari;
    }

    /**
     * Getter de la contrasenya
     * 
     * @return Retorna la contrasenya de l'usuari
     */
    public String getContrasenya() {
        return this.contrasenya;
    }

    /**
     * Getter de l'email
     * 
     * @return Retorna l'email de l'usuari
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Getter de l'edat
     * 
     * @return Retorna l'edat de l'usuari
     */
    public int getEdat() {
        return this.edat;
    }

    /**
     * Getter dels titols de les enquestes creades per l'usuari
     * 
     * @return Retorna un array de Strings que són els titols de les enquestes
     *         creades per l'usuari
     */
    public Integer[] getEnquestesCreadesTitol() {
        return enquestescreades.toArray(new Integer[0]);
    }

    /**
     * Getter dels titols de les enquestes respostes per l'usuari
     * 
     * @return Retorna un array Strings que són els titols de les enquestes
     *         respostes per l'usuari
     */
    public Integer[] getRespostesCreadesTitol() {
        return respostescreades.toArray(new Integer[0]);
    }

    /**
     * Getter de totes les dades públiques de l'usuari
     * 
     * @return Retorna un array de Strings amb totes les dades públiques de l'usuari
     */
    public String[] getDadesPubliques() {
        String edatString;
        if (edat == -1)
            edatString = "No especificada";
        else
            edatString = Integer.toString(edat);
        String emailString = this.email;
        if (emailString.isEmpty())
            emailString = "No especificat";
        return new String[] { this.nomusuari, emailString, edatString };
    }

    /**
     * Getter de totes les dades privades de l'usuari
     * 
     * @return Retorna un array de Strings amb totes les dades privades de l'usuari
     */
    public String[] getDadesPrivades() {
        String edatString;
        if (edat == -1)
            edatString = "No especificat";
        else
            edatString = Integer.toString(edat);
        String emailString = this.email;
        if (emailString.isEmpty())
            emailString = "No especificat";
        return new String[] { this.nomusuari, this.contrasenya, emailString, edatString };
    }
}