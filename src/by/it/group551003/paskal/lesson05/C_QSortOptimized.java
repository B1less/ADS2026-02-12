package by.it.group551003.paskal.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче А.

        По сравнению с задачей A доработайте алгоритм так, чтобы
        1) он оптимально использовал время и память:
            - за стек отвечает элиминация хвостовой рекурсии
            - за сам массив отрезков - сортировка на месте
            - рекурсивные вызовы должны проводиться на основе 3-разбиения

        2) при поиске подходящих отрезков для точки реализуйте метод бинарного поиска
        для первого отрезка решения, а затем найдите оставшуюся часть решения
        (т.е. отрезков, подходящих для точки, может быть много)

    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/


public class C_QSortOptimized {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        //число отрезков отсортированного массива
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        //число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            //читаем начало и конец каждого отрезка
            int start = scanner.nextInt();
            int end = scanner.nextInt();
            //гарантируем, что start <= end
            if (start > end) {
                int temp = start;
                start = end;
                end = temp;
            }
            segments[i] = new Segment(start, end);
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор

        // Сортируем отрезки с помощью оптимизированной быстрой сортировки
        quickSort3Way(segments, 0, n - 1);

        // Для каждой точки находим количество отрезков, которым она принадлежит
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // Находим первый отрезок, у которого start <= point
            int left = findFirstSegment(segments, point);

            if (left == -1) {
                result[i] = 0;
                continue;
            }

            // Находим последний отрезок, у которого start <= point
            int right = findLastSegment(segments, point);

            // Считаем количество отрезков, у которых stop >= point
            int count = 0;
            for (int j = left; j <= right; j++) {
                if (point <= segments[j].stop) {
                    count++;
                }
            }

            result[i] = count;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // 3-way quicksort with tail recursion elimination
    private void quickSort3Way(Segment[] arr, int low, int high) {
        while (low < high) {
            // Partition array and get boundaries of equal elements
            int[] bounds = partition3Way(arr, low, high);
            int lt = bounds[0];
            int gt = bounds[1];

            // Recurse on the smaller part first, then handle the larger part iteratively
            if (lt - low < high - gt) {
                quickSort3Way(arr, low, lt - 1);
                low = gt + 1;
            } else {
                quickSort3Way(arr, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    // 3-way partitioning using Dutch national flag algorithm
    private int[] partition3Way(Segment[] arr, int low, int high) {
        Segment pivot = arr[low];
        int lt = low;
        int i = low + 1;
        int gt = high;

        while (i <= gt) {
            int cmp = arr[i].compareTo(pivot);
            if (cmp < 0) {
                swap(arr, lt, i);
                lt++;
                i++;
            } else if (cmp > 0) {
                swap(arr, i, gt);
                gt--;
            } else {
                i++;
            }
        }

        return new int[]{lt, gt};
    }

    // Binary search for the first segment where start <= point
    private int findFirstSegment(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return result;
    }

    // Binary search for the last segment where start <= point
    private int findLastSegment(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result;
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков
            // Сортируем по началу отрезка
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            // Если начала равны, сортируем по концу
            return Integer.compare(this.stop, o.stop);
        }
    }

}