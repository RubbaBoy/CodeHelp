package com.uddernetworks.search;

public class SearchTest {

    public static void main(String[] args) {
        String input1 = "This test is a fcickin test of stuff to test some stuff and like test it lmao gg test.";

        System.out.println(findAmount(input1, "test")); // Should be 5

    }

    private static int findAmount(String input, String search) {
        int ret = 0;
        System.out.println("2222222222 input = " + input);
        while (true) {
            System.out.println("ret = " + ret);
            System.out.println("input = " + input);
            if (input.contains(search)) {
                input = input.replaceFirst(search, "");
                ret++;
            } else {
                break;
            }
        }
        return ret;
    }

}
