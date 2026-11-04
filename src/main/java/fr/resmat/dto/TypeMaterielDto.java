package fr.resmat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TypeMaterielDto {

    private Long id;

    @NotBlank(message = "Le libellé est obligatoire.")
    @Size(max = 100, message = "Le libellé ne doit pas dépasser 100 caractères.")
    private String libelle = "";

    /** Nombre de matériels rattachés (lecture seule, pour l'affichage). */
    private long nbMateriels;

    public TypeMaterielDto() {}

    public TypeMaterielDto(Long id, String libelle, long nbMateriels) {
        this.id = id; this.libelle = libelle; this.nbMateriels = nbMateriels;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    public long getNbMateriels() { return nbMateriels; }
    public void setNbMateriels(long nbMateriels) { this.nbMateriels = nbMateriels; }
}
