import java.awt.Font;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

import net.miginfocom.swing.MigLayout;

public class OverviewGUI {

	private JPanel panel = new JPanel();
	
	// Things for table (these are created later)
	private JTable actualTable;
	private JScrollPane billTable;
	private DefaultTableModel model;
	private String[] columnHeaders = { "Type", "Notes", "Pay period", "Due date","Paid" }; // Table column headings
	
	// Menu which appears on right clicking a row
	private JPopupMenu rightClickMenu = new JPopupMenu();
	
	private JMenuItem viewItem = new JMenuItem("View");
	private JMenuItem editItem = new JMenuItem("Edit");
	private JMenuItem paidItem = new JMenuItem("Toggle paid");
	private JMenuItem deleteItem = new JMenuItem("Delete");
	
	private JLabel deleteWarning = new JLabel();
	
	// Constructor for the overview GUI
	public OverviewGUI(LinkedList<Bill> listOfBills) {
		// Creating the JTable here because it needs a linked list
		model = new DefaultTableModel(fillTable(listOfBills),columnHeaders); // used so that a row can be deleted from table
		actualTable = new JTable(model);
		billTable = new JScrollPane(actualTable); // Lets the column headings can be seen
	
		// Table settings
		actualTable.setDefaultEditor(Object.class, null); // table can't be edited
		actualTable.getTableHeader().setReorderingAllowed(false); // columns can't be dragged
		actualTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // only one row can be selected
		actualTable.getTableHeader().setResizingAllowed(false); // cannot resize the columns
		actualTable.setRowHeight(actualTable.getRowHeight() + 10); // making the row size bigger
		actualTable.setFocusable(false); // can't select a single cell

		// Setting the font sizes
		actualTable.setFont(new Font(null,Font.PLAIN,16)); // Sets font of the info in the table
		actualTable.getTableHeader().setFont(new Font(null,Font.PLAIN,18)); // Sets font of the table headers
		
		// Layout and adding table
		panel.setLayout(new MigLayout());		
		panel.add(billTable,"width 595!, height 200!, align center"); 
		// (it's the JScrollPane that's being added, but that's how it works)
		
		// Right click menu things
		rightClickMenu.add(viewItem);
		rightClickMenu.add(editItem);
		rightClickMenu.add(paidItem);
		rightClickMenu.add(deleteItem);
		
		Font menuFont = new Font(null,Font.PLAIN,16);
		viewItem.setFont(menuFont);
		editItem.setFont(menuFont);
		paidItem.setFont(menuFont);
		deleteItem.setFont(menuFont);
		
		// For showing the right click menu
		actualTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				// If a row isn't selected, the menu won't show
				if (actualTable.getSelectedRow() == -1)	actualTable.setComponentPopupMenu(null);
				
				// if a row is selected, the menu does show
				else actualTable.setComponentPopupMenu(rightClickMenu);
			}
		});
		
		// What happens when viewItem is clicked
		viewItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selRow = actualTable.getSelectedRow();
				new ViewGUI(listOfBills.get(selRow));			
				}
		});
		
		// What happens when editItem is clicked
		editItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		
		// What happens when paidItem is clicked
		paidItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selRow = actualTable.getSelectedRow();
				
				if (listOfBills.get(selRow).getPaid() == true) {
					listOfBills.get(selRow).setPaid(false);
					actualTable.getModel().setValueAt("No", selRow, 4);
				}
				else {
					listOfBills.get(selRow).setPaid(true);
					actualTable.getModel().setValueAt("Yes", selRow, 4);					
				}
				
				actualTable.repaint(); // updates table
			}
		});
		
		// What happens when deleteItem is clicked
		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selRow = actualTable.getSelectedRow();
				
				// warning message label
				deleteWarning.setText("Are you sure you want to delete "+listOfBills.get(selRow).getType()+"?");
				deleteWarning.setFont(menuFont);
				
				int option = JOptionPane.showConfirmDialog(panel, deleteWarning);
			
				if (option == JOptionPane.OK_OPTION) {
					listOfBills.remove(selRow);
					((DefaultTableModel) actualTable.getModel()).removeRow(selRow);
					actualTable.repaint();
				}
			}
		});
	}
	
	// Lets MainGUI access this panel of GUI
	public JPanel getPanel() {
		return panel;
	}
	
	// Fills in the table data
	private String[][] fillTable(LinkedList<Bill> listOfBills) {
		int listLength = listOfBills.size();
		
		String[][] tableData = new String[listLength][5]; // [rows][columns]
		
		for (int i = 0; i < listLength; i++) {
			tableData[i] = listOfBills.get(i).getArray();
		}
		
		return tableData;
	}
	
}
