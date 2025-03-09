package tda.darkarmy.mvpserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tda.darkarmy.mvpserver.model.Lead;

public interface LeadRepository extends JpaRepository<Lead, Long> {
}
