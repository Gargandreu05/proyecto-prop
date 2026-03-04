package edu.upc.prop.cluster422.Dades;

import edu.upc.prop.cluster422.Domini.Classes.*;
import edu.upc.prop.cluster422.Domini.Controladors.CustomException;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * Aquesta classe representa el gestor de persistència i la seva funció és
 * comunicar-se amb
 * la capa de dades, en el nostre cas els fitxers, per emmagatzemar les dades de
 * la nostra aplicació.
 *
 * @author Roger Guinovart
 * @version 10/12/2025
 */
public class GestorEnquestes {
    /**
     * Instància de gestor enquestes per complir amb el patró singleton i que només
     * existeixi una única instància
     */
    private static GestorEnquestes instance;

    /**
     * Atribut que indica el path del subdirectori Enquestes contingut dins de
     * Storage.
     */
    private final Path PATH_ENQUESTES_DIR;

    /**
     * Atribut que indica el path base d'una enquesta concreta. Quan es sàpiga el id
     * de l'enquesta
     */
    private Path PATH_ENQUESTA;

    private final Path PATH_LLISTAT;

    /**
     * Creadora del gestor d'enquestes privada per assolir el patró singleton.
     * Crea els directoris i fitxers necessaris per al funcionament del gestor
     * d'enquestes.
     *
     * @param data_path Path base on es troba el directori de dades (conté el
     *                  subdirectori "Enquestes").
     * @throws IOException Si hi ha un error d'entrada/sortida al crear directoris o
     *                     fitxers
     */
    private GestorEnquestes(Path data_path) throws IOException {
        PATH_ENQUESTES_DIR = data_path.resolve("Enquestes");
        PATH_LLISTAT = PATH_ENQUESTES_DIR.resolve(Paths.get("llistat_enquestes.csv"));
        if (!Files.exists(PATH_ENQUESTES_DIR)) {
            Files.createDirectories(PATH_ENQUESTES_DIR);
        }
        if (!Files.exists(PATH_LLISTAT)) {
            Files.createFile(PATH_LLISTAT);
        }
    }

    /**
     * Getter de l'instància de gestor enquestes. Si no existeix la crea i la
     * retorna.
     *
     * @param data_path Path base on es troba el directori de dades (conté el
     *                  subdirectori "Enquestes").
     * @return Instància de gestor enquestes.
     * @throws IOException Si hi ha un error d'entrada/sortida en la inicialització
     *                     del gestor
     */
    public static GestorEnquestes getInstance(Path data_path) throws IOException {
        if (instance == null) {
            instance = new GestorEnquestes(data_path);
        }
        return instance;
    }

    /**
     * Aquesta funció s'encarrega de crear una enquesta en la capa de dades i crear
     * el subdirectori i fitxers necessaris
     * per poder guardar-la, a més de modificar altres fitxers per mantenir
     * consistència de la capa de persistència.
     *
     * @param e Enquesta que es vol crear.
     * @return Id de l'enquesta creada, o -1 si ja existeix una enquesta amb el
     *         mateix títol.
     * @throws IOException Si hi ha un error d'entrada/sortida en l'escriptura dels
     *                     fitxers
     */
    public Integer crearEnquesta(Enquesta e) throws IOException {
        Map<Integer, String> llistat_enquestes = llegirLlistatCSV();
        if (llistat_enquestes.containsValue(e.getTitol())) {
            mostraMissatgeError("Ja existeix una enquesta amb aquest títol.");
            return -1;
        }
        int next_id = llistat_enquestes.size() + 1;
        insertEnquestaToJSON(e, next_id);
        insertEnquestaToCSV(next_id, e.getTitol());
        return next_id;
    }

    /**
     * Aquesta funció s'encarrega de obtenir una enquesta de la capa de dades i
     * convertir-la en un objecte
     * Enquesta per retornar-la al control de persistència.
     *
     * @param titolEnquesta Títol de l'enquesta que es vol obtenir.
     * @return Un objecte Enquesta corresponent a l'enquesta que es volia obtenir.
     * @throws IOException     Si hi ha un error d'entrada/sortida en la lectura
     *                         dels fitxers
     * @throws CustomException Si no existeix cap enquesta amb aquest títol (error
     *                         de domini)
     */
    public Enquesta getEnquesta(String titolEnquesta) throws IOException, CustomException {
        Map<String, Integer> llistat = llegirLlistatCSVInvers();
        if (!llistat.containsKey(titolEnquesta)) {
            throw new CustomException("No existeix cap enquesta amb aquest títol.");
        }
        return carregarEnquestaFromJSON(llistat.get(titolEnquesta));
    }

    /**
     * Obté una enquesta a partir del seu identificador numèric i la carrega des del
     * sistema de fitxers.
     *
     * @param idEnquesta Id de l'enquesta que es vol obtenir.
     * @return Objecte Enquesta corresponent a l'enquesta amb l'id proporcionat.
     * @throws IOException     Si hi ha un error d'entrada/sortida en la lectura
     *                         dels fitxers
     * @throws CustomException Si no existeix cap enquesta amb aquest id (error de
     *                         domini)
     */
    public Enquesta getEnquestaById(Integer idEnquesta) throws IOException, CustomException {
        Map<Integer, String> llistat = llegirLlistatCSV();
        if (!llistat.containsKey(idEnquesta)) {
            throw new CustomException("No existeix cap enquesta amb aquest id.");
        }
        return carregarEnquestaFromJSON(idEnquesta);
    }

    /**
     * Aquesta funció elimina una enquesta de la capa de persistència i totes les
     * seves relacions.
     *
     * @param titolEnquesta Títol de l'enquesta que es vol eliminar.
     * @throws IOException     Si hi ha un error d'entrada/sortida en l'eliminació
     *                         de fitxers/directoris
     * @throws CustomException Si l'enquesta no existeix o no es pot eliminar per
     *                         regles del domini
     */
    public void deleteEnquesta(String titolEnquesta) throws IOException, CustomException {
        // recordar ficar custom exception si no existeix l'enquesta
        Integer idEnquesta = getIdEnquesta(titolEnquesta);

        // Eliminar directori de l'enquesta i tota info de dins
        PATH_ENQUESTA = PATH_ENQUESTES_DIR.resolve("Enquesta_" + idEnquesta);
        if (Files.exists(PATH_ENQUESTA)) {
            List<Path> toDelete = new ArrayList<>();
            try (java.util.stream.Stream<Path> stream = Files.walk(PATH_ENQUESTA)) {
                stream.forEach(toDelete::add);
            }
            toDelete.sort(Comparator.reverseOrder());
            for (Path p : toDelete) {
                Files.deleteIfExists(p);
            }
        }
        // Esborrar enquesta del llistat CSV
        // Es copia en un fitxer temporal totes les línies excepte la de l'enquesta a eliminar i despres
        // es fa la sobrescriptura del fitxer origianal
        Path tmp = Files.createTempFile(PATH_ENQUESTES_DIR, "llistat_temp", ".csv");
        try (java.io.BufferedReader reader = Files.newBufferedReader(PATH_LLISTAT);
             java.io.BufferedWriter writer = Files.newBufferedWriter(tmp, StandardOpenOption.TRUNCATE_EXISTING)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1) {
                    try {
                        int currentId = Integer.parseInt(parts[0].trim());
                        if (currentId != idEnquesta) {
                            writer.write(line);
                            writer.newLine();
                        }
                    } catch (NumberFormatException numExc) {
                        // Si la linia no és vàlida (no té un id o no és un número), la copiem igualment
                        // No hauria de passar perquè sempre insertem al fitxer en format correcte pero per seguretat ho comprovem
                        writer.write(line);
                        writer.newLine();
                    }
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }
        Files.move(tmp, PATH_LLISTAT, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Aquesta funció obté el títol de totes les enquestes al nostre sistema per
     * mostrar el nom per pantalla.
     *
     * @return Map amb els títols i ids de totes les enquestes existents en el
     *         sistema (clau = titol, valor = id).
     * @throws IOException Si hi ha un error d'entrada/sortida en la lectura del
     *                     llistat
     */
    public Map<String, Integer> getLlistatEnquestes() throws IOException {
        return llegirLlistatCSVInvers();
    }

    /**
     * Aquesta funció mostra un missatge d'error per pantalla.
     *
     * @param msg Missatge d'error que es vol mostrar.
     */
    private void mostraMissatgeError(String msg) {
        System.out.println("Error: " + msg);
    }

    /**
     * Aquesta funció retorna el id d'una enquesta donat el seu títol.
     *
     * @param titolEnquesta Títol de l'enquesta de la qual es vol obtenir l'id.
     * @return Id de l'enquesta.
     * @throws IOException     Si hi ha un error d'entrada/sortida en la lectura del
     *                         llistat
     * @throws CustomException Si no existeix cap enquesta amb aquest títol (error
     *                         de domini)
     */
    private Integer getIdEnquesta(String titolEnquesta) throws IOException, CustomException {
        Map<String, Integer> invers = llegirLlistatCSVInvers();
        if (!invers.containsKey(titolEnquesta)) {
            throw new CustomException("No existeix cap enquesta amb aquest títol.");
        }
        return invers.get(titolEnquesta);
    }

    /**
     * Aquesta funció llegeix el fitxer CSV que conté el llistat d'enquestes i els
     * seus ids i els retorna
     * en un mapa per facilitar la seva consulta.
     *
     * @return Mapa amb els ids i títols de les enquestes. La clau és l'id de
     *         l'enquesta i value el títol
     *         de l'enquesta.
     * @throws IOException Si hi ha un error d'entrada/sortida en la lectura del
     *                     fitxer CSV
     */
    private Map<Integer, String> llegirLlistatCSV() throws IOException {
        Map<Integer, String> llistat = new HashMap<>();
        List<String> lines = Files.readAllLines(PATH_LLISTAT);
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 2) {
                Integer id = Integer.parseInt(parts[0].trim());
                String titol = parts[1].trim();
                llistat.put(id, titol);
            }
        }
        return llistat;
    }

    /**
     * Aquesta funció llegeix el fitxer CSV que conté el llistat d'enquestes i els
     * seus ids i els retorna
     * en un mapa invers per facilitar la seva consulta.
     *
     * @return Mapa amb els títols i ids de les enquestes amb clau el titol de
     *         l'enquesta i value l'id de l'enquesta..
     * @throws IOException Si hi ha un error d'entrada/sortida en la lectura del
     *                     fitxer CSV
     */

    private Map<String, Integer> llegirLlistatCSVInvers() throws IOException {
        Map<String, Integer> llistat = new HashMap<>();
        List<String> lines = Files.readAllLines(PATH_LLISTAT);
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 2) {
                Integer id = Integer.parseInt(parts[0].trim());
                String titol = parts[1].trim();
                llistat.put(titol, id);
            }
        }
        return llistat;
    }

    /**
     * Aquesta funció s'encarrega d'inserir una enquesta en format JSON dins de la
     * capa de dades.
     *
     * @param e  Enquesta que es vol inserir.
     * @param id Id que s'ha assignat a l'enquesta.
     * @throws IOException Si hi ha un error d'entrada/sortida en la creació del
     *                     directori o escrit del fitxer
     */
    private void insertEnquestaToJSON(Enquesta e, Integer id) throws IOException {
        // Crear el directori de l'enquesta
        PATH_ENQUESTA = PATH_ENQUESTES_DIR.resolve("Enquesta_" + id);
        if (!Files.exists(PATH_ENQUESTA)) {
            Files.createDirectories(PATH_ENQUESTA);
        }
        // Crear el fitxer JSON de l'enquesta
        Path fitxer_enquesta = PATH_ENQUESTA.resolve("enquesta.json");
        StringBuilder jsonContent = new StringBuilder();
        jsonContent.append("{\n");
        jsonContent.append("    \"id\": ").append(id).append(",\n");
        jsonContent.append("    \"titol\": \"").append(e.getTitol()).append("\",\n");
        jsonContent.append("    \"creador\": \"").append(e.getCreador()).append("\",\n");
        // Ficar les preguntes de l'enquesta
        List<Pregunta> preguntes = e.getPreguntes();
        jsonContent.append("    \"preguntes\": [\n");
        for (int i = 0; i < preguntes.size(); i++) {
            Pregunta p = preguntes.get(i);
            jsonContent.append("    {\n");
            jsonContent.append("     \"tipus\": ").append(stringToJson(p.tipusPregunta())).append(",\n");
            jsonContent.append("     \"enunciat\": ").append(stringToJson(p.getEnunciat())).append(",\n");
            // Insertem les opcions
            List<String> opcions = p.getOpcions();
            jsonContent.append("     \"opcions\": [\n");
            for (int j = 0; j < opcions.size(); j++) {
                jsonContent.append("      ").append(stringToJson(opcions.get(j)));
                if (j < opcions.size() - 1) {
                    jsonContent.append(",\n");
                } else {
                    jsonContent.append("\n");
                }
            }
            jsonContent.append("     ]\n");
            jsonContent.append("    }");
            if (i < preguntes.size() - 1) {
                jsonContent.append(",\n");
            } else {
                jsonContent.append("\n");
            }
        }
        jsonContent.append("  ]\n");
        jsonContent.append("}\n");
        Files.writeString(fitxer_enquesta, jsonContent.toString());
    }

    /**
     * Aquesta funció afegeix una enquesta al fitxer CSV que conté el llistat
     * d'enquestes.
     *
     * @param id    Id de l'enquesta que s'ha creat.
     * @param titol Títol de l'enquesta que s'ha creat.
     * @throws IOException Si hi ha un error d'entrada/sortida en l'escriptura del
     *                     fitxer CSV
     */
    private void insertEnquestaToCSV(Integer id, String titol) throws IOException {
        String novaLinia = id + "," + titol + "\n";
        Files.writeString(PATH_LLISTAT, novaLinia, StandardOpenOption.APPEND);
    }

    /**
     * Aquesta funció afegeix una pregunta en format JSON dins de l'enquesta
     * corresponent a la capa de dades.
     *
     * @param titolEnquesta Títol de l'enquesta a la qual s'ha d'afegir la pregunta.
     * @param p             Pregunta que es vol afegir.
     * @throws IOException Si hi ha un error d'entrada/sortida en l'operació
     *                     d'escriptura
     * @throws CustomException Si no existeix cap enquesta amb aquest títol
     */
    public void addQuestionToEnquestaJSON(String titolEnquesta, Pregunta p) throws IOException, CustomException {
        Integer idEnquesta = getIdEnquesta(titolEnquesta);
        Enquesta e = carregarEnquestaFromJSON(idEnquesta);
        e.afegeixPregunta(p);
        insertEnquestaToJSON(e, idEnquesta);
    }

    /**
     * Aquesta funció elimina una pregunta en format JSON dins de l'enquesta
     * corresponent a la capa de dades.
     * @param titolEnquesta Títol de l'enquesta de la qual s'ha d'eliminar la
     *                      pregunta.
     * @param numPregunta   Índex o identificador de la pregunta que es vol
     *                      eliminar.
     * @throws IOException Si hi ha un error d'entrada/sortida en l'operació
     *                     d'esborrat
     * @throws CustomException Si no existeix cap enquesta amb aquest títol
     */
    public void deleteQuestionFromEnquestaJSON(String titolEnquesta, Integer numPregunta) throws IOException, CustomException {
        Integer idEnquesta = getIdEnquesta(titolEnquesta);
        Enquesta e = carregarEnquestaFromJSON(idEnquesta);
        e.eliminarPregunta(numPregunta);
        insertEnquestaToJSON(e, idEnquesta);
    }

    /**
     * Aquesta funció modifica el títol d'una enquesta en la capa de dades. Modifica
     * tant el fitxer JSON de l'enquesta
     * com el fitxer CSV que conté el llistat d'enquestes.
     *
     * @param titolEnquesta Títol actual de l'enquesta que es vol modificar.
     * @param nouTitol      Nou títol que es vol assignar a l'enquesta.
     * @throws IOException Si hi ha un error d'entrada/sortida en l'actualització
     *                     dels fitxers
     */
    public void setTitleEnquesta(String titolEnquesta, String nouTitol) throws IOException, CustomException {
        // Actualitzar el fitxer CSV
        Map<String, Integer> llistat = llegirLlistatCSVInvers();
        if (!llistat.containsKey(titolEnquesta)) {
            throw new CustomException("No existeix cap enquesta amb aquest títol.");
        }
        if (nouTitol == null || nouTitol.trim().isEmpty()) {
            throw new CustomException("El nou titol no pot ser buit.");
        }
        if (llistat.containsKey(nouTitol)) {
            throw new CustomException("Ja existeix una enquesta amb aquest títol.");
        }

        Integer idEnquesta = llistat.get(titolEnquesta);

        // Carregar enquesta, modificar títol i reescriure el JSON
        Enquesta e = carregarEnquestaFromJSON(idEnquesta);
        e.setTitol(nouTitol); // pot llençar CustomException si titol no vàlid
        insertEnquestaToJSON(e, idEnquesta);

        // Actualitzar el llistat CSV: crear un temporal i escriure totes les línies
        Path tmp = Files.createTempFile(PATH_ENQUESTES_DIR, "llistat_temp", ".csv");
        try (java.io.BufferedReader reader = Files.newBufferedReader(PATH_LLISTAT);
             java.io.BufferedWriter writer = Files.newBufferedWriter(tmp, StandardOpenOption.TRUNCATE_EXISTING)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1) {
                    try {
                        int currentId = Integer.parseInt(parts[0].trim());
                        if (currentId == idEnquesta) {
                            writer.write(currentId + "," + nouTitol);
                            writer.newLine();
                        } else {
                            writer.write(line);
                            writer.newLine();
                        }
                    } catch (NumberFormatException numExc) {
                        // Linia no comença per un numero, linia no valida pero la copiem igualment,
                        // no hauria de passar perque sempre escribim amb el mateix format al fixer.
                        writer.write(line);
                        writer.newLine();
                    }
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }
        Files.move(tmp, PATH_LLISTAT, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Aquesta funció carrega una enquesta des de la capa de dades i la converteix
     * en un objecte Enquesta.
     * @param idEnquesta Id de l'enquesta que es vol carregar.
     * @return Objecte Enquesta corresponent a l'enquesta carregada.
     * @throws IOException Si hi ha un error d'entrada/sortida en la lectura del
     *                     fitxer JSON
     */
    private Enquesta carregarEnquestaFromJSON(Integer idEnquesta) throws IOException, CustomException {
        // Llegir el fitxer JSON de l'enquesta
        PATH_ENQUESTA = PATH_ENQUESTES_DIR.resolve("Enquesta_" + idEnquesta);
        Path fitxerEnquesta = PATH_ENQUESTA.resolve("enquesta.json");
        String contingutJson = Files.readString(fitxerEnquesta);
        String titolEnquesta = getValueFromJSON(contingutJson, "titol");
        int idJson = Integer.parseInt(getValueFromJSON(contingutJson, "id"));
        int creadorJson = Integer.parseInt(getValueFromJSON(contingutJson, "creador"));
        Enquesta enquesta = new Enquesta(titolEnquesta, creadorJson, idJson);
        // Carregar les preguntes a l'enquesta
        List<Pregunta> llistaPreguntes = new ArrayList<>();
        int idxPreguntes = contingutJson.indexOf("\"preguntes\"");
        if (idxPreguntes != -1) {
            int iniciArray = contingutJson.indexOf('[', idxPreguntes);
            if (iniciArray != -1) {
                int profunditat = 0;
                int fiArray = -1;
                for (int k = iniciArray; k < contingutJson.length(); k++) {
                    char c = contingutJson.charAt(k);
                    if (c == '[') profunditat++;
                    else if (c == ']') {
                        profunditat--;
                        if (profunditat == 0) {
                            fiArray = k;
                            break;
                        }
                    }
                }
                if (fiArray != -1 && fiArray > iniciArray) {
                    String contingutArray = contingutJson.substring(iniciArray + 1, fiArray);
                    int posicio = 0;
                    while (true) {
                        int iniciObjecte = contingutArray.indexOf('{', posicio);
                        if (iniciObjecte == -1) break;
                        int profundObjecte = 0;
                        int fiObjecte = -1;
                        for (int j = iniciObjecte; j < contingutArray.length(); j++) {
                            char c = contingutArray.charAt(j);
                            if (c == '{') profundObjecte++;
                            else if (c == '}') {
                                profundObjecte--;
                                if (profundObjecte == 0) {
                                    fiObjecte = j;
                                    break;
                                }
                            }
                        }
                        if (fiObjecte == -1) break; // format invalid
                        String objecteJson = contingutArray.substring(iniciObjecte, fiObjecte + 1);

                        String tipusStr = getValueFromJSON(objecteJson, "tipus");
                        String enunciatStr = getValueFromJSON(objecteJson, "enunciat");
                        List<String> opcionsLista = getListFromJSON(objecteJson, "opcions");

                        if (tipusStr == null) tipusStr = "";
                        switch (tipusStr.trim().toUpperCase()) {
                            case "UNIC":
                                llistaPreguntes.add(new PreguntaFormatUnica(enunciatStr, opcionsLista));
                                break;
                            case "MULTIPLE":
                                llistaPreguntes.add(new PreguntaMultipleResposta(enunciatStr, opcionsLista));
                                break;
                            case "NUMERIC":
                                llistaPreguntes.add(new PreguntaFormatNumeric(enunciatStr));
                                break;
                            case "LLIURE":
                                llistaPreguntes.add(new PreguntaFormatLliure(enunciatStr));
                                break;
                            default:
                                throw new CustomException("Tipus de pregunta no valid");
                        }

                        posicio = fiObjecte + 1;
                    }
                }
            }
        }

        for (Pregunta pregunta : llistaPreguntes) {
            enquesta.afegeixPregunta(pregunta);
        }
        return enquesta;
    }
    /**
     * Aquesta funció obté un valor des d'un fitxer JSON donat una clau específica.
     * @param json Fitxer json on es vol buscar el valor (contingut JSON en
     *                  text).
     * @param key       Clau per la qual es vol obtenir el valor.
     * @return Valor associat a la clau donada, o cadena buida si no es troba.
     */
    private String getValueFromJSON(String json, String key) {
        int index = json.indexOf("\"" + key + "\":");
        if (index == -1) return "";
        int colon = json.indexOf(':', index);
        if (colon == -1) return "";
        int i = colon + 1;
        while (i < json.length() && Character.isWhitespace(json.charAt(i))) i++;
        if (i < json.length() && json.charAt(i) == '"') {
            int startQuote = i;
            int endQuote = json.indexOf('"', startQuote + 1);
            if (endQuote == -1) return "";
            return json.substring(startQuote + 1, endQuote);
        } else {
            int j = i;
            while (j < json.length() && ",}] \n\r\t".indexOf(json.charAt(j)) == -1) j++;
            return json.substring(i, j).trim();
        }
    }

    /**
     * Aquesta funció obté una llista de valors des d'un fitxer JSON donat una clau
     * específica.
     *
     * @param contingutJson Fitxer json on es vol buscar el valor (contingut JSON en
     *                      text).
     * @param clau       Clau per la qual es vol obtenir la llista de valors.
     * @return Llista de valors associats a la clau donada. Pot ser buida si no es
     *         troben valors.
     */
    private List<String> getListFromJSON(String contingutJson, String clau) {
        List<String> llista = new ArrayList<>();
        int idx = contingutJson.indexOf("\"" + clau + "\":");
        if (idx == -1) return llista;
        int inici = contingutJson.indexOf('[', idx);
        if (inici == -1) return llista;
        int profunditat = 0;
        int fi = -1;
        for (int i = inici; i < contingutJson.length(); i++) {
            char c = contingutJson.charAt(i);
            if (c == '[') profunditat++;
            else if (c == ']') {
                profunditat--;
                if (profunditat == 0) {
                    fi = i;
                    break;
                }
            }
        }
        if (fi == -1) return llista;
        String dinsArray = contingutJson.substring(inici + 1, fi);
        int posicio = 0;
        while (true) {
            int qInici = dinsArray.indexOf('"', posicio);
            if (qInici == -1) break;
            int qFi = dinsArray.indexOf('"', qInici + 1);
            if (qFi == -1) break;
            String valor = dinsArray.substring(qInici + 1, qFi);
            llista.add(valor);
            posicio = qFi + 1;
        }
        return llista;
    }

    /**
     * Aquesta funció converteix una cadena de text en un format vàlid per a ser
     * inclòs en un fitxer JSON.
     *
     * @param s Cadena original (pot ser null).
     * @return Cadena de text en format vàlid per a JSON (entre cometes) o cadena
     *         buida si s és null.
     */
    private String stringToJson(String s) {
        if (s == null)
            return "";
        return "\"" + s + "\"";
    }

}
