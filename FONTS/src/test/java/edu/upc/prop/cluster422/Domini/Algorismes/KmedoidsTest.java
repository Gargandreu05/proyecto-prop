package edu.upc.prop.cluster422.Domini.Algorismes;

import edu.upc.prop.cluster422.Domini.Classes.*;
import edu.upc.prop.cluster422.Domini.Controladors.CustomException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test unitari per a l'algorisme Kmedoids.
 * S'utilitzen Mocks per crear un escenari de dades controlat
 * i simular les distàncies entre punts.
 */
public class KmedoidsTest {

    private Kmedoids kmedoids;

    // --- Mocks de l'entorn ---
    @Mock private Enquesta mockEnquesta;
    @Mock private Pregunta mockPregunta; // Només una pregunta per simplificar

    // Mocks per als 4 usuaris
    @Mock private Usuari mockU1, mockU2, mockU3, mockU4;
    @Mock private RespostaEnquesta mockRE1, mockRE2, mockRE3, mockRE4;
    @Mock private RespostaPregunta mockRP1, mockRP2, mockRP3, mockRP4;

    @Before
    public void setUp() throws CustomException {
        MockitoAnnotations.openMocks(this);
        kmedoids = new Kmedoids();

        // --- 1. Definir els usuaris ---
        when(mockU1.getUsername()).thenReturn("u1_aProp");
        when(mockU2.getUsername()).thenReturn("u2_aProp");
        when(mockU3.getUsername()).thenReturn("u3_lluny");
        when(mockU4.getUsername()).thenReturn("u4_lluny");

        // --- 2. Definir les RespostesEnquesta (llista de respostes) ---
        when(mockRE1.getUsuari()).thenReturn(mockU1);
        when(mockRE2.getUsuari()).thenReturn(mockU2);
        when(mockRE3.getUsuari()).thenReturn(mockU3);
        when(mockRE4.getUsuari()).thenReturn(mockU4);
        
        ArrayList<RespostaEnquesta> listRespostes = new ArrayList<>(
            Arrays.asList(mockRE1, mockRE2, mockRE3, mockRE4)
        );

        // --- 3. Definir la MatriuRespostes (matriu de RespostaPregunta) ---
        ArrayList<ArrayList<RespostaPregunta>> matriuRespostes = new ArrayList<>();
        matriuRespostes.add(new ArrayList<>(Arrays.asList(mockRP1)));
        matriuRespostes.add(new ArrayList<>(Arrays.asList(mockRP2)));
        matriuRespostes.add(new ArrayList<>(Arrays.asList(mockRP3)));
        matriuRespostes.add(new ArrayList<>(Arrays.asList(mockRP4)));
        
        // --- 4. Definir les Preguntes ---
        ArrayList<Pregunta> preguntes = new ArrayList<>(Arrays.asList(mockPregunta));

        // --- 5. Configurar l'Enquesta Mock ---
        when(mockEnquesta.getMatriuRespostes()).thenReturn(matriuRespostes);
        when(mockEnquesta.getRespostesEnquestaComAList()).thenReturn(listRespostes);
        when(mockEnquesta.getPreguntes()).thenReturn(preguntes);
        
        // --- 6. Simular les distàncies (LA PART CLAU) ---
        // Grup 1 (a prop)
        when(mockRP1.calcularDistanciaLocal(mockRP2)).thenReturn(0.1);
        when(mockRP2.calcularDistanciaLocal(mockRP1)).thenReturn(0.1);

        // Grup 2 (a prop)
        when(mockRP3.calcularDistanciaLocal(mockRP4)).thenReturn(0.1);
        when(mockRP4.calcularDistanciaLocal(mockRP3)).thenReturn(0.1);

        // Distàncies entre grups (lluny)
        when(mockRP1.calcularDistanciaLocal(mockRP3)).thenReturn(1.0);
        when(mockRP3.calcularDistanciaLocal(mockRP1)).thenReturn(1.0);
        when(mockRP1.calcularDistanciaLocal(mockRP4)).thenReturn(1.0);
        when(mockRP4.calcularDistanciaLocal(mockRP1)).thenReturn(1.0);
        when(mockRP2.calcularDistanciaLocal(mockRP3)).thenReturn(1.0);
        when(mockRP3.calcularDistanciaLocal(mockRP2)).thenReturn(1.0);
        when(mockRP2.calcularDistanciaLocal(mockRP4)).thenReturn(1.0);
        when(mockRP4.calcularDistanciaLocal(mockRP2)).thenReturn(1.0);

        // Distàncies a si mateix (0)
        when(mockRP1.calcularDistanciaLocal(mockRP1)).thenReturn(0.0);
        when(mockRP2.calcularDistanciaLocal(mockRP2)).thenReturn(0.0);
        when(mockRP3.calcularDistanciaLocal(mockRP3)).thenReturn(0.0);
        when(mockRP4.calcularDistanciaLocal(mockRP4)).thenReturn(0.0);
    }

    // Cas convencional: Algorisme amb dades controlades
    @Test
    public void testExecutaAlgorismeControlat() throws CustomException {
        // Executem l'algorisme amb k=2
        Map<Integer, Cluster> resultats = kmedoids.executa(mockEnquesta, 2);

        // --- Comprovació dels resultats ---
        assertEquals("El nombre de clusters ha de ser 2", 2, resultats.size());

        // Com que la inicialització és aleatòria, no sabem quin cluster serà el 0 i quin serà l'1.
        // Hem de comprovar els dos grups independentment de la seva clau.
        
        Cluster c0 = resultats.get(0);
        Cluster c1 = resultats.get(1);

        // Creem conjunts (Set) per comprovar els membres totals (representant + membres)
        HashSet<String> membresTotalsC0 = new HashSet<>(c0.getMembres());
        membresTotalsC0.add(c0.getRepresentantReal());

        HashSet<String> membresTotalsC1 = new HashSet<>(c1.getMembres());
        membresTotalsC1.add(c1.getRepresentantReal());

        // Els dos grups que esperem trobar
        HashSet<String> grupEsperatA = new HashSet<>(Arrays.asList("u1_aProp", "u2_aProp"));
        HashSet<String> grupEsperatB = new HashSet<>(Arrays.asList("u3_lluny", "u4_lluny"));

        // Comprovem que els resultats siguin (GrupA, GrupB) o (GrupB, GrupA)
        boolean cas1 = membresTotalsC0.equals(grupEsperatA) && membresTotalsC1.equals(grupEsperatB);
        boolean cas2 = membresTotalsC0.equals(grupEsperatB) && membresTotalsC1.equals(grupEsperatA);

        assertTrue("Els clusters trobats no coincideixen amb els grups esperats", cas1 || cas2);
    }

    // Cas extrem: Dades insuficients
    @Test(expected = CustomException.class)
    public void testExecutaDadesInsuficients() throws CustomException {
        // Creem una matriu de respostes amb només 1 element
        ArrayList<ArrayList<RespostaPregunta>> matriuPetita = new ArrayList<>();
        matriuPetita.add(new ArrayList<>(Arrays.asList(mockRP1)));
        
        when(mockEnquesta.getMatriuRespostes()).thenReturn(matriuPetita);

        // Intentem executar amb k=2 (fallarà el check MatriuRespostes.size() < k)
        kmedoids.executa(mockEnquesta, 2); 
    }
}