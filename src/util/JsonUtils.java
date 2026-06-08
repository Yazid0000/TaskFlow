package util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonPrimitive;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
* Fournit une instance Gson configurée pour tout le projet.
* Gère la sérialisation des types Java 8 (LocalDate, LocalDateTime).
*/
public class JsonUtils {
// Instance unique créée une seule fois (pattern Singleton light)
private static final Gson GSON = buildGson();
// Constructeur privé : on n'instancie jamais cette classe
private JsonUtils() {}
private static Gson buildGson() {
return new GsonBuilder()
// Sérialiser LocalDate → "2025-06-15"
.registerTypeAdapter(LocalDate.class,
(JsonSerializer<LocalDate>) (src, type, ctx) ->
new JsonPrimitive(src.toString()))
// Désérialiser "2025-06-15" → LocalDate
.registerTypeAdapter(LocalDate.class,
(JsonDeserializer<LocalDate>) (json, type, ctx) ->
LocalDate.parse(json.getAsString()))
// Sérialiser LocalDateTime → "2025-06-15T14:30:00"
.registerTypeAdapter(LocalDateTime.class,
(JsonSerializer<LocalDateTime>) (src, type, ctx) ->
new JsonPrimitive(src.toString()))
// Désérialiser "2025-06-15T14:30:00" → LocalDateTime
.registerTypeAdapter(LocalDateTime.class,
(JsonDeserializer<LocalDateTime>) (json, type, ctx) ->
LocalDateTime.parse(json.getAsString()))
.setPrettyPrinting() // JSON lisible avec indentation
.create();
}
// --- API publique ---
public static String toJson(Object obj) {
return GSON.toJson(obj);
}
public static <T> T fromJson(String json, Class<T> classOfT) {
return GSON.fromJson(json, classOfT);
}
public static Gson get() {
return GSON;
}
}