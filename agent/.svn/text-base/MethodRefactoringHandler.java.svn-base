/**
 * 
 */
package agent;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

import exceptions.CompilationFailureException;

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
public class MethodRefactoringHandler extends StartHandler {

	private final BufferedReader br;

	private static final String LOGFILE = "/home/xiang/MethodRefactoringExample/MethodRefactoringExec.log";

	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

	public MethodRefactoringHandler(AgendaItem associatedItem, BufferedReader br) {
		super(associatedItem);
		this.br = br;
		// TODO Auto-generated constructor stub
	}

	@Override
	public synchronized void posted() {
		// TODO Auto-generated method stub
		try {
			if (item.getStep().equals("Rename Method")) {
				item.setParameter("sourcefilename",
						"/home/xiang/MethodRefactoringExample/Main.java");
				System.out.println("Parameter Set");
				item.start();
			} else
				super.posted();
		} catch (AMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownParameter e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalTransition e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void started() {
		// TODO Auto-generated method stub
		try {
			if (item.getStep().getName().equals("Choose a badly named method")) {
				System.out.println("Current source file is: ");
				// System.out
				// .println((String) item.getParameter("sourcefilename"));
				System.out
						.println(FileUtil
								.readfromFile("/home/xiang/MethodRefactoringExample/Main.java"));
				System.out.println("\nWhich method do you want to rename?");
				String oldmethodname = br.readLine();
				item.setParameter("methodname", oldmethodname);
				FileUtil.writetofile(LOGFILE, item.getStep().getName()
						+ ": set old method name to " + oldmethodname + ";\n");
			}
			if (item.getStep().getName().equals(
					"Declare new method with new name")) {
				System.out
						.println("What do you want the new method name to be?");
				String newmethodname = br.readLine();
				item.setParameter("newmethodname", newmethodname);
				clipboard.setContents(new StringSelection(newmethodname), null);
				// String originalfile = FileUtil
				// .readfromFile("/home/xiang/MethodRefactoringExample/Main.java");
				System.out
						.println("Please add the new method declaration to the source file (new method declaration copied to clipboard)");
				br.readLine();
				FileUtil.writetofile(LOGFILE, item.getStep().getName()
						+ ": set new method name to " + newmethodname + ";\n");

			}
			if (item.getStep().getName().equals(
					"Copy code body from old method to new one")) {
				System.out
						.println("Please now copy the code body of the old method to the new one");
				br.readLine();
				FileUtil
						.writetofile(
								LOGFILE,
								item.getStep().getName()
										+ ": code body of the old method is copied to the new one\n");
			}
			if (item.getStep().getName().equals("Compile")) {
				System.out.println("Compiling new source file...");
				String cmd = "javac /home/xiang/MethodRefactoringExample/Main.java";
				Process pr = Runtime.getRuntime().exec(cmd);
				String errormsg = FileUtil.printLines(cmd + " stderr:", pr
						.getErrorStream());
				pr.waitFor();
				if (pr.exitValue() != 0) {
					System.out.println("Compilation Failed");
					FileUtil.writetofile(LOGFILE, item.getStep().getName()
							+ ": Compilation Failed with error message:\n"
							+ errormsg);
					terminate(new CompilationFailureException());
					return;
				} else {
					System.out.println("Compilation Success!");
					FileUtil.writetofile(LOGFILE, item.getStep().getName()
							+ ": Compilation Success\n");
				}
			}
			if (item.getStep().getName().equals(
					"Check and modify method declaration")) {
				System.out
						.println("Please check and change the method declaration");
				br.readLine();
				FileUtil.writetofile(LOGFILE, item.getStep().getName()
						+ ": Fixed method declaration\n");
			}
			if (item.getStep().getName().equals(
					"Change old method body to call new one")) {
				System.out
						.println("Please now change the old method body to call the new one");
				br.readLine();
				FileUtil.writetofile(LOGFILE, item.getStep().getName()
						+ ": Old method changed to call the new one\n");
			}
			if (item.getStep().getName().equals("Check for old method code")) {
				System.out.println("Please check and change old method code");
				FileUtil.writetofile(LOGFILE, item.getStep().getName()
						+ ": checked and changed the old method code\n");
				br.readLine();
			}
			if (item.getStep().getName().equals("Find references")) {
				System.out.println("Looking for reference...");
				String oldmethodname = (String) item.getParameter("methodname");
				String[] methodsignature = oldmethodname.split("\\s");
				String methodname = methodsignature[methodsignature.length - 1]
						.trim();
				int m = -1;
				if ((m = FileUtil.readfromFile(
						"/home/xiang/MethodRefactoringExample/Main.java")
						.indexOf(methodname)) != -1) {
					System.out.println("Reference found at index " + m + "!");
					FileUtil.writetofile(LOGFILE, "Reference found at index "
							+ m + "!\n");
				}
				item.setParameter("referenceindex", String.valueOf(m));
			}
			if (item.getStep().getName().equals(
					"Change references to refer to new one")) {
				String oldmethodname = (String) item.getParameter("methodname");
				String[] methodsignature = oldmethodname.split("\\s");
				String methodname = methodsignature[methodsignature.length - 1]
						.trim();
				methodname = methodname.substring(0, methodname.length() - 2);
				String newmethodname = (String) item
						.getParameter("newmethodname");
				methodsignature = newmethodname.split("\\s");
				String nmethodname = methodsignature[methodsignature.length - 1]
						.trim();
				nmethodname = nmethodname
						.substring(0, nmethodname.length() - 2);
				String code = FileUtil
						.readfromFile("/home/xiang/MethodRefactoringExample/Main.java");
				code=code.replaceFirst(methodname, nmethodname);
				FileUtil.rewritetofile(
						"/home/xiang/MethodRefactoringExample/Main.java", code);
				System.out.println("1 reference updated!");
				FileUtil.writetofile(LOGFILE, "1 reference updated from "
						+ oldmethodname + "to " + newmethodname + "\n");
			}
			if (item.getStep().isLeaf())
				item.complete();
		} catch (AMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownParameter e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalTransition e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void terminate(Serializable e) {
		// TODO Auto-generated method stub
		System.out.println("Go to handling process...");
		System.out
				.println("Please refer to the following execution history and check the method declaration: ");
		System.out.println(FileUtil.readfromFile(LOGFILE));
		// System.out.println("The data gradereport is missing in datadictionary");
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
}
