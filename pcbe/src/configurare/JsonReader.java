package configurare;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.google.gson.Gson;
public class JsonReader {
	private ConfigurationDetails configurare;
	public JsonReader() {
		 try {   
	            Reader reader = Files.newBufferedReader(Paths.get("C:\\Users\\diana\\git\\PCBE_project\\pcbe\\Configurare.json"));
	            Gson gson = new Gson();
	            setConfigurare(gson.fromJson(reader,ConfigurationDetails.class));
	            reader.close();
	            
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	}
	public ConfigurationDetails getConfigurare() {
		return configurare;
	}
	private void setConfigurare(ConfigurationDetails configurare) {
		this.configurare = configurare;
	}
}
