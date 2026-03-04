package edu.upc.prop.cluster422.Domini.Classes;

import edu.upc.prop.cluster422.Domini.Controladors.CustomException;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * Resposta que emmagatzema un vector d'enters.
 * S'utilitza per preguntes de selecció múltiple (llista d'índexs d'opcions seleccionades).
 * 
 * @author Enzo Miquel
 * @version 17/11/2025/Entrega
 */

public class RespostaVector extends RespostaPregunta {
    
    /**
     * Vector d'enters que representa la resposta.
     * Cada enter correspon a l'índex d'una opció seleccionada.
     */
    private Vector<Integer> entrega;

    // ---- CONSTRUCTORA ----

    /**
     * Constructora per defecte. Crea una resposta amb un vector buit.
     */
    public RespostaVector() {
        this.entrega = new Vector<>();
    }

    /**
     * Constructora amb vector inicial.
     * @param entrega El vector d'enters de la resposta.
     */
    public RespostaVector(Vector<Integer> entrega) {
        if (entrega == null) {
            this.entrega = new Vector<>();
        } else {
            this.entrega = new Vector<>(entrega);
        }
    }

    // ---- GETTERS ----

    /**
     * Retorna el vector d'enters de la resposta.
     * @return Una còpia del vector emmagatzemat.
     */
    public Vector<Integer> getEntrega() {
        return new Vector<>(this.entrega);
    }

    // ---- SETTERS ----

    /**
     * Estableix el vector d'enters de la resposta.
     * @param entrega El nou vector d'enters.
     */
    public void setEntrega(Vector<Integer> entrega) {
        if (entrega == null) {
            this.entrega = new Vector<>();
        } else {
            this.entrega = new Vector<>(entrega);
        }
    }

    // ---- MÈTODES ----

    /**
     * Retorna la puntuació de la resposta per al clustering.
     * Es calcula com la mitjana dels valors del vector.
     * Si el vector és buit, retorna 0.0.
     * @return La mitjana dels valors del vector, o 0.0 si està buit.
     */
    @Override
    public double TreurePuntuacio() {
        if (this.entrega == null || this.entrega.isEmpty()) {
            return 0.0;
        }
        double suma = 0.0;
        for (Integer valor : this.entrega) {
            if (valor != null) {
                suma += valor;
            }
        }
        return suma / this.entrega.size();
    }

    /**
     * Comprova si la resposta està buida (no contestada).
     * @return true si el vector és null o buit, false altrament.
     */
    @Override
    public boolean esBuida() {
        return this.entrega == null || this.entrega.isEmpty();
    }

    /**
     * Calcula la distància local normalitzada segons el coeficient de Jaccard per a conjunts:
     * D_local(Xi, Xj) = 1 - Jaccard(Xi, Xj) = 1 - |Xi ∩ Xj| / |Xi ∪ Xj|
     * Aquesta fórmula mesura la dissimilitud entre dos conjunts de valors.
     * @param altra L'altra resposta amb la qual calcular la distància.
     * @return La distància normalitzada entre [0,1]. Retorna 1.0 si alguna de les respostes és buida.
     */
    @Override
    public double calcularDistanciaLocal(RespostaPregunta altra) {
        if(this.esBuida() && altra.esBuida()) {
            return 0.0; // Ambdós buits són idèntics
        }

        // Si alguna de les respostes està buida, retorna distància màxima
        if (this.esBuida() || altra.esBuida()) {
            return 1.0;
        }
        
        // Comprova que l'altra resposta també sigui de tipus Vector
        if (!(altra instanceof RespostaVector)) {
            return 1.0; // Tipus incompatibles
        }
        
        RespostaVector altraVec = (RespostaVector) altra;
        
        // Converteix els vectors a conjunts per facilitar les operacions
        Set<Integer> conjunt1 = new HashSet<>(this.entrega);
        Set<Integer> conjunt2 = new HashSet<>(altraVec.entrega);
        
        // Calcula la intersecció
        Set<Integer> interseccio = new HashSet<>(conjunt1);
        interseccio.retainAll(conjunt2);
        int tamInterseccio = interseccio.size();
        
        // Calcula la unió
        Set<Integer> unio = new HashSet<>(conjunt1);
        unio.addAll(conjunt2);
        int tamUnio = unio.size();
        
        // Cas especial: si ambdós conjunts són buits
        if (tamUnio == 0) {
            return 0.0; // Són idèntics (tots dos buits)
        }
        
        // Calcula el coeficient de Jaccard
        double jaccard = (double) tamInterseccio / tamUnio;
        
        // La distància és 1 - Jaccard
        return 1.0 - jaccard;
    }
    
    /**
     * Modifica la resposta amb una nova dada.
     * @param novaDada La nova dada que substituirà l'actual.
     */
    @Override
    public void modificarResposta(Object novaDada) throws CustomException {
        if (!(novaDada instanceof Vector)) {
            throw new CustomException("La nova dada ha de ser un Vector d'Integers.");
        }
        @SuppressWarnings("unchecked")
        Vector<Integer> nouVector = (Vector<Integer>) novaDada;
        this.setEntrega(nouVector);
    }

}
