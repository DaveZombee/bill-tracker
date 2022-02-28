// GUI
import java.awt.Font;
import javax.swing.*;
import javax.swing.JSpinner.DefaultEditor; // for JSpinners to have wider text field
import net.miginfocom.swing.MigLayout;

import java.awt.event.*; // Action listeners
import java.util.ArrayList; // the list of reminders and bills which need reminding

public class SettGUI {

	private JPanel panel = new JPanel();

	// instantiating the spinners
	private SpinnerModel firstSpinModel;
	private SpinnerModel secondSpinModel;

	private JSpinner firstRemindSpinner;
	private JSpinner secondRemindSpinner;

	// Labels
	private JLabel firstReminder = new JLabel("First reminder:");
	private JLabel secondReminder = new JLabel("Second reminder:");
	private JLabel info = new JLabel("Set the amount of days before the due date of a payment for a reminder");
	private JLabel saveDialog = new JLabel("Settings saved");

	// Save settings button
	private JButton saveButton = new JButton("Save");

	private Font fontSize = new Font(null, Font.PLAIN, 18); // font for all components

	// Constructor for the settings GUI
	public SettGUI(ArrayList<Integer> remindList) {
		panel.setLayout(new MigLayout());
		
		// Creating the spinners	
		int firstSpinAmt = remindList.get(0);
		int secondSpinAmt = remindList.get(1);
		
		firstSpinModel = new SpinnerNumberModel(firstSpinAmt,0,10,1);
		secondSpinModel = new SpinnerNumberModel(secondSpinAmt,0,10,1);
	
		firstRemindSpinner = new JSpinner(firstSpinModel);
		secondRemindSpinner = new JSpinner(secondSpinModel);
		
		// Setting fonts of the labels and spinners
		firstRemindSpinner.setFont(fontSize);
		secondRemindSpinner.setFont(fontSize);
		firstReminder.setFont(fontSize);
		secondReminder.setFont(fontSize);
		info.setFont(new Font(null,Font.ITALIC,16));
		saveButton.setFont(fontSize);
		saveDialog.setFont(new Font(null, Font.PLAIN,16));
		
		// Setting spinners so they can't be edited
		firstRemindSpinner.setEditor(new JSpinner.DefaultEditor(firstRemindSpinner));
		secondRemindSpinner.setEditor(new JSpinner.DefaultEditor(secondRemindSpinner));
		
		// Spinner text to be centered
		JSpinner.DefaultEditor firstSpinEditor = (DefaultEditor) firstRemindSpinner.getEditor();
		firstSpinEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		
		JSpinner.DefaultEditor secondSpinEditor = (DefaultEditor) secondRemindSpinner.getEditor();
		secondSpinEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		
		// Adding components
		panel.add(info,"wrap, span 2, gapbottom 15");
		panel.add(firstReminder, "align right");
		panel.add(firstRemindSpinner, "wrap,width 40!");
		panel.add(secondReminder);
		panel.add(secondRemindSpinner,"width 40!");
		panel.add(saveButton,"cell 3 4, gaptop 65, gapleft 15");
		
		// Action when button is clicked
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				remindList.set(0, (int) firstRemindSpinner.getValue());
				remindList.set(1, (int) secondRemindSpinner.getValue());
				
				JOptionPane.showMessageDialog(panel, saveDialog);
			}
		});

	}

	// Lets MainGUI access this panel of GUI
	public JPanel getPanel() {
		return panel;
	}
	
	// Checking if the inputs have been changed, but not saved
	public boolean isChanged(ArrayList<Integer> remindList) {
		boolean changed = false;
		
		// Getting the values from the spinners
		int firstSpinnerAmt = (int) firstRemindSpinner.getValue();
		int secondSpinnerAmt = (int) secondRemindSpinner.getValue();
		
		// Actually checking
		if(firstSpinnerAmt != remindList.get(0) || secondSpinnerAmt != remindList.get(1)) {
			changed = true;
		}
		
		return changed;
	}
	
	// Resets user fields when clicking between panels
	public void resetFields(ArrayList<Integer> remindList) {
		firstRemindSpinner.setValue(remindList.get(0));
		secondRemindSpinner.setValue(remindList.get(1));
	}
}
