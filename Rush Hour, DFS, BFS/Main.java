import java.io.*;
import java.util.*;

class Node {
    int num;
    char crossroads[][];
    int hash;
    Auto cars[];
    LinkedList<Node> children;
    Node parent;
    String note;

    public Node(Node parent, int numberOfCars) {
        children = new LinkedList<>();
        this.parent = parent;
        if (parent != null) {
            num = parent.num + 1;
            this.cars = new Auto[numberOfCars];
            Auto car;
            for (int n = 0; n < numberOfCars; n++) {
                car = parent.cars[n];
                if (parent.cars[n].position == 'v')
                    this.cars[n] = new Auto(car.x, car.y, car.length, 0, car.id);
                else
                    this.cars[n] = new Auto(car.x, car.y, car.length, 1, car.id);
            }
        }
        else {
            num = 0;
            note = "Start";
        }
    }
}

class Auto {
    int x, y, length, id;
    char position, idChar;

    public Auto(int x, int y, int length, int position, int id) {
        this.x = x;
        this.y = y;
        this.length = length;
        if (position == 0) this.position = 'v'; else this.position = 'h';
        this.id = id;
        this.idChar = (id < 10) ? (char)(id + 48) : (char)(id + 87);
    }
}

public class Main {
    static int numberOfCars;
    static boolean hashTable[];

    public static void main(String[] args) throws Exception{
        hashTable = new boolean[2000000]; // all 2 million values are "false" by default

        LinkedList<String> solutionList = new LinkedList<>();
        Scanner sc = new Scanner(new File("C:\\Users\\user\\Desktop\\input.txt"));
        numberOfCars = sc.nextInt();

        Node root = new Node(null, numberOfCars);
        root.cars = new Auto[numberOfCars];
        root.crossroads = new char[6][6];
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 6; j++)
                root.crossroads[i][j] = '-';

        for (int i = 0; i < numberOfCars; i++)
            root.cars[i] = new Auto(sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt(), i);
        drawCrossroads(root.cars, root.crossroads);
        root.hash = Arrays.deepHashCode(root.crossroads) % 1000000 + 999999;
        hashTable[root.hash] = true;

        sc.close();
        System.out.println("1 - DFS");
        System.out.println("2 - BFS");
        System.out.print(">>");
        sc = new Scanner(System.in);
        String input = sc.nextLine();

        System.out.println("Initial configuration:");
        display(root);

        System.out.println("Final configuration:");

        if (input.equals("1"))
            DFS(root, solutionList, 0);
        else
            BFS(root, solutionList);

        Iterator<String> iterator = solutionList.iterator();
        int n = 0;
        while (iterator.hasNext())
            System.out.println(n++ + ") " + iterator.next());
    }

    static void display(Node node) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                System.out.print(" " + node.crossroads[i][j] + " ");
            }
            System.out.println();
        }
    }

    static void drawCrossroads(Auto cars[], char crossroads[][]) {
        for (int i = 0; i < numberOfCars ; i++) {
            if (cars[i].position == 'h') {
                for (int y = cars[i].y; y < cars[i].y + cars[i].length; y++)
                    crossroads[cars[i].x][y] = cars[i].idChar;
            }
            else {
                for (int x = cars[i].x; x < cars[i].x + cars[i].length; x++)
                    crossroads[x][cars[i].y] = cars[i].idChar;
            }
        }
    }

    static boolean exists(int hash) {
        return hashTable[hash];
    }

    static void newChild(Node child, Auto car, int newCoordinate) {
        if (car.position == 'h')
            child.cars[car.id].y = newCoordinate;
        else child.cars[car.id].x = newCoordinate;
        child.crossroads = new char[6][6];
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 6; j++)
                child.crossroads[i][j]='-';

        drawCrossroads(child.cars, child.crossroads);
        child.hash=Arrays.deepHashCode(child.crossroads) % 1000000 + 999999;

        if (!exists(child.hash)) {
            child.parent.children.add(child);
            hashTable[child.hash] = true;

            int delta;
            if (car.position == 'h') {
                delta = newCoordinate - child.parent.cars[car.id].y;
                if (delta < 0)
                    child.note = "L (car_" + car.id + ", " + delta*(-1) + ")";
                else
                    child.note = "R (car_" + car.id + ", " + delta + ")";
            }
            else {
                delta = newCoordinate - child.parent.cars[car.id].x;
                if (delta < 0)
                    child.note = "U (car_" + car.id + ", " + delta*(-1) + ")";
                else
                    child.note = "D (car_" + car.id + ", " + delta + ")";
            }
        }
    }

    static void doChildren(Node node) {
        for (int n=0; n < numberOfCars; n++) {
            Auto car = node.cars[n];
            if (car.position == 'h') {
                for (int y=car.y-1; y >= 0; y--) {
                    if(node.crossroads[car.x][y] == '-') {
                        newChild(new Node(node, numberOfCars), car, y);
                    }
                    else break;
                }
                for (int y=car.y+car.length; y < 6; y++) {
                    if(node.crossroads[car.x][y] == '-') {
                        newChild(new Node(node, numberOfCars), car, y-car.length+1);
                    }
                    else break;
                }
            }
            else {
                for (int x=car.x-1; x >= 0; x--) {
                    if(node.crossroads[x][car.y] == '-') {
                        newChild(new Node(node, numberOfCars), car, x);
                    }
                    else break;
                }
                for (int x=car.x+car.length; x < 6; x++) {
                    if(node.crossroads[x][car.y] == '-') {
                        newChild(new Node(node, numberOfCars), car, x-car.length+1);
                    }
                    else break;
                }
            }
        }
    }

    static boolean DFS(Node node, LinkedList<String> solution, int count) {
        count++;
        if (node.cars[0].y == 6 - node.cars[0].length) {
            display(node);
            System.out.println(count + " configurations have been considered");
            solution.add(node.note);
            return true;
        }
        if (node.num == 1000)
            return false;

        doChildren(node);
        Iterator<Node> iterator = node.children.iterator();
        Node child;
        while (iterator.hasNext()) {
            child = iterator.next();
            if (DFS(child, solution, count)) {
                solution.addFirst(node.note);
                return true;
            }
        }
        return false;
    }

    static boolean BFS(Node node, LinkedList<String> solution) {
        LinkedList<Node> queue = new LinkedList<>();
        queue.add(node);
        int count = 0;

        Node head = null;
        while (queue.size() > 0) {
            count++;
            head = queue.removeFirst();
            if (head.cars[0].y == 6 - head.cars[0].length) {
                display(head);
                System.out.println(count + " configurations have been considered");
                solution.addFirst(head.note);
                head = head.parent;
                break;
            }
            else {
                if (head.num == 1000)
                    return false;
                doChildren(head);
                queue.addAll(head.children);
            }
        }
        if (solution.isEmpty())
            return false;
        else {
            while (head != null) {
                solution.addFirst(head.note);
                head = head.parent;
            }
            return true;
        }
    }
}