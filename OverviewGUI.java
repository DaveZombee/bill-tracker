import java.awt.Font;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.time.LocalDate;

import net.miginfocom.swing.MigLayout;

public class OverviewGUI {

	private JPanel panel = new JPanel();

	// Things for table (these are created later)
	private JTable actualTable;
	private JScrollPane billTable;
	private DefaultTableModel model;
	private String[] columnHeaders = { "Type", "Notes", "Pay period", "Due date", "Paid" }; // Table column headings

	// Menu which appears on right clicking a row
	private JPopupMenu rightClickMenu = new JPopupMenu();

	private JMenu sortMenu = new JMenu("Sort");
	private JMenuItem ascType = new JMenuItem("Ascending type"); // first letter first
	private JMenuItem descType = new JMenuItem("Descending type"); // last letter first
	private JMenuItem nearDate = new JMenuItem("Nearest due date"); // nearest due date first
	private JMenuItem lateDate = new JMenuItem("Latest due date"); // latest due date first
	private JMenuItem resetSort = new JMenuItem("Reset to default"); // resets back to order of added

	private JMenuItem viewItem = new JMenuItem("View");
	private JMenuItem editItem = new JMenuItem("Edit");
	private JMenuItem paidItem = new JMenuItem("Toggle paid");
	private JMenuItem deleteItem = new JMenuItem("Delete");

	private JLabel dialogText = new JLabel();

	// This list is used for sorting purposes
	private LinkedList<Bill> sortListOfBills = new LinkedList<Bill>(); // temporary list of bills

	// Constructor for the overview GUI
	public OverviewGUI(LinkedList<Bill> listOfBills) {
		// Creating the JTable here because it needs a linked list
		model = new DefaultTableModel(fillTable(listOfBills), columnHeaders); // so that a row can be deleted from table
		actualTable = new JTable(model);
		billTable = new JScrollPane(actualTable); // Lets the column headings can be seen

		// Table settings
		actualTable.setDefaultEditor(Object.class, null); // table can't be edited
		actualTable.getTableHeader().setReorderingAllowed(false); // columns can't be dragged
		actualTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // only one row can be selected
		actualTable.getTableHeader().setResizingAllowed(false); // cannot resize the columns
		actualTable.setRowHeight(actualTable.getRowHeight() + 10); // making the row size bigger
		actualTable.setFocusable(false); // can't select a single cell

		// Setting the font sizes
		actualTable.setFont(new Font(null, Font.PLAIN, 16)); // Sets font of the info in the table
		actualTable.getTableHeader().setFont(new Font(null, Font.PLAIN, 18)); // Sets font of the table headers

		// Layout and adding table
		panel.setLayout(new MigLayout());
		panel.add(billTable, "width 595!, height 200!, align center");
		// (it's the JScrollPane that's being added, but that's how it works)

		// Things for the right click menu
		sortMenu.add(ascType);
		sortMenu.add(descType);
		sortMenu.add(nearDate);
		sortMenu.add(lateDate);
		sortMenu.add(resetSort);

		rightClickMenu.add(sortMenu);
		rightClickMenu.add(viewItem);
		rightClickMenu.add(editItem);
		rightClickMenu.add(paidItem);
		rightClickMenu.add(deleteItem);

		Font menuFont = new Font(null, Font.PLAIN, 16);
		sortMenu.setFont(menuFont);
		ascType.setFont(menuFont);
		descType.setFont(menuFont);
		nearDate.setFont(menuFont);
		lateDate.setFont(menuFont);

		viewItem.setFont(menuFont);
		editItem.setFont(menuFont);
		paidItem.setFont(menuFont);
		deleteItem.setFont(menuFont);
		resetSort.setFont(menuFont);

		dialogText.setFont(menuFont);

		actualTable.setComponentPopupMenu(rightClickMenu); // sets menu to be viewable

		//////////////////////////////
		// right click menu actions //
		//////////////////////////////

		viewItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selRow = actualTable.getSelectedRow();

				if (selRow == -1) { // if a bill isn't selected
					dialogText.setText("You haven't selected a bill to view. Try again.");
					JOptionPane.showMessageDialog(panel, dialogText);
				}

				else {
					new ViewGUI(findBill(listOfBills, selRow));
				}

			}
		});

		editItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selRow = actualTable.getSelectedRow();

				if (selRow == -1) { // if a bill isn't selected
					dialogText.setText("You haven't selected a bill to edit. Try again.");
					JOptionPane.showMessageDialog(panel, dialogText);
				}

				else {
					Bill selectedBill = findBill(listOfBills, selRow);
					EditGUI editGUI = new EditGUI(selectedBill);

					// When the edit frame is closed
					editGUI.getFrame().addWindowListener(new WindowAdapter() {
						public void windowClosing(WindowEvent e) {
							reloadTable(listOfBills);
						}
					});
				}
			}
		});

		paidItem.addActionListener(new ActionListener() { // this doesn't work :(
			public void actionPerformed(ActionEvent e) {
				int selRow = actualTable.getSelectedRow();

				if (selRow == -1) { // if a bill isn't selected
					dialogText.setText("You haven't selected a bill to toggle. Try again.");
					JOptionPane.showMessageDialog(panel, dialogText);
				}

				else {
					Bill selectedBill = findBill(listOfBills, selRow);

					if (selectedBill.getPaid() == true)
						selectedBill.setPaid(false);
					else
						selectedBill.setPaid(true);

					reloadTable(listOfBills);
				}
			}
		});

		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selRow = actualTable.getSelectedRow();

				if (selRow == -1) { // if a bill isn't selected
					dialogText.setText("You haven't selected a bill to delete. Try again.");
					JOptionPane.showMessageDialog(panel, dialogText);
				}

				else {
					Bill selectedBill = findBill(listOfBills, selRow);
					// warning message label
					dialogText.setText("Are you sure you want to delete " + selectedBill.getType() + "?");

					int option = JOptionPane.showConfirmDialog(panel, dialogText);

					if (option == JOptionPane.OK_OPTION) {
						listOfBills.remove(selectedBill);
						// if table is sorted, also remove it from the sortList
						if (sortListOfBills.isEmpty() == false)
							sortListOfBills.remove(selectedBill);
						reloadTable(listOfBills);
					}
				}
			}
		});

		////////////////////////
		// Table sorter items //
		////////////////////////

		ascType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				typeSorter(listOfBills, 0);
			}
		});

		descType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				typeSorter(listOfBills, 1);
			}
		});
		
		nearDate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dueDateSorter(listOfBills,0);
			}
		});
		
		lateDate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dueDateSorter(listOfBills,1);
			}
		});

		resetSort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sortListOfBills.clear();
				reloadTable(listOfBills); // the default table model
			}
		});

	}

	// Lets MainGUI access this panel of GUI
	public JPanel getPanel() {
		return panel;
	}

	// Provides a 2d array of the listOfBills to create the table model
	private String[][] fillTable(LinkedList<Bill> listOfBills) {
		int listLength = listOfBills.size();

		String[][] tableData = new String[listLength][5]; // [rows][columns]

		for (int i = 0; i < listLength; i++) {
			tableData[i] = listOfBills.get(i).getArray();
		}

		return tableData;
	}

	// Reloads table with updated info
	public void reloadTable(LinkedList<Bill> listOfBills) {
		if (sortListOfBills.isEmpty() == true) { // if the table is not sorted by anything
			model = new DefaultTableModel(fillTable(listOfBills), columnHeaders);
		}

		else { // if it is sorted
			model = new DefaultTableModel(fillTable(sortListOfBills), columnHeaders);
		}

		actualTable.setModel(model);
	}

	// finds where the location of bill in relation to the linkedlist is if the table has been sorted
	private Bill findBill(LinkedList<Bill> listOfBills, int selRow) {
		Bill returnBill = null;

		// if the temp linked list is empty meaning the table hasn't been sorted
		if (sortListOfBills.size() == 0) {
			returnBill = listOfBills.get(selRow);
		}
		
		// finding the bill
		else {
			int selPos = listOfBills.indexOf(sortListOfBills.get(selRow)); // find its position
			returnBill = listOfBills.get(selPos); // get the bill
		}

		return returnBill;
	}

	// Sorts the type (selection sort)
	private void typeSorter(LinkedList<Bill> listOfBills, int type) {
		sortListOfBills = (LinkedList<Bill>) listOfBills.clone(); // clone of listOfBills
		int listLength = sortListOfBills.size();

		for (int i = 0; i < listLength - 1; i++) {
			int indexPos = i;

			for (int j = i + 1; j < listLength; j++) {
				String tempType1 = sortListOfBills.get(j).getType();
				String tempType2 = sortListOfBills.get(indexPos).getType();

				if (type == 0) { // ascending order
					if (tempType1.compareTo(tempType2) < 0)
						indexPos = j;
				}

				else if (type == 1) { // descending order
					if (tempType1.compareTo(tempType2) > 0)
						indexPos = j;
				}
			}

			Bill tempBill = sortListOfBills.get(indexPos);
			sortListOfBills.set(indexPos, sortListOfBills.get(i));
			sortListOfBills.set(i, tempBill);
		}

		// Reloads the table with the temporarily sorted list of bills
		reloadTable(listOfBills);
	}

	// Sorts the due date (selecton sort)
	private void dueDateSorter(LinkedList<Bill> listOfBills, int type) {
		sortListOfBills = (LinkedList<Bill>) listOfBills.clone(); // clones list of bills
		int listLength = sortListOfBills.size();

		for (int i = 0; i < listLength - 1; i++) {
			int indexPos = i;

			for (int j = i + 1; j < listLength; j++) {
				LocalDate tempDate1 = sortListOfBills.get(j).getDueDate();
				LocalDate tempDate2 = sortListOfBills.get(indexPos).getDueDate();

				if(type == 0) { // soonest due date
					if (tempDate1.isBefore(tempDate2)) indexPos = j; 
				}
				
				if (type == 1) { // latest due date
					if(tempDate1.isAfter(tempDate2)) indexPos = j;
				}
			}
			Bill tempBill = sortListOfBills.get(indexPos);
			sortListOfBills.set(indexPos, sortListOfBills.get(i));
			sortListOfBills.set(i,tempBill);
		}
		
		// Reloads table with the temp sorted list of bills
		reloadTable(listOfBills);
	}
}
