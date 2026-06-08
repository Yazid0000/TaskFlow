package model;
import java.io.Serializable;
import java.time.LocalDateTime;
/**
* Représente une pièce jointe (image) liée à une tâche.
* Stocke le chemin absolu du fichier sur le disque.
*/
public class Attachment implements Serializable {
private static final long serialVersionUID = 1L;
private String fileName; // "photo_vacances.jpg"
private String filePath; // Chemin absolu sur le disque
private LocalDateTime addedAt; // Date d'ajout
// --- Constructeur ---
public Attachment(String fileName, String filePath) {
this.fileName = fileName;
this.filePath = filePath;
this.addedAt = LocalDateTime.now();
}
// --- Getters & Setters ---
public String getFileName() { return fileName; }
public void setFileName(String fileName) { this.fileName = fileName; }
public String getFilePath() { return filePath; }
public void setFilePath(String filePath) { this.filePath = filePath; }
public LocalDateTime getAddedAt() { return addedAt; }
@Override
public String toString() {
return "Attachment{" + fileName + "}";
}
}