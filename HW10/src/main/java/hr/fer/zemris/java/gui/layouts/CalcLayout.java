package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Implementation of a layout manager used in {@link CalcLayout} class.
 * Component at index (1,1) occupies 5 spaces, therefore indexes from (1,2) to (1,5) are
 * illegal.
 * @author Ante Grbesa
 *
 */
public class CalcLayout implements LayoutManager2 {
	
	/**Gap between components*/
	private int gap;

	/**Maximum number of rows*/
	private static final int MAX_ROW = 5;
	/**Maximum number of columns*/
	private static final int MAX_COLUMN = 7;
	
	/**
	 * Illegal positions for constraints. 
	 */
	private static List<RCPosition> illegalPositions;
	
	/**
	 * Collection of constrains.
	 */
	private Map<Component, RCPosition> constraintsColl;
	
	static {
		illegalPositions = new ArrayList<>();
		illegalPositions.add(new RCPosition(1, 2));
		illegalPositions.add(new RCPosition(1, 3));
		illegalPositions.add(new RCPosition(1, 4));
		illegalPositions.add(new RCPosition(1, 5));
	}
	
	/**
	 * Creates this layout with gap set to 0.
	 */
	public CalcLayout() {
		this(0);
	}
	
	/**
	 * Creates this layout with gap set to specified one.
	 * @param gap gap to set
	 * @throws IllegalArgumentException if gap is negative
	 */
	public CalcLayout(int gap) {
		if (gap < 0) {
			throw new IllegalArgumentException("Gap was negative");
		}
		
		this.gap = gap;
		constraintsColl = new HashMap<>();
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		RCPosition position;
		if (constraints instanceof String) {
			position = new RCPosition((String) constraints);
		} else if (constraints instanceof RCPosition) {
			position = (RCPosition) constraints;
		} else {
			throw new IllegalArgumentException("Invalid constraint");
		}
		
		int row = position.getRow();
		int column = position.getColumn();
		
		if (row > MAX_ROW || row < 1 || column > MAX_COLUMN || column < 1 || illegalPositions.contains(position)) {
			throw new IllegalArgumentException("Given constraint is illegal");
		}
		if (constraintsColl.containsKey(comp)) {
			throw new IllegalArgumentException("Given constraint is already occupied");
		}
		
		constraintsColl.put(comp, position);
	}
	
	@Override
	public void removeLayoutComponent(Component comp) {
		if (! constraintsColl.containsKey(comp)) {
			throw new IllegalArgumentException("Component does not exist");
		}
		
		constraintsColl.remove(comp);
	}
	
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		Dimension d = calculateSize(parent, (c) -> c.getPreferredSize());
		return d;
	}
	
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		Dimension d = calculateSize(parent, (c) -> c.getMinimumSize());		
		return d;
	}
	
	/**
	 * Calculates specified layout size. 
	 * @param parent parent container
	 * @param func function to use for calcuation
	 * @return calculated size
	 */
	private Dimension calculateSize(Container parent, Function<Component, Dimension> func) {
		Insets insets = parent.getInsets();
		int numberOfComp = parent.getComponentCount();
		int width = 0;
		int height = 0;

		
		for (int i = 0; i < numberOfComp; i++) {
			Component c = parent.getComponent(i);
			Dimension d;
			RCPosition pos = constraintsColl.get(c);
			if (pos == null) {
				continue;
			}
			
			if (constraintsColl.get(c).equals(new RCPosition(1, 1))) {
				Dimension temp = func.apply(c);
				d = new Dimension(temp.width / 5, temp.height / 5);
			} else {
				d = func.apply(c);
			}
			
            if (d == null) {
            	continue;
            }
            
            if (width < d.width) {
                width = d.width;
            }
            if (height < d.height) {
                height = d.height;
            }
		}

		return new Dimension(insets.left + insets.right + MAX_COLUMN*width + (MAX_COLUMN-1)*gap,
				insets.top + insets.bottom + MAX_ROW*height + (MAX_ROW-1)*gap);
	}
	
	
	@Override
	public void layoutContainer(Container parent) {
		Insets insets = parent.getInsets();
		int maxWidth = (parent.getWidth()- (insets.left + insets.right + MAX_COLUMN * gap)) / MAX_COLUMN;
		int maxHeight = (parent.getHeight()- (insets.top + insets.bottom + MAX_ROW * gap)) / MAX_ROW;
		int nComps = parent.getComponentCount();
		int x = insets.left , y = insets.top;
		
		for (int i = 0; i < nComps; i++) {
			Component c = parent.getComponent(i);
			RCPosition pos = constraintsColl.get(c);
			if (pos == null) {
				continue;
			}
			
			if (pos.getColumn() == 1 && pos.getRow() == 1) {
				c.setBounds(x, y, (maxWidth + gap) * 5 - gap, maxHeight);
				continue;
			}
			
			x = insets.left + ((maxWidth + gap) * (pos.getColumn()-1));
			y = insets.top +  ((maxHeight + gap) * (pos.getRow()-1));
			c.setBounds(x, y, maxWidth, maxHeight);	
		}
		
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		//operation not supported 
		throw new UnsupportedOperationException();
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		Dimension d = calculateSize(target, (c) -> c.getMaximumSize());
		return d;
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0.5f;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0.5f;
	}

	@Override
	public void invalidateLayout(Container target) {
		
		
	}
	

}
