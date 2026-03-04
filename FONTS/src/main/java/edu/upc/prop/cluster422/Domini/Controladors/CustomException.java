package edu.upc.prop.cluster422.Domini.Controladors;

public class CustomException extends Exception {

    public CustomException(String message) {
        super(message);
    }

    // Constructor per defecte
    public CustomException() {
        super("Algun crac no ha fet be la seva feina.");
    }

    /**
     * Valida que un text no contingui cometes dobles (").
     * Es fa servir per evitar problemes amb la serialització JSON.
     * 
     * @param text    El text a validar.
     * @param nomCamp El nom del camp per al missatge d'error.
     * @throws CustomException Si el text conté cometes dobles.
     */
    public static void validarSenseCometes(String text, String nomCamp) throws CustomException {
        if (text != null && text.contains("\"")) {
            throw new CustomException(nomCamp + " no pot contenir cometes dobles (\").");
        }
    }
}