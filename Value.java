import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

abstract class Value {

    abstract void print();

    static int factorial(int n) {
        if (n == 0)
            return 1;
        else
            return (n * factorial(n - 1));
    }

    static boolean isInteger(Float number) {
        String asStr = number.toString();
        int sepPos = asStr.indexOf('.');
        for (int i = sepPos + 1; i < asStr.length(); i++) {
            if (asStr.charAt(i) != '0') {
                return false;
            }
        }
        return true;
    }

    private static ArrayList<Map.Entry<String, Integer>> findAllOccurrences(String line, String op) {
        ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>();
        for (int i = line.length() - 1; i >= 0; i--) {
            if (Character.toString(line.charAt(i)).equals(op)) {
                list.add(new AbstractMap.SimpleEntry<String, Integer>(op, i));
            }
        }
        return list;
    }

    private static boolean checkBracketsPos(String line, int opIndex) {
        int leftOpenBracket = line.substring(0, opIndex).lastIndexOf('(');
        int leftClosedBracket = line.substring(0, opIndex).lastIndexOf(')');
        int rightOpenBracket = line.indexOf('(', opIndex);
        int rightClosedBracket = line.indexOf(')', opIndex);

        if (leftOpenBracket * leftClosedBracket * rightOpenBracket * rightClosedBracket < 0) {
            return false;
        }
        if (leftOpenBracket == -1 && leftClosedBracket == -1) {
            return true;
        }
        if (rightOpenBracket == -1 && rightClosedBracket == -1) {
            return true;
        }
        if (leftClosedBracket > leftOpenBracket && rightOpenBracket < rightClosedBracket) {
            return true;
        }
        return false;
    }

    static Value parse(String line) throws Exception {
        line = line.replaceAll("\\s", "");
        String[] ops = new String[] { "+", "-", "*", "/", "^" };
        // System.out.println(line);
        if (findAllOccurrences(line, "(").size() != findAllOccurrences(line, ")").size()) {
            throw new Exception("Error: Uneven brackets");
        }
        ArrayList<Map.Entry<String, Integer>> allOpsPositions = new ArrayList<Map.Entry<String, Integer>>();
        for (String op : ops) {
            allOpsPositions.addAll(findAllOccurrences(line, op));
        }

        for (Map.Entry<String, Integer> OpPos : allOpsPositions) {
            if (checkBracketsPos(line, OpPos.getValue())) {
                return new Pair(new Atom(OpPos.getKey()),
                        new Pair(
                                Value.parse(line.substring(0, OpPos.getValue())),
                                Value.parse(line.substring(OpPos.getValue() + 1))));
            }

            if (line.indexOf('(') == -1 && line.indexOf(')') == -1) {
                return new Pair(new Atom(OpPos.getKey()),
                        new Pair(
                                Value.parse(line.substring(0, OpPos.getValue())),
                                Value.parse(line.substring(OpPos.getValue() + 1))));
            }
        }
        // remove outer brackets
        if (line.charAt(0) == '(' && line.charAt(line.length() - 1) == ')') {
            return Value.parse(line.substring(1, line.length() - 1));
        }
        // factorial
        if (line.charAt(line.length() - 1) == '!') {
            return new Pair(
                    new Atom("!"),
                    new Pair(Value.parse(line.substring(0, line.length() - 1)),
                            new Atom()));
        }

        String[] funcOperators = new String[] { "sin", "cos", "tan", "exp" };
        for (String func : funcOperators) {
            if (line.startsWith(func)) {
                return new Pair(
                        new Atom(func),
                        new Pair(Value.parse(line.substring(func.length(), line.length())),
                                new Atom()));
            }
        }
        return new Atom(line);
    }

    float calc() {
        if (this.getClass() == Atom.class) {
            switch (((Atom) this).value) {
                case "PI":
                    return (float) Math.PI;
                case "E":
                    return (float) Math.E;
                default:
                    return Float.parseFloat(((Atom) this).value);
            }
        }
        Pair pair = (Pair) this;

        switch (((Atom) pair.left).value) {
            case "+":
                return ((Pair) pair.right).left.calc() + ((Pair) pair.right).right.calc();

            case "-":
                return ((Pair) pair.right).left.calc() - ((Pair) pair.right).right.calc();

            case "*":
                return ((Pair) pair.right).left.calc() * ((Pair) pair.right).right.calc();

            case "/":
                return ((Pair) pair.right).left.calc() / ((Pair) pair.right).right.calc();

            case "^":
                return (float) Math.pow(((Pair) pair.right).left.calc(), ((Pair) pair.right).right.calc());

            case "!":
                boolean isInt = isInteger(((Pair) pair.right).left.calc());
                if (isInt)
                    return factorial((int) ((Pair) pair.right).left.calc());
                else {
                    throw new NumberFormatException("Error: Factorial of Float");
                }

            case "sin":
                return (float) Math.sin((double) ((Pair) pair.right).left.calc());

            case "cos":
                return (float) Math.cos((double) ((Pair) pair.right).left.calc());

            case "tan":
                return (float) Math.tan((double) ((Pair) pair.right).left.calc());
            case "exp":
                return (float) Math.exp((double) ((Pair) pair.right).left.calc());
            default:
                break;
        }
        System.out.println("\nError in calc");
        return 0.0f;
    }
}
