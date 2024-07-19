package IR;

import GrammarTypes.*;
import GrammarTypes.Number;
import IR.Type.*;
import IR.Value.*;
import IR.Value.Instructions.Instruction;
import IR.Value.Instructions.RetInst;
import Token.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Visitor {
    private IRModule irModule;
    private IRBuildFactory buildFactory;
    private ArrayList<HashMap<String, Value>> symbolValMap;
    private int curSymbolValMapIndex = 0; //0->全局变量
    private Function curFunction;
    private BasicBlock curBlock;

    private Value curValue;
    private int curConst;
    private TokenType curOp;
    private Type curType;


    /*--------定义函数用--------*/
    private FunctionType curFuncType;
    private String curIdent;
    private Type curParamType;
    private ArrayList<Type> argsList = new ArrayList<>(); //记录函数参数的类型
    private ArrayList<Value> argsValueList = new ArrayList<>();
    private ArrayList<String> argsIdentList = new ArrayList<>();
    /*------------------------*/


    /*--------定义数组用--------*/
    private ArrayList<Value> arrayElements = new ArrayList<>(); //记录数组里的元素 （constInt 或 constArr）的形式

    private ArrayType arrayType2 = null; //二维数组中，{{},{},{}} 里面一层的数组类型

    private Boolean isInArray = false;

    /*------------------------*/


    /*--------- 分支语块----------*/
    private BasicBlock curTrueBlock = null;
    private BasicBlock curFalseBlock = null;
    private BasicBlock curFinalBlock = null;
    private ArrayList<BasicBlock> trueBlockStack = new ArrayList<>();
    private ArrayList<BasicBlock> falseBlockStack = new ArrayList<>();
    private ArrayList<BasicBlock> finalBlockStack = new ArrayList<>();
    private ArrayList<BasicBlock> curBlockStack = new ArrayList<>();
    private ArrayList<BasicBlock> forTrueBlockStack = new ArrayList<>();
    private ArrayList<BasicBlock> forEndBlockStack = new ArrayList<>();
    private ArrayList<BasicBlock> forCondBlockStack = new ArrayList<>();
    private ArrayList<BasicBlock> forStmt2BlockStack = new ArrayList<>();

    /*-----------------------*/

    private Boolean isAllConstant = false;
    private Boolean isGlobal = false;

    public Visitor() {
        this.buildFactory = new IRBuildFactory();
        this.symbolValMap = new ArrayList<HashMap<String, Value>>(){{add(new HashMap<>());
        }};
        this.curFunction = null;
        this.curBlock = null;
        this.curValue = null;
        this.curConst = 0;
        this.curOp = null;
        this.curType = null;
        this.irModule = new IRModule();
    }

    public IRModule getIrModule() {
        return irModule;
    }

    private void addSymbol(String name, Value value) {
        this.symbolValMap.get(curSymbolValMapIndex).put(name, value);
    }

    private Value getValueFromSymbolMap(String tokenName) {
        for (int i = curSymbolValMapIndex; i >= 0; i--) {
            HashMap<String, Value> valMap = this.symbolValMap.get(i);
            if (valMap.containsKey(tokenName)) {
                return valMap.get(tokenName);
            }
        }
        return null;
    }

    private void addNewBlock() {
        curSymbolValMapIndex++;
        this.symbolValMap.add(curSymbolValMapIndex, new HashMap<String, Value>());
    }

    private void quitBlock() {
        this.symbolValMap.remove(curSymbolValMapIndex);
        curSymbolValMapIndex--;
    }

    private BasicBlock popBlock(ArrayList<BasicBlock> blockStack) {
        return blockStack.remove(blockStack.size() - 1);
    }

    private void setInitFunction() {
        //声明 IO 函数

        //declare i32 @getint()          ; 读取一个整数
        argsList.clear();
        curFuncType = new FunctionType(argsList, new IntegerType(32));
        irModule.addFunction(buildFactory.buildFunction(curFuncType, "getint", true));

        //declare void @putint(i32)      ; 输出一个整数
        argsList.clear();
        argsList.add(new IntegerType(32));
        curFuncType = new FunctionType(argsList, new VoidType());
        irModule.addFunction(buildFactory.buildFunction(curFuncType, "putint", true));

        //declare void @putch(i32)       ; 输出一个字符
        argsList.clear();
        argsList.add(new IntegerType(32));
        curFuncType = new FunctionType(argsList, new VoidType());
        irModule.addFunction(buildFactory.buildFunction(curFuncType, "putch", true));

        //declare void @putstr(i8*)     ; 输出字符串
        argsList.clear();
        argsList.add(new PointerType(new IntegerType(8)));
        curFuncType = new FunctionType(argsList, new VoidType());
        irModule.addFunction(buildFactory.buildFunction(curFuncType, "putstr", true));
    }

    public void visitCompUnit(CompUnit compUnit) {
        //CompUnit -> {Decl} {FuncDef} MainFuncDef

        setInitFunction();

        ArrayList<Decl> declList = compUnit.getDeclList();
        ArrayList<FuncDef> funcDefsList = compUnit.getFuncDefList();
        MainFuncDef mainFuncDef = compUnit.getMainFuncDef();

        isGlobal = true;
        for (Decl decl: declList) {
            visitDecl(decl);
        }
        for (FuncDef funcDef: funcDefsList) {
            visitFuncDef(funcDef);
        }
        visitMainFuncDef(mainFuncDef);
    }

    private void visitMainFuncDef(MainFuncDef mainFuncDef) {
        //MainFuncDef -> 'int' 'main' '(' ')' Block
        isGlobal = false;

        //build function
        Type returnType = new IntegerType(32);
        argsList = new ArrayList<>();
        FunctionType functionType = buildFactory.buildFunctionType(returnType, argsList);
        Function function = buildFactory.buildFunction(functionType, "main",false); //false->不是提供的函数
        irModule.addFunction(function);
        curFunction = function;

        //build basic block
        curBlock = buildFactory.buildBasicBlock(function);
        curBlockStack.add(curBlock);

        addNewBlock();
        visitBlock(mainFuncDef.getBlock());
        curBlockStack.remove(curBlockStack.size() - 1);
    }



    private void visitFuncDef(FuncDef funcDef) {
        //先声明函数，函数名字 返回值 参数类型 再加入符号表
        //接着继续新的block做代码生成，这时参数还需要再遍历一次，加入新符号表
        //FuncDef -> FuncType Ident '(' [FuncFParams] ')' Block
        FuncType funcType = funcDef.getFuncType();
        String functionName = funcDef.getIdent().getToken();
        FuncFParams funcFParams = funcDef.getFuncFParams();

        argsList.clear();
        argsIdentList.clear();
        if (funcFParams != null) {
            for (FuncFParam funcFParam: funcFParams.getFuncFParamList()) {
                visitFuncFParam(funcFParam);
                argsList.add(curParamType);
                argsIdentList.add(curIdent);
            }
        }

        if (funcType.getFuncTypeToken().getTokenType().equals(TokenType.VOIDTK)) {
            curFuncType = buildFactory.buildFunctionType(new VoidType(), argsList);
        } else {
            curFuncType = buildFactory.buildFunctionType( new IntegerType(32), argsList);
        }

        Function function = buildFactory.buildFunction(curFuncType, functionName, false);
        irModule.addFunction(function);
        curFunction = function;
        isGlobal = false;
        BasicBlock basicBlock = buildFactory.buildBasicBlock(curFunction);
        curBlock = basicBlock;
        curBlockStack.add(curBlock);

        /*
        参数 (int a, int b)
        int c = a;
        int d = b;
         */
        addNewBlock();
        argsValueList.clear();
        argsValueList.addAll(curFunction.getArgumentsList());
        for (int i = 0; i < argsValueList.size(); i++) {
            curValue = buildFactory.buildVar(curBlock, argsValueList.get(i), argsValueList.get(i).getType());
            addSymbol(argsIdentList.get(i), curValue);
        }
        visitBlock(funcDef.getBlock());


        //检查是否有返回指令
        //BasicBlock block = function.getBlock(0);
        curBlock = popBlock(curBlockStack);
        int instNum = curBlock.getInstructionsList().size();
        if (instNum != 0) {
            Instruction lastInst = curBlock.getInstructionsList().get(instNum - 1);
            if (!(lastInst instanceof RetInst)) {
                buildFactory.buildRet(curBlock, null);
            }
        } else {
            buildFactory.buildRet(curBlock,null);
        }

        isGlobal = true;
    }

    private void visitFuncFParam(FuncFParam funcFParam) {
        // FuncFParam → BType Ident
        // FuncFParam → BType Ident '[' ']'
        // FuncFParam → BType Ident '[' ']''[' ConstExp ']'

        int dimension = funcFParam.getDimension();
        if (dimension == 0) {
            // i32
            curParamType = new IntegerType(32);
        } else if (dimension == 1) {
            // i32*
            curParamType = new PointerType(new IntegerType(32));
        } else {
            // [size * i32]*
            isAllConstant = true;
            visitConstExp(funcFParam.getConstExp());
            int dimSize = curConst;
            isAllConstant = false;
            curParamType = new PointerType(new ArrayType(new IntegerType(32), dimSize));
        }
        curIdent = funcFParam.getIdent().getToken();
    }

    private void visitDecl(Decl decl) {
        // Decl → ConstDecl
        // Decl → VarDecl
        if (decl.getConstDecl() != null) {
            visitConstDecl(decl.getConstDecl());
        } else {
            visitVarDecl(decl.getVarDecl());
        }
    }

    private void visitVarDecl(VarDecl varDecl) {
        //VarDecl → BType VarDef { ',' VarDef } ';'
        curType = new IntegerType(32);

        ArrayList<VarDef> varDefsList = varDecl.getVarDef();
        for (VarDef varDef: varDefsList) {
            visitVarDef(varDef);
        }
    }

    private void visitConstDecl(ConstDecl constDecl) {
        //ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'
        //btype = int
        curType = new IntegerType(32);

        ArrayList<ConstDef> constDefsList = constDecl.getConstDef();
        for (ConstDef constDef: constDefsList) {
            visitConstDef(constDef);
        }
    }

    private void visitVarDef(VarDef varDef) {
        //VarDef → Ident { '[' ConstExp ']' }
        // |  Ident { '[' ConstExp ']' } '=' InitVal
        int dimension = varDef.getDimension();
        String ident = varDef.getIdent().getToken();
        if (dimension == 0) {
            //不是数组
            if (isGlobal) {
                //全局变量
                if (varDef.getInitVal() != null) {
                    // 有初始值
                    isAllConstant = true;
                    visitInitVal(varDef.getInitVal());
                    curValue = buildFactory.buildConstInt(curConst, 32);
                    isAllConstant = false;
                } else {
                    // 没有初始值，= 0
                    curValue = buildFactory.buildConstInt(0, 32);
                }
                curValue = buildFactory.buildGlobalVar(ident, curValue,false);
                this.irModule.addGlobalVar((GlobalVar) curValue);
                addSymbol(ident, curValue);

            } else {
                if (varDef.getInitVal() != null) {
                    // 有初始值 -> alloca & store
                    visitInitVal(varDef.getInitVal());
                } else {
                    // 没有初始值 -> alloca
                    curValue = null;
                }
                if (isInArray) {
                    curValue = buildFactory.buildVar(curBlock, curValue, arrayType2);
                } else {
                    curValue = buildFactory.buildVar(curBlock, curValue, curType);
                }
               addSymbol(ident, curValue);
            }

        } else {
            //数组
            // Ident { '[' ConstExp ']' } '=' InitVal
            isInArray = true;
            int dimSize1, dimSize2 = 0;
            ArrayType arrayType;
            arrayElements = new ArrayList<>();
            arrayType2 = null;

            //一维数组
            isAllConstant = true;
            visitConstExp(varDef.getConstExp1());
            dimSize1 = curConst;
            isAllConstant = false;

            //二维数组
            if (dimension == 2) {
                isAllConstant = true;
                visitConstExp(varDef.getConstExp2());
                dimSize2 = curConst;
                isAllConstant = false;

                //二维数组的创建arrayType
                arrayType2 = buildFactory.buildArrayType(new IntegerType(32), dimSize2);
                arrayType = buildFactory.buildArrayType(arrayType2, dimSize1);
            } else {
                //一维数组的创建arrayType
                arrayType = buildFactory.buildArrayType(new IntegerType(32), dimSize1);
            }

            if (isGlobal) {
                //全局变量
                if (varDef.getInitVal() != null) {
                    //有初始值
                    isAllConstant = true;
                    visitInitVal(varDef.getInitVal());
                    curValue = buildFactory.buildConstArr(arrayType, arrayElements);
                    isAllConstant = false;
                } else {
                    //没有初始值，zeroInitializer
                    curValue = buildFactory.buildConstArr(arrayType, null);
                }
                curValue = buildFactory.buildGlobalVar(ident, curValue, false);
                this.irModule.addGlobalVar((GlobalVar) curValue);
                addSymbol(ident, curValue);
            } else {
                if (varDef.getInitVal() != null) {
                    // 有初始值 -> alloca & store
                    visitInitVal(varDef.getInitVal());
                    //curValue = buildFactory.buildConstArr(arrayType, arrayElements);
                } else {
                    // 没有初始值 -> alloca
                    arrayElements = null;
                }
                curValue = buildFactory.buildArrVar(curBlock, arrayType, arrayElements,
                        dimension, dimSize1, dimSize2);
                addSymbol(ident, curValue);
            }
            isInArray = false;
        }
    }

    private void visitConstDef(ConstDef constDef) {
        // ConstDef -> Ident { '[' ConstExp ']' } '=' ConstInitVal
        // 全局变量：constInitVal 只会是常数表达式
        int dimension = constDef.getDimension();
        String ident = constDef.getIdent().getToken();
        if (dimension == 0) {
            //不是数组
            if (isGlobal) {
                //全局变量
                isAllConstant = true;
                visitConstInitVal(constDef.getConstInitVal());
                curValue = buildFactory.buildConstInt(curConst, 32);
                isAllConstant = false;

                curValue = buildFactory.buildGlobalVar(ident, curValue, true);
                this.irModule.addGlobalVar((GlobalVar) curValue);
                addSymbol(ident, curValue);
            } else {
                visitConstInitVal(constDef.getConstInitVal());
                if (isInArray) {
                    curValue = buildFactory.buildVar(curBlock, curValue, arrayType2);
                } else {
                    curValue = buildFactory.buildVar(curBlock, curValue, curType);
                }
                addSymbol(ident, curValue);
            }

        } else {
            //数组
            //-------
            // ConstDef -> Ident { '[' ConstExp ']' } '=' ConstInitVal
            isInArray = true;
            int dimSize1, dimSize2 = 0;
            ArrayType arrayType;
            arrayElements = new ArrayList<>();
            arrayType2 = null;


            //一维数组
            isAllConstant = true;
            visitConstExp(constDef.getConstExp1());
            dimSize1 = curConst;
            isAllConstant = false;

            //二维数组
            if (dimension == 2) {
                isAllConstant = true;
                visitConstExp(constDef.getConstExp2());
                dimSize2 = curConst;
                isAllConstant = false;

                arrayType2 = buildFactory.buildArrayType(new IntegerType(32), dimSize2);
                arrayType = buildFactory.buildArrayType(arrayType2, dimSize1);
            } else {
                arrayType = buildFactory.buildArrayType(new IntegerType(32), dimSize1);
            }


            if (isGlobal) {
                //全局变量
                isAllConstant = true;
                visitConstInitVal(constDef.getConstInitVal());
                curValue = buildFactory.buildConstArr(arrayType, arrayElements);
                isAllConstant = false;

                curValue = buildFactory.buildGlobalVar(ident, curValue, true);
                this.irModule.addGlobalVar((GlobalVar) curValue);
                addSymbol(ident, curValue);
            } else {
                //局部变量------------
                visitConstInitVal(constDef.getConstInitVal());
                curValue = buildFactory.buildArrVar(curBlock, arrayType, arrayElements,
                        dimension,dimSize1,dimSize2);
                addSymbol(ident, curValue);
            }
            isInArray = false;
        }
    }

    private void visitInitVal(InitVal initVal) {
        // InitVal → Exp | '{' [ InitVal { ',' InitVal } ] '}'
        if (initVal.getExp() != null) {
            visitExp(initVal.getExp());
        } else {
            // 数组
            //-----
            // {' [ InitVal { ',' InitVal } ] '}'
            ArrayList<Value> array = new ArrayList<>();
            for (InitVal initVal1: initVal.getInitValList()) {
                if (initVal1.getExp() != null) {
                    //一个元素罢了 不是数组
                    visitInitVal(initVal1);
                    if (isAllConstant) {
                        //转换成value先
                        curValue = buildFactory.buildConstInt(curConst, 32);
                    }
                    array.add(curValue);
                } else {
                    //二维数组中的 一行数组元素
                    visitInitVal(initVal1);
                    ConstArr constArr = buildFactory.buildConstArr(arrayType2, arrayElements);
                    array.add(constArr);
                }
            }
            arrayElements = array;
        }
    }

    private void visitConstInitVal(ConstInitVal constInitVal) {
        //ConstInitVal → ConstExp
        //    | '{' [ ConstInitVal { ',' ConstInitVal } ] '}'
        if (constInitVal.getConstExp() != null) {
            visitConstExp(constInitVal.getConstExp());
        } else {
            //数组初始化
            //-----
            ArrayList<Value> array = new ArrayList<>();
            for (ConstInitVal constInitVal1: constInitVal.getConstInitValList()) {
                if (constInitVal1.getConstExp() != null) {
                    //一个元素罢了 不是数组
                    visitConstInitVal(constInitVal1);
                    if (isAllConstant) {
                        //转换成value先
                        curValue = buildFactory.buildConstInt(curConst, 32);
                    }
                    array.add(curValue);
                } else {
                    //二维数组中的 一行数组元素
                    visitConstInitVal(constInitVal1);
                    ConstArr constArr = buildFactory.buildConstArr(arrayType2, arrayElements);
                    array.add(constArr);
                }
            }
            arrayElements = array;
        }
    }

    private void visitBlock(Block block) {
        //Block -> '{' { BlockItem } '}'
        ArrayList<BlockItem> blockItemsList = block.getBlockItemsList();
        for (BlockItem blockItem: blockItemsList) {
            visitBlockItem(blockItem);
        }
        quitBlock();
    }

    private void visitBlockItem(BlockItem blockItem) {
        // BlockItem -> Decl | Stmt
        if (blockItem.getStmt() != null) {
            visitStmt(blockItem.getStmt());
        } else {
            visitDecl(blockItem.getDecl());
        }
    }

    private void visitStmt(Stmt stmt) {
        if (stmt.getIfTk() != null) {
            visitIfStmt(stmt);
        } else if (stmt.getForTk() != null) {
            visitForStmt(stmt);
        } else if (stmt.getBreakTk() != null) {
            visitBreakStmt(stmt);
        } else if (stmt.getContinueTk() != null) {
            visitContinueStmt(stmt);
        } else if (stmt.getReturnTk() != null) {
            visitReturnStmt(stmt);
        } else if (stmt.getPrintfTk() != null) {
            visitPrintfStmt(stmt);
        } else if (stmt.getBlock() != null) {
            addNewBlock();
            visitBlock(stmt.getBlock());
        } else if (stmt.getlVal() != null) {
            //LVal '=' Exp ';'
            //LVal '=' 'getint' '(' ')' ';'
            visitAssignStmt(stmt);
        } else {
            //[Exp] ';'
            if (stmt.getExp() != null) {
                visitExp(stmt.getExp());
            }
        }
    }

    private void visitAssignStmt(Stmt stmt) {
        LVal lVal = stmt.getlVal();

        //把lval的位置找到
        visitLValForAssign(lVal);
        Value valueLVal = curValue;

        if (stmt.getGetIntTk() != null) {
            //LVal '=' 'getint' '(' ')' ';'
            Function usedFunction = irModule.getFunctionByName("getint");
            ArrayList<Value> rParamsList = new ArrayList<>();
            curValue = buildFactory.buildCallInst(curBlock, usedFunction, rParamsList, false);
            Value valueGetInt = curValue;

            //赋值 store getint->lval
            buildFactory.buildStore(curBlock, valueGetInt, valueLVal);

        } else {
            //LVal '=' Exp ';'
            visitExp(stmt.getExp());
            Value valueExp = curValue;

            //赋值 store exp->lval
            buildFactory.buildStore(curBlock, valueExp, valueLVal);
        }
    }


    private void visitLValForAssign(LVal lVal) {
        //LVal '='
        /* 赋值过程
            普通变量 -> store
            数组 -> gep , store
        */

        int lValDimension = lVal.getDimension();
        Value dimIndex1, dimIndex2;
        Value valueLVal;

        //把lval的位置找到
        String lvalIdent = lVal.getIdent().getToken();
        curValue = getValueFromSymbolMap(lvalIdent);
        valueLVal = curValue;

        if (lValDimension != 0) {

            visitExp(lVal.getExp1());
            dimIndex1 = curValue;


            ArrayList<Value> Indices = new ArrayList<>(){{
                add(new ConstInt(new IntegerType(32), 0));
                add(dimIndex1);
            }};

            if (lValDimension == 2) {
                visitExp(lVal.getExp2());
                dimIndex2 = curValue;
                Indices.add(dimIndex2);
            }

            //-----
            if (valueLVal.getType().getElementType() instanceof PointerType) {
                curValue = buildFactory.buildLoadInst(curBlock, valueLVal);
                valueLVal = curValue;
                Indices.remove(0);
            }
            //-----

            curValue = buildFactory.buildGEPInst(curBlock, valueLVal, Indices);
        }
    }

    private int doCalculation(int const1, int const2, TokenType curOp) {
        if (curOp.equals(TokenType.PLUS)) {
            return const1 + const2;
        } else if (curOp.equals(TokenType.MINU)) {
            return const1 - const2;
        } else if (curOp.equals(TokenType.MULT)) {
            return const1 * const2;
        } else if (curOp.equals(TokenType.DIV)) {
            return const1 / const2;
        } else if (curOp.equals(TokenType.MOD)) {
            return const1 % const2;
        } else {
            return 0;
        }
    }

    private void visitConstExp(ConstExp constExp) {
        //ConstExp → AddExp
        visitAddExp(constExp.getAddExp());
    }

    private void visitExp(Exp exp) {
        //Exp → AddExp
        visitAddExp(exp.getAddExp());
    }


    private void visitAddExp(AddExp addExp) {
        // AddExp → MulExp | AddExp ('+' | '−') MulExp

        if (isAllConstant) {
            //都是常量

            if (addExp.getAddExp() == null) {
                // MulExp 
                visitMulExp(addExp.getMulExp());
            } else {
                // AddExp ('+' | '−') MulExp
                visitAddExp(addExp.getAddExp());
                int const1 = curConst;
                visitMulExp(addExp.getMulExp());
                int const2 = curConst;

                curOp = addExp.getOp().getTokenType();
                curConst = doCalculation(const1, const2, curOp);
            }
        } else {
            //不是常数 创建指令 ADD 
            if (addExp.getAddExp() == null) {
                // MulExp 
                visitMulExp(addExp.getMulExp());
            } else {
                // AddExp ('+' | '−') MulExp

                visitAddExp(addExp.getAddExp());
                Value value1 = curValue;
                visitMulExp(addExp.getMulExp());
                Value value2 = curValue;

                curOp = addExp.getOp().getTokenType();
                curValue = buildFactory.buildBinaryInst(curBlock, curOp, value1, value2);
            }
        }
    }




    private void visitMulExp(MulExp mulExp) {
        // 乘除模表达式 MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp

        if (isAllConstant) {
            //都是常量
            if (mulExp.getMulExp() == null) {
                // UnaryExp
                visitUnaryExp(mulExp.getUnaryExp());
            } else {
                // MulExp ('*' | '/' | '%') UnaryExp

                visitMulExp(mulExp.getMulExp());
                int const1 = curConst;
                visitUnaryExp(mulExp.getUnaryExp());
                int const2 = curConst;

                curOp = mulExp.getOp().getTokenType();
                curConst = doCalculation(const1, const2, curOp);
            }
        } else {
            //不是常数 创建指令 MUL
            if (mulExp.getMulExp() == null) {
                // UnaryExp
                visitUnaryExp(mulExp.getUnaryExp());
            } else {
                // MulExp ('*' | '/' | '%') UnaryExp

                visitMulExp(mulExp.getMulExp());
                Value value1 = curValue;
                visitUnaryExp(mulExp.getUnaryExp());
                Value value2 = curValue;

                curOp = mulExp.getOp().getTokenType();
                curValue = buildFactory.buildBinaryInst(curBlock, curOp, value1, value2);
            }
        }
    }

    private void visitUnaryExp(UnaryExp unaryExp) {
        //UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
        if (unaryExp.getPrimaryExp() != null) {
            //PrimaryExp
            visitPrimaryExp(unaryExp.getPrimaryExp());

        } else if (unaryExp.getIdent() != null) {
            //函数调用
            // Ident '(' [FuncRParams] ')'
            Function usedFunction = irModule.getFunctionByName(unaryExp.getIdent().getToken());
            Type returnType = usedFunction.getReturnType();
            Boolean isVoid = returnType instanceof VoidType;
            ArrayList<Value> rParamsList = new ArrayList<>();

            if (unaryExp.getFuncRParams() != null) {
                for (Exp exp: unaryExp.getFuncRParams().getExpsList()) {
                    visitExp(exp);
                    if (isAllConstant) {
                        curValue = buildFactory.buildConstInt(curConst, 32);
                    }
                    rParamsList.add(curValue);
                }
            }
            if (isVoid) {
                buildFactory.buildCallInst(curBlock, usedFunction, rParamsList, isVoid);
            } else {
                curValue = buildFactory.buildCallInst(curBlock, usedFunction, rParamsList, isVoid);

            }
        } else {
            //UnaryOp UnaryExp
            curOp = unaryExp.getUnaryOp().getOp().getTokenType();
            if (curOp.equals(TokenType.PLUS)) {
                // '+'
                visitUnaryExp(unaryExp.getUnaryExp());
            } else if (curOp.equals(TokenType.MINU)) {
                //'-'
                visitUnaryExp(unaryExp.getUnaryExp());
                if (isAllConstant) {
                    curConst = -1 * curConst;
                } else {
                    Value constZero = buildFactory.buildConstInt(0,32);
                    curOp = unaryExp.getUnaryOp().getOp().getTokenType();
                    curValue = buildFactory.buildBinaryInst(curBlock, curOp, constZero, curValue);
                }
            } else {
                // !a -> (a == 0)
                Value constZero = buildFactory.buildConstInt(0,32);
                visitUnaryExp(unaryExp.getUnaryExp());
                curValue = buildFactory.buildIcmpInst(curBlock, TokenType.EQL, curValue, constZero);
            }
        }
    }

    private void visitPrimaryExp(PrimaryExp primaryExp) {
        // PrimaryExp → '(' Exp ')' | LVal | Number
        if (primaryExp.getExp() !=  null) {
            visitExp(primaryExp.getExp());
        } else if (primaryExp.getLVal() != null) {
            visitLVal(primaryExp.getLVal());
        } else {
            visitNumber(primaryExp.getNumber());
        }
    }

    private void visitNumber(Number number) {
        if (isAllConstant) {
            curConst =  Integer.parseInt(number.getIntConst().getToken());
        } else {
            curValue = buildFactory.buildConstInt(Integer.parseInt(number.getIntConst().getToken()), 32);
        }
    }

    private void visitLVal(LVal lVal) {
        // LVal -> Ident {'[' Exp ']'}
        //变量
        int dimension = lVal.getDimension();

        Value valueLVal;
        String ident = lVal.getIdent().getToken();
        curValue = getValueFromSymbolMap(ident);
        valueLVal = curValue;
        // valueLVal -> globalVar / allocaInst
        //           -> 都是pointerType
        //           -> pointerType的targetType -> integer / array / pointer

        if (isAllConstant) {
            int dimIndex1, dimIndex2;
            if (dimension == 0) {
                curConst = curValue.getIntConst();
            } else if (dimension == 1) {
                visitExp(lVal.getExp1());
                dimIndex1 = curConst;
                curValue = valueLVal.getElement(dimIndex1);
                curConst = curValue.getIntConst();
            } else {
                visitExp(lVal.getExp1());
                dimIndex1 = curConst;
                curValue = valueLVal.getElement(dimIndex1);
                valueLVal = curValue;
                visitExp(lVal.getExp2());
                dimIndex2 = curConst;
                curValue = valueLVal.getElement(dimIndex2);
                curConst = curValue.getIntConst();
            }
        } else {
            // valueLVal -> globalVar / allocaInst
            //           -> 都是pointerType
            //           -> pointerType的targetType -> integer / array / pointer
            Value dimIndex1, dimIndex2;
            if (dimension == 0) {
                if (((PointerType) valueLVal.getType()).getTargetType() instanceof ArrayType) {
                    //数组作为传参 （不用load）
                    ArrayList<Value> Indices = new ArrayList<>() {{
                        add(new ConstInt(new IntegerType(32), 0));
                        add(new ConstInt(new IntegerType(32), 0));
                    }};
                    curValue = buildFactory.buildGEPInst(curBlock, valueLVal, Indices);
                } else {
                    curValue = buildFactory.buildLoadInst(curBlock, valueLVal);
                }
            } else {
                if (dimension == 1) {
                    //有可能是 a[1] = 1 / a[1] = {1,2,3} (pointer) / a[1] = 1 (pointer)
                    visitExp(lVal.getExp1());
                    dimIndex1 = curValue;

                    if (valueLVal.getType().getElementType() instanceof PointerType) {
                        //是指针型 需要先load
                        //load
                        curValue = buildFactory.buildLoadInst(curBlock, valueLVal);
                        ArrayList<Value> Indices = new ArrayList<>() {{
                            add(dimIndex1);
                        }};
                        curValue = buildFactory.buildGEPInst(curBlock, curValue, Indices);
                        if (curValue.getType().getElementType() instanceof ArrayType) {
                            //数组指针
                            ArrayList<Value> Indices2 = new ArrayList<>() {{
                                add(new ConstInt(new IntegerType(32), 0));
                                add(new ConstInt(new IntegerType(32), 0));
                            }};
                            curValue = buildFactory.buildGEPInst(curBlock, curValue, Indices2);
                        } else {
                            curValue = buildFactory.buildLoadInst(curBlock, curValue);
                        }
                    } else {
                        ArrayList<Value> Indices = new ArrayList<>() {{
                            add(new ConstInt(new IntegerType(32), 0));
                            add(dimIndex1);
                        }};
                        curValue = buildFactory.buildGEPInst(curBlock, valueLVal, Indices);
                        if (curValue.getType().getElementType() instanceof ArrayType) {
                            Indices.set(1,new ConstInt(new IntegerType(32), 0));
                            curValue = buildFactory.buildGEPInst(curBlock, curValue, Indices);
                        } else {
                            curValue = buildFactory.buildLoadInst(curBlock, curValue);
                        }
                    }
                } else {
                    visitExp(lVal.getExp1());
                    dimIndex1 = curValue;
                    visitExp(lVal.getExp2());
                    dimIndex2 = curValue;

                    if (valueLVal.getType().getElementType() instanceof PointerType) {
                        //是指针型 需要先load
                        //load
                        curValue = buildFactory.buildLoadInst(curBlock, valueLVal);
                        ArrayList<Value> Indices = new ArrayList<>() {{
                            add(dimIndex1);
                            add(dimIndex2);
                        }};
                        curValue = buildFactory.buildGEPInst(curBlock, curValue, Indices);
                        if (curValue.getType().getElementType() instanceof ArrayType) {
                            ArrayList<Value> Indices2 = new ArrayList<>() {{
                                add(new ConstInt(new IntegerType(32), 0));
                                add(new ConstInt(new IntegerType(32), 0));
                            }};
                            curValue = buildFactory.buildGEPInst(curBlock, curValue, Indices2);
                        } else {
                            curValue = buildFactory.buildLoadInst(curBlock, curValue);
                        }
                    } else {
                        ArrayList<Value> Indices = new ArrayList<>() {{
                            add(new ConstInt(new IntegerType(32), 0));
                            add(dimIndex1);
                            add(dimIndex2);
                        }};
                        curValue = buildFactory.buildGEPInst(curBlock, valueLVal, Indices);
                        if (curValue.getType().getElementType() instanceof ArrayType) {
                            Indices.set(1,new ConstInt(new IntegerType(32), 0));
                            curValue = buildFactory.buildGEPInst(curBlock, curValue, Indices);
                        } else {
                            curValue = buildFactory.buildLoadInst(curBlock, curValue);
                        }
                    }

                }
            }
        }
    }



    private void visitPrintfStmt(Stmt stmt) {
        String outputStr = stmt.getStrcon().replace("\"","");
        outputStr = outputStr.replace("\\n", "\n");

        //处理 %d
        ArrayList<Value> outputVarList = new ArrayList<>();
        if (stmt.getExpList() != null) {
            for (Exp exp: stmt.getExpList()) {
                visitExp(exp);
                outputVarList.add(curValue);
            }
        }

        StringBuilder tempValidStr = new StringBuilder();
        for (int i = 0, outputVarIndex = 0; i < outputStr.length(); i++) {
            if (outputStr.charAt(i) == '%' && outputStr.charAt(i+1) == 'd') {
                //把前段字符串切割
                manageConstStrNPrint(tempValidStr.toString());
                doPutInt(outputVarList, outputVarIndex);
                i = i + 1;
                outputVarIndex++;
                tempValidStr = new StringBuilder();
            } else {
                tempValidStr.append(outputStr.charAt(i));
            }
        }

        manageConstStrNPrint(tempValidStr.toString());
    }

    private void doPutInt(ArrayList<Value> outputVarList, int outputVarIndex) {
        curValue = outputVarList.get(outputVarIndex);
        Function putint = irModule.getFunctionByName("putint");
        buildFactory.buildCallInst(curBlock, putint, new ArrayList<>() {
            {
                add(curValue);
            }
        }, true);
    }

    private void manageConstStrNPrint(String validStr) {

        if (validStr.isEmpty()) {
            return;
        }

        //如果只有一个字符
        if (validStr.length() == 1 ) {
            // @putch(i32)
            curValue = buildFactory.buildConstInt(validStr.charAt(0), 32);
            Function putch = irModule.getFunctionByName("putch");
            buildFactory.buildCallInst(curBlock, putch, new ArrayList<>() {
                {
                    add(curValue);
                }
            }, true);

        } else {
            // call @putstr(i8*)

            //先计算字符串长度 ( 终结字符 +1 )
            int length = validStr.length() + 1;

            ConstStr constStr = buildFactory.buildStr(validStr, length);
            curValue = buildFactory.buildGlobalVarStr(constStr, true);
            this.irModule.addGlobalVar((GlobalVar) curValue);

            //基地址，起始index
            ArrayList<Value> Indices = new ArrayList<>() {
                {
                   add(new ConstInt(new IntegerType(32), 0));
                    add(new ConstInt(new IntegerType(32), 0));
                }
            };
            //curValue = buildFactory.buildGEPInstStr(curBlock, curValue, Indices,constStr);
            curValue = buildFactory.buildGEPInst(curBlock, curValue, Indices);
            Function putstr = irModule.getFunctionByName("putstr");
            buildFactory.buildCallInst(curBlock, putstr, new ArrayList<>() {
                {
                    add(curValue);
                }
            }, true);
        }
    }

    private void visitReturnStmt(Stmt stmt) {
        //'return' [Exp] ';'
        if (stmt.getExp() == null) {
            buildFactory.buildRet(curBlock, null);
        } else {
            visitExp(stmt.getExp());
            buildFactory.buildRet(curBlock, curValue);
        }
    }

    private void visitForStmt(Stmt stmt) {
       // 'for' '('[ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt

        BasicBlock forTrueBlock = buildFactory.buildBasicBlock(curFunction);
        BasicBlock forCondBlock = buildFactory.buildBasicBlock(curFunction);
        BasicBlock forEndBlock = buildFactory.buildBasicBlock(curFunction);
        BasicBlock forStmt2Block = buildFactory.buildBasicBlock(curFunction);
        forTrueBlockStack.add(forTrueBlock);
        forCondBlockStack.add(forCondBlock);
        forEndBlockStack.add(forEndBlock);
        forStmt2BlockStack.add(forStmt2Block);

        curBlock = curBlockStack.get(curBlockStack.size() - 1);
        if (stmt.getForStmt1() != null) {
            visitForStmtinForStmt(stmt.getForStmt1());
        }

        //mainBlock: go to -> cond
        curBlock = popBlock(curBlockStack);
        buildFactory.buildBrInstNoCond(curBlock, forCondBlock);

        curBlock = forCondBlock;
        curBlockStack.add(forCondBlock);

        //cond: t->forTrue f->forEnd
        if (stmt.getCond() != null) {
            trueBlockStack.add(forTrueBlock);
            falseBlockStack.add(forEndBlock);
            visitCond(stmt.getCond());
            trueBlockStack.remove(trueBlockStack.size() - 1);
            falseBlockStack.remove(falseBlockStack.size() - 1);
        } else {
            //cond: go to ->forTrue
            buildFactory.buildBrInstNoCond(curBlock, forTrueBlock);
            curBlockStack.remove(curBlockStack.size() - 1);
        }

        //forTrue:
        curBlock = forTrueBlock;
        curBlockStack.add(curBlock);
        visitStmt(stmt.getStmt1());

        //curBlock: go to -> forStmt2
        curBlock = popBlock(curBlockStack);
        forStmt2Block = forStmt2BlockStack.get(forStmt2BlockStack.size() - 1);
        buildFactory.buildBrInstNoCond(curBlock, forStmt2Block);

        //forStmt2: go to -> Cond
        curBlock = forStmt2Block;
        curBlockStack.add(forStmt2Block);
        if (stmt.getForStmt2() != null) {
            visitForStmtinForStmt(stmt.getForStmt2());
        }

        curBlock = popBlock(curBlockStack);
        forCondBlock = forCondBlockStack.get(forCondBlockStack.size() - 1);
        buildFactory.buildBrInstNoCond(curBlock, forCondBlock);

        curBlock = forEndBlockStack.get(forEndBlockStack.size() - 1);
        curBlockStack.add(curBlock);

        forEndBlockStack.remove(forEndBlockStack.size() - 1);
        forCondBlockStack.remove(forCondBlockStack.size() - 1);
        forTrueBlockStack.remove(forTrueBlockStack.size() - 1);
        forStmt2BlockStack.remove(forStmt2BlockStack.size() - 1);
    }

    private void visitForStmtinForStmt(ForStmt forStmt) {
        //ForStmt → LVal '=' Exp
        visitLValForAssign(forStmt.getlVal());
        Value valueLVal = curValue;
        visitExp(forStmt.getExp());
        Value valueExp = curValue;

        buildFactory.buildStore(curBlock, valueExp, valueLVal);

    }


    private void visitBreakStmt(Stmt stmt) {
        BasicBlock forEndBlock = forEndBlockStack.get(forEndBlockStack.size() - 1);
        buildFactory.buildBrInstNoCond(curBlock, forEndBlock);
    }

    private void visitContinueStmt(Stmt stmt) {
        BasicBlock forStmt2Block = forStmt2BlockStack.get(forStmt2BlockStack.size() - 1);
        buildFactory.buildBrInstNoCond(curBlock, forStmt2Block);
    }


    private void visitIfStmt(Stmt stmt) {
        // 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
        BasicBlock ifStmtBlock = buildFactory.buildBasicBlock(curFunction);
        BasicBlock elseStmtBlock = buildFactory.buildBasicBlock(curFunction);
        BasicBlock finalBlock = buildFactory.buildBasicBlock(curFunction);
        trueBlockStack.add(ifStmtBlock);
        falseBlockStack.add(elseStmtBlock);
        finalBlockStack.add(finalBlock);

        //mainblock: +cond
        visitCond(stmt.getCond());

        //trueBlock: +stmt1
        curBlock = popBlock(trueBlockStack);
        curBlockStack.add(curBlock);
        visitStmt(stmt.getStmt1());

        //ifstmtBlockEnd: go to -> finalBlock
        curBlock = popBlock(curBlockStack);
        curFinalBlock = finalBlockStack.get(finalBlockStack.size() - 1);
        buildFactory.buildBrInstNoCond(curBlock, curFinalBlock);

        //elseBlock: +stmt2
        curBlock = popBlock(falseBlockStack);
        curBlockStack.add(curBlock);
        if (stmt.getElseTk() != null) {
            visitStmt(stmt.getStmt2());
        }

        //elseBlockEnd: go to ->finalBlock
        curBlock = popBlock(curBlockStack);
        curFinalBlock = finalBlockStack.get(finalBlockStack.size() - 1);
        buildFactory.buildBrInstNoCond(curBlock, curFinalBlock);

        //curBlock -> finalBlock
        curBlock = popBlock(finalBlockStack);
        curBlockStack.add(curBlock);
    }

    private void visitCond(Cond cond) {
        //Cond → LOrExp 
        visitLOrExp(cond.getlOrExp());
    }

    private void visitLOrExp(LOrExp lOrExp) {
        //LOrExp → LAndExp | LOrExp '||' LAndExp

        if (lOrExp.getlOrExp() == null) {
            visitLAndExp(lOrExp.getlAndExp());
        } else {
            //短路求值
            //下一个条件变falseBlock 当前条件若=true可以直接跳到true了 不需要执行下一个条件判断
            BasicBlock nextCondBlock = buildFactory.buildBasicBlock(curFunction);
            falseBlockStack.add(nextCondBlock);
            visitLOrExp(lOrExp.getlOrExp());


            //curBlock = falseBlockStack.get(falseBlockStack.size() - 1);
            //falseBlockStack.remove(falseBlockStack.size() - 1);
            curBlock = popBlock(falseBlockStack);
            curBlockStack.add(curBlock);
            visitLAndExp(lOrExp.getlAndExp());
        }
    }

    private void visitLAndExp(LAndExp lAndExp) {
        //LAndExp -> EqExp | LAndExp '&&' EqExp
        if (lAndExp.getlAndExp() == null) {
            curBlock = curBlockStack.get(curBlockStack.size()-1);
            visitEqExp(lAndExp.getEqExp());
            Value cond = curValue;

            curBlock = popBlock(curBlockStack);
            curTrueBlock = trueBlockStack.get(trueBlockStack.size() - 1);
            curFalseBlock = falseBlockStack.get(falseBlockStack.size() - 1);
            buildFactory.buildBrInstWithCond(curBlock, cond, curTrueBlock, curFalseBlock);
        } else {
            //下一个条件变trueBlock 当前条件若=false可以直接跳到false了 不需要执行下一个条件判断
            BasicBlock nextCondBlock = buildFactory.buildBasicBlock(curFunction);
            trueBlockStack.add(nextCondBlock);
            visitLAndExp(lAndExp.getlAndExp());

            curBlock = popBlock(trueBlockStack);
            curBlockStack.add(curBlock);

            visitEqExp(lAndExp.getEqExp());
            Value cond = curValue;

            curBlock = popBlock(curBlockStack);
            curTrueBlock = trueBlockStack.get(trueBlockStack.size() - 1);
            curFalseBlock = falseBlockStack.get(falseBlockStack.size() - 1);
            buildFactory.buildBrInstWithCond(curBlock, cond, curTrueBlock, curFalseBlock);
        }
    }

    private void visitEqExp(EqExp eqExp) {
        //EqExp -> RelExp |   EqExp ('==' | '!=') RelExp
        if (eqExp.getEqExp() == null) {
            visitRelExp(eqExp.getRelExp());
        } else {
            visitEqExp(eqExp.getEqExp());
            Value exp1 = curValue;
            visitRelExp(eqExp.getRelExp());
            Value exp2 = curValue;

            curOp = eqExp.getRelation().getTokenType();
            curValue = buildFactory.buildIcmpInst(curBlock, curOp, exp1, exp2);
        }
    }

    private void visitRelExp(RelExp relExp) {
        //RelExp -> AddExp |RelExp ('<' | '>' | '<=' | '>=') AddExp
        if (relExp.getRelExp() == null) {
            visitAddExp(relExp.getAddExp());
        } else {
            visitRelExp(relExp.getRelExp());
            Value exp1 = curValue;
            visitAddExp(relExp.getAddExp());
            Value exp2 = curValue;

            curOp = relExp.getRelation().getTokenType();
            curValue = buildFactory.buildIcmpInst(curBlock, curOp, exp1, exp2);
        }
    }


}
