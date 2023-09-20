package edu.coursera.concurrent.boruvka.sequential;

import edu.coursera.concurrent.boruvka.BoruvkaFactory;

/**
 * A factory for generating components and edges when performing a sequential
 * traversal.
 */
public final class SeqBoruvkaFactory
        implements BoruvkaFactory<ParComponent, SeqEdge> {
    /**
     * {@inheritDoc}
     */
    @Override
    public ParComponent newComponent(final int nodeId) {
        return new ParComponent(nodeId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SeqEdge newEdge(final ParComponent from, final ParComponent to,
                           final double weight) {
        return new SeqEdge(from, to, weight);
    }
}
