package tda.darkarmy.mvpserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tda.darkarmy.mvpserver.dto.BillScanOcrDto;
import tda.darkarmy.mvpserver.exception.ResourceNotFoundException;
import tda.darkarmy.mvpserver.exception.UserAlreadyExistsException;
import tda.darkarmy.mvpserver.model.InvoiceDetails;
import tda.darkarmy.mvpserver.model.Transaction;
import tda.darkarmy.mvpserver.repository.InvoiceDetailsRepository;
import tda.darkarmy.mvpserver.repository.TransactionRepository;
import tda.darkarmy.mvpserver.service.BillScanOcrService;
import tda.darkarmy.mvpserver.service.UserService;
import tda.darkarmy.mvpserver.utils.AmazonBillParser;
import tda.darkarmy.mvpserver.utils.PdfReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class BillScanOcrServiceImpl implements BillScanOcrService {

    @Autowired
    private InvoiceDetailsRepository invoiceDetailsRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    @Value("${biilscan.pointsconversionrate}")
    private int POINTS_CONVERSION_RATE;

    private Path fileStoragePath;

    public BillScanOcrServiceImpl() {
        try {
            fileStoragePath = Paths.get("src\\main\\resources\\static\\fileStorage").toAbsolutePath().normalize();
            Files.createDirectories(fileStoragePath);
        } catch (IOException e) {
            throw new RuntimeException("Issue in creating file directory");
        }
    }

    public List<String> extractAllPatterns(String text, String regex) {
        List<String> matches = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            matches.add(matcher.group(1));
        }

        return matches;
    }

    private String extractPattern(String text, String regex) {
        return extractPattern(text, regex, 0);
    }

    private String extractPattern(String text, String regex, int flags) {
        Pattern pattern = Pattern.compile(regex, flags);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    private String cleanAddress(String rawAddress) {
        if (rawAddress == null || rawAddress.isEmpty()) {
            return rawAddress;
        }

        // Split the address into lines
        String[] lines = rawAddress.split("\\r?\\n");

        // Build the cleaned address
        StringBuilder cleanedAddress = new StringBuilder();
        for (String line : lines) {
            line = line.trim();

            // Exclude lines containing unwanted information
            if (!line.startsWith("PAN No") && !line.startsWith("GST Registration No") && !line.equals("IN")) {
                cleanedAddress.append(line).append(", ");
            }
        }

        // Remove the trailing comma and space
        if (cleanedAddress.length() > 0) {
            cleanedAddress.setLength(cleanedAddress.length() - 2);
        }

        return cleanedAddress.toString();
    }


    public InvoiceDetails scanBill(BillScanOcrDto billScanOcrDto) {
        try {
            // Save the file locally (optional)
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(billScanOcrDto.getBill().getOriginalFilename()));
            fileName = fileName.replace(" ", "");
            String filePathString = fileStoragePath + "\\" + fileName;
            Path filePath = Paths.get(filePathString);
            Files.copy(billScanOcrDto.getBill().getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Extract text from PDF
            String extractedText = PdfReader.extractTextFromPDF(filePathString);
            InvoiceDetails details = new InvoiceDetails();
            details.setStatus("Pending");

            // Detect whether it's Amazon or Flipkart
            if (extractedText.contains("Invoice Number") && extractedText.contains("Order Number")) {
                // Amazon Invoice
//                AmazonBillParser.parseBill(extractedText);
                details.setInvoiceNumber(extractPattern(extractedText, "Invoice Number\\s*:\\s*(\\S+)"));
                details.setInvoiceDate(extractPattern(extractedText, "Invoice Date\\s*:\\s*([0-9./-]+)"));
                details.setSeller(extractPattern(extractedText, "Sold By\\s*:\\s*([^\\n]+)"));
                details.setAddress(extractPattern(extractedText, "Sold By\\s*:\\s*[^\\n]+\\n(.*?)(\\n\\n|Billing Address)", Pattern.DOTALL));
                details.setAmount(extractPattern(extractedText, "Invoice Value\\s*:\\s*([₹0-9,.]+)"));
            } else if (extractedText.contains("Original Invoice Number:") && extractedText.contains("Order ID:")) {
                // Flipkart Invoice
//                FlipkartBillParser.parseBill(extractedText);
                details.setInvoiceNumber(extractPattern(extractedText, "Original Invoice Number:\\s*(\\S+)"));
                details.setInvoiceDate(extractPattern(extractedText, "Order Date:\\s*(\\S+)(\\n|Invoice)"));
                details.setSeller(extractPattern(extractedText, "Sold By\\s*:\\s*([^\\n]+)"));
                details.setAddress(extractPattern(extractedText, "Sold By\\s*:\\s*[^\\n]+\\n(.*?)(\\n\\n|GSTIN)", Pattern.DOTALL));
                List<String> amountMatches = extractAllPatterns(extractedText, "₹\\s*([0-9,.]+)");

                // Sum up all found values
                double totalAmount = amountMatches.stream()
                        .mapToDouble(amount -> Double.parseDouble(amount.replaceAll(",", "")))
                        .sum();
//                details.setAmount(extractPattern(extractedText, "Total\\s*₹\\s*([0-9,.]+)"));
                details.setAmount(String.valueOf(totalAmount));
            } else {
                throw new RuntimeException("Unsupported invoice format");
            }

            // Check for duplicate invoices
            List<InvoiceDetails> existingInvoices = invoiceDetailsRepository.findByInvoiceNumber(details.getInvoiceNumber());
            if (!existingInvoices.isEmpty()) throw new UserAlreadyExistsException("Invoice already exists");

            // Create Transaction
            Transaction transaction = new Transaction();
            transaction.setBillAmount(details.getAmount());
            transaction.setPointsEarned((int) Double.parseDouble(transaction.getBillAmount().replaceAll(",", "")) * POINTS_CONVERSION_RATE);
            transaction.setUser(userService.getLoggedInUser());
            transaction.setDate(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
            transaction.setStatus("Completed");
            transaction.setPointsRedeemed(0);
            transactionRepository.save(transaction);

            return invoiceDetailsRepository.save(details);
        } catch (IOException e) {
            throw new RuntimeException("Error processing invoice", e);
        }
    }


    @Override
    public List<InvoiceDetails> getAll() {
        return invoiceDetailsRepository.findAll();
    }

    @Override
    public InvoiceDetails verify(String id) {
        InvoiceDetails invoiceDetails = invoiceDetailsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Invoice not found!"));
        invoiceDetails.setStatus("Verified");
        return invoiceDetailsRepository.save(invoiceDetails);
    }
}
