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
public class ReceiveShipment extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTable table;
	private JScrollPane scrollPane;
	private Connection _conn;
	private JFrame frameReceiveShipment = this;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReceiveShipment frame = new ReceiveShipment();
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
	public ReceiveShipment() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 489, 333);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		setTitle("Receive shipment");

		JLabel lblEnterTheOrder = new JLabel("Please enter the shipping notice ID:");
		lblEnterTheOrder.setBounds(30, 34, 255, 15);
		contentPane.add(lblEnterTheOrder);
		
		textField = new JTextField();
		textField.setBounds(290, 32, 140, 19);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String snID=textField.getText();
				displayProducts(snID);
			}
		});
		btnOk.setBounds(376, 54, 54, 19);
		contentPane.add(btnOk);
		
		JButton button = new JButton("Back");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new ExternalWorld().setVisible(true);
			}
		});
		button.setBounds(352, 264, 117, 25);
		contentPane.add(button);
		
		JButton btnNewButton = new JButton("Confirm");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateDatabase();
				JOptionPane.showMessageDialog(frameReceiveShipment,
						"Shipment confirmed.",
						"",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnNewButton.setBounds(30, 264, 117, 25);
		contentPane.add(btnNewButton);

		
	}

	protected void updateDatabase() {
		// TODO Auto-generated method stub
		
		String manu = "";
		String modelno = "";
		int quantity = 0;
		int curQty;
		int newQty = 0;
		
		String query = "SELECT SP.manufacturer FROM eDepot_ShipmentProcess SP, eDepot_ShipmentContents SC "
				+ "WHERE SP.snotice ='"+textField.getText()+"' AND SP.pid=SC.pid";
		
		try {
			PreparedStatement stmt = _conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();	
			
			while (rs.next()) {
				manu = rs.getString("manufacturer");
			}
		
			query = "SELECT SC.modelno, SC.quantity "
					+ "FROM eDepot_ShipmentContents SC, eDepot_ShipmentProcess SP "
					+ "WHERE SP.snotice = '"+textField.getText()+"' AND SP.pid=SC.pid";
			
			stmt = _conn.prepareStatement(query);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				modelno = rs.getString("modelno");
				quantity = rs.getInt("quantity");
				
				query = "SELECT quantity, location FROM eDepot_Products WHERE modelno='"+modelno+"' AND manufacturer='"+manu+"'";
				
				stmt = _conn.prepareStatement(query);
				ResultSet qty = stmt.executeQuery();
				
				while (qty.next()) {
					curQty = qty.getInt("quantity");
					
					newQty = quantity+curQty;
					
					String q = "UPDATE eDepot_Products "
							+ "SET replenishment = 0, quantity = "+newQty+" WHERE modelno = '"+modelno+"' AND manufacturer = '"+manu+"'";

					stmt.executeUpdate(q);
				}
				
				String q = "UPDATE eDepot_ShipmentProcess "
						+ "SET status = 'Received' WHERE snotice = '"+textField.getText()+"'";
				stmt.executeUpdate(q);
				
			}
//			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	protected void displayProducts(String snID) {
		// TODO Auto-generated method stub
		
		String strConn = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
		String strUsername = "dschuerhoff";
		String strPassword = "X280715";

		String query = "SELECT SC.modelno, SC.quantity "
				+ "FROM eDepot_ShipmentContents SC, eDepot_ShipmentProcess SP "
				+ "WHERE SP.snotice = '"+snID+"' AND SP.pid=SC.pid";

		try {

			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			_conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			PreparedStatement stmt = _conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			
			int size = 0;
			while (rs.next()) {
				size++;
			}

			Object[][] tableContents = new Object[size][2];

			rs = stmt.executeQuery();

			int pos = 0;
			while (rs.next()) {
				tableContents[pos][0] = rs.getString("modelno");
				tableContents[pos][1] = rs.getInt("quantity");
				pos++;
			}
			
			table = new JTable();
	
			table.setModel(new DefaultTableModel(tableContents,new String[] {"Product", "Quantity"}));
	
			table.setRowSelectionAllowed(false);
			table.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
	
			table.setBounds(30, 70, 400, 80);
			scrollPane = new JScrollPane(table);
			scrollPane.setToolTipText("");
			scrollPane.setLocation(30, 90);
			scrollPane.setSize(400, 120);
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			table.getColumnModel().getColumn(0).setPreferredWidth(300);
			contentPane.add(scrollPane);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
