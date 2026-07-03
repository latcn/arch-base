package io.github.latcn.archbase.web.spring.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequestParamResolver {

	private final ObjectMapper objectMapper;

	@Autowired
	public RequestParamResolver(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public <T> T resolveCommand(HttpServletRequest request, Class<T> commandType) throws IOException {
		String body = request.getReader().lines().collect(Collectors.joining());
		return objectMapper.readValue(body, commandType);
	}

	public <T> T resolveQuery(HttpServletRequest request, Class<T> queryType) throws IOException {
		if (isJsonBody(request)) {
			return resolveCommand(request, queryType);
		}
		Map<String, String[]> params = request.getParameterMap();
		return objectMapper.convertValue(params, queryType);
	}

	private boolean isJsonBody(HttpServletRequest request) {
		String contentType = request.getContentType();
		return contentType != null && contentType.contains("application/json");
	}

}