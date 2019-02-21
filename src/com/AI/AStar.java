package com.AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Scanner;

public class AStar {

    HashMap<String, State> states;
    HashMap<String, State> visitedStates;
    public State startState;
    public State goalState;

    public class State {
        String name;
        int searchHeuristic;
        ArrayList<Action> actions;
        public void addAction(String target, int cost){
            actions.add(new Action(target, cost));
        }

        // state object constructor
        public State(String d, int heuristic){
            name = d;
            searchHeuristic = heuristic;
            actions = new ArrayList();
        }
    }

    public class Action {
        State targetState;
        int pathCost;

        public Action(String t, int c){
            targetState = states.get(t);
            pathCost = c;
        }
    }

    public class Node{
        State state;
        Node parent;
        Action action;
        int pathCost;
        public Node takeAction(Action a){
            Node n = new Node();
            n.parent = this;
            n.state = a.targetState;
            n.action = a;
            n.pathCost = this.pathCost + a.pathCost;
            return n;
        }
    }

    public State addState(String n, int h){
        State s = new State(n, h);
        states.put(n, s);
        return s;
    }

    // path finders
    public boolean depthFirst() {
        if(startState.name.equals(goalState.name)){
            System.out.println("Start and Goal states are equal");
            return true;
        }
        Stack<Node> nodeStack = new Stack<Node>();
        ArrayList<Node> visitedNodes = new ArrayList<Node>();

        Node startNode = new Node();
        startNode.state = startState;
        startNode.pathCost = 0;
        nodeStack.add(startNode);

        while(!nodeStack.isEmpty()){
            Node current = nodeStack.pop();
            System.out.print(current.state.name + " ");
            if(current.state.name.equals(goalState.name)){
                System.out.println(".");
                System.out.println("Goal state reached with " + current.pathCost + " cost");
                return true;
            }
            else {
                visitedStates.put(current.state.name, current.state);
                for(int i = 0; i < current.state.actions.size(); i++) { // right first
                    //for(int i = current.state.actions.size() - 1; i >= 0; i--) { // left first
                    Action a = current.state.actions.get(i);
                    if (visitedStates.get(a.targetState.name) == null) {
                        nodeStack.add(current.takeAction(a));
                    }
                }
            }
        }
        System.out.println("No path found to goal");
        return false;
    }

    public boolean aStar() {
        if(startState.name.equals(goalState.name)){
            System.out.println("Start and Goal states are equal");
            return true;
        }
        LinkedList<Node> nodeList = new LinkedList<Node>();

        Node startNode = new Node();
        startNode.state = startState;
        startNode.pathCost = 0;
        nodeList.add(startNode);

        while(!nodeList.isEmpty()){
            int leastValue = 0, nodeValue = 0;
            int leastNode = 0;
            // find next node with least current cost + estimated/guessed further cost
            // note this might be faster if the list is sorted on insert
            for (int i=0; i < nodeList.size(); i++){
                nodeValue = nodeList.get(i).pathCost + nodeList.get(i).state.searchHeuristic;
                if(nodeValue < leastValue || leastValue == 0) {
                    leastNode = i;
                    leastValue = nodeValue;
                }
            }
            Node current = nodeList.get(leastNode);
            nodeList.remove(leastNode);
            System.out.print(current.state.name + " ");
            if(current.state.name.equals(goalState.name)){
                System.out.println(".");
                System.out.println("Goal state reached with " + current.pathCost + " cost");
                System.out.print("Path (from goal to start): ");
                printPath(current);
                return true;
            }
            else {
                visitedStates.put(current.state.name, current.state);
                for(int i = 0; i < current.state.actions.size(); i++) { // right first
                    //for(int i = current.state.actions.size() - 1; i >= 0; i--) { // left first
                    Action a = current.state.actions.get(i);
                    if (visitedStates.get(a.targetState.name) == null){
                        nodeList.add(current.takeAction(a));
                    }
                }
            }
        }
        System.out.println("No path found to goal");
        return false;
    }

    // constructor
    public AStar(){
        states = new HashMap<String, State>();
        visitedStates = new HashMap<String, State>();
    }

    public static void printPath(Node lastNode) {
        Node n = lastNode;
        while (n != null){
            System.out.print(n.state.name + " ");
            n = n.parent;
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        Boolean pathFound;
        AStar tsp = new AStar();
        AStar.State a;

//Load States
        a = tsp.addState("CP", 15);
        //tsp.startState = a;
        a = tsp.addState("BH", 13);
        a = tsp.addState("RH", 8);
        a = tsp.addState("SC", 7);
        a = tsp.addState("UH", 8);
        a = tsp.addState("DH", 5);
        a = tsp.addState("SLG", 0);
        //tsp.goalState = a;

//Load Actions
        tsp.states.get("CP").addAction("BH",6);
        tsp.states.get("CP").addAction("SC", 6);
        tsp.states.get("CP").addAction("UH", 10);
        tsp.states.get("BH").addAction("SC", 4);
        tsp.states.get("BH").addAction("RH", 8);
        tsp.states.get("SC").addAction("RH", 3);
        tsp.states.get("SC").addAction("UH", 3);
        tsp.states.get("DH").addAction("SLG", 5);
        tsp.states.get("SC").addAction("SLG", 10);
        tsp.states.get("UH").addAction("DH", 7);
        tsp.states.get("RH").addAction("SLG", 8);

//Input Start State
        while (tsp.startState == null) {
            System.out.println("Enter Start state: ");
            String startName = scan.next();
            tsp.startState = tsp.states.get(startName);
        }
//Input Goal
        while (tsp.goalState == null) {
            System.out.println("Enter Goal state: ");
            String startName = scan.next();
            tsp.goalState = tsp.states.get(startName);
        }

        System.out.println("Trying depth first:");
        pathFound = tsp.depthFirst();
        System.out.println("Trying A*:");
        pathFound = tsp.aStar();
    }
}