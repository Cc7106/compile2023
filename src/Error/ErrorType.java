package Error;

public enum ErrorType {
    a, //formatString有非法字符
    b, //名字重定义
    c, //未定义名字
    d, //参数个数与函数不匹配
    e, //参数类型不匹配
    f, //无返回值函数return语句的错
    g,  //有返回值缺少return
    h, //对const进行修改
    i, //缺少；
    j, //缺少）
    k, // 缺少]
    l, //printf格式字符串与表达式个数不匹配
    m //非循环语块出现break continue
}
