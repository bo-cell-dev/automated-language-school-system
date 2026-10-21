package com.network;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;

public class Client {
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private String serverIp;
    private int serverPort;

    public Client() throws Exception {
        loadProperties();
    }

    private void loadProperties() throws Exception {
        Properties props = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("server.properties");
        props.load(inputStream);
        serverIp = props.getProperty("SERVER_IP", "127.0.0.1");
        serverPort = Integer.parseInt(props.getProperty("SERVER_PORT", "8080"));
        System.out.println("Загружен IP: " + serverIp + ", порт: " + serverPort);
    }
    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }
    public void connect() throws Exception {
        try {
            System.out.println("Попытка подключиться к серверу " + serverIp + ":" + serverPort);
            socket = new Socket(serverIp, serverPort);
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(socket.getInputStream());

            System.out.println("Подключено к серверу " + serverIp + ":" + serverPort);
        } catch (Exception e) {
            System.err.println("Ошибка соединения: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Не удалось подключиться к серверу.");
        }
    }


    public Response sendRequest(Request request) throws Exception {
        try {
            output.writeObject(request);
            output.flush();

            Object response = input.readObject();
            if (response instanceof Response) {
                return (Response) response;
            } else {
                throw new Exception("Некорректный ответ от сервера");
            }
        } catch (Exception e) {
            System.err.println("Ошибка при отправке запроса: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Ошибка при отправке запроса");
        }
    }

    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                System.out.println("Отключение: отправка запроса DISCONNECT");
                output.writeObject(new Request(Operation.DISCONNECT));
                output.flush();
                socket.close();
                System.out.println("Отключено от сервера");
            } else {
                System.out.println("Сокет уже закрыт");
            }
        } catch (Exception e) {
            System.out.println("Ошибка при отключении:");
            e.printStackTrace();
        }
    }
}
