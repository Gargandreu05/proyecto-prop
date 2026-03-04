package edu.upc.prop.cluster422.Dades;

import edu.upc.prop.cluster422.Domini.Classes.*;
import edu.upc.prop.cluster422.Domini.Controladors.CustomException;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Aquesta classe és el gestor de la persistència de les respostes a enquestes.
 * Totes les respostes a una enquesta es guarden en un únic fitxer
 * respostes.json
 * dins del directori de l'enquesta (Enquesta_X/respostes.json) per optimitzar
 * l'execució dels algorismes de clustering.
 * 
 * @author Enzo Miquel
 * @version 14/12/2025
 */
public class GestorRespostes {

    /**
     * Instància única del gestor de respostes per complir amb el patró singleton.
     */
    private static GestorRespostes instance;

    /**
     * Path base del directori d'enquestes (compartit amb GestorEnquestes)
     */
    private final Path path_respostes_enquesta;

    /**
     * Creadora privada del GestorRespostes
     * 
     * @param path_data el path del directori per guardar dades
     * @throws FileSystemNotFoundException si salta algun error en accedir al
     *                                     directori
     */
    private GestorRespostes(Path path_data) throws FileSystemNotFoundException {
        path_respostes_enquesta = path_data.resolve("Enquestes");
        if (!Files.exists(path_respostes_enquesta)) {
            try {
                Files.createDirectories(path_respostes_enquesta);
            } catch (IOException e) {
                throw new FileSystemNotFoundException("Error al crear el directori d'enquestes: " + e.getMessage());
            }
        }
    }

    /**
     * getter de l'instància única del GestorRespostes
     * 
     * @param path_data path indicat pel CtrlPersistencia
     * @return l'instància única del GestorRespostes
     * @throws FileSystemNotFoundException si salta algun error
     */
    public static GestorRespostes getInstance(Path path_data) throws FileSystemNotFoundException {
        if (instance == null)
            instance = new GestorRespostes(path_data);
        return instance;
    }

    /**
     * Afegeix una nova resposta al fitxer respostes.json de l'enquesta
     * 
     * @param r          nova resposta a guardar
     * @param idUsuari   ID de l'usuari que ha respost
     * @param idEnquesta ID de l'enquesta resposta
     * @throws IOException si salta algun error escrivint al fitxer
     */
    public void crearResposta(RespostaEnquesta r, int idUsuari, int idEnquesta) throws IOException {
        // Llegir les respostes existents (si n'hi ha)
        List<Map<String, Object>> respostesExistents = llegirTotsRespostes(idEnquesta);

        // Afegir la nova resposta
        Map<String, Object> novaResposta = respostaToMap(r, idUsuari);
        respostesExistents.add(novaResposta);

        // Guardar tot l'array de respostes
        guardarTotsRespostes(idEnquesta, respostesExistents);
    }

    /**
     * Obté totes les respostes d'una enquesta
     * 
     * @param idEnquesta ID de l'enquesta
     * @return llista de RespostaEnquesta
     * @throws IOException si salta algun error llegint el fitxer
     */
    public Map<Integer,RespostaEnquesta> getRespostesEnquesta(int idEnquesta)
            throws IOException, CustomException {
        List<Map<String, Object>> respostesMap = llegirTotsRespostes(idEnquesta);
        Map<Integer, RespostaEnquesta> respostes = new HashMap<>();

        for (Map<String, Object> respMap : respostesMap) {
            int idUsuari = (int) respMap.get("id_usuari");
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> respostesPreguntes = (List<Map<String, Object>>) respMap
                    .get("respostes_preguntes");
            List<RespostaPregunta> llistaRespostesPreguntes = parseRespostesPreguntes(respostesPreguntes);

            RespostaEnquesta re = new RespostaEnquesta(idEnquesta, idUsuari, llistaRespostesPreguntes);
            respostes.put(idUsuari,re);
        }

        return respostes;
    }

    /**
     * Obté la resposta d'un usuari específic a una enquesta
     * 
     * @param idEnquesta ID de l'enquesta
     * @param idUsuari   ID de l'usuari
     * @return RespostaEnquesta de l'usuari o null si no ha respost
     * @throws IOException si salta algun error llegint el fitxer
     */
    public RespostaEnquesta getRespostaUsuari(int idEnquesta, int idUsuari)
            throws IOException, CustomException {
        List<Map<String, Object>> respostesMap = llegirTotsRespostes(idEnquesta);

        for (Map<String, Object> respMap : respostesMap) {
            int id = (int) respMap.get("id_usuari");
            if (id == idUsuari) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> respostesPreguntes = (List<Map<String, Object>>) respMap
                        .get("respostes_preguntes");
                List<RespostaPregunta> llistaRespostesPreguntes = parseRespostesPreguntes(respostesPreguntes);

                return new RespostaEnquesta(idEnquesta, idUsuari, llistaRespostesPreguntes);
            }
        }

        return null;
    }

    /**
     * Esborra la resposta d'un usuari a una enquesta
     * 
     * @param idEnquesta ID de l'enquesta
     * @param idUsuari   ID de l'usuari
     * @throws IOException si salta algun error actualitzant el fitxer
     */
    public void deleteResposta(int idEnquesta, int idUsuari) throws IOException {
        List<Map<String, Object>> respostesExistens = llegirTotsRespostes(idEnquesta);

        // Filtrar per eliminar la resposta de l'usuari
        respostesExistens.removeIf(resp -> (int) resp.get("id_usuari") == idUsuari);

        // Guardar l'array actualitzat
        guardarTotsRespostes(idEnquesta, respostesExistens);
    }

    /**
     * Actualitza la resposta d'un usuari a una enquesta
     * 
     * @param r          nova resposta
     * @param idUsuari   ID de l'usuari
     * @param idEnquesta ID de l'enquesta
     * @throws IOException si salta algun error actualitzant el fitxer
     */
    public void updateResposta(RespostaEnquesta r, int idUsuari, int idEnquesta) throws IOException {
        List<Map<String, Object>> respostesExistens = llegirTotsRespostes(idEnquesta);

        // Buscar i actualitzar la resposta de l'usuari
        boolean trobat = false;
        for (int i = 0; i < respostesExistens.size(); i++) {
            if ((int) respostesExistens.get(i).get("id_usuari") == idUsuari) {
                respostesExistens.set(i, respostaToMap(r, idUsuari));
                trobat = true;
                break;
            }
        }

        // Si no existia, la creem
        if (!trobat) {
            respostesExistens.add(respostaToMap(r, idUsuari));
        }

        // Guardar l'array actualitzat
        guardarTotsRespostes(idEnquesta, respostesExistens);
    }

    /**
     * Comprova si un usuari ja ha respost una enquesta
     * 
     * @param idEnquesta ID de l'enquesta
     * @param idUsuari   ID de l'usuari
     * @return true si ja ha respost, false altrament
     * @throws IOException si salta algun error llegint el fitxer
     */
    public boolean existsResposta(int idEnquesta, int idUsuari) throws IOException {
        List<Map<String, Object>> respostesMap = llegirTotsRespostes(idEnquesta);

        for (Map<String, Object> respMap : respostesMap) {
            if ((int) respMap.get("id_usuari") == idUsuari) {
                return true;
            }
        }

        return false;
    }

    /**
     * Obté el path del fitxer respostes.json d'una enquesta
     * 
     * @param idEnquesta ID de l'enquesta
     * @return Path al fitxer respostes.json
     */
    private Path getFitxerRespostes(int idEnquesta) {
        return path_respostes_enquesta.resolve("Enquesta_" + idEnquesta).resolve("respostes.json");
    }

    /**
     * Llegeix totes les respostes d'una enquesta del fitxer JSON
     * 
     * @param idEnquesta ID de l'enquesta
     * @return llista de Maps amb les dades de cada resposta
     * @throws IOException si salta algun error llegint el fitxer
     */
    private List<Map<String, Object>> llegirTotsRespostes(int idEnquesta) throws IOException {
        Path fitxer = getFitxerRespostes(idEnquesta);

        if (!Files.exists(fitxer)) {
            return new ArrayList<>();
        }

        String content = Files.readString(fitxer);
        if (content.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return parseJsonArray(content);
    }

    /**
     * Guarda totes les respostes d'una enquesta al fitxer JSON
     * 
     * @param idEnquesta ID de l'enquesta
     * @param respostes  llista de Maps amb les dades de cada resposta
     * @throws IOException si salta algun error escrivint el fitxer
     */
    private void guardarTotsRespostes(int idEnquesta, List<Map<String, Object>> respostes) throws IOException {
        Path dirEnquesta = path_respostes_enquesta.resolve("Enquesta_" + idEnquesta);
        if (!Files.exists(dirEnquesta)) {
            Files.createDirectories(dirEnquesta);
        }

        Path fitxer = getFitxerRespostes(idEnquesta);

        // Generar JSON array
        StringBuilder json = new StringBuilder();
        json.append("[\n");

        for (int i = 0; i < respostes.size(); i++) {
            json.append("  ").append(mapToJson(respostes.get(i)));
            if (i < respostes.size() - 1)
                json.append(",");
            json.append("\n");
        }

        json.append("]");

        Files.writeString(fitxer, json.toString());
    }

    /**
     * Converteix una RespostaEnquesta a un Map per serialitzar
     * 
     * @param r        resposta a convertir
     * @param idUsuari ID de l'usuari
     * @return Map amb les dades
     */
    private Map<String, Object> respostaToMap(RespostaEnquesta r, int idUsuari) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id_usuari", idUsuari);

        List<Map<String, Object>> respostesPreguntes = new ArrayList<>();
        List<RespostaPregunta> respostes = r.getRespostaAPregunta();

        for (int i = 0; i < respostes.size(); i++) {
            respostesPreguntes.add(respostaPreguntaToMap(respostes.get(i), i));
        }

        map.put("respostes_preguntes", respostesPreguntes);

        return map;
    }

    /**
     * Converteix una RespostaPregunta a un Map
     * 
     * @param rp    resposta a convertir
     * @param index índex de la pregunta
     * @return Map amb les dades
     */
    private Map<String, Object> respostaPreguntaToMap(RespostaPregunta rp, int index) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("index", index);

        if (rp instanceof RespostaInteger) {
            map.put("tipus", "INTEGER");
            map.put("valor", ((RespostaInteger) rp).getEntrega());
        } else if (rp instanceof RespostaString) {
            map.put("tipus", "STRING");
            map.put("valor", ((RespostaString) rp).getEntrega());
        } else if (rp instanceof RespostaVector) {
            map.put("tipus", "VECTOR");
            map.put("valor", new ArrayList<>(((RespostaVector) rp).getEntrega()));
        }

        return map;
    }

    /**
     * Parseja una llista de Maps a llista de RespostaPregunta
     * 
     * @param respostesMap llista de maps amb les dades
     * @return llista de RespostaPregunta
     */
    private List<RespostaPregunta> parseRespostesPreguntes(List<Map<String, Object>> respostesMap)
            throws CustomException {
        List<RespostaPregunta> respostes = new ArrayList<>();

        for (Map<String, Object> map : respostesMap) {
            String tipus = (String) map.get("tipus");
            Object valor = map.get("valor");

            if (tipus.equals("INTEGER")) {
                Integer val = (valor == null) ? null : ((Number) valor).intValue();
                respostes.add(new RespostaInteger(val));
            } else if (tipus.equals("STRING")) {
                respostes.add(new RespostaString((String) valor));
            } else if (tipus.equals("VECTOR")) {
                @SuppressWarnings("unchecked")
                List<Integer> llista = (List<Integer>) valor;
                Vector<Integer> vector = new Vector<>(llista);
                respostes.add(new RespostaVector(vector));
            }
        }

        return respostes;
    }

    // ==================== PARSERS JSON ====================

    /**
     * Parseja un array JSON a List<Map>
     * 
     * @param jsonArray string amb el JSON array
     * @return llista de Maps
     */
    private List<Map<String, Object>> parseJsonArray(String jsonArray) {
        List<Map<String, Object>> result = new ArrayList<>();

        String trimmed = jsonArray.trim();
        if (!trimmed.startsWith("[") || !trimmed.endsWith("]")) {
            return result;
        }

        String content = trimmed.substring(1, trimmed.length() - 1).trim();
        if (content.isEmpty())
            return result;

        List<String> objectes = splitJsonObjects(content);
        for (String obj : objectes) {
            result.add(parseJsonObject(obj));
        }

        return result;
    }

    /**
     * Parseja un objecte JSON a Map
     * 
     * @param jsonObj string amb l'objecte JSON
     * @return Map amb les dades
     */
    private Map<String, Object> parseJsonObject(String jsonObj) {
        Map<String, Object> map = new LinkedHashMap<>();

        String trimmed = jsonObj.trim();
        if (!trimmed.startsWith("{") || !trimmed.endsWith("}")) {
            return map;
        }

        String content = trimmed.substring(1, trimmed.length() - 1);

        // Parseja clau-valor
        int i = 0;
        while (i < content.length()) {
            // Trobar la clau
            int keyStart = content.indexOf('"', i);
            if (keyStart == -1)
                break;
            int keyEnd = content.indexOf('"', keyStart + 1);
            String key = content.substring(keyStart + 1, keyEnd);

            // Trobar el valor
            int colonIndex = content.indexOf(':', keyEnd);
            i = colonIndex + 1;

            // Determinar tipus de valor
            while (i < content.length() && Character.isWhitespace(content.charAt(i)))
                i++;

            Object value;
            if (i < content.length() && content.charAt(i) == '"') {
                // String
                int valEnd = content.indexOf('"', i + 1);
                value = content.substring(i + 1, valEnd);
                i = valEnd + 1;
            } else if (i < content.length() && content.charAt(i) == '[') {
                // Array
                int arrEnd = findMatchingBracket(content, i);
                String arrayContent = content.substring(i, arrEnd + 1);
                value = parseArray(arrayContent);
                i = arrEnd + 1;
            } else if (i < content.length() && (content.charAt(i) == 'n')) {
                // null
                value = null;
                i = content.indexOf(',', i);
                if (i == -1)
                    i = content.length();
            } else {
                // Number
                int commaIndex = content.indexOf(',', i);
                if (commaIndex == -1)
                    commaIndex = content.length();
                String numStr = content.substring(i, commaIndex).trim();
                try {
                    value = Integer.parseInt(numStr);
                } catch (NumberFormatException e) {
                    value = numStr;
                }
                i = commaIndex;
            }

            map.put(key, value);

            // Avançar fins la següent clau
            int nextComma = content.indexOf(',', i);
            if (nextComma == -1)
                break;
            i = nextComma + 1;
        }

        return map;
    }

    /**
     * Parseja un array JSON
     * 
     * @param arrayStr string amb l'array
     * @return List d'objectes
     */
    private List<Object> parseArray(String arrayStr) {
        List<Object> result = new ArrayList<>();

        String trimmed = arrayStr.trim();
        if (!trimmed.startsWith("[") || !trimmed.endsWith("]")) {
            return result;
        }

        String content = trimmed.substring(1, trimmed.length() - 1).trim();
        if (content.isEmpty())
            return result;

        // Si són objectes
        if (content.contains("{")) {
            List<String> objectes = splitJsonObjects(content);
            for (String obj : objectes) {
                result.add(parseJsonObject(obj));
            }
        } else {
            // Arrays simples de números
            String[] parts = content.split(",");
            for (String part : parts) {
                try {
                    result.add(Integer.parseInt(part.trim()));
                } catch (NumberFormatException e) {
                    result.add(part.trim());
                }
            }
        }

        return result;
    }

    /**
     * Converteix un Map a JSON string
     * 
     * @param map Map a convertir
     * @return string JSON
     */
    private String mapToJson(Map<String, Object> map) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");

        List<String> entries = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            String valueStr;
            if (value == null) {
                valueStr = "null";
            } else if (value instanceof String) {
                valueStr = "\"" + escapeJson((String) value) + "\"";
            } else if (value instanceof List) {
                valueStr = listToJson((List<?>) value);
            } else {
                valueStr = value.toString();
            }

            entries.add("    \"" + key + "\": " + valueStr);
        }

        json.append(String.join(",\n", entries));
        json.append("\n  }");

        return json.toString();
    }

    /**
     * Converteix una List a JSON array string
     * 
     * @param list llista a convertir
     * @return string JSON array
     */
    private String listToJson(List<?> list) {
        if (list.isEmpty())
            return "[]";

        if (list.get(0) instanceof Map) {
            // Array d'objectes
            StringBuilder json = new StringBuilder();
            json.append("[\n");
            for (int i = 0; i < list.size(); i++) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) list.get(i);
                String objJson = mapToJson(map);
                String[] lines = objJson.split("\n");
                for (String line : lines) {
                    json.append("      ").append(line).append("\n");
                }
                if (i < list.size() - 1)
                    json.append(",");
            }
            json.append("    ]");
            return json.toString();
        } else {
            // Array simple
            return list.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(", ", "[", "]"));
        }
    }

    /**
     * Separa objectes JSON dins d'un string
     * 
     * @param content contingut amb objectes
     * @return llista d'objectes individuals
     */
    private List<String> splitJsonObjects(String content) {
        List<String> objectes = new ArrayList<>();
        int depth = 0;
        int start = 0;

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c == '{') {
                if (depth == 0)
                    start = i;
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0) {
                    objectes.add(content.substring(start, i + 1));
                }
            }
        }

        return objectes;
    }

    /**
     * Troba el claudàtor que tanca un array
     * 
     * @param str       string on buscar
     * @param openIndex índex del caràcter d'obertura
     * @return índex del caràcter de tancament
     */
    private int findMatchingBracket(String str, int openIndex) {
        char openChar = str.charAt(openIndex);
        char closeChar = (openChar == '[') ? ']' : '}';
        int depth = 1;

        for (int i = openIndex + 1; i < str.length(); i++) {
            if (str.charAt(i) == openChar)
                depth++;
            else if (str.charAt(i) == closeChar) {
                depth--;
                if (depth == 0)
                    return i;
            }
        }
        return -1;
    }

    /**
     * Escapa caràcters especials per JSON
     * 
     * @param s string a escapar
     * @return string escapat
     */
    private String escapeJson(String s) {
        if (s == null)
            return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
