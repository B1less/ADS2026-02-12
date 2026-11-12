package by.it.group551003.paskal.lesson13;
import java.util.Scanner;

public class GraphA {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine().trim();
        sc.close();

        String[] pairs = line.split(",\\s*");

        int maxV = -1;
        char[][] raw = new char[pairs.length][2];
        for (int i = 0; i < pairs.length; i++) {
            String p = pairs[i].trim();
            String[] parts = p.split("\\s*->\\s*");
            char u = parts[0].trim().charAt(0);
            char v = parts[1].trim().charAt(0);
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

        boolean[] used = new boolean[n];
        int[] result = new int[n];
        int cnt = 0;

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
            result[cnt++] = pick;
            for (int v = 0; v < n; v++) {
                if (adj[pick][v]) {
                    indeg[v]--;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cnt; i++) {
            if (i > 0) sb.append(' ');
            sb.append((char) result[i]);
        }
        System.out.println(sb.toString());
    }
}