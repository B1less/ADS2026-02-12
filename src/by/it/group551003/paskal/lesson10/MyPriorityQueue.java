package by.it.group551003.paskal.lesson10;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyPriorityQueue<E> implements Collection<E> {

    private E[] queue;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        queue = (E[]) new Object[11];
    }

    private void grow() {
        int oldCapacity = queue.length;
        int newCapacity = oldCapacity + ((oldCapacity < 64) ? (oldCapacity + 2) : (oldCapacity >> 1));
        queue = Arrays.copyOf(queue, newCapacity);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            queue[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E e) {
        return offer(e);
    }

    public boolean offer(E e) {
        if (e == null) throw new NullPointerException();
        int i = size;
        if (i >= queue.length) {
            grow();
        }
        size = i + 1;
        if (i == 0) {
            queue[0] = e;
        } else {
            siftUp(i, e);
        }
        return true;
    }

    public E remove() {
        E x = poll();
        if (x != null) return x;
        else throw new NoSuchElementException();
    }

    public E poll() {
        if (size == 0) return null;
        int s = --size;
        E result = queue[0];
        E x = queue[s];
        queue[s] = null;
        if (s != 0) {
            siftDown(0, x);
        }
        return result;
    }

    public E peek() {
        return (size == 0) ? null : queue[0];
    }

    public E element() {
        E x = peek();
        if (x != null) return x;
        else throw new NoSuchElementException();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    private int indexOf(Object o) {
        if (o != null) {
            for (int i = 0; i < size; i++) {
                if (o.equals(queue[i])) return i;
            }
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    private void siftUp(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            E e = queue[parent];
            if (key.compareTo(e) >= 0) break;
            queue[k] = e;
            k = parent;
        }
        queue[k] = (E) key;
    }

    @SuppressWarnings("unchecked")
    private void siftDown(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            E c = queue[child];
            int right = child + 1;
            if (right < size && ((Comparable<? super E>) c).compareTo(queue[right]) > 0) {
                child = right;
                c = queue[child];
            }
            if (key.compareTo(c) <= 0) break;
            queue[k] = c;
            k = child;
        }
        queue[k] = (E) key;
    }

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, queue[i]);
        }
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
        if (c == null) throw new NullPointerException();
        if (c == this) throw new IllegalArgumentException();
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        return batchRemove(c, false);
    }


    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        return batchRemove(c, true);
    }

    private boolean batchRemove(Collection<?> c, boolean complement) {
        int w = 0;
        boolean modified = false;
        for (int r = 0; r < size; r++) {
            if (c.contains(queue[r]) == complement) {
                queue[w++] = queue[r];
            } else {
                modified = true;
            }
        }
        if (modified) {
            for (int i = w; i < size; i++) {
                queue[i] = null;
            }
            size = w;
            heapify();
        }
        return modified;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            sb.append(queue[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
        return sb.toString();
    }

    @Override public boolean remove(Object o) { int i = indexOf(o); if (i == -1) return false; else { removeAt(i); return true; } }
    private void removeAt(int i) {
        int s = --size;
        if (s == i) queue[i] = null;
        else {
            E moved = queue[s];
            queue[s] = null;
            siftDown(i, moved);
            if (queue[i] == moved) {
                siftUp(i, moved);
            }
        }
    }
    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}
