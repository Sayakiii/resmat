package fr.resmat.repository;

import static org.assertj.core.api.Assertions.assertThat;

import fr.resmat.entity.*;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/** Vérifie que le schéma est créé et que les associations fonctionnent (base H2 en mémoire). */
@DataJpaTest
class RepositoriesTest {

    @Autowired UtilisateurRepository utilisateurs;
    @Autowired TypeMaterielRepository types;
    @Autowired MaterielRepository materiels;
    @Autowired ReservationRepository reservations;
    @Autowired EmpruntRepository emprunts;

    @Test
    void creationEtRelectureDeChaqueEntite() {
        Utilisateur u = utilisateurs.save(new Utilisateur("Nom", "Prenom", "test@resmat.fr", "x", Role.ROLE_USER));
        TypeMateriel t = types.save(new TypeMateriel("Caméra"));
        Materiel m = materiels.save(new Materiel("Canon", "desc", 2, 7, StatutMateriel.DISPONIBLE, t));
        Reservation r = reservations.save(new Reservation(u, m, LocalDate.now(), LocalDate.now().plusDays(3),
            StatutReservation.ACCEPTEE, null, null));
        Emprunt e = emprunts.save(new Emprunt(r, LocalDate.now(), LocalDate.now().plusDays(3), null, null));

        assertThat(utilisateurs.findByEmail("test@resmat.fr")).isPresent();
        assertThat(materiels.findByTypeMaterielId(t.getId())).hasSize(1);
        assertThat(reservations.findByUtilisateurId(u.getId())).hasSize(1);
        assertThat(emprunts.findById(e.getId()).orElseThrow().getReservation().getMateriel().getNom()).isEqualTo("Canon");
    }

    @Test
    void detectionDesReservationsAccepteesQuiSeChevauchent() {
        Utilisateur u = utilisateurs.save(new Utilisateur("N", "P", "a@b.fr", "x", Role.ROLE_USER));
        Materiel m = materiels.save(new Materiel("Micro", "d", 1, null, StatutMateriel.DISPONIBLE, types.save(new TypeMateriel("Micro"))));
        LocalDate d = LocalDate.now();
        reservations.save(new Reservation(u, m, d, d.plusDays(5), StatutReservation.ACCEPTEE, null, null));
        reservations.save(new Reservation(u, m, d, d.plusDays(5), StatutReservation.REFUSEE, null, "non"));

        assertThat(reservations.findAccepteesChevauchant(m.getId(), d.plusDays(2), d.plusDays(8))).hasSize(1);
        assertThat(reservations.findAccepteesChevauchant(m.getId(), d.plusDays(6), d.plusDays(8))).isEmpty();
    }

    @Test
    void empruntsEnRetard() {
        Utilisateur u = utilisateurs.save(new Utilisateur("N", "P", "c@d.fr", "x", Role.ROLE_USER));
        Materiel m = materiels.save(new Materiel("Pc", "d", 1, null, StatutMateriel.DISPONIBLE, types.save(new TypeMateriel("PC"))));
        LocalDate d = LocalDate.now();
        Reservation r1 = reservations.save(new Reservation(u, m, d.minusDays(10), d.minusDays(5), StatutReservation.ACCEPTEE, null, null));
        Reservation r2 = reservations.save(new Reservation(u, m, d.minusDays(30), d.minusDays(25), StatutReservation.ACCEPTEE, null, null));
        emprunts.save(new Emprunt(r1, d.minusDays(10), d.minusDays(5), null, null));
        emprunts.save(new Emprunt(r2, d.minusDays(30), d.minusDays(25), d.minusDays(26), null));

        assertThat(emprunts.findEnRetard(d)).hasSize(1);
        assertThat(emprunts.findByDateRetourEffectiveIsNull()).hasSize(1);
    }
}
