package com.efp.common.spinnermodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.swing.SpinnerNumberModel;

public class YearSpinnerModel extends SpinnerNumberModel {
    public static final String PROPERTY_NAME_LOCALE = "locale";
    public static final String PROPERTY_NAME_DATE = "date";
    public static final String PROPERTY_NAME_ZONE = "zone";
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private Locale locale;
    private TimeZone zone;
    private Calendar calendar;

    public YearSpinnerModel(Date date, Locale locale, TimeZone zone) {
        this.locale = locale;
        this.zone = zone;
        this.createLocaleAndZoneSensitive();
        this.calendar.setTime(date);
    }

    private void createLocaleAndZoneSensitive() {
        if (this.calendar != null) {
            Date old = this.calendar.getTime();
            this.calendar = Calendar.getInstance(this.zone, this.locale);
            this.calendar.setTime(old);
        } else {
            this.calendar = Calendar.getInstance(this.zone, this.locale);
        }

    }

    @Override
    public Object getValue() {
        return new Integer(this.calendar.get(1));
    }

    @Override
    public void setValue(Object value) {
        Number newVal = (Number) value;
        Number oldVal = (Number) this.getValue();
        if (oldVal.longValue() != newVal.longValue()) {
            int diff = newVal.intValue() - oldVal.intValue();
            int sign = diff > 0 ? 1 : -1;
            if (diff < 0) {
                diff = -diff;
            }

            Date oldDate = this.calendar.getTime();

            for (int i = 0; i < diff; ++i) {
                this.calendar.add(1, sign);
            }

            this.changeSupport.firePropertyChange("date", oldDate, this.getDate());
            this.fireStateChanged();
        }

    }

    public Object getNextValue() {
        Integer currVal = (Integer) this.getValue();
        int newVal = currVal + 1;
        return newVal <= this.calendar.getActualMaximum(1) ? new Integer(newVal) : currVal;
    }

    public Object getPreviousValue() {
        Integer currVal = (Integer) this.getValue();
        int newVal = currVal - 1;
        return newVal >= this.calendar.getActualMinimum(1) ? new Integer(newVal) : currVal;
    }

    public Date getDate() {
        return this.calendar.getTime();
    }

    public void setDate(Date date) {
        Date old = this.calendar.getTime();
        if (!old.equals(date)) {
            this.calendar.setTime(date);
            this.changeSupport.firePropertyChange("date", old, date);
            this.fireStateChanged();
        }

    }

    public Locale getLocale() {
        return this.locale;
    }

    public void setLocale(Locale locale) {
        Locale old = this.locale;
        this.locale = locale;
        this.createLocaleAndZoneSensitive();
        this.changeSupport.firePropertyChange("locale", old, locale);
        this.fireStateChanged();
    }

    public TimeZone getZone() {
        return this.zone;
    }

    public void setZone(TimeZone zone) {
        TimeZone old = this.zone;
        this.zone = zone;
        this.createLocaleAndZoneSensitive();
        this.changeSupport.firePropertyChange("locale", old, this.locale);
        this.fireStateChanged();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.changeSupport.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        this.changeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.changeSupport.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        this.changeSupport.removePropertyChangeListener(propertyName, listener);
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        return this.changeSupport.getPropertyChangeListeners();
    }

    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
        return this.changeSupport.getPropertyChangeListeners(propertyName);
    }

    public boolean hasListeners(String propertyName) {
        return this.changeSupport.hasListeners(propertyName);
    }
}
