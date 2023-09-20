package edu.coursera.distributed;

public class Main {
    public static void main(String[] args) {
        int nodeId = 0;
        for (int i = 0; i < 10; i++) {
            System.out.println(nodeId);
            nodeId = getId(nodeId);
        }
    }
    private static int getId(int nodeId) {
        return ~ nodeId ;
    }
}
