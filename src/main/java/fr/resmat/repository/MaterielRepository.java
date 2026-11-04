package fr.resmat.repository;

import fr.resmat.entity.Materiel;
import fr.resmat.entity.StatutMateriel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterielRepository extends JpaRepository<Materiel, Long> {
    List<Materiel> findByTypeMaterielId(Long typeId);
    List<Materiel> findByStatut(StatutMateriel statut);
    List<Materiel> findByNomContainingIgnoreCase(String nom);
    long countByTypeMaterielId(Long typeId);
}
