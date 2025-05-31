package tda.darkarmy.mvpserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tda.darkarmy.mvpserver.model.Property;
import tda.darkarmy.mvpserver.model.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadDto {
    private String id;
    private User user;
    private Property property;
}
