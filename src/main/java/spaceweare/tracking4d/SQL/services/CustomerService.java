package spaceweare.tracking4d.SQL.services;

import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import spaceweare.tracking4d.Exceptions.ExportFileException;
import spaceweare.tracking4d.FileManagement.service.FileStorageService;
import spaceweare.tracking4d.SQL.dao.CustomerDao;
import spaceweare.tracking4d.SQL.dao.ImageDao;
import spaceweare.tracking4d.SQL.models.Customer;
import org.apache.poi.ss.usermodel.*;
import spaceweare.tracking4d.SQL.models.Match;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

@Service
public class CustomerService {

    private final FileStorageService fileStorageService;
    private final ImageDao imageDao;
    private final CustomerDao customerDao;
    public CustomerService(CustomerDao customerDao, ImageDao imageDao, FileStorageService fileStorageService) {
        this.customerDao = customerDao;
        this.imageDao = imageDao;
        this.fileStorageService = fileStorageService;
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
    public XSSFWorkbook writeOutputFile(List<Match> customerList){
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
            String[] columns = {"Nombre", "Apellido", "Rut", "Género", "Usuario", "Correo", "Celular", "Zona de trabajo"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            for (Match match : customerList) {
                if(!match.getCustomer().getUnknown()){
                    Row row = mySheet.createRow(rownum++);
                    Hyperlink link = (Hyperlink) createHelper.createHyperlink(HyperlinkType.URL);
                    //path = URLEncoder.encode(path, "UTF-8");
                    link.setAddress(match.getCustomer().getFirstName());
                    row.createCell(0)
                            .setCellValue(match.getCustomer().getLastName());
                    row.createCell(1)
                            .setCellValue(match.getCustomer().getRut());
                    //INT VALUES
                    row.createCell(2)
                            .setCellValue(match.getCustomer().getGenre());
                    row.createCell(3)
                            .setCellValue( match.getCustomer().getUser().getUsername());
                    row.createCell(4)
                            .setCellValue(match.getCustomer().getMail());
                    row.createCell(5)
                            .setCellValue(match.getCustomer().getPhoneNumber());
                    row.createCell(6)
                            .setCellValue(match.getCustomer().getActivity().getName());

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

    public  void writeXlsx(List<Match> matchList, String path) throws IOException {
        try{
            XSSFWorkbook myWorkBook = writeOutputFile(matchList);
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