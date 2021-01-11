package com.holdings.server.service.extra;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("emailSenderService")
public class EmailSenderService {
	private JavaMailSender javaMailSender;

	@Autowired
	public EmailSenderService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Async
	@Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 3000l, maxDelay = 2))
	public void sendSimpleEmail(SimpleMailMessage email) throws Exception {
		javaMailSender.send(email);
	}

	@Async
	@Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 3000l, maxDelay = 2))
	public void sendComplexEmail(String[] to, String from, String subject, String text) throws Exception {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = null;
		helper = new MimeMessageHelper(message, true);

		helper.setFrom(from);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text, true);

		javaMailSender.send(message);
	}
}
