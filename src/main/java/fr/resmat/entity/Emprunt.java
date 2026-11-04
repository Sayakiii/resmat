package fr.resmat.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "emprunt")
public class Emprunt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "reservation_id", unique = true)
    private Reservation reservation;

    @Column(nullable = false)
    private LocalDate dateRemise;

    @Column(nullable = false)
    private LocalDate dateRetourPrevue;

    /** Renseignée par le gestionnaire au retour du matériel. */
    private LocalDate dateRetourEffective;

    @Column(length = 1000)
    private String remarque;

    public Emprunt() {}

    public Emprunt(Reservation reservation, LocalDate dateRemise, LocalDate dateRetourPrevue,
                   LocalDate dateRetourEffective, String remarque) {
        this.reservation = reservation; this.dateRemise = dateRemise; this.dateRetourPrevue = dateRetourPrevue;
        this.dateRetourEffective = dateRetourEffective; this.remarque = remarque;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Reservation getReservation() { return reservation; }
    public void setReservation(Reservation reservation) { this.reservation = reservation; }
    public LocalDate getDateRemise() { return dateRemise; }
    public void setDateRemise(LocalDate dateRemise) { this.dateRemise = dateRemise; }
    public LocalDate getDateRetourPrevue() { return dateRetourPrevue; }
    public void setDateRetourPrevue(LocalDate dateRetourPrevue) { this.dateRetourPrevue = dateRetourPrevue; }
    public LocalDate getDateRetourEffective() { return dateRetourEffective; }
    public void setDateRetourEffective(LocalDate dateRetourEffective) { this.dateRetourEffective = dateRetourEffective; }
    public String getRemarque() { return remarque; }
    public void setRemarque(String remarque) { this.remarque = remarque; }
}
