import java.util.*;

public class TspBranchAndBound {

    public static Node upper = null;
    // Node start = null;

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        int inf = 1000000;
        // int[][] matrix = {
        //     {inf, 20, 30, 10, 11},
        //     {15, inf, 16, 4, 2}, 
        //     {3, 5, inf, 2, 4}, 
        //     {19, 6, 18, inf, 3}, 
        //     {16, 4, 7, 16, inf}
        // };

        // base matrix
        // int[][] matrix = {
        //     {inf, 10, 15, 20},
        //     {5, inf, 0, 10}, 
        //     {6, 13, inf, 12}, 
        //     {8, 8, 9, inf}
        // };

        int[][] matrix = {
            {inf, 14, 12, 1, 11, 12, 19, 4, 20, 16},
            {14, inf, 9, 2, 13, 15, 17, 5, 2, 19},
            {12, 9, inf, 6, 2, 15, 14, 11, 3, 19},
            {1, 2, 6, inf, 9, 13, 2, 2, 19, 2},
            {11, 13, 2, 9, inf, 7, 18, 9, 4, 6},
            {12, 15, 15, 13, 7, inf, 4, 4, 20, 7},
            {19, 17, 14, 2, 18, 4, inf, 15, 1, 8},
            {4, 5, 11, 2, 9, 4, 15, inf, 11, 17},
            {20, 2, 3, 19, 4, 20, 1, 11, inf, 20},
            {16, 19, 19, 2, 6, 7, 8, 17, 20, inf}
        };

        int reducedCost = 0;

        // Reducing all rows and calculating reduced cost for them.
        for(int[] row : matrix) {
            int minVal = inf;
            for(int e : row) minVal = Math.min(minVal, e);
            for(int i = 0; i < row.length; i++) {
                row[i] = row[i] - minVal;
            }
            reducedCost += minVal;
        }

        // Reducing all columns and calculating reduced cost for them.
        for(int i = 0; i < matrix.length; i++) {
            int minVal = inf;
            for(int j = 0; j < matrix.length; j++) {
                minVal = Math.min(minVal, matrix[j][i]);
            }
            for(int j = 0; j < matrix.length; j++) {
                matrix[j][i] = matrix[j][i] - minVal;
            }
            reducedCost+=minVal;
        }

        // displayGrid(matrix);

        PriorityQueue<Node> pq = new PriorityQueue<Node>(Comparator.comparingInt(a -> a.cost));
        boolean[] visited = new boolean[matrix.length];
        Node root = new Node(null, null, 0); // Node(parent, start, index)
        
        // setting respective values
        root.grid = matrix;
        root.cost = reducedCost;
        root.visited = new boolean[visited.length];
        for (int ind = 0; ind < visited.length; ind++) root.visited[ind] = visited[ind]; // Copy visited array into root.visited
        
        pq.add(root); // add root to pq
        
        visited[0] = true; // mark self as visited 
        
        solve(pq, visited);

        // Generating the path
        System.out.println();
        System.out.println("PathCost: " + upper.cost);
        Stack<Integer> stack = new Stack();
        int leng = matrix.length;
        while(upper.parent != null) {
            stack.push(upper.index);
            upper = upper.parent;
            leng--;
        }
        stack.push(upper.index);
        System.out.print("Result path: ");
        while(!stack.isEmpty()) System.out.print(stack.pop() + " ");

        long finish = System.currentTimeMillis();
        // long timeElapsed = finish - start;
        System.out.println("Time taken: " + (finish - start) + "ms");
    }


    public static void displayGrid(int[][] grid) {
        for(int[] item : grid) {
            for(int i : item) {
                if(i > (int)(1e5)) {
                    System.out.print("inf ");
                } else {
                    System.out.print(i + " ");
                }
            }
            System.out.println();
        }
    }

    public static int[][] calculateCostAndGenerateGrid(int[][] grid, Node currentNode, Node parentNode) {
        int reducedCost = 0;
        int inf = 1000000;

        System.out.println(parentNode.index + " " + currentNode.index);

        // parent to child base cost
        int pathCost = grid[parentNode.index][currentNode.index];
        int[][] matrix = copyGrid(grid);

        // Make parent row infinity
        for(int i = 0; i < matrix.length; i++) {
            matrix[parentNode.index][i] = inf;
        }

        // Make child column infinity
        for(int i = 0; i < matrix.length; i++) {
            matrix[i][currentNode.index] = inf;
        }

        for(int[] row : matrix) {
            int minVal = inf;
            for(int e : row) minVal = Math.min(minVal, e);

            // if minVal is infinity then don't add it into reducedCost
            if(minVal > 0 && minVal < 1000){
                reducedCost += minVal;
            }

            if(minVal > 0 && minVal < 1000){
                for(int i = 0; i < row.length; i++) {
                    row[i] = row[i] - minVal;
                }
            }
        }

        for(int i = 0; i < matrix.length; i++) {
            int minVal = inf;
            for(int j = 0; j < matrix.length; j++) {
                minVal = Math.min(minVal, matrix[j][i]);
            }

            // if minVal is infinity then don't add it into reducedCost
            if(minVal > 0 && minVal < 1000) {
                reducedCost+=minVal;
            }

            if(minVal > 0 && minVal < 1000){
                for(int j = 0; j < matrix.length; j++) {
                    matrix[j][i] = matrix[j][i] - minVal;
                }
            }
        }

        // make matrix[parent][child] and matrix[child][parent] equal to infinity 
        matrix[parentNode.index][currentNode.index] = inf;
        matrix[currentNode.index][parentNode.start.index] = inf;

        // Remember to add pathCost 
        int currCost = parentNode.cost + pathCost + reducedCost;
        int currCostWithoutPathCost = parentNode.cost + reducedCost;
        System.out.println("Parent Node Cost: " + parentNode.cost);
        System.out.println("Path Cost: " + pathCost);
        System.out.println("Current Reduced Cost: " + reducedCost);
        if(currCost > 0 && currCost < 1000) {
            currentNode.cost = currCost;
        }
        System.out.println();
        displayGrid(matrix);
        System.out.println();
        return matrix;
    }

    public static int[][] copyGrid(int[][] old) {
        int[][] current = new int[old.length][old.length];
        for(int i=0; i<old.length; i++)
            for(int j=0; j<old[i].length; j++)
                current[i][j]=old[i][j];
        return current;
    }

    static int stage = 0;
    public static void solve(PriorityQueue<Node> pq, boolean[] visited) {

        Node parent = pq.poll();

        // stage == 0 indicates that solve has been called for the first time i.e. parent == root
        // so it sets the parent of root node to root node.
        if(stage == 0) {
            parent.start = parent;
            stage = 1;
        }

        parent.visited[parent.index] = true;
        
        // Explore the remaining children of the minimum cost node
        // Similar to BFS
        for(int i = 0; i < visited.length; i++) {
            if(!parent.visited[i]) {
                Node current = new Node(parent, parent.start, i);
                current.grid = new int[visited.length][visited.length];
                current.grid = calculateCostAndGenerateGrid(parent.grid, current, parent);

                current.visited =  new boolean[visited.length];
                for (int ind = 0; ind < visited.length; ind++) current.visited[ind] = parent.visited[ind];

                pq.add(current);
            }
        }

        for(int i = 0; i < visited.length; i++) visited[i] = parent.visited[i];
        System.out.print("Current visited: ");
        for(boolean visit : visited) System.out.print(visit + " ");
        System.out.println();
        System.out.println();

        // Elements present in pq
        for(Node i : pq) {
            System.out.println(i.parent.index + "-" + i.index + ": " + i.cost);
            // for(boolean item : i.visited) System.out.print(" " + item);
        }
        System.out.println();

        boolean allSet = true;
        for(boolean i : visited) if(i!=true) allSet = false;
        System.out.println("Is allset: " + allSet);

        if(allSet) {

            // Upper is the best solution that we currently have
            upper = (upper == null || upper.cost > parent.cost) ? new Node(parent) : upper;
            while(!pq.isEmpty()) {
                Node temp = pq.peek();
                System.out.println("popped: " + temp.cost);
                if(temp.cost >= upper.cost) {
                    pq.poll();
                } else {
                    break;
                }
            }

            if(pq.isEmpty()) {
                return;
            } else {
                boolean[] tempVisited = new boolean[visited.length];
                Node topElement = pq.peek();
                for(int i = 0; i < visited.length; i++) tempVisited[i] = topElement.visited[i]; 
                solve(pq, tempVisited);
            }

        }
        
        solve(pq, visited);
    }
}

class Node{
    public Node parent, start;
    public int index, cost;
    public int[][] grid;
    public boolean[] visited;

    // General constructor
    Node(Node parent, Node start, int index) {
        this.parent = parent;
        this.index = index;
        this.start = start;
        this.cost = 1000000;
    }

    // Copy constructor
    Node(Node another_node) {
        this.parent = another_node.parent;
        this.start = another_node.start;
        this.index = another_node.index;
        this.cost = another_node.cost;
        this.grid = copyGrid(another_node.grid);
        this.visited =  new boolean[another_node.visited.length];
        for (int ind = 0; ind < visited.length; ind++) this.visited[ind] = another_node.visited[ind];;
    }

    public static int[][] copyGrid(int[][] old) {
        int[][] current = new int[old.length][old.length];
        for(int i=0; i<old.length; i++)
            for(int j=0; j<old[i].length; j++)
                current[i][j]=old[i][j];
        return current;
    }
}