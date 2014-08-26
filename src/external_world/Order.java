package external_world;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;

/**
 * @author Richa Patel
 *Date: 03/12/2014
 */
public class Order extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTable table, table2;
	private JScrollPane scrollPane, scrollPane2;
	private JFrame orderFrame = this;
	private Connection _conn;
	private Statement _statement;
	private ResultSet _rs;
	private JFrame frameOrder = this;
	private String orderNum;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Order frame = new Order();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Order() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 489, 333);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		setTitle("Fill order");

		JLabel lblEnterTheOrder = new JLabel("Please enter the order ID:");
		lblEnterTheOrder.setBounds(22, 44, 200, 15);
		contentPane.add(lblEnterTheOrder);

		textField = new JTextField();
		textField.setBounds(218, 42, 140, 19);
		contentPane.add(textField);
		textField.setColumns(10);

		JLabel lblItemsSold = new JLabel("Items sold:");
		lblItemsSold.setBounds(22, 71, 90, 15);
		contentPane.add(lblItemsSold);

		table = new JTable();

		table.setRowSelectionAllowed(false);
		table.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));

		table.setBounds(22, 117, 400, 80);

		scrollPane = new JScrollPane(table);
		scrollPane.setToolTipText("");
		scrollPane.setLocation(22, 88);
		scrollPane.setSize(400, 100);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(scrollPane);

		JButton btnConfirm = new JButton("Confirm");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog(null, "Would you like to confirm this order?", "Order comfirmation", JOptionPane.YES_NO_OPTION);
				if(dialogResult == JOptionPane.YES_OPTION){
					adjustInventory();
					checkInventoryLevels();
				}
			}
		});
		btnConfirm.setBounds(22, 197, 117, 25);
		contentPane.add(btnConfirm);

		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				new ExternalWorld().setVisible(true);
			}
		});
		btnBack.setBounds(352, 260, 117, 25);
		contentPane.add(btnBack);

		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String orderNo = textField.getText();
				if (orderNo.equals("")) {
					JOptionPane.showMessageDialog(orderFrame,
							"Please enter an order ID.",
							"Error",
							JOptionPane.WARNING_MESSAGE);
				} else {
					orderNum = textField.getText();
					scrollPane.setVisible(false);
					getOrderItems();
				}

			}
		});
		btnOk.setBounds(362, 41, 60, 20);
		contentPane.add(btnOk);}

	protected void checkInventoryLevels() {
		// TODO Auto-generated method stub

	}

	protected void adjustInventory() {
		// TODO Auto-generated method stub
		try {

			Statement items = _conn.createStatement();
			_rs = items.executeQuery("SELECT * FROM eMart_OrderContents WHERE orderID = '"+orderNum+"'");

			while (_rs.next()) {

				String query = "SELECT quantity, manufacturer, min, max FROM eDepot_Products WHERE stockno = ?";

				ResultSet rsCurQty;
				String stockno = _rs.getString("stockno");

				PreparedStatement stmt = _conn.prepareStatement(query);
				stmt.setString(1, stockno);
				rsCurQty = stmt.executeQuery();

				int curQty = -1;
				int minQty = -1;
				String manu = null;

				while (rsCurQty.next()) {
					curQty = rsCurQty.getInt("quantity");
					manu = rsCurQty.getString("manufacturer");
					minQty = rsCurQty.getInt("min");
				}

				int qtySold = _rs.getInt("quantity");

				stmt = _conn.prepareStatement("UPDATE eDepot_Products SET quantity = '"+(curQty-qtySold)+"' WHERE stockno = '"+stockno+"'");

				stmt.executeUpdate();

				if ((curQty-qtySold) < minQty) {

					String update = "UPDATE eDepot_Manufacturers SET lowproducts = lowproducts+1 WHERE name = '"+manu+"'";
					stmt = _conn.prepareStatement(update);
					stmt.executeUpdate();

					String lowOnProducts = "SELECT lowproducts FROM eDepot_Manufacturers WHERE name='"+manu+"'";
					Statement lowProds = _conn.createStatement();
					ResultSet numLow = lowProds.executeQuery(lowOnProducts);

					int low = 0;

					while (numLow.next()) {
						low = numLow.getInt("lowproducts");
					}

					lowProds.close();

					if (low == 3) {

						Statement s =  _conn.createStatement();
						String listStock = "SELECT * FROM eDepot_Products WHERE manufacturer = '"+manu+"'";
						ResultSet r = s.executeQuery(listStock);

						String pidQuery = "SELECT MAX(pid) FROM eDepot_ShipmentProcess";
						String oidQuery = "SELECT MAX(orderno) FROM eDepot_ShipmentProcess";

						int pid =  generateID(pidQuery);
						int oid = generateID(oidQuery);

						int prodOrdered = 0;
						boolean orderAdded = false;

						while (r.next()) {
							
							String model = r.getString("modelno");
							int qty = r.getInt("quantity");
							int min = r.getInt("min");
							int max = r.getInt("max");
							int rm = r.getInt("replenishment");

							Statement sInsert =  _conn.createStatement();

							if ((qty < min) && (rm == 0)) {

								if (!orderAdded) {
									
									String queryInsertOrder = "insert into eDepot_ShipmentProcess "
											+ "values('"+String.format("%06d",pid)+"', '"+String.format("%06d",oid)+"', null, 'Ordered', '"+manu+"')";

									Statement sInsertOrder =  _conn.createStatement();
									sInsertOrder.executeUpdate(queryInsertOrder);
									
									orderAdded = true;

									sInsertOrder.close();

								}

								String queryInsert = "insert into eDepot_ShipmentContents values('"+String.format("%06d",pid)+"', '"+model+"', "+(max-qty)+")";
								sInsert.executeUpdate(queryInsert);
								prodOrdered++;
							}

							sInsert.close();
							
						}
						
						JOptionPane.showMessageDialog(this, 
								"Automatic order sent to "+manu.trim()+". Order ID is: "+String.format("%06d",oid), "Order sent", JOptionPane.OK_OPTION);
					
					}
					
				}

				stmt.close();

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private int generateID(String query) {

		int maxID = 1;

		try {
			Statement s = _conn.createStatement();
			_rs = s.executeQuery (query);

			while (_rs.next()) {

				if (_rs.getString(1) != null) {
					maxID = Integer.parseInt(_rs.getString(1).trim()) + 1;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return maxID;	
	}

	protected void getOrderItems() {

		String strConn = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
		String strUsername = "dschuerhoff";
		String strPassword = "X280715";

		String query = "SELECT * FROM eMart_OrderContents WHERE orderID = '"+orderNum+"'";

		try {

			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			_conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			PreparedStatement stmt = _conn.prepareStatement(query);
			_rs = stmt.executeQuery();

			int size = 0;
			while (_rs.next()) {
				size++;
			}

			Object[][] tableContents = new Object[size][2];

			_rs = stmt.executeQuery();


			int pos = 0;
			while (_rs.next()) {
				tableContents[pos][0] = _rs.getString("stockno");
				tableContents[pos][1] = _rs.getInt("quantity");
				pos++;
			}


			table2 = new JTable();

			table2.setModel(new DefaultTableModel(tableContents,new String[] {"Product", "Quantity"}));

			table2.setRowSelectionAllowed(false);
			table2.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));

			table2.setBounds(22, 117, 400, 80);
			scrollPane2 = new JScrollPane(table2);
			scrollPane2.setToolTipText("");
			scrollPane2.setLocation(22, 88);
			scrollPane2.setSize(400, 100);
			scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			table2.getColumnModel().getColumn(0).setPreferredWidth(300);
			contentPane.add(scrollPane2);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}
