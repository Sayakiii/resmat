package fr.resmat.repository;

import fr.resmat.entity.Reservation;
import fr.resmat.entity.StatutReservation;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUtilisateurId(Long utilisateurId);

    List<Reservation> findByStatut(StatutReservation statut);

    /** Réservations acceptées d'un matériel qui chevauchent la période [debut, fin]. */
    @Query("select r from Reservation r where r.materiel.id = :materielId "
         + "and r.statut = fr.resmat.entity.StatutReservation.ACCEPTEE "
         + "and r.dateDebut <= :fin and r.dateFin >= :debut")
    List<Reservation> findAccepteesChevauchant(@Param("materielId") Long materielId,
                                               @Param("debut") LocalDate debut,
                                               @Param("fin") LocalDate fin);
}
