package spaceweare.tracking4d.SQL.services;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import spaceweare.tracking4d.Exceptions.ExportFileException;
import spaceweare.tracking4d.FileManagement.service.FileStorageService;
import spaceweare.tracking4d.SQL.dao.ContactDao;
import spaceweare.tracking4d.SQL.dao.CustomerDao;
import spaceweare.tracking4d.SQL.dao.ImageDao;
import spaceweare.tracking4d.SQL.dao.MatchDao;
import spaceweare.tracking4d.SQL.models.Contact;
import spaceweare.tracking4d.SQL.models.Customer;
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
public class CustomerService {

    private final FileStorageService fileStorageService;
    private final ImageDao imageDao;
    private final CustomerDao customerDao;
    private final MatchDao matchDao;
    private final ContactDao contactDao;
    private final MatchService matchService;
    public CustomerService(CustomerDao customerDao, ImageDao imageDao, MatchDao matchDao, ContactDao contactDao, FileStorageService fileStorageService, MatchService matchService) {
        this.customerDao = customerDao;
        this.imageDao = imageDao;
        this.fileStorageService = fileStorageService;
        this.matchService = matchService;
        this.matchDao = matchDao;
        this.contactDao = contactDao;
    }

    public Customer create(Customer customer){
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        String directory = absoluteFilePath + "/" + customer.getRut();
        File directoryFile = new File(directory);
        if (! directoryFile.exists()){
            directoryFile.mkdir();
        }
        return customerDao.save(customer);
    }

    public Customer readById(Integer id){
        if(customerDao.findById(id).isPresent()){
            return customerDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Customer> readAll(){
        return customerDao.findAll();
    }

    public Customer update(Customer customer, Integer id){
        if(customerDao.findById(id).isPresent()){
            Customer customerFound = customerDao.findById(id).get();
            customerFound.setFirstName(customer.getFirstName());
            customerFound.setLastName(customer.getLastName());
            customerFound.setRut(customer.getRut());
            customerFound.setActivity(customer.getActivity());
            customerFound.setCompany(customer.getCompany());
            customerFound.setGenre(customer.getGenre());
            customerFound.setImages(customer.getImages());
            customerFound.setMail(customer.getMail());
            customerFound.setPhoneNumber(customer.getPhoneNumber());
            customerFound.setUser(customer.getUser());
            customerFound.setUnknown(customer.getUnknown());
            return customerDao.save(customerFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(customerDao.findById(id).isPresent()){
            customerDao.delete(customerDao.findById(id).get());
            return "deleted";
        }
        else{
            return  null;
        }
    }

    public Customer uploadImage(Customer customerToUpdate, String name, byte[] fileBytes) throws IOException {
        String ext = name.substring(name.lastIndexOf("."));
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        Integer index = 0;
        if (imageDao.findTopByOrderByIdDesc() != null)
        {
            index = imageDao.findTopByOrderByIdDesc().getId()+1;
        }
        String fileName = customerToUpdate.getRut() + "_" + index.toString();
        File convertFile = new File(absoluteFilePath + "/" + fileName + ext);
        try(FileOutputStream fos = new FileOutputStream(convertFile)) {
            byte[] bytes = fileBytes;
            fos.write(bytes);
            ImageService.createImageWithCustomer(customerToUpdate, ext, fileName);
            return customerToUpdate;
        }catch(IOException IEX){
            return null;
        }
    }

    public Customer register(String name) {
        String[] data = name.split(" ");
        String firstName = data[0];
        String lastName = data[1];
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        String directory = absoluteFilePath + "/" + firstName + " " + lastName;
        File directoryFile = new File(directory);
        if (! directoryFile.exists()){
            directoryFile.mkdir();
        }
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        return customerDao.save(customer);
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
            String[] columns = {"Nombre", "Apellido", "Rut", "Género", "Usuario", "Correo", "Celular", "Zona de trabajo", "Ingreso", "Salida", "Estadía", "Cámara", "Contactos"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            for (Match match : matchList) {
                if(!match.getCustomer().getUnknown()){
                    System.out.println("Creando");
                    List<Match> inOut = matchService.getIncomeOutcome(day, match.getCustomer().getId());
                    Row row = mySheet.createRow(rownum++);
                    Hyperlink link = (Hyperlink) createHelper.createHyperlink(HyperlinkType.URL);
                    //path = URLEncoder.encode(path, "UTF-8");
                    //link.setAddress(match.getCustomer().getFirstName());
                    row.createCell(0)
                            .setCellValue(match.getCustomer().getFirstName());
                    row.createCell(1)
                            .setCellValue(match.getCustomer().getLastName());
                    row.createCell(2)
                            .setCellValue(match.getCustomer().getRut());
                    //INT VALUES
                    row.createCell(3)
                            .setCellValue(match.getCustomer().getGenre());
                    if(match.getCustomer().getUser() != null)
                    {
                        row.createCell(4)
                                .setCellValue( match.getCustomer().getUser().getUsername());
                    }
                    else{
                        row.createCell(4)
                                .setCellValue("");
                    }
                    row.createCell(5)
                            .setCellValue(match.getCustomer().getMail());
                    row.createCell(6)
                            .setCellValue(match.getCustomer().getPhoneNumber());
                    row.createCell(7)
                            .setCellValue(match.getCustomer().getActivity());
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
                            List<Customer> ready = new ArrayList<>();
                            Integer customerId = match.getCustomer().getId();
                            int count = 0;
                            for (Match matchContact:contact.getMatches()
                            ) {
                                if(!matchContact.getCustomer().getId().equals(customerId) && !ready.contains(matchContact.getCustomer()) )
                                {
                                    row.createCell(12 + count)
                                            .setCellValue(matchContact.getCustomer().getFirstName());
                                    count++;
                                    ready.add(matchContact.getCustomer());
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

    private boolean customerWrite(List<Customer> readies, Customer customer) {
        for (Customer ready :readies
                ) {
            if(customer.getId().equals(ready.getId())) {
                return true;
            }
        }
        return false;
    }

    private List<Match> getListOfContacts(List<Match> matches, Customer customer) {
        List<Match> matchListWithoutCustomer = new ArrayList<>();
        for (Match match:matches
             ) {
            if(!match.getCustomer().getId().equals(customer.getId()))
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
            List<Customer> customers = new ArrayList<>();
            for (Match match : matchList) {
                if(!customers.contains(match.getCustomer())) {
                    customers.add(match.getCustomer());
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
}