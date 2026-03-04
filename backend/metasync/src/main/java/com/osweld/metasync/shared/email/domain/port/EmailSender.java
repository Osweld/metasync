package com.osweld.metasync.shared.email.domain.port;

import com.osweld.metasync.shared.email.domain.model.EmailMessage;

public interface EmailSender {

    void send(EmailMessage message);

}
