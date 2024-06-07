class Pair extends Value {
    Value left, right;

    Pair() {
        left = null;
        right = null;
    }

    Pair(Value left, Value right) {
        this.left = left;
        this.right = right;
    }

    void print() {
        System.out.print("(");
        left.print();
        System.out.print(", ");
        right.print();
        System.out.print(")");
    }
}
