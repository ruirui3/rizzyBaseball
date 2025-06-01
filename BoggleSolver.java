import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {

    private final TrieST69 ts;
    private Set<String> validWords;
    private Set<String> prefixCache;
    private Set<String> invalidPrefixCache;

    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) {
            throw new IllegalArgumentException("Dictionary cannot be null.");
        }

        ts = new TrieST69();
        for (String word : dictionary) {
            if (word.length() >= 3) {
                ts.add(word);
            }
        }
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null.");
        }

        validWords = new HashSet<>();
        prefixCache = new HashSet<>();
        invalidPrefixCache = new HashSet<>();
        boolean[][] visited = new boolean[board.rows()][board.cols()];

        for (int r = 0; r < board.rows(); r++) {
            for (int c = 0; c < board.cols(); c++) {
                dfs(board, visited, new StringBuilder(), r, c);
            }
        }

        return validWords;
    }

    private void dfs(BoggleBoard board, boolean[][] visited, StringBuilder prefix, int r, int c) {
        if (!isValidMove(board, visited, r, c))
            return;

        int lengthBefore = prefix.length();
        char letter = board.getLetter(r, c);
        if (letter == 'Q') {
            prefix.append("QU");
        } else {
            prefix.append(letter);
        }

        String currentWord = prefix.toString();

        if (invalidPrefixCache.contains(currentWord)) {
            prefix.setLength(lengthBefore);
            return;
        }

        if (!prefixCache.contains(currentWord) && !ts.keysWithPrefix(currentWord).iterator().hasNext()) {
            invalidPrefixCache.add(currentWord);
            prefix.setLength(lengthBefore);
            return;
        } else {
            prefixCache.add(currentWord);
        }

        visited[r][c] = true;

        if (currentWord.length() >= 3 && ts.contains(currentWord)) {
            validWords.add(currentWord);
        }

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr != 0 || dc != 0) {
                    dfs(board, visited, prefix, r + dr, c + dc);
                }
            }
        }

        visited[r][c] = false;
        prefix.setLength(lengthBefore);
    }

    private boolean isValidMove(BoggleBoard board, boolean[][] visited, int r, int c) {
        return r >= 0 && r < board.rows() &&
                c >= 0 && c < board.cols() &&
                !visited[r][c];
    }

    public int scoreOf(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }

        if (!ts.contains(word) || word.length() < 3)
            return 0;

        int len = word.length();
        if (len == 3 || len == 4)
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

    /*
     * public static void main(String[] args) {
     * In in = new In("things/dictionary-yawl.txt");
     * String[] dictionary = in.readAllStrings();
     * BoggleSolver solver = new BoggleSolver(dictionary);
     * BoggleBoard board = new BoggleBoard("things/board-points26539.txt");
     * int score = 0;
     * for (String word : solver.getAllValidWords(board)) {
     * StdOut.println(word);
     * score += solver.scoreOf(word);
     * }
     * StdOut.println("Score = " + score);
     * }
     */
}
