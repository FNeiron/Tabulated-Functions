package functions.basic;

import functions.Function;

public class Log implements Function {
    private double a;

    public Log() {
        this.a = Math.E;
    }

    public Log(double a) {
        if(a < 0 || a == 1) return;
        this.a = a;
    }

    @Override
    public double getLeftDomainBorder() {
        return 0;
    }

    @Override
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double getFunctionValue(double x) {
        if(x>0)
            return Math.log(x) / Math.log(a);
        else return Double.NaN;
    }
}
