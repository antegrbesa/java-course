package hr.fer.zemris.java.hw16.frame;

import javax.swing.AbstractListModel;

import hr.fer.zemris.java.hw16.interfaces.DrawingModel;
import hr.fer.zemris.java.hw16.interfaces.DrawingModelListener;
import hr.fer.zemris.java.hw16.jvdraw.model.GeometricalObject;

/**
 * Implementation of a {@link AbstractListModel} used in {@link JVDraw} class. It also implements {@link DrawingModelListener}
 * interface. 
 * @author Ante
 *
 */
public class DrawingObjectListModel extends AbstractListModel<GeometricalObject> 
	implements DrawingModelListener {

	/**
	 * Reference to DrawingModel.
	 */
	private DrawingModel model;

	/**
	 * Constructor.
	 * @param model reference to DrawingModel.
	 */
	public DrawingObjectListModel(DrawingModel model) {
		super();
		this.model = model;
	}

	/**
	 * UID
	 */
	private static final long serialVersionUID = -6464697730725614313L;

	@Override
	public int getSize() {
		return model.getSize();
	}

	@Override
	public GeometricalObject getElementAt(int index) {
		return model.getObject(index);
	}

	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		fireIntervalAdded(this, index0, index1);
	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		fireIntervalRemoved(this, index0, index1);
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		fireIntervalAdded(this, index0, index1);
}
	
}
