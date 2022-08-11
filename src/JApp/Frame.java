package JApp;

import functions.Function;
import functions.FunctionPoint;
import functions.InappropriateFunctionPointException;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class Frame extends JFrame {
    private JClassLoader jClassLoader;
    private TabFunParam tabFunParam;
    private TabulatedFunctionDoc tabulatedFunctionDoc;
    private JFileChooser jFileChooser;

    private JPanel panel;

    private JTextField xField;
    private JTextField yField;

    private JButton addPointButton;
    private JButton deletePointButton;

    private JLabel xLabel;
    private JLabel yLabel;

    private JMenuBar barMenu;
    private JTable table;
    private JScrollPane scroll;

    private JMenu menuFile;
    private JMenu menuTabulate;

    private JMenuItem fileNew;
    private JMenuItem fileOpen;
    private JMenuItem fileSave;
    private JMenuItem fileSaveAs;
    private JMenuItem fileExit;

    private JMenuItem loadTabulate;

    private void cancel() {
        if (tabulatedFunctionDoc.isModified() || JOptionPane.showConfirmDialog(new JPanel(), "Функция была изменена без сохранения.\nВЫХОД БЕЗ СОХРАНЕНИЯ?", "ВЫХОД?", JOptionPane.YES_NO_OPTION) == 0) {
            dispose();
        }
    }

    private void saveAs() {
        if(jFileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        String fileName = jFileChooser.getSelectedFile().getAbsolutePath();
        try {
            tabulatedFunctionDoc.saveFunctionAs(fileName);
        } catch (IOException ioException) {
            JOptionPane.showMessageDialog(new JPanel(), ioException.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    public Frame() {
        setTitle("Tabulated Functions");
        setResizable(true);
        setContentPane(panel);
        getRootPane().setDefaultButton(addPointButton); //
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(400,350);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(400, 350));

        jClassLoader = new JClassLoader();

        menuFile = new JMenu("File");
        menuTabulate = new JMenu("Tabulate");

        fileNew = new JMenuItem("New");
        fileOpen = new JMenuItem("Open");
        fileSave = new JMenuItem("Save");
        fileSaveAs = new JMenuItem("SaveAs");
        fileExit = new JMenuItem("Exit");
        loadTabulate = new JMenuItem("Load Tabulated Function");

        tabFunParam = new TabFunParam();
        tabulatedFunctionDoc = new TabulatedFunctionDoc();
        jFileChooser = new JFileChooser();

        TableTabFun tableTabFun = new TableTabFun(tabulatedFunctionDoc, this);
        table.setModel(tableTabFun);

        menuFile.add(fileNew);
        menuFile.add(fileOpen);
        menuFile.add(fileSave);
        menuFile.add(fileSaveAs);
        menuFile.add(fileExit);
        menuTabulate.add(loadTabulate);
        barMenu.add(menuFile);
        barMenu.add(menuTabulate);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancel();
            }
        });

        fileNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tabFunParam.showDialog() == TabFunParam.OK) {
                    tabulatedFunctionDoc.newFunction(tabFunParam.getLeftDomainBorder(),tabFunParam.getRightDomainBorder(), tabFunParam.getPointsCount());
                    table.revalidate();
                    table.repaint();
                }
            }
        });

        fileSaveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAs();
            }
        });

        fileSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tabulatedFunctionDoc.isFileNameAssigned()) {
                    try {
                        tabulatedFunctionDoc.saveFunction();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                else saveAs();
            }
        });

        fileOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jFileChooser.showOpenDialog(Frame.this) != JFileChooser.APPROVE_OPTION)
                    return;
                try {
                    String fileName = jFileChooser.getSelectedFile().getAbsolutePath();
                    tabulatedFunctionDoc.loadFunction(fileName);
                }
                catch (NullPointerException r) {
                    JOptionPane.showMessageDialog(Frame.this, r.getMessage(), "Ошибка!", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(Frame.this, ioException.getMessage(), "Ошибка!", JOptionPane.ERROR_MESSAGE);
                } catch (ParseException parseException) {
                    JOptionPane.showMessageDialog(Frame.this, parseException.getMessage(), "Файл неверного формата!", JOptionPane.ERROR_MESSAGE);
                }
                table.revalidate();
                table.repaint();
            }
        });

        fileExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });

        deletePointButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    tabulatedFunctionDoc.deletePoint(table.getSelectedRow());
                    table.revalidate();
                    table.repaint();
                }
                catch(IllegalStateException er) {
                    JOptionPane.showMessageDialog(Frame.this, "В функции две точки. Удаление невозможно.", "Ошибка!", JOptionPane.ERROR_MESSAGE);
                }
                catch(IndexOutOfBoundsException er) {
                    JOptionPane.showMessageDialog(Frame.this, "Точка не выбрана", "Ошибка!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addPointButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    tabulatedFunctionDoc.addPoint(new FunctionPoint(Double.parseDouble(xField.getText()),Double.parseDouble(yField.getText())));
                    table.revalidate();
                    table.repaint();
                } catch (InappropriateFunctionPointException er) {
                    JOptionPane.showMessageDialog(Frame.this, "Точка существует!", "Ошибка!", JOptionPane.ERROR_MESSAGE);
                }
                catch (NumberFormatException er) {
                    JOptionPane.showMessageDialog(Frame.this, "Некорректные данные!", "Ошибка!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loadTabulate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jFileChooser.showOpenDialog(Frame.this) != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                try {
                    tabFunParam.showDialog();
                    if (tabFunParam.getLastButton() == tabFunParam.OK) {
                        Class functionClass = jClassLoader.loadClassFromFile(jFileChooser.getSelectedFile().getAbsolutePath());
                        Function function = (Function) functionClass.newInstance();
                        tabulatedFunctionDoc.tabulateFunction(function, tabFunParam.getLeftDomainBorder(), tabFunParam.getRightDomainBorder(), tabFunParam.getPointsCount());
                        table.revalidate();
                        table.repaint();
                    }
                } catch (IOException er) {
                    JOptionPane.showMessageDialog(Frame.this, "IOException", "Ошибка!", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalAccessException er) {
                    JOptionPane.showMessageDialog(Frame.this, "Файл недоступен", "Ошибка!", JOptionPane.ERROR_MESSAGE);
                } catch (InstantiationException er) {
                    JOptionPane.showMessageDialog(Frame.this, "Не удалось создать объект", "Ошибка!", JOptionPane.ERROR_MESSAGE);
                } catch (ClassFormatError er) {
                    JOptionPane.showMessageDialog(Frame.this, "Файл не байт-код JAVA class");
                } catch (LinkageError er) {
                    JOptionPane.showMessageDialog(Frame.this, "loader (instance of  JApp/JClassLoader): attempted  duplicate class definition for name: \"functions/basic/Log");
                }
            }
        });



    }



    public static void main(String[] args){

        Frame frame = new Frame();
        frame.setVisible(true);
    }
}
