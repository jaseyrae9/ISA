package isa.project.listener;


import java.util.UUID;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import isa.project.event.OnRegistrationCompleteEvent;
import isa.project.model.users.Customer;
import isa.project.service.EmailService;
import isa.project.service.VerificationTokenService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {


    @Autowired
    VerificationTokenService tokenService;

    @Autowired
    EmailService emailService;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        try {
            this.confirmRegistration(event);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) throws MessagingException {
        Customer customer = event.getCustomer();
        String token = UUID.randomUUID().toString();

        tokenService.createVerificationToken(customer, token);

        String recipientMail = customer.getEmail();
        String subject = "Potvrda registracije";
        String confirmationUrl = "http://localhost:8080" + event.getUrl() + "/confirmRegistration?token=" + token;
        //String link = "<a href='"+ confirmationUrl +"'>Kliknite ovdje</a>";

        String message = "<html><body>Kliknite na sledeci link kako biste aktivirali nalog<br>" + confirmationUrl + "</body></html>";

        try {
            emailService.sendNotificaitionAsync(recipientMail, subject, message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}