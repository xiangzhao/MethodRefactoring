/**
 * 
 */
package agentutil;

import laser.juliette.agent.ItemHandlerAdapter;
import laser.juliette.ams.AMSException;
import laser.juliette.ams.AgendaItem;
import laser.juliette.ams.IllegalTransition;

/**
 * @author xiang
 * 
 */
public class HistoryStartHandler extends ItemHandlerAdapter {

	public HistoryStartHandler(SQLAgendaItem associatedItem) {
		item = associatedItem;
	}

	public synchronized void posted() {
		try {
			item.start();
		} catch (AMSException ex) {
			throw new RuntimeException(ex.toString());
		} catch (IllegalTransition ex) {
			throw new RuntimeException(ex.toString());
		}
	}

	protected SQLAgendaItem item;
}
