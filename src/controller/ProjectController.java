package controller;
import model.Project;
import model.Task;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
* Contrôleur principal : gère les projets et notifie les observateurs.
* Implémente le pattern Singleton + Observer.
*/
public class ProjectController {
// ---- Singleton ----
private static ProjectController instance;
public static ProjectController getInstance() {
if (instance == null) instance = new ProjectController();
return instance;
}
// ---- État interne ----
private final List<Project> projects = new ArrayList<>();
private final List<ProjectListener> listeners = new ArrayList<>();
private Project currentProject;
private ProjectController() {}
// ========== Gestion des observateurs ==========
public void addListener(ProjectListener l) {
if (!listeners.contains(l)) listeners.add(l);
}
public void removeListener(ProjectListener l) {
listeners.remove(l);
}
// Notifie tous les observateurs : liste changée
private void fireProjectListChanged() {
for (ProjectListener l : listeners) l.onProjectListChanged();
}
// Notifie tous les observateurs : projet sélectionné
private void fireProjectSelected(Project p) {
for (ProjectListener l : listeners) l.onProjectSelected(p);
}
// Notifie tous les observateurs : tâche modifiée
void fireTaskChanged(Project p, Task t) {
for (ProjectListener l : listeners) l.onTaskChanged(p, t);
}
// ========== CRUD Projets ==========
public Project createProject(String name, String description) {
Project p = new Project(name, description);
projects.add(p);
saveAll();
fireProjectListChanged(); // ← notifie la vue
return p;
}
public void updateProject(Project p, String name, String desc) {
p.setName(name);
p.setDescription(desc);
saveAll();
fireProjectListChanged();
}
public void deleteProject(Project p) {
projects.remove(p);
if (currentProject == p) currentProject = null;
saveAll();
fireProjectListChanged();
}
public void selectProject(Project p) {
this.currentProject = p;
fireProjectSelected(p); // ← le Kanban va se rafraîchir
}
// ========== Persistance ==========
public void loadAll() {
List<Project> saved = PersistenceManager.getInstance().load();
projects.clear();
projects.addAll(saved);
fireProjectListChanged();
}
public void saveAll() {
PersistenceManager.getInstance().save(projects);
}
// ========== Getters ==========
public List<Project> getProjects() {
return Collections.unmodifiableList(projects);
}
public Project getCurrentProject() { return currentProject; }
}