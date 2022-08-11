package functions.meta;

import functions.Function;

public class Scale implements Function {
    private double dX, dY;
    private Function fun;

    public Scale(Function fun, double dX, double dY) {
        this.dX = dX;
        this.dY = dY;
        this.fun = fun;
    }

    @Override
    public double getLeftDomainBorder() {
        return fun.getLeftDomainBorder() * dX;
    }

    @Override
    public double getRightDomainBorder() {
        return fun.getRightDomainBorder() * dX;
    }

    @Override
    public double getFunctionValue(double x) {
        return fun.getFunctionValue(x) * dY;
    }
}
