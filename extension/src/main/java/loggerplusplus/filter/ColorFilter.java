package loggerplusplus.filter;

import loggerplusplus.filter.parser.ParseException;

import java.awt.*;
import java.util.UUID;

/**
 * Created by corey on 19/07/17.
 */
public class ColorFilter implements Comparable<ColorFilter>{
    private UUID uid;
    private String name;
    private LogFilter filter;
    private String filterString;
    private Color backgroundColor;
    private Color foregroundColor;
    private boolean enabled;
    private boolean modified;
    private boolean shouldRetest;
    private short priority;

    public ColorFilter(){
        this.uid = UUID.randomUUID();
        this.enabled = true;
        this.shouldRetest = true;
    }

    public ColorFilter(String title, String filterString) throws ParseException {
        this();
        this.name = title;
        this.setFilterString(filterString);
        this.setFilter(new LogFilter(filterString));
    }

    public ColorFilter(String title, LogFilter filter) throws ParseException {
        this();
        this.name = title;
        this.setFilter(new LogFilter(filter.toString()));
    }

    public UUID getUid() {
        return uid;
    }

    public void setBackgroundColor(Color backgroundColor){
        this.backgroundColor = backgroundColor;
        this.modified = true;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getForegroundColor() {return foregroundColor;}

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
        modified = true;
    }

    public LogFilter getFilter() {
        return filter;
    }

    public void setFilter(LogFilter filter) {
        this.filter = filter;
        if(filter != null)
            this.filterString = filter.toString();
        modified = true;
        shouldRetest = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        modified = true;
        shouldRetest = true;
    }

    public String getFilterString() {
        return filterString;
    }

    public void setFilterString(String filterString) {
        this.filterString = filterString;
    }

    public boolean equals(Object obj){
        if(obj instanceof ColorFilter){
            return ((ColorFilter) obj).getUid().equals(this.uid);
        }else{
            return super.equals(obj);
        }
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public void setPriority(short priority){
        this.priority = priority;
        this.modified = true;
    }

    public short getPriority() {
        return priority;
    }

    @Override
    public int compareTo(ColorFilter colorFilter) {
        return ((Comparable) this.priority).compareTo(colorFilter.getPriority());
    }

    public boolean shouldRetest() {
        return shouldRetest;
    }

    @Override
    public String toString() {
        return "ColorFilter[" + (this.filter != null ? this.filter.toString() : "") + "]";
    }
}
