package controller;
import model.Project;
import model.Task;
/**
* Interface Observer du pattern Observer.
* Toute classe voulant réagir aux changements de données
* doit implémenter cette interface.
*/
public interface ProjectListener {
/** Appelé quand la liste des projets change (ajout/suppression) */
void onProjectListChanged();
/** Appelé quand un projet est sélectionné dans la sidebar */
void onProjectSelected(Project project);
/** Appelé quand les tâches d'un projet sont modifiées */
void onTaskChanged(Project project, Task task);
}