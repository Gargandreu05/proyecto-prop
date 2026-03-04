package edu.upc.prop.cluster422.Domini.Classes;

import edu.upc.prop.cluster422.Domini.Controladors.CustomException;

/**
 * Classe abstracta que representa una resposta individual a una pregunta d'una enquesta.
 * Les subclasses concretes defineixen el tipus de dada que es pot emmagatzemar com a resposta.
 * 
 * @author Enzo Miquel
 * @version 17/11/2025/Entrega
 */

public abstract class RespostaPregunta {
   
    /**
     * Mètode abstract per obtenir una puntuació numèrica representativa de la resposta.
     * Aquesta puntuació s'utilitzarà per als algorismes de clustering.
     * @return Un valor double que representa la resposta.
     */
    public abstract double TreurePuntuacio();
    
    /**
     * Comprova si la resposta està buida o no contestada.
     * @return true si la resposta no ha estat contestada, false altrament.
     */
    public abstract boolean esBuida();
    
    /**
     * Calcula la distància local normalitzada [0,1] entre aquesta resposta i una altra.
     * La implementació específica depèn del tipus de resposta.
     * @param altra L'altra resposta amb la qual calcular la distància.
     * @return La distància normalitzada entre [0,1].
     */
    public abstract double calcularDistanciaLocal(RespostaPregunta altra);
    

    /**
     * Modifica la resposta amb una nova dada.
     * @param novaDada La nova dada que substituirà l'actual.
     */
    public abstract void modificarResposta(Object novaDada) throws CustomException;
}

