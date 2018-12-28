package isa.project.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Qualifier("getJavaMailSender")
	@Autowired
	JavaMailSender sender;

	@Async
	public void sendNotificaitionAsync(String recipientEmail, String subject, String message)
			throws MailException, InterruptedException, MessagingException {

		MimeMessage msg = sender.createMimeMessage();
		msg.setContent(message, "text/html");
		MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");

		helper.setTo(recipientEmail);
		helper.setFrom("isa.team21.2019@gmail.com");
		helper.setSubject(subject);
		helper.setText(message, true);
		sender.send(msg);
	}
}