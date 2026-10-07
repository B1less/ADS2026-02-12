package by.it.group551003.paskal.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Collection<E> {

    private E[] elements;
    private int head;
    private int tail;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[16];
    }

    @SuppressWarnings("unchecked")
    private void doubleCapacity() {
        int p = head;
        int n = elements.length;
        int r = n - p;
        int newCapacity = n << 1;
        if (newCapacity < 0) {
            throw new IllegalStateException("Sorry, deque too big");
        }
        E[] a = (E[]) new Object[newCapacity];
        System.arraycopy(elements, p, a, 0, r);
        System.arraycopy(elements, 0, a, r, p);
        elements = a;
        head = 0;
        tail = n;
    }

    @Override
    public int size() {
        return (tail - head) & (elements.length - 1);
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    public void addFirst(E e) {
        if (e == null) throw new NullPointerException();
        head = (head - 1) & (elements.length - 1);
        elements[head] = e;
        if (head == tail) {
            doubleCapacity();
        }
    }

    public void addLast(E e) {
        if (e == null) throw new NullPointerException();
        elements[tail] = e;
        tail = (tail + 1) & (elements.length - 1);
        if (tail == head) {
            doubleCapacity();
        }
    }

    public E element() {
        return getFirst();
    }

    public E getFirst() {
        E result = elements[head];
        if (result == null) {
            throw new NoSuchElementException();
        }
        return result;
    }

    public E getLast() {
        E result = elements[(tail - 1) & (elements.length - 1)];
        if (result == null) {
            throw new NoSuchElementException();
        }
        return result;
    }

    public E poll() {
        return pollFirst();
    }

    public E pollFirst() {
        int h = head;
        E result = elements[h];
        if (result == null) {
            return null;
        }
        elements[h] = null;
        head = (h + 1) & (elements.length - 1);
        return result;
    }

    public E pollLast() {
        int t = (tail - 1) & (elements.length - 1);
        E result = elements[t];
        if (result == null) {
            return null;
        }
        elements[t] = null;
        tail = t;
        return result;
    }

    @Override
    public String toString() {
        if (head == tail) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        int i = head;
        int mask = elements.length - 1;
        while (true) {
            E e = elements[i];
            sb.append(e == this ? "(this Collection)" : e);
            i = (i + 1) & mask;
            if (i == tail) {
                return sb.append(']').toString();
            }
            sb.append(", ");
        }
    }

    @Override public boolean isEmpty() { return head == tail; }
    @Override public boolean contains(Object o) { return false; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public boolean remove(Object o) { return false; }
    @Override public boolean containsAll(Collection<?> c) { return false; }
    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public void clear() {}
}
