import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {

    private int numOfTeams;
    private FordFulkerson s;
    private String[] teams;
    private int[][] matchesAgainst;
    private int[] wins;
    private int[] losses;
    private int[] remaining;

    private int teamToIndex(String team) {
        return 
    }

    public BaseballElimination(String filename) {

    In in = new In(filename);

    numOfTeams = in.readInt(); 
    teams = new String[numOfTeams];
    wins = new int[numOfTeams];
    losses = new int[numOfTeams];
    remaining = new int[numOfTeams];
    matchesAgainst = new int[numOfTeams][numOfTeams];
    

    for (int i = 0; i<numOfTeams; i++) {
        teams[i] = in.readString();
        wins[i] = in.readInt();
        losses[i] = in.readInt();
        remaining[i] = in.readInt();
        
        for (int j = 0; j<numOfTeams; j++) {
            matchesAgainst[i][j] = in.readInt();
        }
        
    } 

    //FlowNetwork flowNetwork = new FlowNetwork(2 + numOfTeams + (numOfTeams-1)*(numOfTeams-2)/2);

    for (int i = 0; i<numOfTeams; i++) {



    }



    } 
    public int numberOfTeams() {
        return numOfTeams;
    }     
    public Iterable<String> teams() {
        
    }   
    public int wins(String team) {
        return 
    }  
    public int losses(String team) {
        return 
    } 
    public int remaining(String team) {
        return 
    }  
    public int against(String team1, String team2)  {
        return 
    }  
    public boolean isEliminated(String team)   {
    
    }    
    public Iterable<String> certificateOfElimination(String team){
    
    }  
    
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

}


