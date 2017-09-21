package hr.fer.zemris.java.hw14.dao;

import java.util.List;

import hr.fer.zemris.java.hw14.model.DataModel;
import hr.fer.zemris.java.hw14.model.PollModel;

/**
 * Interface towards subsystem for data persistency. 
 * @author Ante Grbeša
 *
 */
public interface DAO {

	/**
	 * Returns a list of all polls from Polls table.
	 * @return list of all polls
	 * @throws DAOException if an exception occurs
	 */
	public List<PollModel> getPolls() throws DAOException;

	/**
	 * Returns a list of all data for specified poll from pollsOptions table. 
	 * @param pollID id of poll 
	 * @return a list of all data for specified poll
	 * @throws DAOException if an exception occurs
	 */
	public List<DataModel> getPollData(long pollID) throws DAOException;

	/**
	 * Returns a single entry with given ID. 
	 * @param id id to search for
	 * @return single entry
	 */
	public DataModel getByID(long id);

	/**
	 * Updates specified entry in pollsOptions table. 
	 * @param model model to update
	 */
	public void updateOption(DataModel model);

}