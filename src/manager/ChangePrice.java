package manager;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
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
public class ChangePrice extends JFrame {

	private JPanel contentPane;
	private JList _list;
	private Statement _statement;
	private ResultSet _rs;
	private Connection _conn;
	private JLabel lblCurrentPrice;
	private JLabel lblNewPrice;
	private JLabel lblCurPrice;
	private JTextField newPrice;
	private JButton btnConfirm;
	private JButton btnBack;
	private JButton btnSelect;
	private String stockNum;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChangePrice frame = new ChangePrice();
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
	public ChangePrice() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 510, 340);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.setTitle("Change price of item");
		
		JLabel lblNewLabel = new JLabel("Select Item:");
		lblNewLabel.setBounds(10, 47, 114, 15);
		contentPane.add(lblNewLabel);
		
		JScrollPane listScrollPane = new JScrollPane();

		String[] itemList =  setListData();
		_list = new JList(itemList);
		_list.setVisibleRowCount(8);
		_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_list.setSelectedIndex(0);
		JScrollPane scrollPane = new JScrollPane(_list);
		scrollPane.setLocation(102, 45);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setSize(258, 130);
		getContentPane().add(scrollPane);
		
		lblCurrentPrice = new JLabel("Current price:");
		lblCurrentPrice.setBounds(10, 191, 112, 20);
		contentPane.add(lblCurrentPrice);
		
		lblNewPrice = new JLabel("New price:");
		lblNewPrice.setBounds(10, 215, 112, 20);
		contentPane.add(lblNewPrice);
		
		lblCurPrice = new JLabel("");
		lblCurPrice.setBounds(120, 194, 70, 15);
		contentPane.add(lblCurPrice);
		
		newPrice= new JTextField();
		newPrice.setBounds(120, 216, 114, 19);
		contentPane.add(newPrice);
		newPrice.setColumns(10);
		
		btnConfirm = new JButton("Confirm");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String priceNew = newPrice.getText();
				updatePrice(priceNew);
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
		
		btnSelect = new JButton("Select");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = _list.getSelectedIndex();
				stockNum = (String) _list.getSelectedValue();
				Float price = getPrice(stockNum);
				lblCurPrice.setText(""+price);
			}
		});
		btnSelect.setBounds(372, 150, 117, 25);
		contentPane.add(btnSelect);

		
	}


	protected void updatePrice(String priceNew) {
		// TODO Auto-generated method stub
		String query = "UPDATE eMart_Items SET price="+priceNew+" WHERE stockno = '"+stockNum+"'";
		
		try {
			_statement.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	protected float getPrice(String stockNum) {
		// TODO Auto-generated method stub
		String query = "SELECT price FROM eMart_Items WHERE stockno = '"+stockNum+"'";
		
		float price = (float) 0;
		
		try {
			_rs = _statement.executeQuery(query);
			while (_rs.next()) {
				price = _rs.getFloat("price");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return price;
	}

	private String[] setListData() {
		// TODO Auto-generated method stub
		ArrayList<String> _items = new ArrayList<String>();
		String[] _itemList = {""};
		
		String strConn = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
		String strUsername = "dschuerhoff";
		String strPassword = "X280715";

		String query = "SELECT stockno "
				+ "FROM eMart_Items";

		try {
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			_conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			_statement = _conn.createStatement();
			_rs = _statement.executeQuery (query);

			while (_rs.next()) {
				_items.add(_rs.getString("stockno"));
			}

			_itemList = new String[_items.size()];
			_itemList = _items.toArray(_itemList);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return _itemList;
	}

}
