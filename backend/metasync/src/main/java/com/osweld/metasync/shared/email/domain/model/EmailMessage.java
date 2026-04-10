package com.osweld.metasync.shared.email.domain.model;

import java.util.Map;

public record EmailMessage(
    String recipient,
    String subject,
    EmailType emailType,
    Map<String, Object> templateModel
) {

}
