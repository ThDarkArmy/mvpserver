package tda.darkarmy.mvpserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tda.darkarmy.mvpserver.dto.BillScanOcrDto;
import tda.darkarmy.mvpserver.service.BillScanOcrService;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/v1/bill-scan-ocr")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BillScanOcrController {

    @Autowired
    private BillScanOcrService billScanOcrService;

    @PostMapping("/scan-bill")
    public ResponseEntity<?> scanBill(@ModelAttribute BillScanOcrDto billScanOcrDto){
        return status(200).body(billScanOcrService.scanBill(billScanOcrDto));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return status(200).body(billScanOcrService.getAll());
    }

    @PutMapping("/verify-receipt/{id}")
    public ResponseEntity<?> verifyReceipt(@PathVariable String id){
        return status(200).body(billScanOcrService.verify(id));
    }
}
