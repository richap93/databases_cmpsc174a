package external_world;

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

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import database_Initialization.DB_Ini_Main;
import java.awt.Font;

/**
 * @author Richa Patel
 *Date: 03/12/2014
 */
public class CheckItem extends JFrame {

	private JPanel contentPane;

	private Statement _statement;
	private ResultSet _rs;
	private Connection _conn;
	private String _qty;
	private JList _list;
	private JEditorPane _editorPane;
	final JFrame frameCheckQty = this;

	
	private DefaultListModel _listModel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CheckItem frame = new CheckItem();
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
	public CheckItem() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 489, 333);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		setTitle("Check for an item");

		JLabel lblNewLabel = new JLabel("Select Item:");
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		lblNewLabel.setBounds(10, 47, 106, 19);
		contentPane.add(lblNewLabel);

		JScrollPane listScrollPane = new JScrollPane();

		String[] itemList =  setListData();
		_list = new JList(itemList);
		_list.setFont(new Font("Dialog", Font.PLAIN, 12));
		_list.setVisibleRowCount(8);
		_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_list.setSelectedIndex(0);
		JScrollPane scrollPane = new JScrollPane(_list);
		scrollPane.setLocation(110, 50);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setSize(258, 130);
		getContentPane().add(scrollPane);

		JLabel lblQuantity = new JLabel("Quantity:");
		lblQuantity.setFont(new Font("Dialog", Font.BOLD, 12));
		lblQuantity.setBounds(10, 227, 81, 19);
		contentPane.add(lblQuantity);

		_editorPane = new JEditorPane();
		_editorPane.setText("");
		_editorPane.setBounds(110, 226, 106, 20);
		contentPane.add(_editorPane);

		JButton btnSelect = new JButton("Select");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = _list.getSelectedIndex();
				String stockNum = (String) _list.getSelectedValue();
				_qty = getQty(stockNum);
				_editorPane.setText(_qty);
			}
		});
		btnSelect.setBounds(110, 191, 89, 23);
		contentPane.add(btnSelect);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new ExternalWorld().setVisible(true);
			}
		});
		btnBack.setBounds(374, 261, 89, 23);
		contentPane.add(btnBack);

		_list.setVisible(true);


	}

	private String getQty(String stock) {
		// TODO Auto-generated method stub

		String query = "SELECT quantity FROM edepot_Products WHERE stockno = ?";
		
		try {
			PreparedStatement stmt = _conn.prepareStatement(query);
			stmt.setString(1, stock);
			_rs = stmt.executeQuery();
			while (_rs.next()) {
				_qty = _rs.getString("quantity");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return _qty;
	}

	private String[] setListData() {

		ArrayList<String> _items = new ArrayList<String>();
		String[] _itemList = {""};
		
		String strConn = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
		String strUsername = "dschuerhoff";
		String strPassword = "X280715";

		String query = "SELECT stockno "
				+ "FROM eDepot_Products";

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
