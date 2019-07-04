package loggerplusplus.userinterface.dialog;

import loggerplusplus.filter.ColorFilter;
import loggerplusplus.filter.ColorFilterListener;
import loggerplusplus.filter.LogFilter;
import loggerplusplus.filter.parser.ParseException;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by corey on 19/07/17.
 */
public class ColorFilterTableModel extends AbstractTableModel {

    private final Map<Short, UUID> rowUUIDs = new HashMap<Short, UUID>();
    private final Map<UUID, ColorFilter> filters;
    private final ArrayList<ColorFilterListener> colorFilterListeners;
    private final String[] columnNames = {"Title", "LogFilter", "Foreground Color", "Background Color", "Enabled", ""};
    private final JButton removeButton = new JButton("Remove");

    ColorFilterTableModel(Map<UUID, ColorFilter> filters, ArrayList<ColorFilterListener> colorFilterListeners){
        this.filters = filters;
        //Sort existing filters by their priority before adding to table.
        List<ColorFilter> sorted = new ArrayList<ColorFilter>(filters.values());
        Collections.sort(sorted);
        for (ColorFilter filter : sorted) {
            rowUUIDs.put((short) rowUUIDs.size(), filter.getUid());
        }

        this.colorFilterListeners = colorFilterListeners;
    }

    @Override
    public int getRowCount() {
        return filters.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int i) {
        return columnNames[i];
    }

    @Override
    public Object getValueAt(int row, int col) {
        UUID rowUid = rowUUIDs.get((short) row);
        switch (col) {
            case 0:
                return (filters.get(rowUid).getName() == null ? "" : filters.get(rowUid).getName());
            case 1:
                return (filters.get(rowUid).getFilterString() == null ? "" : filters.get(rowUid).getFilterString());
            case 2:
                return (filters.get(rowUid).getForegroundColor() == null ? Color.BLACK : filters.get(rowUid).getForegroundColor());
            case 3:
                return (filters.get(rowUid).getBackgroundColor() == null ? Color.WHITE : filters.get(rowUid).getBackgroundColor());
            case 4:
                return filters.get(rowUid).isEnabled();
            case 5:
                return removeButton;
            default:
                return false;
        }
    }

    public boolean validFilterAtRow(int row) {
        return getFilterAtRow(row).getFilter() != null;
    }

//    public LogFilter getFilterAtRow(int row){
//        return filters.get(rowUUIDs.get((short) row)).getFilter();
//    }

    public ColorFilter getFilterAtRow(int row){
        return filters.get(rowUUIDs.get((short) row));
    }

    public void setValueAt(Object value, int row, int col) {
        UUID rowUid = rowUUIDs.get((short) row);
        ColorFilter filter = filters.get(rowUid);
        switch (col) {
            case 0:
                filter.setName((String) value);
                break;
            case 1: {
                filter.setFilterString((String) value);
                try {
                    filter.setFilter(new LogFilter((String) value));
                } catch (ParseException e) {
                    filter.setFilter(null);
                }
                break;
            }
            case 2:
                filter.setForegroundColor((Color) value);
                break;
            case 3:
                filter.setBackgroundColor((Color) value);
                break;
            case 4:
                filter.setEnabled((Boolean) value);
                break;
            default:
                return;
        }
        for (ColorFilterListener colorFilterListener : this.colorFilterListeners) {
            colorFilterListener.onFilterChange(filter);
        }
    }



    @Override
    public Class<?> getColumnClass(int columnIndex){
        switch (columnIndex) {
            case 0: return String.class;
            case 1: return String.class;
            case 2: return Color.class;
            case 3: return Color.class;
            case 4: return Boolean.class;
            case 5: return JButton.class;
            default: return String.class;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col != 5;
    }

    public void addFilter(ColorFilter filter){
        int i = filters.size();
        rowUUIDs.put((short) i, filter.getUid());
        filters.put(filter.getUid(), filter);
        filter.setPriority((short) i);
        this.fireTableRowsInserted(i, i);
    }

    public void onClick(int row, int column) {
        if(row != -1 && row < filters.size() && column == 5) {
            synchronized (rowUUIDs) {
                ColorFilter removedFilter = this.filters.remove(rowUUIDs.get((short) row));
                this.fireTableRowsDeleted(row, row);
                rowUUIDs.remove((short) row);
                for (ColorFilterListener colorFilterListener : this.colorFilterListeners) {
                    colorFilterListener.onFilterRemove(removedFilter);
                }
                for (int i = row + 1; i <= rowUUIDs.size(); i++) {
                    rowUUIDs.put((short) (i - 1), rowUUIDs.get((short) i));
                    filters.get(rowUUIDs.get((short) i)).setPriority((short) (i-1));
                    rowUUIDs.remove((short) i);
                }
            }
        }
    }

    public void switchRows(int from, int to) {
        UUID toUid = this.rowUUIDs.get((short) to);
        rowUUIDs.put((short) to, rowUUIDs.get((short) from));
        rowUUIDs.put((short) from, toUid);
        filters.get(rowUUIDs.get((short) to)).setPriority((short) to);
        filters.get(rowUUIDs.get((short) from)).setPriority((short) from);
        this.fireTableRowsUpdated(from, from);
        this.fireTableRowsUpdated(to, to);
    }

    public void removeAll() {
        this.filters.clear();
        for(ColorFilterListener listener : colorFilterListeners){
            listener.onFilterRemoveAll();
        }
        this.fireTableDataChanged();
    }
}
