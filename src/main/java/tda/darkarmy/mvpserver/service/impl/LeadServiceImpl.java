package tda.darkarmy.mvpserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tda.darkarmy.mvpserver.dto.LeadDto;
import tda.darkarmy.mvpserver.exception.ResourceNotFoundException;
import tda.darkarmy.mvpserver.model.Lead;
import tda.darkarmy.mvpserver.model.Property;
import tda.darkarmy.mvpserver.model.User;
import tda.darkarmy.mvpserver.repository.LeadRepository;
import tda.darkarmy.mvpserver.repository.PropertyRepository;
import tda.darkarmy.mvpserver.service.LeadService;
import tda.darkarmy.mvpserver.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeadServiceImpl implements LeadService {

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public List<LeadDto> getAllLeads() {
        List<Lead> leads = leadRepository.findAll();
        List<LeadDto> leadDtos = leads.stream().map(lead -> {
            Property property = propertyRepository.findById(lead.getPropertyId()).get();
            User user = userService.getUserById(lead.getUserId()).get();
            return new LeadDto(lead.getId(), user, property);
        }).collect(Collectors.toList());

        return leadDtos;
    }

    @Override
    public Lead createLead(String propertyId) {
        Property property = propertyRepository.findById(propertyId).orElseThrow(()-> new ResourceNotFoundException("Property not found!"));
        User user = userService.getLoggedInUser();
        Lead lead = new Lead();
        lead.setUserId(user.getId());
        lead.setPropertyId(property.getId());
        return leadRepository.save(lead);
    }
}
