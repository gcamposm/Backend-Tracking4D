package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.ItemDao;
import spaceweare.tracking4d.SQL.models.Item;
import java.util.List;

@Service
public class ItemService {

    private final ItemDao itemDaoDao;
    public ItemService(ItemDao itemDaoDao) {
        this.itemDaoDao = itemDaoDao;
    }

    public Item create(Item item){
        return itemDaoDao.save(item);
    }

    public Item readById(Integer id){
        if(itemDaoDao.findById(id).isPresent()){
            return itemDaoDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Item> readAll(){
        return itemDaoDao.findAll();
    }

    public Item update(Item item, Integer id){
        if(itemDaoDao.findById(id).isPresent()){
            Item itemFound = itemDaoDao.findById(id).get();
            itemFound.setName(item.getName());
            itemFound.setCompanyList(item.getCompanyList());
            return itemDaoDao.save(itemFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(itemDaoDao.findById(id).isPresent()){
            itemDaoDao.delete(itemDaoDao.findById(id).get());
            return "deleted";
        }
        else {
            return null;
        }
    }
}