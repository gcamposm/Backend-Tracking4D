package spaceweare.tracking4d;

import spaceweare.tracking4d.FileManagement.service.FileStorageService;
import spaceweare.tracking4d.SQL.dao.*;
import spaceweare.tracking4d.SQL.models.*;
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
    private final PersonPositionDao personPositionDao;
    private final FileStorageService fileStorageService;

    public DBSeeder(RegionDao regionDao, PersonPositionDao personPositionDao, PersonTypeDao personTypeDao, CommuneDao communeDao, FileStorageService fileStorageService, CameraDao cameraDao) {
        this.regionDao = regionDao;
        this.communeDao = communeDao;
        this.fileStorageService = fileStorageService;
        this.cameraDao = cameraDao;
        this.personTypeDao = personTypeDao;
        this.personPositionDao = personPositionDao;
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
        camera1.setIsCovidCamera(false);
        cameraDao.save(camera1);
        Camera camera2 = new Camera();
        camera2.setValue("2");
        camera2.setIsCovidCamera(false);
        cameraDao.save(camera2);
        Camera camera3 = new Camera();
        camera3.setValue("3");
        camera3.setIsCovidCamera(false);
        cameraDao.save(camera3);
        Camera camera4 = new Camera();
        camera4.setValue("4");
        camera4.setIsCovidCamera(false);
        cameraDao.save(camera4);
        Camera camera5 = new Camera();
        camera5.setValue("5");
        camera5.setIsCovidCamera(false);
        cameraDao.save(camera5);
        Camera camera6 = new Camera();
        camera6.setValue("covid");
        camera6.setIsCovidCamera(true);
        cameraDao.save(camera6);
    }

    public void seedPersonTypes(){
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
        PersonType partner = new PersonType();
        partner.setName("Socio");
        personTypeDao.save(partner);
    }

    public void seedPersonPositions(){
        PersonPosition grocer = new PersonPosition();
        grocer.setName("Bodeguero");
        personPositionDao.save(grocer);
        PersonPosition administrative = new PersonPosition();
        administrative.setName("Administrativo");
        personPositionDao.save(administrative);
        PersonPosition craneOperator = new PersonPosition();
        craneOperator.setName("Yalero");
        personPositionDao.save(craneOperator);
        PersonPosition unknown = new PersonPosition();
        unknown.setName("Desconocido");
        personPositionDao.save(unknown);
    }

    @Override
    public void run(String... args) throws Exception {
        if(regionDao.findAll().size() == 0){
            seedRegions();
        }
        seedCameras();
        seedPersonTypes();
        seedPersonPositions();
    }
}