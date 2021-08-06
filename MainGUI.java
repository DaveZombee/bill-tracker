import javax.swing.*;
import net.miginfocom.swing.MigLayout;

import java.awt.Font;
import java.awt.event.*;

public class MainGUI {

	JFrame frame = new JFrame("Bill tracker");

	// Navigation bar buttons
	JButton OVButton = new JButton("Overview");
	JButton AddNewButton = new JButton("Add new");
	JButton SettButton = new JButton("Settings");

	// Font
	Font fontSetter = new Font(null, Font.PLAIN, 24);

	// Creating the main page
	public MainGUI() {
		frame.setLayout(new MigLayout("","[200][200][200]",""));
		NavBar();
		frame.add(new OverviewGUI().getPanel(),"span"); // Starting GUI, when program is first opened

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	// Creating the navigation bar and its functions
	public void NavBar() {

		frame.add(OVButton,"growx, width 200!");
		frame.add(AddNewButton,"growx, width 200!");
		frame.add(SettButton, "wrap,growx,width 200!");

		// Setting the font size of the buttons
		OVButton.setFont(fontSetter);
		AddNewButton.setFont(fontSetter);
		SettButton.setFont(fontSetter);

		// Button toggles overview
		OVButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonClicked(new OverviewGUI().getPanel());
			}
		});

		// Button toggles add new
		AddNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonClicked(new AddNewGUI().getPanel());
			}
		});

		// Button toggles settings
		SettButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonClicked(new SettGUI().getPanel());
			}
		});

	}

	// Generic form of action taken when any of the nav buttons are clicked, used above
	public void buttonClicked(JPanel targetPanel) {
		frame.getContentPane().removeAll();
		NavBar();
		frame.add(targetPanel,"span");
		
		frame.revalidate();
		frame.repaint();
	}

	public static void main(String args[]) {
		new MainGUI();
	}

}
