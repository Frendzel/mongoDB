package pl.lodz.sda;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.lang.Integer.valueOf;

public class PropertiesLoader {

    public static String PATH = "connection.properties";
    Properties prop = new Properties();

    public void init() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PATH);

        if (inputStream != null) {
            try {
                prop.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                inputStream.close();
            }
        } else {
            throw new FileNotFoundException("property file '" + PATH + "' not found in the classpath");
        }
    }

    public String getUser() {
        return getProperty("our.super.mongodb.user");
    }

    public String getPass() {
        return getProperty("our.super.mongodb.password");
    }

    public String getHost() {
        return getProperty("our.super.mongodb.host");
    }

    public int getPort() {
        return valueOf(getProperty("our.super.mongodb.port"));
    }

    private String getProperty(String key) {
        return prop.getProperty(key);
    }

}
