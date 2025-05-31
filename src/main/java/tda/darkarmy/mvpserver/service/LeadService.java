package tda.darkarmy.mvpserver.service;


import tda.darkarmy.mvpserver.dto.LeadDto;
import tda.darkarmy.mvpserver.model.Lead;

import java.util.List;

public interface LeadService {
    List<LeadDto> getAllLeads();

    Lead createLead(String propertyId);
}
