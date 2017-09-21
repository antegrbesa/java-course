package hr.fer.zemris.java.tecaj_13.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOException;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Class represents a Java Persistence API implementation of {@link DAO} interface. 
 * @author Ante Grbeša
 *
 */
public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		BlogEntry blogEntry = JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
		return blogEntry;
	}

	@Override
	public BlogUser getBlogUser(Long id) throws DAOException {
		BlogUser user = JPAEMProvider.getEntityManager().find(BlogUser.class, id);
		return user;
	}

	@Override
	public void addBlogUser(BlogUser user) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		
		em.persist(user);		
	}

	@Override
	public List<BlogUser> getAuthors() throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		List<BlogUser> authors = (List<BlogUser>) em.createQuery(
				"SELECT DISTINCT a FROM BlogUser a, IN(a.blogs) b")
				.getResultList();
		
		return authors;
	}

	@Override
	public boolean containsNick(String nick) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		List<BlogUser> users = (List<BlogUser>) em.createQuery(
				"SELECT DISTINCT a FROM BlogUser a")
				.getResultList();
		
		boolean present = false;
		for (BlogUser u : users) {
			if (u.getNick().equals(nick)) {
				present = true;
				break;
			}
		}
		
		return present;
	}

	@Override
	public BlogUser getBlogUserByNick(String nick) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		BlogUser user = null;
		try {
			user = (BlogUser) em.createQuery(
					"SELECT a FROM BlogUser a WHERE a.nick=:ni")
					.setParameter("ni", nick)
					.getSingleResult();
		} catch (NoResultException e) {
			throw new IllegalArgumentException();
		}
		
		return user;
	}

	@Override
	public void addBlogEntry(BlogEntry e) throws DAOException {
		if (e.getId() != null) {
			BlogEntry existing = getBlogEntry(e.getId());
			existing.update(e);
		} else {
			JPAEMProvider.getEntityManager().persist(e);
		}
	}
	
	

}