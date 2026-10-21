package com.network;

import com.Server;
import com.controller.*;
import com.dto.*;
import com.models.Lesson;
import com.models.Person;
import com.models.Registration;
import com.models.User;
import com.request.*;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import com.repository.UserRepository;
import com.repository.LessonRepository;
import com.repository.RegistrationRepository;


@Component
@Scope("prototype")
@RequiredArgsConstructor
@Log4j2
@Setter
public class ClientServer implements Runnable {
    private Socket clientSocket;
    private final RegistrationController registrationController;
    private final LoginController loginController;
    private final LessonController lessonController;

    private final ConnectionController connectionController;
    private UserDTO currentUser;


    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final RegistrationRepository registrationRepository;

    @Override
    public void run() {
        if (clientSocket == null) {
            throw new IllegalStateException("Сокет не установлен.");
        }

        if (!clientSocket.isClosed()) {
            try (
                    ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
                    ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream())
            ) {
                output.flush();
                boolean keepRunning = true;

                while (keepRunning) {
                    try {
                        Object receivedObject = input.readObject();
                        if (receivedObject instanceof Request request) {
                            Response response = processRequest(request);

                            if (request.getOperation() == Operation.DISCONNECT) {
                                keepRunning = false;
                                System.out.println("Отключил.");
                            }

                            output.writeObject(response);
                            output.flush();
                        } else {
                            Response errorResponse = new Response(false, "Неверный объект запроса");
                            output.writeObject(errorResponse);
                            output.flush();
                        }

                    } catch (EOFException e) {
                        System.out.println("Клиент закрыл соединение (EOF). Завершаем работу с клиентом.");
                        break;
                    } catch (IOException e) {
                        System.err.println("Ошибка соединения: " + e.getMessage());
                        break;
                    } catch (ClassNotFoundException e) {
                        System.err.println("Неизвестный класс: " + e.getMessage());
                        break;
                    } catch (Exception e) {
                        System.err.println("Внутренняя ошибка сервера: ");
                        e.printStackTrace();
                        break;
                    }
                }
            } catch (IOException e) {
                System.err.println("Ошибка при создании потоков ввода/вывода: " + e.getMessage());
            }
        } else {
            System.out.println("Сокет закрыт до начала обработки.");
        }

        closeConnection();
    }


    private Response processRequest(Request request) {
        switch (request.getOperation()) {
            case REGISTER:
                try {
                    RegistrationRequest registrationRequest = request.getRegistrationRequest();

                    RegistrationDTO registrationDTO = new RegistrationDTO(
                            registrationRequest.getLogin(),
                            registrationRequest.getPassword(),
                            registrationRequest.getFirstName(),
                            registrationRequest.getLastName(),
                            registrationRequest.getPatronymic()
                    );

                    String registrationResult = registrationController.register(registrationDTO);
                    boolean success = !"Логин уже занят".equals(registrationResult);

                    return new Response(success, registrationResult);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Response(false, "Ошибка при регистрации: " + e.getMessage());
                }

            case LOGIN:
                LoginRequest loginRequest = request.getLoginRequest();
                LoginDTO loginDTO = new LoginDTO(loginRequest.getLogin(), loginRequest.getPassword());
                Response loginResponse = loginController.login(loginDTO);
                if (loginResponse.isSuccess()) {
                    Object data = loginResponse.getData();
                    if (data instanceof UserDTO userDTO) {
                        this.currentUser = userDTO;
                        connectionController.addUser(userDTO);
                        System.out.println("Добавлен пользователь в connectedUsers: " + userDTO.getLogin());
                    }
                }
                return loginResponse;

            case ADD_LESSON:
                try {
                    EditLessonRequest editLessonRequest = request.getEditLessonRequest();
                    System.out.println("Получен запрос: " + request);
                    System.out.println("Поле data: " + request.getData());
                    System.out.println("Поле editLessonRequest: " + editLessonRequest);

                    if (editLessonRequest == null) {
                        return new Response(false, "Данные урока не переданы");
                    }

                    String result = lessonController.addLesson(editLessonRequest);
                    boolean success = "Урок добавлен".equals(result);

                    return new Response(success, result);

                } catch (Exception e) {
                    e.printStackTrace();
                    return new Response(false, "Ошибка при добавлении урока: " + e.getMessage());
                }

            case DELETE_LESSON: {
                Long lessonId = request.getLessonId();
                if (lessonId == null) {
                    return new Response(false, "ID урока не передан для удаления");
                }
                String result = lessonController.deleteLesson(lessonId);
                boolean success = "Урок удалён".equals(result);
                return new Response(success, result);
            }

            case UPDATE_LESSON: {
                EditLessonRequest2 editRequest = request.getEditLessonRequest2();

                if (editRequest == null) {
                    return new Response(false, "Данные для обновления урока отсутствуют или имеют неверный формат");
                }

                if (editRequest.getId() == null) {
                    return new Response(false, "ID урока не задан для обновления");
                }

                String result = lessonController.updateLesson(editRequest);
                boolean success = "Урок обновлён".equals(result);
                return new Response(success, result);
            }

            case GET_LESSONS:
                try {
                    List<LessonDTO> lessons = lessonController.getAllLessons();
                    return new Response(true, "Уроки загружены", (Serializable) lessons);

                } catch (Exception e) {
                    e.printStackTrace();
                    return new Response(false, "Ошибка при загрузке уроков: " + e.getMessage());
                }

            case GET_SCHEDULE:
                try {
                    ScheduleResponseDTO schedule = lessonController.getSchedule();
                    return new Response(true, "Расписание загружено", schedule);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Response(false, "Ошибка при загрузке расписания: " + e.getMessage());
                }

            case REGISTER_TO_LESSON: {
                RegisterToLessonRequest regReq = request.getRegisterToLessonRequest();

                if (regReq == null) {
                    return new Response(false, "Недопустимые данные: объект не является RegisterToLessonRequest.");
                }

                if (regReq.getLessonId() == null || regReq.getUser() == null) {
                    return new Response(false, "Недопустимые данные: пустые поля.");
                }

                Long lessonId = regReq.getLessonId();
                UserDTO userDTO = regReq.getUser();

                Optional<User> userOpt = userRepository.findById(userDTO.getId());
                if (userOpt.isEmpty()) {
                    return new Response(false, "Пользователь не найден.");
                }

                User currentUser = userOpt.get();

                Optional<Lesson> lessonOpt = lessonRepository.findById(lessonId);
                if (lessonOpt.isEmpty()) {
                    return new Response(false, "Урок не найден.");
                }

                Lesson lesson = lessonOpt.get();

                boolean exists = registrationRepository.existsByLessonIdAndUserId(lessonId, currentUser.getId());
                if (exists) {
                    return new Response(false, "Вы уже записаны на этот урок.");
                }

                Registration reg = new Registration();
                reg.setLesson(lesson);
                reg.setUser(currentUser);
                registrationRepository.save(reg);

                return new Response(true, "Запись успешна.");
            }
            case CANCEL_REGISTER_TO_LESSON: {
                RegisterToLessonRequest cancelReq = request.getRegisterToLessonRequest();

                if (cancelReq == null) {
                    return new Response(false, "Недопустимые данные: объект не является RegisterToLessonRequest.");
                }

                Long lessonId = cancelReq.getLessonId();
                UserDTO userDTO = cancelReq.getUser();

                if (lessonId == null || userDTO == null) {
                    return new Response(false, "Недопустимые данные: пустые поля.");
                }

                Optional<User> userOpt = userRepository.findById(userDTO.getId());
                if (userOpt.isEmpty()) {
                    return new Response(false, "Пользователь не найден.");
                }
                User currentUser = userOpt.get();

                Optional<Lesson> lessonOpt = lessonRepository.findById(lessonId);
                if (lessonOpt.isEmpty()) {
                    return new Response(false, "Урок не найден.");
                }

                // Проверяем, есть ли запись
                Optional<Registration> regOpt = registrationRepository.findByLessonIdAndUserId(lessonId, currentUser.getId());
                if (regOpt.isEmpty()) {
                    return new Response(false, "Вы не записаны на этот урок.");
                }

                Registration reg = regOpt.get();
                registrationRepository.delete(reg);

                return new Response(true, "Запись успешно отменена.");
            }

            case GET_USER_INFO: {
                try {
                    UserDTO currentUserDTO = (UserDTO) request.getData();
                    Optional<User> userOpt = userRepository.findById(currentUserDTO.getId());

                    if (userOpt.isEmpty()) {
                        return new Response(false, "Пользователь не найден.");
                    }

                    User user = userOpt.get();
                    Person person = user.getPerson();

                    PersonDTO personDTO = new PersonDTO(person);
                    UserDTO resultDTO = new UserDTO(
                            user.getId(),
                            user.getLogin(),
                            user.getPassword(),
                            personDTO,
                            new RoleDTO(user.getRole())
                    );

                    return new Response(true, resultDTO);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Response(false, "Ошибка при получении данных пользователя: " + e.getMessage());
                }
            }

            case GET_CONNECTED_USERS:
                return connectionController.getConnectedUsers();


            case GET_USERS_BY_LESSON: {
                Long lessonId = (Long) request.getData();
                if (lessonId == null) {
                    return new Response(false, "ID урока не передан");
                }

                try {
                    List<Registration> registrations = registrationRepository.findByLessonId(lessonId);

                    List<UserDTO> userDTOs = registrations.stream()
                            .map(Registration::getUser)
                            .map(user -> {
                                System.out.println("User: " + user.getLogin() + ", MiddleName: " + (user.getPerson() != null ? user.getPerson().getMiddleName() : "null"));
                                return new UserDTO(
                                        user.getId(),
                                        user.getLogin(),
                                        user.getPassword(),
                                        new PersonDTO(user.getPerson()),  // Проверь этот конструктор
                                        new RoleDTO(user.getRole())
                                );
                            })
                            .toList();


                    return new Response(true, "Пользователи загружены", (Serializable) userDTOs);

                } catch (Exception e) {
                    e.printStackTrace();
                    return new Response(false, "Ошибка при получении списка пользователей: " + e.getMessage());
                }
            }


            default:
                return new Response(true, "Запрос обработан успешно");
        }
    }


    private void closeConnection() {
        if (clientSocket != null && !clientSocket.isClosed()) {
            try {
                // Закрытие сокета
                clientSocket.close();
                System.out.println("Сокет закрыт.");
            } catch (IOException e) {
                System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }

        Server.decrementClientCount();
        connectionController.removeUser(currentUser);
        System.out.println("Соединение с клиентом закрыто, счётчик клиентов уменьшен.");
    }
}
