//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.neo4j.kernel.impl.coreapi;

import org.neo4j.graphdb.Transaction;
import org.neo4j.internal.kernel.api.Transaction.Type;
import org.neo4j.internal.kernel.api.security.SecurityContext;
import org.neo4j.kernel.api.KernelTransaction;
import org.neo4j.kernel.api.KernelTransaction.Revertable;
import org.neo4j.kernel.api.exceptions.Status;

import java.util.Map;
import java.util.Optional;

public interface InternalTransaction extends Transaction {

    KernelTransaction getKernelTransaction();

    Type transactionType();

    SecurityContext securityContext();

    Revertable overrideWith(SecurityContext var1);

    Optional<Status> terminationReason();

    void setMetaData(Map<String, Object> var1);
}
