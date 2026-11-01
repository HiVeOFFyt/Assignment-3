import connectivity.SCC;
import connectivity.TopoResult;
import connectivity.TopologicalSort;
import graph.Edge;
import graph.Graph;
import mst.Kruskal;
import mst.MSTResult;
import mst.Prim;
import shortestpath.Dijkstra;
import shortestpath.DijkstraResult;
import java.util.List;

// small demo that prints the results of every algorithm on tiny city graphs
public class Demo {
    public static void main(String[] args) {
        mstDemo();
        topoDemo();
        sccDemo();
        dijkstraDemo();
    }

    static void mstDemo() {
        System.out.println("=== MST (6 districts, 8 roads) ===");
        Graph g = new Graph(6, false);
        g.addEdge(0, 1, 7);
        g.addEdge(0, 2, 9);
        g.addEdge(1, 2, 3);
        g.addEdge(1, 3, 4);
        g.addEdge(2, 4, 2);
        g.addEdge(3, 4, 6);
        g.addEdge(3, 5, 5);
        g.addEdge(4, 5, 8);
        print("Kruskal", Kruskal.run(g));
        print("Prim   ", Prim.run(g));

        System.out.println("Disconnected graph (vertex 4 has no roads):");
        Graph d = new Graph(5, false);
        d.addEdge(0, 1, 1);
        d.addEdge(1, 2, 2);
        d.addEdge(2, 3, 3);
        print("Kruskal", Kruskal.run(d));
        print("Prim   ", Prim.run(d));
        System.out.println();
    }

    static void print(String name, MSTResult r) {
        System.out.print(name + ": weight = " + r.totalWeight + ", connected = " + r.connected
                + ", components = " + r.components + ", edges = ");
        for (Edge e : r.edges) {
            System.out.print("(" + e.from + "-" + e.to + ":" + e.weight + ") ");
        }
        System.out.println();
    }

    static void topoDemo() {
        System.out.println("=== Topological order ===");
        Graph dag = new Graph(6, true);
        dag.addEdge(5, 2, 1);
        dag.addEdge(5, 0, 1);
        dag.addEdge(4, 0, 1);
        dag.addEdge(4, 1, 1);
        dag.addEdge(2, 3, 1);
        dag.addEdge(3, 1, 1);
        TopoResult t = TopologicalSort.run(dag);
        System.out.println("DAG: has cycle = " + t.hasCycle + ", order = " + t.order);

        Graph cyc = new Graph(4, true);
        cyc.addEdge(0, 1, 1);
        cyc.addEdge(1, 2, 1);
        cyc.addEdge(2, 1, 1);
        cyc.addEdge(2, 3, 1);
        TopoResult c = TopologicalSort.run(cyc);
        System.out.println("Graph with cycle 1->2->1: has cycle = " + c.hasCycle
                + (c.hasCycle ? " (no topological order exists)" : ""));
        System.out.println();
    }

    static void sccDemo() {
        System.out.println("=== Strongly connected components ===");
        Graph g = new Graph(8, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 4, 1);
        g.addEdge(4, 5, 1);
        g.addEdge(5, 3, 1);
        g.addEdge(5, 6, 1);
        // vertex 7 has no roads
        SCC s = SCC.run(g);
        System.out.println("Number of components: " + s.components.size());
        int number = 1;
        for (List<Integer> comp : s.components) {
            System.out.println("  component " + number + ": " + comp);
            number++;
        }
        System.out.println();
    }

    static void dijkstraDemo() {
        System.out.println("=== Dijkstra from district 0 ===");
        Graph g = new Graph(6, false);
        g.addEdge(0, 1, 4);
        g.addEdge(0, 2, 1);
        g.addEdge(2, 1, 2);
        g.addEdge(1, 3, 1);
        g.addEdge(3, 4, 5);
        // vertex 5 is not connected to the others
        DijkstraResult r = Dijkstra.run(g, 0);
        for (int v = 0; v < g.n; v++) {
            if (r.dist[v] == DijkstraResult.INF) {
                System.out.println("  to " + v + ": unreachable");
            } else {
                System.out.println("  to " + v + ": distance = " + r.dist[v] + ", path = " + r.pathTo(v));
            }
        }
    }
}
