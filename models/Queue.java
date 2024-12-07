package models;

import java.util.function.Function;

public class Queue<T> {
    private Node<T> head;
    private int size;

    public Queue(int size) {
        this.head = null;
        this.size = size;
    }

    public void add(T data) {
        Node<T> newNode = new Node<T>(data);

        if (head == null) {
            head = newNode;
        } else {
            Node<T> tmp = head;
            if (size() < size) {
                while (tmp.next != null) {
                    tmp = tmp.next;
                }
                tmp.next = newNode;
                return;
            }
            return;
        }
    }

    public boolean insert(T data, int position) {
        Node<T> newNode = new Node<T>(data);
        if (position + 1 <= size) {
            if (position == 0) {
                newNode.next = head;
                head = newNode;
                return false;
            }

            Node<T> tmp = head;
            for (int i = 1; i < position && tmp != null; i++) {
                tmp = tmp.next;
            }

            if (tmp != null) {
                newNode.next = tmp.next;
                tmp.next = newNode;
                return true;
            }
        }
        return false;
    }

    public boolean update(T data, T newData) {
        Node<T> tmp = head;

        while (tmp != null) {
            if (tmp.datas.equals(data)) {
                tmp.datas = newData;
                return true;
            }
            tmp = tmp.next;
        }
        return false;
    }

    public boolean remove(T data) {
        if (head == null)
            return false;

        if (head.datas.equals(data)) {
            Node<T> temp = head;
            head = temp.next;
            return true;
        }

        Node<T> tmp = head;
        while (tmp.next != null && !tmp.next.datas.equals(data)) {
            tmp = tmp.next;
        }

        if (tmp.next == null)
            return false;

        Node<T> temp = tmp.next;
        tmp.next = temp.next;
        return true;
    }

    public Node<T> get(T identifier) {
        Node<T> tmp = head;

        while (tmp != null) {
            if (tmp.datas.equals(identifier))
                return tmp;
            tmp = tmp.next;
        }

        return null;
    }

    public int size() {
        Node<T> tmp = head;
        int size = 0;
        while (tmp != null) {
            tmp = tmp.next;
            size++;
        }

        return size;
    }

    public int getPosition(T data) {
        Node<T> current = head;
        int position = 0;

        while (current != null) {
            if (current.datas.equals(data))
                return position;
            current = current.next;
            position++;
        }

        return -1;
    }

    public void foreach(Function<T, ?> func) {
        Node<T> tmp = head;
        while (tmp != null) {
            func.apply(tmp.datas);
            tmp = tmp.next;
        }
    }

    public Queue<T> map(Function<T, T> func) {
        Queue<T> newList = new Queue<T>(size);
        Node<T> tmp = head;

        while (tmp != null) {
            newList.add(func.apply(tmp.datas));
            tmp = tmp.next;
        }
        return newList;
    }

    public void printList() {
        Node<T> tmp = head;
        while (tmp != null) {
            System.out.print(tmp.datas.toString() + " -> ");
            tmp = tmp.next;
        }
        System.out.println("null");
    }
}
