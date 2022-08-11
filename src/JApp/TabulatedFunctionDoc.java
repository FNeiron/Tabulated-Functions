package JApp;

import functions.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class TabulatedFunctionDoc implements TabulatedFunction {
    public TabulatedFunction array;        // массив, хранящийся в документе
    private String file_name = "";                      // Название документа

    public boolean isModified() {
        return modified;
    }

    private boolean modified = false;                   // Был ли сохранён файл(т.е. функция в нём)
    private boolean fileNameAssigned = false;           // было ли установлено имя файла

    public boolean isFileNameAssigned() {
        return fileNameAssigned;
    }

    public TabulatedFunctionDoc() {
        array = new ArrayTabulatedFunction(0, 10, 11);
    }

    public void newFunction(double leftX, double rightX, int pointsCount) {
        array = new ArrayTabulatedFunction(leftX, rightX, pointsCount);
    }

    public void tabulateFunction(Function function, double leftX, double rightX, int pointsCount) {
        array = TabulatedFunctions.tabulate(function, leftX, rightX, pointsCount);
        modified = false;
    }

    public void saveFunctionAs(String fileName) throws IOException {
        if (fileNameAssigned && this.file_name.equals(fileName))
            return;

        file_name = fileName;
        modified = true;
        fileNameAssigned = true;

        JSONObject docs = new JSONObject();
        docs.put("size", array.getPointsCount());
        for (int i = 0; i < array.getPointsCount(); ++i) {
            docs.put("x" + i, array.getPointX(i));
            docs.put("y" + i, array.getPointY(i));
        }
        FileWriter fw = new FileWriter(fileName);
        fw.write(docs.toJSONString());
        fw.flush();
        fw.close();
    }

    public void loadFunction(String fileName) throws IOException, ParseException {
        FileReader reader = new FileReader(fileName);
        file_name = fileName;
        fileNameAssigned = true;
        modified = true; // FIXME

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

        int size = (int) (long) jsonObject.get("size");
        FunctionPoint[] points = new FunctionPoint[size];
        for (int i = 0; i < size; ++i) {
            points[i] = new FunctionPoint((double) jsonObject.get("x" + i), (double) jsonObject.get("y" + i));
        }

        array = new ArrayTabulatedFunction(points);
    }

    public void saveFunction() throws IOException {
        if (!modified) {
            if (!fileNameAssigned)
                throw new FileNotFoundException();

            JSONObject docs = new JSONObject();
            docs.put("size", array.getPointsCount());
            for (int i = 0; i < array.getPointsCount(); ++i) {
                docs.put("x" + i, array.getPointX(i));
                docs.put("y" + i, array.getPointY(i));
            }
            FileWriter fw = new FileWriter(file_name);
            fw.write(docs.toJSONString());
            fw.flush();
            fw.close();
            modified = true;
        }
    }

    @Override
    public double getLeftDomainBorder() {
        return array.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        return array.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x) {
        return array.getFunctionValue(x);
    }

    @Override
    public int getPointsCount() {
        return array.getPointsCount();
    }

    @Override
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        return array.getPoint(index);
    }

    @Override
    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        array.setPoint(index, point);
        modified = false;
    }

    @Override
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        return array.getPointX(index);
    }

    @Override
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        array.setPointX(index, x);
        modified = false;
    }

    @Override
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        return array.getPointY(index);
    }

    @Override
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        array.setPointY(index, y);
        modified = false;
    }

    @Override
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        array.deletePoint(index);
        modified = false;
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        array.addPoint(point);
        modified = false;
    }

    @Override
    public String toString() {
        return array.toString();
    }

    @Override
    public boolean equals(Object o) {
        return array.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(array.hashCode(), file_name, fileNameAssigned, modified);
    }

    @Override
    public Object clone() {
        TabulatedFunctionDoc clone = new TabulatedFunctionDoc();
        clone.array = (ArrayTabulatedFunction) array.clone();
        clone.file_name = file_name;
        clone.modified = modified;
        clone.fileNameAssigned = fileNameAssigned;
        return clone;
    }
}
