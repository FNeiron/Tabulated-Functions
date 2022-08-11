package functions;

import java.io.*;

public class LinkedListTabulatedFunction implements TabulatedFunction, Externalizable {

    private class FunctionNode {
        private FunctionPoint point;
        private FunctionNode prev = null;
        private FunctionNode next = null;
        private int index;
    }

    private FunctionNode head;
    private int size;

    {
        head = new FunctionNode();
        size = 0;
        head.index = -1;
        head.prev = head.next = head;
        head.point = null;
    }

    public LinkedListTabulatedFunction() {

    }
    public LinkedListTabulatedFunction(FunctionPoint[] points) {
        if(points.length < 2) throw new IllegalArgumentException();

        for(int i = 0; i < points.length - 1; ++i) {
            if (points[i].getX() < points[i + 1].getX()) {
                head = addNodeToTail();
                head.point = new FunctionPoint(points[i]);
            } else throw new IllegalArgumentException();
        }
        head = addNodeToTail();
        head.point = new FunctionPoint(points[size - 1]);
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException {
        //size = getPointsCount;
        if (leftX >= rightX || pointsCount < 2) throw new IllegalArgumentException();

        double interval = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; ++i) {
            head = addNodeToTail();
            head.point = new FunctionPoint(leftX + interval * i, 0);
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException{
        //size = values.length;
        if(leftX >= rightX || values.length < 2) throw new IllegalArgumentException();

        double interval = (rightX - leftX) / (values.length - 1);

        for (int i = 0; i < values.length; ++i) {
            head = addNodeToTail();
            head.point = new FunctionPoint(leftX + interval * i, values[i]);
        }
    }

    private FunctionNode getNodeByIndex(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= size) throw new FunctionPointIndexOutOfBoundsException();

        if (head.index > index && index + size - head.index < head.index - index
                || index > head.index && index - head.index > head.index + size - index)
            while (head.index != index) head = head.next;
        else while (head.index != index) head = head.prev;
        return head;
    }

    private FunctionNode addNodeToTail() {
        FunctionNode node = new FunctionNode();
        if (size != 0) head = getNodeByIndex(size - 1);
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
        node.index = node.prev.index + 1;
        ++size;
        return node;
    }

    private FunctionNode addNodeByIndex(int index) {
        head = getNodeByIndex(index);
        FunctionNode node = new FunctionNode();
        node.index = index;
        node.next = head;
        node.prev = head.prev;
        head.prev.next = node;
        head.prev = node;
        ++size;
        for (int i = index + 1; i < size; ++i) {
            ++head.index;
            head = head.next;
        }
        return node;
    }

    private FunctionNode deleteNodeByIndex(int index) {
        head = getNodeByIndex(index);
        FunctionNode node = head;
        head.next.prev = head.prev;
        head.prev.next = head.next;
        --size;
        head = head.next;
        for(int i = index; i < size; ++i) {
            --head.index;
            head = head.next;
        }
        return node;
    }

    public double getLeftDomainBorder() {
        return getNodeByIndex(0).point.getX();
    }

    public double getRightDomainBorder() {
        return getNodeByIndex(size - 1).point.getX();
    }

    public double getFunctionValue(double x) {
        if (x > getRightDomainBorder() || x < getLeftDomainBorder()) return Double.NaN;

        int i = 0;
        while (x > getNodeByIndex(i).point.getX()) ++i;
        if (i != 0) {
            return (x - getNodeByIndex(i - 1).point.getX()) / (getNodeByIndex(i).point.getX() - getNodeByIndex(i - 1).point.getX()) *
                    (getNodeByIndex(i).point.getY() - getNodeByIndex(i - 1).point.getY()) + getNodeByIndex(i - 1).point.getY();
        }
        else {
            return getNodeByIndex(i).point.getY();
        }
    }

    public int getPointsCount() { return size; }

    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= size) throw new FunctionPointIndexOutOfBoundsException();
        return new FunctionPoint(getNodeByIndex(index).point);
    }

    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index >= size) throw new FunctionPointIndexOutOfBoundsException();

        if(index > 0 && index < size - 1 && point.getX() > getNodeByIndex(index - 1).point.getX() &&
                point.getX() < getNodeByIndex(index + 1).point.getX() ||
                (index == 0 && point.getX() < getNodeByIndex(1).point.getX()) ||
                (index == size - 1 && point.getX() > getNodeByIndex(index - 1).point.getX()))
            getNodeByIndex(index).point = new FunctionPoint(point);
        else throw new InappropriateFunctionPointException();
    }

    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= size) throw new FunctionPointIndexOutOfBoundsException();
        return getNodeByIndex(index).point.getX();
    }

    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index >= size) throw new FunctionPointIndexOutOfBoundsException();

        if ((index > 0 && (index < size - 1) && x > getPointX(index - 1) && x < getPointX(index + 1)) ||
                (index == 0 && x < getPointX(1) ||
                        ((index == size - 1 && x > getPointX(index - 1)))))
            getNodeByIndex(index).point.setX(x);
        else throw new InappropriateFunctionPointException();
    }

    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= size) throw new FunctionPointIndexOutOfBoundsException();
        return getNodeByIndex(index).point.getY();
    }

    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= size) throw new FunctionPointIndexOutOfBoundsException();
        getNodeByIndex(index).point.setY(y);
    }

    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        if (index < 0 || index >= size) throw new FunctionPointIndexOutOfBoundsException();
        if(size == 2) throw new IllegalStateException();
        deleteNodeByIndex(index);
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {

        int i = 0;
        while (i < size && point.getX() > getNodeByIndex(i).point.getX()) ++i;
        if (i < size && point.getX() == getNodeByIndex(i).point.getX()) {
            throw new InappropriateFunctionPointException();
        }
        if(i < size)
        addNodeByIndex(i).point = new FunctionPoint(point);
        else addNodeToTail().point = new FunctionPoint(point);
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
        if(o.getClass() == getClass()) {
            LinkedListTabulatedFunction temp = (LinkedListTabulatedFunction) o;

            if(size != temp.getPointsCount()|| hashCode() != temp.hashCode()) return false;

            for (int i = 0; i < size; ++i) {
                if (!getPoint(i).equals(temp.getPoint(i))) return false;
            }
            return true;
        }
        else if(o.getClass() == ArrayTabulatedFunction.class) {
            ArrayTabulatedFunction temp = (ArrayTabulatedFunction) o;

            if(size != temp.getPointsCount()|| hashCode() != temp.hashCode()) return false;

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
        for(int i = 0; i < size; ++i) {
            summary = 31 * summary ^ getPoint(i).hashCode();
        }
        return summary ^ size;
    }

    @Override
    public Object clone() {
        FunctionPoint[] points = new FunctionPoint[size];
        for(int i = 0; i < size; ++i) points[i] = new FunctionPoint(getPoint(i));
        return new LinkedListTabulatedFunction(points);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        FunctionPoint[] temp = new FunctionPoint[size];
        for(int i = 0; i < size; ++i) {
            temp[i] = new FunctionPoint(getPoint(i));
        }
        out.writeObject(temp);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        FunctionPoint[] t = (FunctionPoint[]) in.readObject();
        for(int i = 0; i < t.length; ++i) {
            try {
                addPoint(t[i]);
            } catch (InappropriateFunctionPointException e) {
                e.printStackTrace();
            }
        }
    }

    public void print() {
        for(int i = 0; i < size; ++i) {
            System.out.println(getNodeByIndex(i).point.getX() + " " + getNodeByIndex(i).point.getY());
        }
    }
}