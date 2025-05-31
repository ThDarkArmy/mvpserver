package tda.darkarmy.mvpserver.service.impl;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tda.darkarmy.mvpserver.dto.PropertyDto;
import tda.darkarmy.mvpserver.exception.ResourceNotFoundException;
import tda.darkarmy.mvpserver.model.Notifications;
import tda.darkarmy.mvpserver.model.Property;
import tda.darkarmy.mvpserver.model.User;
import tda.darkarmy.mvpserver.repository.PropertyRepository;
import tda.darkarmy.mvpserver.repository.UserRepository;
import tda.darkarmy.mvpserver.service.UserService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    public PropertyService(PropertyRepository propertyRepository, UserRepository userRepository) {
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
    }

    // Convert Multipart Image to Base64
    private String convertImageToBase64(MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            return Base64.getEncoder().encodeToString(file.getBytes());
        }
        return null;
    }

    // Create Property with Image Upload
    public Property createProperty(PropertyDto propertyDTO) throws IOException {
        System.out.println("PropertyDto: "+propertyDTO.toString());
        User user = userService.getLoggedInUser();
        Property property = new Property();
        property.setName(propertyDTO.getName());
        property.setLocation(propertyDTO.getLocation());
        property.setPrice(propertyDTO.getPrice());
        property.setBuilder(propertyDTO.getBuilder());
        property.setContact(propertyDTO.getContact());
        property.setDescription(propertyDTO.getDescription());
        property.setFeatures(propertyDTO.getFeatures());
        property.setPointsRequired(propertyDTO.getPointsRequired());
        property.setDiscount(propertyDTO.getDiscount());
        property.setUserId(user.getId());

        // Convert MultipartFile to Base64
        List<String> base64Images = propertyDTO.getImages().stream().map(image-> {
            try {
                return convertImageToBase64(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();
        property.setImages(base64Images);
        Notifications notifications = new Notifications();
        notifications.setRead(false);
        notifications.setCreatedAt(LocalDateTime.now());
        notifications.setMessage(property.getName() +" " +property.getDescription());
        notifications.setUserId(userService.getLoggedInUser().getId());
        notifications.setTitle("New property added");
        notificationService.createNotification(notifications);
        return propertyRepository.save(property);
    }

    // Edit Property with Image Upload
    @SneakyThrows
    public Property editProperty(String id, PropertyDto propertyDTO) throws IOException {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        property.setName(propertyDTO.getName());
        property.setLocation(propertyDTO.getLocation());
        property.setPrice(propertyDTO.getPrice());
        property.setBuilder(propertyDTO.getBuilder());
        property.setContact(propertyDTO.getContact());
        property.setDescription(propertyDTO.getDescription());
        property.setFeatures(propertyDTO.getFeatures());
        property.setPointsRequired(propertyDTO.getPointsRequired());
        property.setDiscount(propertyDTO.getDiscount());

        // If a new image is uploaded, replace the existing one
        List<String> base64Images = propertyDTO.getImages().stream().map(image-> {
            try {
                return convertImageToBase64(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();
        property.setImages(base64Images);

        return propertyRepository.save(property);
    }

    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    public String deleteProperty(String id){
        propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        propertyRepository.deleteById(id);
        return "Property deleted successfully";
    }

    public List<Property> getPropertiesByUserId(String userId) {
        return propertyRepository.findByUserId(userId);
    }
}

