package JApp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TabFunParam extends JDialog {
    private JPanel winPanel;

    private JTextField xField;
    private JTextField yField;

    private JSpinner pointsSpinner;

    private JLabel xDomainBorder;
    private JLabel yDomainBorder;
    private JLabel pointsCount;

    private JButton cancelButton;
    private JButton OKButton;

    public static final int CANCEL = 0;
    public static final int OK = 1;
    private int lastButton;

    public TabFunParam() {
        setTitle("Function Parameters");
        getRootPane().setDefaultButton(OKButton);
        xField.setText("0");
        yField.setText("10");
        pointsSpinner.setModel(new SpinnerNumberModel(11,2,Integer.MAX_VALUE, 1));
        setContentPane(winPanel);
        setModal(true);
        setResizable(false);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        getRootPane().setDefaultButton(OKButton);

        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double leftX = Double.parseDouble(xField.getText());
                double leftY = Double.parseDouble(yField.getText());
                int points = (int) pointsSpinner.getValue();

                if(leftX < leftY && points > 1) {
                    lastButton = OK;
                    setVisible(false);
                }
                else JOptionPane.showMessageDialog(new JPanel(),"Некорректные данные", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                lastButton = CANCEL;
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                lastButton = CANCEL;
            }
        });
    }

    public int showDialog() {
        setSize(300, 200);
        setLocationRelativeTo(getParent());
        setVisible(true);
        return lastButton = OK;
    }

    public double getLeftDomainBorder() {
        return Double.parseDouble(xField.getText());
    }


    public double getRightDomainBorder() {
        return  Double.parseDouble(yField.getText());
    }


    public int getPointsCount() {
        return (int)pointsSpinner.getValue();
    }

    public static void main(String[] args) {
        TabFunParam dialog = new TabFunParam();
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        System.exit(0);
    }

    public int getLastButton() {
        return lastButton;
    }
}
