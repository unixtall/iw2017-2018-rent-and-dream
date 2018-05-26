package es.uca.iw.rentAndDream.Utils;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.vaadin.addons.tuningdatefield.CellItemCustomizerAdapter;
import org.vaadin.addons.tuningdatefield.InlineTuningDateField;
import org.vaadin.addons.tuningdatefield.TuningDateField;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Notification;

public class MultiSelectDateField extends CustomField<Set<LocalDate>> {

    private static final long serialVersionUID = -8898013458211900016L;

	Set<LocalDate> availabilityDates = new HashSet<>();
	Set<LocalDate> selectedDates = new HashSet<>();
    
    @Override
    protected Component initContent() {
        addStyleName("multi-select-date-field");

        InlineTuningDateField inlineTuningDateField = new InlineTuningDateField();
        inlineTuningDateField.setWeekendDisabled(false);
        //inlineTuningDateField.setDateRange(LocalDate.now(), LocalDate.now().plusYears(1), "Date not allow");
        inlineTuningDateField.setLocalDate(LocalDate.now());
       
        
        /*for(int i = 0; i < 5; i++)
        	availabilitiesDates.add(LocalDate.now().plusDays(i));*/
        
        inlineTuningDateField.addDayClickListener(evt -> {
	            
	            if (!selectedDates.contains(evt.getLocalDate())) {
	                selectedDates.add(evt.getLocalDate());
	            } else {
	                selectedDates.remove(evt.getLocalDate());
	            }
	            
	            setValue(selectedDates);
	            fireValueChange(false);
	
	            // Force repaint
	            inlineTuningDateField.markAsDirty();
	
	            Notification.show("Selected dates: " + selectedDates);
	            
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
                style = "day-availability";
            } else {
                style = "day-unavailability";
            }
            
            if (multiSelectDateField.getSelectedDates() != null && multiSelectDateField.getSelectedDates().contains(date))
            {
            	style += " day-selected";
            }
            
            return style;
        }
        
        @Override
        public String getTooltip(LocalDate date, TuningDateField tuningDateField) {
            String style = null;
            if (multiSelectDateField.getValue() != null && multiSelectDateField.getValue().contains(date)) {
                style = "Day available";
            } else {
                style = "Day not available";
            }
            return style;
        }
        
        @Override
        public boolean isEnabled(LocalDate date, TuningDateField tuningDateField) {
            boolean result;
            if (multiSelectDateField.getValue() != null && multiSelectDateField.getValue().contains(date)) {
                result = true;
            } else {
                result = false;
            }
            return result;
        }
        
        
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Class<? extends Set<LocalDate>> getType() {
        return (Class<Set<LocalDate>>) ((Class) Set.class);
    }

	@Override
	public Set<LocalDate> getValue() {
        
		return availabilityDates;
	}

	@Override
	protected void doSetValue(Set<LocalDate> value) {
		// TODO Auto-generated method stub
		
	}
	
	
	public Set<LocalDate> getAvailabilitiesDates() {
		return availabilityDates;
	}

	public void setAvailabilyDates(Set<LocalDate> availabilitiesDates) {
		this.availabilityDates = availabilitiesDates;
	}

	public void setSelectedDates(Set<LocalDate> selectedDates) {
		this.selectedDates = selectedDates;
	}

	public Set<LocalDate> getSelectedDates()
	{
		return this.selectedDates;
	}

}