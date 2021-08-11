/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym.clases;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Alvarez
 */
public class ColorTablaIngresos extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
        column = 8;
        setEnabled(table == null || table.isEnabled());
        setBackground(Color.white);
        table.setForeground(Color.black);
        setHorizontalAlignment(2);
        if (table.getValueAt(row, column) != null) {
            if (Integer.valueOf(table.getValueAt(row, column).toString()) == 0) {
                setBackground(Color.RED);
                table.setForeground(Color.WHITE);
            }
        }
        super.getTableCellRendererComponent(table, value, selected, focused, row, column);
        return this;
    }
}
