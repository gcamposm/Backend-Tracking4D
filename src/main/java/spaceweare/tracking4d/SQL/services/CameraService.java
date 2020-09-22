package spaceweare.tracking4d.SQL.services;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import spaceweare.tracking4d.Exceptions.ExportFileException;
import spaceweare.tracking4d.SQL.dao.CameraDao;
import spaceweare.tracking4d.SQL.models.*;

import java.awt.*;
import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class CameraService {

    private final CameraDao cameraDao;
    public CameraService(CameraDao cameraDao) {
        this.cameraDao = cameraDao;
    }

    public Camera create(Camera camera){
        return cameraDao.save(camera);
    }

    public Camera readById(Integer id){
        if(cameraDao.findById(id).isPresent()){
            return cameraDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Camera> readAll(){
        return cameraDao.findAll();
    }

    public Camera update(Camera camera, Integer id){
        if(cameraDao.findById(id).isPresent()){
            Camera cameraFound = cameraDao.findById(id).get();
            cameraFound.setValue(camera.getValue());
            cameraFound.setArea(camera.getArea());
            cameraFound.setMatchList(camera.getMatchList());
            return cameraDao.save(cameraFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(cameraDao.findById(id).isPresent()){
            cameraDao.delete(cameraDao.findById(id).get());
            return "deleted";
        }
        else {
            return null;
        }
    }

    public  void writeXlsx(List<Match> matchList, String path, Date day) throws IOException {
        try{
            // Se inicializa el archivo a para escribir
            XSSFWorkbook myWorkBook = new XSSFWorkbook();
            myWorkBook.createSheet("Sheet1");
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            FontUnderline fontUnderline = FontUnderline.SINGLE;
            CreationHelper createHelper = myWorkBook.getCreationHelper();
            XSSFCellStyle linkStyle = myWorkBook.createCellStyle();
            XSSFFont linkFont = myWorkBook.createFont();
            linkFont.setUnderline(XSSFFont.U_SINGLE);
            linkFont.setColor(new XSSFColor(Color.BLUE.brighter()));
            linkFont.setUnderline(fontUnderline);
            linkStyle.setFont(linkFont);

            // Se realiza un filtrado para obtener a las personas
            // captadas en un lugar
            List<Match> matchesFilteredByCostumers = new ArrayList<>();
            List<Person> people = new ArrayList<>();
            for (Camera camera: cameraDao.findAll()
                 ) {
                for (Match match:camera.getMatchList()
                     ) {
                    if(!people.contains(match.getPerson())) {
                        people.add(match.getPerson());
                        matchesFilteredByCostumers.add(match);
                    }
                }
                mySheet = writeOutputFile(matchesFilteredByCostumers, day, mySheet);
                matchesFilteredByCostumers.clear();
                people.clear();
            }

            FileOutputStream os = new FileOutputStream(path);
            myWorkBook.write(os);
            os.close();
            myWorkBook.close();
        }catch (Exception e){
            throw new ExportFileException("An error happened when the file has been exporting", e);
        }
        System.out.println("Writing on XLSX file Finished ...");
    }

    public XSSFSheet writeOutputFile(List<Match> matchList, Date day, XSSFSheet mySheet){
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

    public Integer getDetectionCamWithSplitCam4(Integer x, Integer y, Integer height, Integer width){
        //System.out.println("x: "+x+" y: "+y+" height: "+height+" width: "+width);
        Integer heightLimit = height/2;
        Integer widthLimit = width/2;
        if(y < heightLimit)
        {
            if(x<widthLimit)
            {
                //return cameraDao.findCameraById(1).get();
                return 1;
            }
            else{
                return 2;
            }
        }
        else {
            if (x < widthLimit) {
                return 3;
            } else {
                return 4;
            }
        }
    }

    public Camera getDetectionCamWithSplitCam5(Integer x, Integer y, Integer height, Integer width){
        //System.out.println("x: "+x+" y: "+y+" height: "+height+" width: "+width);
        Integer heightLimit = height/3;
        Integer widthLimit = width/2;
        if(y < heightLimit)
        {
            if(x<widthLimit)
            {
                return cameraDao.findCameraById(1).get();
            }
            else{
                return cameraDao.findCameraById(2).get();
            }
        }
        else {
            if (y < heightLimit * 2) {
                if (x < widthLimit) {
                    return cameraDao.findCameraById(3).get();
                } else {
                    return cameraDao.findCameraById(4).get();
                }
            } else {
                return cameraDao.findCameraById(5).get();
            }
        }
    }
}