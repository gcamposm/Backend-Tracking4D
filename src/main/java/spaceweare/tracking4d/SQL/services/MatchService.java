package spaceweare.tracking4d.SQL.services;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import spaceweare.tracking4d.Exceptions.ExportFileException;
import spaceweare.tracking4d.SQL.dao.CameraDao;
import spaceweare.tracking4d.SQL.dao.ContactDao;
import spaceweare.tracking4d.SQL.dao.PersonDao;
import spaceweare.tracking4d.SQL.dao.MatchDao;
import spaceweare.tracking4d.SQL.models.Camera;
import spaceweare.tracking4d.SQL.models.Person;
import spaceweare.tracking4d.SQL.models.Match;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

@Service
public class MatchService {

    private final MatchDao matchDao;
    private final PersonDao personDao;
    private final CameraDao cameraDao;
    private final ContactDao contactDao;
    public MatchService(MatchDao matchDao, PersonDao personDao, CameraDao cameraDao, ContactDao contactDao) {
        this.matchDao = matchDao;
        this.personDao = personDao;
        this.cameraDao = cameraDao;
        this.contactDao = contactDao;
    }

    public Match create(Match match){
        return matchDao.save(match);
    }

    public Match readById(Integer id){
        if(matchDao.findById(id).isPresent()){
            return matchDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Match> readAll(){
        return matchDao.findAll();
    }

    public Match update(Match match, Integer id){
        if(matchDao.findById(id).isPresent()){
            Match matchFound = matchDao.findById(id).get();
            matchFound.setName(match.getName());
            matchFound.setCompany(match.getCompany());
            matchFound.setHour(match.getHour());
            matchFound.setPerson(match.getPerson());
            matchFound.setTemperature(match.getTemperature());
            return matchDao.save(matchFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(matchDao.findById(id).isPresent()){
            matchDao.delete(matchDao.findById(id).get());
            return "deleted";
        }
        else{
            return  null;
        }
    }

    public String createByDetection(String match) {
        String[] data = match.split("=");
        match = data[1];
        data = match.split("&");
        String name = data[0];
        data = name.split(java.util.regex.Pattern.quote("+"));
        String firstName = data[0];
        String lastName = data[1];
        Person person = personDao.findPersonByFirstNameAndLastName(firstName, lastName).get();
        if(person != null)
        {
            return person.getFirstName();
        }
        return "No encontré al pinche wero, debe ser NN";
    }

    public String registerDetection(String detection) {
        System.out.println(detection);
        return detection;
    }

    public List<Match> withFilteredMatches(List<String> rutList, Integer cameraId) {
        // Se maneja la hora actual
        Calendar cal = Calendar.getInstance();
        Integer hour = cal.get(Calendar.HOUR);
        cal.set(Calendar.HOUR, hour - 4);
        Date date = cal.getTime();
        LocalDateTime now = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        // Se crean los match según los ruts de las personas detectadas
        List<Match> matches = new ArrayList<>();
        for (String rut:rutList
             ) {
            if(personDao.findPersonByRut(rut).isPresent()){
                Match match = new Match();
                match.setPerson(personDao.findPersonByRut(rut).get());
                match.setHour(now);
                if(cameraDao.findCameraById(cameraId).isPresent())
                {
                    match.setCamera(cameraDao.findCameraById(cameraId).get());
                }
                matchDao.save(match);
                matches.add(match);
            }
        }
        return matches;
    }

    public List<Match> getMatchesByDate(Date firstDate, Date secondDate) {
        Instant firstCurrent = firstDate.toInstant();
        Instant secondCurrent = secondDate.toInstant();
        LocalDateTime firstLocalDate = LocalDateTime.ofInstant(firstCurrent,
                ZoneId.systemDefault());
        LocalDateTime secondLocalDate = LocalDateTime.ofInstant(secondCurrent,
                ZoneId.systemDefault()).plusDays(1);
        return matchDao.findMatchByHourBetween(firstLocalDate, secondLocalDate);
    }

    public List<Match> findMatchByInterval(Integer interval, String dateString) throws ParseException {
        // Según el intervalo se definen las fechas de búsqueda
        // Se maneja la hora actual
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date secondDate = formatter.parse(dateString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(secondDate);
        Integer second = cal.get(Calendar.SECOND);
        cal.set(Calendar.SECOND, second - interval);
        Date firstDate = cal.getTime();
        LocalDateTime firstCurrentDate = LocalDateTime.ofInstant(firstDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime secondCurrentDate = LocalDateTime.ofInstant(secondDate.toInstant(), ZoneId.systemDefault());
        // Se buscan los matchs que coincidan
        List<Match> matchListByDate = matchDao.findMatchByHourBetween(firstCurrentDate, secondCurrentDate);
        List<Match> matchListByDateAndCovid = new ArrayList<>();
        for (Match match:matchListByDate
             ) {
            if (match.getCamera().getIsCovidCamera()){
                matchListByDateAndCovid.add(match);
            }
        }
        return matchListByDateAndCovid;
    }

    public List<Match> filterByPerson(List<Match> matchListPerDay, Integer personId) {
        List<Match> matchListByPerson = new ArrayList<>();
        for (Match match:matchListPerDay
             ) {
            if(match.getPerson().getId().equals(personId)) {
                matchListByPerson.add(match);
            }
        }
        return matchListByPerson;
    }

    public List<Match> getIncomeOutcome(Date day, Integer personId) {
        System.out.println(("La fecha es: "+ day.toString()));
        List<Match> matchListPerDay = getMatchesByDate(day, day);
        List<Match> matchListByPerson = filterByPerson(matchListPerDay, personId);
        Integer matchIdIn = 0;
        Integer matchIdOut = 0;
        for (Match match:matchListByPerson
             ) {
                if (matchIdIn.equals(0)) {
                    matchIdIn = match.getId();
                }
                if (matchIdOut.equals(0)) {
                    matchIdOut = match.getId();
                }
                if (matchDao.findById(matchIdIn).get().getHour().compareTo(match.getHour()) > 0) {
                    matchIdIn = match.getId();
                }
                if (matchDao.findById(matchIdOut).get().getHour().compareTo(match.getHour()) < 0) {
                    matchIdOut = match.getId();
                }
        }
        List<Match> matchList = new ArrayList<>();
        if(matchDao.findById(matchIdIn).isPresent())
        {
            Match matchIn = matchDao.findById(matchIdIn).get();
            matchList.add(matchIn);
        }
        if(matchDao.findById(matchIdOut).isPresent())
        {
            Match matchOut = matchDao.findById(matchIdOut).get();
            matchList.add(matchOut);
        }
        return matchList;
    }

    public List<Match> getAllByContact(Integer contactId) {
        if(contactDao.findById(contactId).isPresent()) {
            return matchDao.findAllByContact(contactDao.findById(contactId).get());
        }
        return new ArrayList<>();
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public List<Map<Object, Object>> getMatchesByDateWithRandomLocation(Date firstDate, Date secondDate) {
        List<Match> matchList = getMatchesByDate(firstDate, secondDate);
        List<Map<Object, Object>> matches = new ArrayList<>();
        for (Match match:matchList
             ){
            Map<Object, Object> matchInHash = new HashMap<>();
            matchInHash.put("match", match);
            matchInHash.put("top", getRandomNumber(70, 85));
            matchInHash.put("left", getRandomNumber(33, 50));
            matches.add(matchInHash);
        }
        return matches;
    }

    public Object alerts() {
        List<Match> matchList = matchDao.findAllByHighTemperature(true);
        List<Person> personList = new ArrayList<>();
        for (Match match:matchList
             ) {
            if(!personList.contains(match.getPerson()))
            {
                personList.add(match.getPerson());
            }
        }
        return personList;
    }

    public  void writeXlsx(List<Match> matchList, String path) throws IOException {
        try{
            // Se inicializa el archivo a para escribir
            XSSFWorkbook myWorkBook = new XSSFWorkbook();
            myWorkBook.createSheet("Sheet1");
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            FontUnderline fontUnderline = FontUnderline.SINGLE;
            XSSFCellStyle linkStyle = myWorkBook.createCellStyle();
            XSSFFont linkFont = myWorkBook.createFont();
            linkFont.setUnderline(XSSFFont.U_SINGLE);
            linkFont.setColor(new XSSFColor(Color.BLUE.brighter()));
            linkFont.setUnderline(fontUnderline);
            linkStyle.setFont(linkFont);

            // Se realiza un filtrado para obtener a las personas
            // captadas en un lugar
                mySheet = writeOutputFile(matchList, mySheet);


            FileOutputStream os = new FileOutputStream(path);
            myWorkBook.write(os);
            os.close();
            myWorkBook.close();
        }catch (Exception e){
            throw new ExportFileException("An error happened when the file has been exporting", e);
        }
        System.out.println("Writing on XLSX file Finished ...");
    }
    public XSSFSheet writeOutputFile(List<Match> matchList, XSSFSheet mySheet){
        try {
            // Se obtiene el valor de la última fila y se agrega uno para añadir nuevos datos
            int rownum = mySheet.getLastRowNum();
            Row headerRow = mySheet.createRow(rownum++);

            // Se defienen las cabeceras de las columnas
            String[] columns = {"Lugar", "Cantidad de personas que estuvieron", "Personas que estuvieron"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Se procede a escribir los datos recabados
            // Se crea la nueva fila
            Row row = mySheet.createRow(rownum++);
            // Se escribe primero el lugar, que es el mismo en
            // en toda la lista
            row.createCell(0)
                    .setCellValue(matchList.get(0).getCamera().getValue());
            for (Match match : matchList) {
                // Se descartan los desconocidos
                int count = 0;
                if(!match.getPerson().getUnknown()){
                    row.createCell(2 + count)
                            .setCellValue(match.getPerson().getLastName());
                    count += 1;
                }
                row.createCell(1)
                        .setCellValue(count+"");
            }
            for (int i = 0; i < columns.length; i++) {
                mySheet.autoSizeColumn(i);
            }
            return mySheet;
        }catch (Exception e){
            throw new ExportFileException("Cant create excel file with the data", e);
        }
    }
}