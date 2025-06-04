import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {

    private final TrieST69 ts;

    // Initializes the data structure using the given array of strings as the
    // dictionary.
    // (You can assume each word in the dictionary contains only the uppercase
    // letters A through Z.)
    public BoggleSolver(String[] dictionary) {

        if (dictionary == null) {
            throw new IllegalArgumentException(); // if the argument - dictionary is null, throw argument illegal
                                                  // exectpon
        }
        ts = new TrieST69();

        // add all the words to the trie
        for (String word : dictionary) {
            if (word != null && word.matches("[A-Z]+")) {
                ts.add(word);
            }
        }
        // testing
        System.out.println("Words in disctnary: " + ts.size());

    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) {
            throw new IllegalArgumentException("null board passed to getAllValidWords");
        }

        // make hashset to store the valid words
        HashSet<String> validWords = new HashSet<String>();
        // 2d array to store words, make sure no duplicates
        boolean[][] visited = new boolean[board.rows()][board.cols()];
        // cache prefixes
        HashSet<String> activePrefixes = new HashSet<>();
        for (int r = 0; r < board.rows(); r++) {
            for (int c = 0; c < board.cols(); c++) {
                // recursive...
                // start dfs traversal, call
                buildValidWords(validWords, board, visited, "", r, c, activePrefixes);
            }
        }
        // testing
        System.out.println("Finding words on board:");
        for (String word : validWords) {
            System.out.println(word);
        }

        // recursive function done, return the set that was found
        return validWords;

    }

    // helper method, takes in board, hashset with dictionary of valid words, set to
    // see if visited,
    // WHAt is candidateWord?
    // row and column value in the board
    private void buildValidWords(HashSet<String> vw, BoggleBoard board, boolean[][] visited,
            String candidateWord, int r, int c, HashSet<String> activePrefixes) {
        // implemented earlier: HashSet<String> activePrefixes = new HashSet<String>();
        // add onto the initial entered word, adding on the next letter we're going
        // through
        String word = candidateWord + board.getLetter(r, c);
        if (board.getLetter(r, c) == 'Q') {
            word += 'U';
        }
        // mark as visited

        visited[r][c] = true;
        // prefixes!!!!!
        // cache prefixes? keysWithPrefix is expensive
        // if we know there is a prefix already, we shouldn't check again - because a
        // lot of the time we already ahve
        // take a result of computed, save it somewehre and do a faster clal next time
        // if i check activeprefix hashset - if its not active then check the trie,
        // if (!activePrefixes.contains(word)) {
        // if (!ts.keysWithPrefix(word).iterator().hasNext()){
        // visited[r][c] = false; //reset the visited array to get state back to 0
        // return;
        // }

        // }

        // Only skip if the prefix is cached as bad, AND the Trie confirms it
        if (activePrefixes.contains(word)) {
            visited[r][c] = false;
            return;
        }

        if (!ts.keysWithPrefix(word).iterator().hasNext()) {
            activePrefixes.add(word); // cache bad prefix
            visited[r][c] = false;
            return;
        }

        // do we add to the set?
        // 1. does it match dictionary
        // 2. is it at least 3 letters?
        if (word.length() > 2 && ts.contains(word)) {
            vw.add(word);
        }
        // boundary checks, but we can go diagonal

        // check 8 possible moves
        for (int ri = -1; ri < 2; ri++) {
            for (int cj = -1; cj < 2; cj++) {
                // check if already visited
                // check borders
                // is it itself?
                if (!(ri == 0 && cj == 0)) {
                    if (canMove(board, visited, r + ri, c + cj)) {
                        // recursion call iehehehe
                        buildValidWords(vw, board, visited, word, r + ri, c + cj, activePrefixes);
                        // AND RESET

                    }
                }
            }
        }
        visited[r][c] = false;

    }

    private boolean canMove(BoggleBoard board, boolean[][] visited, int r, int c) {
        // check bounds

        // //if out of bounds
        // if (r < 0 || r >= board.rows() || c < 0 || c >= board.cols()){
        // return false;
        // }

        // //if already visited
        // if(visited[r][c]){
        // return false;
        // }

        // return true;

        return r >= 0 && r < board.rows() &&
                c >= 0 && c < board.cols() &&
                !visited[r][c];
    }

    // Returns the score of the given word if it is in the dictionary, zero
    // otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null || !ts.contains(word) || word.length() < 3) {
            return 0;
        }
        int len = word.length();
        if (len <= 4)
            return 1;
        if (len == 5)
            return 2;
        if (len == 6)
            return 3;
        if (len == 7)
            return 5;
        return 11;
    }

    public static void main(String[] args) {
        In in = new In("things/dictionary-16q.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("things/board-16q.txt");
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
