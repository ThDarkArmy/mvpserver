package tda.darkarmy.mvpserver.mapper;

import tda.darkarmy.mvpserver.dto.UserDto;
import tda.darkarmy.mvpserver.model.User;
import tda.darkarmy.mvpserver.enums.Role;

public class UserMapper {

    public static UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }

        return UserDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .password(user.getPassword())
                .role(user.getRole() != null ? user.getRole().name() : null)
                .address(user.getAddress())
                .isVerified(user.getEmailVerified())
                .build();
    }

    public static User toUserEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .phoneNumber(userDto.getPhoneNumber())
                .password(userDto.getPassword())
                .role(userDto.getRole() != null ? Role.valueOf(userDto.getRole()) : null)
                .address(userDto.getAddress())
                .emailVerified(userDto.getIsVerified() != null ? userDto.getIsVerified() : false)
                .isActive(true)
                .pointsBalance(0)
                .build();
    }
}
