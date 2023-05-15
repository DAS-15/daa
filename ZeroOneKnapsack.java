import java.util.*;

public class ZeroOneKnapsack {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        long start = System.currentTimeMillis();
        System.out.print("Enter the number of items: ");
        int n = sc.nextInt();
        System.out.print("Enter capacity of knapsack: ");
        int w = sc.nextInt();
        int[] values = new int[n];
        int[] weights = new int[n];
        System.out.print("Enter values: ");
        for(int i = 0; i < n; i++) {
            values[i] = sc.nextInt();
        }
        System.out.print("Enter weights: ");
        for(int i = 0; i < n; i++) {
            weights[i] = sc.nextInt();
        }
        System.out.println("Result: " + knapsack(n, w, values, weights));
        long end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) + "ms");
    }

    static int[][] memo;
    public static int knapsack(int n, int w, int[] values, int[] weights) {
        memo = new int[n+1][w+1];
        for(int[] row : memo) Arrays.fill(row, -1);
        int res = dp(values, weights, w, 0);
        print2d(memo);
        printIncludedItems(memo, values, weights, w, n);
        return res;
    }

    public static int dp(int[] values, int[] weights, int target, int ind) {
        if(ind>=values.length || target<0) return 0;
        if(memo[ind][target] != -1) return memo[ind][target];
        int notTake = dp(values, weights, target, ind+1);
        int take = 0;
        if(target >= weights[ind]) take = values[ind] + dp(values, weights, target-weights[ind], ind+1);
        return memo[ind][target] = Math.max(take, notTake);
    }

    public static void print2d(int[][] grid) {
        for(int[] row : grid) {
            for(int item : row) {
                System.out.print(item + " ");
            }
            System.out.println();
        }
    }
}
