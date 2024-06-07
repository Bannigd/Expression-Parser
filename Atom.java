class Atom extends Value {
    String value;

    Atom() {
        value = null;
    }

    Atom(String value) {
        this.value = value;
    }

    boolean isNull() {
        return value == null;
    }

    void print() {
        if (value == null)
            System.out.print("null");
        else
            System.out.print(value);
    }
}
