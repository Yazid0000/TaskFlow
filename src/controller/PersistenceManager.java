package controller;
import com.google.gson.reflect.TypeToken;
import model.Project;
import util.JsonUtils;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
/**
* Gère la sauvegarde et le chargement des projets en JSON.
* Implémente le pattern Singleton : une seule instance dans l'app.
*/
public class PersistenceManager {
// Chemin du fichier de sauvegarde dans le dossier data/
private static final String DATA_FILE = "data/taskflow_data.json";
// ---- Pattern Singleton ----
private static PersistenceManager instance;
private PersistenceManager() {
ensureDataFolderExists();
}
public static PersistenceManager getInstance() {
if (instance == null) {
instance = new PersistenceManager();
}
return instance;
}
// ---- Sauvegarde ----
public void save(List<Project> projects) {
try {
String json = JsonUtils.toJson(projects);
Files.write(
Paths.get(DATA_FILE),
json.getBytes(StandardCharsets.UTF_8),
StandardOpenOption.CREATE,
StandardOpenOption.TRUNCATE_EXISTING
);
System.out.println("[PersistenceManager] Sauvegarde OK : " + DATA_FILE);
} catch (IOException e) {
System.err.println("[PersistenceManager] Erreur sauvegarde : " + e.getMessage());
}
}
// ---- Chargement ----
public List<Project> load() {
Path path = Paths.get(DATA_FILE);
if (!Files.exists(path)) {
System.out.println("[PersistenceManager] Aucune sauvegarde trouvée.");
return new ArrayList<>(); // Premier démarrage
}
try {
String json = new String(
Files.readAllBytes(path),
StandardCharsets.UTF_8
);
// Type générique : List<Project> (Gson en a besoin)
Type listType = new TypeToken<List<Project>>(){}.getType();
List<Project> projects = JsonUtils.get().fromJson(json, listType);
System.out.println("[PersistenceManager] " + projects.size() + " projet(s) chargé(s).");
return projects;
} catch (IOException e) {
System.err.println("[PersistenceManager] Erreur chargement : " + e.getMessage());
return new ArrayList<>();
}
}
// ---- Utilitaire : crée le dossier data/ s'il n'existe pas ----
private void ensureDataFolderExists() {
File dir = new File("data");
if (!dir.exists()) {
dir.mkdirs();
System.out.println("[PersistenceManager] Dossier data/ créé.");
}
}
// ---- Vérification d'existence du fichier ----
public boolean hasSaveFile() {
return Files.exists(Paths.get(DATA_FILE));
}
}