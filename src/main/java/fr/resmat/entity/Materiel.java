package fr.resmat.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "materiel")
public class Materiel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private int quantiteDisponible;

    /** Durée maximale d'emprunt en jours (facultative). */
    private Integer dureeMaxJours;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutMateriel statut = StatutMateriel.DISPONIBLE;

    @ManyToOne(optional = false)
    @JoinColumn(name = "type_materiel_id")
    private TypeMateriel typeMateriel;

    @OneToMany(mappedBy = "materiel")
    private List<Reservation> reservations = new ArrayList<>();

    public Materiel() {}

    public Materiel(String nom, String description, int quantiteDisponible, Integer dureeMaxJours,
                    StatutMateriel statut, TypeMateriel typeMateriel) {
        this.nom = nom; this.description = description; this.quantiteDisponible = quantiteDisponible;
        this.dureeMaxJours = dureeMaxJours; this.statut = statut; this.typeMateriel = typeMateriel;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getQuantiteDisponible() { return quantiteDisponible; }
    public void setQuantiteDisponible(int quantiteDisponible) { this.quantiteDisponible = quantiteDisponible; }
    public Integer getDureeMaxJours() { return dureeMaxJours; }
    public void setDureeMaxJours(Integer dureeMaxJours) { this.dureeMaxJours = dureeMaxJours; }
    public StatutMateriel getStatut() { return statut; }
    public void setStatut(StatutMateriel statut) { this.statut = statut; }
    public TypeMateriel getTypeMateriel() { return typeMateriel; }
    public void setTypeMateriel(TypeMateriel typeMateriel) { this.typeMateriel = typeMateriel; }
    public List<Reservation> getReservations() { return reservations; }
    public void setReservations(List<Reservation> reservations) { this.reservations = reservations; }
}
