package functions;

import java.io.*;

public class ArrayTabulatedFunction implements TabulatedFunction, Externalizable {

    private FunctionPoint[] array;
    private int size;

    public ArrayTabulatedFunction() {

    }

    public ArrayTabulatedFunction(FunctionPoint[] points) {
        if(points.length < 2) throw new IllegalArgumentException();
        size = points.length;
        array = new FunctionPoint[size * 2];

        for(int i = 0; i < size - 1; ++i) {
            if (points[i].getX() < points[i + 1].getX())
                array[i] = new FunctionPoint(points[i]);
            else throw new IllegalArgumentException();
        }
        array[size - 1] = new FunctionPoint(points[size - 1]);
    }

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException{
        size = pointsCount;
        array = new FunctionPoint[size * 2];


        if(leftX >= rightX || pointsCount < 2) throw new IllegalArgumentException();


        double interval = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < size; ++i)
            array[i] = new FunctionPoint(leftX + interval * i, 0);
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException{
        size = values.length;
        if(leftX >= rightX) throw new IllegalArgumentException();
        array = new FunctionPoint[size * 2];

        double interval = (rightX - leftX) / (values.length - 1);

        for (int i = 0; i < size; ++i) {
            array[i] = new FunctionPoint(leftX + interval * i, values[i]);
        }
    }

    public double getLeftDomainBorder() {
        return array[0].getX();
    }

    public double getRightDomainBorder() {
        return array[size - 1].getX();
    }

    public double getFunctionValue(double x) {
        if (x > array[size - 1].getX() || x < array[0].getX()) return Double.NaN;

        int i = 0;
        while (x > array[i].getX()) ++i;
        if (i != 0) {
            return (x - array[i - 1].getX()) / (array[i].getX() - array[i - 1].getX()) *
                    (array[i].getY() - array[i - 1].getY()) + array[i - 1].getY();
        }
        else {
            return array[i].getY();
        }
    }

    public int getPointsCount() { return size; }

    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= size) throw new FunctionPointIndexOutOfBoundsException();
        return new FunctionPoint(array[index]);
    }

    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index >= size) throw new FunctionPointIndexOutOfBoundsException();

        if(index > 0 && index < size - 1 && point.getX() > getPointX(index - 1) && point.getX() < getPointX(index + 1) ||
                (index == 0 && point.getX() < getPointX(1)) ||
                (index == size - 1 && point.getX() > getPointX(index - 1)))
            array[index] = new FunctionPoint(point);
        else throw new InappropriateFunctionPointException();
    }

    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= size) throw new FunctionPointIndexOutOfBoundsException();
        return array[index].getX();
    }

    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index >= size) throw new FunctionPointIndexOutOfBoundsException();

        if ((index > 0 && (index < size - 1) && x > getPointX(index - 1) && x < getPointX(index + 1)) ||
                (index == 0 && x < getPointX(1) ||
                        ((index == size - 1 && x > getPointX(index - 1)))))
            array[index].setX(x);
        else throw new InappropriateFunctionPointException();
    }

    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= size) throw new FunctionPointIndexOutOfBoundsException();
        return array[index].getY();
    }

    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= size) throw new FunctionPointIndexOutOfBoundsException();
        array[index].setY(y);
    }

    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= size) throw new FunctionPointIndexOutOfBoundsException();
        if(size == 2) throw new IllegalStateException();
        //for (int i = index; i < size - 1; ++i) array[i] = array[i + 1];
        System.arraycopy(array, index + 1, array, index, size - index - 1);
        --size;
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {

        if(size == array.length) {
            FunctionPoint[] new_array = new FunctionPoint[array.length * 2];

            System.arraycopy(array, 0, new_array, 0, size);

            int i = 0;
            while (i < size && point.getX() > array[i].getX()) ++i;
            if (i < size && point.getX() == array[i].getX()) {
                throw new InappropriateFunctionPointException();
            }

            //for (int j = i; j < size - 1; ++j) new_array[j+1] = new_array[j];
            System.arraycopy(new_array, i, new_array, i + 1, size - i);
            new_array[i] = new FunctionPoint(point);
            ++size;
            array = new_array;
        }
        else {
            int i = 0;
            while (i < size && point.getX() > array[i].getX()) ++i;
            if (i < size && point.getX() == array[i].getX()) {
                throw new InappropriateFunctionPointException();
            }
            //for (int j = size - 1; j >= i; --j) array[j+1] = array[j];      // Переписать через arraycopy
            System.arraycopy(array, i, array, i + 1, size - i);
            array[i] = new FunctionPoint(point);

            ++size;
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("{");
        str.append(getPoint(0));
        for(int i = 1; i < size; ++i)
            str.append(", " + getPoint(i));
        str.append("}");
        return str.toString();
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(this == o) return true;
        if(o.getClass() == getClass() &&
                getPointsCount() == ((ArrayTabulatedFunction)o).getPointsCount()
                        && hashCode() == ((ArrayTabulatedFunction)o).hashCode()) {
            ArrayTabulatedFunction temp = (ArrayTabulatedFunction) o;

            for (int i = 0; i < size; ++i) {
                if (!getPoint(i).equals(temp.getPoint(i))) return false;
            }
            return true;
        }
        else if(o.getClass() == LinkedListTabulatedFunction.class &&
                getPointsCount() == ((LinkedListTabulatedFunction)o).getPointsCount()
                        && hashCode() == ((LinkedListTabulatedFunction)o).hashCode()) {
            LinkedListTabulatedFunction temp = (LinkedListTabulatedFunction) o;

            for (int i = 0; i < size; ++i) {
                if (!getPoint(i).equals(temp.getPoint(i))) return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int summary = 0;
        for(int i = 0; i < size;++i) {
            summary = 31 * summary ^ array[i].hashCode();
        }
        return summary ^ size;
    }

    @Override
    public Object clone() {
        FunctionPoint[] temp = new FunctionPoint[size];
        System.arraycopy(array, 0, temp, 0, size);
        return new ArrayTabulatedFunction(temp);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(size);
        out.writeObject(array);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        size = (int)in.readObject();
        array = (FunctionPoint[])in.readObject();
    }
}
