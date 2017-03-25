package controllers;

import domain.User;

import repositories.IDatabaseRepository;
import repositories.UserRepository;
import utils.CryptWithMD5;
import utils.exceptions.ControllerException;
import utils.exceptions.RepositoryException;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sergiu on 1/19/2017.
 */
public class UserController{
    private UserRepository repository;

    public UserController() {
    }

    public UserController(UserRepository repository) {
        this.repository = repository;
    }


    public void add(String username, String password) throws ControllerException {
        try
        {
            password = CryptWithMD5.cryptWithMD5(password);
            repository.add(new User(username, password));
        }
        catch (RepositoryException e)
        {
            codeThrowControllerExceptionStatement(e);
        }
    }

    public User logIn(String username,String password) throws ControllerException {

        try
        {
            User u = repository.findById(username);
            if (CryptWithMD5.cryptWithMD5(password).equals(u.getPassword()))
                return u;
            codeThrowControllerExceptionStatement("Invalid username or password");
        }
        catch (RepositoryException e) {
            codeThrowControllerExceptionStatement("Invalid username or password");
        }
        return null;
    }

    private void codeThrowControllerExceptionStatement(Exception e) throws ControllerException {
        throw new ControllerException(e);
    }

    private void codeThrowControllerExceptionStatement(String e) throws ControllerException {
        throw new ControllerException(e);
    }
}
