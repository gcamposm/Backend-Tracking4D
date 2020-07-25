package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.ReportDao;
import spaceweare.tracking4d.SQL.models.Report;

import java.util.List;

@Service
public class ReportService {

    private final ReportDao reportDao;
    public ReportService(ReportDao reportDao) {
        this.reportDao = reportDao;
    }

    public Report create(Report report){
        return reportDao.save(report);
    }

    public Report readById(Integer id){
        if(reportDao.findById(id).isPresent()){
            return reportDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Report> readAll(){
        return reportDao.findAll();
    }

    public Report update(Report report, Integer id){
        if(reportDao.findById(id).isPresent()){
            Report reportFound = reportDao.findById(id).get();
            reportFound.setName(report.getName());
            reportFound.setContent(report.getContent());
            return reportDao.save(reportFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(reportDao.findById(id).isPresent()){
            reportDao.delete(reportDao.findById(id).get());
            return "deleted";
        }
        else{
            return  null;
        }
    }
}