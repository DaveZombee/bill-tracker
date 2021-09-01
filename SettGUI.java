import java.awt.Font;
import javax.swing.*;
import javax.swing.JSpinner.DefaultEditor;

import net.miginfocom.swing.MigLayout;
import java.awt.event.*;

public class SettGUI {

	private JPanel panel = new JPanel();

	// Making the spinners
	private SpinnerModel firstSpinModel = new SpinnerNumberModel(3, 0, 10, 1);
	private SpinnerModel secondSpinModel = new SpinnerNumberModel(1,0,10,1);
	
	private JSpinner firstRemindSpinner = new JSpinner(firstSpinModel);
	private JSpinner secondRemindSpinner = new JSpinner(secondSpinModel);

	// Labels
	private JLabel firstReminder = new JLabel("First reminder:");
	private JLabel secondReminder = new JLabel("Second reminder:");
	private JLabel info = new JLabel("Set the amount of days before the due date of a payment for a reminder");
	private JLabel saveDialog = new JLabel("Settings saved");
	
	// Save settings button
	private JButton saveButton = new JButton("Save");
	
	private Font fontSize = new Font(null, Font.PLAIN, 18); // font for all components
	
	// Constructor for the settings GUI
	public SettGUI() {
		panel.setLayout(new MigLayout());
		
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
		
		// Setting spinner text to be centered
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
				// Gotta do some other stuff in here, but that's not implemented yet
				// eg. save the set amount of days before to remind
				
				JOptionPane.showMessageDialog(panel, saveDialog);
			}
		});

	}

	// Lets MainGUI access this panel of GUI
	public JPanel getPanel() {
		return panel;
	}

}
