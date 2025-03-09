package tda.darkarmy.mvpserver.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tda.darkarmy.mvpserver.service.LeadService;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/v1/leads")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LeadController {

    @Autowired
    private LeadService leadService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return status(200).body(leadService.getAllLeads());
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> createLead(@PathVariable Long id){
        return status(201).body(leadService.createLead(id));
    }
}
