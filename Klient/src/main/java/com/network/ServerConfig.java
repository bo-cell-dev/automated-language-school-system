package com.network;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServerConfig {

    private static final String CONFIG_FILE = "/server.properties";  // Путь относительно папки resources
    private static String serverIp;
    private static int serverPort;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = ServerConfig.class.getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                System.out.println("Ошибка: Не удается найти файл конфигурации " + CONFIG_FILE);
                return;
            }
            properties.load(input);
            serverIp = properties.getProperty("SERVER_IP", "127.0.0.1");
            serverPort = Integer.parseInt(properties.getProperty("SERVER_PORT", "8080"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getServerIp() {
        return serverIp;
    }

    public static int getServerPort() {
        return serverPort;
    }
}
