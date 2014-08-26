package database_Initialization;
import java.sql.*;

public class DB_Ini_Main {

	public static void main(String[] args) throws SQLException{
		//=========================================================================================
		// Database Initialization for eMart and eDepot
		// author: Daniel Schuerhoff
		// Version: 1.3
		// Last Modified: 12.03.2014
		//=========================================================================================
				
		
		
		System.out.format("=============================================================================== %n  Database Initialization for eMart and eDepot%n===============================================================================%n");
		System.out.format("  Opening Database Connection ... ");
		
		
		
		//=========================================================================================
		// Initializind DB Driver and connecting to database
		//=========================================================================================
		DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
		String strConn = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
		String strUsername = "dschuerhoff";
		String strPassword = "X280715";
		Connection conn = DriverManager.getConnection(strConn,strUsername,strPassword);
		System.out.format("DONE%n  Dropping all existing user tables ... ");

		// Create a Statement
		Statement stmt = conn.createStatement();

		
		
		//=========================================================================================
		// D-D-D-D-D-D-D-D-D-Dropping all existing tables
		//=========================================================================================
		String DeleteTableString = ("BEGIN \n FOR i IN (SELECT table_name FROM user_tables) \n  LOOP \n   EXECUTE IMMEDIATE ('DROP TABLE ' || user || '.' || i.table_name || ' CASCADE CONSTRAINTS'); \n  END LOOP; \n END;");
		stmt.executeUpdate(DeleteTableString);
		System.out.format("DONE%n  Creating eMart Tables ...");
		
		
		
		//=========================================================================================
		// Creating all tables
		//=========================================================================================
		// Strings to Create eMart Tables
		String SMCustomers = "CREATE TABLE eMart_Customers(id char(20), pw char(20) not null, name char(20), email char(20), adress char(40), status int, manager int, constraint pkeyCustID primary key(id), constraint uniqueMail unique (email))";
		String SMOrders = "CREATE TABLE eMart_Orders(id char(20), customer char(20), odate int, cstatus int, constraint pkeyOrderID primary key(id), constraint fkeyCustID foreign key (customer) references eMart_Customers(id))";
		String SMItems = "CREATE TABLE eMart_Items(stockno char(7), manufacturer char(20), modelno char(20), category char(20), description char(200), price real, warranty int, constraint pkeyStockno primary key(stockno), constraint uniqueManMod unique (manufacturer, modelno))";
		String SMAccessorieOf = "CREATE TABLE eMart_AccessorieOf(stockno_parent char(7), stockno_child char(7), constraint pkeyAccessoriesOf primary key(stockno_parent,stockno_child), constraint fkeyStocknoParent foreign key(stockno_parent) references eMart_Items(stockno), constraint fkeyStocknoChild foreign key (stockno_child) references eMart_Items(stockno))";
		String SMOrderContents = "CREATE TABLE eMart_OrderContents(orderID char(20), stockno char(7), quantity int, price real, constraint pkeyOrderContents primary key(orderID, stockno), constraint fkeyOrderContentsOrderID foreign key (orderID) references eMart_Orders(id), constraint fkeyOrderContentsStockno foreign key (stockno) references eMart_Items(stockno))";
		String SMItemsInCart = "CREATE TABLE eMart_ItemsInCart(customerID char(20), stockno char(7), quantity int, constraint pkeyCartContents primary key(customerID, stockno), constraint fkeyCartContentsCustID foreign key (customerID) references eMart_Customers(id), constraint fkeyCartContentsStockno foreign key (stockno) references eMart_Items(stockno))";
		String SMDiscounts = "create table emart_discounts(startdate int,SandH real,waived real,new real,green real,silver real,gold real,primary key (startdate))";
		String SMDiscount_Initial = "insert into emart_discounts values(0,0.1,100,0,0,0.05,0.1)";
		String SMClevels = "create table emart_clevels(startdate int, green real, silver real, gold real)";
		String SMClevels_Initial = "insert into emart_clevels values(0,0,100,500)";
	
		// Strings to Create eDepot Tables
		String SDProducts = "CREATE TABLE eDepot_Products(stockno CHAR(7), quantity INT, min INT, max INT, location CHAR(20), replenishment INT, manufacturer CHAR(20), modelno CHAR(7), constraint pkeyProducts PRIMARY KEY (stockno), constraint fkeyProductManufacturer FOREIGN KEY (manufacturer) REFERENCES eDepot_Manufacturers(name), constraint uniqueStorageLocation unique (location), constraint uniqueProdManufMod unique (manufacturer, modelno))";
		String SDManufacturers = "CREATE TABLE eDepot_Manufacturers(name CHAR(20), lowproducts INT, adress CHAR(40), email CHAR(20), constraint pkeyManufacturerName PRIMARY KEY (name), constraint uniqueManufAdress unique (adress), constraint uniqueManufEmail unique (email))";
		String SDShipmentProcess = "CREATE TABLE eDepot_ShipmentProcess(pid CHAR(20), orderno CHAR(20), snotice CHAR(20), status CHAR(20), manufacturer CHAR(20), constraint pkeyPIDSHipmentProcess PRIMARY KEY (pid), constraint fkeyShipProcessManufac FOREIGN KEY (manufacturer) REFERENCES eDepot_Manufacturers(name), constraint uniqueOrderno unique (orderno))";
		String SDShipmentContents = "CREATE TABLE eDepot_ShipmentContents(pid CHAR(20), modelno CHAR(7), quantity INT, constraint pkeyShipmentContents PRIMARY KEY (pid, modelno), constraint fkeyShipmentContentsProcessID FOREIGN KEY (pid) REFERENCES eDepot_ShipmentProcess(pid))";
		
		// Creating eMart Tables
		stmt.executeUpdate(SMCustomers);
		stmt.executeUpdate(SMItems);
		stmt.executeUpdate(SMOrders);
		stmt.executeUpdate(SMAccessorieOf);
		stmt.executeUpdate(SMOrderContents);
		stmt.executeUpdate(SMItemsInCart);
		stmt.executeUpdate(SMDiscounts);
		stmt.executeUpdate(SMDiscount_Initial);
		stmt.executeUpdate(SMClevels);
		stmt.executeUpdate(SMClevels_Initial);
		System.out.format("DONE%n  Creating eDepot Tables ...");
		
		// Creating eDepot Tables
		stmt.executeUpdate(SDManufacturers);
		stmt.executeUpdate(SDProducts);
		stmt.executeUpdate(SDShipmentProcess);
		stmt.executeUpdate(SDShipmentContents);
		System.out.format("DONE%n  Feeding Sample Data into eMart Tables ... ");
		
		
		
		//=========================================================================================
		// Feeding Sample Values eMart
		//=========================================================================================
		// Feeding Customer Sample Data
		String Customer1 = "insert into emart_customers values('Rhagrid','Rhagrid','Rubeus Hagrid','rhagrid@cs','123 MyStreet, Goleta apt A, Ca',4,0)";
		String Customer2 = "insert into emart_customers values('Mhooch','Mhooch','Madam Hooch','mhooch@cs','123 MyStreet, Goleta apt B, Ca',3,0)";
		String Customer3 = "insert into emart_customers values('Amoody','Amoody','Alastor Moody','amoody@cs','123 MyStreet, Goleta apt C, Ca',1,0)";
		String Customer4 = "insert into emart_customers values('Pquirrell','Pquirrell','Professor Quirrell','pquirrell@cs','123 MyStreet, Goleta apt D, Ca',1,0)";
		String Customer5 = "insert into emart_customers values('Sblack','Sblack','Sirius Black','sblack@cs','123 MyStreet, Goleta apt E, Ca',2,1)";
		String Customer6 = "insert into emart_customers values('Ddiggle','Ddiggle','Dedalus Diggle','ddiggle@cs','123 MyStreet, Goleta apt F, Ca',2,0)";

		stmt.executeUpdate(Customer1);	stmt.executeUpdate(Customer2);	stmt.executeUpdate(Customer3);
		stmt.executeUpdate(Customer4);	stmt.executeUpdate(Customer5);	stmt.executeUpdate(Customer6);
	
		// Feeding Item Data
		String Item01 = "insert into emart_items values('AA00101','HP','6111','Laptop','Processer speed: 3.33Ghz; Ram size: 512 Mb; Hard disk size: 100Gb; Display Size: 17 in',1630,12)";
		String Item02 = "insert into emart_items values('AA00201','Dell','420','Desktop','Processer speed: 2.53Ghz; Ram size: 256 Mb; Hard disk size: 80Gb; Os: none',239,12)";
		String Item03 = "insert into emart_items values('AA00202','Emachine','3958','Desktop','Processer speed: 2.9Ghz; Ram size: 512 Mb; Hard disk size: 80Gb',369.99,12)";
		String Item04 = "insert into emart_items values('AA00301','Envision','720','Monitor','Size: 17 in; Weight: 25 lb.',69.99,36)";
		String Item05 = "insert into emart_items values('AA00302','Samsung','712','Monitor','Size: 17 in; Weight: 9.6 lb.',279.99,36)";
		String Item06 = "insert into emart_items values('AA00401','Symantec','2005','Software','Required disk size: 128 MB; Required RAM size: 64 MB',19.99,60)";
		String Item07 = "insert into emart_items values('AA00402','Mcafee','2005','Software','Required disk size: 128 MB; Required RAM size: 64 MB',19.99,60)";
		String Item08 = "insert into emart_items values('AA00501','HP','1320','Printer','Resolution: 1200 dpi; Sheet capacity: 500; Weight: 0.4 lb',299.99,12)";
		String Item09 = "insert into emart_items values('AA00601','HP','435','Camera','Resolution: 3.1 Mp; Max zoom: 5 times; Weight: 24.7 lb',119.99,3)";
		String Item10 = "insert into emart_items values('AA00602','Cannon','738','Camera','Resolution: 3.1 Mp; Max zoom: 5 times; Weight: 24.7 lb',329.99,1)";
		
		stmt.executeUpdate(Item01);	stmt.executeUpdate(Item02);	stmt.executeUpdate(Item03);	stmt.executeUpdate(Item04);
		stmt.executeUpdate(Item05);	stmt.executeUpdate(Item06);	stmt.executeUpdate(Item07);	stmt.executeUpdate(Item08);
		stmt.executeUpdate(Item09);	stmt.executeUpdate(Item10);
		
		// Feeding AccessorieOf Data
		String AccessOf01 = "insert into emart_accessorieof values('AA00201','AA00301')";
		String AccessOf02 = "insert into emart_accessorieof values('AA00201','AA00302')";
		String AccessOf03 = "insert into emart_accessorieof values('AA00201','AA00401')";
		String AccessOf04 = "insert into emart_accessorieof values('AA00201','AA00402')";
		String AccessOf05 = "insert into emart_accessorieof values('AA00201','AA00501')";
		String AccessOf06 = "insert into emart_accessorieof values('AA00201','AA00601')";
		String AccessOf07 = "insert into emart_accessorieof values('AA00201','AA00602')";
		
		String AccessOf08 = "insert into emart_accessorieof values('AA00202','AA00301')";
		String AccessOf09 = "insert into emart_accessorieof values('AA00202','AA00302')";
		String AccessOf10 = "insert into emart_accessorieof values('AA00202','AA00401')";
		String AccessOf11 = "insert into emart_accessorieof values('AA00202','AA00402')";
		String AccessOf12 = "insert into emart_accessorieof values('AA00202','AA00501')";
		String AccessOf13 = "insert into emart_accessorieof values('AA00202','AA00601')";
		String AccessOf14 = "insert into emart_accessorieof values('AA00202','AA00602')";
		
		String AccessOf15 = "insert into emart_accessorieof values('AA00101','AA00401')";
		String AccessOf16 = "insert into emart_accessorieof values('AA00101','AA00402')";
		
		stmt.executeUpdate(AccessOf01);	stmt.executeUpdate(AccessOf02);	stmt.executeUpdate(AccessOf03);	stmt.executeUpdate(AccessOf04);
		stmt.executeUpdate(AccessOf05);	stmt.executeUpdate(AccessOf06);	stmt.executeUpdate(AccessOf07);	stmt.executeUpdate(AccessOf08);
		stmt.executeUpdate(AccessOf09);	stmt.executeUpdate(AccessOf10);	stmt.executeUpdate(AccessOf11);	stmt.executeUpdate(AccessOf12);
		stmt.executeUpdate(AccessOf13);	stmt.executeUpdate(AccessOf14);	stmt.executeUpdate(AccessOf15);	stmt.executeUpdate(AccessOf16);
		System.out.format("DONE%n  Feeding Sample Data into eDepot Tables ... ");
		
		
		
		//=========================================================================================
		// Feeding Sample Values eDepot
		//=========================================================================================
		// Feeding Manufacturer Data
		String Manu1 = "insert into edepot_manufacturers values('HP',0,'123 HPStreet, Goleta apt A, Ca','hp@cs')";
		String Manu2 = "insert into edepot_manufacturers values('Dell',0,'123 DellStreet, Goleta apt A, Ca','dell@cs')";
		String Manu3 = "insert into edepot_manufacturers values('Emachine',0,'123 EmachineStreet, Goleta apt A, Ca','emachine@cs')";
		String Manu4 = "insert into edepot_manufacturers values('Envision',0,'123 EnvisionStreet, Goleta apt A, Ca','envision@cs')";
		String Manu5 = "insert into edepot_manufacturers values('Samsung',0,'123 SamsungStreet, Goleta apt A, Ca','samsung@cs')";
		String Manu6 = "insert into edepot_manufacturers values('Symantec',0,'123 SymantecStreet, Goleta apt A, Ca','symantec@cs')";
		String Manu7 = "insert into edepot_manufacturers values('Mcafee',0,'123 McafeeStreet, Goleta apt A, Ca','mcafee@cs')";
		String Manu8 = "insert into edepot_manufacturers values('Cannon',0,'123 CannonStreet, Goleta apt A, Ca','cannon@cs')";
		
		stmt.executeUpdate(Manu1);	stmt.executeUpdate(Manu2);	stmt.executeUpdate(Manu3);	stmt.executeUpdate(Manu4);
		stmt.executeUpdate(Manu5);	stmt.executeUpdate(Manu6);	stmt.executeUpdate(Manu7);	stmt.executeUpdate(Manu8);
		
		// Feeding Products Data
		String Product01 = "insert into edepot_products values('AA00101',2,1,10,'A9',0,'HP','6111')";
		String Product02 = "insert into edepot_products values('AA00201',3,2,15,'A7',0,'Dell','420')";
		String Product03 = "insert into edepot_products values('AA00202',4,2,8,'B52',0,'Emachine','3958')";
		String Product04 = "insert into edepot_products values('AA00301',4,3,6,'C27',0,'Envision','720')";
		String Product05 = "insert into edepot_products values('AA00302',4,3,6,'C13',0,'Samsung','712')";
		String Product06 = "insert into edepot_products values('AA00401',7,5,9,'D27',0,'Symantec','2005')";
		String Product07 = "insert into edepot_products values('AA00402',7,5,9,'D1',0,'Mcafee','2005')";
		String Product08 = "insert into edepot_products values('AA00501',3,2,5,'E7',0,'HP','1320')";
		String Product09 = "insert into edepot_products values('AA00601',3,2,9,'F9',0,'HP','435')";
		String Product10 = "insert into edepot_products values('AA00602',3,2,5,'F3',0,'Cannon','738')";
		
		
		stmt.executeUpdate(Product01);	stmt.executeUpdate(Product02);	stmt.executeUpdate(Product03);	stmt.executeUpdate(Product04);
		stmt.executeUpdate(Product05);	stmt.executeUpdate(Product06);	stmt.executeUpdate(Product07);	stmt.executeUpdate(Product08);
		stmt.executeUpdate(Product09);	stmt.executeUpdate(Product10);
		System.out.format("DONE%n");
		
		System.out.format("=============================================================================== %n  Initialization of Database successfully completed.%n");
	}
}
