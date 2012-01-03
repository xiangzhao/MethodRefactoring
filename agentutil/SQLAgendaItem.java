/**
 * 
 */
package agentutil;

import java.io.Serializable;

import laser.juliette.ams.AMSException;
import laser.juliette.ams.UnknownParameter;
import laser.juliette.rmiams.client.AgendaItemAdapter;
import laser.juliette.rmiams.server.api.RemoteAgendaItem;

/**
 * @author xiang
 * 
 */
public class SQLAgendaItem extends AgendaItemAdapter {

	public SQLAgendaItem(RemoteAgendaItem delegate) {
		super(delegate);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7030736418620429170L;

	@Override
	public void setParameter(String arg0, Serializable arg1)
			throws UnknownParameter, AMSException {
		// TODO Auto-generated method stub
		super.setParameter(arg0, arg1);
		logParameterEntry(arg0, arg1);
	}

	private void logParameterEntry(String arg0, Serializable arg1) {
		// TODO Auto-generated method stub
		System.out.println("logged");
	}

}
