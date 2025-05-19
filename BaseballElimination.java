import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {

    private int numOfTeams;
    private String[] teams;
    private int[][] matchesAgainst;
    private int[] wins;
    private int[] losses;
    private int[] remaining;
    private boolean[] eliminated;
    private HashMap<String, Integer> teamID;
    private List<String>[] certificate;

    public BaseballElimination(String filename) {

        In in = new In(filename);

        numOfTeams = in.readInt();

        teams = new String[numOfTeams];

        wins = new int[numOfTeams];
        losses = new int[numOfTeams];
        remaining = new int[numOfTeams];
        matchesAgainst = new int[numOfTeams][numOfTeams];

        teamID = new HashMap<>();
        eliminated = new boolean[numOfTeams];
        certificate = (List<String>[]) new ArrayList[numOfTeams];
        int SOURCE = 0;
        int SINK = 1;

        for (int i = 0; i < numOfTeams; i++) {
            teams[i] = in.readString();
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remaining[i] = in.readInt();
            teamID.put(teams[i], i);

            for (int j = 0; j < numOfTeams; j++) {
                matchesAgainst[i][j] = in.readInt();
            }

        }

        int totalVertices = 2 + numOfTeams + ((numOfTeams - 1) * (numOfTeams - 2) / 2); // 2 source/sink, n for team
                                                                                        // vert, n-1 choose 2 for game
                                                                                        // vert betw others

        for (int x = 0; x < numOfTeams; x++) {

            List<String> certification = new ArrayList<>();

            int maxWinsPossible = wins[x] + remaining[x]; // right

            for (int i = 0; i < numOfTeams; i++) {

                if ((i != x) && maxWinsPossible < wins[i]) {
                    certification.add(teams[i]);
                    eliminated[x] = true;
                    break;

                }

            }

            if (eliminated[x]) {
                certificate[x] = certification;
            }

            FlowNetwork flowNetwork = new FlowNetwork(totalVertices);
            int gameNode = 2 + numOfTeams; // second part of the picture. draw the connecting lines between source and
                                           // gameNode, associate it with team verticies then to sink. compute maxFlow
                                           // with folkerson

            for (int i = 0; i < numOfTeams; i++) {

                if (maxWinsPossible - wins[i] >= 0) {

                    flowNetwork.addEdge(new FlowEdge(i + 2, SINK, maxWinsPossible - wins[i])); // end
                    // System.out.println(maxWinsPossible - wins[i]);
                } else {
                    break;
                }

                if (i == x) {
                    continue;
                }

                for (int j = i; j < numOfTeams; j++) {
                    if (j == x || j == i) {
                        continue;
                    }

                    flowNetwork.addEdge(new FlowEdge(SOURCE, gameNode, matchesAgainst[i][j]));
                    flowNetwork.addEdge(new FlowEdge(gameNode, 2 + i, Double.POSITIVE_INFINITY));
                    flowNetwork.addEdge(new FlowEdge(gameNode, 2 + j, Double.POSITIVE_INFINITY));
                    gameNode++;

                }

            }

            FordFulkerson ff = new FordFulkerson(flowNetwork, SOURCE, SINK); // compute maxflow

            for (int i = 0; i < numOfTeams; i++) {

                if (i != x && ff.inCut(2 + i)) {
                    certification.add(teams[i]);
                }

            }

            if (!certification.isEmpty()) {
                eliminated[x] = true;
                certificate[x] = certification;
            }

        }

    }

    public int numberOfTeams() {
        return numOfTeams;
    }

    public Iterable<String> teams() {
        return Arrays.asList(teams);
    }

    public int wins(String team) {
        Integer i = teamID.get(team);
        if (i == null) {
            throw new IllegalArgumentException();
        }
        return wins[i];
    }

    public int losses(String team) {
        Integer i = teamID.get(team);
        if (i == null) {
            throw new IllegalArgumentException();
        }
        return losses[i];
    }

    public int remaining(String team) {
        Integer i = teamID.get(team);
        if (i == null) {
            throw new IllegalArgumentException();
        }
        return remaining[i];
    }

    public int against(String team1, String team2) {
        Integer i = teamID.get(team1);
        Integer j = teamID.get(team2);
        if (i == null || j == null) {
            throw new IllegalArgumentException();
        }
        return matchesAgainst[i][j];
    }

    public boolean isEliminated(String team) {
        Integer i = teamID.get(team);
        if (i == null) {
            throw new IllegalArgumentException();
        }
        return eliminated[i];
    }

    public Iterable<String> certificateOfElimination(String team) {
        Integer i = teamID.get(team);
        if (i == null) {
            throw new IllegalArgumentException();
        }
        return certificate[i];
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination("teams5a.txt");
        // BaseballElimination division = new
        // BaseballElimination("baseball\\teams54.txt");
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

}
