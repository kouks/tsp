package io.pavelkoch.tsp;

import java.util.ArrayList;
import java.util.List;

public class Tree<T> {

    private Node<T> root;
    private List<Node<T>> children = new ArrayList<>();

    public static class Node<T> {
        private T data;
        private Node<T> parent;
        private List<Node<T>> children;
    }
}
