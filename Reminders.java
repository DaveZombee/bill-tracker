// for the system tray
import java.awt.*;
import java.awt.TrayIcon.MessageType;

import java.time.LocalDate; // handling date time things
import java.util.ArrayList; // list of reminders and bills that need reminding
import java.util.LinkedList; // list of bills

public class Reminders {

	private ArrayList<Bill> needReminders; // list of the stuff that needs to have an alert sent out

	// Determines which bills which need to have a reminder sent out
	public Reminders(LinkedList<Bill> listOfBills, ArrayList<Integer> remindList) {
		needReminders = new ArrayList<Bill>(listOfBills.size());

		int daysBeforeFirstRemind = remindList.get(0);
		int daysBeforeSecondRemind = remindList.get(1);

		for (int i = 0; i < listOfBills.size(); i++) {
			LocalDate firstDaysBefore = listOfBills.get(i).getDueDate().minusDays(daysBeforeFirstRemind);
			LocalDate secondDaysBefore = listOfBills.get(i).getDueDate().minusDays(daysBeforeSecondRemind);

			// if the current date is equal to the dates calculated
			if (firstDaysBefore.compareTo(LocalDate.now()) == 0 || secondDaysBefore.compareTo(LocalDate.now()) == 0) {
				if (!listOfBills.get(i).getPaid()) { // if it hasn't been indicated that it's paid
					// Add to the list of bills which DO need reminders
					needReminders.add(listOfBills.get(i));
				}
			}
		}
	}

	// Actually sends out the alerts
	public void alertSender(String messageStr) throws AWTException {
		SystemTray tray = SystemTray.getSystemTray();

		Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
		TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
		trayIcon.setImageAutoSize(true);

		trayIcon.setToolTip(null);
		tray.add(trayIcon);

		trayIcon.displayMessage("Alert", messageStr, MessageType.INFO);
	}
	
	// Creates the string needed for the alert
	public String getAlertMessageStr() {
		String alertStr = ""; // add the bills which are being alerted to it

		for (int i = 0; i < needReminders.size(); i++) {
			if (i == needReminders.size() - 1) { // if its the last one
				if (alertStr == "")
					alertStr += needReminders.get(i).getType().toLowerCase();
				else
					alertStr += "and " + needReminders.get(i).getType().toLowerCase();
			} else // any of the other ones
				alertStr += needReminders.get(i).getType().toLowerCase() + ", ";
		}
		
		String messageStr = "The following bill(s) are due soon: " + alertStr
				+ ". \nIf you have paid them, set them as paid to stop these alerts.";
		
		return messageStr;
	}
	
	// Returns if there are bills which do need to be alerted
	public boolean hasAlerts() {
		boolean hasAlerts = false;
		
		if (!needReminders.isEmpty()) hasAlerts = true;
		
		return hasAlerts;
	}
}
