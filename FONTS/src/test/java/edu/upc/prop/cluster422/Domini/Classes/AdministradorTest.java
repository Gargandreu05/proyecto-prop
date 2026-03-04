package edu.upc.prop.cluster422.Domini.Classes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests unitaris per a la classe Administrador.
 * Comprova la funcionalitat afegida a la classe Usuari.
 */
public class AdministradorTest {

    /**
     * Cas constructor: Crear un Administrador i verificar els atributs.
     */
    @Test
    public void testConstructorAdministrador() {
        Administrador admin = new Administrador("adminUser", "password", "rootAdmin", 3);
        
        assertEquals("adminUser", admin.getUsername());
        assertEquals("password", admin.getContrasenya());
        assertEquals("rootAdmin", admin.getAcreditador());
    }

    /**
     * Cas obtenir dades privades de l'Administrador: comprovar que es retornen correctament.
     */
    @Test
    public void testGetDadesPrivadesAdmin() {
        Administrador admin = new Administrador("adminUser", "password", "rootAdmin", 3);
        admin.setEdat(20);
        admin.setEmail("admin@cs.upc.edu");

        String[] dades = admin.getDadesPrivades();
        
        assertEquals(5, dades.length); // Usuari, Pass, Email, Edat, Acreditador
        assertEquals("adminUser", dades[0]);
        assertEquals("password", dades[1]);
        assertEquals("admin@cs.upc.edu", dades[2]);
        assertEquals("20", dades[3]);
        assertEquals("rootAdmin", dades[4]);
    }
}