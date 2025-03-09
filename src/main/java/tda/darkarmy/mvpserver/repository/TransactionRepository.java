package tda.darkarmy.mvpserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tda.darkarmy.mvpserver.model.Transaction;
import tda.darkarmy.mvpserver.model.User;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
}
