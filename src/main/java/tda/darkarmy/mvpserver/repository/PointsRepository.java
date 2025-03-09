package tda.darkarmy.mvpserver.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tda.darkarmy.mvpserver.model.Points;

public interface PointsRepository extends JpaRepository<Points, Long> {
    Points findByUserId(Long userId);
}
