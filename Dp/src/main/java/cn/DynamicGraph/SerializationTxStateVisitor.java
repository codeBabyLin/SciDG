package cn.DynamicGraph;

import org.eclipse.collections.api.IntIterable;
import org.eclipse.collections.api.set.primitive.LongSet;
import org.neo4j.internal.kernel.api.exceptions.schema.ConstraintValidationException;
import org.neo4j.internal.kernel.api.exceptions.schema.CreateConstraintFailureException;
import org.neo4j.internal.kernel.api.schema.constraints.ConstraintDescriptor;
import org.neo4j.storageengine.api.StorageProperty;
import org.neo4j.storageengine.api.schema.IndexDescriptor;
import org.neo4j.storageengine.api.txstate.TxStateVisitor;

import java.util.Iterator;

public class SerializationTxStateVisitor implements TxStateVisitor {
    @Override
    public void visitCreatedNode(long l) {

    }

    @Override
    public void visitDeletedNode(long l) {

    }

    @Override
    public void visitCreatedRelationship(long l, int i, long l1, long l2) throws ConstraintValidationException {

    }

    @Override
    public void visitDeletedRelationship(long l) {

    }

    @Override
    public void visitNodePropertyChanges(long l, Iterator<StorageProperty> iterator, Iterator<StorageProperty> iterator1, IntIterable intIterable) throws ConstraintValidationException {

    }

    @Override
    public void visitRelPropertyChanges(long l, Iterator<StorageProperty> iterator, Iterator<StorageProperty> iterator1, IntIterable intIterable) throws ConstraintValidationException {

    }

    @Override
    public void visitGraphPropertyChanges(Iterator<StorageProperty> iterator, Iterator<StorageProperty> iterator1, IntIterable intIterable) {

    }

    @Override
    public void visitNodeLabelChanges(long l, LongSet longSet, LongSet longSet1) throws ConstraintValidationException {

    }

    @Override
    public void visitAddedIndex(IndexDescriptor indexDescriptor) {

    }

    @Override
    public void visitRemovedIndex(IndexDescriptor indexDescriptor) {

    }

    @Override
    public void visitAddedConstraint(ConstraintDescriptor constraintDescriptor) throws CreateConstraintFailureException {

    }

    @Override
    public void visitRemovedConstraint(ConstraintDescriptor constraintDescriptor) {

    }

    @Override
    public void visitCreatedLabelToken(long l, String s) {

    }

    @Override
    public void visitCreatedPropertyKeyToken(long l, String s) {

    }

    @Override
    public void visitCreatedRelationshipTypeToken(long l, String s) {

    }

    @Override
    public void close() {

    }
}
