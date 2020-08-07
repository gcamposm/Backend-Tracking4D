package spaceweare.tracking4d;

import spaceweare.tracking4d.FileManagement.service.FileStorageService;
import spaceweare.tracking4d.SQL.dao.CameraDao;
import spaceweare.tracking4d.SQL.dao.CommuneDao;
import spaceweare.tracking4d.SQL.dao.CustomerTypeDao;
import spaceweare.tracking4d.SQL.dao.RegionDao;
import spaceweare.tracking4d.SQL.models.Camera;
import spaceweare.tracking4d.SQL.models.Commune;
import spaceweare.tracking4d.SQL.models.CustomerType;
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
    private final CustomerTypeDao customerTypeDao;
    private final FileStorageService fileStorageService;

    public DBSeeder(RegionDao regionDao, CustomerTypeDao customerTypeDao, CommuneDao communeDao, FileStorageService fileStorageService, CameraDao cameraDao) {
        this.regionDao = regionDao;
        this.communeDao = communeDao;
        this.fileStorageService = fileStorageService;
        this.cameraDao = cameraDao;
        this.customerTypeDao = customerTypeDao;
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
        CustomerType seller = new CustomerType();
        seller.setName("Vendedor");
        customerTypeDao.save(seller);
        CustomerType customer = new CustomerType();
        customer.setName("Cliente");
        customerTypeDao.save(customer);
        CustomerType provider = new CustomerType();
        provider.setName("Proveedor");
        customerTypeDao.save(provider);
        CustomerType student = new CustomerType();
        student.setName("Alumno");
        customerTypeDao.save(student);
        CustomerType attorney = new CustomerType();
        attorney.setName("Apoderado");
        customerTypeDao.save(attorney);
        CustomerType teacher = new CustomerType();
        teacher.setName("Docente");
        customerTypeDao.save(teacher);
        CustomerType inspector = new CustomerType();
        inspector.setName("Inspector");
        customerTypeDao.save(inspector);
        CustomerType unknown = new CustomerType();
        unknown.setName("Desconocido");
        customerTypeDao.save(unknown);
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