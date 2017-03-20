package controllers;

import domain.Sale;
import repositories.IDatabaseRepository;
import repositories.IRepository;
import utils.exceptions.ControllerException;
import utils.exceptions.RepositoryException;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by Sergiu on 3/16/2017.
 */
public class SaleController {
    IDatabaseRepository<Sale,Integer> saleRepository;

    public SaleController(IDatabaseRepository<Sale, Integer> saleRepository) {
        this.saleRepository = saleRepository;
    }

    public Integer add(Integer idTicket,Integer idMatch,String person,Integer quantity,boolean startTransaction,boolean endTransaction) throws ControllerException {
        Integer id = null;
        try {
            id = this.saleRepository.add(new Sale(idMatch,idTicket,person,quantity),startTransaction,endTransaction);//id-ul e fictiv deoarece in DB id e autoincrement
        } catch (RepositoryException | SQLException e) {
            codeThrowControllerExceptionStatement(e);
        }
        return id;
    }

    public Sale delete(Integer id,boolean startTransaciton,boolean endTransaction) throws ControllerException {
        try {
            return saleRepository.delete(id,startTransaciton,endTransaction);
        } catch (RepositoryException e) {
            codeThrowControllerExceptionStatement(e);
        }
        return null;
    }

    public List<Sale> getAll() throws ControllerException {
        List<Sale> list = null;
        try {
            list = this.saleRepository.getAll();
        } catch (RepositoryException e) {
            codeThrowControllerExceptionStatement(e);
        }
        return list;
    }

    public List<Sale> getAllWithThisTicketId(Integer id) throws ControllerException {
        Map<String,String> map = new HashMap<>();
        List<Sale> list = null;

        map.put("idTicket",id.toString());
        try {
            list = saleRepository.getItemsByProperty(map);
        } catch (RepositoryException e) {
            codeThrowControllerExceptionStatement(e);
        }
        return list;
    }

    private void codeThrowControllerExceptionStatement(Exception e) throws ControllerException {
        throw new ControllerException(e);
    }
}
