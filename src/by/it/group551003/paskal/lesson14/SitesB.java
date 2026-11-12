package by.it.group551003.paskal.lesson14;
import java.util.Scanner;

public class SitesB {

    static String[] names;
    static int[] parent;
    static int[] rank;
    static int[] size;
    static int count;

    static int find(int x) {
        int root = x;
        while (parent[root] != root) {
            root = parent[root];
        }
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
        if (rank[ra] < rank[rb]) {
            parent[ra] = rb;
            size[rb] += size[ra];
        } else if (rank[ra] > rank[rb]) {
            parent[rb] = ra;
            size[ra] += size[rb];
        } else {
            parent[rb] = ra;
            rank[ra]++;
            size[ra] += size[rb];
        }
    }

    static int getId(String name) {
        for (int i = 0; i < count; i++) {
            if (names[i].equals(name)) {
                return i;
            }
        }
        return -1;
    }

    static int addName(String name) {
        int id = getId(name);
        if (id != -1) return id;
        names[count] = name;
        parent[count] = count;
        rank[count] = 0;
        size[count] = 1;
        return count++;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int cap = 1024;
        names = new String[cap];
        parent = new int[cap];
        rank = new int[cap];
        size = new int[cap];
        count = 0;

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            if (line.equals("end")) break;

            int plus = line.indexOf('+');
            String a = line.substring(0, plus).trim();
            String b = line.substring(plus + 1).trim();

            int ia = addName(a);
            int ib = addName(b);
            union(ia, ib);
        }
        sc.close();

        int[] counts = new int[count];
        int cnt = 0;
        for (int i = 0; i < count; i++) {
            if (find(i) == i) {
                counts[cnt++] = size[i];
            }
        }

        for (int i = 1; i < cnt; i++) {
            int key = counts[i];
            int j = i - 1;
            while (j >= 0 && counts[j] < key) {
                counts[j + 1] = counts[j];
                j--;
            }
            counts[j + 1] = key;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cnt; i++) {
            if (i > 0) sb.append(' ');
            sb.append(counts[i]);
        }
        System.out.println(sb.toString());
    }
}