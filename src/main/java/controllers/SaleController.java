package controllers;

import domain.Sale;
import repositories.IRepository;
import utils.exceptions.ControllerException;
import utils.exceptions.RepositoryException;

import java.sql.SQLException;

/**
 * Created by Sergiu on 3/16/2017.
 */
public class SaleController {
    IRepository<Sale,Integer> saleRepository;

    public SaleController(IRepository<Sale, Integer> saleRepository) {
        this.saleRepository = saleRepository;
    }

    public Integer add(Integer idTicket,Integer idMatch,String person,Integer quantity) throws ControllerException {
        Integer id = null;
        try {
            id = this.saleRepository.add(new Sale(idMatch,idTicket,person,quantity));//id-ul e fictiv deoarece in DB id e autoincrement
        } catch (RepositoryException | SQLException e) {
            codeThrowControllerExceptionStatement(e);
        }
        return id;
    }

    public Sale delete(Integer id) throws ControllerException {
        try {
            return saleRepository.delete(id);
        } catch (RepositoryException e) {
            codeThrowControllerExceptionStatement(e);
        }
        return null;
    }

    private void codeThrowControllerExceptionStatement(Exception e) throws ControllerException {
        throw new ControllerException(e);
    }
}
