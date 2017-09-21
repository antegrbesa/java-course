package hr.fer.zemris.java.hw04.db;

import java.util.List;

/**
 * Implementation of {@link IFilter} interface. It gets a list of {@link ConditionalExpression}
 * through it's constructor and uses members of ConditionalExpression to filter given StudentRecord.
 * @author Ante
 *
 */
public class QueryFilter implements IFilter {
	/**List of conditional expressions*/
	private List<ConditionalExpression> list;
	
	/**
	 * Constructs an instance of this class with specified list of {@link ConditionalExpression}
	 * @param list list to set
	 */
	public QueryFilter(List<ConditionalExpression> list) {
		this.list = list;
	}
	
	@Override
	public boolean accepts(StudentRecord record) {
		boolean accepts = false;
		for(ConditionalExpression e : list) {
			if(e.getComparisonOperator().satisfied(e.getFieldGetter().get(record)
					, e.getStringLiteral())) {
				accepts = true;
			} else {
				accepts = false;
				break;
			}
		}
		
		return accepts;
	}

}
