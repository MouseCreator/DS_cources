package edu.coursera.concurrent.boruvka.sequential;

import edu.coursera.concurrent.boruvka.Edge;

/**
 * An edge class used in the sequential Boruvka implementation.
 */
public final class SeqEdge extends Edge<ParComponent>
        implements Comparable<Edge> {

    /**
     * Source component.
     */
    protected ParComponent fromComponent;
    /**
     * Destination component.
     */
    protected ParComponent toComponent;
    /**
     * Weight of this edge.
     */
    public double weight;

    /**
     * Constructor.
     *
     * @param from From edge.
     * @param to To edges.
     * @param w Weight of this edge.
     */
    protected SeqEdge(final ParComponent from, final ParComponent to,
                      final double w) {
        fromComponent = from;
        toComponent = to;
        weight = w;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParComponent fromComponent() {
        return fromComponent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParComponent toComponent() {
        return toComponent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double weight() {
        return weight;
    }

    /**
     * {@inheritDoc}
     */
    public ParComponent getOther(final ParComponent from) {
        if (fromComponent == from) {
            assert (toComponent != from);
            return toComponent;
        }

        if (toComponent == from) {
            assert (fromComponent != from);
            return fromComponent;
        }
        assert (false);
        return null;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final Edge e) {
        if (e.weight() == weight) {
            return 0;
        } else if (weight < e.weight()) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * {@inheritDoc}
     */
    public SeqEdge replaceComponent(final ParComponent from,
            final ParComponent to) {
        if (fromComponent == from) {
            fromComponent = to;
        }
        if (toComponent == from) {
            toComponent = to;
        }
        return this;
    }
}
