class Link extends Value {
    Value left, right;

    Link() {
        left = null;
        right = null;
    }

    Link(Value left, Value right) {
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
