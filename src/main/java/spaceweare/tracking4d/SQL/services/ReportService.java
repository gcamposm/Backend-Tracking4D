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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.PersonDao;
import spaceweare.tracking4d.SQL.dao.ReportDao;
import spaceweare.tracking4d.SQL.models.Person;
import spaceweare.tracking4d.SQL.models.Report;
import java.util.List;

@Service
public class ReportService {

    private static final Logger logger = LoggerFactory.getLogger(Report.class);
    private final ReportDao reportDao;
    private final PersonDao personDao;
    public ReportService(ReportDao reportDao, PersonDao personDao) {
        this.reportDao = reportDao;
        this.personDao = personDao;
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
}