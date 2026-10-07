package by.it.group551003.paskal.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Collection<E> {

    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev;

        Node(E data, Node<E> prev, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> first;
    private Node<E> last;
    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    public void addFirst(E e) {
        final Node<E> f = first;
        final Node<E> newNode = new Node<>(e, null, f);
        first = newNode;
        if (f == null) {
            last = newNode;
        } else {
            f.prev = newNode;
        }
        size++;
    }

    public void addLast(E e) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(e, l, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        size++;
    }

    public E element() {
        return getFirst();
    }

    public E getFirst() {
        final Node<E> f = first;
        if (f == null) throw new NoSuchElementException();
        return f.data;
    }

    public E getLast() {
        final Node<E> l = last;
        if (l == null) throw new NoSuchElementException();
        return l.data;
    }

    public E poll() {
        return pollFirst();
    }

    public E pollFirst() {
        final Node<E> f = first;
        return (f == null) ? null : unlinkFirst(f);
    }

    public E pollLast() {
        final Node<E> l = last;
        return (l == null) ? null : unlinkLast(l);
    }

    private E unlinkFirst(Node<E> f) {
        final E element = f.data;
        final Node<E> next = f.next;
        f.data = null;
        f.next = null;
        first = next;
        if (next == null) {
            last = null;
        } else {
            next.prev = null;
        }
        size--;
        return element;
    }

    private E unlinkLast(Node<E> l) {
        final E element = l.data;
        final Node<E> prev = l.prev;
        l.data = null;
        l.prev = null;
        last = prev;
        if (prev == null) {
            first = null;
        } else {
            prev.next = null;
        }
        size--;
        return element;
    }

    private E unlink(Node<E> x) {
        final E element = x.data;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.data = null;
        size--;
        return element;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> x;
        if (index < (size >> 1)) {
            x = first;
            for (int i = 0; i < index; i++) x = x.next;
        } else {
            x = last;
            for (int i = size - 1; i > index; i--) x = x.prev;
        }
        return unlink(x);
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.data == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.data)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<E> curr = first;
        while (curr != null) {
            sb.append(curr.data);
            curr = curr.next;
            if (curr != null) {
                sb.append(", ");
            }
        }
        sb.append(']');
        return sb.toString();
    }

    @Override public boolean isEmpty() { return size == 0; }
    @Override public boolean contains(Object o) { return false; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public boolean containsAll(Collection<?> c) { return false; }
    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public void clear() { first = last = null; size = 0; }
}
