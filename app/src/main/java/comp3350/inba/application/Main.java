package comp3350.inba.application;

import comp3350.inba.presentation.CLI;

public class Main
{
	private static String dbName="TXN";

	public static void main(String[] args)
	{
		CLI.run();
		System.out.println("All done");
	}

	public static void setDBPathName(final String name) {
		try {
			Class.forName("org.hsqldb.jdbcDriver").newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		dbName = name;
	}

	public static String getDBPathName() {
		return dbName;
	}
}
