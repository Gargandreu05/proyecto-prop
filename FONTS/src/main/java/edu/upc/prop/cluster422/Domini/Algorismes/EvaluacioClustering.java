package edu.upc.prop.cluster422.Domini.Algorismes;

import edu.upc.prop.cluster422.Domini.Classes.*;
import edu.upc.prop.cluster422.Domini.Controladors.CustomException;

import java.util.Map;
import java.util.HashMap;
import java.util.List;



/**
 * Classe per avaluar la qualitat del clustering mitjançant el Coeficient de Silhouette.
 * 
 * @author Enzo Miquel
 * @version 17/11/2025/Entrega
 */
public class EvaluacioClustering {

    /**
     * Calcula el Coeficient de Silhouette per avaluar la qualitat d'un clustering.
     * El valor està entre -1 i +1:
     * - Valors propers a +1: Els punts estan ben assignats als seus clusters
     * - Valors propers a 0: Els punts estan a la frontera entre clusters
     * - Valors propers a -1: Els punts estan mal assignats
     * @param clusters Map amb els clusters resultants del clustering
     * @param enquesta L'enquesta sobre la qual s'ha fet el clustering
     * @return El coeficient de Silhouette (entre -1 i +1)
     */
    public static double calcularSilhouette(Map<Integer, Cluster> clusters, Enquesta enquesta) {
        if (clusters == null || clusters.size() <= 1) {
            return 0.0;
        }

        Map<Integer, RespostaEnquesta> mapaRespostes = enquesta.getRespostesEnquesta();
        List<Pregunta> preguntes = enquesta.getPreguntes();
        
        Map<Integer, Integer> assignacions = new HashMap<>();
        for (Map.Entry<Integer, Cluster> entry : clusters.entrySet()) {
            int clusterID = entry.getKey();
            Cluster cluster = entry.getValue();
            for (Integer nomMembre : cluster.getMembres()) {
                assignacions.put(nomMembre, clusterID);
            }
            if (cluster.teRepresentant()) {
                try {
                    Integer Representant = cluster.getRepresentantReal();
                    assignacions.put(Representant, clusterID);
                } catch (CustomException e) {
                    // Ignorar si no es pot obtenir el representant
                }
            }
        }

        double sumaSilhouette = 0.0;
        int totalPunts = 0;

        for (Map.Entry<Integer, RespostaEnquesta> entry : mapaRespostes.entrySet()) {
            Integer nomUsuari = entry.getKey();
            RespostaEnquesta respostaActual = entry.getValue();
            
            Integer clusterID = assignacions.get(nomUsuari);
            if (clusterID == null) continue;
            
            Cluster clusterActual = clusters.get(clusterID);
            List<Integer> membresCluster = clusterActual.getMembres();
            
            if (membresCluster.size() == 1) {
                continue;
            }

            double a_i = calcularCohesio(respostaActual, membresCluster, nomUsuari, mapaRespostes, preguntes);
            double b_i = calcularSeparacio(respostaActual, clusterID, clusters, mapaRespostes, preguntes);
            
            double max_ab = Math.max(a_i, b_i);
            double s_i = (max_ab > 0) ? (b_i - a_i) / max_ab : 0.0;
            
            sumaSilhouette += s_i;
            totalPunts++;
        }

        return (totalPunts > 0) ? sumaSilhouette / totalPunts : 0.0;
    }

    /**
     * Calcula la cohesió (a(i)) d'una resposta dins del seu cluster.
     * Aquest valor és la distància mitjana entre la resposta i totes les altres respostes del mateix cluster.
     * @param respostaActual La resposta per a la qual es calcula la cohesió.
     * @param membresCluster La llista de noms d'usuari dels membres del cluster.
     * @param UsuariActual l'id d'usuari de la resposta actual.
     * @param mapaRespostes Mapa de totes les respostes de l'enquesta.
     * @param preguntes La llista de preguntes de l'enquesta.
     * @return La cohesió a(i) de la resposta actual.
     */
    private static double calcularCohesio(RespostaEnquesta respostaActual, List<Integer> membresCluster, Integer UsuariActual, Map<Integer, RespostaEnquesta> mapaRespostes, List<Pregunta> preguntes) {
        
        double sumaDistancies = 0.0;
        int count = 0;

        for (Integer nomMembre : membresCluster) {
            if (nomMembre.equals(UsuariActual)) continue;
            
            RespostaEnquesta altraResposta = mapaRespostes.get(nomMembre);
            if (altraResposta != null) {
                double distancia = calcularDistanciaEntreRespostes(respostaActual, altraResposta, preguntes);
                sumaDistancies += distancia;
                count++;
            }
        }

        return (count > 0) ? sumaDistancies / count : 0.0;
    }

    /**
     * Calcula la separació (b(i)) d'una resposta respecte als altres clusters.
     * Aquest valor és la distància mitjana mínima entre la resposta i totes les respost
     * @param respostaActual La resposta per a la qual es calcula la separació.
     * @param clusterIDActual El ID del cluster al qual pertany la resposta actual.
     * @param clusters Els clusters resultants del clustering.
     * @param mapaRespostes Mapa de totes les respostes de l'enquesta.
     * @param preguntes La llista de preguntes de l'enquesta.
     * @return La separació b(i) de la resposta actual.
     */
    private static double calcularSeparacio(RespostaEnquesta respostaActual, int clusterIDActual, Map<Integer, Cluster> clusters, Map<Integer, RespostaEnquesta> mapaRespostes, List<Pregunta> preguntes) {
        
        double minDistanciaMitjana = Double.MAX_VALUE;

        for (Map.Entry<Integer, Cluster> entry : clusters.entrySet()) {
            int altreClusterID = entry.getKey();
            
            if (altreClusterID == clusterIDActual) continue;
            
            Cluster altreCluster = entry.getValue();
            List<Integer> membresAltreCluster = altreCluster.getMembres();
            
            double sumaDistancies = 0.0;
            int count = 0;

            for (Integer nomMembre : membresAltreCluster) {
                RespostaEnquesta altraResposta = mapaRespostes.get(nomMembre);
                if (altraResposta != null) {
                    double distancia = calcularDistanciaEntreRespostes(respostaActual, altraResposta, preguntes);
                    sumaDistancies += distancia;
                    count++;
                }
            }
            
            double distanciaMitjana = (count > 0) ? sumaDistancies / count : Double.MAX_VALUE;
            minDistanciaMitjana = Math.min(minDistanciaMitjana, distanciaMitjana);
        }

        return minDistanciaMitjana;
    }

    /**
     * Calcula la distància entre dues respostes tenint en compte les preguntes de l'enquesta.
     * @param r1 RespostaEnquesta r1
     * @param r2 RespostaEnquesta r2
     * @param preguntes Llista de preguntes de l'enquesta
     * @return La distància entre les dues respostes (normalitzada entre 0 i 1)
     */
    private static double calcularDistanciaEntreRespostes(RespostaEnquesta r1, RespostaEnquesta r2, List<Pregunta> preguntes) {
        
        List<RespostaPregunta> respostes1 = r1.getRespostaAPregunta();
        List<RespostaPregunta> respostes2 = r2.getRespostaAPregunta();
        
        double distanciaTotal = 0.0;
        int count = 0;

        for (int i = 0; i < preguntes.size() && i < respostes1.size() && i < respostes2.size(); i++) {
            RespostaPregunta rp1 = respostes1.get(i);
            RespostaPregunta rp2 = respostes2.get(i);
            
            if (rp1.esBuida() || rp2.esBuida()) {
                distanciaTotal += 1.0;
            } else {
                try {
                    double distancia = rp1.calcularDistanciaLocal(rp2);
                    distanciaTotal += distancia;
                } catch (Exception e) {
                    distanciaTotal += 1.0;
                }
            }
            count++;
        }

        return (count > 0) ? distanciaTotal / count : 1.0;
    }

    /**
     * Retorna una interpretació textual del coeficient de Silhouette.
     * 
     * @param silhouette El valor del coeficient de Silhouette
     * @return String amb la interpretació de la qualitat
     */
    public static String interpretarSilhouette(double silhouette) {
        if (silhouette > 0.7) {
            return "Excel·lent";
        } else if (silhouette > 0.5) {
            return "Bona";
        } else if (silhouette > 0.25) {
            return "Acceptable";
        } else if (silhouette > 0) {
            return "Feble";
        } else {
            return "Dolenta";
        }
    }
}
