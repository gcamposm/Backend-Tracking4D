package drii.pingeso.backend.Email.Desk;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JSONFileReader {
    public List<String> readEmailConfigJSON() {
        JSONParser parser = new JSONParser();
        List<String> result = new ArrayList<String>();

        try {
            Path path = Paths.get(getClass().getResource("/files/emailConfiguration.json").toURI());
            Object obj = parser.parse(new FileReader(path.toString()));
            JSONObject jsonObject = (JSONObject) obj;

            String email = (String) jsonObject.get("emailFrom");
            String password = (String) jsonObject.get("password");
            result.add(email);
            result.add(password);
            return result;
        }
        catch(Exception e){
            System.out.println("Error while reading JSON file.");
            return result;
        }
    }
}