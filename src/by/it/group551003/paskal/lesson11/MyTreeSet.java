package by.it.group551003.paskal.lesson11;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class MyTreeSet<E> implements Collection<E> {

    private E[] elements;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        elements = (E[]) new Object[10];
    }

    private void grow() {
        elements = Arrays.copyOf(elements, elements.length * 2);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(Object o) {
        if (size == 0) return false;
        int low = 0;
        int high = size - 1;
        Comparable<? super E> key = (Comparable<? super E>) o;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = key.compareTo(elements[mid]);
            if (cmp < 0) high = mid - 1;
            else if (cmp > 0) low = mid + 1;
            else return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();
        int low = 0;
        int high = size - 1;
        Comparable<? super E> key = (Comparable<? super E>) e;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = key.compareTo(elements[mid]);
            if (cmp < 0) high = mid - 1;
            else if (cmp > 0) low = mid + 1;
            else return false; // Уже существует
        }

        if (size == elements.length) {
            grow();
        }

        // Сдвигаем элементы для вставки нового в позицию 'low'
        System.arraycopy(elements, low, elements, low + 1, size - low);
        elements[low] = e;
        size++;
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object o) {
        int low = 0;
        int high = size - 1;
        Comparable<? super E> key = (Comparable<? super E>) o;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = key.compareTo(elements[mid]);
            if (cmp < 0) high = mid - 1;
            else if (cmp > 0) low = mid + 1;
            else {
                // Нашли индекс 'mid', удаляем элемент сдвигом влево
                int numMoved = size - mid - 1;
                if (numMoved > 0) {
                    System.arraycopy(elements, mid + 1, elements, mid, numMoved);
                }
                elements[--size] = null;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object e : c) {
            if (remove(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                remove(elements[i]);
                modified = true;
            }
        }
        return modified;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
        return sb.toString();
    }

    // Заглушки интерфейса Collection
    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}
