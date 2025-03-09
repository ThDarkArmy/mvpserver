package tda.darkarmy.mvpserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tda.darkarmy.mvpserver.model.InvoiceDetails;

import java.util.List;
import java.util.Optional;

public interface InvoiceDetailsRepository extends JpaRepository<InvoiceDetails, Long> {
    List<InvoiceDetails> findByInvoiceNumber(String invoiceNumber);
}
