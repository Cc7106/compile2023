package IR.Value;

import IR.Type.Type;

import java.util.ArrayList;

public class User extends Value {

    private ArrayList<Value> operands = new ArrayList<>();

    public User(String name, Type type) {
        super(name, type);
        this.operands = new ArrayList<>();
    }

    public Value getOperand(int index) {
        return operands.get(index);
    }

    public void addOperand(Value value) {
        this.operands.add(value);
    }

    public ArrayList<Value> getOperands() {
        return operands;
    }

    public Boolean operandEmpty() {
        return this.operands.isEmpty();
    }
}
