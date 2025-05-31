package tda.darkarmy.mvpserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tda.darkarmy.mvpserver.model.Transaction;
import tda.darkarmy.mvpserver.service.impl.TransactionService;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/v1/transactions")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/my-transactions")
    public ResponseEntity<List<Transaction>> getMyTransactions(){
        return status(200).body(transactionService.getMyTransactions());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Transaction>> getAllTransactions(){
        return status(200).body(transactionService.getAllTransactions());
    }

    @PostMapping("/redeem")
    public ResponseEntity<Transaction> redeemPoints(@RequestBody Transaction transaction) {
        return status(201).body(transactionService.redeem(transaction));
    }
}
