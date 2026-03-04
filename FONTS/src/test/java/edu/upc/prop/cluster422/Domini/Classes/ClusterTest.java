package edu.upc.prop.cluster422.Domini.Classes;

import edu.upc.prop.cluster422.Domini.Controladors.CustomException;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

/**
 * Tests unitaris per a la classe Cluster.
 */
public class ClusterTest {

    /**
     * Cas constructor: Constructor de K-Medoids (amb representant)
     */
    @Test
    public void testConstructorKMedoids() throws CustomException {
        String representantUsername = "usuariMedoide";
        Cluster cluster = new Cluster(representantUsername);

        assertTrue("El cluster hauria de tenir representant", cluster.teRepresentant());
        assertEquals("El representant és incorrecte", representantUsername, cluster.getRepresentantReal());
        assertTrue("La llista de membres hauria d'estar buida", cluster.getMembres().isEmpty());
    }

    /**
     * Cas constructor: Constructor de K-Means (sense representant)
     * Construeix un cluster amb una llista de membres i comprova els atributs.
     */
    @Test
    public void testConstructorKMeans() {
        List<String> membres = Arrays.asList("membre1", "membre2");
        Cluster cluster = new Cluster(membres); // Constructor de K-Means

        assertFalse("El cluster no hauria de tenir representant", cluster.teRepresentant());
        assertEquals("La llista de membres no coincideix", 2, cluster.getMembres().size());
        assertTrue("La llista de membres no conté 'membre1'", cluster.getMembres().contains("membre1"));
    }

    /**
     * Cas convencional: Afegir membres i centroide
     * Construeix un cluster K-Medoids, afegeix membres i un centroide, i comprova
     * els atributs.
     */
    @Test
    public void testAfegirMembresICentroide() {
        Cluster cluster = new Cluster("representant"); // K-Medoids
        cluster.afegirMembre("membreA");
        cluster.afegirMembre("membreB");

        List<Object> centroide = Arrays.asList(1, "moda");
        cluster.afegirCentroide(centroide);

        assertEquals("El nombre de membres és incorrecte", 2, cluster.getMembres().size());
        assertTrue("Falta 'membreB'", cluster.getMembres().contains("membreB"));
        assertEquals("El centroide és incorrecte", centroide, cluster.getValorsCentroide());
    }

    /**
     * Cas extrem: Intentar obtenir representant real quan no n'hi ha
     * Hauria de llençar una excepció.
     */
    @Test(expected = CustomException.class)
    public void testGetRepresentantRealQuanNoNHiHa() throws CustomException {
        Cluster cluster = new Cluster(Arrays.asList("m1", "m2")); // K-Means
        try {
            cluster.getRepresentantReal();
        } catch (CustomException e) {
            assertEquals("El cluster no té representant", e.getMessage());
            throw e;
        }
    }

    /**
     * Cas convencional: Eliminar un membre existent
     * Comprova que el membre s'ha eliminat correctament.
     */
    @Test
    public void testRemoveMembreExit() throws CustomException {
        Cluster cluster = new Cluster("representant");
        cluster.afegirMembre("membreA");
        cluster.afegirMembre("membreB");

        cluster.removeMembre("membreA");

        assertEquals(1, cluster.getMembres().size());
        assertFalse(cluster.getMembres().contains("membreA"));
    }

    /**
     * Cas extrem: Eliminar un membre inexistent
     * Hauria de llençar una excepció.
     */
    @Test(expected = CustomException.class)
    public void testRemoveMembreInexistent() throws CustomException {
        Cluster cluster = new Cluster("representant");
        try {
            cluster.removeMembre("membrez");
        } catch (CustomException e) {
            assertEquals("El membre no existeix al cluster", e.getMessage());
            throw e;
        }
    }
}