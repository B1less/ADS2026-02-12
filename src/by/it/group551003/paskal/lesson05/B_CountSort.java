package by.it.group551003.paskal.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Первая строка содержит число 1<=n<=10000, вторая - n натуральных чисел, не превышающих 10.
Выведите упорядоченную по неубыванию последовательность этих чисел.

При сортировке реализуйте метод со сложностью O(n)
*/

public class B_CountSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_CountSort.class.getResourceAsStream("dataB.txt");
        B_CountSort instance = new B_CountSort();
        int[] result = instance.countSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] countSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // размер массива
        int n = scanner.nextInt();
        int[] points = new int[n];

        // читаем точки
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        // так как числа не превышают 10, создаем счетчик на 11 элементов (индексы 0-10)
        // но числа натуральные (от 1), поэтому индекс 0 не используется
        int[] count = new int[11]; // индексы от 0 до 10

        // подсчитываем количество вхождений каждого числа
        for (int i = 0; i < n; i++) {
            count[points[i]]++;
        }

        // восстанавливаем отсортированный массив
        int index = 0;
        for (int i = 1; i <= 10; i++) { // начинаем с 1, так как числа натуральные
            for (int j = 0; j < count[i]; j++) {
                points[index++] = i;
            }
        }

        return points;
    }
}