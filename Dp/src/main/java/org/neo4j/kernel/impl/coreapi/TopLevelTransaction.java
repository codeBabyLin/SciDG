//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.neo4j.kernel.impl.coreapi;

import org.neo4j.graphdb.*;
import org.neo4j.internal.kernel.api.exceptions.KernelException;
import org.neo4j.internal.kernel.api.security.SecurityContext;
import org.neo4j.kernel.api.KernelTransaction;
import org.neo4j.kernel.api.exceptions.ConstraintViolationTransactionFailureException;
import org.neo4j.kernel.api.exceptions.Status;
import org.neo4j.kernel.api.exceptions.Status.Classification;
import org.neo4j.kernel.api.exceptions.Status.Transaction;

import java.util.Map;
import java.util.Optional;

public class TopLevelTransaction implements InternalTransaction {
    private static final PropertyContainerLocker locker = new PropertyContainerLocker();
    private boolean successCalled;
    private final KernelTransaction transaction;

    public TopLevelTransaction(KernelTransaction transaction) {
        this.transaction = transaction;
    }

    public void failure() {
        this.transaction.failure();
    }

    public void success() {
        this.successCalled = true;
        this.transaction.success();
    }

    public final void terminate() {
        this.transaction.markForTermination(Transaction.Terminated);
    }

    public void close() {
        try {
            if (this.transaction.isOpen()) {
                this.transaction.close();
            }

        } catch (TransientFailureException var3) {
            throw var3;
        } catch (ConstraintViolationTransactionFailureException var4) {
            throw new ConstraintViolationException(var4.getMessage(), var4);
        } catch (TransactionTerminatedException | KernelException var5) {
            Status.Code statusCode = ((Status.HasStatus)var5).status().code();
            if (statusCode.classification() == Classification.TransientError) {
                throw new TransientTransactionFailureException(this.closeFailureMessage() + ": " + statusCode.description(), var5);
            } else {
                throw new TransactionFailureException(this.closeFailureMessage(), var5);
            }
        } catch (Exception var6) {
            throw new TransactionFailureException(this.closeFailureMessage(), var6);
        }
    }

    private String closeFailureMessage() {
        return this.successCalled ? "Transaction was marked as successful, but unable to commit transaction so rolled back." : "Unable to rollback transaction";
    }

    public Lock acquireWriteLock(PropertyContainer entity) {
        return locker.exclusiveLock(this.transaction, entity);
    }

    public Lock acquireReadLock(PropertyContainer entity) {
        return locker.sharedLock(this.transaction, entity);
    }

    @Override
    public KernelTransaction getKernelTransaction() {
        return this.transaction;
    }

    public org.neo4j.internal.kernel.api.Transaction.Type transactionType() {
        return this.transaction.transactionType();
    }

    public SecurityContext securityContext() {
        return this.transaction.securityContext();
    }

    public KernelTransaction.Revertable overrideWith(SecurityContext context) {
        return this.transaction.overrideWith(context);
    }

    public Optional<Status> terminationReason() {
        return this.transaction.getReasonIfTerminated();
    }

    public void setMetaData(Map<String, Object> txMeta) {
        this.transaction.setMetaData(txMeta);
    }
}
