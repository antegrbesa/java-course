package hr.fer.zemris.java.gui.prim;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Implementation of a {@link ListModel}. Generates prim numbers using
 * {@link #next()} method and notifies listeners of change.
 * @author Ante
 *
 */
public class PrimListModel implements ListModel<Integer> {

	/**Data collection*/
	private List<Integer> data;
	
	/**Listeners*/
	private List<ListDataListener> listeners;
	
	/**last prime generated*/
	private int lastPrime;
	
	/**
	 * Constructor for this class.
	 */
	public PrimListModel() {
		data = new ArrayList<Integer>();
		listeners = new ArrayList<>();
		
		lastPrime = 1;
		data.add(1);
	}
	
	@Override
	public int getSize() {
		return data.size();
	}

	@Override
	public Integer getElementAt(int index) {
		return data.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listeners = new ArrayList<>(listeners);
		listeners.add(l);
		
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners = new ArrayList<>(listeners);
		if (listeners.contains(l)) {
			listeners.remove(l);
		}
		
	}
	
	/**
	 * Generates next prim number and notifies listeners.
	 */
	public void next() {
		int index = data.size();
		data.add(getNext());
		ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index, index);
		for (ListDataListener l : listeners) {
			l.intervalAdded(event);
		}
	}

	/**
	 * Gets next prim number.
	 * @return next prim
	 */
	private Integer getNext() {
		if (lastPrime == 1) {
			lastPrime += 2;
			return 2;
		}
		
		while (true) {
			boolean prime = true;
			for (int i = 3; i*i<=lastPrime; i+=2) {
				if (lastPrime % i==0) {
					prime = false;
					break;
				}
			}
			if (prime) {
				int retValue = lastPrime;
				lastPrime += 2;
				return retValue;
			}
			
			lastPrime += 2;
		}
	}
	
	

	
}
