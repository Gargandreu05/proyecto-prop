package edu.upc.prop.cluster422.Domini.Classes;

import edu.upc.prop.cluster422.Domini.Controladors.CustomException;

/**
 * Resposta que emmagatzema una cadena de text (String).
 * S'utilitza per preguntes de format lliure.
 * 
 * @author Enzo Miquel
 * @version 17/11/2025/Entrega
 */

public class RespostaString extends RespostaPregunta {

    /**
     * El text de la resposta.
     */
    private String entrega;

    // ---- CONSTRUCTORA ----

    /**
     * Constructora per defecte. Crea una resposta sense valor (no contestada).
     */
    public RespostaString() {
        this.entrega = null;
    }

    /**
     * Constructora amb valor inicial.
     * 
     * @param entrega El text de la resposta.
     * @throws CustomException Si el text conté cometes dobles.
     */
    public RespostaString(String entrega) throws CustomException {
        CustomException.validarSenseCometes(entrega, "La resposta");
        this.entrega = entrega;
    }

    // ---- GETTERS ----

    /**
     * Retorna el text de la resposta.
     * 
     * @return El String emmagatzemat, o null si no s'ha contestat.
     */
    public String getEntrega() {
        return this.entrega;
    }

    // ---- SETTERS ----

    /**
     * Estableix el text de la resposta.
     * 
     * @param entrega El nou text.
     * @throws CustomException Si el text conté cometes dobles.
     */
    public void setEntrega(String entrega) throws CustomException {
        CustomException.validarSenseCometes(entrega, "La resposta");
        this.entrega = entrega;
    }

    // ---- MÈTODES ----

    /**
     * Retorna la puntuació de la resposta per al clustering.
     * Per a respostes de tipus String, es retorna el hash code normalitzat.
     * Si la resposta és nul·la o buida, retorna 0.0.
     * 
     * @return Un valor double basat en el hash de la cadena, o 0.0 si no hi ha
     *         resposta.
     */
    @Override
    public double TreurePuntuacio() {
        if (this.entrega == null || this.entrega.trim().isEmpty()) {
            return 0.0;
        }
        // Utilitzem el hashCode per convertir el String en un valor numèric
        return Math.abs(this.entrega.hashCode() % 1000);
    }

    /**
     * Comprova si la resposta està buida (no contestada).
     * 
     * @return true si l'entrega és null o buida, false altrament.
     */
    @Override
    public boolean esBuida() {
        return this.entrega == null || this.entrega.trim().isEmpty();
    }

    /**
     * Calcula la distància local normalitzada segons la distància de Levenshtein
     * per a strings:
     * D_local(Xi, Xj) = lev(Xi, Xj) / max(length(Xi), length(Xj))
     * Aquesta fórmula normalitza la distància d'edició pel màxim de les longituds.
     * 
     * @param altra L'altra resposta amb la qual calcular la distància.
     * @return La distància normalitzada entre [0,1]. Retorna 1.0 si alguna de les
     *         respostes és buida.
     */
    @Override
    public double calcularDistanciaLocal(RespostaPregunta altra) {
        // Si alguna de les respostes està buida, retorna distància màxima
        if (this.esBuida() || altra.esBuida()) {
            return 1.0;
        }

        // Comprova que l'altra resposta també sigui de tipus String
        if (!(altra instanceof RespostaString)) {
            return 1.0; // Tipus incompatibles
        }

        RespostaString altraStr = (RespostaString) altra;

        String s1 = this.entrega;
        String s2 = altraStr.entrega;

        // Cas especial: si ambdues són iguals, distància 0
        if (s1.equals(s2)) {
            return 0.0;
        }

        // Calcula la distància de Levenshtein
        int distLev = calcularLevenshtein(s1, s2);
        int maxLen = Math.max(s1.length(), s2.length());

        // Evita divisió per zero (encara que no hauria de passar si no són buides)
        if (maxLen == 0) {
            return 0.0;
        }

        // Normalitza: fórmula del document adaptada
        double distNorm = (double) distLev / maxLen;

        // Assegura que estigui en el rang [0,1]
        return Math.min(1.0, distNorm);
    }

    /**
     * Calcula la distància de Levenshtein entre dos strings.
     * Aquesta és la distància d'edició mínima (insertions, eliminacions,
     * substitucions).
     * 
     * @param a Primer string.
     * @param b Segon string.
     * @return El nombre mínim d'operacions d'edició.
     */
    private int calcularLevenshtein(String a, String b) {
        int m = a.length();
        int n = b.length();

        // Matriu de programació dinàmica
        int[][] dp = new int[m + 1][n + 1];

        // Inicialització: casos base
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i; // Eliminar tots els caràcters d'a
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j; // Inserir tots els caràcters de b
        }

        // Omplir la matriu
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;

                dp[i][j] = Math.min(
                        Math.min(
                                dp[i - 1][j] + 1, // Eliminació
                                dp[i][j - 1] + 1), // Inserció
                        dp[i - 1][j - 1] + cost // Substitució
                );
            }
        }

        return dp[m][n];
    }

    /**
     * Modifica la resposta amb una nova dada.
     * 
     * @param novaDada La nova dada que substituirà l'actual.
     */
    @Override
    public void modificarResposta(Object novaDada) throws CustomException {
        if (novaDada == null) {
            throw new CustomException("La nova dada no pot ser null per a RespostaString.");
        }
        if (novaDada instanceof String) {
            String text = (String) novaDada;
            CustomException.validarSenseCometes(text, "La resposta");
            this.entrega = text;
        } else {
            throw new CustomException("Tipus de dada invàlid per a RespostaString.");
        }
    }

}
