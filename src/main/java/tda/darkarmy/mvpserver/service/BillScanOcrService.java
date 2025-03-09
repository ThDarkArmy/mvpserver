package tda.darkarmy.mvpserver.service;

import tda.darkarmy.mvpserver.dto.BillScanOcrDto;
import tda.darkarmy.mvpserver.model.InvoiceDetails;

import java.util.List;

public interface BillScanOcrService {

    InvoiceDetails scanBill(BillScanOcrDto billScanOcrDto);

    List<InvoiceDetails> getAll();

    InvoiceDetails verify(Long id);
}
