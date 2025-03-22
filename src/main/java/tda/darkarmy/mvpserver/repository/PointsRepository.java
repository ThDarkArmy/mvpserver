package tda.darkarmy.mvpserver.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import tda.darkarmy.mvpserver.model.Points;

public interface PointsRepository extends MongoRepository<Points, String> {
    Points findByUserId(Long userId);
}
