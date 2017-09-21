package hr.fer.zemris.java.hw14.dao.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.hw14.dao.DAO;
import hr.fer.zemris.java.hw14.dao.DAOException;
import hr.fer.zemris.java.hw14.model.DataModel;
import hr.fer.zemris.java.hw14.model.PollModel;

/**
 * Implementation of {@link DAO} class. 
 * @author Ante Grbeša
 *
 */
public class SQLDAO implements DAO {

	@Override
	public DataModel getByID(long id) {
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		DataModel entry = null;
		try {
			pst = con.prepareStatement("SELECT * FROM pollOptions WHERE id=?");
			pst.setLong(1, id);
			try {
				ResultSet rs = pst.executeQuery();

				try {
					while (rs != null && rs.next()) {
						entry = new DataModel();
						entry.setId(rs.getLong(1));
						entry.setOptionTitle(rs.getString(2));
						entry.setOptionLink(rs.getString(3));
						entry.setPollID(rs.getLong(4));
						entry.setVotesCount(rs.getLong(5));
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Error while retrieving polls", ex);
		} finally {
			try {
				pst.close();
			} catch (Exception ignorable) {
			}
		}

		return entry;
	}

	@Override
	public List<PollModel> getPolls() throws DAOException {
		List<PollModel> polls = new LinkedList<>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("SELECT id, title, message FROM Polls ORDER BY id DESC");
			try {
				ResultSet rs = pst.executeQuery();

				try {
					while (rs != null && rs.next()) {
						PollModel poll = new PollModel();
						poll.setId(rs.getLong(1));
						poll.setTitle(rs.getString(2));
						poll.setMessage(rs.getString(3));
						polls.add(poll);
					}

				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Error while retrieving polls", ex);
		}
		return polls;
	}

	@Override
	public List<DataModel> getPollData(long pollID) throws DAOException {
		List<DataModel> data = new LinkedList<>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("SELECT id, optionTitle, optionLink, votesCount" + " FROM PollOptions"
					+ " WHERE PollOptions.pollID = ?" + " ORDER BY votesCount DESC");
			pst.setLong(1, Long.valueOf(pollID));
			try {
				ResultSet rs = pst.executeQuery();

				try {
					while (rs != null && rs.next()) {
						DataModel entry = new DataModel();
						entry.setId(rs.getLong(1));
						entry.setOptionTitle(rs.getString(2));
						entry.setOptionLink(rs.getString(3));
						entry.setVotesCount(rs.getLong(4));
						entry.setPollID(pollID);
						data.add(entry);
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Error while retrieving polls", ex);
		}
		return data;
	}

	@Override
	public void updateOption(DataModel model) {
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("UPDATE pollOptions SET votesCount=? WHERE id=?");
			pst.setLong(1, model.getVotesCount());
			pst.setLong(2, model.getId());
			pst.executeUpdate();
		} catch (Exception ex) {
			throw new DAOException("Error while retrieving polls", ex);
		} finally {
			try {
				pst.close();
			} catch (Exception ignorable) {
			}
		}

	}

}