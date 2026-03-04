package edu.upc.prop.cluster422.Domini.Classes;

/**
 * Aquesta clase representa un Administrador, que és un tipus d'Usuari amb atributs addicionals.
 * @author: Ramon Sanchez
 * @version: 14/12/2025/1
 */

public class Administrador extends Usuari {

    // Atributs
    private final String acreditador;

    // Constructora
    /**
     * Constructora de la clase Administrador
     * @param nomusuari El paràmetre nomusuari defineix el nom d'usuari de l'administrador
     * @param contrassenya El paràmetre contrassenya defineix la contrassenya de l'administrador
     * @param acreditador El paràmetre acreditador és el nom d'usuari de l'administrador que ha fet la creació d'aquest nou administrador
     * @throws IllegalArgumentException Si l'acreditador conté caràcters invàlids
     */
    public Administrador(String nomusuari, String contrassenya, String acreditador, Integer id) {
        super(nomusuari, contrassenya, id);
        if(acreditador.contains("\"")) throw new IllegalArgumentException("L'acreditador no pot contenir els seguents caracters: \".");
        this.acreditador = acreditador;
    }

    // Getters
    /**
     * Getter de l'acreditador
     * @return Retorna el nom d'usuari de l'administrador que va crear aquest administrador
     */
    public String getAcreditador() {
        return acreditador;
    }

    /**
     * Getter de totes les dades privades de l'administrador
     * @return Retorna un array de Strings amb totes les dades privades de l'administrador
     */
    @Override
    public String[] getDadesPrivades(){
        String edatString;
        int edat = getEdat();
        if (edat == -1) edatString = "No especificada";
        else edatString = Integer.toString(edat);
        String emailString = getEmail();
        if (emailString.isEmpty()) emailString = "No especificat";
        return new String[]{getUsername(), getContrasenya(), emailString, edatString, this.acreditador};
    }

}