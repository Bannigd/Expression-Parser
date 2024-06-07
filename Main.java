import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        interactive();
        // test();
        // try {
        // String expr = "E^0";
        // System.out.println("Expr: " + expr);
        // Value v = Value.parse(expr);
        // v.print();
        // float val = v.calc();
        // System.out.println();
        // System.out.println(val);
        // System.out.println();
        // } catch (Exception e) {
        // System.out.println(e.getMessage());
        // }
    }

    static void interactive() {
        Scanner Scanner = new Scanner(System.in);
        System.out.print(">>>");
        String input = Scanner.nextLine();
        System.out.println("Expr: " + input);
        while (!input.equals("exit")) {
            try {
                Value v = Value.parse(input);
                v.print();
                float val = v.calc();
                System.out.println();
                System.out.println(val);
                System.out.println();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.print(">>>");
            input = Scanner.nextLine();
            System.out.println("Expr: " + input);
        }
        Scanner.close();
    }

    static void test() {
        float EPSILON = (float) 1e-6;
        Map<Map.Entry<String, Float>, String> tests = Map.ofEntries(
                Map.entry(Map.entry(
                        "1-2-3", (float) 1. - 2 - 3),
                        "SUCCESS"),
                Map.entry(Map.entry(
                        "1/2/3",
                        (float) 1. / 2 / 3), "SUCCESS"),
                Map.entry(Map.entry(
                        "(1-2)-3",
                        (float) (1. - 2) - 3), "SUCCESS"),
                Map.entry(Map.entry(
                        "((1-2)-3)/2",
                        (float) ((1. - 2) - 3) / 2), "SUCCESS"),
                Map.entry(Map.entry(
                        "(1-2)-3)/2",
                        Float.POSITIVE_INFINITY), "FAIL"),
                Map.entry(Map.entry(
                        "(((1+2)/2+1)/3+1)*4",
                        (float) (((1. + 2) / 2 + 1) / 3 + 1) * 4), "SUCCESS"),
                Map.entry(Map.entry(
                        "(1+2)-(4+2)",
                        (float) (1. + 2) - (4 + 2)), "SUCCESS"),
                Map.entry(Map.entry(
                        "(5+2.2)!",
                        Float.POSITIVE_INFINITY), "FAIL"),
                Map.entry(Map.entry(
                        "(3+4)!*3",
                        (float) Value.factorial(3 + 4) * 3), "SUCCESS"),
                Map.entry(Map.entry(
                        "(3+2)/(5-(4-7))*4/3",
                        (float) (3. + 2) / (5 - (4 - 7)) * 4 / 3), "SUCCESS"),
                Map.entry(Map.entry(
                        "1-(2-3)",
                        (float) 1. - (2 - 3)), "SUCCESS"),
                Map.entry(Map.entry(
                        "cos(PI/2)",
                        (float) Math.cos(Math.PI / 2)), "SUCCESS"),
                Map.entry(Map.entry(
                        "sin(5/2*PI) + 4!*2",
                        (float) (Math.sin(5. / 2 * Math.PI) + 2 * Value.factorial(4))), "SUCCESS"),
                Map.entry(Map.entry(
                        "tan(0)/3",
                        (float) Math.tan(0) / 3), "SUCCESS"),
                Map.entry(Map.entry(
                        "cos(3)^2+sin(3)^2",
                        (float) (Math.cos(3) * Math.cos(3) + Math.sin(3) * Math.sin(3))), "SUCCESS"),
                Map.entry(Map.entry(
                        "2^3",
                        (float) Math.pow(2, 3)), "SUCCESS"),
                Map.entry(Map.entry(
                        "(1+1)^3",
                        (float) Math.pow(1 + 1, 3)), "SUCCESS"),
                Map.entry(Map.entry(
                        "(1+1)^(2+2)",
                        (float) Math.pow(2, 4)), "SUCCESS"),
                Map.entry(Map.entry(
                        "1/2^2",
                        (float) (1. / Math.pow(2, 2))), "SUCCESS"));

        int passCounter = 0;
        for (Map.Entry<Map.Entry<String, Float>, String> test : tests.entrySet()) {
            System.out.println();
            System.out.println("Expr: " + test.getKey().getKey());

            Value v = new Atom();
            try {
                v = Value.parse(test.getKey().getKey());
            } catch (Exception e) {
                if (test.getValue() == "FAIL") {
                    System.out.println("PASSED: Expected " + test.getValue() + " Recieved " + "FAIL");
                    System.out.println(e.getMessage());
                    passCounter++;
                    continue;
                }
            }
            v.print();
            System.out.println();
            float val = Float.POSITIVE_INFINITY;
            try {
                val = v.calc();
            } catch (NumberFormatException e) {
                if (test.getValue() == "FAIL") {
                    System.out.println("PASSED: Expected " + test.getValue() + " Recieved " + "FAIL");
                    System.out.println(e.getMessage());
                    passCounter++;
                    continue;
                }
            }
            if (Math.abs(val - test.getKey().getValue()) > EPSILON) {
                System.out.println("NOT PASSED: Expected " + test.getKey().getValue() +
                        " Recieved " + val + ", diff: " + Math.abs(val - test.getKey().getValue()));
                continue;
            } else {
                System.out.println("PASSED: Expected " + test.getValue() +
                        " Recieved " + "SUCCESS | " + val + " "
                        + test.getKey().getValue());
                passCounter++;
                continue;
            }
        }
        System.out.println("PASSED " + passCounter + " of " + tests.size() + " tests.");
    }
}
