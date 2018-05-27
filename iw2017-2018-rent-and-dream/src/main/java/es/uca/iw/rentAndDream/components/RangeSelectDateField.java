package es.uca.iw.rentAndDream.components;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.vaadin.addons.tuningdatefield.CellItemCustomizerAdapter;
import org.vaadin.addons.tuningdatefield.InlineTuningDateField;
import org.vaadin.addons.tuningdatefield.TuningDateField;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

@SpringComponent
@Scope("prototype")
public class RangeSelectDateField extends CustomField<Set<LocalDate>> {

    private static final long serialVersionUID = -8898013458211900016L;

	Set<LocalDate> availabilityDates = new HashSet<>();
	Set<LocalDate> selectedDates = new HashSet<>();
	Set<LocalDate> value = new HashSet<>();
	InlineTuningDateField inlineTuningDateField = new InlineTuningDateField();
	private LocalDate startDate = null;
	private LocalDate endDate = null;
	
    
    @Override
    protected Component initContent() {
        addStyleName("multi-select-date-field");

       
        inlineTuningDateField.setWeekendDisabled(false);
        inlineTuningDateField.setLocalDate(LocalDate.now().minusDays(1));  
        
        inlineTuningDateField.addDayClickListener(evt -> {
	            
    		
        	if(startDate != null && endDate != null)
        	{
        		startDate = null;
        		endDate = null;
        		selectedDates.clear();
        		//value.clear();
        		doSetValue(selectedDates);
        		
        	}
        	
        	if(startDate == null)
        	{
        		
         		startDate = evt.getLocalDate();
         		//endDate = startDate.plusDays(1);
        		selectedDates.add(startDate);
        		//value.add(startDate);
         		doSetValue(selectedDates);
         		
        		//this.doSetValue(selectedDates);
       			//Notification.show("entra por 1 "  + getValue() + " stardate:" + getStartDate());	
        		//Notification.show("entra por 2 "  + getValue() + " stardate:" + getStartDate());	
        	}
        	else
        	if(startDate != null && evt.getLocalDate().isAfter(startDate))
        	{
    		
        		this.endDate = evt.getLocalDate().plusDays(1);
        		for(LocalDate d = startDate; !d.isEqual(endDate); d = d.plusDays(1) )
				{
					if(availabilityDates.contains(d)) {
						selectedDates.add(d);
						//value.add(d);
					}
					else {
		        		startDate = d;
		        		endDate = null;
		        		selectedDates.clear();
		        		value.clear();
						selectedDates.add(d);
						value.add(d);
						return;
					}
				}

		  		doSetValue(selectedDates);
        		selectedDates.clear();

        	}
        
        	//Notification.show("Values Dates:" + getValue() + " startDate: " + this.getStartDate());
            fireValueChange(true);

            // Force repaint
            inlineTuningDateField.markAsDirty();
    		inlineTuningDateField.setLocalDate(LocalDate.now().minusDays(1)); 
            //Notification.show("Selected dates: " + selectedDates + "\n Values Dates:" + getValue());
	            
            });

        	inlineTuningDateField.setCellItemCustomizer(new MultiSelectCalendarCustomizer(this));

        return inlineTuningDateField;
    }

    private void fireValueChange(boolean b) {
		// TODO Auto-generated method stub
    	
		
	}

	public static class MultiSelectCalendarCustomizer extends CellItemCustomizerAdapter {

        private static final long serialVersionUID = -8049917653096153300L;

        private RangeSelectDateField multiSelectDateField;

        public MultiSelectCalendarCustomizer(RangeSelectDateField multiSelectDateField) {
            this.multiSelectDateField = multiSelectDateField;
        }

        /**
         * Careful cause selected style is used by tuning datefield
         */
        @Override
        public String getStyle(LocalDate date, TuningDateField tuningDateField) {
            String style = null;
            
            if (multiSelectDateField.getAvailabilityDates() != null && multiSelectDateField.getAvailabilityDates().contains(date) 
            		&& ( multiSelectDateField.getSelectedDates().isEmpty() || date.isAfter(multiSelectDateField.getStartDate()))) {
                style = "day-availability";
            } else {
                style = "day-unavailability";
            }
            
            if (multiSelectDateField.getValue() != null && multiSelectDateField.getValue().contains(date))
            {
            	style += " day-selected";
            }
            
            return style;
        }
        
        @Override
        public String getTooltip(LocalDate date, TuningDateField tuningDateField) {
            String style = null;
            if (multiSelectDateField.getAvailabilityDates() != null && multiSelectDateField.getAvailabilityDates().contains(date) 
            		&& ( multiSelectDateField.getSelectedDates().isEmpty() || date.isAfter(multiSelectDateField.getStartDate()))) {
                style = "Day available";
            } else {
                style = "Day not available";
            }
            return style;
        }
        
        @Override
        public boolean isEnabled(LocalDate date, TuningDateField tuningDateField) {
            boolean result;
            if (multiSelectDateField.getAvailabilityDates() != null && multiSelectDateField.getAvailabilityDates().contains(date)
            		&& ( multiSelectDateField.getSelectedDates().isEmpty() || date.isAfter(multiSelectDateField.getStartDate()))) {
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
        
		return this.value;
	}

	@Override
	protected void doSetValue(Set<LocalDate> value) {
		// TODO Auto-generated method stub
		this.value.clear();
		this.value.addAll(value);	
	}
		
	public Set<LocalDate> getAvailabilityDates() {
		return availabilityDates;
	}

	public void setAvailabilyDates(Set<LocalDate> availabilitiesDates) {
		this.availabilityDates = availabilitiesDates;
	}

	private void setSelectedDates(Set<LocalDate> selectedDates) {
		this.selectedDates = selectedDates;
	}

	private Set<LocalDate> getSelectedDates()
	{
		return this.selectedDates;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public void setAvailabilityDates(Set<LocalDate> availabilityDates) {
		this.availabilityDates = availabilityDates;
	}

	public void setValue(Set<LocalDate> value) {
		this.value = value;
	}

	public InlineTuningDateField getInlineTuningDateField() {
		return inlineTuningDateField;
	}

	public void setInlineTuningDateField(InlineTuningDateField inlineTuningDateField) {
		this.inlineTuningDateField = inlineTuningDateField;
	}
	
	
	
}