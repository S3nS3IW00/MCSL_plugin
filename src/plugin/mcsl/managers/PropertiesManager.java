package plugin.mcsl.managers;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PropertiesManager {

    Properties properties;
    InputStream inputStream;
    File file;

    public PropertiesManager(File file) {
        this.file = file;
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(file.getAbsolutePath())) {
            properties.load(new InputStreamReader(fis, Charset.forName("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PropertiesManager(InputStream inputStream) {
        this.inputStream = inputStream;
        properties = new Properties();
        try {
            properties.load(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setProp(String key, String value) {
        try (FileOutputStream fos = new FileOutputStream(file.getAbsolutePath())) {
            properties.setProperty(key, value);
            properties.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setBoolProp(String key, boolean value) {
        try (FileOutputStream fos = new FileOutputStream(file.getAbsolutePath())) {
            properties.setProperty(key, (value ? "true" : "false"));
            properties.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProp(String key) {
        String value = "";
        value = properties.getProperty(key);
        return value;
    }

    public Integer getIntegerProp(String key) {
        try {
            return Integer.parseInt(getProp(key));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public boolean getBoolProp(String key) {
        return getProp(key).equalsIgnoreCase("true");
    }

    public void removeProp(File file, String key) {
        try (FileOutputStream fos = new FileOutputStream(file.getAbsolutePath())) {
            properties.remove(key);
            properties.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasProp(String key) {
        return properties.containsKey(key);
    }

    public void loadFile(File file) {
        properties.clear();
        try {
            properties.load(new InputStreamReader(new FileInputStream(file.getAbsolutePath()), Charset.forName("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }

    public List<Object> getKeys() {
        return new ArrayList<>(properties.keySet());
    }

    public List<Object> getValues() {
        return new ArrayList<>(properties.values());
    }

    public void close() {
        properties.clear();
    }

    public InputStream getFileInputStream() {
        return inputStream;
    }
}
