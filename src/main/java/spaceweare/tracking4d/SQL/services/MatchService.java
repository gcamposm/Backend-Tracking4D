package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.MatchDao;
import spaceweare.tracking4d.SQL.models.Match;

import java.util.List;

@Service
public class MatchService {

    private final MatchDao matchDao;
    public MatchService(MatchDao matchDao) {
        this.matchDao = matchDao;
    }

    public Match create(Match match){
        return matchDao.save(match);
    }

    public Match readById(Integer id){
        if(matchDao.findById(id).isPresent()){
            return matchDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Match> readAll(){
        return matchDao.findAll();
    }

    public Match update(Match match, Integer id){
        if(matchDao.findById(id).isPresent()){
            Match matchFound = matchDao.findById(id).get();
            matchFound.setName(match.getName());
            matchFound.setCompany(match.getCompany());
            matchFound.setHour(match.getHour());
            matchFound.setPerson(match.getPerson());
            return matchDao.save(matchFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(matchDao.findById(id).isPresent()){
            matchDao.delete(matchDao.findById(id).get());
            return "deleted";
        }
        else{
            return  null;
        }
    }
}