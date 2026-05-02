package com.osweld.metasync.identityaccess.infrastructure.in.web;

import static org.mockito.ArgumentMatchers.refEq;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.osweld.metasync.identityaccess.application.port.in.ProvisionTenantCommand;
import com.osweld.metasync.identityaccess.application.usecase.ProvisionTenantUseCase;
import com.osweld.metasync.identityaccess.domain.model.shared.EmailAddress;
import com.osweld.metasync.identityaccess.domain.model.tenant.PlanType;
import com.osweld.metasync.identityaccess.domain.model.tenant.Tenant;
import com.osweld.metasync.shared.domain.model.vo.TenantId;
import com.osweld.metasync.identityaccess.domain.model.tenant.TenantPlan;
import com.osweld.metasync.identityaccess.infrastructure.in.web.controller.TenantProvisioningController;
import com.osweld.metasync.identityaccess.infrastructure.in.web.dto.ProvisionTenantRequest;
import com.osweld.metasync.shared.domain.model.vo.SchemaName;
import com.osweld.metasync.shared.domain.model.vo.TenantAlias;
import com.osweld.metasync.shared.domain.model.vo.TenantName;



@WebMvcTest(controllers =  TenantProvisioningController.class)
public class TenantProvisioningControllerTest {

    private final static String BASE_URL = "/api/v1/tenants/register";
    private final static String PROVISION_SUCCESS_MESSAGE = "Tenant provisioned successfully";

    private final TenantId tenantId = new TenantId(UUID.randomUUID());
    private final TenantAlias tenantAlias = new TenantAlias("acme-corp");
    private final SchemaName schemaName = new SchemaName("t_acme_corp");
    private final TenantName tenantName = new TenantName("Acme Corp");
    private final EmailAddress contactEmail = new EmailAddress("contact@acme-corp.com");
    private final TenantPlan tenantPlan = new TenantPlan(PlanType.FREE);
    private final Instant now = Instant.now();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @MockitoBean
    private ProvisionTenantUseCase provisionTenantUseCase;

    @Test
    @DisplayName("Should provision a new tenant successfully")
    void shouldProvisionNewTenantSuccessfully() throws Exception {

        ProvisionTenantRequest request = new ProvisionTenantRequest(
                tenantName.value(),
                "FREE",
                "John",
                "Doe",
                contactEmail.value(),
                "pasSword123$");

        Tenant mockTenant = Tenant.provision(
                tenantId,
                tenantAlias,
                schemaName,
                tenantName,
                contactEmail,
                tenantPlan,
                now);
        
        ProvisionTenantCommand expectedCommand = new ProvisionTenantCommand(
                request.tenantName(), request.planName(), request.ownerFirstName(),
                request.ownerLastName(), request.ownerEmail(), request.ownerPassword());
        

                given(provisionTenantUseCase.provisionTenant(refEq(expectedCommand)))
                .willReturn(mockTenant);


        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.tenantId").value(tenantId.value().toString()))
                .andExpect(jsonPath("$.status").value(mockTenant.getStatus().value().toString()))
                .andExpect(jsonPath("$.message").value(PROVISION_SUCCESS_MESSAGE));
    }


    @Test
    @DisplayName("Should return 400 Bad Request when request is invalid")
    void shouldReturnBadRequestForInvalidRequest() throws Exception {
        ProvisionTenantRequest invalidRequest = new ProvisionTenantRequest(
                "",
                "FREE",
                "John",
                "Doe",
                contactEmail.value(),
                "pasSword123$");

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isInternalServerError());
    }

}

