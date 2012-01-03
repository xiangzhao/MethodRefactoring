/**
 * 
 */
package agent;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import laser.juliette.agent.AbstractAgent;
import laser.juliette.agent.ItemHandler;
import laser.juliette.agent.ItemHandlerFactory;
import laser.juliette.ams.AgendaItem;

/**
 * @author xiang
 * 
 */
public class MethodRefactoringAgent extends AbstractAgent {

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
		System.out.println("I am going to work on method refactoring");
		setItemHandlerFactory(new ItemHandlerFactory() {

			@Override
			public ItemHandler createItemHandler(AgendaItem arg0) {
				// TODO Auto-generated method stub
				return new MethodRefactoringHandler(arg0, br);
			}

		});
	}

}
