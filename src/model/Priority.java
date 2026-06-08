package model;
/**
* Niveaux de priorité d'une tâche.
* Chaque constante porte un libellé affiché dans l'interface
* et une couleur hexadécimale pour la vue.
*/
public enum Priority {
LOW("Faible", "#1D9E75"),
MEDIUM("Moyenne", "#EF9F27"),
HIGH("Haute", "#D85A30"),
CRITICAL("Critique", "#A32D2D");
private final String label;
private final String color;
// Constructeur privé (obligatoire pour les enums avec champs)
Priority(String label, String color) {
this.label = label;
this.color = color;
}
public String getLabel() { return label; }
public String getColor() { return color; }
@Override
public String toString() { return label; }
}