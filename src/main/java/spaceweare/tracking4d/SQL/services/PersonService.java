package spaceweare.tracking4d.SQL.services;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.http.ResponseEntity;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class PersonService {

    private final PersonDao personDao;
    private final MatchDao matchDao;
    private final ContactDao contactDao;
    private final MatchService matchService;
    private final ImageService imageService;
    private final FileStorageService fileStorageService;
    public PersonService(PersonDao personDao, MatchDao matchDao, ContactDao contactDao, FileStorageService fileStorageService, MatchService matchService, ImageService imageService) {
        this.personDao = personDao;
        this.fileStorageService = fileStorageService;
        this.matchService = matchService;
        this.matchDao = matchDao;
        this.contactDao = contactDao;
        this.imageService = imageService;
    }

    public Object create(Person person){
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        String directory = absoluteFilePath + "/users/" + person.getRut();
        File directoryFile = new File(directory);
        if (! directoryFile.exists()){
            directoryFile.mkdir();
        }
        if(personDao.findPersonByRut(person.getRut()).isPresent()){
            Person personFound = personDao.findPersonByRut(person.getRut()).get();
            personFound.setUnknown(false);
            personFound.setDeleted(false);
            personFound.setToTrain(false);
            return imageService.pathsWithOnePerson(personDao.save(personFound));
        }
        person.setUnknown(false);
        return imageService.pathsWithOnePerson(personDao.save(person));
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
            personFound.setMail(person.getMail());
            personFound.setPhoneNumber(person.getPhoneNumber());
            personFound.setUser(person.getUser());
            personFound.setUnknown(person.getUnknown());
            personFound.setToTrain(person.getToTrain());
            personFound.setActual(person.getActual());
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
    public Object deleteByRut(String personRut) {
        if(personDao.findPersonByRut(personRut).isPresent()){
            Person person = personDao.findPersonByRut(personRut).get();
            person.setDeleted(true);
            personDao.save(person);
            return imageService.pathsWithPerson();
        }
        else{
            return  null;
        }
    }

    public XSSFWorkbook writeOutputFile(List<Match> matchList, Date day, List<Contact> contacts, Boolean covid){
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
            String[] columns = {"Nombre", "Apellido", "Rut", "Género", "Usuario", "Correo", "Celular", "Zona de trabajo", "Cámara", "Ingreso", "Salida", "Estadía (minutos)", "Nº contactos", "Contactos"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }
            for (Match match : matchList) {
                if(covid)
                {
                    if( (!match.getPerson().getUnknown()) && match.getPerson().getCovid()){
                        List<Match> inOut = matchService.getIncomeOutcome(day, match.getPerson().getId());
                        Row row = mySheet.createRow(rownum++);
                        Hyperlink link = (Hyperlink) createHelper.createHyperlink(HyperlinkType.URL);
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
                                    .setCellValue(Duration.between(inOut.get(0).getHour(), inOut.get(1).getHour()).toMinutes());
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
                                Integer personId = match.getPerson().getId();
                                int count = 0;
                                for (Match matchContact:contact.getMatches()
                                ) {
                                    if(!matchContact.getPerson().getId().equals(personId) && !ready.contains(matchContact.getPerson()) )
                                    {
                                        String contactCell = matchContact.getPerson().getFirstName() + " (" + matchContact.getPerson().getRut() + ")";
                                        row.createCell(13 + count)
                                                .setCellValue(contactCell);
                                        count++;
                                        ready.add(matchContact.getPerson());
                                    }
                                }
                                row.createCell(12)
                                        .setCellValue(count+"");
                            }
                        }
                    }
                }
                else{
                    if(!match.getPerson().getUnknown()){
                        List<Match> inOut = matchService.getIncomeOutcome(day, match.getPerson().getId());
                        Row row = mySheet.createRow(rownum++);
                        Hyperlink link = (Hyperlink) createHelper.createHyperlink(HyperlinkType.URL);
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
                                    .setCellValue(Duration.between(inOut.get(0).getHour(), inOut.get(1).getHour()).toMinutes());
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
                                Integer personId = match.getPerson().getId();
                                int count = 0;
                                for (Match matchContact:contact.getMatches()
                                ) {
                                    if(!matchContact.getPerson().getId().equals(personId) && !ready.contains(matchContact.getPerson()) )
                                    {
                                        String contactCell = matchContact.getPerson().getFirstName() + " (" + matchContact.getPerson().getRut() + ")";
                                        row.createCell(13 + count)
                                                .setCellValue(contactCell);
                                        count++;
                                        ready.add(matchContact.getPerson());
                                    }
                                }
                                row.createCell(12)
                                        .setCellValue(count+"");
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

    public  List<Contact> contactsBetweenPersons(Date hour) {
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

    public  void writeXlsx(List<Match> matchList, String path, Date day, Boolean covid) throws IOException {
        try{
            List<Contact> contacts = contactsBetweenPersons(day);
            List<Match> matchesFilteredByCostumers = new ArrayList<>();
            List<Person> people = new ArrayList<>();
            for (Match match : matchList) {
                if(!people.contains(match.getPerson())) {
                    people.add(match.getPerson());
                    matchesFilteredByCostumers.add(match);
                }
            }
            XSSFWorkbook myWorkBook = writeOutputFile(matchesFilteredByCostumers, day, contacts, covid);
            
            FileOutputStream os = new FileOutputStream(path);
            myWorkBook.write(os);
            os.close();
            myWorkBook.close();
        }catch (Exception e){
            throw new ExportFileException("An error happened when the file has been exporting", e);
        }
        System.out.println("Writing on XLSX file Finished ...");
    }

    public Object byRut(String personRut) {
        return imageService.pathsWithOnePerson(personDao.findPersonByRut(personRut).get());
    }

    public Object setActual(String rut) {
        if(personDao.findPersonByActual(true).isPresent()){
            Person actual = personDao.findPersonByActual(true).get();
            actual.setActual(false);
            personDao.save(actual);
        }
        if(personDao.findPersonByRut(rut).isPresent())
        {
            Person person = personDao.findPersonByRut(rut).get();
            person.setActual(true);
            return imageService.pathsWithOnePerson(personDao.save(person));
        }
        return null;
    }

    public Object getActual() {
        if(personDao.findPersonByActual(true).isPresent()){
            return imageService.pathsWithOnePerson(personDao.findPersonByActual(true).get());
        }
        return null;
    }

    private List<Match> getListOfContacts(List<Match> matches, Person person) {
        List<Match> matchListWithoutPerson = new ArrayList<>();
        for (Match match:matches
        ) {
            if(!match.getPerson().getId().equals(person.getId()))
            {
                matchListWithoutPerson.add(match);
            }
        }
        return  matchListWithoutPerson;
    }

    public Object createUnknown(String photoUnknown, List<Float> descriptors) throws IOException {
        Person unknown = new Person();
        unknown.setDeleted(false);
        unknown.setToTrain(false);
        unknown.setUnknown(true);
        
        personDao.save(unknown);
        String unknownID = "unknown"+unknown.getId();
        unknown.setFirstName(unknownID);
        unknown.setRut(unknownID);
        personDao.save(unknown);
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        String directory = absoluteFilePath + "/users/" + unknown.getRut();
        File directoryFile = new File(directory);
        if (! directoryFile.exists()){
            directoryFile.mkdir();
        }
        imageService.chargeData(descriptors, imageService.uploadPhotos(unknown, photoUnknown), unknown.getRut());
        return imageService.getAllFaces();
    }

    public Object prueba(String day, Integer personId) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(day);
        List<Match> inOut = matchService.getIncomeOutcome(date, personId);
        Duration duration = Duration.between(inOut.get(0).getHour(), inOut.get(1).getHour());
        long minutes = Duration.between(inOut.get(0).getHour(), inOut.get(1).getHour()).toMinutes();
        return minutes;
    }

    public List<Person> getUnknowns() {
        return personDao.findAllByUnknownAndDeleted(true, false);
    }

    public Person updateUnknown(Person personToUpdate, Integer id) {
        Person person = personDao.findById(id).get();
        person.setFirstName(personToUpdate.getFirstName());
        person.setLastName(personToUpdate.getLastName());
        person.setRut(personToUpdate.getRut());
        person.setActivity(personToUpdate.getActivity());
        person.setMail(personToUpdate.getMail());
        person.setPhoneNumber(personToUpdate.getPhoneNumber());
        person.setUnknown(false);
        return personDao.save(person);
    }

    public Person deleteAlert(String personRut) {
        Person person = personDao.findPersonByRut(personRut).get();
        person.setNewAlert(false);
        return personDao.save(person);
    }

    public Person personHighTemperature(Match match, String temperature){
        Person person = match.getPerson();
        person.setCovid(true);
        person.setNewAlert(true);
        person.setAlertTemperature(temperature);
        person.setLastMatchTime(match.getHour());
        return personDao.save(person);
    }
}