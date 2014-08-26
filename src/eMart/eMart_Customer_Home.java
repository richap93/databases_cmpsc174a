package eMart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class eMart_Customer_Home extends JFrame {
	
	private String UserID="";
	private int date=0;
	

	private JPanel contentPane;
	private JLayeredPane layeredPane;
	private JPanel Home;
	private JPanel Item_Search;
	private JPanel Cart;
	private JPanel Find_Order;
	private JTextField search_stockno;
	private JTextField search_manu;
	private JTextField search_modelno;
	private JTextField search_cat;
	private JButton btnBack;
	private JTextField descr_value;
	private JTextField search_access;
	private JButton btnSearch;
	private JLabel lblMainMenu;
	private JButton btnExit;
	private JTextField SearchToCart;
	private JTextField SearchToCartQuant;
	private JButton btnBack_1;
	private JLabel lblFindOrder;
	private JButton btnBack_2;
	private JTextField findOrderNo;
	private JButton btnFindOrder_1;
	private JTable tableOrderContents;
	private JTextField findOrderTotal;
	private JButton rerunOrder;
	private JTextField CartAdd;
	private JButton btnAddItem;
	private JTextField CartPrice;
	private static Statement stmt;
	private JTextField LoginID;
	private JTextField LoginPW;
	private JPanel Logon;
	private JFrame FrameHome=this;
	private JButton btnLogoff;
	private JButton btnExit_1;
	private JTextField descr_attribute;
	private JTable table;
	private Object[][] tableContents;
	private Object[][] CartContents;
	private JScrollPane scrollPane;
	private JScrollPane OCscrollPane;
	private JScrollPane scrollPane_1;
	private JTable table_Cart;
	private JTextField CartAddQty;
	private JTextField PayPrice;
	private JTextField dateInput;
	private String SearchString;
	private int invalidSearch;
	private int accessorieSearch;
	private String newstatusString;
	private JTextField resultOrderID;
	private JTextField resultOrderCustomer;
	private JTextField resultDate;
	private JTextField resultCStatus;
	private Object[][] OrderContents;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws SQLException{
		
		
		DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
		String strConn = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
		String strUsername = "dschuerhoff";
		String strPassword = "X280715";
		Connection conn = DriverManager.getConnection(strConn,strUsername,strPassword);
		System.out.format("Connection to Database established \n");

		// Create a Statement
		stmt = conn.createStatement();
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					eMart_Customer_Home frame = new eMart_Customer_Home();
					frame.setTitle("eMart_Customer");
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
	public eMart_Customer_Home() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		layeredPane = new JLayeredPane();
		contentPane.add(layeredPane, BorderLayout.CENTER);
		layeredPane.setLayout(null);
		
		Home = new JPanel();
		layeredPane.setLayer(Home, 0);
		Home.setBounds(0, 0, 582, 458);
		layeredPane.add(Home);
		Home.setLayout(null);
		
		JButton btnSearchForItem = new JButton("Search for Item");
		btnSearchForItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				layeredPane.setLayer(Home, 0);
				layeredPane.setLayer(Item_Search, 1);
			}
		});
		btnSearchForItem.setBounds(59, 76, 203, 25);
		Home.add(btnSearchForItem);
		
		JButton btnCart = new JButton("Cart");
		btnCart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				layeredPane.setLayer(Cart, 1);
				layeredPane.setLayer(Home, 0);
				CartContents=Query_to_Table("select n.quantity,n.stockno,i.manufacturer, i.modelno, i.category, i.description,i.price,i.warranty from emart_itemsincart n, emart_items i where n.customerid='"+UserID+"' and i.stockno=n.stockno",8);
				table_Cart.setModel(new DefaultTableModel(CartContents,	new String[] {"Quantity","Stockno", "Manuf", "Modelno", "Categ", "Description", "Unit Prize", "Warranty"}));
				CartPrice.setText(String.valueOf(Calculate_Total()[0]));
				PayPrice.setText(String.valueOf(Calculate_Total()[1]));
			}
		});
		btnCart.setBounds(391, 153, 117, 25);
		Home.add(btnCart);
		
		JButton btnFindOrder = new JButton("Find Order");
		btnFindOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				layeredPane.setLayer(Find_Order, 1);
				layeredPane.setLayer(Home, 0);
			}
		});
		btnFindOrder.setBounds(59, 238, 117, 25);
		Home.add(btnFindOrder);
		
		lblMainMenu = new JLabel("Main Menu");
		lblMainMenu.setBounds(250, 12, 90, 15);
		Home.add(lblMainMenu);
		
		btnExit = new JButton("Exit");
		btnExit.setBackground(UIManager.getColor("Button.background"));
		btnExit.setForeground(Color.BLACK);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnExit.setBounds(391, 309, 117, 25);
		Home.add(btnExit);
		
		btnLogoff = new JButton("LogOff");
		btnLogoff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				layeredPane.setLayer(Home, 0);
				layeredPane.setLayer(Logon, 1);
				
				FrameHome.setTitle("eMart_Customer");
			}
		});
		btnLogoff.setBounds(59, 370, 117, 25);
		Home.add(btnLogoff);
		
		Item_Search = new JPanel();
		layeredPane.setLayer(Item_Search, 0);
		Item_Search.setBounds(0, 0, 582, 458);
		layeredPane.add(Item_Search);
		Item_Search.setLayout(null);
		
		JLabel lblSearchForItems = new JLabel("Search for Items");
		lblSearchForItems.setBounds(229, 12, 150, 15);
		Item_Search.add(lblSearchForItems);
		
		search_stockno = new JTextField();
		search_stockno.setText("StockNo");
		search_stockno.setBounds(40, 51, 114, 19);
		Item_Search.add(search_stockno);
		search_stockno.setColumns(10);
		
		search_manu = new JTextField();
		search_manu.setText("Manufacturer");
		search_manu.setBounds(166, 51, 114, 19);
		Item_Search.add(search_manu);
		search_manu.setColumns(10);
		
		search_modelno = new JTextField();
		search_modelno.setText("ModelNo");
		search_modelno.setBounds(292, 51, 114, 19);
		Item_Search.add(search_modelno);
		search_modelno.setColumns(10);
		
		search_cat = new JTextField();
		search_cat.setText("Category");
		search_cat.setBounds(418, 51, 114, 19);
		Item_Search.add(search_cat);
		search_cat.setColumns(10);
		
		btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				layeredPane.setLayer(Home, 1);
				layeredPane.setLayer(Item_Search, 0);
			}
		});
		btnBack.setBounds(453, 421, 117, 25);
		Item_Search.add(btnBack);
		
		descr_value = new JTextField();
		descr_value.setText("Descr_Value");
		descr_value.setBounds(166, 82, 114, 19);
		Item_Search.add(descr_value);
		descr_value.setColumns(10);
		
		search_access = new JTextField();
		search_access.setText("AccessorieOf");
		search_access.setBounds(40, 82, 114, 19);
		Item_Search.add(search_access);
		search_access.setColumns(10);
		
		
		tableContents=new Object[][] {
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
			};
		
		SearchString="select i.stockno,i.manufacturer,i.modelno,i.category,i.description,i.price,i.warranty from emart_accessorieof a, emart_items i where ";
		invalidSearch=1;
		accessorieSearch=0;
		
		btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(( !search_stockno.getText().equals("")) && ( !search_stockno.getText().equals("StockNo") ) ){
					SearchString=SearchString+"i.stockno='"+search_stockno.getText()+"' and ";
					invalidSearch=0;
				}if(( !search_manu.getText().equals("")) && ( !search_manu.getText().equals("Manufacturer") )){
					SearchString=SearchString+"i.manufacturer='"+search_manu.getText()+"' and ";
					invalidSearch=0;
				}if(( !search_modelno.getText().equals("")) && ( !search_modelno.getText().equals("ModelNo") )){
					SearchString=SearchString+"i.modelno='"+search_modelno.getText()+"' and ";
					invalidSearch=0;
				}if(( !search_cat.getText().equals("")) && ( !search_cat.getText().equals("Category") )){
					SearchString=SearchString+"i.category='"+search_cat.getText()+"' and ";
					invalidSearch=0;
				}if(( !search_access.getText().equals("")) && ( !search_access.getText().equals("AccessorieOf") )){
					SearchString=SearchString+"a.stockno_parent='"+search_access.getText()+"' and a.stockno_child=i.stockno and ";
					accessorieSearch=1;
					invalidSearch=0;
				}if(( !descr_value.getText().equals("")) && ( !descr_value.getText().equals("Descr_Value") )){
					SearchString=SearchString+"i.description like '%"+descr_value.getText()+"%' and ";
					invalidSearch=0;
				}if(( !descr_attribute.getText().equals("")) && ( !descr_attribute.getText().equals("Descr_Attribute") )){
					SearchString=SearchString+"i.description like '%"+descr_attribute.getText()+"%' and ";
					invalidSearch=0;
				}
				if(! (accessorieSearch==1)){
					SearchString = SearchString.replaceAll("emart_accessorieof a, ", "");
				}
				if(invalidSearch==0){
					SearchString = SearchString.substring(0, SearchString.lastIndexOf(" "));
					SearchString = SearchString.substring(0, SearchString.lastIndexOf(" "));
					tableContents=Query_to_Table(SearchString,7);
					table.setModel(new DefaultTableModel(tableContents,	new String[] {"Stockno", "Manuf", "Modelno", "Categ", "Description", "Prize", "Warranty"}));
				}else{
					JOptionPane.showMessageDialog(FrameHome, "Dude - dont hit 'search' when you ain't searching for something!!", "Error", JOptionPane.OK_OPTION);
				}
				SearchString="select i.stockno,i.manufacturer,i.modelno,i.category,i.description,i.price,i.warranty from emart_accessorieof a, emart_items i where ";
				invalidSearch=1;
				accessorieSearch=0;
			}
		});
		
		table = new JTable();
		table.setModel(new DefaultTableModel(tableContents,	new String[] {"Stockno", "Manuf", "Modelno", "Categ", "Description", "Prize", "Warranty"}));
		
		table.setBounds(40, 113, 492, 271);
		scrollPane = new JScrollPane(table);
		scrollPane.setSize(492, 288);
		scrollPane.setLocation(40, 107);
		Item_Search.add(scrollPane);
		btnSearch.setBounds(418, 82, 117, 19);
		Item_Search.add(btnSearch);
		
		SearchToCart = new JTextField();
		SearchToCart.setText("Stock_to_buy");
		SearchToCart.setBounds(40, 424, 114, 19);
		Item_Search.add(SearchToCart);
		SearchToCart.setColumns(10);
		
		SearchToCartQuant = new JTextField();
		SearchToCartQuant.setText("Quantity");
		SearchToCartQuant.setBounds(166, 424, 114, 19);
		Item_Search.add(SearchToCartQuant);
		SearchToCartQuant.setColumns(10);
		
		JButton btnAddToCart = new JButton("Add to Cart");
		btnAddToCart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ItemIntoCart(SearchToCart,SearchToCartQuant);
			}
		});
		btnAddToCart.setBounds(292, 421, 117, 25);
		Item_Search.add(btnAddToCart);
		
		descr_attribute = new JTextField();
		descr_attribute.setText("Descr_Attribute");
		descr_attribute.setBounds(292, 82, 114, 19);
		Item_Search.add(descr_attribute);
		descr_attribute.setColumns(10);
		
		
		Cart = new JPanel();
		layeredPane.setLayer(Cart, 0);
		Cart.setBounds(0, 0, 582, 458);
		layeredPane.add(Cart);
		Cart.setLayout(null);
		
		JLabel lblCart = new JLabel("Cart");
		lblCart.setBounds(275, 12, 30, 15);
		Cart.add(lblCart);
		
		JButton btnCheckout = new JButton("Checkout");
		btnCheckout.setBackground(Color.GREEN);
		btnCheckout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Checkout();
				CartContents=Query_to_Table("select n.quantity,n.stockno,i.manufacturer, i.modelno, i.category, i.description,i.price,i.warranty from emart_itemsincart n, emart_items i where n.customerid='"+UserID+"' and i.stockno=n.stockno",8);
				table_Cart.setModel(new DefaultTableModel(CartContents,	new String[] {"Quantity","Stockno", "Manuf", "Modelno", "Categ", "Description", "Unit Prize", "Warranty"}));
				CartPrice.setText(String.valueOf(Calculate_Total()[0]));
				PayPrice.setText(String.valueOf(Calculate_Total()[1]));
			}
		});
		btnCheckout.setBounds(190, 400, 201, 47);
		Cart.add(btnCheckout);
		
		btnBack_1 = new JButton("BACK");
		btnBack_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				layeredPane.setLayer(Home, 1);
				layeredPane.setLayer(Cart, 0);
			}
		});
		btnBack_1.setBackground(UIManager.getColor("Button.background"));
		btnBack_1.setBounds(440, 421, 117, 25);
		Cart.add(btnBack_1);
		
		CartAdd = new JTextField();
		CartAdd.setText("Stock_to_add/rem");
		CartAdd.setBounds(73, 339, 126, 19);
		Cart.add(CartAdd);
		CartAdd.setColumns(10);
		
		CartContents=new Object[][] {
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
			};
		
		btnAddItem = new JButton("Add/Remove Item");
		btnAddItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ItemIntoCart(CartAdd, CartAddQty);
				CartContents=Query_to_Table("select n.quantity,n.stockno,i.manufacturer, i.modelno, i.category, i.description,i.price,i.warranty from emart_itemsincart n, emart_items i where n.customerid='"+UserID+"' and i.stockno=n.stockno",8);
				table_Cart.setModel(new DefaultTableModel(CartContents,	new String[] {"Quantity","Stockno", "Manuf", "Modelno", "Categ", "Description", "Unit Prize", "Warranty"}));
				CartPrice.setText(String.valueOf(Calculate_Total()[0]));
				PayPrice.setText(String.valueOf(Calculate_Total()[1]));
			}
		});
		btnAddItem.setBounds(365, 336, 172, 25);
		Cart.add(btnAddItem);
		
		CartPrice = new JTextField();
		CartPrice.setText("Total");
		CartPrice.setBounds(41, 400, 114, 19);
		Cart.add(CartPrice);
		CartPrice.setColumns(10);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(26, 33, 544, 278);
		Cart.add(scrollPane_1);
		
		table_Cart = new JTable();
		table_Cart.setModel(new DefaultTableModel(CartContents,	new String[] {"Quantity","Stockno", "Manuf", "Modelno", "Categ", "Description", "Prize", "Warranty"}));
		scrollPane_1.setViewportView(table_Cart);
		
		CartAddQty = new JTextField();
		CartAddQty.setText("Quantity");
		CartAddQty.setBounds(222, 339, 114, 19);
		Cart.add(CartAddQty);
		CartAddQty.setColumns(10);
		
		PayPrice = new JTextField();
		PayPrice.setText("Pay");
		PayPrice.setBounds(41, 428, 114, 19);
		Cart.add(PayPrice);
		PayPrice.setColumns(10);
		
		JButton btnEmptyCart = new JButton("Empty Cart");
		btnEmptyCart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Empty_Cart();
				CartContents=Query_to_Table("select n.quantity,n.stockno,i.manufacturer, i.modelno, i.category, i.description,i.price,i.warranty from emart_itemsincart n, emart_items i where n.customerid='"+UserID+"' and i.stockno=n.stockno",8);
				table_Cart.setModel(new DefaultTableModel(CartContents,	new String[] {"Quantity","Stockno", "Manuf", "Modelno", "Categ", "Description", "Unit Prize", "Warranty"}));
				CartPrice.setText(String.valueOf(Calculate_Total()[0]));
				PayPrice.setText(String.valueOf(Calculate_Total()[1]));
			}
		});
		btnEmptyCart.setBounds(440, 373, 117, 25);
		Cart.add(btnEmptyCart);
		
		Find_Order = new JPanel();
		layeredPane.setLayer(Find_Order, 0);
		Find_Order.setBounds(0, 0, 582, 458);
		layeredPane.add(Find_Order);
		Find_Order.setLayout(null);
		
		lblFindOrder = new JLabel("Find Order");
		lblFindOrder.setBounds(254, 12, 80, 15);
		Find_Order.add(lblFindOrder);
		
		btnBack_2 = new JButton("Back");
		btnBack_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				layeredPane.setLayer(Home, 1);
				layeredPane.setLayer(Find_Order, 0);
			}
		});
		btnBack_2.setBackground(UIManager.getColor("Button.background"));
		btnBack_2.setBounds(434, 401, 117, 25);
		Find_Order.add(btnBack_2);
		
		findOrderNo = new JTextField();
		findOrderNo.setText("000001");
		findOrderNo.setBounds(76, 39, 114, 19);
		Find_Order.add(findOrderNo);
		findOrderNo.setColumns(10);
		
		OrderContents=new Object[][] {
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
			};
		
		btnFindOrder_1 = new JButton("Find Order");
		btnFindOrder_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ResultSet rs;
				try {
					rs = stmt.executeQuery("select * from emart_orders o where o.id='"+findOrderNo.getText()+"'");
					rs.next();
					resultOrderID.setText(rs.getString(1));
					resultOrderCustomer.setText(rs.getString(2));
					resultDate.setText(rs.getString(3));
					if(rs.getString(4).equals("1")){resultCStatus.setText("New");}
					else if(rs.getString(4).equals("2")){resultCStatus.setText("Green");}
					else if(rs.getString(4).equals("3")){resultCStatus.setText("Silver");}
					else if(rs.getString(4).equals("4")){resultCStatus.setText("Gold");}
					OrderContents=Query_to_Table("select oc.quantity,oc.stockno,oc.price,i.price,i.manufacturer, i.modelno, i.category from emart_ordercontents oc, emart_items i where oc.orderid='"+findOrderNo.getText()+"' and i.stockno=oc.stockno",7);
					tableOrderContents.setModel(new DefaultTableModel(OrderContents,	new String[] {"Quantity","Stockno", "Price (at time)", "Price (now)", "Manuf", "Modelno", "Category"}));
					findOrderTotal.setText(Calculate_Total_of_order(findOrderNo.getText()));
					
				} catch (SQLException e1) {
					resultOrderID.setText("Order #");
					resultOrderCustomer.setText("Customer");
					resultDate.setText("Date");
					resultCStatus.setText("CStatus (at the time)");
					OrderContents=new Object[][] {
							{null, null, null, null, null, null, null, null},
							{null, null, null, null, null, null, null, null},
							{null, null, null, null, null, null, null, null},
							{null, null, null, null, null, null, null, null},
							{null, null, null, null, null, null, null, null},
							{null, null, null, null, null, null, null, null},
							{null, null, null, null, null, null, null, null},
						};
					tableOrderContents.setModel(new DefaultTableModel(OrderContents,	new String[] {"Quantity","Stockno", "Price (at time)", "Price (now)", "Manuf", "Modelno", "Category"}));
					findOrderTotal.setText(Calculate_Total_of_order(findOrderNo.getText()));
				}
			}
		});
		btnFindOrder_1.setBounds(230, 39, 117, 25);
		Find_Order.add(btnFindOrder_1);
		
		tableOrderContents = new JTable();
		tableOrderContents.setBounds(61, 115, 458, 234);
		OCscrollPane = new JScrollPane(tableOrderContents);
		OCscrollPane.setSize(511, 282);
		OCscrollPane.setLocation(40, 107);
		Find_Order.add(OCscrollPane);
		tableOrderContents.setModel(new DefaultTableModel(OrderContents,	new String[] {"Quantity","Stockno", "Price (at time)", "Price (now)", "Manuf", "Modelno", "Category"}));

		
		findOrderTotal = new JTextField();
		findOrderTotal.setText("Total");
		findOrderTotal.setBounds(61, 404, 114, 19);
		Find_Order.add(findOrderTotal);
		findOrderTotal.setColumns(10);
		
		rerunOrder = new JButton("Rerun");
		rerunOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Empty_Cart();
				CopyOrderToCart(findOrderNo.getText());
				layeredPane.setLayer(Cart, 1);
				layeredPane.setLayer(Find_Order, 0);
				CartContents=Query_to_Table("select n.quantity,n.stockno,i.manufacturer, i.modelno, i.category, i.description,i.price,i.warranty from emart_itemsincart n, emart_items i where n.customerid='"+UserID+"' and i.stockno=n.stockno",8);
				table_Cart.setModel(new DefaultTableModel(CartContents,	new String[] {"Quantity","Stockno", "Manuf", "Modelno", "Categ", "Description", "Unit Prize", "Warranty"}));
				CartPrice.setText(String.valueOf(Calculate_Total()[0]));
				PayPrice.setText(String.valueOf(Calculate_Total()[1]));
			}
		});
		rerunOrder.setToolTipText("Replaces contents of Cart with these Items");
		rerunOrder.setBounds(230, 401, 117, 25);
		Find_Order.add(rerunOrder);
		
		JLabel lblOrder = new JLabel("Order #");
		lblOrder.setBounds(78, 24, 70, 15);
		Find_Order.add(lblOrder);
		
		resultOrderID = new JTextField();
		resultOrderID.setText("Order #");
		resultOrderID.setBounds(61, 84, 114, 19);
		Find_Order.add(resultOrderID);
		resultOrderID.setColumns(10);
		
		resultOrderCustomer = new JTextField();
		resultOrderCustomer.setText("Customer");
		resultOrderCustomer.setBounds(187, 84, 114, 19);
		Find_Order.add(resultOrderCustomer);
		resultOrderCustomer.setColumns(10);
		
		resultDate = new JTextField();
		resultDate.setText("Date");
		resultDate.setBounds(313, 84, 114, 19);
		Find_Order.add(resultDate);
		resultDate.setColumns(10);
		
		resultCStatus = new JTextField();
		resultCStatus.setText("CStatus (at the time)");
		resultCStatus.setBounds(437, 84, 114, 19);
		Find_Order.add(resultCStatus);
		resultCStatus.setColumns(10);
		
		Logon = new JPanel();
		layeredPane.setLayer(Logon, 1);
		Logon.setBounds(0, 0, 582, 458);
		layeredPane.add(Logon);
		Logon.setLayout(null);
		
		JLabel lblEmartcustomerlogon = new JLabel("eMart_Customer_Logon");
		lblEmartcustomerlogon.setBounds(207, 12, 193, 15);
		Logon.add(lblEmartcustomerlogon);
		
		LoginID = new JTextField();
		LoginID.setText("Rhagrid");
		LoginID.setBounds(207, 126, 114, 19);
		Logon.add(LoginID);
		LoginID.setColumns(10);
		
		LoginPW = new JTextField();
		LoginPW.setText("Rhagrid");
		LoginPW.setBounds(207, 183, 114, 19);
		Logon.add(LoginPW);
		LoginPW.setColumns(10);
		
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String LoginString="select * from eMart_Customers c where c.id='"+LoginID.getText()+"'";
				try {
					ResultSet rs=stmt.executeQuery(LoginString);
					rs.next();
					try {
						if(rs.getString(2).trim().equals(LoginPW.getText())){
							UserID=LoginID.getText();
							date=Integer.parseInt(dateInput.getText());

							layeredPane.setLayer(Home, 1);
							layeredPane.setLayer(Logon, 0);
							
							FrameHome.setTitle("eMart_Customer - User: "+UserID+"   Date: "+date);
						}else{
							JOptionPane.showMessageDialog(FrameHome, "Password incorrect!", "Error", JOptionPane.OK_OPTION);
						};
					} catch (SQLException e1){
						JOptionPane.showMessageDialog(FrameHome, "User does not exist!", "Error", JOptionPane.OK_OPTION);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnLogin.setBounds(207, 232, 117, 25);
		Logon.add(btnLogin);
		
		btnExit_1 = new JButton("Exit");
		btnExit_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnExit_1.setBounds(207, 324, 117, 25);
		Logon.add(btnExit_1);
		
		dateInput = new JTextField();
		dateInput.setText("20140307");
		dateInput.setBounds(207, 48, 114, 19);
		Logon.add(dateInput);
		dateInput.setColumns(10);
	}
	
	
	
	
	private void ItemIntoCart(JTextField searchToCart,JTextField searchToCartQuant) {
		try {
			ResultSet rs=stmt.executeQuery("select * from emart_itemsincart i where i.customerid='"+UserID+"' and i.stockno='"+searchToCart.getText()+"'");
			rs.next();
			try {
				int qtynew=rs.getInt(3)+Integer.parseInt(searchToCartQuant.getText());
				if (qtynew<1){
					stmt.executeUpdate("delete from emart_itemsincart i where i.customerid='"+UserID+"' and i.stockno='"+searchToCart.getText()+"'");
				}else{
					stmt.executeUpdate("update emart_itemsincart i set i.quantity='"+qtynew+"' where i.customerid='"+UserID+"' and i.stockno='"+searchToCart.getText()+"'");
				}
			} catch (SQLException e1){
				if(Integer.parseInt(searchToCartQuant.getText())>0){
					stmt.executeUpdate("insert into emart_itemsincart values('"+UserID+"','"+searchToCart.getText()+"','"+Integer.parseInt(searchToCartQuant.getText())+"')");
				}
			}
		} catch (SQLException e1) {	e1.printStackTrace();}		
	}
	
	
	private Object[][] Query_to_Table(String Query, int n){
		Object[][] tableContents = new Object[0][n];
		try {
			ResultSet rs = stmt.executeQuery(Query);
			int size = 0;
			while (rs.next()) { size++; }
			tableContents = new Object[size][n];
			rs = stmt.executeQuery(Query);
			int pos = 0;
			while (rs.next()) {
				int i=0;
				while(i<n){tableContents[pos][i] = rs.getString(i+1);i++;}
				pos++; 
			} 
		} catch (SQLException e) {}
		return tableContents;
	}
	
	private double[] Calculate_Total(){
		double ReturnNo[]={0,0};
		double discounts[]={0,0,0,0,0,0};
		int i=0;
		try {
			ResultSet rs=stmt.executeQuery("select sum(c.quantity*i.price) from emart_itemsincart c, emart_items i where c.customerid='"+UserID+"' and c.stockno=i.stockno");
			rs.next(); ReturnNo[0]=(double) rs.getDouble(1);
			String query = "select startdate,sandh,waived,new,green,silver,gold from (select a.*,rank() over (order by startdate desc) r from (select * from emart_discounts d where d.startdate<"+date+") a ) where r=1";
			rs=stmt.executeQuery(query);
			rs.next(); 
			while(i<6){discounts[i]=(double) Double.parseDouble(rs.getString(i+2));i++;}
			rs=stmt.executeQuery("select c.status from emart_customers c where c.id='"+UserID+"'");
			rs.next(); int cstatus=Integer.parseInt(rs.getString(1));
			if(ReturnNo[0]>discounts[1]){													// S&H waived
				if(cstatus==1){ReturnNo[1]=ReturnNo[0]*(1-discounts[2]);}							//New Customer
				if(cstatus==2){ReturnNo[1]=ReturnNo[0]*(1-discounts[3]);}							//Bronze Customer
				if(cstatus==3){ReturnNo[1]=ReturnNo[0]*(1-discounts[4]);}							//Silver Customer
				if(cstatus==4){ReturnNo[1]=ReturnNo[0]*(1-discounts[5]);}							//Gold Customer
			}else{																			// S&H paid
				if(cstatus==1){ReturnNo[1]=ReturnNo[0]*(1+discounts[0]-discounts[2]);}				//New Customer
				if(cstatus==2){ReturnNo[1]=ReturnNo[0]*(1+discounts[0]-discounts[3]);}				//Bronze Customer
				if(cstatus==3){ReturnNo[1]=ReturnNo[0]*(1+discounts[0]-discounts[4]);}				//Silver Customer
				if(cstatus==4){ReturnNo[1]=ReturnNo[0]*(1+discounts[0]-discounts[5]);}				//Gold Customer							
			}
		} catch (SQLException e1) {e1.printStackTrace();}
		return ReturnNo;
	}
	
	private String Calculate_Total_of_order(String orderid) {
		String Result="Total";
		ResultSet rs;
		try {
			rs = stmt.executeQuery("select sum(cost) from(select sum(oc.quantity*oc.price) as cost from emart_ordercontents oc where orderid='"+orderid+"' group by stockno)");
			rs.next();
			Result=rs.getString(1);
			if(Result==null){Result="Total";}
		} catch (SQLException e) {e.printStackTrace();}
		return Result;
	}
	
	private void Checkout(){
		ResultSet rs;
		int orderno=1;
		try {
			rs=stmt.executeQuery("select * from emart_itemsincart c where c.customerid='"+UserID+"'");
			try{
				rs.next();System.out.println(rs.getString(2));											// Cart not empty
				
				rs=stmt.executeQuery("select c.stockno,c.quantity,p.quantity from emart_itemsincart c, edepot_products p where c.stockno=p.stockno and p.quantity<c.quantity and c.customerid='"+UserID+"'");
				try {																					// Item not available in ordered quantity
					rs.next();String OutofStock=rs.getString(1);String Stock=rs.getString(3);
					JOptionPane.showMessageDialog(FrameHome, "Stockno: "+OutofStock+" is only available "+Stock+" times. Order Process cancelled", "Error", JOptionPane.OK_OPTION);
				} catch (SQLException e) {																// All Items available in ordered quantity
					rs=stmt.executeQuery("select c.status from emart_customers c where c.id='"+UserID+"'");
					rs.next(); int cstatus=Integer.parseInt(rs.getString(1));
					rs=stmt.executeQuery("select max(o.id) from emart_orders o");
					try{rs.next(); orderno=Integer.parseInt(rs.getString(1).trim())+1;}	catch(NullPointerException e1) {}
					stmt.executeUpdate("insert into emart_orders values('"+String.format("%06d", orderno)+"','"+UserID+"',"+date+","+cstatus+")");
					
					JOptionPane.showMessageDialog(FrameHome, "Payment of "+Calculate_Total()[1], "Payment", JOptionPane.INFORMATION_MESSAGE);
				
					CopyCartToOrder(String.format("%06d", orderno));
				
					JOptionPane.showMessageDialog(FrameHome, "Checkout Complete - Order # "+String.format("%06d", orderno), "Order Completed", JOptionPane.INFORMATION_MESSAGE);
					Customer_status_adjust();
					Empty_Cart();
				}
			} catch (SQLException e3){
				JOptionPane.showMessageDialog(FrameHome, "Don't try checking out an empty cart please", "Error", JOptionPane.OK_OPTION);
			}
		} catch (SQLException e) {e.printStackTrace();}
	}
	
	
	private void Empty_Cart() {
		try{
			String CartContents="delete from emart_itemsincart n where n.customerid='"+UserID+"'";
			stmt.executeUpdate(CartContents);
		}catch(SQLException e){e.printStackTrace();}
		
	}

	private void CopyCartToOrder(String orderno){
		ResultSet rs;
		try {
			String CartContents="select n.quantity,n.stockno,i.price from emart_itemsincart n, emart_items i where n.customerid='"+UserID+"' and i.stockno=n.stockno";
			rs=stmt.executeQuery(CartContents);
			int i=0;
			while(rs.next()){i++;}
			String[] insertOrderContents=new String[i];i=0;
			
			rs=stmt.executeQuery(CartContents);
			while(rs.next()){
				insertOrderContents[i]="insert into emart_ordercontents values('"+orderno+"','"+rs.getString(2)+"','"+rs.getString(1)+"','"+rs.getString(3)+"')";
				i++;
			}i=i-1;
			while(i>-1){stmt.executeUpdate(insertOrderContents[i]);i=i-1;}
		} catch (SQLException e) {e.printStackTrace();}
	}
	
	private void CopyOrderToCart(String orderno){
		ResultSet rs;
		try {
			String OrderContents="select oc.quantity,oc.stockno,oc.price from emart_ordercontents oc where oc.orderid='"+orderno+"'";
			rs=stmt.executeQuery(OrderContents);
			int i=0;
			while(rs.next()){i++;}
			String[] insertOrderContents=new String[i];i=0;
			
			rs=stmt.executeQuery(OrderContents);
			while(rs.next()){
				insertOrderContents[i]="insert into emart_itemsincart values('"+UserID+"','"+rs.getString(2)+"','"+rs.getString(1)+"')";
				i++;
			}i=i-1;
			while(i>-1){stmt.executeUpdate(insertOrderContents[i]);i=i-1;}
		} catch (SQLException e) {e.printStackTrace();}
	}
	
	
	private void Customer_status_adjust(){
		ResultSet rs;
		double amountLastThree=0.0;
		try{
			String getlastthree="select sum(oc.quantity*oc.price) from emart_ordercontents oc,( select id from( select a.*,rank() over(order by odate desc,id desc) r	from (select o.odate,o.id from emart_orders o where o.customer='"+UserID+"' ) a) where r<4 ) oid where oid.id=oc.orderid";
			rs=stmt.executeQuery(getlastthree);
			rs.next(); amountLastThree=Double.parseDouble(rs.getString(1));
			String queryclevels="select * from (select a.*,rank() over (order by startdate desc) r from (select * from emart_clevels d where d.startdate<"+date+") a ) where r=1";
			rs=stmt.executeQuery(queryclevels);rs.next();
			int newstatus=1;									// 1 new, 2 green, 3 silver, 4 gold
			newstatusString="New";
			if(amountLastThree>rs.getDouble(2)){
				newstatus=2; newstatusString="Green";
			}if(amountLastThree>rs.getDouble(3)){
				newstatus=3; newstatusString="Silver";
			}if(amountLastThree>rs.getDouble(4)){
				newstatus=4; newstatusString="Gold";
			}
			stmt.executeUpdate("update emart_customers c set c.status="+newstatus+" where c.id='"+UserID+"'");
			JOptionPane.showMessageDialog(FrameHome, "Customer Status changed to: "+newstatusString, "Status Changed: "+amountLastThree, JOptionPane.INFORMATION_MESSAGE);

		}catch(SQLException e){e.printStackTrace();}
	}
}
