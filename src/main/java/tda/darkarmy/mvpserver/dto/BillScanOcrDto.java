package tda.darkarmy.mvpserver.dto;

import org.springframework.web.multipart.MultipartFile;

public class BillScanOcrDto {
    private MultipartFile bill;

    public BillScanOcrDto() {
    }

    public BillScanOcrDto(MultipartFile bill) {
        this.bill = bill;
    }

    public MultipartFile getBill() {
        return bill;
    }

    public void setBill(MultipartFile bill) {
        this.bill = bill;
    }


}
