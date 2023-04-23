package main.managers.manager_history;

import main.managers.manager_history.Node.Node;
import main.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    protected final Map<Integer, Node> historyView = new HashMap<>();
    protected Node nodeList;

    private Node nodeFirst;
    private Node nodeLast;

    private Node linkLast(Task task) {
        nodeList = nodeLast;
        if (historyView.size() == 0) {
            nodeList = new Node(task);
            nodeFirst = nodeList;
            nodeLast = nodeList;
        } else {

            if (historyView.containsKey(task.getId())) {
                removeNode(historyView.get(task.getId()));
                historyView.remove(task.getId());
            }

            nodeList = new Node(task, nodeList);
            nodeList.getPrevious().setNext(nodeList);
            nodeLast = nodeList;
        }
        return nodeList;
    }

    private void removeNode(Node node) {
        Node nodePrevious = node.getPrevious();
        Node nodeNext = node.getNext();
        nodePrevious.setNext(nodeNext);
        nodeNext.setPrevious(nodePrevious);
    }

    @Override
    public void add(Task task) {
        historyView.put(task.getId(), linkLast(task));
    }

    @Override
    public void remove(int id) {
        for (Integer taskId : historyView.keySet()) {
            if (taskId == id) {
                removeNode(historyView.get(taskId));
                historyView.remove(id);
            }
        }
    }

    public ArrayList<Task> getTasks() {
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
        return tasks;
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}
