package edu.upc.prop.cluster422.Domini.Algorismes;

import edu.upc.prop.cluster422.Domini.Classes.*;
import edu.upc.prop.cluster422.Domini.Controladors.CustomException;

// Imports de les classes de Domini (Reals)
import edu.upc.prop.cluster422.Domini.Classes.PreguntaFormatLliure;
import edu.upc.prop.cluster422.Domini.Classes.PreguntaFormatNumeric;
import edu.upc.prop.cluster422.Domini.Classes.RespostaInteger;
import edu.upc.prop.cluster422.Domini.Classes.RespostaString;

// Imports de JUnit 4
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

// Imports de Mockito
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.util.*;

/**
 * Test unitari (amb integració de dades) per a la classe KmeansPlusPlus.
 * Aquest test comprova que l'algorisme K-Means++ convergeix al resultat esperat
 * en un escenari de dades controlat.
 * * L'estructura és idèntica a KmeansTest, ja que el resultat final
 * esperat (clústers i centroides) és el mateix.
 */
public class KmeansPlusPlusTest {

    // L'objecte que estem provant
    private KmeansPlusPlus kmeansPlusPlus;

    // --- Mocks per a l'Entorn ---
    @Mock
    private Enquesta mockEnquesta;
    @Mock
    private Usuari mockU1, mockU2, mockU3, mockU4;
    @Mock
    private RespostaEnquesta mockRE1, mockRE2, mockRE3, mockRE4;

    // --- Objectes Reals per a les Dades ---
    private ArrayList<Pregunta> preguntes;
    private ArrayList<ArrayList<RespostaPregunta>> matriuRespostes;
    private ArrayList<RespostaEnquesta> listRespostes;

    /**
     * S'executa abans de cada test.
     * Prepara un escenari amb 4 usuaris dividits en 2 grups clars.
     * Grup A (u1, u2): [20, "CiutatA"]
     * Grup B (u3, u4): [80, "CiutatB"]
     */
    @Before
    public void setUp() throws CustomException {
        MockitoAnnotations.openMocks(this);
        kmeansPlusPlus = new KmeansPlusPlus(); // Classe que estem provant

        // 1. Definir Preguntes (Reals)
        preguntes = new ArrayList<>();
        preguntes.add(new PreguntaFormatNumeric("Edat"));
        preguntes.add(new PreguntaFormatLliure("Ciutat"));

        // 2. Definir Respostes (Reals)
        // Grup A
        RespostaPregunta rp1_num = new RespostaInteger(20);
        RespostaPregunta rp1_str = new RespostaString("CiutatA");
        RespostaPregunta rp2_num = new RespostaInteger(20);
        RespostaPregunta rp2_str = new RespostaString("CiutatA");
        // Grup B
        RespostaPregunta rp3_num = new RespostaInteger(80);
        RespostaPregunta rp3_str = new RespostaString("CiutatB");
        RespostaPregunta rp4_num = new RespostaInteger(80);
        RespostaPregunta rp4_str = new RespostaString("CiutatB");

        // 3. Crear Matriu de Respostes (Reals)
        matriuRespostes = new ArrayList<>();
        matriuRespostes.add(new ArrayList<>(Arrays.asList(rp1_num, rp1_str))); // Fila 0: u1
        matriuRespostes.add(new ArrayList<>(Arrays.asList(rp2_num, rp2_str))); // Fila 1: u2
        matriuRespostes.add(new ArrayList<>(Arrays.asList(rp3_num, rp3_str))); // Fila 2: u3
        matriuRespostes.add(new ArrayList<>(Arrays.asList(rp4_num, rp4_str))); // Fila 3: u4

        // 4. Configurar Mocks d'Usuari
        when(mockU1.getUsername()).thenReturn("u1_grupA");
        when(mockU2.getUsername()).thenReturn("u2_grupA");
        when(mockU3.getUsername()).thenReturn("u3_grupB");
        when(mockU4.getUsername()).thenReturn("u4_grupB");

        // 5. Configurar Mocks de RespostaEnquesta
        when(mockRE1.getUsuari()).thenReturn(mockU1);
        when(mockRE2.getUsuari()).thenReturn(mockU2);
        when(mockRE3.getUsuari()).thenReturn(mockU3);
        when(mockRE4.getUsuari()).thenReturn(mockU4);
        listRespostes = new ArrayList<>(Arrays.asList(mockRE1, mockRE2, mockRE3, mockRE4));

        // 6. Configurar Mock d'Enquesta
        when(mockEnquesta.getMatriuRespostes()).thenReturn(matriuRespostes);
        when(mockEnquesta.getPreguntes()).thenReturn(preguntes);
        when(mockEnquesta.getRespostesEnquestaComAList()).thenReturn(listRespostes);
    }

    /**
     * Test principal: execució de l'algorisme.
     * Comprova que, donat l'escenari, l'algorisme troba els 2 clústers correctes.
     */
    @Test
    public void testExecutaAlgorisme() throws CustomException {
        // Executem l'algorisme amb k=2
        Map<Integer, Cluster> resultats = kmeansPlusPlus.executa(mockEnquesta, 2);

        // --- Verificació dels Resultats ---
        
        // 1. S'han de trobar 2 clústers
        assertEquals("El nombre de clústers trobats ha de ser 2", 2, resultats.size());

        // Com que la inicialització té un component aleatori,
        // no sabem quin clúster serà el 0 i quin l'1.
        // Hem de comprovar els *continguts* dels clústers.
        
        Cluster c0 = resultats.get(0);
        Cluster c1 = resultats.get(1);
        
        // Creem Sets (conjunts) amb els membres de cada clúster
        Set<String> membresC0 = new HashSet<>(c0.getMembres());
        Set<String> membresC1 = new HashSet<>(c1.getMembres());

        // Els dos grups que esperem trobar
        Set<String> grupEsperatA = Set.of("u1_grupA", "u2_grupA");
        Set<String> grupEsperatB = Set.of("u3_grupB", "u4_grupB");

        // Comprovem que els resultats siguin (GrupA, GrupB) o (GrupB, GrupA)
        boolean cas1 = membresC0.equals(grupEsperatA) && membresC1.equals(grupEsperatB);
        boolean cas2 = membresC0.equals(grupEsperatB) && membresC1.equals(grupEsperatA);

        assertTrue("Els clústers trobats no contenen els membres esperats.", cas1 || cas2);
        
        // Verifiquem els centroides virtuals
        List<Object> centroideA = Arrays.asList(20.0, "CiutatA"); // Mitjana(20,20), Moda("A","A")
        List<Object> centroideB = Arrays.asList(80.0, "CiutatB"); // Mitjana(80,80), Moda("B","B")

        if (cas1) {
            assertEquals("El centroide del clúster 0 és incorrecte", centroideA, c0.getValorsCentroide());
            assertEquals("El centroide del clúster 1 és incorrecte", centroideB, c1.getValorsCentroide());
        } else { // cas2
            assertEquals("El centroide del clúster 0 és incorrecte", centroideB, c0.getValorsCentroide());
            assertEquals("El centroide del clúster 1 és incorrecte", centroideA, c1.getValorsCentroide());
        }
    }

    /**
     * Test d'excepció: dades insuficients.
     * Comprova la guarda 'if (MatriuRespostes.size() < k)'
     */
    @Test(expected = CustomException.class)
    public void testExecutaDadesInsuficients() throws CustomException {
        // L'enquesta té 4 respostes. Si demanem k=5, ha de fallar.
        kmeansPlusPlus.executa(mockEnquesta, 5);
    }
}