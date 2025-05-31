package tda.darkarmy.mvpserver.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tda.darkarmy.mvpserver.model.Lead;

public interface LeadRepository extends MongoRepository<Lead, String> {
}
