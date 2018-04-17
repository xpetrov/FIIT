import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

class Node {
    Node[] children;
    int countOfPrefix;
    boolean endOfWord;

    protected Node() {
        children = new Node[26];
        countOfPrefix = 0;
        endOfWord = false;
    }
}

public class Source {
    static int n;

    public static void main(String[] args) throws Exception {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(stdin.readLine());

        Node root = new Node();
        int count = 0;

        StringTokenizer tokenizer = new StringTokenizer(stdin.readLine());
        String s;
        while (tokenizer.hasMoreTokens()) {
            s = tokenizer.nextToken();
            if (!exists(s, root)) {
                add(s, root);
                count += s.length();
            }
            else {
                count += 1 + getClicks(s.substring(1), root.children[indexOfFirstLetter(s)]);
            }
        }
        stdin.close();
        System.out.println(count);
    }

    static int getClicks(String s, Node node) {
        if (s.length() == 0)
            return 0;
        if (node.countOfPrefix == 1 && !node.endOfWord)
            return 0;

        return 1 + getClicks(s.substring(1), node.children[indexOfFirstLetter(s)]);
    }

    static boolean exists(String s, Node node) {
        if (s.length() == 0) {
            if (node.endOfWord == true)
                return true;
            else
                return false;
        }
        if (node.children[indexOfFirstLetter(s)] == null)
            return false;
        else
            return exists(s.substring(1), node.children[indexOfFirstLetter(s)]);

    }

    static void add(String s, Node node) {
        node.countOfPrefix++;
        if (s.length() == 0) {
            node.endOfWord = true;
            return;
        }
        if (node.children[indexOfFirstLetter(s)] == null)
            node.children[indexOfFirstLetter(s)] = new Node();
        add(s.substring(1), node.children[indexOfFirstLetter(s)]);
    }

    static int indexOfFirstLetter(String s) {
        return (int)(s.charAt(0)) - 97;
    }
}