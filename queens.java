import java.util.*;

public class queens {

    static int count = 0;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter dimensions: ");
        int n = sc.nextInt();
        List<List<String>> res = solveNQueens(n);
        // printGrid(res);
        System.out.println("Result: " + count);
    }

    static boolean[] col, d1, d2;
    public static List<List<String>> solveNQueens(int n) {
        List<List<String>> res = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            List<String> temp = new ArrayList<>(n);
            for(int j = 0; j < n; j++) temp.add(".");
            res.add(temp);
        }
        col = new boolean[n+1];
        d1 = new boolean[2*n+1];
        d2 = new boolean[2*n+1];
        backtrack(res, 0, n);
        return res;
    }

    public static void backtrack(List<List<String>> grid, int ind, int n) {
        if(ind == n) {
            count++;
            printGrid(grid);
            System.out.println();
            System.out.println();
            return;
        }
        for(int c = 0; c < n; c++) {
            int d1Val = ind-c+n;    // rl
            int d2Val = ind+c;  //lr
            if(!col[c] && !d1[d1Val] && !d2[d2Val]) {
                col[c] = true;
                d1[d1Val] = true;
                d2[d2Val] = true;
                grid.get(ind).set(c, "Q");
                backtrack(grid, ind+1, n);
                grid.get(ind).set(c, ".");
                col[c] = false;
                d1[d1Val] = false;
                d2[d2Val] = false;
            }
        }
    }

    public static void printGrid(List<List<String>> grid) {
        for(List<String> row : grid) {
            for(String item : row) {
                System.out.print(item + " ");
            }
            System.out.println();
        }
    }
}
