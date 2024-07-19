package IR;

import IR.Type.*;
import IR.Value.*;
import IR.Value.Instructions.*;
import IR.Value.Instructions.BinaryInst.*;
import Token.TokenType;

import java.util.ArrayList;

public class IRBuildFactory {
    public IRBuildFactory() {
    }

    public Function buildFunction(FunctionType functionType, String functionName, Boolean isLibrary) {
        return new Function(functionName, functionType, isLibrary);
    }

    public GlobalVar buildGlobalVar(String ident, Value curValue, Boolean isConst) {
        return new GlobalVar(ident, curValue, isConst);
    }

    public GlobalVar buildGlobalVarStr(Value curValue, Boolean isVoid) {
        return new GlobalVar(curValue, isVoid);
    }

    public ConstStr buildStr(String content, int length) {
        return new ConstStr(content, length);
    }


    public FunctionType buildFunctionType(Type returnType, ArrayList<Type> argsList) {
        return new FunctionType(argsList, returnType);
    }

    public ArrayType buildArrayType(Type elementType, int length) {
        return new ArrayType(elementType, length);
    }

    public ConstArr buildConstArr(ArrayType arrayType, ArrayList<Value> elements) {
        return new ConstArr(arrayType, elements);
    }

    public BasicBlock buildBasicBlock(Function function) {
        BasicBlock basicBlock = new BasicBlock(function);
        function.addBasicBlock(basicBlock);
        return basicBlock;
    }

    public StoreInst buildStore(BasicBlock curBlock, Value value, Value storeLocation) {
        StoreInst inst = new StoreInst(value, storeLocation);
        curBlock.addInstructionsList(inst);
        return inst;
    }

    public RetInst buildRet(BasicBlock curBlock, Value value) {
        RetInst inst;
        if (value == null) {
            inst = new RetInst();
        } else {
            inst = new RetInst(curBlock, value);
        }
        curBlock.addInstructionsList(inst);
        return inst;
    }

    public BinaryInst buildBinaryInst(BasicBlock curBlock, TokenType curOp, Value value1, Value value2) {

        BinaryInst inst = null;
        if (curOp.equals(TokenType.PLUS)) {
            inst = new Add(value1, value2);
        } else if (curOp.equals(TokenType.MINU)) {
            inst = new Sub(value1, value2);
        } else if (curOp.equals(TokenType.MULT)) {
            inst = new Mul(value1, value2);
        } else if (curOp.equals(TokenType.DIV)) {
            inst = new Sdiv(value1, value2);
        } else if (curOp.equals(TokenType.MOD)) {
            inst = new Srem(value1, value2);
        }
        curBlock.addInstructionsList(inst);
        return inst;
    }

    public BinaryInst buildIcmpInst(BasicBlock curBlock, TokenType curOp, Value value1, Value value2) {
        //icmp的结果是i1类型，两个操作数为i32类型
        if (!((IntegerType)value1.getType()).isInt32()) {
            value1 = buildZextInst(curBlock, value1, new IntegerType(32));
        }
        if (!((IntegerType)value2.getType()).isInt32()) {
            value2 = buildZextInst(curBlock, value2, new IntegerType(32));
        }
        BinaryInst inst = null;
        if (curOp.equals(TokenType.EQL)) {
            inst = new Eq(value1, value2);
        } else if (curOp.equals(TokenType.NEQ)) {
            inst = new Ne(value1, value2);
        } else if (curOp.equals(TokenType.LSS)) {
            inst = new Slt(value1, value2);
        } else if (curOp.equals(TokenType.LEQ)) {
            inst = new Sle(value1, value2);
        } else if (curOp.equals(TokenType.GRE)) {
            inst = new Sgt(value1, value2);
        } else if (curOp.equals(TokenType.GEQ)) {
            inst = new Sge(value1, value2);
        }
        curBlock.addInstructionsList(inst);
        return inst;
    }

    public Value buildConstInt(int constValue, int bits) {
        return new ConstInt(new IntegerType(bits), constValue);
    }


    public Value buildVar(BasicBlock curBlock, Value curValue, Type varType) {
        //
        // int a = b;
        // %1 = alloca i32
        // store i32 %b, i32* %1
        AllocaInst allocaInst = new AllocaInst(varType);
        curBlock.addInstructionsList(allocaInst);
        if (curValue != null) {
            StoreInst storeInst = new StoreInst(curValue, allocaInst);
            curBlock.addInstructionsList(storeInst);
        }

        return allocaInst;
    }

    public Value buildArrVar(BasicBlock curBlock, Type arrayType, ArrayList<Value> arrayElements,
                             int dimension, int dimSize1, int dimSize2) {
        // alloca
        // {gep, store}
        AllocaInst allocaInst = new AllocaInst(arrayType);
        curBlock.addInstructionsList(allocaInst);

        if (arrayElements != null) {
            //build gep n store
            GEPInst gepInst;
            StoreInst storeInst;
            Value arrElement1, arrElement2; //arrElement1 -> 第一层数组，arrElement2 -> 第二层数组

            ArrayList<Value> Indices = new ArrayList<>();
            //1->基地址 2->一维数组下标 3->二维数组下标
            //a[2][1] = i32 0, i32 2, i32 1
            Indices.add(new ConstInt(new IntegerType(32), 0));
            for (int i = 0; i < dimSize1; i++) {
                arrElement1 = arrayElements.get(i);
                ConstInt constInt1 = new ConstInt(new IntegerType(32), i);
                Indices.add(constInt1);
                if (dimension == 2) {
                    for(int j = 0; j < dimSize2; j++) {
                        arrElement2 = ((ConstArr)arrElement1).getElement(j);
                        ConstInt constInt2 = new ConstInt(new IntegerType(32), j);
                        Indices.add(constInt2);
                        gepInst = new GEPInst(allocaInst, Indices);
                        curBlock.addInstructionsList(gepInst);
                        storeInst = new StoreInst(arrElement2 ,gepInst);
                        curBlock.addInstructionsList(storeInst);
                        Indices.remove(2);
                    }
                } else {
                    gepInst = new GEPInst(allocaInst, Indices);
                    curBlock.addInstructionsList(gepInst);
                    storeInst = new StoreInst(arrElement1, gepInst);
                    curBlock.addInstructionsList(storeInst);
                }
                Indices.remove(1);

            }
        }

        return allocaInst;
    }

    public Value buildLoadInst(BasicBlock curBlock, Value curValue) {
        // %6 = load i32, i32* %4
        LoadInst inst = new LoadInst(curValue);
        curBlock.addInstructionsList(inst);
        return inst;

    }

    public Value buildCallInst(BasicBlock curBlock, Function function, ArrayList<Value> rParams, Boolean isVoid) {
        //调用函数
        CallInst inst = new CallInst(function, rParams, isVoid);
        curBlock.addInstructionsList(inst);
        return inst;
    }

    public Value buildGEPInstStr(BasicBlock curBlock, Value curValue, ArrayList<Value> Indices, ConstStr constStr) {
        //GEPInst inst = new GEPInst(curValue, Indices, constStr);
        GEPInst inst = new GEPInst(curValue, Indices);
        curBlock.addInstructionsList(inst);
        return inst;
    }

    public Value buildGEPInst(BasicBlock curBlock, Value curArr, ArrayList<Value> Indices) {
        GEPInst inst = new GEPInst(curArr, Indices);
        curBlock.addInstructionsList(inst);
        return inst;
    }

    public Value buildBrInstNoCond(BasicBlock curBlock, BasicBlock toBlock) {
        BrInst inst = new BrInst(toBlock);
        curBlock.addInstructionsList(inst);
        return inst;
    }

    public Value buildBrInstWithCond(BasicBlock curBlock, Value cond, BasicBlock trueBlock, BasicBlock falseBlock) {
        if (((IntegerType)cond.getType()).isInt32()) {
            //是i32,  ==0 -> false
            cond = buildIcmpInst(curBlock,TokenType.NEQ,cond,new ConstInt(new IntegerType(32),0));
        }

        BrInst inst = new BrInst(cond, trueBlock, falseBlock);
        curBlock.addInstructionsList(inst);
        return inst;
    }

    public Value buildZextInst(BasicBlock curBlock, Value oriValue, Type targetType) {
        Zext inst = new Zext(oriValue, targetType);
        curBlock.addInstructionsList(inst);
        return inst;
    }



}
