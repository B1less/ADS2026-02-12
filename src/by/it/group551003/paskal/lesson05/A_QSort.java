package by.it.group551003.paskal.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

/*
Видеорегистраторы и площадь.
*/
public class A_QSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt(); // число отрезков
        int m = scanner.nextInt(); // число точек

        Segment[] segments = new Segment[n];
        int[] points = new int[m];
        int[] result = new int[m];

        // читаем отрезки
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            segments[i] = new Segment(Math.min(start, stop), Math.max(start, stop));
        }

        // читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // быстрая сортировка отрезков по началу
        quickSort(segments, 0, segments.length - 1);

        // для каждой точки считаем количество покрывающих отрезков
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // находим первый отрезок, который может содержать точку (start <= point)
            int left = 0;
            int right = segments.length - 1;
            int firstIndex = segments.length;

            while (left <= right) {
                int mid = (left + right) / 2;
                if (segments[mid].start <= point) {
                    firstIndex = mid;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            // считаем количество отрезков, которые содержат точку
            int count = 0;
            for (int j = 0; j <= firstIndex; j++) {
                if (segments[j].stop >= point) {
                    count++;
                }
            }

            result[i] = count;
        }

        return result;
    }

    // быстрая сортировка
    private void quickSort(Segment[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    private int partition(Segment[] arr, int low, int high) {
        Segment pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j].compareTo(pivot) <= 0) {
                i++;
                Segment temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        Segment temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    // отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            // сортировка по началу отрезка
            return Integer.compare(this.start, o.start);
        }
    }
}