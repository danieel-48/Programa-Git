package com.example.bookstore.dao;

import com.example.bookstore.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public interface UserDAO {

    User create(User user) throws SQLException;

    Optional<User> findById(long idUser) throws SQLException;

    List<User> findAll() throws SQLException;

    boolean update(User user) throws SQLException;

    boolean delete(long idUser) throws SQLException;
}
