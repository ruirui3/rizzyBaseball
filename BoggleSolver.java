import java.util.HashSet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {

    private final TrieST69 ts;
    private HashSet<String> validWords;

    public BoggleSolver(String[] dictionary) {

        if (dictionary == null) {
            throw new IllegalArgumentException();
        }

        ts = new TrieST69();

        for (String word : dictionary) {
            ts.add(word);
        }

    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {

        if (board == null) {
            throw new IllegalArgumentException();
        }

        HashSet<String> validWords = new HashSet<String>();

        boolean[][] visited = new boolean[board.rows()][board.cols()];

        for (int r = 0; r < board.rows(); r++) {

            for (int c = 0; c < board.cols(); c++) {

                buildValidWords(validWords, board, visited, "", r, c);

            }

        }

        return validWords;

    }

    private void buildValidWords(HashSet<String> validWords, BoggleBoard board, boolean[][] visited,
            String candidateWord, int r, int c) {

        char letter = board.getLetter(r, c);
        String word = candidateWord + letter; // String builder optimization? StringBuilder

        if (letter == 'Q') {
            word += 'U';
        }
        word = word.toUpperCase();
        visited[r][c] = true;

        // Check ActivePrefix (the ones that you have already checked) hashset - if it's
        // not active then check the trie
        if (!ts.keysWithPrefix(word).iterator().hasNext()) {
            visited[r][c] = false;
            return;
        }

        if (word.length() > 2 && ts.contains(word)) {
            validWords.add(word);
        }

        // check 8 possible moves (directional)
        for (int ri = -1; ri < 2; ri++) {

            for (int cj = -1; cj < 2; cj++) {

                // need to check it is not previous node, not itself, and not out of bounds
                if (!(ri == 0 && cj == 0)) {
                    if (isValidMove(board, visited, r + ri, c + cj)) {

                        buildValidWords(validWords, board, visited, word, r + ri, c + cj);

                    }
                }

            }

        }

        visited[r][c] = false;

    }

    private boolean isValidMove(BoggleBoard board, boolean[][] visited, int r, int c) {

        // bounds
        // if already visited
        // if out of bounds

        return r >= 0 && r < board.rows() &&
                c >= 0 && c < board.cols() &&
                !visited[r][c];

    }

    public int scoreOf(String word) {

        if (word == null || word.length() < 3) {
            throw new IllegalArgumentException();
        }

        if (word.length() == 3 || word.length() == 4) {
            return 1;
        }
        if (word.length() == 5) {
            return 2;
        }
        if (word.length() == 6) {
            return 3;
        }
        if (word.length() == 7) {
            return 5;
        }
        return 11;
    }

    public static void main(String[] args) {
        In in = new In("dictionary-yawl.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("board-q.txt");
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}