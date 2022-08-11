package functions;

import java.io.*;
import java.util.Objects;

public class FunctionPoint implements Externalizable {

    private double numX, numY;

    public FunctionPoint() {
        numX = numY = 0;
    }

    public FunctionPoint(double X, double Y) {
        numX = X; numY = Y;
    }

    public FunctionPoint(FunctionPoint point) {
        numX = point.getX();
        numY = point.getY();
    }

    public double getX() { return numX; }
    public double getY() { return numY; }

    public void setX(double x) { numX = x; }
    public void setY(double y) { numY = y; }

    @Override
    public String toString() {
        return "(" + numX + "; " + numY + ")";
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || getClass() != o.getClass()) return false;
        if(this == o) return true;
        return numX == ((FunctionPoint) o).numX && numY == ((FunctionPoint) o).numY;
    }

    @Override
    public int hashCode() {

        int x_1 = (int) (Double.doubleToLongBits(numX) >>> 32);
        int x_2 = (int) (Double.doubleToLongBits(numX) & -1);
        int y_1 = (int) (Double.doubleToLongBits(numY) >> 32);
        int y_2 = (int) (Double.doubleToLongBits(numY) & -1);
        return 31 * (x_1 ^ x_2) + y_1 ^ y_2;

        //return Objects.hash(numX, numY);
    }

    @Override
    protected Object clone() {
        return new FunctionPoint(numX, numY);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeDouble(numX);
        out.writeDouble(numY);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        numX = in.readDouble();
        numY = in.readDouble();
    }
}
