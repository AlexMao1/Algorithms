import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.*;
import java.util.*;

class Main
{
    int n; // number of candidates
    int k; // number of recruiters

    // provided data structures (already filled in)
    ArrayList<ArrayList<Integer>> neighbors;
    int[] recruiterCapacities;
    int[] preliminaryAssignment;

    // provided data structures (you need to fill these in)
    boolean existsValidAssignment;
    int[] validAssignment;
    int[] bottleneckRecruiters;

    // reading the input
    void input()
    {
        BufferedReader reader = null;

        try
        {
            reader = new BufferedReader(new InputStreamReader(System.in));

            String text = reader.readLine();
            String[] parts = text.split(" ");

            n = Integer.parseInt(parts[0]);
            k = Integer.parseInt(parts[1]);
            neighbors = new ArrayList<ArrayList<Integer>>(n+k);
            recruiterCapacities = new int[n+k];
            preliminaryAssignment = new int[n];

            for (int j = 0; j < n+k; j++) {
                neighbors.add(new ArrayList<Integer>());
            }
            for (int i = 0; i < n; i++) {
                text = reader.readLine();
                parts = text.split(" ");
                int numRecruiters = Integer.parseInt(parts[0]);
                for (int j = 0; j < numRecruiters; j++) {
                    int recruiter = Integer.parseInt(parts[j+1]);
                    neighbors.get(i).add(recruiter);
                    neighbors.get(recruiter).add(i);
                }
            }

            text = reader.readLine();
            parts = text.split(" ");
            for (int j = 0; j < k; j++) {
                recruiterCapacities[n+j] = Integer.parseInt(parts[j]);
            }

            text = reader.readLine();
            parts = text.split(" ");
            for (int i = 0; i < n-1; i++) {
                preliminaryAssignment[i] = Integer.parseInt(parts[i]);
            }

            reader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // writing the output
    void output()
    {
        try
        {
            PrintWriter writer = new PrintWriter(System.out);

            if (existsValidAssignment) {
                writer.println("Yes");
                for (int i = 0; i < n-1; i++) {
                    writer.print(validAssignment[i] + " ");
                }
                writer.println(validAssignment[n-1]);
            } else {
                writer.println("No");
                for (int j = 0; j < n+k-1; j++) {
                    writer.print(bottleneckRecruiters[j] + " ");
                }
                writer.println(bottleneckRecruiters[n+k-1]);
            }

            writer.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static final class PairKey<Integer> {
        public final Integer a;
        public final Integer b;

        private PairKey(Integer a, Integer b) { this.a = a; this.b = b; }

        public static <Integer> PairKey<Integer> make(Integer a, Integer b) { return new PairKey<Integer>(a, b); }

        public int hashCode() {
            return (a != null ? a.hashCode() : 0) + 31 * (b != null ? b.hashCode() : 0);
        }

        public boolean equals(Object o) {
            if (o == null || o.getClass() != this.getClass()) { return false; }
            PairKey that = (PairKey) o;
            return (Objects.equals(a, that.a))
                    && (Objects.equals(b, that.b));
        }
    }

    public Main()
    {
        input();

        // Fill these in as instructed in the problem statement.
        existsValidAssignment = false;
        validAssignment = new int[n];
        bottleneckRecruiters = new int[n+k];

        //YOUR CODE STARTS HERE
        // Create a list to store each interviewer's current candidates
        ArrayList<ArrayList<Integer>> listOfCandidates = new ArrayList<ArrayList<Integer>>(n + k);
        for (int i = 0; i < n + k; i++) {
            listOfCandidates.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < n; i++) {
            listOfCandidates.get(preliminaryAssignment[i]).add(i);
        }

        // Create a hash map to store the edges and weights of the residual graph
        HashMap<PairKey<Integer>, Integer> residual = new HashMap<PairKey<Integer>, Integer>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < neighbors.get(i).size(); j++) {
                PairKey<Integer> forwardEdge = new PairKey<Integer>(i, neighbors.get(i).get(j));
                PairKey<Integer> backEdge = new PairKey<Integer>(neighbors.get(i).get(j), i);
                if (preliminaryAssignment[i] != neighbors.get(i).get(j)) {
                    residual.put(forwardEdge, 1);
                    residual.put(backEdge, 0);
                } else {
                    residual.put(forwardEdge, 0);
                    residual.put(backEdge, 1);
                }
            }
        }

        // Create a sink that is connected to all interviewers and initiate weights for edges
        for (int i = n; i < n + k; i++) {
            int currWeight = listOfCandidates.get(i).size();
            int residualWeight = recruiterCapacities[i] - currWeight;
            neighbors.get(i).add(n + k);
            PairKey<Integer> forwardEdge = new PairKey<Integer>(i, n + k);
            PairKey<Integer> backEdge = new PairKey<Integer>(n + k, i);
            residual.put(forwardEdge, residualWeight);
            residual.put(backEdge, currWeight);
        }

        // Start BFS from candidate n - 1 to find the shortest augmenting path
        Queue<Integer> q = new LinkedList<>();
        q.add(n - 1);
        boolean[] visited = new boolean[n + k + 1];
        int[] dist = new int[n + k + 1];
        int[] pred = new int[n + k + 1];
        for (int i = 0; i < n + k + 1; i++) {
            visited[i] = false;
            dist[i] = Integer.MAX_VALUE;
            pred[i] = -1;
        }
        visited[n - 1] = true;
        dist[n - 1] = 0;

        boolean finished = false;
        while (!q.isEmpty() && !finished) {
            int curr = q.remove();

            for (int i = 0; i < neighbors.get(curr).size(); i++) {
                int neighbor = neighbors.get(curr).get(i);
                PairKey<Integer> edge = new PairKey<Integer>(curr, neighbor);
                if (!visited[neighbor]) {
                    if (residual.get(edge) > 0) {
                        visited[neighbor] = true;
                        dist[neighbor] = dist[curr] + 1;
                        pred[neighbor] = curr;
                        q.add(neighbor);
                        if (neighbor == n + k) {
                            finished = true;
                            existsValidAssignment = true;
                            break;
                        }
                    } else {
                        if (neighbor == n + k){
                            bottleneckRecruiters[curr] = 1;
                        }
                    }
                }
            }
        }

        if (existsValidAssignment) {
            // Backtrack to get the shortest path from n - 1 to the sink in the residual graph
            ArrayList<Integer> path = new ArrayList<Integer>();
            int dst = n + k;
            while (pred[dst] != -1) {
                path.add(pred[dst]);
                dst = pred[dst];
            }
            Collections.reverse(path);
            for (int i = 0; i < path.size(); i++) {
                System.out.print(path.get(i).toString() + " ");
            }
            System.out.println("\n");

            // Modify the edges along the path to fill in the (n - 1)th entry of preliminaryAssignment
            for (int i = 0; i < path.size() - 1; i++) {
                int curr = path.get(i);
                int next = path.get(i + 1);
                PairKey<Integer> edge = new PairKey<Integer>(curr, next);
                PairKey<Integer> reverseEdge = new PairKey<Integer>(next, curr);
                residual.put(edge, 0);
                residual.put(reverseEdge, 1);
                if (curr < next) {
                    preliminaryAssignment[curr] = next;
                    listOfCandidates.get(next).add(curr);
                } else {
                    listOfCandidates.get(curr).remove(next);
                }
            }
            validAssignment = preliminaryAssignment;
        }
        //YOUR CODE ENDS HERE

        output();
    }

    // Strings in Args are the name of the input file followed by
    // the name of the output file.
    public static void main(String [] Args)
    {
        new Main();
    }
}

