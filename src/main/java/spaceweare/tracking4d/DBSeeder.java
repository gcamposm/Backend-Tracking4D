package spaceweare.tracking4d;

import spaceweare.tracking4d.FileManagement.service.FileStorageService;
import spaceweare.tracking4d.SQL.dao.CameraDao;
import spaceweare.tracking4d.SQL.dao.CommuneDao;
import spaceweare.tracking4d.SQL.dao.PersonTypeDao;
import spaceweare.tracking4d.SQL.dao.RegionDao;
import spaceweare.tracking4d.SQL.models.Camera;
import spaceweare.tracking4d.SQL.models.Commune;
import spaceweare.tracking4d.SQL.models.PersonType;
import spaceweare.tracking4d.SQL.models.Region;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Component
@Order(2)
public class DBSeeder implements CommandLineRunner {

    private final RegionDao regionDao;
    private final CommuneDao communeDao;
    private final CameraDao cameraDao;
    private final PersonTypeDao personTypeDao;
    private final FileStorageService fileStorageService;

    public DBSeeder(RegionDao regionDao, PersonTypeDao personTypeDao, CommuneDao communeDao, FileStorageService fileStorageService, CameraDao cameraDao) {
        this.regionDao = regionDao;
        this.communeDao = communeDao;
        this.fileStorageService = fileStorageService;
        this.cameraDao = cameraDao;
        this.personTypeDao = personTypeDao;
    }

    public void seedRegions(){
        //JSONParser parser = new JSONParser();
        try {
            Path path = fileStorageService.getFileStorageLocation();
            JSONObject obj=(JSONObject) JSONValue.parse(new FileReader(path + "/address.json"));
            JSONArray arr =(JSONArray)obj.get("Address");
            for(int i = 0; i < arr.size() ; i ++){
                JSONObject address = (JSONObject) arr.get(i);
                //SEED
                Region region = new Region();
                region.setCreatedAt(LocalDateTime.now());
                region.setName(address.get("region").toString());
                region.setNumber(address.get("numero").toString());
                regionDao.save(region);
                JSONArray comunas =(JSONArray)address.get("comunas");
                for(int j = 0; j < comunas.size(); j ++ ){
                    Commune commune = new Commune();
                    commune.setName(comunas.get(j).toString());
                    commune.setRegion(region);
                    communeDao.save(commune);
                }
            }
        } catch(Exception e) {
            System.out.println("file not found" + e);
        }
    }

    public void seedCameras(){
        Camera camera1 = new Camera();
        camera1.setValue("1");
        cameraDao.save(camera1);
        Camera camera2 = new Camera();
        camera2.setValue("2");
        cameraDao.save(camera2);
    }

    public void seedCustomerTypes(){
        PersonType seller = new PersonType();
        seller.setName("Vendedor");
        personTypeDao.save(seller);
        PersonType customer = new PersonType();
        customer.setName("Cliente");
        personTypeDao.save(customer);
        PersonType provider = new PersonType();
        provider.setName("Proveedor");
        personTypeDao.save(provider);
        PersonType student = new PersonType();
        student.setName("Alumno");
        personTypeDao.save(student);
        PersonType attorney = new PersonType();
        attorney.setName("Apoderado");
        personTypeDao.save(attorney);
        PersonType teacher = new PersonType();
        teacher.setName("Docente");
        personTypeDao.save(teacher);
        PersonType inspector = new PersonType();
        inspector.setName("Inspector");
        personTypeDao.save(inspector);
        PersonType unknown = new PersonType();
        unknown.setName("Desconocido");
        personTypeDao.save(unknown);
    }

    @Override
    public void run(String... args) throws Exception {
        if(regionDao.findAll().size() == 0){
            seedRegions();
        }
        seedCameras();
        seedCustomerTypes();
    }
}