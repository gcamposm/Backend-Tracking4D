package spaceweare.tracking4d.SQL.dao;

import spaceweare.tracking4d.SQL.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {
  ArrayList<User> findAll();
  Optional<User> findByUsername(String username);
  User findUserByUsername(String userName);
  User findById(long id);
}