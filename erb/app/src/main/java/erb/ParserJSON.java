package erb;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ParserJSON {
	
	public ParserJSON () {
		
	}
	
	public ArrayList<JSONObject> parseFile(String filePath, String whatToRetrieve) {
		ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		try {
			Object object = new JSONParser().parse(new FileReader(filePath));
			JSONObject jsonObject = (JSONObject) object;
			JSONArray jsonArray = (JSONArray) jsonObject.get(whatToRetrieve);
			Iterator<?> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				JSONObject goalObject = (JSONObject) iterator.next();
				jsonObjects.add(goalObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObjects;
	}
}
