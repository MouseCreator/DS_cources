package edu.coursera.parallel;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        int R = 8;
        int[][] tree = new int[R][];
        for (int i = 0; i < R; i++) {
            tree[i] = new int[i+1];
        }
        print(tree);
        calculateRow(tree, R);
        System.out.println(getterCalled);
    }
    private static int getterCalled;

    private static class Cell {
        private int x = 0;
        private int y= 0;

        Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cell cell = (Cell) o;
            return x == cell.x && y == cell.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
    private static Set<Cell> cache = new HashSet<>();
    private static int choose(final int N, final int K) {
        Cell key = new Cell(N, K);
        if (!cache.contains(key)) {
            if (N == 0) {
                return 1;
            } else if (K == 0) {
                choose(N-1, K);
                getterCalled++;
            } else if (N == K) {
                choose(N - 1, K - 1);
                getterCalled++;
            } else {
                choose(N - 1, K - 1);
                choose(N -1, K);
                getterCalled += 2;
            }
            cache.add(key);
        }
        return 1;

    }
    private static int calculateRow(int[][] tree, int r) {
        for (int i = 0; i < r; i++) {
            choose(i, r);
        }
        return 0;
    }

    private static void print(int[][] tree) {
        for (int[] a : tree) {
            StringBuilder res = new StringBuilder();
            for (int b : a) {
                res.append(b).append(" ");
            }
            System.out.println(res);
        }
    }
}
