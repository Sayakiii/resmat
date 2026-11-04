package fr.resmat.repository;

import fr.resmat.entity.Emprunt;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmpruntRepository extends JpaRepository<Emprunt, Long> {

    /** Emprunts en cours : pas encore rendus. */
    List<Emprunt> findByDateRetourEffectiveIsNull();

    List<Emprunt> findByReservationUtilisateurId(Long utilisateurId);

    /** Emprunts en retard : retour prévu dépassé et non rendus. */
    @Query("select e from Emprunt e where e.dateRetourEffective is null and e.dateRetourPrevue < :aujourdhui")
    List<Emprunt> findEnRetard(@Param("aujourdhui") LocalDate aujourdhui);
}
