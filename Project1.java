/*
1. Jawit Poopradit 6480087
2. Possathorn Sujipisut 6480274
3. Piyakorn Rodthanong 6480569
4. Phasin Ploypicha 6480567
*/
package Project1;

import java.io.*;
import java.util.*;

public class Project1 {

    public static void main(String[] args) {
        /*Board board = new Board(2);
        board.printBoard();*/
        Scanner sc = new Scanner(System.in);
        int step = 1;
        String restart;
        while (true){
            System.out.printf( "=-".repeat(20) + "=");   
            System.out.printf("\nEnter number of marbles on each side: ");
            try{
                int amount = Integer.parseInt(sc.next());
                if(amount < 2) {
                    System.out.printf("Number of marbles must be more than 2");
                    continue;
                }
                System.out.printf( "=-".repeat(20) + "=");
                Board board = new Board(amount);
                
                System.out.printf("\nInitial Board:\n"); 
                board.printBoard();
                System.out.printf( "=-".repeat(20) + "=");
                
                while(!board.puzzleSolved()){
                    //Print current board
                    System.out.printf("\nCurrent Step: Step %d", step);
                    System.out.printf("\nCurrent Board: \n");
                    board.printBoard();
                    
                    //Ask for next command                    
                    System.out.printf("\nEnter marble ID or A to switch to auto mode or Q to end the program");
                    System.out.printf("\nEnter command: ");
                    String input = sc.next();
                    System.out.println();
                    
                    if(!input.equalsIgnoreCase("q")){
                        if(input.equalsIgnoreCase("A")){
                            board.AutoMode();
                        }
                        else {
                            System.out.printf("Action done: ");
                            board.move(input,true);
                            step++;
                            System.out.printf( "=-".repeat(20) + "=");
                        }
                    }
                    else {
                        System.out.println("Terminating Program");
                        System.out.printf( "=-".repeat(20) + "=");
                        System.exit(0);
                    }
                }
                System.out.println("\nDone");
                System.out.println("R to restart or Q to end the program");
                restart = sc.next();
                if(restart.equalsIgnoreCase("r")){
                    continue;
                }
                else if(restart.equalsIgnoreCase("q")){
                    System.exit(0);
                }
            } catch (NumberFormatException e){
                System.out.println("Enter your input again");
            }
        }
    }
}

class Marble {
    private char color;
    private String marblenum;
    public Marble(char c, String n){
        this.color = c;
        this.marblenum = n;
    }
    
    public char getColor(){
        return color;
    }
    public String getMNum(){
        return marblenum;
    }
    public String PrintMarble() {
        return marblenum;
    }
}

class Board {
    private List<Marble> Board;
    private int size;
    Stack<List<Marble>> StackBoard = new Stack<>();
    
    public Board(int n){
        Board = new ArrayList<>();
        this.size = n;
        for (int i = 0; i < n; i++){
            Board.add(new Marble('w',"w"+i));
        }
        Board.add(null);
        for (int j = 0; j < n; j++){
            Board.add(new Marble('b',"b"+j));
        }
    }
    public void printBoard(){ //Reference: line 41-53 from https://github.com/felikf/cindys-puzzle/blob/master/src/main/java/com/felix/cindyspuzzle/Puzzle.java#L10
        //System.out.println("▯▮  ▢▇ ⚪⚫ ○●");  #Saving Symbol For Easier Copying#      
        //Marble Code
        for (Marble m : Board) {
            if (m == null) {
                System.out.print("__ ");
            } else {
                System.out.print(m.PrintMarble() + " ");
            }
        }
        
        //Marble Symbol
        System.out.println();
        for (Marble m : Board){        
            if (m == null){
                System.out.printf("__ ");
            }
            else if (m.getColor() == 'w'){
                System.out.printf("○  ");
            }
            else if (m.getColor() == 'b'){
                System.out.printf("●  ");
            }
        }
        System.out.println();
    }
    public void move(String marble, boolean PrintAction){
        List<Marble> CopyBoard = new ArrayList<>(); //For saving marble to push and pop in StackBoard
        for(Marble m : Board){
            if(m == null){
                CopyBoard.add(null);
            }
            else {
                CopyBoard.add(new Marble(m.getColor(), m.getMNum())); //add a copy of marble
            }
        }
        StackBoard.push(CopyBoard);
        
        int position = -1; //Find position of marble Reference: https://stackoverflow.com/questions/39318754/how-to-get-position-of-element-in-arraylist
        for(int i = 0; i < Board.size(); i++){
            Marble m = Board.get(i);
            if(m != null && m.PrintMarble().equalsIgnoreCase(marble)){
                position = i;
                break;
            }
        }
        if (position == -1) {
            System.out.println("Please input only marble that exist in current board");
            return;
        }
        Marble marb = Board.get(position);
        if (canMove(position)){
            if(marb.getColor() == 'w'){
                if(position+1 < Board.size() && Board.get(position+1) == null){
                    Collections.swap(Board, position, position+1);
                    //System.out.printf("Move right\n");
                    if(PrintAction){
                        System.out.printf("Move right\n");
                    }
                }
                else if(position+2 < Board.size() && Board.get(position+2) == null && Board.get(position+1).getColor() == 'b'){
                    Collections.swap(Board,position,position+2);
                    //System.out.printf("Jump right\n");
                    if(PrintAction){
                        System.out.printf("Jump right\n");
                    }
                }
            }
            if(marb.getColor() == 'b'){
                if(position-1 >= 0 && Board.get(position-1) == null){
                    Collections.swap(Board,position,position-1);
                    //System.out.printf("Move left\n");
                    if(PrintAction){
                        System.out.printf("Move left\n");
                    }
                }
                else if(position-2 >= 0 && Board.get(position-2) == null && Board.get(position-1).getColor() == 'w'){
                    Collections.swap(Board,position,position-2);
                    //System.out.printf("Jump left\n");
                    if(PrintAction){
                        System.out.printf("Jump left\n");
                    }
                }
            }
        }
        else {
            System.out.println("You can't move that marble");
        }
    }
    public boolean canMove(int i){ //Reference: line 55-81 https://github.com/felikf/cindys-puzzle/blob/master/src/main/java/com/felix/cindyspuzzle/Puzzle.java#L10
        Marble m = Board.get(i);
        if(m != null){
            if(m.getColor() == 'w'){
                if(i+1 < Board.size() && Board.get(i+1) == null){ //Check if the position is out of bound or not and whether it is an empty spot
                    return true;
                }
                if(i+2 < Board.size() && Board.get(i+2) == null && Board.get(i+1).getColor() == 'b'){ //if the position next to white marble is black check the position next to black whether it is empty and not out of bound
                    return true;
                }
            }
            if(m.getColor() == 'b'){ //Same as white but for black
                if(i-1 >= 0 && Board.get(i-1) == null){
                    return true;
                }
                if(i-2 >= 0 && Board.get(i-2) == null && Board.get(i-1).getColor() == 'w'){
                    return true;
                }
            }
            if(Board.get(i) == null) {
                return false;
            }
        }
        
        return false;
    }
    
    public boolean puzzleSolved(){ //Reference: line 124-140 from https://github.com/felikf/cindys-puzzle/blob/master/src/main/java/com/felix/cindyspuzzle/Puzzle.java#L10
        //int half = size/2;
        /*for(int i = 0; i < size; i++){
            if(Board.get(i).getColor() != 'b' || Board.get(i) == null){
                return false;
            }   
        }
        for(int i = size+1; i < 2*size+1; i++){
            if(Board.get(i).getColor() != 'w' || Board.get(i) == null){
                return false;
            }
        }*/
        for(int i = 0; i < size; i++){
            Marble m = Board.get(i);
            if(m == null || m.getColor() != 'b'){
                return false;
            }   
        }
        for(int i = size+1; i < 2*size+1; i++){
            Marble m = Board.get(i);
            if(m == null || m.getColor() != 'w'){
                return false;
            }
        }
        return true;
    }
    
    public boolean AutoMode() {
        //ArrayList correctPath = new ArrayList();
        //List<Marble> correctBoard = new ArrayList<>();
        if(puzzleSolved()){
            //printBoard();
            /*for(Object m : correctPath){
                System.out.println(m);
            }*/
            return true;
        }
        for(int i = 0; i < Board.size(); i++){
            if(canMove(i)){
                String marble = Board.get(i).getMNum();
                move(marble,false);
                System.out.println("AutoMove:");
                printBoard();
               
                if (AutoMode()){ //Check if AutoMode is true
                    return true;
                }
                undoMove();
                System.out.println("UndoMove:");
                printBoard();
            }
        }
        return false;
    }
    public void undoMove(){
        if(!StackBoard.isEmpty()){
            Board = StackBoard.pop();
        }
    }
}
