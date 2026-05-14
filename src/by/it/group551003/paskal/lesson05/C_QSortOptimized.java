package by.it.group551003.paskal.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_QSortOptimized {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];

        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }

        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }


        quickSort(segments, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int point = points[i];

            int count = 0;


            int left = 0, right = n - 1;
            int pos = n;

            while (left <= right) {
                int mid = (left + right) / 2;
                if (segments[mid].start > point) {
                    pos = mid;
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }


            for (int j = 0; j < pos; j++) {
                if (segments[j].stop >= point) {
                    count++;
                }
            }

            result[i] = count;
        }

        return result;
    }


    void quickSort(Segment[] a, int l, int r) {
        while (l < r) {
            int[] m = partition(a, l, r);


            if (m[0] - l < r - m[1]) {
                quickSort(a, l, m[0] - 1);
                l = m[1] + 1;
            } else {
                quickSort(a, m[1] + 1, r);
                r = m[0] - 1;
            }
        }
    }


    int[] partition(Segment[] a, int l, int r) {
        Segment pivot = a[l];

        int lt = l;
        int gt = r;
        int i = l;

        while (i <= gt) {
            int cmp = a[i].compareTo(pivot);
            if (cmp < 0) {
                swap(a, lt++, i++);
            } else if (cmp > 0) {
                swap(a, i, gt--);
            } else {
                i++;
            }
        }
        return new int[]{lt, gt};
    }

    void swap(Segment[] a, int i, int j) {
        Segment temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }


    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            // сортируем по началу, при равенстве — по концу
            if (this.start != o.start)
                return this.start - o.start;
            return this.stop - o.stop;
        }
    }
}