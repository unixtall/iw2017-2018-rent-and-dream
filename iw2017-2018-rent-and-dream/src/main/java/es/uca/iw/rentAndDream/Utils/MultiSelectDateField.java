package es.uca.iw.rentAndDream.Utils;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.vaadin.addons.tuningdatefield.CellItemCustomizerAdapter;
import org.vaadin.addons.tuningdatefield.InlineTuningDateField;
import org.vaadin.addons.tuningdatefield.TuningDateField;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Notification;

public class MultiSelectDateField extends CustomField<Set<LocalDate>> {

    private static final long serialVersionUID = -8898013458211900016L;

	Set<LocalDate> value = new HashSet<>();
    
    @Override
    protected Component initContent() {
        addStyleName("multi-select-date-field");

        InlineTuningDateField inlineTuningDateField = new InlineTuningDateField();

        value.add(LocalDate.of(2018, 05, 25));
        
        inlineTuningDateField.addDayClickListener(evt -> {
            Set<LocalDate> value = getValue();
            if (value == null) {
                value = new HashSet<>();
            }

            if (!value.contains(evt.getLocalDate())) {
                value.add(evt.getLocalDate());
            } else {
                value.remove(evt.getLocalDate());
            }
            setValue(value);
            fireValueChange(false);

            // Force repaint
                inlineTuningDateField.markAsDirty();


                //Notification.show("Selected dates: " + evt.getLocalDate());

                
                Notification.show("Selected dates: " + getValue());
            });

        inlineTuningDateField.setCellItemCustomizer(new MultiSelectCalendarCustomizer(this));

        return inlineTuningDateField;
    }

    private void fireValueChange(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public static class MultiSelectCalendarCustomizer extends CellItemCustomizerAdapter {

        private static final long serialVersionUID = -8049917653096153300L;

        private MultiSelectDateField multiSelectDateField;

        public MultiSelectCalendarCustomizer(MultiSelectDateField multiSelectDateField) {
            this.multiSelectDateField = multiSelectDateField;
        }

        /**
         * Careful cause selected style is used by tuning datefield
         */
        @Override
        public String getStyle(LocalDate date, TuningDateField tuningDateField) {
            String style = null;
            if (multiSelectDateField.getValue() != null && multiSelectDateField.getValue().contains(date)) {
                style = "multi-selected";
            } else {
                style = "multi-unselected";
            }
            return style;
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Class<? extends Set<LocalDate>> getType() {
        return (Class<Set<LocalDate>>) ((Class) Set.class);
    }

	@Override
	public Set<LocalDate> getValue() {
        
		return value;
	}

	@Override
	protected void doSetValue(Set<LocalDate> value) {
		// TODO Auto-generated method stub
		
	}

}