package tda.darkarmy.mvpserver.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tda.darkarmy.mvpserver.model.Property;

import java.util.List;

public interface PropertyRepository extends MongoRepository<Property, String> {
        List<Property> findByUserId(String userId);
}
