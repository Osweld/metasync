package com.osweld.metasync.identityaccess.internal.application.port.out;

import com.osweld.metasync.shared.domain.model.vo.SchemaName;

public interface SchemaProvisionerPort {

    void ensureSchemaExists(SchemaName schemaName);
    void dropSchema(SchemaName schemaName);

}
