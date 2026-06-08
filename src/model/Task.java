package model;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
/**
* Représente une tâche dans le tableau Kanban.
* C'est la classe centrale du modèle de TaskFlow.
*/
public class Task implements Serializable {
private static final long serialVersionUID = 1L;
// --- Identité ---
private final String id; // Identifiant unique, jamais modifiable
private String title;
private String description;
// --- Métadonnées ---
private Priority priority; // Utilisation de notre enum
private LocalDate dueDate; // Date d'échéance (peut être null)
private LocalDateTime createdAt;
private LocalDateTime updatedAt;
// --- Composition : une tâche POSSÈDE une pièce jointe ---
private Attachment attachment; // null si aucune pièce jointe
// --- Constructeur principal ---
public Task(String title, String description, Priority priority) {
this.id = UUID.randomUUID().toString();
this.title = title;
this.description = description;
this.priority = priority;
this.createdAt = LocalDateTime.now();
this.updatedAt = LocalDateTime.now();
this.attachment = null;
}
// --- Méthode métier : marquer comme mis à jour ---
public void touch() {
this.updatedAt = LocalDateTime.now();
}
// --- Méthode métier : vérifier si en retard ---
public boolean isOverdue() {
return dueDate != null && dueDate.isBefore(LocalDate.now());
}
// --- Méthode métier : attacher une pièce jointe ---
public void setAttachment(Attachment attachment) {
this.attachment = attachment;
touch(); // Mise à jour automatique du timestamp
}
public boolean hasAttachment() {
return attachment != null;
}
// --- Getters ---
public String getId() { return id; }
public String getTitle() { return title; }
public String getDescription() { return description; }
public Priority getPriority() { return priority; }
public LocalDate getDueDate() { return dueDate; }
public LocalDateTime getCreatedAt() { return createdAt; }
public LocalDateTime getUpdatedAt() { return updatedAt; }
public Attachment getAttachment() { return attachment; }
// --- Setters (id exclu : immuable) ---
public void setTitle(String title) { this.title = title; touch(); }
public void setDescription(String description) { this.description = description; touch(); }
public void setPriority(Priority priority) { this.priority = priority; touch(); }
public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; touch(); }
@Override
public String toString() {
return "Task{" + title + ", " + priority + "}";
}
}