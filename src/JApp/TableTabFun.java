package JApp;

import functions.FunctionPoint;
import functions.InappropriateFunctionPointException;
import functions.TabulatedFunction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TableTabFun extends DefaultTableModel {
    TabulatedFunction function;
    Component parent;

    public TableTabFun(TabulatedFunction f, Component c) {
        function = f;
        parent = c;
    }

    @Override
    public int getRowCount() {
        return function != null ? function.getPointsCount() : 0;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        return column == 0 ? "X" : "Y";
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return row >= 0 && column >= 0  && row < getRowCount() && column < getColumnCount();
    }

    @Override
    public Object getValueAt(int row, int column) {
        if( row >= 0 && row < getRowCount()) {
            if(column == 0) return function.getPointX(row);
            else if(column == 1) return  function.getPointY(row);
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {

        try {
            if (column == 0)
                if((double) aValue > function.getPointX(row - 1) && (double) aValue < function.getPointX(row + 1)) {
                    function.setPointX(row, (double) aValue);
                }
                else{
                    double y = function.getPointY(row);
                    double x = (double) aValue;
                    function.deletePoint(row);
                    function.addPoint(new FunctionPoint(x, y));
                }
            else if (column == 1)
                function.setPointY(row, (double) aValue);
        } catch (InappropriateFunctionPointException er) {
            JOptionPane.showMessageDialog(parent, "Такой X уже существует!");
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Double.class;
    }
}
