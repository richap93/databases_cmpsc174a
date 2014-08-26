package manager;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

/**
 * @author Richa Patel
 *Date: 03/12/2014
 */
public class AdjustCustStatus extends JFrame {

	private JPanel contentPane;
	private JList _list;
	private Statement _statement;
	private ResultSet _rs;
	private Connection _conn;
	private JLabel lblCurrentStatus, lblNewStatus, lblCurStatus;
	private JTextField newStatus;
	private JButton btnConfirm, btnBack;
	private String custID;
	private JFrame frameAdjustStat = this;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdjustCustStatus frame = new AdjustCustStatus();
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
	public AdjustCustStatus() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 510, 340);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.setTitle("Adjust Customer Status");
		
		JLabel lblNewLabel = new JLabel("Select Customer:");
		lblNewLabel.setBounds(10, 47, 147, 15);
		contentPane.add(lblNewLabel);
		
		JScrollPane listScrollPane = new JScrollPane();

		String[] itemList =  setListData();
		_list = new JList(itemList);
		_list.setVisibleRowCount(8);
		_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_list.setSelectedIndex(0);
		JScrollPane scrollPane = new JScrollPane(_list);
		scrollPane.setLocation(146, 45);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setSize(258, 130);
		getContentPane().add(scrollPane);
		
		JButton button = new JButton("Select");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = _list.getSelectedIndex();
				custID = (String) _list.getSelectedValue();
				String status = getStatus(custID);
				lblCurStatus.setText(""+status);
			}
		});
		button.setBounds(414, 150, 80, 25);
		contentPane.add(button);
		
		lblCurrentStatus = new JLabel("Current status:");
		lblCurrentStatus.setBounds(10, 191, 112, 20);
		contentPane.add(lblCurrentStatus);
		
		lblNewStatus = new JLabel("New status:");
		lblNewStatus.setBounds(10, 215, 112, 20);
		contentPane.add(lblNewStatus);
		
		lblCurStatus = new JLabel("");
		lblCurStatus.setBounds(130, 194, 70, 15);
		contentPane.add(lblCurStatus);
		
		newStatus= new JTextField();
		newStatus.setBounds(130, 216, 114, 19);
		contentPane.add(newStatus);
		newStatus.setColumns(10);
		
		btnConfirm = new JButton("Confirm");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String statusNew = newStatus.getText();
				updateStatus(statusNew);
			}
		});
		btnConfirm.setBounds(10, 247, 117, 25);
		contentPane.add(btnConfirm);
		
		btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new ManagerHome().setVisible(true);
			}
		});
		btnBack.setBounds(373, 271, 117, 25);
		contentPane.add(btnBack);

		
	}

	protected String getStatus(String custID2) {
		// TODO Auto-generated method stub
		String query = "SELECT status FROM eMart_Customers WHERE id = '"+custID2+"'";
		
		String status = "";
		
		try {
			_rs = _statement.executeQuery(query);
			while (_rs.next()) {
				int stat = _rs.getInt("status");
				if (stat == 1) {
					status = "New";
				} else if (stat == 2) {
					status = "Green";
				} else if (stat == 3) {
					status = "Silver";
				} else if (stat == 4){
					status = "Gold";
				} else {
					JOptionPane.showMessageDialog(frameAdjustStat,
							"Invalid customer status!",
							"Error",
							JOptionPane.OK_OPTION);
				}
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return status;	
		
	}

	protected void updateStatus(String statusNew) {
		// TODO Auto-generated method stub
		int statusInt = 1;
		if (statusNew.equals("New")) {
			statusInt = 1;
		} else if (statusNew.equals("Green")) {
			statusInt = 2;
		} else if (statusNew.equals("Silver")) {
			statusInt = 3;
		} else if (statusNew.equals("Gold")){
			statusInt = 4;
		}
		
		String query = "UPDATE eMart_Customers SET status="+statusInt+" WHERE id = '"+custID+"'";
		
		try {
			_statement.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String[] setListData() {
		// TODO Auto-generated method stub
		ArrayList<String> _items = new ArrayList<String>();
		String[] _itemList = {""};
		
		String strConn = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
		String strUsername = "dschuerhoff";
		String strPassword = "X280715";

		String query = "SELECT id "
				+ "FROM eMart_Customers";

		try {
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			_conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			_statement = _conn.createStatement();
			_rs = _statement.executeQuery (query);

			while (_rs.next()) {
				_items.add(_rs.getString("id"));
			}

			_itemList = new String[_items.size()];
			_itemList = _items.toArray(_itemList);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return _itemList;	}

}
