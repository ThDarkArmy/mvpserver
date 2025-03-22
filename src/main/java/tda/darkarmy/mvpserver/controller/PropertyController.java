package tda.darkarmy.mvpserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tda.darkarmy.mvpserver.dto.PropertyDto;
import tda.darkarmy.mvpserver.exception.ResourceNotFoundException;
import tda.darkarmy.mvpserver.model.Property;
import tda.darkarmy.mvpserver.service.impl.PropertyService;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/v1/properties")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    // Get all properties
    @GetMapping
    public List<Property> getAllProperties() {
        return propertyService.getAllProperties();
    }

    // Get properties by user ID
    @GetMapping("/user/{userId}")
    public List<Property> getPropertiesByUserId(@PathVariable String userId) {
        return propertyService.getPropertiesByUserId(userId);
    }

    // Create Property with Image Upload
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Property> createProperty(@ModelAttribute PropertyDto propertyDTO) throws IOException {
        return ResponseEntity.ok(propertyService.createProperty(propertyDTO));
    }

    // Edit Property with Image Upload
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Property> editProperty(@PathVariable String id, @ModelAttribute PropertyDto propertyDTO) throws IOException {
        return ResponseEntity.ok(propertyService.editProperty(id, propertyDTO));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteProperty(@PathVariable String id) throws ResourceNotFoundException {
        return status(200).body(propertyService.deleteProperty(id));
    }
}
