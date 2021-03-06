package com.holdings.server.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailContentBuilder {
	public enum Template {
		SIGN_UP, CONFIRM_AGAIN, RESET_PASSWORD
	}

	private TemplateEngine templateEngine;

	@Autowired
	public MailContentBuilder(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	public String generateMailContent(String url, Template template) {
		Context context = new Context();
		switch (template) {
		case SIGN_UP:
			context.setVariable("confirm", url);
			return templateEngine.process("confirmMail", context);
		case CONFIRM_AGAIN:
			context.setVariable("confirm", url);
			return templateEngine.process("confirmAgainMail", context);
//		default:
//			context.setVariable("confirm", url);
//			return templateEngine.process("resetPassword", context);
		default:
			return "";
		}
	}
}
