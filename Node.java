package Assignement;

import java.util.ArrayList;

public class Node {
    GameState state;
    Node parent;
    private int cost;
    private int heCost;

    public Node(GameState state, Node parent, int cost, boolean he){ //constructor for creating nodes
        this.state = state;
        this.parent = parent;
        this.cost = cost;
        if(he == true){
            this.heCost = setHECost();
        }
    }

    public Node(GameState state){this(state,null,0, false);} //constructor to create the initial node

    public int getCost(){ return cost; }

    public String toString() {
        return "Node:" + state + " ";
    }

    public static Node findNodeWithState(ArrayList<Node> nodeList, GameState gs) {
        for (Node n : nodeList) {
            if (gs.sameBoard(n.state)) return n;
        }
        return null;
    }

    public int getTrueCost(){ return cost + heCost; }

    public int setHECost(){ return state.calcHECost(this.state); }

}
