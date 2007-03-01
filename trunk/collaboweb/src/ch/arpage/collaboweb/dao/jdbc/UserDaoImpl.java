/**
 * collaboweb5
 * Dec 10, 2006
 */
package ch.arpage.collaboweb.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.JdbcUtils;

import ch.arpage.collaboweb.dao.UserDao;
import ch.arpage.collaboweb.model.Community;
import ch.arpage.collaboweb.model.User;

/**
 * UserDaoImpl
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class UserDaoImpl extends JdbcDaoSupport implements UserDao {

	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.UserDao#get(java.lang.String, java.lang.String, java.lang.String)
	 */
	public User get(final String community, final String email, final String password) {
		return (User) super.getJdbcTemplate().execute(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) 
					throws SQLException {
				PreparedStatement stmt = conn.prepareStatement("SELECT r.*, ra.*, a.* FROM resources r INNER JOIN resources c ON c.RESOURCE_ID=r.COMMUNITY_ID INNER JOIN attributes ca ON c.TYPE_ID=ca.TYPE_ID INNER JOIN resource_attributes cra ON cra.RESOURCE_ID=c.RESOURCE_ID AND cra.ATTRIBUTE_ID=ca.ATTRIBUTE_ID INNER JOIN attributes pa ON r.TYPE_ID=pa.TYPE_ID INNER JOIN resource_attributes pra ON pra.RESOURCE_ID=r.RESOURCE_ID AND pra.ATTRIBUTE_ID=pa.ATTRIBUTE_ID INNER JOIN attributes ma ON r.TYPE_ID=ma.TYPE_ID INNER JOIN resource_attributes mra ON mra.RESOURCE_ID=r.RESOURCE_ID AND mra.ATTRIBUTE_ID=ma.ATTRIBUTE_ID INNER JOIN attributes a ON r.TYPE_ID=a.TYPE_ID LEFT JOIN resource_attributes ra ON ra.RESOURCE_ID=r.RESOURCE_ID AND ra.ATTRIBUTE_ID=a.ATTRIBUTE_ID WHERE ca.IDENTIFIER='nickname' AND cra.VARCHAR_VALUE=? AND pa.IDENTIFIER='email' AND pra.VARCHAR_VALUE=? AND ma.IDENTIFIER='password' AND mra.VARCHAR_VALUE=?");
				stmt.setString(1, community);
				stmt.setString(2, email);
				stmt.setString(3, password);
				return stmt;
			}
		}, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement stmt) 
					throws SQLException, DataAccessException {
				return createUser(stmt.executeQuery(), stmt.getConnection());
			}
		});
	}

	/**
	 * Creates a user based on the given resultset
	 * @param rs		The resultset
	 * @param conn		The open connection
	 * @return			The created user
	 * @throws SQLException
	 */
	private Object createUser(ResultSet rs, Connection conn) throws SQLException {
		User user = null;
		long communityId = 0;
		while (rs.next()) {
			if (user == null) {
				communityId = rs.getLong("COMMUNITY_ID");
				user = new User();
				user.setResourceId(rs.getLong("RESOURCE_ID"));
			}
			if ("first-name".equals(rs.getString("IDENTIFIER"))) {
				user.setFirstName(rs.getString("VARCHAR_VALUE"));
			} else if ("last-name".equals(rs.getString("IDENTIFIER"))) {
				user.setLastName(rs.getString("VARCHAR_VALUE"));
			} else if ("email".equals(rs.getString("IDENTIFIER"))) {
				user.setEmail(rs.getString("VARCHAR_VALUE"));
			} else if ("language".equals(rs.getString("IDENTIFIER"))) {
				user.setLanguage(rs.getString("VARCHAR_VALUE"));
			} else if ("last-login".equals(rs.getString("IDENTIFIER"))) {
				user.setLastLogin(rs.getString("VARCHAR_VALUE"));
			}
		}
		if (user != null) {
			user.setCommunity(createCommunity(communityId, conn));
			return user;
		} else {
			throw new DataRetrievalFailureException("User not found");
		}
	}

	/**
	 * Creates the community object for the given community's ID
	 * @param communityId	The community's ID
	 * @param conn			The open connection
	 * @return				The created community object
	 */
	private Community createCommunity(long communityId, Connection conn) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement("SELECT r.*, ra.*, a.* FROM resources r LEFT JOIN attributes a ON r.TYPE_ID=a.TYPE_ID LEFT JOIN resource_attributes ra ON ra.RESOURCE_ID=r.RESOURCE_ID AND ra.ATTRIBUTE_ID=a.ATTRIBUTE_ID WHERE r.RESOURCE_ID=?");
			stmt.setLong(1, communityId);
			rs = stmt.executeQuery();
			Community community = null;
			while (rs.next()) {
				if (community == null) {
					community = new Community();
					community.setCommunityId(communityId);
					community.setName(rs.getString("NAME"));
				}
				if ("nickname".equals(rs.getString("IDENTIFIER"))) {
					community.setNickname(rs.getString("VARCHAR_VALUE"));
				} 
			} 
			if (community != null) {
				setCommunityRoots(community, conn);
				return community;
			} else {
				throw new DataRetrievalFailureException("User not found");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(stmt);
		}
	}

	/**
	 * Sets the community roots (resources and people) for the given
	 * community
	 * @param community		The community object
	 * @param conn			The open connection
	 */
	private void setCommunityRoots(Community community, Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement("SELECT RESOURCE_ID, NAME FROM resources WHERE PARENT_ID=?");
			stmt.setLong(1, community.getCommunityId());
			rs = stmt.executeQuery();
			while (rs.next()) {
				if ("Resources".equalsIgnoreCase(rs.getString("NAME"))) {
					community.setResourceRootId(rs.getLong("RESOURCE_ID"));
				} else if ("People".equalsIgnoreCase(rs.getString("NAME"))) {
					community.setPeopleRootId(rs.getLong("RESOURCE_ID"));
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(stmt);
		}
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.UserDao#logout(ch.arpage.collaboweb.model.User)
	 */
	public void logout(final User user) {
		getJdbcTemplate().update(new PreparedStatementCreator() {
			/* (non-Javadoc)
			 * @see org.springframework.jdbc.core.PreparedStatementCreator#createPreparedStatement(java.sql.Connection)
			 */
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"REPLACE resource_attributes (RESOURCE_ID, ATTRIBUTE_ID, VARCHAR_VALUE) SELECT ?, ATTRIBUTE_ID, NOW() FROM attributes WHERE TYPE_ID=2 and IDENTIFIER='last-login'");
				stmt.setLong(1, user.getResourceId());
				return stmt;
			}
		});
	}

}
