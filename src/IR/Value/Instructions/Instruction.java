package IR.Value.Instructions;

import IR.Type.Type;
import IR.Value.BasicBlock;
import IR.Value.User;
import IR.Value.Value;

public class Instruction extends User {
    public Instruction(String name, Type type) {
        super(name, type);
    }
}

