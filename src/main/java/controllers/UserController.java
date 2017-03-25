package controllers;

import domain.User;
import repositories.DatabaseRepository;

import repositories.IDatabaseRepository;
import utils.CryptWithMD5;
import utils.exceptions.ControllerException;
import utils.exceptions.RepositoryException;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Sergiu on 1/19/2017.
 */
public class UserController{
    private IDatabaseRepository<User,Integer> repository;

    public UserController() {
    }

    public UserController(IDatabaseRepository<User,Integer> repository) {
        this.repository = repository;
    }

    public void add(String user,String password,boolean startTransaction,boolean endTransaction) throws ControllerException {
        try {
            password = CryptWithMD5.cryptWithMD5(password);
            repository.add (new User(user,password),startTransaction,endTransaction);
        } catch (RepositoryException | SQLException e) {
            codeThrowControllerExceptionStatement(e);
        }
    }

    public void register(String username,String password,boolean startTransaction,boolean endTransaction) throws ControllerException {
        Map<String,String> map = new HashMap<>();
        map.put("username",username);
        if(username.length() < 6 || username.length() > 12){
            codeThrowControllerExceptionStatement("Username length must be between 6 and 12 characters.");
        }
        if(password.length() < 6 || password.length() > 12){
            codeThrowControllerExceptionStatement("Username length must be between 6 and 12 characters.");
        }
        try {
            if(repository.getItemsByProperty(map).size() == 1)
                codeThrowControllerExceptionStatement("There exist this username. Try again with another.");
            else{
                password = CryptWithMD5.cryptWithMD5(password);
                this.repository.add(new User(username,password),startTransaction,endTransaction);
            }
        } catch (RepositoryException | SQLException e) {
            codeThrowControllerExceptionStatement(e);
        }
    }

    public boolean existUsername(String username) throws ControllerException {
        Map<String,String> map = new HashMap<>();
        map.put("username",username);
        try {
            return repository.getItemsByProperty(map).size() == 1;
        } catch (RepositoryException e) {
            codeThrowControllerExceptionStatement(e);
        }
        return false;
    }

    public boolean logIn(String username,String password) throws ControllerException {
        Map<String,String> map = new HashMap<>();
        map.put("username",username);
        try {
            List<User> list = repository.getItemsByProperty(map);
            if(list.size() == 1){
                User u = list.get(0);
                if( CryptWithMD5.cryptWithMD5(password).equals(u.getPassword())){
                    return true;
                }
                else{
                    return false;
                }
            }
        } catch (RepositoryException e) {
            codeThrowControllerExceptionStatement(e);
        }
        return false;
    }

    private void codeThrowControllerExceptionStatement(Exception e) throws ControllerException {
        throw new ControllerException(e);
    }

    private void codeThrowControllerExceptionStatement(String e) throws ControllerException {
        throw new ControllerException(e);
    }
}
