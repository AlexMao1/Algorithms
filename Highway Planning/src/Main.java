import java.util.*;
import java.io.*;

class Main {
    public static class GasStation{
        GasStation(int position, int price){
            pos = position;
            cost = price;
        }
        int pos;
        int cost;
    }

    public static void main(String args[]){
        Scanner x = new Scanner(System.in);
        int n = x.nextInt();
        int M = x.nextInt();
        int m = x.nextInt();
        int count = 0;
        int idx = 1;
        GasStation GasStations[] = new GasStation[n + 2];
        GasStations[0] = new GasStation(0, 0);
        GasStations[n + 1] = new GasStation(M , 0);
        while (count < 2 * n){
            int pos = x.nextInt();
            int cost = x.nextInt();
            GasStations[idx] = new GasStation(pos, cost);
            idx += 1;
            count += 2;
        }
        long Opt[] = new long[n + 2];
        int link[] = new int[n + 2];
        Opt[0] = 0;
        Opt[1] = GasStations[0].cost;
        link[0] = 0;
        link[1] = 0;
        for (int i = 2; i < n + 2; i++){
            ArrayList<Long> reachable = new ArrayList<>();
            int j = i - 1;
            while (j >= 0 && GasStations[i].pos - GasStations[j].pos <= m){
                reachable.add(Opt[j]);
                j -= 1;
            }
            long minCost = reachable.get(0);
            for (int k = 0; k < reachable.size(); k++){
                if (reachable.get(k) <= minCost){
                    minCost = reachable.get(k);
                    link[i] = i - 1 - k;
                }
            }
            Opt[i] = minCost + GasStations[i].cost;
        }
        System.out.print(Opt[n + 1] + "\n");
        ArrayList<Integer> stations = new ArrayList<Integer>();
        int backtrack = n + 1;
        while (backtrack != 0){
            backtrack = link[backtrack];
            stations.add(GasStations[backtrack].pos);
        }
        for (int i = stations.size() - 2; i >= 0; i--){
            if (i != 0){
                System.out.print(stations.get(i) + " ");
            }
            else{
                System.out.print(stations.get(i) + "\n");
            }
        }
    }
}
