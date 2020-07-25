package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.CompanyDao;
import spaceweare.tracking4d.SQL.models.Company;
import java.util.List;

@Service
public class CompanyService {

    private final CompanyDao companyDao;
    public CompanyService(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    public Company create(Company company){
        return companyDao.save(company);
    }

    public Company readById(Integer id){
        if(companyDao.findById(id).isPresent()){
            return companyDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Company> readAll(){
        return companyDao.findAll();
    }

    public Company update(Company company, Integer id){
        if(companyDao.findById(id).isPresent()){
            Company companyFound = companyDao.findById(id).get();
            companyFound.setName(company.getName());
            companyFound.setAddresses(company.getAddresses());
            companyFound.setMaps(company.getMaps());
            companyFound.setMatches(company.getMatches());
            companyFound.setCustomerList(company.getCustomerList());
            companyFound.setRut(company.getRut());
            return companyDao.save(companyFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(companyDao.findById(id).isPresent()){
            companyDao.delete(companyDao.findById(id).get());
            return "deleted";
        }
        else{
            return  null;
        }
    }
}