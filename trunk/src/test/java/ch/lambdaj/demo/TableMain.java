package ch.lambdaj.demo;

import static ch.lambdaj.Lambda.*;

import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.util.List;
import java.awt.*;

public class TableMain {

    private static final int BEAN_NUMBER = 10;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel(new GridLayout(1,0));
        panel.setOpaque(true);
        frame.setContentPane(panel);

        List<Bean> model = new ArrayList<Bean>();
        for (int i = 0; i < BEAN_NUMBER; i++) model.add(new Bean(true));

        JTable table = new JTable(new LambdajTableModel(model));
        table.setPreferredScrollableViewportSize(new Dimension(400, 200));
        table.setFillsViewportHeight(true);
        panel.add(new JScrollPane(table));

        frame.pack();
        frame.setVisible(true);
    }

    private static class LambdajTableModel extends AbstractTableModel {

        private List<Bean> model;

        LambdajTableModel(List<Bean> beans) {
            model = new ArrayList(beans);

            Bean total = new Bean();
/*
            for (Bean bean : beans) {
                total.setA(total.getA() + bean.getA());
                total.setB(total.getB() + bean.getB());
                total.setC(total.getC() + bean.getC());
            }
*/
            total.setA(sum(beans, on(Bean.class).getA()));
            total.setB(sum(beans, on(Bean.class).getB()));
            total.setC(sum(beans, on(Bean.class).getC()));

            model.add(total);
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) return getRowName(rowIndex);
            Bean bean = model.get(rowIndex);
            switch (columnIndex) {
                case 1: return bean.getA();
                case 2: return bean.getB();
                case 3: return bean.getC();
            }
            return 0;
        }

        public int getRowCount() {
            return model.size();
        }

        public int getColumnCount() {
            return 4;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnIndex == 0 ? String.class : Integer.class;
        }

        private String getRowName(int rowIndex) {
            return rowIndex == BEAN_NUMBER ? "Totals" : "Bean " + rowIndex;
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0: return "Object";
                case 1: return "A";
                case 2: return "B";
                case 3: return "C";
            }
            return "";
        }
    }

    private static Random random = new Random(0);

    public static class Bean {

        public Bean() { this(false); }

        public Bean(boolean initValues) {
            if (initValues) {
                a = random.nextInt(1000);
                b = random.nextInt(1000);
                c = random.nextInt(1000);
            }
        }

        private int a;
        public int getA() { return a; }
        public void setA(int a) { this.a = a; }

        private int b;
        public int getB() { return b; }
        public void setB(int b) { this.b = b; }

        private int c;
        public int getC() { return c; }
        public void setC(int c) { this.c = c; }
    }
}
