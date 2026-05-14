package by.it.group551003.paskal.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
2 5 7 | 3 6
*/


public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        //long startTime = System.currentTimeMillis();
        int result = instance.calc(stream);
        //long finishTime = System.currentTimeMillis();
        System.out.print(result);
    }

    int calc(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!
        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!
        // Создаем временный массив для слияния
        int[] temp = new int[n];
        int result = mergeSortAndCount(a, temp, 0, n - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Рекурсивный метод сортировки слиянием и подсчета инверсий
    private int mergeSortAndCount(int[] array, int[] temp, int left, int right) {
        int inversions = 0;

        if (left < right) {
            // Находим середину
            int mid = left + (right - left) / 2;

            // Подсчитываем инверсии в левой половине
            inversions += mergeSortAndCount(array, temp, left, mid);

            // Подсчитываем инверсии в правой половине
            inversions += mergeSortAndCount(array, temp, mid + 1, right);

            // Подсчитываем инверсии при слиянии
            inversions += mergeAndCount(array, temp, left, mid, right);
        }

        return inversions;
    }

    // Метод слияния двух отсортированных половин и подсчета инверсий
    private int mergeAndCount(int[] array, int[] temp, int left, int mid, int right) {
        int i = left;      // индекс для левой половины
        int j = mid + 1;   // индекс для правой половины
        int k = left;      // индекс для временного массива
        int inversions = 0;

        // Слияние двух половин с подсчетом инверсий
        while (i <= mid && j <= right) {
            if (array[i] <= array[j]) {
                temp[k++] = array[i++];
            } else {
                // Если array[i] > array[j], то все оставшиеся элементы в левой половине
                // тоже больше array[j], так как обе половины отсортированы
                // Значит, array[i], array[i+1], ..., array[mid] образуют инверсии с array[j]
                temp[k++] = array[j++];
                inversions += (mid - i + 1);
            }
        }

        // Копируем оставшиеся элементы левой половины (если есть)
        while (i <= mid) {
            temp[k++] = array[i++];
        }

        // Копируем оставшиеся элементы правой половины (если есть)
        while (j <= right) {
            temp[k++] = array[j++];
        }

        // Копируем обратно из временного массива в оригинальный
        for (i = left; i <= right; i++) {
            array[i] = temp[i];
        }

        return inversions;
    }
}