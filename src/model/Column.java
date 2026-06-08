package model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
* Représente une colonne du tableau Kanban (ex : "À faire").
* Contient une liste ordonnée de tâches.
*/
public class Column implements Serializable {
private static final long serialVersionUID = 1L;
private String name; // "À faire", "En cours"...
private String color; // Couleur d'en-tête de la colonne
private final List<Task> tasks; // Agrégation de tâches
public Column(String name, String color) {
this.name = name;
this.color = color;
this.tasks = new ArrayList<>(); // Liste vide à la création
}
// --- Méthodes métier ---
public void addTask(Task task) {
if (task != null && !tasks.contains(task)) {
tasks.add(task);
}
}
public boolean removeTask(Task task) {
return tasks.remove(task);
}
public Task findById(String id) {
for (Task t : tasks) {
if (t.getId().equals(id)) return t;
}
return null;
}
public int getTaskCount() { return tasks.size(); }
public boolean isEmpty() { return tasks.isEmpty(); }
// Retourne une vue non modifiable pour protéger la liste interne
public List<Task> getTasks() {
return Collections.unmodifiableList(tasks);
}
public String getName() { return name; }
public void setName(String name) { this.name = name; }
public String getColor() { return color; }
public void setColor(String color) { this.color = color; }
@Override
public String toString() {
return "Column{" + name + ", tasks=" + tasks.size() + "}";
}
}