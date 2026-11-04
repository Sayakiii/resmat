package fr.resmat.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/** Catégorie de matériel (ordinateur, caméra, micro...). */
@Entity
@Table(name = "type_materiel")
public class TypeMateriel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String libelle;

    @OneToMany(mappedBy = "typeMateriel")
    private List<Materiel> materiels = new ArrayList<>();

    public TypeMateriel() {}

    public TypeMateriel(String libelle) { this.libelle = libelle; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    public List<Materiel> getMateriels() { return materiels; }
    public void setMateriels(List<Materiel> materiels) { this.materiels = materiels; }
}
