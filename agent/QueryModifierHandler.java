/**
 * 
 */
package agent;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import exceptions.CompilationFailureException;
import exceptions.UnitTestFailureException;
import util.FileUtil;
import laser.juliette.agent.StartHandler;
import laser.juliette.ams.AMSException;
import laser.juliette.ams.AgendaItem;
import laser.juliette.ams.IllegalTransition;
import laser.juliette.ams.UnknownParameter;

/**
 * @author xiang
 * 
 */
public class QueryModifierHandler extends StartHandler {

	private static PrintStream realSystemOut = System.out;

	private static class NullOutputStream extends OutputStream {
		@Override
		public void write(int b) {
			return;
		}

		@Override
		public void write(byte[] b) {
			return;
		}

		@Override
		public void write(byte[] b, int off, int len) {
			return;
		}

		public NullOutputStream() {
		}
	}

	private final BufferedReader br;
	private Connection conn;
	private static final String EXCEPTIONQUERY = "select value from compilationfdbk where vid=(select MAX(vid) from compilationfdbk)";
	private static final String UNITTESTSQUERY = "select value from unittestsfdbk where vid=(select MAX(vid) from unittestsfdbk)";

	private static final String SRCFILE = "/home/xiang/MethodRefactoringExample/QueryModifierMethod/src/account/CheckingAccount.java";
	private static final String FOLDERNAME = "/home/xiang/MethodRefactoringExample/QueryModifierMethod/src/account/";

	public QueryModifierHandler(AgendaItem associatedItem, BufferedReader br,
			Connection conn) {
		super(associatedItem);
		this.br = br;
		this.conn = conn;

	}

	public QueryModifierHandler(AgendaItem associatedItem, BufferedReader br) {
		super(associatedItem);
		// realSystemOut = System.out;
		this.br = br;
	}

	@Override
	public void started() {
		System.setOut(new PrintStream(new NullOutputStream()));
		try {
			if (item.getStep().getName().equals("Specify QueryModifier Method")) {
				setParameter("sourcefilename", SRCFILE);
				String sourcefilecontent = FileUtil.readfromFile(SRCFILE);
				realSystemOut.println("Current source file content: ");
				realSystemOut.println(sourcefilecontent);
				setParameter("sourcefilecontent", sourcefilecontent.replace(
						"\n", "\\n"));
				realSystemOut.println("Please specify a querymodifier method:");
				String querymodifiermethodname = br.readLine();
				setParameter("querymodifiermethodname", querymodifiermethodname);
			} else if (item.getStep().getName().equals("Declare Query Method")) {
				realSystemOut
						.println("Please now declare a new query method in the source file");
				br.readLine();
				executeShellCmd("gedit " + SRCFILE);
				br.readLine();
				setSourcefilecontent();
			} else if (item.getStep().getName().equals(
					"Modify Query to return same value as original one")) {
				realSystemOut
						.println("Please now modify the query to return same value as original method");
				br.readLine();
				executeShellCmd("gedit " + SRCFILE);
				br.readLine();
				setSourcefilecontent();
			} else if (item.getStep().getName().equals("Check declaration")) {
				realSystemOut
						.println("Please make sure your query method declaration is correct");
				br.readLine();
				executeShellCmd("gedit " + SRCFILE);
				br.readLine();
				setSourcefilecontent();
			} else if (item.getStep().getName().equals("Check method call")) {
				realSystemOut
						.println("Please make sure you got it right calling the new method");
				br.readLine();
				executeShellCmd("gedit " + SRCFILE);
				br.readLine();
				setSourcefilecontent();
			} else if (item.getStep().getName().equals("Check Query body")) {
				realSystemOut
						.println("Please make sure your query returns the same value as original one");
				br.readLine();
				executeShellCmd("gedit " + SRCFILE);
				br.readLine();
				setSourcefilecontent();
			} else if (item.getStep().getName().equals(
					"Make original Method return a call to the new query")) {
				realSystemOut
						.println("Please make original method return a call to the new query");
				br.readLine();
				executeShellCmd("gedit " + SRCFILE);
				br.readLine();
				setSourcefilecontent();
			} else if (item.getStep().getName().equals("Retrieve source file")) {
				String currentfilename = null;
				String sourcefilename = (String) item
						.getParameter("sourcefilename");
				File dir = new File(sourcefilename.substring(0, sourcefilename
						.lastIndexOf("/")));
				String[] children = dir.list();
				if (children == null) {
					// Either dir does not exist or is not a directory
				} else {
					for (String filename : children) {
						if (!sourcefilename.equals(filename)
								&& !filename.contains("class")
								&& !FileUtil
										.readfromFile(
												"/home/xiang/MethodRefactoringExample/result.txt")
										.contains(filename)) {
							FileUtil
									.writetofile(
											"/home/xiang/MethodRefactoringExample/result.txt",
											filename + ";");
							currentfilename = filename;
							break;
						}

					}
				}
				item.setParameter("currentfilename", currentfilename);

			} else if (item.getStep().getName().equals(
					"Replace original call to call the query")) {
				String fullpathcurrentfilename = FOLDERNAME
						+ item.getParameter("currentfilename");
				realSystemOut
						.println("Please replace the original call to call the query for this file");
				br.readLine();
				executeShellCmd("gedit " + fullpathcurrentfilename);
				item.setParameter("referencefilecontent", FileUtil
						.readfromFile(fullpathcurrentfilename).replace("\n",
								"\\n"));

			} else if (item.getStep().getName().equals(
					"Add a call to original method before the query")) {

			} else if (item.getStep().getName().equals("Commit")) {

			} else if (item.getStep().getName().equals("Compile")) {
				realSystemOut.println("Compiling new source file...");
				Process pr = null;
				String sourcefilename = (String) item
						.getParameter("sourcefilename");
				if (item.getStep().getParent().getName().equals(
						"Update References")
						|| item.getStep().getName().equals(
								"Handle Reference Compilation Error")) {
					sourcefilename = sourcefilename.substring(0, sourcefilename
							.lastIndexOf("/"))
							+ File.separator + "*.java";
					pr = Runtime.getRuntime().exec(
							new String[] { "sh", "-c",
									"javac " + sourcefilename });
					// pr.waitFor();
				} else {
					String cmd = "javac " + sourcefilename;
					pr = Runtime.getRuntime().exec(cmd);
				}
				String errormsg = FileUtil.printLines(" stderr:", pr
						.getErrorStream());
				realSystemOut.println(errormsg);
				pr.waitFor();
				if (pr.exitValue() != 0) {
					realSystemOut.println("Compilation Failed");
					// logCompilationfdbkToHistory(errormsg, "failure");
					terminate(new CompilationFailureException());
					return;
				} else {
					realSystemOut.println("Compilation Success!");
					// logCompilationfdbkToHistory("Compilation Success","success");
				}
			} else if (item.getStep().getName().equals("Run unit tests")) {
				realSystemOut.println("Running unit tests...");
				String cmd = "/home/xiang/tests.sh";
				Process pr = Runtime.getRuntime().exec(cmd);
				String errormsg = FileUtil.printLines(cmd + " stdout:", pr
						.getInputStream());
				pr.waitFor();
				realSystemOut.println(errormsg);
				if (pr.exitValue() != 0) {
					realSystemOut.println("Unit tests failed");
					// logUnittestsfdbkToHistory(errormsg, "failure");
					terminate(new UnitTestFailureException());
					return;
				} else {
					realSystemOut.println("Unit tests passed!");
					// logUnittestsfdbkToHistory("Unit tests passed!",
					// "success");
				}
			}
			if (item.getStep().isLeaf())
				item.complete();
		} catch (AMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalTransition e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownParameter e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setSourcefilecontent() throws AMSException, UnknownParameter {
		String sourcefilecontent = FileUtil.readfromFile(SRCFILE);
		setParameter("sourcefilecontent", sourcefilecontent
				.replace("\n", "\\n"));
	}

	private void setParameter(String parameterName, Serializable object)
			throws AMSException, UnknownParameter {
		item.setParameter(parameterName, object);
		// logToHistory(parameterName, object);
	}

	private void logToHistory(String parameterName, Serializable object) {
		// TODO Auto-generated method stub
		try {
			PreparedStatement statement = conn.prepareStatement("insert into "
					+ parameterName + " values (default,?,?,?,?)");
			statement.setString(1, (String) object);
			statement.setString(2, item.getStep().getName());
			statement.setString(3, "Jack");
			statement.setTime(4, new Time(Calendar.getInstance().getTime()
					.getTime()));
			statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void logCompilationfdbkToHistory(String compilationfdbk, String type) {
		// TODO Auto-generated method stub
		try {
			PreparedStatement statement = conn
					.prepareStatement("insert into compilationfdbk values (default,?,?,?,?,?)");
			statement.setString(1, compilationfdbk);
			statement.setString(2, item.getStep().getParent().getName());
			statement.setString(3, "Compiler");
			statement.setString(4, type);
			statement.setTime(5, new Time(Calendar.getInstance().getTime()
					.getTime()));
			statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void logUnittestsfdbkToHistory(String unittestsfdbk, String type) {
		// TODO Auto-generated method stub
		try {
			PreparedStatement statement = conn
					.prepareStatement("insert into unittestsfdbk values (default,?,?,?,?,?)");
			statement.setString(1, unittestsfdbk);
			statement.setString(2, item.getStep().getParent().getName());
			statement.setString(3, "Bash");
			statement.setString(4, type);
			statement.setTime(5, new Time(Calendar.getInstance().getTime()
					.getTime()));
			statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void terminate(Serializable e) throws IOException, SQLException,
			InterruptedException {

		realSystemOut.println("Go to handling process...");
		// realSystemOut.println("Below is the exception message: ");
		// Statement s = conn.createStatement();
		// if (e instanceof CompilationFailureException) {
		// s.executeQuery(EXCEPTIONQUERY);
		// } else {
		// s.executeQuery(UNITTESTSQUERY);
		// }
		// ResultSet rs = s.getResultSet();
		// while (rs.next()) {
		// String value = rs.getString("value");
		// realSystemOut.println(value);
		// }
		// realSystemOut
		// .println("Do you want to look at your recent change to the source file?(y/n)");
		realSystemOut.println("Please refer to the ddg we get");
		String response = br.readLine();
		if (!response.startsWith("n"))
			executeShellCmd("java -jar /home/xiang/ddgviewer.jar /home/xiang/ProvenanceData/provenance.txt");
		// String cmd =
		// "java -jar /home/xiang/ddgviewer.jar /home/xiang/ProvenanceData/provenance.txt";
		// Process pr = Runtime.getRuntime().exec(cmd);
		// pr.waitFor();
		// if (response.equals("y")) {
		// // realSystemOut.println("open");
		// s
		// .executeQuery("SELECT value FROM sourcefilecontent s order by ttime DESC LIMIT 0,3 ");
		// ResultSet rts = s.getResultSet();
		// rts.next();
		// String file1 = rts.getString("value");
		// rts.next();
		// String file2 = rts.getString("value");
		// rts.next();
		// String file3 = rts.getString("value");
		// FileUtil
		// .rewritetofile(
		// "/home/xiang/MethodRefactoringExample/CheckingAccount1.java",
		// file1);
		// FileUtil
		// .rewritetofile(
		// "/home/xiang/MethodRefactoringExample/CheckingAccount2.java",
		// file2);
		// FileUtil
		// .rewritetofile(
		// "/home/xiang/MethodRefactoringExample/CheckingAccount3.java",
		// file3);
		// String cmd =
		// "meld /home/xiang/MethodRefactoringExample/CheckingAccount1.java /home/xiang/MethodRefactoringExample/CheckingAccount2.java /home/xiang/MethodRefactoringExample/CheckingAccount3.java";
		// Process pr = Runtime.getRuntime().exec(cmd);
		// pr.waitFor();
		// }
		// String query = null;
		// realSystemOut.println("Type in query as you want");
		// while (!((query = br.readLine()).equals("end"))) {
		// if (query.startsWith("where")) {
		// query =
		// "select step,ttime from sourcefilecontent order by ttime ASC";
		// }
		// s.executeQuery(query);
		// rs = s.getResultSet();
		// realSystemOut.println("\n");
		// while (rs.next()) {
		// int i = 1;
		// while (true) {
		// try {
		// realSystemOut.print(rs.getString(i++) + "\t");
		// } catch (SQLException err) {
		// break;
		// }
		// }
		// realSystemOut.println();
		// }
		// realSystemOut.println("\nAny other information you want to know?");
		// }

		// realSystemOut.println(FileUtil.readfromFile(LOGFILE));
		// realSystemOut.println("The data gradereport is missing in datadictionary");
		Set<Serializable> exceptions = new HashSet<Serializable>();
		exceptions.add(e);
		try {
			item.terminate(exceptions);
		} catch (AMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalTransition e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * @param cmd
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void executeShellCmd(String cmd) throws IOException,
			InterruptedException {
		Process pr = Runtime.getRuntime().exec(cmd);
		pr.waitFor();
	}
}
