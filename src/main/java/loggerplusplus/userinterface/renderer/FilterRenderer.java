package loggerplusplus.userinterface.renderer;

import loggerplusplus.filter.LogFilter;
import loggerplusplus.userinterface.dialog.ColorFilterTable;
import loggerplusplus.userinterface.dialog.ColorFilterTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Created by corey on 22/08/17.
 */
public class FilterRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        boolean validFilter;
        if(table instanceof ColorFilterTable){
            validFilter = ((ColorFilterTableModel) table.getModel()).validFilterAtRow(row);
        }else{
            validFilter = (value instanceof LogFilter);
        }

        if(validFilter){
            c.setBackground(new Color(76,255, 155));
            c.setForeground(Color.BLACK);
        }else{
            c.setBackground(new Color(221, 70, 57));
            c.setForeground(Color.WHITE);
        }

        return c;
    }
}
