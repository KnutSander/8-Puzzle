package Assignement;

import java.io.File;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;

public class Solver {
    ArrayList<Node> unexpanded = new ArrayList<>();
    ArrayList<Node> expanded = new ArrayList<>();
    Node rootNode;
    static String filename;
    static int method;
    Instant start;
    Instant end;

    public Solver(char[][] initialBoard){
        GameState initialState = new GameState(initialBoard);
        rootNode = new Node(initialState);
        Scanner input = new Scanner(System.in);
        System.out.println("Choose search method: \n Type 1 for Uniform Cost Search \n Type 2 for A* Search");
        method = input.nextInt();

        if(method == 1) filename = "outputUniCost.txt";
        else if(method == 2) filename = "outputAstar.txt";
        else System.out.println("Invalid input");
    }

    /*
    The method uniformCostSolver uses Uniform Cost Search to find the optimal solution to the puzzle.
    In the case of this puzzle, the method doesn't differ from Breadth First Search, as the cost only
    increases by an increment of one for each step. It always picks the first element in the ArrayList,
    which will always be the one, or one of the nodes with the lowest cost. If the cost could differ I
    would implement a method that gets the lowest cost item form the array, but as stated that isn't
    necessary for this puzzle
    */

    public void uniformCostSolver(PrintWriter output){
        start = Instant.now();
        unexpanded.add(rootNode); //add the root node to the unexpanded list
        while(unexpanded.size() > 0){ //run until there aren't any nodes to explore
            Node n = unexpanded.get(0); //grab the first node in the array, which is the node with the lowest cost
            expanded.add(n);
            unexpanded.remove(n);
            if(n.state.isGoal()){ //check if the state is goal
                reportSolution(n, output);
                return;
            } else {
                ArrayList<GameState> moveList = n.state.possibleMoves(); //get an array of possible moves
                for(GameState gs : moveList){
                    if(Node.findNodeWithState(expanded, gs) == null && //check that the node doesn't already exist
                    Node.findNodeWithState(unexpanded, gs) == null){ //if true, add it to the unexpanded ArrayList
                        int newCost = n.getCost() + 1;
                        Node newNode = new Node(gs, n, newCost, false);
                        unexpanded.add(newNode);
                    }
                }
            }
        }
        System.out.println("No solution found!");
    }

    /*
    The method aStarSolver works much in the same way as uniformCostSolver. However, when it iterates
    through the moveList and finds a GameState that hasn't been explored, it checks if its trueCost, which
    is a combination of the cost of getting to the GameState and the heuristic estimate of moves left, is
    lower tha the trueCost of the first element in the unexpanded list. Doing this from the start ensures
    that the node with the lowest cost is always expanded first
     */

    public void aStarSolver(PrintWriter output){
        start = Instant.now();
        unexpanded.add(rootNode);
        while (unexpanded.size() > 0){
            Node n = unexpanded.get(0);
            expanded.add(n);
            unexpanded.remove(n);
            if(n.state.isGoal()){ //check if the state is goal
                reportSolution(n, output);
                return;
            } else {
                ArrayList<GameState> moveList = n.state.possibleMoves(); //get an array of possible moves
                for(GameState gs : moveList){
                    if(Node.findNodeWithState(expanded, gs) == null && //check that the node doesn't already exist
                            Node.findNodeWithState(unexpanded, gs) == null){ //if true, add it to the unexpanded ArrayList
                        int newCost = n.getCost() + 1;
                        Node newNode = new Node(gs, n, newCost, true);
                        if (!unexpanded.isEmpty() && (newNode.getTrueCost() <= unexpanded.get(0).getTrueCost())){
                            unexpanded.add(0, newNode);
                        } else {
                            unexpanded.add(newNode);
                        }
                    }
                }
            }
        }
        System.out.println("No solution found!");
    }

    public void printSolution(Node n, PrintWriter output) {
        if (n.parent != null) printSolution(n.parent, output);
        output.println(n.state);
    }

    public void reportSolution(Node n, PrintWriter output) {
        end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        output.println("Solution found!");
        printSolution(n, output);
        output.println(n.getCost() + " Moves");
        output.println("Nodes expanded: " + this.expanded.size());
        output.println("Nodes unexpanded: " + this.unexpanded.size());
        output.println(timeElapsed);
        output.println();
    }

    public static void main(String[] args) throws Exception {
        Solver problem = new Solver(GameState.INITIAL_STATE);
        File outFile = new File(filename);
        PrintWriter output = new PrintWriter(outFile);
        if(method == 1) problem.uniformCostSolver(output);
        else if(method == 2) problem.aStarSolver(output);
        output.close();
        System.out.println("Success!");
    }
}


