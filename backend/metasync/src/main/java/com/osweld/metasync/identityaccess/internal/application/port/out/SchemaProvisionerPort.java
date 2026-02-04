package com.osweld.metasync.identityaccess.internal.application.port.out;

import com.osweld.metasync.identityaccess.internal.domain.model.tenant.SchemaName;

public interface SchemaProvisionerPort {

    void ensureSchemaExists(SchemaName schemaName);

}
