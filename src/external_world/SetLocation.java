package external_world;

import java.awt.EventQueue;
import java.awt.Font;
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
public class SetLocation extends JFrame {

	private JPanel contentPane;
	private Statement _statement;
	private ResultSet _rs;
	private Connection _conn;
	private JList _list;
	final JFrame frameSetLoc = this;
	private JTextField _fieldLoc;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SetLocation frame = new SetLocation();
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
	public SetLocation() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 489, 333);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		setTitle("Set location of products");
		
		JLabel lblNewLabel = new JLabel("Select Item:");
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		lblNewLabel.setBounds(10, 47, 98, 19);
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

		JLabel lblQuantity = new JLabel("Location:");
		lblQuantity.setFont(new Font("Dialog", Font.BOLD, 12));
		lblQuantity.setBounds(11, 197, 81, 19);
		contentPane.add(lblQuantity);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new ExternalWorld().setVisible(true);
			}
		});
		btnBack.setBounds(374, 261, 89, 23);
		contentPane.add(btnBack);
		
		_fieldLoc = new JTextField();
		_fieldLoc.setBounds(110, 194, 117, 25);
		contentPane.add(_fieldLoc);
		_fieldLoc.setColumns(10);
		
		JButton btnNewButton = new JButton("Insert");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = _list.getSelectedIndex();
				String stockNum = (String) _list.getSelectedValue();
				updateLoc(stockNum);
				
				JOptionPane.showMessageDialog(frameSetLoc,
						"Location updated!",
						"",
						JOptionPane.INFORMATION_MESSAGE);
				
			}
		});
		btnNewButton.setBounds(110, 224, 117, 25);
		contentPane.add(btnNewButton);

		_list.setVisible(true);

		
	}

	protected void updateLoc(String stockNum) {
		// TODO Auto-generated method stub
		String query = "UPDATE edepot_Products SET location = '"+_fieldLoc.getText()+"' WHERE stockno = '"+stockNum+"'";
		
		try {
			Statement stmt = _conn.createStatement();
			stmt.executeUpdate(query);
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

		String query = "SELECT stockno "
				+ "FROM eDepot_Products "
				+ "WHERE location IS NULL";

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
