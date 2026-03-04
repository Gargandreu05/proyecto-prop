package edu.upc.prop.cluster422.Domini.Algorismes;

import edu.upc.prop.cluster422.Domini.Classes.*;
import edu.upc.prop.cluster422.Domini.Controladors.CustomException;

import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementació de l'algorisme de clustering K-medoids.
 * Aquest algorisme selecciona punts reals de les dades com a medoids i assigna
 * altres punts al medoid més proper, minimitzant la distància total dins dels
 * clústers.
 * Aquest enfocament és més robust davant de valors extrems en comparació amb
 * K-means, ja que utilitza punts reals com a centres de clúster.
 * 
 * @author Andreu Puerto
 * @version 17/11/2025/Entrega
 */

public class Kmedoids implements StrategyClustering {
    // Màxim d'iteracions que pot fer l'algorisme abans de rendir-se
    private int maxIteracions = 100;
    // Nombre random per assegurar aleatorietat al executar l'algorisme
    private Random rand = new Random();

    @Override

    /**
     * Executa l'algorisme de K-medoids sobre una enquesta donada.
     * 
     * @param e L'enquesta sobre la qual s'executa l'algorisme.
     * @param k El nombre de clústers a crear.
     * @return Un mapa que associa cada clúster amb els seus membres.
     * @throws CustomException Si hi ha dades insuficients per al càlcul.
     */
    public Map<Integer, Cluster> executa(Enquesta e, int k) throws CustomException {
        // ---- INICI I COMPROVACIONS ----
        ArrayList<ArrayList<RespostaPregunta>> MatriuRespostes = e.getMatriuRespostes();
        ArrayList<Pregunta> Preguntes = e.getPreguntes();
        ArrayList<RespostaEnquesta> Respostes = e.getRespostesEnquestaComAList();

        // Comprova que almenys es pugui posar una resposta per grup
        if (MatriuRespostes.size() < k)
            throw new CustomException("Dades Insuficients per al càlcul.");

        // ---- PREPARACIÓ ----

        int numResp = MatriuRespostes.size();
        int it = 0;
        int[] assignacions = new int[numResp];
        ArrayList<Integer> indexosDisponibles = new ArrayList<>();
        for (int i = 0; i < numResp; i++)
            indexosDisponibles.add(i);
        Collections.shuffle(indexosDisponibles, rand);
        ArrayList<Integer> indexosMedoide = new ArrayList<>();
        for (int i = 0; i < k; i++)
            indexosMedoide.add(indexosDisponibles.get(i));

        Boolean haCanviat = true;

        while (haCanviat && it < maxIteracions) {
            // ---- ASSIGNACIÓ DE CLÚSTERS ----
            for (int i = 0; i < numResp; i++) {
                Double minDist = Double.MAX_VALUE;
                int millorCluster = -1;
                for (int j = 0; j < k; j++) {
                    int index = indexosMedoide.get(j);
                    double distancia = calculaDistanciaGlobal(MatriuRespostes.get(i), MatriuRespostes.get(index),
                            Preguntes);
                    if (minDist > distancia) {
                        minDist = distancia;
                        millorCluster = j;
                    }
                }
                assignacions[i] = millorCluster;
            }

            // ---- ACTUALITZACIÓ DE CLÚSTERS ----
            haCanviat = false;
            for (int i = 0; i < k; i++) {
                ArrayList<Integer> MembresCluster = new ArrayList<>();
                for (int j = 0; j < numResp; j++) {
                    if (assignacions[j] == i)
                        MembresCluster.add(j);
                }
                if (!MembresCluster.isEmpty()) {
                    int millorCandidat = trobarMillorCandidat(MembresCluster, MatriuRespostes, Preguntes);
                    if (indexosMedoide.get(i) != millorCandidat && millorCandidat != -1) {
                        indexosMedoide.set(i, millorCandidat);
                        haCanviat = true;
                    }
                }
            }
            // Afegim iteració
            it++;
        }
        return generaClusters(indexosMedoide, assignacions, Respostes);

    }

    /**
     * Calcula la distància global entre dues respostes tenint en compte totes les
     * preguntes.
     * 
     * @param resposta1 La resposta 1 a la enquesta.
     * @param resposta2 La resposta 2 a la enquesta.
     * @param Preguntes Llista amb les preguntes de l'enquesta.
     * @return La distància global entre les dues respostes (normalitzada entre 0 i
     *         1).
     */
    private double calculaDistanciaGlobal(ArrayList<RespostaPregunta> resposta1, ArrayList<RespostaPregunta> resposta2,
            ArrayList<Pregunta> Preguntes) {
        int N = Preguntes.size();
        if (N == 0)
            return 1.0;

        double distancia = 0.0;

        for (int i = 0; i < N; i++) {
            distancia += resposta1.get(i).calcularDistanciaLocal(resposta2.get(i));
        }
        return distancia / N;
    }

    /**
     * Troba el millor candidat a medoide dins d'un cluster donat.
     * 
     * @param MembresCluster  Índexos dels membres del cluster.
     * @param MatriuRespostes La matriu de respostes.
     * @param Preguntes       Llista amb les preguntes de l'enquesta.
     * @return L'índex del millor candidat a medoide per al cluster.
     */
    private int trobarMillorCandidat(ArrayList<Integer> MembresCluster,
            ArrayList<ArrayList<RespostaPregunta>> MatriuRespostes, ArrayList<Pregunta> Preguntes) {
        int candidat = -1;
        double costMinim = Double.MAX_VALUE;

        // Per cada candidat del cluster
        for (int candidatIndex : MembresCluster) {
            double costTotalCandidat = 0.0;

            // Calculem la distància
            for (int membreIndex : MembresCluster) {
                // No cal calcular la distància d'un punt a si mateix (és 0)
                if (candidatIndex != membreIndex) {
                    costTotalCandidat += calculaDistanciaGlobal(MatriuRespostes.get(candidatIndex),
                            MatriuRespostes.get(membreIndex), Preguntes);
                }
            }

            // Si aquest candidat té menys cost, és el nostre nou millor
            if (costTotalCandidat < costMinim) {
                costMinim = costTotalCandidat;
                candidat = candidatIndex;
            }
        }
        return candidat; // Retorna l'índex del millor medoide
    }

    /**
     * Converteix els resultats de l'algorisme (indexos) enm un mapa de clústers que
     * contenen els usernames dels membres.
     * 
     * @param indexosMedoide Els índexos dels medoids seleccionats.
     * @param assignacions   El vector d'assignacions de clústers.
     * @param ListRespostes  La llista de respostes a l'enquesta.
     * @return Mapa de clústers amb els seus membres preparat per ser retornat.
     */
    private Map<Integer, Cluster> generaClusters(ArrayList<Integer> indexosMedoide, int[] assignacions,
            ArrayList<RespostaEnquesta> ListRespostes) {

        Map<Integer, Cluster> clusterMap = new HashMap<>();
        for (int i = 0; i < indexosMedoide.size(); i++) {
            int indexDelMedoide = indexosMedoide.get(i);
            RespostaEnquesta medoide = ListRespostes.get(indexDelMedoide);
            Integer usernameMedoide = medoide.getUsuari();
            Cluster c = new Cluster(usernameMedoide);
            clusterMap.put(i, c);
        }

        for (int i = 0; i < assignacions.length; i++) {
            int ClusterIndex = assignacions[i];

            if (i != indexosMedoide.get(ClusterIndex)) {
                RespostaEnquesta membre = ListRespostes.get(i);
                Integer usernameMembre = membre.getUsuari();
                clusterMap.get(ClusterIndex).afegirMembre(usernameMembre);
            }
        }

        return clusterMap;
    }
}