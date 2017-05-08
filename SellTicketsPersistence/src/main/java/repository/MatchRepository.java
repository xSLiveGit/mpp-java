package repository;


import entity.Match;
import exceptions.RepositoryException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import sun.security.validator.ValidatorException;
import utils.validator.ValidatorMatch;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Sergiu on 3/25/2017.
 */
public class MatchRepository implements IRepository<Match,Integer> {
    DataAccessObject<Match,Integer> dao;
    ValidatorMatch validator;
    public MatchRepository(DataAccessObject<Match,Integer> dao,ValidatorMatch validator) {
        this.dao = dao;
        this.validator = validator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Match> getAll() throws RepositoryException {
        return this.dao.getAll(Match.class,"matches");
    }

    @Override
    public Match findById(Integer integer) throws RepositoryException {
        return this.dao.findById(Match.class,integer);
    }

    @Override
    public void update(Match element) throws RepositoryException {
        validator.validate(element);
        this.dao.update(Match.class,element);
    }

    @Override
    public Integer add(Match element) throws RepositoryException, SQLException {
        validator.validate(element);
        return this.dao.add(Match.class,element);
    }

    @Override
    public Match delete(Integer integer) throws RepositoryException {
        return this.dao.delete(Match.class,integer);
    }

    @Override
    public Integer getSize() throws RepositoryException, SQLException {
        return this.dao.getSize("matches");
    }

    @Override
    public void clear() throws RepositoryException {

    }
}

