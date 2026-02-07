package com.osweld.metasync.identityaccess.internal.infrastructure.persistence.tenant;

import java.time.LocalDateTime;
import java.util.UUID;

import com.osweld.metasync.identityaccess.internal.domain.model.tenant.PlanType;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.StatusType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tenants", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TenantJpaEntity {

    @Id
    @Column(name = "tenant_id", nullable = false, updatable = false)
    private UUID tenantId;
    @Column(name = "tenant_name", nullable = false, length = 100)
    private String tenantName;
    @Column(name = "tenant_alias", nullable = false, unique = true, length = 50)
    private String tenantAlias;
    @Column(name = "schema_name", nullable = false, unique = true, length = 63)
    private String schemaName;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusType status;
    @Enumerated(EnumType.STRING)
    @Column(name = "plan", nullable = false, length = 20)
    private PlanType plan;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; 
   
}
