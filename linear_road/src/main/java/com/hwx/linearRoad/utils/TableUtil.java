package com.hwx.linearRoad.utils;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.util.Bytes;

public final class TableUtil {

	public static HTableDescriptor createTable(Connection conn, String tableName, String... families) throws Exception{
		return createTable(false, conn, tableName, families);
	}

	public static HTableDescriptor createTable(boolean forceDrop, Connection conn, String tableName, String... families) throws Exception{
		Admin admin = conn.getAdmin();

		if (forceDrop){
			System.out.printf("!!! dropping table %s in...\n", tableName);
			for (int i = 5; i > 0; i--) {
				System.out.println(i);
				Thread.sleep(1000);
			}
			dropTable(admin, tableName);
		}

		TableName bname = TableName.valueOf(tableName);
		if (admin.tableExists(bname)) {
			System.out.printf("%s table already exists.\n", tableName);
		} else {
			System.out.printf("Creating %s\n", tableName);
			HTableDescriptor desc = new HTableDescriptor(bname);
			for (String s : families){
				desc.addFamily(new HColumnDescriptor(Bytes.toBytes(s)));
			}
			admin.createTable(desc);
			System.out.printf("%s table created\n", tableName);
		}
		HTableDescriptor t = admin.getTableDescriptor(bname);
		admin.close();
		return t;
	}
	
	public static void dropTable(Connection conn, String tableName) throws Exception{
		Admin admin = conn.getAdmin();

		dropTable(admin, tableName);

		admin.close();
	}

	private static void dropTable(Admin admin, String tableName) throws Exception{
		TableName bname = TableName.valueOf(tableName);
		if (admin.tableExists(bname)) {
			System.out.printf("Deleting %s\n", tableName);
			if (admin.isTableEnabled(bname))
				admin.disableTable(bname);
			admin.deleteTable(bname);
		}
	}
	
	public static HTableDescriptor verifyTable(Connection conn, String tableName) throws Exception{
		Admin admin = conn.getAdmin();
		return verifyTable(admin, tableName);
	}
	
	private static HTableDescriptor verifyTable(Admin admin, String tableName) throws Exception{
		HTableDescriptor t = null;
		
		TableName bname = TableName.valueOf(tableName);
		if (admin.tableExists(bname)) {
			t = admin.getTableDescriptor(bname);
		}
		return t;
	}
	
	public static void truncateTable(Connection conn, String tableName, boolean preserveSplits) throws Exception{
		Admin admin = conn.getAdmin();
		truncateTable(admin, tableName, preserveSplits);
		admin.close();
	}
	
	private static void truncateTable(Admin admin, String tableName, boolean preserveSplits) throws Exception{
		admin.disableTable(TableName.valueOf(tableName));
		admin.truncateTable(TableName.valueOf(tableName), preserveSplits);
	}
	


}
