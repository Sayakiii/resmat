package fr.resmat.service;

import fr.resmat.dto.TypeMaterielDto;
import fr.resmat.entity.TypeMateriel;
import fr.resmat.repository.MaterielRepository;
import fr.resmat.repository.TypeMaterielRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class TypeMaterielService {

    private final TypeMaterielRepository typeRepository;
    private final MaterielRepository materielRepository;

    public TypeMaterielService(TypeMaterielRepository typeRepository, MaterielRepository materielRepository) {
        this.typeRepository = typeRepository;
        this.materielRepository = materielRepository;
    }

    @Transactional(readOnly = true)
    public List<TypeMaterielDto> findAll() {
        return typeRepository.findAll(Sort.by("libelle")).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public TypeMaterielDto findById(Long id) {
        return toDto(getOrThrow(id));
    }

    public void create(TypeMaterielDto dto) {
        verifierLibelleLibre(dto.getLibelle(), null);
        typeRepository.save(new TypeMateriel(dto.getLibelle().trim()));
    }

    public void update(Long id, TypeMaterielDto dto) {
        TypeMateriel type = getOrThrow(id);
        verifierLibelleLibre(dto.getLibelle(), id);
        type.setLibelle(dto.getLibelle().trim());
    }

    public void delete(Long id) {
        TypeMateriel type = getOrThrow(id);
        if (materielRepository.countByTypeMaterielId(id) > 0) {
            throw new RegleMetierException(
                "Impossible de supprimer « " + type.getLibelle() + " » : des matériels y sont rattachés.");
        }
        typeRepository.delete(type);
    }

    private TypeMateriel getOrThrow(Long id) {
        return typeRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Type de matériel introuvable"));
    }

    private void verifierLibelleLibre(String libelle, Long idCourant) {
        typeRepository.findByLibelleIgnoreCase(libelle.trim()).ifPresent(existant -> {
            if (!existant.getId().equals(idCourant)) {
                throw new RegleMetierException("Ce libellé existe déjà.");
            }
        });
    }

    private TypeMaterielDto toDto(TypeMateriel t) {
        return new TypeMaterielDto(t.getId(), t.getLibelle(), materielRepository.countByTypeMaterielId(t.getId()));
    }
}
