package spaceweare.tracking4d.SQL.services;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import spaceweare.tracking4d.FileManagement.service.FileStorageService;
import spaceweare.tracking4d.SQL.dao.CameraDao;
import spaceweare.tracking4d.SQL.dao.MatchDao;
import spaceweare.tracking4d.SQL.dao.PersonDao;
import spaceweare.tracking4d.SQL.dao.ReportDao;
import spaceweare.tracking4d.SQL.models.*;

import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReportService {

    private static final Logger logger = LoggerFactory.getLogger(Report.class);
    private final ReportDao reportDao;
    private final PersonDao personDao;
    private final MatchDao matchDao;
    private final CameraDao cameraDao;
    private final PersonService personService;
    private final MatchService matchService;
    private final FileStorageService fileStorageService;
    public ReportService(ReportDao reportDao, CameraDao cameraDao, PersonDao personDao, MatchDao matchDao, PersonService personService, MatchService matchService, FileStorageService fileStorageService) {
        this.reportDao = reportDao;
        this.personDao = personDao;
        this.matchDao = matchDao;
        this.cameraDao = cameraDao;
        this.personService = personService;
        this.matchService = matchService;
        this.fileStorageService = fileStorageService;
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

    public ByteArrayInputStream personsPdfReport() {
        List<Person> personList = personDao.findAllByUnknownAndDeleted(false, false);
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(60);
            table.setWidths(new int[]{1, 3, 3});

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase("Id", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Nombre", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Apellido", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            for (Person person : personList) {

                PdfPCell cell;

                cell = new PdfPCell(new Phrase(person.getId().toString()));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(person.getFirstName()));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(person.getLastName())));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);
            }

            PdfWriter.getInstance(document, out);
            document.open();
            document.add(table);

            document.close();

        } catch (DocumentException ex) {

            logger.error("Error occurred: {0}", ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    //public ByteArrayInputStream makePersonPdfReport(Date firstDay, Date lastDay, Boolean covid) {
    public ByteArrayInputStream makePersonPdfReport() {
        Date firstDay = new Date();
        Date lastDay = new Date();
        Boolean covid = false;
        Instant firstCurrent = firstDay.toInstant();
        Instant secondCurrent = lastDay.toInstant();
        LocalDateTime firstLocalDate = LocalDateTime.ofInstant(firstCurrent,
                ZoneId.systemDefault()).plusDays(-1);
        LocalDateTime secondLocalDate = LocalDateTime.ofInstant(secondCurrent,
                ZoneId.systemDefault()).plusDays(1);
        List<Match> matches;
        if(covid)
        {
            matches = matchDao.findAllByHighTemperature(true);
        }
        else{
            matches = matchDao.findMatchByHourBetween(firstLocalDate, secondLocalDate);
        }
        List<Contact> contacts = personService.contactsBetweenPersons(lastDay);

        List<Match> matchesFilteredByCostumers = new ArrayList<>();
        List<Person> people = new ArrayList<>();
        for (Match match : matches) {
            if(!people.contains(match.getPerson()) && !match.getPerson().getUnknown()) {
                people.add(match.getPerson());
                matchesFilteredByCostumers.add(match);
            }
        }
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{1, 1, 1, 1, 1, 1, 1});

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase("Nombre", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Apellido", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Correo", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Celular", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Ingreso", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Salida", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Nº contactos", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            for (Match match : matchesFilteredByCostumers) {
                Person person = match.getPerson();
                List<Match> inOut = matchService.getIncomeOutcome(lastDay, match.getPerson().getId());

                PdfPCell cell;

                cell = new PdfPCell(new Phrase(person.getFirstName()));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(person.getLastName()));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(person.getMail())));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPaddingRight(5);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(person.getPhoneNumber()));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                //Ingreso y salida

                if(inOut.size()>0) {
                    cell = new PdfPCell(new Phrase(inOut.get(0).getHour().toString()));
                    cell.setPaddingLeft(5);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
                else{
                    cell = new PdfPCell(new Phrase(""));
                    cell.setPaddingLeft(5);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }

                if(inOut.size()>1) {
                    cell = new PdfPCell(new Phrase(inOut.get(1).getHour().toString()));
                    cell.setPaddingLeft(5);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
                else{
                    cell = new PdfPCell(new Phrase(""));
                    cell.setPaddingLeft(5);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }

                //Número de contactos
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
                                /*row.createCell(13 + count)
                                        .setCellValue(contactCell);*/
                                count++;
                                ready.add(matchContact.getPerson());
                            }
                        }
                        cell = new PdfPCell(new Phrase(count+""));
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setPaddingRight(5);
                        table.addCell(cell);
                    }
                }
            }

            PdfWriter.getInstance(document, out);
            document.open();
            document.add(table);
            Path filePath = fileStorageService.getFileStorageLocation().resolve("output.pdf").normalize();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(out.toByteArray());
            IOUtils.copy(byteArrayInputStream, new FileOutputStream(filePath.toString()));
            document.close();

        } catch (DocumentException | FileNotFoundException ex) {

            logger.error("Error occurred: {0}", ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream makePlacesPdfReport(Date day) {
        Instant firstCurrent = day.toInstant();
        Instant secondCurrent = day.toInstant();
        LocalDateTime firstLocalDate = LocalDateTime.ofInstant(firstCurrent,
                ZoneId.systemDefault());
        LocalDateTime secondLocalDate = LocalDateTime.ofInstant(secondCurrent,
                ZoneId.systemDefault()).plusDays(1);

        //Se verifica si existen matchs, si es así existen datos de interes
        List<Match> matches = matchDao.findMatchByHourBetween(firstLocalDate, secondLocalDate);
        List<Match> matchesFilteredByCostumers = new ArrayList<>();
        List<Person> people = new ArrayList<>();
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(60);
            table.setWidths(new int[]{4, 3});

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase("Lugar", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Cantidad de personas que estuvieron", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);


            for (Camera camera : cameraDao.findAll()
            ) {
                for (Match match : camera.getMatchList()
                ) {
                    if (!people.contains(match.getPerson())) {
                        people.add(match.getPerson());
                        matchesFilteredByCostumers.add(match);
                    }
                }
                if(matchesFilteredByCostumers.size() >0)
                {
                    table = writePlace(matchesFilteredByCostumers, table);
                    matchesFilteredByCostumers.clear();
                    people.clear();
                }
            }
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(table);
            Path filePath = fileStorageService.getFileStorageLocation().resolve("output.pdf").normalize();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(out.toByteArray());
            IOUtils.copy(byteArrayInputStream, new FileOutputStream(filePath.toString()));
            document.close();

        } catch (DocumentException | FileNotFoundException ex) {

            logger.error("Error occurred: {0}", ex);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    public PdfPTable writePlace(List<Match> matchList, PdfPTable table)
    {
        int count = 0;
        for (Match match : matchList) {
            // Se descartan los desconocidos
            if (!match.getPerson().getUnknown()) {
                count += 1;
            }
        }

            PdfPCell cell;

            cell = new PdfPCell(new Phrase(matchList.get(0).getCamera().getValue()));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(count + ""));
            cell.setPaddingLeft(5);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        return table;
    }
}