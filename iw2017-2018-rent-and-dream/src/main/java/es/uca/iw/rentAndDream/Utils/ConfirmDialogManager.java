package es.uca.iw.rentAndDream.Utils;

import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.ui.Notification;

public class ConfirmDialogManager extends ConfirmDialog {
	
	Boolean reply;
	
	public ConfirmDialogManager(String message)
	{
		this.show(getUI().getCurrent(), message, new ConfirmDialog.Listener() {
			
            public void onClose(ConfirmDialog dialog) {
            	
            	reply = new Boolean(dialog.isConfirmed());
            }
        });
	}

	public Boolean getReply() {
		return reply;
	}

	public void setReply(Boolean reply) {
		this.reply = reply;
	}
	
	
	
}
