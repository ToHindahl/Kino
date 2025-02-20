package de.fhdw.Kino.DB;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserReceiverService {

    @Autowired
    private UserRepo userRepo;

    @RabbitListener(queues = RabbitMQDBConfig.QUEUE_NAME)
    public void receiveUserMessage(UserDTO userR) {
        System.out.println("Empfangen: " + userR);

        UserDTO user = new UserDTO();
        user.setVorname(userR.getVorname());

        userRepo.save(user);
        System.out.println("Gespeichert in DB: " + user);
    }
}