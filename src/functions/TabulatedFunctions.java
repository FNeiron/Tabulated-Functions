package functions;


import java.io.*;

public class TabulatedFunctions {

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) throws IllegalArgumentException {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder())
            throw new IllegalArgumentException();

        double interval = (rightX - leftX) / (pointsCount - 1);

        FunctionPoint[] points = new FunctionPoint[pointsCount];

        for (int i = 0; i < pointsCount; ++i) {
            points[i] = new FunctionPoint(leftX + interval * i,
                    function.getFunctionValue(leftX + interval * i));
        }

        return new LinkedListTabulatedFunction(points);
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeInt(function.getPointsCount());
            for (int i = 0; i < function.getPointsCount(); ++i) {
                dos.writeDouble(function.getPointX(i));
                dos.writeDouble(function.getPointY(i));
            }
            dos.flush();
            dos.close();
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dos = new DataInputStream(in);
        int size = dos.readInt();
        FunctionPoint[] points = new FunctionPoint[size];
        for (int i = 0; i < size; ++i) {
            points[i] = new FunctionPoint(dos.readDouble(), dos.readDouble());
        }
        dos.close();
        return new LinkedListTabulatedFunction(points);
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) {
        PrintWriter pw = new PrintWriter(out);
        pw.print(function.getPointsCount());
        pw.print(' ');

        for (int i = 0; i < function.getPointsCount(); ++i) {
            pw.print(function.getPointX(i));
            pw.print(' ');
            pw.print(function.getPointY(i));
            pw.print(' ');
        }
        pw.flush();
        pw.close();
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer st = new StreamTokenizer(in);
        st.nextToken();
        int size = (int) st.nval;
        st.nextToken();
        FunctionPoint[] points = new FunctionPoint[size];
        for (int i = 0; i < size; ++i) {
            points[i] = new FunctionPoint();
            points[i].setX(st.nval);
            st.nextToken();
            points[i].setY(st.nval);
            st.nextToken();
        }
    return new LinkedListTabulatedFunction(points);
    }
}
