package com.osweld.metasync.shared.email.domain.port;

import java.util.Map;

public interface TemplateEngine {
    String render(String templateName, Map<String, Object> model);
}
