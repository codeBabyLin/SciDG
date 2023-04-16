//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.neo4j.kernel.impl.coreapi;

import org.neo4j.graphdb.Lock;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.internal.kernel.api.security.SecurityContext;
import org.neo4j.kernel.api.KernelTransaction;
import org.neo4j.kernel.api.exceptions.Status;
import org.neo4j.kernel.api.exceptions.Status.Transaction;

import java.util.Map;
import java.util.Optional;

public class PlaceboTransaction implements InternalTransaction {
    private static final PropertyContainerLocker locker = new PropertyContainerLocker();
    private final KernelTransaction currentTransaction;
    private boolean success;

    public PlaceboTransaction(KernelTransaction currentTransaction) {
        this.currentTransaction = currentTransaction;
    }

    public void terminate() {
        this.currentTransaction.markForTermination(Transaction.Terminated);
    }

    public void failure() {
        this.currentTransaction.failure();
    }

    public void success() {
        this.success = true;
    }

    public void close() {
        if (!this.success) {
            this.currentTransaction.failure();
        }

    }

    public Lock acquireWriteLock(PropertyContainer entity) {
        return locker.exclusiveLock(this.currentTransaction, entity);
    }

    public Lock acquireReadLock(PropertyContainer entity) {
        return locker.sharedLock(this.currentTransaction, entity);
    }

    @Override
    public KernelTransaction getKernelTransaction() {
        return this.currentTransaction;
    }

    public org.neo4j.internal.kernel.api.Transaction.Type transactionType() {
        return this.currentTransaction.transactionType();
    }

    public SecurityContext securityContext() {
        return this.currentTransaction.securityContext();
    }

    public KernelTransaction.Revertable overrideWith(SecurityContext context) {
        return this.currentTransaction.overrideWith(context);
    }

    public Optional<Status> terminationReason() {
        return this.currentTransaction.getReasonIfTerminated();
    }

    public void setMetaData(Map<String, Object> txMeta) {
        this.currentTransaction.setMetaData(txMeta);
    }
}
