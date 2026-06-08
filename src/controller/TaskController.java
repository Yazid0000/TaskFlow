package controller;
import model.*;
import java.time.LocalDate;
/**
* Gère les opérations CRUD sur les tâches.
* Délègue la notification au ProjectController.
*/
public class TaskController {
// ---- Singleton ----
private static TaskController instance;
public static TaskController getInstance() {
if (instance == null) instance = new TaskController();
return instance;
}
private TaskController() {}
// Référence vers le ProjectController pour notifier
private final ProjectController pc = ProjectController.getInstance();
// ========== Ajout d'une tâche ==========
public Task addTask(Project project, Column column,
String title, String desc, Priority priority) {
Task task = new Task(title, desc, priority);
column.addTask(task);
pc.saveAll();
pc.fireTaskChanged(project, task); // notifie la vue
return task;
}
// ========== Modification d'une tâche ==========
public void updateTask(Project project, Task task,
String title, String desc,
Priority priority, LocalDate dueDate) {
task.setTitle(title);
task.setDescription(desc);
task.setPriority(priority);
task.setDueDate(dueDate);
// touch() est appelé automatiquement par chaque setter
pc.saveAll();
pc.fireTaskChanged(project, task);
}
// ========== Suppression d'une tâche ==========
public void deleteTask(Project project, Task task) {
Column col = project.findColumnOfTask(task);
if (col != null) {
col.removeTask(task);
pc.saveAll();
pc.fireTaskChanged(project, task);
}
}
// ========== Déplacement entre colonnes ==========
public void moveTask(Project project, Task task, Column target) {
// 1. Trouver la colonne source
Column source = project.findColumnOfTask(task);
// 2. Gardes : source existe et est différente de la cible
if (source == null || source == target) return;
// 3. Déplacer : retirer de la source, ajouter dans la cible
source.removeTask(task);
target.addTask(task);
task.touch(); // met à jour updatedAt
// 4. Sauvegarder et notifier
pc.saveAll();
pc.fireTaskChanged(project, task);
}
// ========== Pièce jointe ==========
public void attachFile(Project project, Task task,
String fileName, String filePath) {
Attachment att = new Attachment(fileName, filePath);
task.setAttachment(att); // touch() appelé automatiquement
pc.saveAll();
pc.fireTaskChanged(project, task);
}
}