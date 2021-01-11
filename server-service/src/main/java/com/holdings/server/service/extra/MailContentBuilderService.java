package com.holdings.server.service.extra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailContentBuilderService {
	public enum Template {
		SIGN_UP, RESET_PASSWORD
	}

	private TemplateEngine templateEngine;

	@Autowired
	public MailContentBuilderService(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	public String generateMailContent(String url, Template template) {
		Context context = new Context();
		switch (template) {
		case SIGN_UP:
			context.setVariable("confirm", url);
			return templateEngine.process("confirmMail", context);
//		default:
//			context.setVariable("confirm", url);
//			return templateEngine.process("resetPassword", context);
		default:
			return "";
		}
	}
}
