package edu.upc.prop.cluster422.Dades;

import java.io.*;
import java.nio.file.*;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

import edu.upc.prop.cluster422.Domini.Classes.Usuari;
import edu.upc.prop.cluster422.Domini.Classes.Administrador;
import edu.upc.prop.cluster422.Domini.Controladors.CustomException;

import javax.security.auth.login.CredentialException;

/**
 * Aquesta clase és el gestor de la persistencia dels usuaris.
 * Guarda usuaris en JSON individuals i el seu llistat en un CSV.
 * @author Ramon Sanchez Torra
 * @version 14/12/2025/1
 */
public class GestorUsuaris {

    /**
     * Instancia unica del GestorUsuaris per complir amb el patro singleton
     */
    private static GestorUsuaris instance;
    /**
     * Path relatiu cap a la carpeta d'Usuaris
     */
    private final Path path_usuaris;
    /**
     * Path relatiu cap al llistat CSV d'usuaris
     */
    private final Path path_llistat;

    /**
     * Creadora privada del GestorUsuaris
     * @param path_data el path del directori per guardar dades
     * @throws FileSystemNotFoundException si salta algun error en crear el direcori de dades
     */
    private GestorUsuaris(Path path_data) throws FileSystemNotFoundException{
        path_usuaris = path_data.resolve("Usuaris");
        path_llistat = path_usuaris.resolve("llistat.csv");
        try {
            if (!Files.exists(path_usuaris)) Files.createDirectories(path_usuaris);
            if (!Files.exists(path_llistat)) {
                Files.createFile(path_llistat);
                try {
                    crearUsuari(new Administrador("admin", "admin", "Developer", 0));
                } catch (CustomException | IOException ignored) {
                }
            }
        } catch (IOException e) {
            throw new FileSystemNotFoundException("Error al crear el gestor perque: " + e.getMessage());
        }
    }

    /**
     * getter de l'instancia unica del GestorUsuaris
     * @param path_data path indicat pel CtrlPersistencia que indica el path del directori per guardar dades
     * @return l'instancia unica del GestorUsuaris
     * @throws FileSystemNotFoundException si salta algun error en crear el direcori de dades
     */
    public static GestorUsuaris getInstance(Path path_data) throws FileSystemNotFoundException {
        if (instance == null) instance = new GestorUsuaris(path_data);
        return instance;
    }

    /**
     * Crea el fitxer per guardar un nou usuari, tambe actualitza el llistat SCV
     * @param user nou usuari a guardar
     * @throws IOException si salta algun errror creant els fitxers
     * @throws CustomException si el nom d'usuari ja esta en us
     */
    public void crearUsuari(Usuari user) throws IOException, CustomException {
        Map<String, Integer> llistat = llegirLlistatCSV();
        if (llistat.containsKey(user.getUsername())) throw new CustomException("El nom de l'usuari ja esta en us.");
        int nouId = llistat.values().stream().max(Integer::compare).orElse(0) + 1;
        guardarUsuariEnJson(user, nouId);
        afegirAlLlistatCSV(user.getUsername(), nouId);
    }

    /**
     * Retorna una instancia de la clase usuari amb les totes les de l'usuari
     * @param nomusuari usuari a buscar
     * @return usuari amb les dades
     * @throws IOException si salta algun error llegint fitxers
     * @throws NoSuchElementException si no s'ha trobat l'usuari
     */
    public Usuari getPrivateUsuari(String nomusuari) throws IOException, NoSuchElementException {
        Map<String, Integer> llistat = llegirLlistatCSV();
        if (!llistat.containsKey(nomusuari)) throw new NoSuchElementException("L'usuari no existeix.");
        int id = llistat.get(nomusuari);
        return carregarUsuariDesdeJson(id);
    }
    /**
     * Retorna una instancia de la clase usuari amb les totes les de l'usuari
     * @param idusuari id de l'usuari a buscar
     * @return usuari amb les dades
     * @throws IOException si salta algun error llegint fitxers
     * @throws NoSuchElementException si no s'ha trobat l'usuari
     */
    public Usuari getPrivateUsuariById(int idusuari) throws IOException, NoSuchElementException {
        Map<Integer, String> llistat = llegirLlistatCSVinvers();
        if (!llistat.containsKey(idusuari)) throw new NoSuchElementException("L'usuari no existeix.");
        return carregarUsuariDesdeJson(idusuari);
    }
    /**
     * Retorna una instancia de la clase usuari amb les dades publiques de l'usuari
     * @param nomusuari usuari a buscar
     * @return usuari amb les dades publiques
     * @throws IOException si salta algun error llegint fitxers
     * @throws NoSuchElementException si no s'ha trobat l'usuari
     */
    public Usuari getPublicUsuari(String nomusuari) throws IOException, NoSuchElementException {
        Usuari user = getPrivateUsuari(nomusuari);
        if (user != null) {
            user.changeContrasenya("private");
            Set<Integer> tem;
            user.setRespostescreades(new TreeSet<Integer>());
            return user;
        }
        throw new NoSuchElementException("L'usuari no existeix.");
    }

    /**
     * Retorna un usuari si les credencials son correctes
     * @param nom nom de l'usuari que es busca
     * @param contrasenya contrasenya de l'usuari a buscar
     * @return Usuari de les credencials
     * @throws IOException si salta algun error llegint fitxers
     * @throws NoSuchElementException si l'usuari no existeix
     * @throws CredentialException si la contrasenya no correspon a la de l'usuari
     */
    public Usuari logIn(String nom, String contrasenya) throws IOException, NoSuchElementException, CredentialException {
        Usuari user = getPrivateUsuari(nom);
        if (user != null && user.getContrasenya() != null && user.getContrasenya().equals(contrasenya)) return user;
        else throw new CredentialException("Contrasenya incorrecta.");
    }

    /**
     * Actualitza l'usuari passat com a parametre.
     * @param user usuari que es vol actualitzar les dades
     * @throws IOException si salta algun error modificant els fitchers
     * @throws CustomException si el nom d'usuari ja esta en us
     */
    public void updateUsuari(Usuari user) throws IOException, CustomException {
        modificarAlLlistatCSV(user.getUsername(), user.getId());
        guardarUsuariEnJson(user, user.getId());
    }

    /**
     * Esborra les dades d'un usuari i modifica el llistat CSV
     * @param id id de l'usuari a esborrar
     * @throws IOException si salta algun error actualitzant el llistat CSV
     * @throws InvalidParameterException si l'id no s'ha trobat
     */
    public void deleteUsuari(int id) throws IOException, InvalidParameterException {
        Path jsonPath = path_usuaris.resolve("usuari_" + id + ".json");
        if(!Files.deleteIfExists(jsonPath)) throw new InvalidParameterException("No s'ha trovat l'usuari amb id: "+id);
        Path temp = Files.createTempFile(path_usuaris, "llistat_temp", ".csv");
        try (BufferedReader reader = Files.newBufferedReader(path_llistat);
             BufferedWriter writer = Files.newBufferedWriter(temp, StandardOpenOption.TRUNCATE_EXISTING)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    try {
                        int currentId = Integer.parseInt(parts[1].trim());
                        if (currentId != id) {
                            writer.write(line);
                            writer.newLine();
                        }
                    } catch (NumberFormatException nfe) {
                        writer.write(line);
                        writer.newLine();
                    }
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }
        Files.move(temp, path_llistat, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Retorna una llista de noms corresponents als ids donats (mateix ordre).
     * @param ids ids dels noms que es vol conseguir
     * @return llista dels noms
     * @throws IOException si salta algun error llegint el fitxer
     * @throws InvalidParameterException si un dels ids no s'ha trobat
     */
    public ArrayList<String> getnomsUsuarus(List<Integer> ids) throws IOException, InvalidParameterException {
        Map<Integer,String> llistat = llegirLlistatCSVinvers();
        ArrayList<String> noms = new ArrayList<>();
        for (int id : ids) {
            String nom = llistat.get(id);
            if (nom == null) throw new InvalidParameterException("No s'ha trobat el nom de "+ id);
            noms.add(nom);
        }
        return noms;
    }

    /**
     * Esborra una enquesta creada de la llista de enquestes creades d'un usuari
     * @param idusuari usuari que es vol modificar
     * @param idenquesta enquesta que es vol esborrar
     * @throws IOException si no s'ha modificat correctament el fitxer de l'usuari
     * @throws NoSuchElementException si l'usuari no ha creat l'enquesta
     */
    public void deleteEnquestaUsuari(int idusuari, int idenquesta) throws IOException, NoSuchElementException {
        Usuari u = carregarUsuariDesdeJson(idusuari);
        u.deleteEnquestaCreada(idenquesta);
        guardarUsuariEnJson(u, idusuari);
    }

    /**
     * Esborra una resposta de la llista de respostes d'un usuari
     * @param idusuari usuari que es vol modificar
     * @param idresposta resposta que es vol esborrar
     * @throws IOException si no s'ha modificat correctament el fitxer de l'usuari
     * @throws NoSuchElementException si l'usuari no ha respost l'enquesta
     */
    public void deleteRespostaUsuari(int idusuari, int idresposta) throws IOException, NoSuchElementException {
        Usuari u = carregarUsuariDesdeJson(idusuari);
        u.deleteRespostacreada(idresposta);
        guardarUsuariEnJson(u, idusuari);
    }

    /**
     * Crea un mapa dels valors del llistat CSV
     * @return mapa amb id associat a cada String de cada valor del CSV
     * @throws IOException si salta algun error llegint al fitxer
     */
    private Map<String, Integer> llegirLlistatCSV() throws IOException {
        Map<String, Integer> map = new HashMap<>();
        if (!Files.exists(path_llistat)) return map;
        List<String> lines = Files.readAllLines(path_llistat);
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 2) {
                try {
                    map.put(parts[0].trim(), Integer.parseInt(parts[1].trim()));
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return map;
    }

    /**
     * Crea un mapa dels valors invertits del llistat CSV
     * @return mapa amb String associat a id de cada valor del CSV
     * @throws IOException si salta algun error llegint al fitxer
     */
    private Map<Integer,String> llegirLlistatCSVinvers() throws IOException {
        Map<Integer, String> map = new HashMap<>();
        if (!Files.exists(path_llistat)) return map;
        List<String> lines = Files.readAllLines(path_llistat);
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 2) {
                try {
                    map.put(Integer.parseInt(parts[1].trim()),parts[0].trim());
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return map;
    }

    /**
     * Afegir un nou usuari al llistat CSV
     * @param nom al nom del nou usuari
     * @param id l'id asossiat al nom
     * @throws IOException si salta algun error actualitzant el fitxer
     */
    private void afegirAlLlistatCSV(String nom, int id) throws IOException {
        String linia = nom + "," + id + System.lineSeparator();
        Files.write(path_llistat, linia.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    /**
     * Actualitza el nom associat a un id del llistat CSV
     * @param nom Nou nom associat a l'id
     * @param id id que es vol actualitzar
     * @throws IOException Si salta algun error actualitzant el fitcher
     * @throws CustomException Si el nom ja esta en us per un altre id
     */
    private void modificarAlLlistatCSV(String nom, int id) throws IOException, CustomException {
        Path temp = Files.createTempFile(path_usuaris, "llistat_mod", ".csv");
        try (BufferedReader reader = Files.newBufferedReader(path_llistat);
             BufferedWriter writer = Files.newBufferedWriter(temp, StandardOpenOption.TRUNCATE_EXISTING)) {
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                try {
                    int currentId = Integer.parseInt(parts[1].trim());
                    if (currentId == id) {
                        writer.write(nom + "," + id);
                        writer.newLine();
                        found = true;
                    } else {
                        if(nom.equals(parts[0].trim())) {
                            Files.deleteIfExists(temp);
                            throw new CustomException("El nom d'usuari ja esta en us");
                        }
                        writer.write(line);
                        writer.newLine();
                    }
                } catch (NumberFormatException ignored) {
                }

            }
            if (!found) {
                writer.write(nom + "," + id);
                writer.newLine();
            }
        }
        Files.move(temp, path_llistat, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * guarda un usuari com a fitcher Json
     * @param user usuari a gurdar
     * @param id identificador assignat pel gestor a l'usuari
     * @throws IOException si salta algun error en crear el ficher
     */
    private void guardarUsuariEnJson(Usuari user, int id) throws IOException {
        Path path = path_usuaris.resolve("usuari_" + id + ".json");
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"type\": \"").append(user instanceof Administrador ? "ADMIN" : "USER").append("\",\n");
        json.append("  \"id\": ").append(id).append(",\n");
        json.append("  \"nom\": \"").append(escapeJson(user.getUsername())).append("\",\n");
        json.append("  \"password\": \"").append(escapeJson(user.getContrasenya())).append("\",\n");
        String email = user.getEmail();
        if (email == null) email = "";
        json.append("  \"email\": \"").append(escapeJson(email)).append("\",\n");
        json.append("  \"edat\": ").append(user.getEdat()).append(",\n");
        if (user instanceof Administrador) {
            json.append("  \"acreditador\": \"").append(escapeJson(((Administrador) user).getAcreditador())).append("\",\n");
        }
        Integer[] creadesArr = user.getEnquestesCreadesTitol();
        Integer[] respostesArr = user.getRespostesCreadesTitol();
        json.append("  \"creades\": ").append(lListToString(creadesArr)).append(",\n");
        json.append("  \"respostes\": ").append(lListToString(respostesArr)).append("\n");
        json.append("}");
        Files.write(path, json.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * Transforma un Integer[] a un String
     * @param arr llista d'integers a transformar
     * @return retorna un String amb els valors de arr
     */
    private String lListToString(Integer[] arr) {
        if (arr == null || arr.length == 0) return "[]";
        return Arrays.stream(arr).map(String::valueOf).collect(Collectors.joining(", ", "[", "]"));
    }

    /**
     * Busca un usuari en el directori de dades i retorna una instancia de la clase Usuari amb les seves dades
     * @param id identificador de l'usuari
     * @return retorna l'usuari correspondent a l'identificador
     * @throws IOException si salta algun error en llegir el json
     */
    private Usuari carregarUsuariDesdeJson(int id) throws IOException {
        Path path = path_usuaris.resolve("usuari_" + id + ".json");
        if (!Files.exists(path)) throw new FileNotFoundException("Usuari " + id + " no trobat.");
        String content = Files.readString(path);
        boolean isAdmin = content.contains("\"type\": \"ADMIN\"");
        String nom = extreureValor(content, "nom");
        String pass = extreureValor(content, "password");
        String email = extreureValor(content, "email");
        int edat = 0;
        String edatStr = extreureValor(content, "edat");
        if (!edatStr.isEmpty()) {
            try {
                edat = Integer.parseInt(edatStr);
            } catch (NumberFormatException ignored) {
            }
        }

        Usuari u;
        if (isAdmin) {
            String acreditador = extreureValor(content, "acreditador");
            u = new Administrador(nom, pass, acreditador, id);
        }
        else u = new Usuari(nom, pass, id);
        if (!email.isEmpty()) {
            u.setEmail(email);
        }
        u.setEdat(edat);
        List<Integer> creades = extreureLlista(content, "creades");
        u.setEnquestescreades(new TreeSet<>(creades));
        List<Integer> respostes = extreureLlista(content, "respostes");
        u.setRespostescreades(new TreeSet<>(respostes));
        return u;
    }

    /**
     * Converteix un valor del json a un String
     * @param json Json on buscar el valor
     * @param key nom del valor del json a transformar
     * @return retorna el valor buscat com a String
     */
    private String extreureValor(String json, String key) {
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
     * Converteix una llista del json a una List<Integer>
     * @param json Json on buscar la llista
     * @param key nom de la llista del json a transformar
     * @return retorna la llista com a List<Integer>
     */
    private List<Integer> extreureLlista(String json, String key) {
        List<Integer> list = new ArrayList<>();
        int index = json.indexOf("\"" + key + "\":");
        if (index == -1) return list;
        int startBracket = json.indexOf('[', index);
        int endBracket = json.indexOf(']', startBracket);
        if (startBracket == -1 || endBracket == -1) return list;
        String arrayContent = json.substring(startBracket + 1, endBracket).trim();
        if (!arrayContent.isEmpty()) {
            String[] nums = arrayContent.split(",");
            for (String n : nums) {
                try {
                    list.add(Integer.parseInt(n.trim()));
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return list;
    }

    /**
     * Modifica un String per poder guardarlo al json
     * @param s String a modifica
     * @return String modificat
     */
    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

}
