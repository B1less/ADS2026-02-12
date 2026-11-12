package by.it.group551003.paskal.lesson13;
import java.util.Scanner;

public class GraphC {

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
        boolean[][] radj = new boolean[n][n];
        boolean[] present = new boolean[n];

        for (int i = 0; i < raw.length; i++) {
            int u = raw[i][0];
            int v = raw[i][1];
            present[u] = true;
            present[v] = true;
            adj[u][v] = true;
            radj[v][u] = true;
        }

        boolean[] visited = new boolean[n];
        int[] order = new int[n];
        int orderCnt = 0;

        for (int v = 0; v < n; v++) {
            if (present[v] && !visited[v]) {
                orderCnt = dfs1(v, adj, visited, order, orderCnt);
            }
        }

        int[] comp = new int[n];
        for (int i = 0; i < n; i++) comp[i] = -1;

        int compCnt = 0;
        int[][] comps = new int[n][n];
        int[] compSize = new int[n];

        for (int i = orderCnt - 1; i >= 0; i--) {
            int v = order[i];
            if (comp[v] == -1) {
                compSize[compCnt] = dfs2(v, radj, comp, compCnt, comps[compCnt], 0);
                compCnt++;
            }
        }

        int[][] dagAdj = new int[compCnt][compCnt];
        int[] dagIndeg = new int[compCnt];
        boolean[][] dagEdge = new boolean[compCnt][compCnt];

        for (int u = 0; u < n; u++) {
            if (!present[u]) continue;
            for (int v = 0; v < n; v++) {
                if (!present[v]) continue;
                if (adj[u][v] && comp[u] != comp[v] && !dagEdge[comp[u]][comp[v]]) {
                    dagEdge[comp[u]][comp[v]] = true;
                    dagAdj[comp[u]][dagIndeg[comp[v]]++] = comp[v];
                }
            }
        }

        int[] result = new int[compCnt];
        boolean[] usedComp = new boolean[compCnt];
        int resultCnt = 0;

        for (int step = 0; step < compCnt; step++) {
            int pick = -1;
            for (int c = 0; c < compCnt; c++) {
                if (!usedComp[c] && dagIndeg[c] == 0) {
                    pick = c;
                    break;
                }
            }
            if (pick == -1) {
                for (int c = 0; c < compCnt; c++) {
                    if (!usedComp[c]) { pick = c; break; }
                }
            }
            if (pick == -1) break;
            usedComp[pick] = true;
            result[resultCnt++] = pick;
            for (int k = 0; k < dagIndeg[pick]; k++) {
            }
            for (int c = 0; c < compCnt; c++) {
                if (dagEdge[pick][c]) {
                    dagIndeg[c]--;
                }
            }
        }

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < resultCnt; i++) {
            int c = result[i];
            int sz = compSize[c];
            int[] arr = new int[sz];
            for (int j = 0; j < sz; j++) arr[j] = comps[c][j];
            sortInts(arr, sz);
            for (int j = 0; j < sz; j++) {
                out.append((char) arr[j]);
            }
            if (i != resultCnt - 1) out.append('\n');
        }
        System.out.println(out.toString());
    }

    static int dfs1(int v, boolean[][] adj, boolean[] visited, int[] order, int cnt) {
        int n = adj.length;
        int[] stack = new int[n];
        int[] idx = new int[n];
        int top = 0;
        stack[top] = v;
        idx[top] = 0;
        visited[v] = true;
        while (top >= 0) {
            int u = stack[top];
            int i = idx[top];
            boolean advanced = false;
            while (i < n) {
                if (adj[u][i] && !visited[i]) {
                    idx[top] = i + 1;
                    visited[i] = true;
                    top++;
                    stack[top] = i;
                    idx[top] = 0;
                    advanced = true;
                    break;
                }
                i++;
            }
            if (!advanced) {
                idx[top] = i;
                order[cnt++] = u;
                top--;
            }
        }
        return cnt;
    }

    static int dfs2(int v, boolean[][] radj, int[] comp, int compId, int[] compNodes, int cnt) {
        int n = radj.length;
        int[] stack = new int[n];
        int top = 0;
        stack[top++] = v;
        comp[v] = compId;
        while (top > 0) {
            int u = stack[--top];
            compNodes[cnt++] = u;
            for (int w = 0; w < n; w++) {
                if (radj[u][w] && comp[w] == -1) {
                    comp[w] = compId;
                    stack[top++] = w;
                }
            }
        }
        return cnt;
    }

    static void sortInts(int[] a, int len) {
        for (int i = 1; i < len; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= 0 && a[j] > key) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = key;
        }
    }
}