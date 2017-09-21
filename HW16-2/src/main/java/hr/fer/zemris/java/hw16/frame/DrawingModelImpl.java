package hr.fer.zemris.java.hw16.frame;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw16.interfaces.DrawingModel;
import hr.fer.zemris.java.hw16.interfaces.DrawingModelListener;
import hr.fer.zemris.java.hw16.jvdraw.model.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.model.Rectangle;

/**
 * Implementation of {@link DrawingModel} interface that is used in {@link JVDraw} class.
 * @author Ante
 *
 */
public class DrawingModelImpl implements DrawingModel {

	/**
	 * List of geometrical objects.
	 */
	private List<GeometricalObject> list;

	/**
	 * Listener list. 
	 */
	private List<DrawingModelListener> listeners;

	/**
	 * Default constructor.
	 */
	public DrawingModelImpl() {
		list = new ArrayList<>();
		listeners = new ArrayList<>();
	}

	@Override
	public int getSize() {
		return list.size();
	}

	@Override
	public GeometricalObject getObject(int index) {
		if (index < 0 || index >= getSize()) {
			throw new IndexOutOfBoundsException("index < 0 || index >= size");
		}
		return list.get(index);
	}

	@Override
	public void add(GeometricalObject object) {
		if (object == null) {
			throw new IllegalArgumentException("Null reference.");
		}
		int index = list.size();
		list.add(object);
		fireIntevalAdded(index, index);
	}

	@Override
	public void addDrawingModelListener(DrawingModelListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("Null reference.");
		}
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeDrawingModelListener(DrawingModelListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Method that notifies listeners whenever a new object is added on interval (inclusive)
	 * index0 to index1.
	 * @param index0 index of first added object
	 * @param index1 index of last added object
	 */
	public void fireIntevalAdded(int index0, int index1) {
		for (DrawingModelListener l : listeners) {
			l.objectsAdded(this, index0, index1);
		}
	}

	/**
	 * Method that notifies listeners whenever objecst are changed on interval (inclusive)
	 * index0 to index1.
	 * @param index0 index of first changed object
	 * @param index1 index of last changed object
	 */
	public void fireIntervalChanged(int index0, int index1) {
		for (DrawingModelListener l : listeners) {
			l.objectsChanged(this, index0, index1);
		}
	}

	/**
	 * Method that notifies listeners whenever objects are removed on interval (inclusive)
	 * index0 to index1.
	 * @param index0 index of first removed object
	 * @param index1 index of last removed object
	 */
	public void fireIntevalRemoved(int index0, int index1) {
		for (DrawingModelListener l : listeners) {
			l.objectsRemoved(this, index0, index1);
		}
	}

	@Override
    public Rectangle getBoundingBox() {
        int left = Integer.MAX_VALUE;
        int top = Integer.MAX_VALUE;
        int right = 0;
        int bottom = 0;

        for (GeometricalObject o : list) {
            Rectangle r = o.getBounds();
            left = Math.min(r.getX(), left);
            right = Math.max(right, r.getX() + r.getWidth());
            top = Math.min(top, r.getY());
            bottom = Math.max(bottom, r.getY() + r.getHeight());
        }

        return new Rectangle(left, top, right - left, bottom - top);
}
	
	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public void removeObject(int index) {
		if (index < 0 || index >= list.size()) {
			return;
		}
		list.remove(index);
		fireIntevalRemoved(index, index);
	}

	@Override
	public void updateObject(int index) {
		if (index < 0 || index >= list.size()) {
			return;
		}
		list.get(index).update();
		fireIntervalChanged(index, index);
}
}
