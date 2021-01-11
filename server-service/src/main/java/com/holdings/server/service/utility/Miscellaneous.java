package com.holdings.server.service.utility;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class Miscellaneous {
	@Autowired
	private ApplicationContext appContext;

	/*
	 * Get mapping path by method name.
	 */
	public String getRequestMappingPath(String target) {
		Map<RequestMappingInfo, HandlerMethod> methods = appContext.getBean(RequestMappingHandlerMapping.class)
				.getHandlerMethods().entrySet().stream().filter(x -> x.getValue().toString().contains(target))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		if (!methods.entrySet().iterator().hasNext()) {
			return "";
		}

		String key = methods.entrySet().iterator().next().getKey().getPatternsCondition().toString();
		key = key.substring(1, key.length() - 1);

		return key;
	}
}
