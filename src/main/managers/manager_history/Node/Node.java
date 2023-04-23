package main.managers.manager_history.Node;

import main.tasks.Task;

public class Node {
    private Task task;
    private Node previous;
    private Node next;


    public Node(Task task) {
        this.task = task;
        previous = null;
        next = null;
    }

    public Node(Task task, Node preview) {
        this.task = task;
        this.previous = preview;
        this.next = null;
    }

    public Task getTask() {
        return task;
    }

    public Node getPrevious() {
        return previous;
    }

    public Node getNext() {
        return next;
    }

    public Node setTask(Task task) {
        this.task = task;
        return this;
    }

    public Node setPrevious(Node previous) {
        this.previous = previous;
        return this;
    }

    public Node setNext(Node next) {
        this.next = next;
        return this;
    }

}
