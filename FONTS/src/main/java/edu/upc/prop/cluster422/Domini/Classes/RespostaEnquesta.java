package edu.upc.prop.cluster422.Domini.Classes;

import edu.upc.prop.cluster422.Domini.Controladors.CustomException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * La clase RespostaEnquesta implementa totes les funcionalitats necessaries per mantenir
 * la coherència entre les respostes a enquestes emmagatzemant quin usuari ha respost una
 * donada enquesta i les preguntes respostes a la enquesta.
 * 
 * @author Grup Roger Guinovart
 * @version 17/11/2025/Entrega
 */

public class RespostaEnquesta {
    // Atributs
    /**
     * Id de l'enquesta que ha sigut resposta.
     */
    private final int idenquesta;
    /**
     * Id de Usuari que ha respost l'enquesta.
     */
    private final int idusuari;
    /**
     * Conjunt de respostes a les preguntes de l'enquesta.
     */
    private List<RespostaPregunta> respostesPregunta;

    // Creadora

    /**
     * Crea una resposta enquesta i n'associa l'usuari, l'enquesta resposa i les respostes a les preguntes.
     * @param ide id de l'Enquesta que ha sigut resposta
     * @param idu id de l'Usuari que ha respost enquesta
     * @param rp Llista de respostes a preguntes
     */
    public RespostaEnquesta(int ide, int idu, List<RespostaPregunta> rp) throws CustomException, IllegalArgumentException {
        this.idenquesta = ide;
        this.idusuari = idu;
        this.respostesPregunta = (rp != null) ? rp : new ArrayList<>(); // Aixo es fa per si rp es null que no doni NullPointerExc
    }

    // Getters

    /**
     * Consulta l'enquesta associada a la resposta de l'enquesta.
     * @return Retorna l'id de l'enquesta associat a la resposta de l'enquesta
     */
    public int getEnquesta() {
        return this.idenquesta;
    }

    /**
     * Consulta l'usuari que ha respost l'enquesta.
     * @return Retorna l'id de l'usuari que ha respost l'enquesta
     */
    public int getUsuari() {
        return this.idusuari;
    }

    /**
     * Consulta el conjunt de respostes a l'enquesta resposta per un donat usuari.
     * @return Retorna el conjunt de respostes a l'enquesta resposta per un donat usuari
     */
    public List<RespostaPregunta> getRespostaAPregunta() {
        return this.respostesPregunta;
    }

    /**
     * Afegeix una resposta a pregunta a la resposta enquesta.
     * @param r Resposta a pregunta que es vol afegir a la resposta enquesta.
     */
    public void afegirRespostaPregunta(RespostaPregunta r){
        respostesPregunta.add(r);
    }

    /**
     * Elimina una resposta a pregunta de la resposta enquesta.
     * @param r Resposta a pregunta que es vol eliminar de la resposta enquesta.
     */
    public void esborrarRespostaPregunta(RespostaPregunta r) {
        respostesPregunta.remove(r);
    }

    /**
     * Modifica la RespostaPregunta en la posició indicada amb la nova dada.
     * @param index Posició de la RespostaPregunta a modificar.
     * @param novaDada Nova dada que substituirà l'actual.
     */
    public void modificarRespostaPregunta(int index, Object novaDada) throws CustomException, IndexOutOfBoundsException {
        if (index < 0 | index >= respostesPregunta.size()) {
            throw new IndexOutOfBoundsException("Índex fora del rang per a les respostes a les preguntes.");
        }
        RespostaPregunta rp = respostesPregunta.get(index);
        rp.modificarResposta(novaDada);

    }
}