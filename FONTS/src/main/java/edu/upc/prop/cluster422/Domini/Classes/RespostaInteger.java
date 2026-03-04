package edu.upc.prop.cluster422.Domini.Classes;

import edu.upc.prop.cluster422.Domini.Controladors.CustomException;

/**
 * Resposta que emmagatzema un valor enter (Integer).
 * S'utilitza per preguntes de format numèric o selecció d'una opció única.
 * 
 * @author Enzo Miquel
 * @version 17/11/2025/Entrega
 */

public class RespostaInteger extends RespostaPregunta {
    
    /*
     * L'enter emmagatzemat com a resposta.
     */
    private Integer entrega;

    // ---- CONSTRUCTORA ----

    /**
     * Constructora per defecte. Crea una resposta sense valor (no contestada).
     */
    public RespostaInteger() {
        this.entrega = null;
    }

    /**
     * Constructora amb valor inicial.
     * @param entrega El valor enter de la resposta.
     */
    public RespostaInteger(Integer entrega) {
        this.entrega = entrega;
    }

    // ---- GETTERS ----

    /**
     * Retorna el valor enter de la resposta.
     * @return L'enter emmagatzemat, o null si no s'ha contestat.
     */
    public Integer getEntrega() {
        return this.entrega;
    }

    // ---- SETTERS ----

    /**
     * Estableix el valor enter de la resposta.
     * @param entrega El nou valor enter.
     */
    public void setEntrega(Integer entrega) {
        this.entrega = entrega;
    }

    // ---- MÈTODES ----

    /**
     * Retorna la puntuació de la resposta per al clustering.
     * Si la resposta és nul·la, retorna 0.0.
     * @return El valor de l'enter com a double, o 0.0 si no hi ha resposta.
     */
    @Override
    public double TreurePuntuacio() {
        if (this.entrega == null) {
            return 0.0;
        }
        return this.entrega.doubleValue();
    }

    /**
     * Comprova si la resposta està buida (no contestada).
     * @return true si l'entrega és null, false altrament.
     */
    @Override
    public boolean esBuida() {
        return this.entrega == null;
    }

    /**
     * Calcula la distància local normalitzada segons la fórmula per a variables numèriques:
     * D_local(Xi, Xj) = |Xi - Xj| / (Vmax - Vmin)
     * NOTA: Assumeix que els valors ja estan normalitzats en el rang [0,1] durant preparaPerClustering().
     * @param altra L'altra resposta amb la qual calcular la distància.
     * @return La distància normalitzada entre [0,1]. Retorna 1.0 si alguna de les respostes és buida.
     */
    @Override
    public double calcularDistanciaLocal(RespostaPregunta altra) {
        // Si alguna de les respostes està buida, retorna distància màxima
        if (this.esBuida() || altra.esBuida()) {
            return 1.0;
        }
        
        // Comprova que l'altra resposta també sigui de tipus Integer
        if (!(altra instanceof RespostaInteger)) {
            return 1.0; // Tipus incompatibles
        }
        
        RespostaInteger altraInt = (RespostaInteger) altra;
        
        // Càlcul de la distància (assumint normalització prèvia)
        // Si no està normalitzat, seria: |val1 - val2| / (max - min)
        double val1 = this.entrega.doubleValue();
        double val2 = altraInt.entrega.doubleValue();
        
        return Math.abs(val1 - val2);
    }
    
    /**
     * Modifica la resposta amb una nova dada.
     * @param novaDada La nova dada que substituirà l'actual. Ha de ser un Integer.
     * @throws CustomException Si la nova dada no és un Integer vàlid.
     */
    @Override
    public void modificarResposta(Object novaDada) throws CustomException {
        if (novaDada == null) {
            this.entrega = null; // Permet establir la resposta com a no contestada
        } else if (novaDada instanceof Integer) {
            this.entrega = (Integer) novaDada;
        } else {
            throw new CustomException("La nova dada ha de ser un Integer vàlid.");
        }
    }

}
