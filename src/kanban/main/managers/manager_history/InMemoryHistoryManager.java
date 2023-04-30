package main.managers.manager_history;

import main.managers.manager_history.Node.Node;
import main.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> historyView = new HashMap<>();

    private Node nodeList;
    private Node nodeFirst;
    private Node nodeLast;

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        if (historyView.containsKey(id)) {
            removeNode(historyView.get(id));
            historyView.remove(id);
        }
    }

    @Override
    public void add(Task task) {
        historyView.put(task.getId(), linkLast(task));
    }

    private Node linkLast(Task task) {
        if (historyView.size() == 0) {
            nodeList = new Node(task);
            nodeFirst = nodeList;
            nodeLast = nodeList;
        } else {

            if (historyView.containsKey(task.getId())) {
                removeNode(historyView.get(task.getId()));
                historyView.remove(task.getId());
            }
            Node newNode = new Node(task, nodeList);
            nodeLast.setNext(newNode);
            newNode.setPrevious(nodeLast);
            nodeLast = newNode;
        }
        return nodeLast;
    }

    private void removeNode(Node node) {
        Node nodePrevious = null;
        Node nodeNext = null;
        if (node.getPrevious() == null) {
            nodeFirst = node.getNext();
        } else {
            nodePrevious = node.getPrevious();
        }

        if (node.getNext() == null) {
            nodeLast = node.getPrevious();
        } else {
            nodeNext = node.getNext();
        }

        if (nodePrevious != null) {
            nodePrevious.setNext(nodeNext);
        }
        if (nodeNext != null) {
            nodeNext.setPrevious(nodePrevious);
        }
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        nodeList = nodeFirst;
        if (historyView.size() == 0) {
            return new ArrayList<>();
        }

        while (nodeList.getNext() != null) {
            tasks.add(nodeList.getTask());
            nodeList = nodeList.getNext();
        }
        tasks.add(nodeList.getTask());
        nodeList = nodeLast;
        return tasks;
    }

}
