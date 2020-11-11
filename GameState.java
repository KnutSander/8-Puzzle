package Assignement;

import java.util.ArrayList;

public class GameState {
    final char[][] board;
    private int spacePosR;
    private int spacePosC;
    static final char[][] INITIAL_STATE = {{'8','7','6'}, {'5','4','3'}, {'2','1',' '}};
    static final char[][] GOAL_STATE = {{'1','2','3'}, {'4','5','6'}, {'7','8',' '}};
    static final int[][] GOAL_STATE_POSITION = {{2,2},{0,0},{0,1},{0,2},{1,0},{1,1},{1,2},{2,0},{2,1}};
                                              // ' '    1     2     3     4     5     6     7     8
    public GameState(char[][] board){ //constructor that takes a 2d array as it's argument
        this.board = board;
        for(int i = 0; i < 3; i++){ //it also gets the space position from the 2d array
            for(int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    this.spacePosR = i;
                    this.spacePosC = j;
                    break;
                }
            }
        }
    }

    public GameState clone(){ //method that returns an exact clone of the GameState
        char[][] clonedBoard = new char[3][];
        for(int i = 0; i < 3; i++){
            clonedBoard[i] = this.board[i].clone();
        }
        return new GameState(clonedBoard);
    }

    public String toString(){ //method that returns a string of the GameState
        String s = "";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++){
                s = s + this.board[i][j] + " ";
            }
            s = s + "\n";
        }
        return s;
    }

    public boolean isGoal(){ //method that checks if the GameState is the goal state
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++){
                if (this.board[i][j] != GOAL_STATE[i][j]) return false;
            }
        }
        return true;
    }

    public boolean sameBoard(GameState gs){ //method to check if two GameStates are the same
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++){
                if (this.board[i][j] != gs.board[i][j]) return false;
            }
        }
        return true;
    }

    /*
    calcHECost calculates the heuristic estimated cost of moves the given gamestate has left before it
    reaches the goal state. To do this it gets the position of every char in both the given gamestates array
    and the GOAL_STATE array. It then uses the taxicab geometry formula to determine how many moves away
    every char is from the goal state, and adds that to cost for every character. The sum of all the different
    characters moves combine into the heuristic estimate.
    */

    public int calcHECost(GameState gs){ //https://www.mathscareers.org.uk/article/taxicab-geometry/
        int cost = 0;
        for(int c = 0; c < 9; c++){
            char findThis;
            if(c == 0) findThis = ' '; //use int 0 to find the space character
            else findThis = (char) c; //cast the number into a char to find it in the array

            int thisCharR = 0;
            int thisCharC = 0;
            int goalCharR = GOAL_STATE_POSITION[c][0]; //use the goal state position array to assign the
            int goalCharC = GOAL_STATE_POSITION[c][1]; //values of goalCharR and goalCharC

            //these for loops assigns the thisCharR and thisCharC, representing the row and column of the char
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++){
                    if(gs.board[i][j] == findThis){
                        thisCharR = i;
                        thisCharC = j;
                    }
                }
            }
            cost += Math.abs(goalCharR - thisCharR) + Math.abs(goalCharC - thisCharC);
            //calculate the moves needed for the char to be in the correct position and add it to the cost
        }
        return cost;
    }

    /*
    The possibleMoves method returns a list of arrays of all the possible moves from the current GameState.
    The way it is implemented is using spacePosR and spacePosC. Since the values are saved in a 2d array that
    is 3x3, it uses the space positions and checks if adding or subtracting to the different space positions
    yields a number higher or equal to zero, or lower or equal to 2. This is because the top left corner of the
    array has the position [0][0] and the bottom right corner has the position [2][2], so having either of the
    space positions values higher then 2 or lower than 0 would not be possible.
     */

    public ArrayList<GameState> possibleMoves(){
        ArrayList<GameState> moves = new ArrayList<>();

        if((spacePosR - 1) >= 0){
            int numbPosR = spacePosR - 1; //get the row position of the number we are swapping with the space
            GameState newState = this.clone(); //make a clone of the current board
            newState.board[this.spacePosR][this.spacePosC] = this.board[numbPosR][this.spacePosC]; //put the number into the space position
            newState.board[numbPosR][this.spacePosC] = ' '; //set the numbers last position as the new place of the space
            newState.spacePosR = numbPosR; //update the space position value
            moves.add(newState); //add the new sate to the array of moves
        }

        if((spacePosR + 1) <= 2){
            int numbPosR = spacePosR + 1; //get the row position of the number we are swapping with the space
            GameState newState = this.clone(); //make a clone of the current board
            newState.board[this.spacePosR][this.spacePosC] = this.board[numbPosR][this.spacePosC]; //put the number into the space position
            newState.board[numbPosR][spacePosC] = ' '; //set the numbers last position as the new place of the space
            newState.spacePosR = numbPosR; //update the space position value
            moves.add(newState); //add the new sate to the array of moves
        }

        if((spacePosC - 1) >= 0){
            int numbPosC = spacePosC - 1; //get the column position of the number we are swapping with the space
            GameState newState = this.clone(); //make a clone of the current board
            newState.board[this.spacePosR][this.spacePosC] = this.board[this.spacePosR][numbPosC]; //put the number into the space position
            newState.board[this.spacePosR][numbPosC] = ' '; //set the numbers last position as the new place of the space
            newState.spacePosC = numbPosC; //update the space position value
            moves.add(newState); //add the new sate to the array of moves
        }

        if((spacePosC + 1) <= 2){
            int numbPosC = spacePosC + 1; //get the column position of the number we are swapping with the space
            GameState newState = this.clone(); //make a clone of the current board
            newState.board[this.spacePosR][this.spacePosC] = this.board[this.spacePosR][numbPosC]; //put the number into the space position
            newState.board[this.spacePosR][numbPosC] = ' '; //set the numbers last position as the new place of the space
            newState.spacePosC = numbPosC; //update the space position value
            moves.add(newState); //add the new sate to the array of moves
        }

        return moves;
    }

}
