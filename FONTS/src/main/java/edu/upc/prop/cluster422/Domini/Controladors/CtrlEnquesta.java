package edu.upc.prop.cluster422.Domini.Controladors;

import edu.upc.prop.cluster422.Dades.CtrlPersistencia;
import edu.upc.prop.cluster422.Domini.Algorismes.*;
import edu.upc.prop.cluster422.Domini.Classes.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.*;

/**
 * Aquesta classe és el controlador que s'encarrega de tots els tràmits
 * relacionats amb enquestes
 * relacionats amb les enquestes.
 * 
 * @author Andreu Puerto, Roger Guinovart, Ramon Sánchez
 * @version 17/11/2025/Entrega
 */

public class CtrlEnquesta {

    /**
     * Control Enquesta té una única instància.
     */
    private static CtrlEnquesta instance;

    /**
     * Control Persistencia té una única instància.
     */
    private final CtrlPersistencia ctrlPersistencia;

    /**
     * Mapa amb totes les enquestes existents.
     */
    Enquesta enquestaTractada;

    /**
     * Mapa amb totes les enquestes existents.
     */
    private final Map<String, Integer> titols;

    /**
     * Estratègia de clustering utilitzada actualment.
     */
    StrategyClustering strategyCluster;

    /**
     * Resultat de l'últim clustering realitzat.
     */
    private Map<Integer, Cluster> resultatClustering;

    /**
     * Coeficient de Silhouette de l'últim clustering realitzat.
     */
    private double ultimSilhouette;

    // CONSTRUCTORA
    /**
     * Constructora del controlador d'enquestes, privada per a complir amb el patró
     * Singleton.
     * S'obté informació de quin usuari està actiu actualment i també inicialitza
     * les estructures de dades com a buides.
     * 
     * @throws IOException Si hi ha un error en accedir a la capa de persistència
     */
    private CtrlEnquesta() throws IOException {
        ctrlPersistencia = CtrlPersistencia.getInstance();
        enquestaTractada = null;
        Map<String, Integer> loaded = ctrlPersistencia.getLlistatEnquestes();
        if (loaded != null)
            this.titols = new HashMap<>(loaded);
        else
            this.titols = new HashMap<String, Integer>();
    }

    /**
     * Getter de la instància del controlador d'enquestes. És única.
     * 
     * @return La instància de CtrlEnquesta
     * @throws IOException Si hi ha un error en inicialitzar la persistència
     */
    public static CtrlEnquesta getInstance() throws IOException {
        if (instance == null) {
            instance = new CtrlEnquesta();
        }
        return instance;
    }

    /**
     * Crea una enquesta buida
     * 
     * @param u      Usuari creador de l'enquesta
     * @param titolE Títol de l'enquesta
     * @throws CustomException Si ja existeix una enquesta amb aquest títol
     * @throws IOException     Si hi ha un error en accedir a la persistència
     */
    public void crearEnquesta(Usuari u, String titolE) throws CustomException, IOException {
        if (titols.containsKey(titolE)) {
            throw new CustomException("Ja existeix una enquesta amb aquest títol.");
        }
        Enquesta e = new Enquesta(titolE, u.getId(), 0);
        int id = ctrlPersistencia.crearEnquesta(e);
        titols.put(titolE, id);
        u.addEnquestaCreada(id);
    }

    /**
     * Modifica el títol d'una enquesta existent
     * 
     * @param titolAntic Títol actual de l'enquesta
     * @param titolNou   Nou títol que es vol assignar a l'enquesta
     * @throws CustomException Llança una excepció si ja existeix una enquesta amb
     *                         el nou títol
     * @throws IOException     Si hi ha un error en accedir a la persistència
     */
    public void setTitolEnquesta(String titolAntic, String titolNou) throws CustomException, IOException {
        if (!titols.containsKey(titolNou)) {
            ensureEnquestaLoaded(titolAntic);
            enquestaTractada.setTitol(titolNou);
            ctrlPersistencia.setTitleEnquesta(titolAntic, titolNou);
            titols.remove(titolAntic);
            titols.put(titolNou, enquestaTractada.getId());
        } else
            throw new CustomException("Aquest títol ja està en ús");
    }

    /**
     * Afegeix una pregunta a l'enquesta
     * 
     * @param titol    Títol de l'enquesta a la que es vol afegir la pregunta
     * @param enunciat Enunciat de la pregunta
     * @param opcions  Opcions de resposta (només per a preguntes de tipus UNIC o
     *                 MULTIPLE)
     * @param format   Format de la pregunta (UNIC, MULTIPLE, NUMERIC, LLIURE)
     * @throws CustomException Llança una excepció si hi ha algun problema en afegir
     *                         la pregunta
     * @throws IOException     Si hi ha un error en accedir a la persistència
     */
    public void afegirPregunta(String titol, String enunciat, List<String> opcions, String format)
            throws CustomException, IOException {
        ensureEnquestaLoaded(titol);
        // cas default es crea format lliure
        Pregunta p = switch (format) {
            case "UNIC" -> new PreguntaFormatUnica(enunciat, opcions);
            case "MULTIPLE" -> new PreguntaMultipleResposta(enunciat, opcions);
            case "NUMERIC" -> new PreguntaFormatNumeric(enunciat);
            default -> new PreguntaFormatLliure(enunciat);
        };
        enquestaTractada.afegeixPregunta(p);
        ctrlPersistencia.addQuestionToEnquestaJSON(enquestaTractada.getTitol(), p);
    }

    /**
     * Es crea una resposta a l'enquesta i es relaciona amb totes les
     * 
     * @param titol El títol de l'enquesta a la que es vol crear la resposta de la
     *              enquesta
     * @param u     L'usuari que ha respòs l'enquesta
     * @throws CustomException          Llança una excepció si hi ha algun problema
     *                                  en crear la resposta de l'enquesta
     * @throws IllegalArgumentException Llança una excepció si les respostes no són
     *                                  vàlides
     * @throws IOException              Si hi ha un error en accedir a la
     *                                  persistència
     */
    public void crearRespostaEnquesta(String titol, Usuari u, List<Object> respostesPreguntes)
            throws CustomException, IllegalArgumentException, IOException {
        ensureEnquestaLoaded(titol);
        List<Pregunta> preg = enquestaTractada.getPreguntes();
        List<RespostaPregunta> respPreg = new ArrayList<RespostaPregunta>();
        for (int i = 0; i < preg.size(); ++i) {
            Pregunta p = preg.get(i);
            Object o = respostesPreguntes.get(i);
            respPreg.add(p.crearResposta(o));
        }
        ctrlPersistencia.crearResposta(new RespostaEnquesta(enquestaTractada.getId(), u.getId(), respPreg), u.getId(),
                enquestaTractada.getId());
        u.addRespostacreada(enquestaTractada.getId());
        ctrlPersistencia.updateUsuari(u);
    }

    /**
     * Modifica la resposta d'una pregunta concreta dins d'una enquesta per un
     * usuari concret.
     * 
     * @param titol       Títol de l'enquesta
     * @param usuari      Usuari que ha respost l'enquesta
     * @param numPregunta Número de la pregunta dins de l'enquesta
     * @param resposta    Nova resposta a la pregunta
     * @throws CustomException           Llança una excepció si l'usuari no ha
     *                                   respost l'enquesta o si la resposta no es
     *                                   valida.
     * @throws IndexOutOfBoundsException Llança una excepció si el número de
     *                                   pregunta no és vàlid.
     * @throws IOException               Si hi ha un error en accedir a la
     *                                   persistència
     */
    public void modificarRespostaPregunta(String titol, Usuari usuari, int numPregunta, Object resposta)
            throws CustomException, IndexOutOfBoundsException, IOException {
        ensureEnquestaLoaded(titol);
        Enquesta enquesta = enquestaTractada;
        RespostaEnquesta respostaEnquesta = enquesta.getRespostaEnquestaUsuari(usuari);
        respostaEnquesta.modificarRespostaPregunta(numPregunta, resposta);
        ctrlPersistencia.updateResposta(respostaEnquesta, usuari.getId(), enquesta.getId());
    }

    /**
     * Elimina una pregunta d'una enquesta concreta.
     *
     * @param titol       Títol de l'enquesta
     * @param numPregunta Número de la pregunta dins de l'enquesta
     * @throws CustomException        Llança una excepció si l'enquesta no existeix.
     * @throws NoSuchElementException Llança una excepció si la pregunta no existeix
     *                                dins de l'enquesta.
     * @throws IOException            Si hi ha un error en accedir a la persistència
     */
    public void eliminarPregunta(String titol, int numPregunta)
            throws NoSuchElementException, IOException, CustomException {
        ensureEnquestaLoaded(titol);
        Enquesta enquesta = enquestaTractada;
        enquesta.eliminarPregunta(numPregunta);
        ctrlPersistencia.deleteQuestionFromEnquestaJSON(titol, Integer.valueOf(numPregunta));
    }

    /**
     * Elimina la resposta d'una enquesta feta per un usuari concret.
     *
     * @param titol  Títol de l'enquesta
     * @param usuari Usuari que ha respost l'enquesta
     * @throws CustomException        Llança una excepció si l'enquesta no existeix.
     * @throws NoSuchElementException Llança una excepció si l'usuari no ha respost
     *                                l'enquesta.
     * @throws IOException            Si hi ha un error en accedir a la persistència
     */
    public void eliminarRespostaEnquesta(String titol, Usuari usuari)
            throws NoSuchElementException, IOException, CustomException {
        ensureEnquestaLoaded(titol);
        try {
            CtrlUsuaris.getInstance().getUsuariActual().deleteRespostacreada(enquestaTractada.getId());
        } catch (Exception ignored) {}
        ctrlPersistencia.deleteResposta(enquestaTractada.getId(), usuari.getId());
    }

    /**
     * Elimina una enquesta concreta per titol
     * 
     * @param titol Títol de l'enquesta a eliminar
     * @throws CustomException Llança una excepció si l'enquesta no existeix.
     * @throws IOException     Si hi ha un error en accedir a la persistència
     */
    public void eliminarEnquesta(String titol) throws IOException, CustomException {
        Integer id = titols.get(titol);
        if (id == null)
            throw new CustomException("L'enquesta no existeix");
        eliminarEnquestaId(id);
    }

    /**
     * Elimina una enquesta concreta per id
     *
     * @param id Id de l'enquesta a eliminar
     * @throws CustomException Llança una excepció si l'enquesta no existeix o no tens permisos.
     * @throws IOException     Si hi ha un error en accedir a la persistència
     */
    public void eliminarEnquestaId(int id) throws IOException, CustomException {
        ensureEnquestaLoadedById(id);
        if ( !(CtrlUsuaris.getInstance().getUsuariActual() instanceof Administrador) && !(CtrlUsuaris.getInstance().getUsuariActual().getId() == enquestaTractada.getCreador()))
            throw new CustomException("No tens els permisos per fer aixo");
        if (CtrlUsuaris.getInstance().getUsuariActual() instanceof Administrador){
            int idU = enquestaTractada.getCreador();
            ctrlPersistencia.getPrivateUsuariById(idU).deleteEnquestaCreada(enquestaTractada.getId());
        }
        if (CtrlUsuaris.getInstance().getUsuariActual().getId() == enquestaTractada.getCreador()) {
            CtrlUsuaris.getInstance().getUsuariActual().deleteEnquestaCreada(enquestaTractada.getId());
        }
        ctrlPersistencia.deleteEnquesta(enquestaTractada.getTitol());
        titols.remove(enquestaTractada.getTitol());
        enquestaTractada = null;
    }

    /**
     * Elimina totes les enquestes d'un usuari concret
     * 
     * @param idenquestes Array de títols de les enquestes a eliminar
     * @throws IOException     Si hi ha un error en accedir a la persistència
     * @throws CustomException Llança una excepció si alguna enquesta no existeix.
     */
    public void eliminarTotesEnquestesUsuari(Integer[] idenquestes) throws IOException, CustomException {
        // Actualitzem només l'estat en memòria (mapa titols) per reflectir que
        // aquestes enquestes seran/eliminades a la capa de persistència.
        if (idenquestes == null)
            return;
        for (Integer id : idenquestes) {
            if (id == null)
                continue;
            String keyToRemove = null;
            for (Map.Entry<String, Integer> entry : titols.entrySet()) {
                if (entry.getValue().equals(id)) {
                    keyToRemove = entry.getKey();
                    break;
                }
            }
            if (keyToRemove != null) {
                titols.remove(keyToRemove);
            }
            if (enquestaTractada != null && enquestaTractada.getId() == id) {
                enquestaTractada = null;
            }
        }
    }

    /**
     * Obté el format d'una pregunta específica dins d'una enquesta.
     * 
     * @param titolEnquesta Títol de l'enquesta.
     * @param numPregunta   Número de la pregunta dins de l'enquesta.
     * @return String representant el format de la pregunta.
     * @throws IndexOutOfBoundsException Si el número de pregunta no és vàlid.
     * @throws IOException               Si hi ha un error en accedir a la
     *                                   persistència
     * @throws CustomException           Si l'enquesta no existeix.
     */
    public String getFormatPregunta(String titolEnquesta, int numPregunta)
            throws IndexOutOfBoundsException, IOException, CustomException {
        ensureEnquestaLoaded(titolEnquesta);
        return enquestaTractada.getFormatPregunta(numPregunta);
    }

    /**
     * Obté les opcions d'una pregunta específica dins d'una enquesta.
     *
     * @param titolEnquesta Títol de l'enquesta.
     * @param numPregunta   Número de la pregunta dins de l'enquesta.
     * @return Llista de Strings representant les opcions de la pregunta.
     * @throws IndexOutOfBoundsException Si el número de pregunta no és vàlid.
     * @throws IOException               Si hi ha un error en accedir a la
     *                                   persistència
     * @throws CustomException           Si l'enquesta no existeix.
     */
    public List<String> getOpcionsPregunta(String titolEnquesta, int numPregunta)
            throws NullPointerException, IndexOutOfBoundsException, IOException, CustomException {
        ensureEnquestaLoaded(titolEnquesta);
        return enquestaTractada.getOpcionsPregunta(numPregunta);
    }

    /**
     * Converteix el resultat del clustering al format de sortida esperat.
     * 
     * @param m Map amb els clusters resultants del clustering.
     * @return Map amb els clusters en format de sortida, on la clau és
     *         l'identificador del cluster i el valor és la llista de noms d'usuaris
     *         que pertanyen al cluster.
     * @throws CustomException Llança una excepció si hi ha algun problema durant la
     *                         conversió.
     */
    private Map<Integer, List<String>> convertClusteringToOutput(Map<Integer, Cluster> m) throws CustomException {
        try {
            Map<Integer, List<String>> convertedClustering = new TreeMap<>();
            for (Map.Entry<Integer, Cluster> entry : m.entrySet()) {
                int clau = entry.getKey();
                Cluster valor = entry.getValue();
                convertedClustering.put(clau, ctrlPersistencia.getnomsUsuarus(valor.getMembres()));
            }
            return convertedClustering;
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }

    /**
     * Executa l'algorisme de clustering escollit sobre l'enquesta indicada.
     * Si k és 0, es calcula automàticament com la raó quadrada del nombre de
     * respostes a l'enquesta.
     * 
     * @param titolEnquesta Títol de l'enquesta sobre la qual es vol fer el
     *                      clustering
     * @param k             Nombre de clusters desitjats (si és 0, es calcula
     *                      automàticament)
     * @param clusterMethod Mètode de clustering a utilitzar (1: K-means, 2:
     *                      K-means++, 3: K-medoids)
     * @return Map amb els clusters resultants, on la clau és l'identificador del
     *         cluster i el valor és la llista de noms d'usuaris que pertanyen al
     *         cluster
     * @throws CustomException Llança una excepció si hi ha algun problema durant
     *                         l'execució del clustering
     * @throws IOException     Si hi ha un error en accedir a la persistència
     */
    public Map<Integer, List<String>> clusterAlgorithm(String titolEnquesta, int k, int clusterMethod)
            throws CustomException, IOException {
        ensureEnquestaLoaded(titolEnquesta);
        enquestaTractada.setResposta((HashMap<Integer, RespostaEnquesta>) ctrlPersistencia.getRespostesEnquesta(enquestaTractada.getId()));
        Enquesta e = enquestaTractada;

        int numRespostes = e.getNumRespostes();
        if (numRespostes < 2) {
            throw new CustomException("No hi ha prou respostes (mínim 2) per fer clustering.");
        }

        switch (clusterMethod) {
            case 1:
                strategyCluster = new Kmeans();
                break;
            case 2:
                strategyCluster = new KmeansPlusPlus();
                break;
            case 3:
                strategyCluster = new Kmedoids();
                break;
            case 4:
                strategyCluster = new Hierarchical();
                break;
            default:
                throw new CustomException("Mètode de clustering desconegut.");
        }

        if (k >= 2) {
            if (k > numRespostes) {
                throw new CustomException(
                        "No pots demanar més clústers (" + k + ") que respostes tens (" + numRespostes + ").");
            }

            resultatClustering = strategyCluster.executa(e, k);
            ultimSilhouette = EvaluacioClustering.calcularSilhouette(resultatClustering, e);

        } else if (k == 0) {
            double millorSilhouetteTrobada = -1.1;
            Map<Integer, Cluster> millorConfiguracio = null;

            int maxK = Math.min(numRespostes - 1, 10);

            for (int i = 2; i <= maxK; i++) {
                Map<Integer, Cluster> resultatTemp = strategyCluster.executa(e, i);
                double silhouetteTemp = EvaluacioClustering.calcularSilhouette(resultatTemp, e);

                if (silhouetteTemp > millorSilhouetteTrobada) {
                    millorSilhouetteTrobada = silhouetteTemp;
                    millorConfiguracio = resultatTemp;
                }
            }

            if (millorConfiguracio != null) {
                resultatClustering = millorConfiguracio;
                ultimSilhouette = millorSilhouetteTrobada;
            } else {
                throw new CustomException("No s'ha pogut calcular una k òptima.");
            }
        } else {
            Random random = new Random();
            int maxK = Math.min(numRespostes - 1, 10);
            int minK = 2;
            if (maxK < minK) {
                k = minK;
            } else {
                k = random.nextInt(maxK - minK + 1) + minK;
            }
            resultatClustering = strategyCluster.executa(e, k);
            ultimSilhouette = EvaluacioClustering.calcularSilhouette(resultatClustering, e);
        }

        return convertClusteringToOutput(resultatClustering);
    }

    /**
     * Retorna el coeficient de Silhouette de l'últim clustering realitzat.
     *
     * @return Coeficient de Silhouette (entre -1 i +1)
     */
    public double getUltimSilhouette() {
        return ultimSilhouette;
    }

    /**
     * Obté els representants reals de cada cluster de l'últim clustering realitzat.
     *
     * @return Llista de Strings amb els noms dels usuaris representants de cada
     *         cluster.
     * @throws CustomException Llança una excepció si algun cluster no té
     *                         representant real.
     * @throws IOException     Si hi ha un error en accedir a la persistència
     */
    public List<String> getRepresentantsCluster() throws CustomException, IOException {
        if (resultatClustering == null)
            throw new CustomException("No s'ha executat cap clustering encara.");
        List<Integer> representants = new ArrayList<>();
        for (Cluster c : resultatClustering.values()) {
            Integer rep = c.getRepresentantReal();
            if (rep == null)
                throw new CustomException("Algún cluster no té representant real.");
            representants.add(rep);
        }
        return ctrlPersistencia.getnomsUsuarus(representants);
    }

    /**
     * Obté la llista de títols de totes les enquestes existents.
     * 
     * @return ArrayList de Strings amb els títols de les enquestes.
     */
    public ArrayList<String> getLlistaEnquestes() {
        return new ArrayList<>(this.titols.keySet());
    }

    /**
     * Retorna l'id de l'enquesta demanada
     * 
     * @param titolenquesta Títol de l'enquesta que es vol
     * @return l'Id de l'enquesta
     * @throws NullPointerException Si el titol no existeix
     */
    public int getIdEnquesta(String titolenquesta) throws NullPointerException {
        if (!titols.containsKey(titolenquesta))
            throw new NullPointerException("l'enquesta no existeix");
        return titols.get(titolenquesta);
    }

    /**
     * Obté la llista d'enunciats de les preguntes d'una enquesta concreta.
     * 
     * @param titolEnquesta Títol de l'enquesta.
     * @return ArrayList de Strings amb els enunciats de les preguntes.
     * @throws NullPointerException Llança una excepció si l'enquesta no existeix.
     * @throws IOException          Si hi ha un error en accedir a la persistència
     * @throws CustomException      Si hi ha algun error del domini
     */
    public ArrayList<String> getPreguntesEnquesta(String titolEnquesta)
            throws NullPointerException, IOException, CustomException {
        ensureEnquestaLoaded(titolEnquesta);
        return enquestaTractada.getEnunciatPreguntes();
    }

    /**
     * Retorna l'enquesta de l'id
     * 
     * @param id identificador de l'enquesta
     * @return Enquesta amb l'id
     * @throws IOException     Si hi ha un error d'escriptura/lectura de fitxers
     * @throws CustomException Si no existeix una enquesta amb l'identificador donat
     */
    public Enquesta getEnquesta(Integer id) throws IOException, CustomException {
        ensureEnquestaLoadedById(id);
        return enquestaTractada;
    }

    /**
     * Importa enquesta al sistema des d'una font externa, s'ha de trobar a la
     * carpeta EXE.
     * 
     * @param fitxer Argument que especifica la font de les dades a importar.
     * @throws CustomException          si no es pot crear l'enquesta.
     * @throws IllegalArgumentException Si el format no és correcte.
     * @throws IOException              Si hi ha un error de lectura/escriptura del
     *                                  fitxer
     */
    public void importarEnquesta(String fitxer, Usuari usuari)
            throws CustomException, IllegalArgumentException, IOException {
        Path executableDir = Paths.get("");
        Path fullPath = Paths.get("");
        String enquesta = null;

        try {
            fullPath = executableDir.resolve(fitxer);
            enquesta = Files.readString(fullPath);
        } catch (IOException e) {
            fullPath = executableDir.resolve("..").resolve("EXE").resolve(fitxer);
            try {
                enquesta = Files.readString(fullPath);
            } catch (IOException e2) {
                throw new NoSuchFileException("No s'ha pogut llegir el fitxer " + fullPath);
            }
        }

        String block = "";
        String format = "", pregunta = "";
        Scanner sc = new Scanner(enquesta);
        // Llegim titol enquesta
        String titolEnquesta = sc.nextLine();
        crearEnquesta(usuari, titolEnquesta);
        while (!block.equals("END")) {
            block = sc.next();
            if (block.equals("F")) {
                format = sc.nextLine().trim();
            } else if (block.equals("P")) {
                pregunta = sc.nextLine().trim();
            } else if (block.equals("O")) {
                String o = "";
                List<String> opcions = new ArrayList<String>();
                while (!o.equals("END_OPCIONS")) {
                    o = sc.nextLine();
                    if (!o.equals("END_OPCIONS") && !o.trim().isEmpty())
                        opcions.add(o);
                }
                afegirPregunta(titolEnquesta, pregunta, opcions, format);
            }
        }
        sc.close();
    }

    /**
     * Importa resposta al sistema des d'una font externa.
     *
     * @param fitxer Argument que especifica la font de les dades a importar.
     * @throws IOException     si hi ha un error de lectura/escriptura del fitxer
     * @throws CustomException Si el format no és correcte.
     */
    public void importarResposta(String enq, Usuari usuari, String fitxer) throws IOException, CustomException {
        Path executableDir = Paths.get("");
        Path fullPath;
        String respostaImportar;

        try {
            fullPath = executableDir.resolve(fitxer);
            respostaImportar = Files.readString(fullPath);
        } catch (IOException e) {
            fullPath = executableDir.resolve("..").resolve("EXE").resolve(fitxer);
            try {
                respostaImportar = Files.readString(fullPath);
            } catch (IOException e2) {
                throw new NoSuchFileException("No s'ha pogut llegir el fitxer " + fullPath);
            }
        }

        Scanner sc = new Scanner(respostaImportar);
        // Agafa preguntes de l'enquesta
        List<String> preg = getPreguntesEnquesta(enq);
        List<Object> respostes = new ArrayList<>();
        for (int i = 0; i < preg.size(); ++i) {
            String format = getFormatPregunta(enq, i);
            String block = sc.nextLine();
            switch (format) {
                case "LLIURE":
                    respostes.add(block);
                    break;
                case "NUMERIC":
                    try {
                        respostes.add(Integer.parseInt(block));
                    } catch (IllegalArgumentException e) {
                        throw new CustomException("Format de resposta NUMERIC invàlid.");
                    }
                    break;
                case "UNIC":
                    try {
                        respostes.add(Integer.parseInt(block));
                    } catch (IllegalArgumentException e) {
                        throw new CustomException("Format de resposta UNIC invàlid.");
                    }
                    break;
                case "MULTIPLE":
                    try {
                        List<Integer> resposta = new ArrayList<>();
                        Scanner scn = new Scanner(block);
                        while (scn.hasNext()) {
                            String num = scn.next();
                            resposta.add(Integer.parseInt(num));
                        }
                        respostes.add(resposta);
                    } catch (NumberFormatException e) {
                        throw new CustomException("Format de resposta MULTIPLE invàlid.");
                    }
                    break;
                default:
                    break;
            }

        }
        crearRespostaEnquesta(enq, usuari, respostes);
        sc.close();
    }

    /**
     * Helper: assegura que l'enquesta tractada és la solicitada per títol.
     * Si no ho és, la carrega des de persistència.
     * 
     * @param titol Títol de l'enquesta
     * @throws IOException     Si hi ha un error en accedir a la persistència
     * @throws CustomException Si no existeix una enquesta amb el títol donat
     */
    private void ensureEnquestaLoaded(String titol) throws IOException, CustomException {
        if (enquestaTractada == null || !titol.equals(enquestaTractada.getTitol())) {
            enquestaTractada = ctrlPersistencia.getEnquesta(titol);
            if (enquestaTractada == null)
                throw new NullPointerException("Aquesta enquesta no existeix");
        }
    }

    /**
     * Helper: assegura que l'enquesta tractada és la solicitada per id.
     * Si no ho és, la carrega des de persistència.
     * 
     * @param id Id de l'enquesta
     * @throws IOException     Si hi ha un error en accedir a la persistència
     * @throws CustomException Si no existeix una enquesta amb l'identificador donat
     */
    private void ensureEnquestaLoadedById(int id) throws IOException, CustomException {
        if (enquestaTractada == null || enquestaTractada.getId() != id) {
            enquestaTractada = ctrlPersistencia.getEnquestaById(id);
        }
    }
}
