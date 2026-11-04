package fr.resmat.config;

import fr.resmat.entity.*;
import fr.resmat.repository.*;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Insère des données de démonstration si la base est vide. Mot de passe de tous les comptes : "password". */
@Component
public class DataLoader implements CommandLineRunner {

    private final UtilisateurRepository utilisateurs;
    private final TypeMaterielRepository types;
    private final MaterielRepository materiels;
    private final ReservationRepository reservations;
    private final EmpruntRepository emprunts;
    private final PasswordEncoder encoder;

    public DataLoader(UtilisateurRepository utilisateurs, TypeMaterielRepository types,
                      MaterielRepository materiels, ReservationRepository reservations,
                      EmpruntRepository emprunts, PasswordEncoder encoder) {
        this.utilisateurs = utilisateurs; this.types = types; this.materiels = materiels;
        this.reservations = reservations; this.emprunts = emprunts; this.encoder = encoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (utilisateurs.count() > 0) return;
        String mdp = encoder.encode("password");
        LocalDate auj = LocalDate.now();

        // --- Utilisateurs (5) ---
        Utilisateur alice = utilisateurs.save(new Utilisateur("Martin", "Alice", "alice@resmat.fr", mdp, Role.ROLE_USER));
        Utilisateur bob = utilisateurs.save(new Utilisateur("Durand", "Bob", "bob@resmat.fr", mdp, Role.ROLE_USER));
        Utilisateur chloe = utilisateurs.save(new Utilisateur("Petit", "Chloé", "chloe@resmat.fr", mdp, Role.ROLE_USER));
        utilisateurs.save(new Utilisateur("Bernard", "Hugo", "gestionnaire@resmat.fr", mdp, Role.ROLE_GESTIONNAIRE));
        utilisateurs.save(new Utilisateur("Moreau", "Sophie", "sophie@resmat.fr", mdp, Role.ROLE_GESTIONNAIRE));

        // --- Types de matériel (5) ---
        TypeMateriel tOrdi = types.save(new TypeMateriel("Ordinateur"));
        TypeMateriel tCam = types.save(new TypeMateriel("Caméra"));
        TypeMateriel tMicro = types.save(new TypeMateriel("Micro"));
        TypeMateriel tVideo = types.save(new TypeMateriel("Vidéoprojecteur"));
        TypeMateriel tAcc = types.save(new TypeMateriel("Accessoire"));

        // --- Matériel (8) ---
        Materiel portable = materiels.save(new Materiel("Portable Dell Latitude", "Portable 15\" avec Windows 11", 5, 14, StatutMateriel.DISPONIBLE, tOrdi));
        Materiel macbook = materiels.save(new Materiel("MacBook Air M2", "Ordinateur portable léger pour le montage", 2, 7, StatutMateriel.DISPONIBLE, tOrdi));
        Materiel canon = materiels.save(new Materiel("Caméra Canon EOS", "Reflex numérique avec objectif 18-55 mm", 3, 7, StatutMateriel.DISPONIBLE, tCam));
        Materiel gopro = materiels.save(new Materiel("GoPro Hero 11", "Caméra d'action étanche", 2, 3, StatutMateriel.INDISPONIBLE, tCam));
        Materiel rode = materiels.save(new Materiel("Micro Rode NT-USB", "Micro USB de studio", 4, null, StatutMateriel.DISPONIBLE, tMicro));
        Materiel epson = materiels.save(new Materiel("Vidéoprojecteur Epson", "Full HD, HDMI et VGA", 2, 5, StatutMateriel.DISPONIBLE, tVideo));
        materiels.save(new Materiel("Ancien projecteur", "Retiré du catalogue (obsolète)", 1, null, StatutMateriel.RETIRE, tVideo));
        materiels.save(new Materiel("Trépied Manfrotto", "Trépied aluminium", 6, 7, StatutMateriel.DISPONIBLE, tAcc));

        // --- Réservations (6) : un exemple par statut ---
        reservations.save(new Reservation(alice, canon, auj.plusDays(3), auj.plusDays(6),
            StatutReservation.EN_ATTENTE, "Tournage d'un court-métrage", null));
        Reservation rRendu = reservations.save(new Reservation(bob, portable, auj.minusDays(30), auj.minusDays(23),
            StatutReservation.ACCEPTEE, "Projet de groupe", null));
        reservations.save(new Reservation(chloe, macbook, auj.plusDays(2), auj.plusDays(4),
            StatutReservation.REFUSEE, "Montage vidéo", "Matériel réservé pour un autre projet"));
        reservations.save(new Reservation(alice, rode, auj.plusDays(10), auj.plusDays(12),
            StatutReservation.ANNULEE, "Enregistrement podcast", null));
        Reservation rEnCours = reservations.save(new Reservation(chloe, epson, auj.minusDays(2), auj.plusDays(2),
            StatutReservation.ACCEPTEE, "Présentation de stage", null));
        Reservation rRetard = reservations.save(new Reservation(bob, gopro, auj.minusDays(10), auj.minusDays(5),
            StatutReservation.ACCEPTEE, "Sortie vélo filmée", null));

        // --- Emprunts (3) : en cours, en retard, rendu ---
        emprunts.save(new Emprunt(rEnCours, auj.minusDays(2), auj.plusDays(2), null, null));
        emprunts.save(new Emprunt(rRetard, auj.minusDays(10), auj.minusDays(5), null, "Relance à faire"));
        emprunts.save(new Emprunt(rRendu, auj.minusDays(30), auj.minusDays(23), auj.minusDays(23), "Rendu en bon état"));
    }
}
