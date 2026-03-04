package com.osweld.metasync.shared.email.domain.model;

import java.util.Map;

public record EmailMessage(
    String recipient,
    String subject,
    String templateName,
    Map<String, Object> templateModel
) {

}
