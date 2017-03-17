//package repositories;
//
//import domain.Sale;
//import utils.database.DatabaseConnectionManager;
//import utils.exceptions.RepositoryException;
//import utils.mapper.IMapper;
//import utils.validators.IValidator;
//
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Map;
//
///**
// * Created by Sergiu on 3/16/2017.
// */
//public class SaleRepository extends DatabaseRepository<Sale,Integer> {
//    public SaleRepository(DatabaseConnectionManager dbConnManager, IMapper<Sale> mapper, String tableName, IValidator<Sale> validator) throws RepositoryException {
//        super(dbConnManager, mapper, tableName, validator);
//    }
//
//    @Override
//    public void add(Sale sale) throws RepositoryException {
//        ArrayList<String> paramsV = new ArrayList<>();
//        ArrayList<String> valuesV = new ArrayList<>();
//        String params = "";
//        String values = "";
//
//        Map<String,String> map;
//        map = mapper.toMap(sale);
//
//        map.remove(mapper.getIdTextField());//id-ul e autoincrement deci il vom sterge
//        for(Map.Entry<String,String> pair : map.entrySet()){
//            valuesV.add(pair.getValue().toString());
//            paramsV.add("`" + pair.getKey() + "`");
//        }
//
//        params = String.join(",",paramsV);
//        values = String.join(",",valuesV);
//
//        String query = String.format("INSERT INTO `%s` (%s) VALUES (%s)",tableName,params,values);
//        try {
//            PreparedStatement stmt = connection.prepareStatement(query);
//            stmt.execute();
//        } catch (SQLException e) {
//            codeThrowRepositoryException(e);
//        }
//    }
//}
