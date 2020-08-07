package spaceweare.tracking4d.SQL.services;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import spaceweare.tracking4d.Exceptions.ExportFileException;
import spaceweare.tracking4d.FileManagement.service.FileStorageService;
import spaceweare.tracking4d.SQL.dao.ContactDao;
import spaceweare.tracking4d.SQL.dao.PersonDao;
import spaceweare.tracking4d.SQL.dao.ImageDao;
import spaceweare.tracking4d.SQL.dao.MatchDao;
import spaceweare.tracking4d.SQL.models.Contact;
import spaceweare.tracking4d.SQL.models.Person;
import org.apache.poi.ss.usermodel.*;
import spaceweare.tracking4d.SQL.models.Match;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class PersonService {

    private final FileStorageService fileStorageService;
    private final ImageDao imageDao;
    private final PersonDao personDao;
    private final MatchDao matchDao;
    private final ContactDao contactDao;
    private final MatchService matchService;
    public PersonService(PersonDao personDao, ImageDao imageDao, MatchDao matchDao, ContactDao contactDao, FileStorageService fileStorageService, MatchService matchService) {
        this.personDao = personDao;
        this.imageDao = imageDao;
        this.fileStorageService = fileStorageService;
        this.matchService = matchService;
        this.matchDao = matchDao;
        this.contactDao = contactDao;
    }

    public Person create(Person person){
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        String directory = absoluteFilePath + "/" + person.getRut();
        File directoryFile = new File(directory);
        if (! directoryFile.exists()){
            directoryFile.mkdir();
        }
        return personDao.save(person);
    }

    public Person readById(Integer id){
        if(personDao.findById(id).isPresent()){
            return personDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Person> readAll(){
        return personDao.findAllByUnknownAndDeleted(false, false);
    }

    public Person update(Person person, Integer id){
        if(personDao.findById(id).isPresent()){
            Person personFound = personDao.findById(id).get();
            personFound.setFirstName(person.getFirstName());
            personFound.setLastName(person.getLastName());
            personFound.setRut(person.getRut());
            personFound.setActivity(person.getActivity());
            personFound.setCompany(person.getCompany());
            personFound.setGenre(person.getGenre());
            personFound.setImages(person.getImages());
            personFound.setMail(person.getMail());
            personFound.setPhoneNumber(person.getPhoneNumber());
            personFound.setUser(person.getUser());
            personFound.setUnknown(person.getUnknown());
            return personDao.save(personFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(personDao.findById(id).isPresent()){
            Person person = personDao.findById(id).get();
            person.setDeleted(true);
            personDao.save(person);
            return "deleted";
        }
        else{
            return  null;
        }
    }
    public String deleteByRut(String customerRut) {
        if(personDao.findCustomerByRut(customerRut).isPresent()){
            Person person = personDao.findCustomerByRut(customerRut).get();
            person.setDeleted(true);
            personDao.save(person);
            return "deleted";
        }
        else{
            return  null;
        }
    }

    public Person uploadImage(Person personToUpdate, String name, byte[] fileBytes) throws IOException {
        String ext = name.substring(name.lastIndexOf("."));
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        Integer index = 0;
        if (imageDao.findTopByOrderByIdDesc() != null)
        {
            index = imageDao.findTopByOrderByIdDesc().getId()+1;
        }
        String fileName = personToUpdate.getRut() + "_" + index.toString();
        File convertFile = new File(absoluteFilePath + "/" + fileName + ext);
        try(FileOutputStream fos = new FileOutputStream(convertFile)) {
            byte[] bytes = fileBytes;
            fos.write(bytes);
            ImageService.createImageWithCustomer(personToUpdate, ext, fileName);
            return personToUpdate;
        }catch(IOException IEX){
            return null;
        }
    }

    public Person register(String name) {
        String[] data = name.split(" ");
        String firstName = data[0];
        String lastName = data[1];
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        String directory = absoluteFilePath + "/" + firstName + " " + lastName;
        File directoryFile = new File(directory);
        if (! directoryFile.exists()){
            directoryFile.mkdir();
        }
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        return personDao.save(person);
    }
    public XSSFWorkbook writeOutputFile(List<Match> matchList, Date day, List<Contact> contacts){
        try {
            XSSFWorkbook myWorkBook = new XSSFWorkbook();
            myWorkBook.createSheet("Sheet1");
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            // get the last row number to append new data
            int rownum = mySheet.getLastRowNum();

            FontUnderline fontUnderline = FontUnderline.SINGLE;
            CreationHelper createHelper = myWorkBook.getCreationHelper();
            XSSFCellStyle linkStyle = myWorkBook.createCellStyle();
            XSSFFont linkFont = myWorkBook.createFont();
            linkFont.setUnderline(XSSFFont.U_SINGLE);
            linkFont.setColor(new XSSFColor(Color.BLUE.brighter()));
            linkFont.setUnderline(fontUnderline);
            linkStyle.setFont(linkFont);

            String path = "";

            Row headerRow = mySheet.createRow(rownum++);
            String[] columns = {"Nombre", "Apellido", "Rut", "Género", "Usuario", "Correo", "Celular", "Zona de trabajo", "Cámara", "Ingreso", "Salida", "Estadía", "Contactos"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            for (Match match : matchList) {
                if(!match.getPerson().getUnknown()){
                    System.out.println("Creando");
                    List<Match> inOut = matchService.getIncomeOutcome(day, match.getPerson().getId());
                    Row row = mySheet.createRow(rownum++);
                    Hyperlink link = (Hyperlink) createHelper.createHyperlink(HyperlinkType.URL);
                    //path = URLEncoder.encode(path, "UTF-8");
                    //link.setAddress(match.getPerson().getFirstName());
                    row.createCell(0)
                            .setCellValue(match.getPerson().getFirstName());
                    row.createCell(1)
                            .setCellValue(match.getPerson().getLastName());
                    row.createCell(2)
                            .setCellValue(match.getPerson().getRut());
                    //INT VALUES
                    row.createCell(3)
                            .setCellValue(match.getPerson().getGenre());
                    if(match.getPerson().getUser() != null)
                    {
                        row.createCell(4)
                                .setCellValue( match.getPerson().getUser().getUsername());
                    }
                    else{
                        row.createCell(4)
                                .setCellValue("");
                    }
                    row.createCell(5)
                            .setCellValue(match.getPerson().getMail());
                    row.createCell(6)
                            .setCellValue(match.getPerson().getPhoneNumber());
                    row.createCell(7)
                            .setCellValue(match.getPerson().getActivity());
                    if(match.getCamera()!= null)
                    {
                        row.createCell(8)
                                .setCellValue(match.getCamera().getValue());
                    }
                    else{
                        row.createCell(8)
                                .setCellValue("");
                    }
                    if(inOut.size()>0)
                    {
                        row.createCell(9)
                                .setCellValue(inOut.get(0).getHour().toString());
                    }
                    else{
                        row.createCell(9)
                                .setCellValue("");
                    }
                    if(inOut.size()>1)
                    {
                        row.createCell(10)
                                .setCellValue(inOut.get(1).getHour().toString());
                        row.createCell(11)
                                .setCellValue(Duration.between(inOut.get(0).getHour(), inOut.get(1).getHour()).toHours());
                    }
                    else{
                        row.createCell(10)
                                .setCellValue("");
                        row.createCell(11)
                                .setCellValue("");
                    }
                    for (Contact contact:contacts
                         ) {
                        if(contact.getMatches().size()>1)
                        {
                            List<Person> ready = new ArrayList<>();
                            Integer customerId = match.getPerson().getId();
                            int count = 0;
                            for (Match matchContact:contact.getMatches()
                            ) {
                                if(!matchContact.getPerson().getId().equals(customerId) && !ready.contains(matchContact.getPerson()) )
                                {
                                    row.createCell(12 + count)
                                            .setCellValue(matchContact.getPerson().getFirstName());
                                    count++;
                                    ready.add(matchContact.getPerson());
                                }
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < columns.length; i++) {
                mySheet.autoSizeColumn(i);
            }
            return myWorkBook;
        }catch (Exception e){
            throw new ExportFileException("Cant create excel file with the data", e);
        }
    }

    private boolean customerWrite(List<Person> readies, Person person) {
        for (Person ready :readies
                ) {
            if(person.getId().equals(ready.getId())) {
                return true;
            }
        }
        return false;
    }

    private List<Match> getListOfContacts(List<Match> matches, Person person) {
        List<Match> matchListWithoutCustomer = new ArrayList<>();
        for (Match match:matches
             ) {
            if(!match.getPerson().getId().equals(person.getId()))
            {
                matchListWithoutCustomer.add(match);
            }
        }
        return  matchListWithoutCustomer;
    }

    public  List<Contact> contactsBetweenCustomers(Date hour) {
        List<Contact> contacts = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(hour);
        Date oneHourLater = hour;
        for (int i = 0; i < 23; i++) {
            Contact contact = new Contact();
            calendar.set(Calendar.HOUR_OF_DAY, i);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            hour = calendar.getTime();
            calendar.set(Calendar.HOUR_OF_DAY, i+1);
            oneHourLater = calendar.getTime();

            Instant firstCurrent = hour.toInstant();
            Instant secondCurrent = oneHourLater.toInstant();
            LocalDateTime firstLocalDate = LocalDateTime.ofInstant(firstCurrent,
                    ZoneId.systemDefault());
            LocalDateTime secondLocalDate = LocalDateTime.ofInstant(secondCurrent,
                    ZoneId.systemDefault());
            contact.setHour(firstLocalDate);
            contact.setOneHourLater(secondLocalDate);
            contact.setMatches(matchDao.findMatchByHourBetween(firstLocalDate, secondLocalDate));
            contacts.add(contactDao.save(contact));
        }
        return contacts;
    }

    public  void writeXlsx(List<Match> matchList, String path, Date day) throws IOException {
        try{
            List<Contact> contacts = contactsBetweenCustomers(day);
            List<Match> matchesFilteredByCostumers = new ArrayList<>();
            List<Person> people = new ArrayList<>();
            for (Match match : matchList) {
                if(!people.contains(match.getPerson())) {
                    people.add(match.getPerson());
                    matchesFilteredByCostumers.add(match);
                }
            }
            XSSFWorkbook myWorkBook = writeOutputFile(matchesFilteredByCostumers, day, contacts);
            System.out.println("Path: " + path);
            FileOutputStream os = new FileOutputStream(path);
            myWorkBook.write(os);
            os.close();
            myWorkBook.close();
        }catch (Exception e){
            throw new ExportFileException("An error happened when the file has been exporting", e);
        }
        System.out.println("Writing on XLSX file Finished ...");
    }

    public Person byRut(String customerRut) {
        return personDao.findCustomerByRut(customerRut).get();
    }
}