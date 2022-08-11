package functions.meta;

import functions.Function;

public class Mult implements Function {

    private double leftX, rightX;
    private Function fun1, fun2;

    public Mult(Function fun1, Function fun2){
        if(fun2.getRightDomainBorder() < fun1.getLeftDomainBorder() ||
                fun1.getRightDomainBorder() < fun2.getLeftDomainBorder()) {
            leftX = rightX = Double.NaN;
            return;
        }

        if(fun1.getLeftDomainBorder() < fun2.getLeftDomainBorder()) leftX = fun2.getLeftDomainBorder();
        else leftX = fun1.getLeftDomainBorder();

        if(fun1.getRightDomainBorder() < fun2.getRightDomainBorder()) rightX = fun1.getRightDomainBorder();
        else rightX = fun2.getRightDomainBorder();

        this.fun1 = fun1;
        this.fun2 = fun2;
    }

    @Override
    public double getLeftDomainBorder() {
        return leftX;
    }

    @Override
    public double getRightDomainBorder() {
        return rightX;
    }

    @Override
    public double getFunctionValue(double x) {
        if(x <= getRightDomainBorder() && x >= getLeftDomainBorder())
            return fun1.getFunctionValue(x) * fun2.getFunctionValue(x);
        else return Double.NaN;
    }
}