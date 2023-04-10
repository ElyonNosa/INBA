package comp3350.inba.application;

public class Main
{
	private static String dbName="INBA_DATABASE";

	public static void main(String[] args)
	{
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
