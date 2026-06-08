package model;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
/**
* Représente un projet dans TaskFlow.
* Un projet contient des colonnes Kanban, chaque colonne des tâches.
*/
public class Project implements Serializable {
private static final long serialVersionUID = 1L;
private final String id;
private String name;
private String description;
private final LocalDateTime createdAt;
private final List<Column> columns;
public Project(String name, String description) {
this.id = UUID.randomUUID().toString();
this.name = name;
this.description = description;
this.createdAt = LocalDateTime.now();
this.columns = new ArrayList<>();
initDefaultColumns();
}
private void initDefaultColumns() {
columns.add(new Column("À faire", "#E6F1FB"));
columns.add(new Column("En cours", "#FAEEDA"));
columns.add(new Column("Terminé", "#EAF3DE"));
}
// --- Gestion des colonnes ---
public void addColumn(Column column) {
if (column != null) columns.add(column);
}
// --- Recherche cross-colonnes ---
public Task findTaskById(String taskId) {
for (Column col : columns) {
Task found = col.findById(taskId);
if (found != null) return found;
}
return null;
}
public Column findColumnOfTask(Task task) {
for (Column col : columns) {
if (col.getTasks().contains(task)) return col;
}
return null;
}
// --- Statistiques globales ---
public int getTotalTaskCount() {
int total = 0;
for (Column col : columns) {
total += col.getTaskCount();
}
return total;
}
public double getCompletionRate() {
int total = getTotalTaskCount();
if (total == 0) return 0.0;
// Dernière colonne = "Terminé" par convention
Column done = columns.get(columns.size() - 1);
return (done.getTaskCount() * 100.0) / total;
}
// --- Getters & Setters ---
public String getId() { return id; }
public String getName() { return name; }
public void setName(String name) { this.name = name; }
public String getDescription() { return description; }
public void setDescription(String d){ this.description = d; }
public LocalDateTime getCreatedAt() { return createdAt; }
public List<Column> getColumns() {
return Collections.unmodifiableList(columns);
}
@Override
public String toString() {
return "Project{" + name + ", cols=" + columns.size() + "}";
}
}