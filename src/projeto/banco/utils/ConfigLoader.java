package projeto.banco.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {

	private Properties props;

	private static ConfigLoader instance;

	private ConfigLoader(String path) {
		try {
			FileInputStream fis = new FileInputStream(path);
			props = new Properties();
			props.load(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ConfigLoader getInstance(String path) {
		if (instance != null) {
			return instance;
		} else {
			return new ConfigLoader(path);
		}
	}

	public String getProperty(String prop) {
		return props.getProperty(prop);
	}

	public String getProperty(String prop, String defaultValue) {
		return props.getProperty(prop);
	}

}
