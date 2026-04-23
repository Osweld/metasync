package com.osweld.metasync.shared.email.domain.model;


public enum EmailType {
    ACCOUNT_ACTIVATION("Account activation", "account-activation.ftl"),;

    private final String type;
    private final String templateURI;

    EmailType(String type, String templateURI) {
        this.type = type;
        this.templateURI = templateURI;
    }

    public String getType() {
        return type;
    }

    public String getTemplateURI() {
        return templateURI;
    }


    
}
