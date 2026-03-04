package edu.upc.prop.cluster422.Domini.Classes;

import java.util.ArrayList;
import java.util.List;
import edu.upc.prop.cluster422.Domini.Controladors.CustomException;

/**
 * Aquesta classe ha estat afegida per a la neteja del codi i la seva organització.
 * Representa un clúster dins l'algorisme de clustering.
 * @author Andreu Puerto
 * @version 17/11/2025/Entrega
 */

public class Cluster{
    /**
     * String del nom de l'usuari que és representant del clúster. Serveix per al K-Medoids.
     */
    private Integer representant;

    /**
     * Llista dels noms dels usuaris que pertanyen al clúster.
     */
    private List<Integer> membres;

    /**
     * Llista dels valors del centroide. Serveixen per al K-Means.
     */
    private List<Object> centroide;


    // ---- CONSTRUCTORA ----

    /**
     * Constructor a partir d'un objecte RespostaEnquesta, en aquest cas el cluster té representant i és l'usuari de la RespostaEnquesta.
     * Ideal per a K-Medoids.
     * @param re L'objecte RespostaEnquesta que representa el centroide.
     */
    public Cluster(Integer re){
        this.representant = re;
        this.membres = new ArrayList<>();
        this.centroide = new ArrayList<>();
    }

    /**
     * Constructor a partir d'una llista de RespostaEnquesta que seran els membres del cluster, aquest cluster no tindrà representant.
     * Ideal per a K-Means i K-Means++.
     * @param  membres La llista membres correspon als membres del cluster.
     */
    public Cluster(List<Integer> membres){
        this.representant = null;
        this.membres = membres;
    }

    // ---- SETTERS ----
    
    /**
     * Afegeix un membre més al clúster.
     * @param membre String del membre nou a afegir.
     */
    public void afegirMembre(Integer membre){
        this.membres.add(membre);
    }

     /**
     * Estableix una llista d'objectes com al nou centroide.
     * @param centroide Llista dels valors del Centroide (No representa cap membre).
     */
    public void afegirCentroide(List<Object> centroide){
        this.centroide = centroide;
    }

    // ---- GETTERS ----

    /**
     * Obté la informació de les respostes dels membres del clúster.
     * @return La llista de respostes que correspon al centroide del cluster.
     */
    public List<Object> getValorsCentroide(){
        return this.centroide;
    }

    /**
     * Obté la informació dels membres del clúster.
     * @return Una Llista de Strings que conté els noms dels usuaris que pertanyen al clúster.
     */
    public List<Integer> getMembres(){
        return this.membres;
    }

    /**
     * Indica de si el clúster té representant real o no.
     * @return True si el clúster té representant, false en cas contrari.
     */
    public boolean teRepresentant(){
        return this.representant != null;
    }

    /**
     * Obté el punt de dades real que representa el clúster.
     * @return El string del nom de l'usuari que és el Medoide.
     * @exception CustomException El cluster no té representant real.
     */
    public Integer getRepresentantReal() throws CustomException{
        if (!teRepresentant()) throw new CustomException("El cluster no té representant");
        else return this.representant;
    }
    
    // ---- EXTRA ----

    /**
     * Elimina un membre del clúster. Aquest membre ha d'estar dins el clúster.
     * @param s String del nom del membre a eliminar del clúster.
     */
    public void removeMembre(String s) throws CustomException{
        if(!this.membres.remove(s)) throw new CustomException("El membre no existeix al cluster");
    }

}