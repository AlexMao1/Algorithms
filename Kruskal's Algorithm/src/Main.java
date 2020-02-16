import java.io.*;
import java.util.*;

class Main {
    static class Graph{
        class Edge implements Comparable<Edge>{
            int src, dst, weight;
            public int compareTo(Edge compareEdge){
                return this.weight - compareEdge.weight;
            }
        }

        class Forest{
            int parent, rank;
            Forest(int parent, int rank){
                this.parent = parent;
                this.rank = rank;
            }
        }

        int V, E;
        Edge edge[];

        Graph(int v, int e){
            V = v;
            E = e;
            edge = new Edge[E];
            for(int i = 0; i < E; i++){
                edge[i] = new Edge();
            }
        }

        int find(Forest forest[], int i){
            if(forest[i].parent != i){
                forest[i].parent = find(forest, forest[i].parent);
            }
            return forest[i].parent;
        }

        void union(Forest forest[], int x, int y){
            int xRoot = find(forest, x);
            int yRoot = find(forest, y);
            if(forest[xRoot].rank < forest[yRoot].rank){
                forest[xRoot].parent = yRoot;
            }
            else if(forest[yRoot].rank < forest[xRoot].rank){
                forest[yRoot].parent = xRoot;
            }
            else{
                forest[yRoot].parent = xRoot;
                forest[xRoot].rank += 1;
            }
        }

        void Kruskal(){
            Edge result[] = new Edge[V];
            int e = 0;
            for(int i = 0; i < V; i++){
                result[i] = new Edge();
            }
            Arrays.sort(edge);
            Forest forest[] = new Forest[V];
            for(int i = 0; i < V; i++){
                forest[i] = new Forest(i, 0);
            }
            int idx = 0;
            while(e < V - 3){
                Edge nextEdge = new Edge();
                nextEdge = edge[idx++];
                int x = find(forest, nextEdge.src);
                int y = find(forest, nextEdge.dst);
                if(x != y){
                    result[e++] = nextEdge;
                    union(forest, x, y);
                }
            }
            ArrayList<Integer> roots = new ArrayList<>();
            for(int i = 0; i < V; i++){
                if(!roots.contains(find(forest, i))){
                    roots.add(find(forest, i));
                }
            }
            int[] componentSize = new int[3];
            for(int i = 0; i < V; i++){
                if(find(forest, i) == roots.get(0)){
                    componentSize[0] += 1;
                }
                else if(find(forest, i) == roots.get(1)){
                    componentSize[1] += 1;
                }
                else{
                    componentSize[2] += 1;
                }
            }
            Arrays.sort(componentSize);
            System.out.print(componentSize[0] + "\n" + componentSize[1] + "\n" + componentSize[2] + "\n");
        }
    }

    public static void main(String[] args){
        Scanner x = new Scanner(System.in);
        int V = x.nextInt();
        int E = x.nextInt();
        Graph G = new Graph(V, E);
        int count = 0;
        int idx = 0;
        while(count < 3 * E){
            G.edge[idx].src = x.nextInt();
            G.edge[idx].dst = x.nextInt();
            G.edge[idx].weight = x.nextInt();
            idx += 1;
            count += 3;
        }
        G.Kruskal();
    }
}
