package edu.coursera.concurrent.boruvka.sequential;

import edu.coursera.concurrent.boruvka.Component;
import edu.coursera.concurrent.boruvka.Edge;

import java.util.ArrayList;
import java.util.List;

/**
 * Component implementation used for the sequential Boruvka implementation.
 */
public final class ParComponent extends Component<ParComponent> {

    /**
     *  A unique identifier for this component in the graph that contains
     *  it.
     */
    public final int nodeId;
    /**
     * List of edges attached to this component, sorted by weight from least
     * to greatest.
     */
    public List<Edge<ParComponent>> edges = new ArrayList<>();
    /**
     * The weight this component accounts for. A component gains weight when
     * it is merged with another component across an edge with a certain
     * weight.
     */
    public double totalWeight = 0;
    /**
     * Number of edges that have been collapsed to create this component.
     */
    public long totalEdges = 0;
    /**
     * Whether this component has already been collapsed into another
     * component.
     */
    public boolean isDead = false;

    /**
     * Constructor.
     *
     * @param setNodeId ID for this node.
     */
    protected ParComponent(final int setNodeId) {
        super();
        this.nodeId = setNodeId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int nodeId() {
        return nodeId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double totalWeight() {
        return totalWeight;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long totalEdges() {
        return totalEdges;
    }

    /**
     * {@inheritDoc}
     *
     * Edge is inserted in weight order, from least to greatest.
     */
    public void addEdge(final Edge<ParComponent> e) {
        oldAddEdge(e);
    }

    private void oldAddEdge(Edge<ParComponent> e) {
        int i = 0;
        while (i < edges.size()) {
            if (e.weight() < edges.get(i).weight()) {
                break;
            }
            i++;
        }
        edges.add(i, e);
    }

    private void newAddEdge(Edge<ParComponent> e) {
        if (edges.isEmpty()) {
            edges.add(e);
            return;
        }
        if (edges.get(0).weight() > e.weight()) {
            edges.add(0, e);
        } else {
            edges.add(e);
        }
    }

    /**
     * Get the edge with minimum weight from the sorted edge list.
     *
     * @return Edge with the smallest weight attached to this component.
     */
    public Edge<ParComponent> getMinEdge() {
        if (edges.size() == 0) {
            return null;
        }
        return edges.get(0);
    }

    /**
     * Merge two components together, connected by an edge with weight
     * edgeWeight.
     *
     * @param other The other component to merge into this component.
     * @param edgeWeight Weight of the edge connecting these components.
     */
    public void merge(final ParComponent other, final double edgeWeight) {
        oldMerge(other, edgeWeight);
    }
    private void newMerge(ParComponent other, double edgeWeight) {
        totalWeight += other.totalWeight + edgeWeight;
        totalEdges += other.totalEdges + 1;
        List<Edge<ParComponent>> toReplaceOne = new ArrayList<>();
        List<Edge<ParComponent>> toReplaceTwo = new ArrayList<>();
        // Get rid of inter-component edges
        for (final Edge<ParComponent> e : edges) {
            if ((e.fromComponent() != this && e.fromComponent() != other) || (e.toComponent() != this && e.toComponent() != other)) {
                toReplaceOne.add(e.replaceComponent(other, this));
            }
        }
        for (final Edge<ParComponent> e : other.edges) {
            if ((e.fromComponent() != this && e.fromComponent() != other) || (e.toComponent() != this && e.toComponent() != other)) {
                toReplaceTwo.add(e.replaceComponent(other, this));
            }
        }
        this.edges = new ArrayList<>();
        for (Edge<ParComponent> componentEdge : toReplaceOne) {
            addEdge(componentEdge);
        }
        for (Edge<ParComponent> parComponentEdge : toReplaceTwo) {
            addEdge(parComponentEdge);
        }
        other.edges.clear();
    }
    private void oldMerge(ParComponent other, double edgeWeight) {
        totalWeight += other.totalWeight + edgeWeight;
        totalEdges += other.totalEdges + 1;
        final List<Edge<ParComponent>> newEdges = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (i + j < edges.size() + other.edges.size()) {
            // get rid of inter-component edges
            while (i < edges.size()) {
                final Edge e = edges.get(i);
                if ((e.fromComponent() != this && e.fromComponent() != other)
                        || (e.toComponent() != this && e.toComponent() != other)
                        ) {
                    break;
                }
                i++;
            }
            while (j < other.edges.size()) {
                final Edge e = other.edges.get(j);
                if ((e.fromComponent() != this && e.fromComponent() != other)
                        || (e.toComponent() != this && e.toComponent() != other)
                        ) {
                    break;
                }
                j++;
            }
            if (j < other.edges.size() && (i >= edges.size()
                        || edges.get(i).weight() > other.edges.get(j).weight())
                    ) {
                newEdges.add(other.edges.get(j++).replaceComponent(other,
                            this));
            } else if (i < edges.size()) {
                newEdges.add(edges.get(i++).replaceComponent(other, this));
            }
        }
        other.edges.clear();
        edges.clear();
        edges = newEdges;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Component)) {
            return false;
        }

        final Component component = (Component) o;

        if (nodeId != component.nodeId()) {
            return false;
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return nodeId;
    }
}
