package legacyfighter.dietary.newproducts;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class OldProductDescriptionRepository {
    private final OldProductRepository oldProductRepository;

    OldProductDescriptionRepository(OldProductRepository oldProductRepository) {
        this.oldProductRepository = oldProductRepository;
    }

    public List<OldProductDescription> findAll() {
        return oldProductRepository.findAll().stream().map(OldProductDescription::new).collect(Collectors.toList());
    }

    public OldProductDescription getOne(UUID productId) {
        return new OldProductDescription(oldProductRepository.getOne(productId));
    }
}
