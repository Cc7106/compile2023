package IR.Value;

import GrammarTypes.AddExp;
import IR.Type.LabelType;
import IR.Type.Type;
import IR.Value.Instructions.Instruction;

import java.util.ArrayList;

public class BasicBlock extends Value {
    private ArrayList<Instruction> instructionsList;

    public BasicBlock(Function curFunction) {
        //block -> labelType
        super("%_"+REG_NUM_COUNT++, new LabelType());
        this.instructionsList = new ArrayList<>();
    }

    public ArrayList<Instruction> getInstructionsList() {
        return instructionsList;
    }

    public void addInstructionsList(Instruction instruction) {
        this.instructionsList.add(instruction);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getName().replace("%","") + ":\n");
        for (Instruction inst: instructionsList) {
            sb.append("\t" + inst + "\n");
        }
        return sb.toString();
    }
}
