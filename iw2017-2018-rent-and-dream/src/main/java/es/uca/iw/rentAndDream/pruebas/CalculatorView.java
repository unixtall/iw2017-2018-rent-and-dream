package es.uca.iw.rentAndDream.pruebas;

import com.vaadin.event.MouseEvents.ClickEvent;

interface CalculatorView {
    public void setDisplay(double value);

    interface CalculatorViewListener {
        void buttonClick(char operation);
    }
    public void addListener(CalculatorViewListener listener);
	void buttonClick(ClickEvent event);
}