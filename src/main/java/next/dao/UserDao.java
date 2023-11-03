package next.dao;

import core.exception.DataAccessException;
import next.model.User;

import java.util.List;

public interface UserDao {
    void insert(User user) throws DataAccessException;

    void update(User user) throws DataAccessException;

    List<User> findAll() throws DataAccessException;

    User findByUserId(String userId) throws DataAccessException;
}
