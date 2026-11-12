package by.it.group551003.paskal.lesson14;
import java.util.Scanner;

public class StatesHanoiTowerC {

    static int[] parent;
    static int[] sizeArr;

    static int find(int x) {
        int root = x;
        while (parent[root] != root) root = parent[root];
        while (parent[x] != root) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }
        return root;
    }

    static void union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) return;
        if (sizeArr[ra] < sizeArr[rb]) {
            parent[ra] = rb;
            sizeArr[rb] += sizeArr[ra];
        } else {
            parent[rb] = ra;
            sizeArr[ra] += sizeArr[rb];
        }
    }

    static int n;
    static int[] peg = new int[64];
    static int[] firstStep = new int[64];
    static long[] countByHeight;
    static int count;

    static void move(int disk, int to) {
        peg[disk] = to;
        int hA = 0, hB = 0, hC = 0;
        for (int d = 1; d <= n; d++) {
            if (peg[d] == 0) hA++;
            else if (peg[d] == 1) hB++;
            else hC++;
        }
        int h = hA;
        if (hB > h) h = hB;
        if (hC > h) h = hC;
        if (firstStep[h] == -1) {
            firstStep[h] = count;
        } else {
            union(firstStep[h], count);
        }
        countByHeight[h]++;
        count++;
    }

    static void hanoi(int disk, int from, int to, int via) {
        if (disk == 0) return;
        hanoi(disk - 1, from, via, to);
        move(disk, to);
        hanoi(disk - 1, via, to, from);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        sc.close();

        long totalSteps = 1L;
        for (int i = 0; i < n; i++) {
            totalSteps = totalSteps * 2L;
        }
        totalSteps = totalSteps - 1L;

        int cap = (int) totalSteps;
        if (cap < 1) cap = 1;

        parent = new int[cap];
        sizeArr = new int[cap];
        for (int i = 0; i < cap; i++) {
            parent[i] = i;
            sizeArr[i] = 1;
        }
        count = 0;
        for (int i = 0; i < firstStep.length; i++) {
            firstStep[i] = -1;
        }
        countByHeight = new long[n + 1];

        for (int d = 0; d <= n; d++) {
            peg[d] = 0;
        }

        hanoi(n, 0, 1, 2);

        long[] result = new long[n + 1];
        int rc = 0;
        for (int h = 1; h <= n; h++) {
            if (firstStep[h] != -1) {
                result[rc++] = countByHeight[h];
            }
        }

        for (int i = 1; i < rc; i++) {
            long key = result[i];
            int j = i - 1;
            while (j >= 0 && result[j] > key) {
                result[j + 1] = result[j];
                j--;
            }
            result[j + 1] = key;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rc; i++) {
            if (i > 0) sb.append(' ');
            sb.append(result[i]);
        }
        System.out.println(sb.toString());
    }
}