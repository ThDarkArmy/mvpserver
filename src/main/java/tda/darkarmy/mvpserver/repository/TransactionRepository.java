package tda.darkarmy.mvpserver.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tda.darkarmy.mvpserver.model.Transaction;
import tda.darkarmy.mvpserver.model.User;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByUser(User user);
}
