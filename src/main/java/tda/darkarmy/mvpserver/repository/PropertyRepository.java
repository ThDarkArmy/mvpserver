package tda.darkarmy.mvpserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tda.darkarmy.mvpserver.model.Property;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {
        List<Property> findByUserId(Long userId);
}
