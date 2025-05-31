package tda.darkarmy.mvpserver.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tda.darkarmy.mvpserver.model.InvoiceDetails;

import java.util.List;

public interface InvoiceDetailsRepository extends MongoRepository<InvoiceDetails, String> {
    List<InvoiceDetails> findByInvoiceNumber(String invoiceNumber);
}
