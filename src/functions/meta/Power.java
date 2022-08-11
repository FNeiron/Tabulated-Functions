package functions.meta;

import functions.Function;

public class Power implements Function {
    private double pow;
    private Function fun;
    public Power(Function fun, double pow) {
        this.pow = pow;
        this.fun = fun;
    }

    @Override
    public double getLeftDomainBorder() {
        return fun.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        return fun.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x) {
        return Math.pow(fun.getFunctionValue(x), pow);
    }
}
