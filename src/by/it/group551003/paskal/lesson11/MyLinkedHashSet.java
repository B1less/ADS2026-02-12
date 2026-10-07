package by.it.group551003.paskal.lesson11;

import java.util.Collection;
import java.util.Iterator;

public class MyLinkedHashSet<E> implements Collection<E> {

    private static class Node<E> {
        E data;
        Node<E> next;     // Для цепочки коллизий в бакете
        Node<E> before;   // Для порядка вставки (двусвязный список)
        Node<E> after;    // Для порядка вставки (двусвязный список)

        Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size = 0;
    private Node<E> head; // Первый добавленный элемент
    private Node<E> tail; // Последний добавленный элемент

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[16];
    }

    private int hash(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
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
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        int index = hash(o);
        Node<E> current = table[index];
        while (current != null) {
            if (o == null ? current.data == null : o.equals(current.data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        }
        if (size >= table.length * 0.75) {
            resize();
        }
        int index = hash(e);
        Node<E> newNode = new Node<>(e, table[index]);
        table[index] = newNode;

        // Привязываем к концу общего списка порядка вставки
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }
        size++;
        return true;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        table = (Node<E>[]) new Node[oldTable.length * 2];

        // Перестраиваем бакеты, сохраняя старые связи порядка вставки
        Node<E> current = head;
        while (current != null) {
            int index = hash(current.data);
            current.next = table[index];
            table[index] = current;
            current = current.after;
        }
    }

    @Override
    public boolean remove(Object o) {
        int index = hash(o);
        Node<E> current = table[index];
        Node<E> prev = null;
        while (current != null) {
            if (o == null ? current.data == null : o.equals(current.data)) {
                // Удаляем из цепочки коллизий
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                // Удаляем из двусвязного списка порядка вставки
                if (current.before != null) {
                    current.before.after = current.after;
                } else {
                    head = current.after;
                }
                if (current.after != null) {
                    current.after.before = current.before;
                } else {
                    tail = current.before;
                }

                size--;
                return true;
            }
            prev = current;
            current = current.next;
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
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.after;
            if (!c.contains(current.data)) {
                remove(current.data);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<E> current = head;
        while (current != null) {
            sb.append(current.data);
            current = current.after;
            if (current != null) {
                sb.append(", ");
            }
        }
        sb.append(']');
        return sb.toString();
    }

    // Оставшиеся редкие заглушки Collection
    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}
