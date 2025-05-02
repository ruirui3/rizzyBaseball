import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {

    private int numOfTeams;
    private FordFulkerson s;
    private HashMap<String, BaseballElimination.Team> teams;
    
    public static class Team {
        private int win;
        private int loss;
        private int remain;
        private int[] against;
        private int teamID;
        private final int SOURCE = 0;
        private final int SINK = 1;

        public Team(int wins, int losses, int remaining, int[] againstTeams, int theTeamID) {
            win = wins;
            loss = losses;
            remain = remaining;
            against = againstTeams;
            teamID = theTeamID;
        }

        public int getWins() {
            return win;
        }

        public int getLosses() {
            return loss;
        }

        public int getRemaining() {
            return remain;
        }

        public int getTeamAgainst(int ID) {
            return against[ID];
        }

        public int getID() {
            return teamID;
        }

    } 

    public BaseballElimination(String filename) {

    In in = new In(filename);

    numOfTeams = in.readInt(); 
    int[] teamsAgainst = new int[numOfTeams];
    teams = new HashMap<String, BaseballElimination.Team>();
    for (int i = 0; i<numOfTeams; i++) {
        String team = in.readString();
        int win = in.readInt();
        int lose = in.readInt();
        int remain = in.readInt();
        
        for (int j = 0; j<numOfTeams; j++) {
            teamsAgainst[j] = in.readInt();
        }

            teams.put(team, new Team(win, lose, remain, teamsAgainst, i));
    } 

    



    } 
    public int numberOfTeams() {
        return numOfTeams;
    }     
    public Iterable<String> teams() {
        
    }   
    public int wins(String team) {
        return teams.get(team).getWins();
    }  
    public int losses(String team) {
        return teams.get(team).getLosses();
    } 
    public int remaining(String team) {
        return teams.get(team).getRemaining();
    }  
    public int against(String team1, String team2)  {
        return teams.get(team1).getTeamAgainst(teams.get(team2).getID());
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


