/**
 * 
 */
package agent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.SQLException;

import laser.juliette.agent.AbstractAgent;
import laser.juliette.agent.ItemHandler;
import laser.juliette.agent.ItemHandlerFactory;
import laser.juliette.ams.AgendaItem;

/**
 * @author xiang
 * 
 */
public class QueryModifierAgent extends AbstractAgent {

	/*
	 * (non-Javadoc)
	 * 
	 * @see laser.juliette.agent.AbstractAgent#configureAgent()
	 */
	@Override
	protected void configureAgent() {
		// TODO Auto-generated method stub
		InputStreamReader isr = new InputStreamReader(System.in);
		final BufferedReader br = new BufferedReader(isr);
		// final String userName = "root";
		// final String password = "jackson";
		// final String url = "jdbc:mysql://localhost:3306/querymodifier";
		// System.out
		// .println("I am going to work on separating query modifier method");
		// try {
		// Class.forName("com.mysql.jdbc.Driver").newInstance();
		// } catch (InstantiationException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IllegalAccessException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (ClassNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		setItemHandlerFactory(new ItemHandlerFactory() {

			@Override
			public ItemHandler createItemHandler(AgendaItem arg0) {
				// TODO Auto-generated method stub
				// try {
				// return new QueryModifierHandler(arg0, br, DriverManager
				// .getConnection(url, userName, password));
				// } catch (SQLException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				return new QueryModifierHandler(arg0, br);
				// return null;
			}

		});
	}

}
