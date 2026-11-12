package by.it.group551003.paskal.lesson13;

import java.util.Scanner;

public class GraphB {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine().trim();
        sc.close();

        String[] pairs = line.split(",\\s*");

        int maxV = -1;
        int[][] raw = new int[pairs.length][2];
        for (int i = 0; i < pairs.length; i++) {
            String p = pairs[i].trim();
            String[] parts = p.split("\\s*->\\s*");
            int u = Integer.parseInt(parts[0].trim());
            int v = Integer.parseInt(parts[1].trim());
            raw[i][0] = u;
            raw[i][1] = v;
            if (u > maxV) maxV = u;
            if (v > maxV) maxV = v;
        }

        int n = maxV + 1;

        boolean[][] adj = new boolean[n][n];
        int[] indeg = new int[n];
        boolean[] present = new boolean[n];

        for (int i = 0; i < raw.length; i++) {
            int u = raw[i][0];
            int v = raw[i][1];
            present[u] = true;
            present[v] = true;
            if (!adj[u][v]) {
                adj[u][v] = true;
                indeg[v]++;
            }
        }

        int removed = 0;
        boolean[] used = new boolean[n];

        for (int step = 0; step < n; step++) {
            int pick = -1;
            for (int v = 0; v < n; v++) {
                if (!used[v] && present[v] && indeg[v] == 0) {
                    pick = v;
                    break;
                }
            }
            if (pick == -1) {
                break;
            }
            used[pick] = true;
            removed++;
            for (int v = 0; v < n; v++) {
                if (adj[pick][v]) {
                    indeg[v]--;
                }
            }
        }

        int total = 0;
        for (int v = 0; v < n; v++) {
            if (present[v]) total++;
        }

        System.out.println(removed == total ? "no" : "yes");
    }
}