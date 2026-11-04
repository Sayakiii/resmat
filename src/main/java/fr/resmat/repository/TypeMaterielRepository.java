package fr.resmat.repository;

import fr.resmat.entity.TypeMateriel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeMaterielRepository extends JpaRepository<TypeMateriel, Long> {
    Optional<TypeMateriel> findByLibelleIgnoreCase(String libelle);
}
