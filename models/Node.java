package models;

public class Node<T> {
    T datas;
    public Node<T> next;

    public Node(T d, Node<T> s) {
        this.datas = d;
        this.next = s;
    }

    public Node(T d) {
        this.datas = d;
        this.next = null;
    }
}
