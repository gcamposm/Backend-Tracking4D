package spaceweare.tracking4d.SQL.mappers;

import spaceweare.tracking4d.SQL.dto.models.UserDto;
import spaceweare.tracking4d.SQL.models.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
@Component
public class UserMapper {

  public User mapToModel(UserDto userDto){
    User user = new User();
    user.setId(userDto.getId().longValue());
    user.setUsername(userDto.getUserName());
    user.setPassword(userDto.getPassword());
    user.setActive(userDto.getActive());
    return user;
  }

  public List<UserDto> mapToDtoList(List<User> users) {
    ArrayList<UserDto> userDtos = new ArrayList<>();
    for (User user: users
         ) {
      userDtos.add(mapToDto(user));
    }
    return userDtos;
  }

  public UserDto mapToDto (User user){
    UserDto userDto = new UserDto();
    userDto.setId(user.getId().intValue());
    userDto.setUserName(user.getUsername());
    userDto.setPassword(user.getPassword());
    userDto.setActive(user.getActive());
    return userDto;
  }
}