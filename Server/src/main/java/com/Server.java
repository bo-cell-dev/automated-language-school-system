package com;

import com.network.ClientServer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
@RequiredArgsConstructor
@PropertySource("classpath:server.properties")
public class Server implements CommandLineRunner {

    private final ApplicationContext context;
    private final DataSource dataSource;

    private static int clientCount = 0;
    private static int monitoringInterval;
    private static final int MAX_CLIENTS = 15;

    public static synchronized void incrementClientCount() {
        if (clientCount < MAX_CLIENTS) {
            clientCount++;
            System.out.println("Клиентов подключено: " + clientCount);
        } else {
            System.out.println("Максимальное количество клиентов достигнуто.");
        }
    }

    public static synchronized void decrementClientCount() {
        clientCount--;
        System.out.println("Клиентов подключено: " + clientCount);
    }

    public static double getServerLoad() {
        double load = ((double) clientCount / MAX_CLIENTS) * 100;
        return Math.round(load * 100.0) / 100.0;
    }


    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }

    @Override
    public void run(String... args) {
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("Успешное подключение к базе данных!");
        } catch (SQLException e) {
            System.err.println("Ошибка при подключении к базе данных: " + e.getMessage());
            return;
        }

        monitoringInterval = Integer.parseInt(System.getProperty("MONITORING_INTERVAL", "10000"));
        int port = Integer.parseInt(System.getProperty("SERVER_PORT", "8080"));

        System.out.println("Сервер запускается на порте " + port);

        Timer monitoringTimer = new Timer();
        monitoringTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Текущее количество подключённых клиентов: " + clientCount);
                System.out.println("Загруженность сервера: " + getServerLoad() + "%");
            }
        }, 0, monitoringInterval);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер успешно запущен!");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                if (clientCount == MAX_CLIENTS) {
                    try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                        out.println("Внимание! Максимальное количество клиентов на сервере достигнуто. Подключение отклонено.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Подключение отклонено: сервер переполнен.");
                    clientSocket.close();
                    continue;
                }
                System.out.println("Клиент подключился.");
                incrementClientCount();
                ClientServer clientServer = context.getBean(ClientServer.class);
                clientServer.setClientSocket(clientSocket);
                new Thread(clientServer).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
