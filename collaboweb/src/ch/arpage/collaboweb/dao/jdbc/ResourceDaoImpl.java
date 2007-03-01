/**
 * collaboweb5
 * Dec 10, 2006
 */
package ch.arpage.collaboweb.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.struts.upload.FormFile;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.JdbcUtils;

import ch.arpage.collaboweb.dao.ResourceDao;
import ch.arpage.collaboweb.dao.ResourceTypeDao;
import ch.arpage.collaboweb.exceptions.DaoException;
import ch.arpage.collaboweb.model.Attribute;
import ch.arpage.collaboweb.model.BinaryAttribute;
import ch.arpage.collaboweb.model.Category;
import ch.arpage.collaboweb.model.Model;
import ch.arpage.collaboweb.model.Rating;
import ch.arpage.collaboweb.model.Relationship;
import ch.arpage.collaboweb.model.Resource;
import ch.arpage.collaboweb.model.ResourceAttribute;
import ch.arpage.collaboweb.model.TagStatistic;
import ch.arpage.collaboweb.model.User;

/**
 * Implementation of the Resource Data Access Object based on the Spring
 * JdbcDaoSupport class.
 *
 * @see	   org.springframework.jdbc.core.support.JdbcDaoSupport
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ResourceDaoImpl extends JdbcDaoSupport implements ResourceDao {
	
	private ResourceTypeDao resourceTypeDao;
	
	/**
	 * Set the resourceTypeDao.
	 * @param resourceTypeDao the resourceTypeDao to set
	 */
	public void setResourceTypeDao(ResourceTypeDao resourceTypeDao) {
		this.resourceTypeDao = resourceTypeDao;
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceDao#delete(ch.arpage.collaboweb.model.Resource)
	 */
	public void delete(final Resource bean) {
		getJdbcTemplate().update(new PreparedStatementCreator() {
			/* (non-Javadoc)
			 * @see org.springframework.jdbc.core.PreparedStatementCreator#createPreparedStatement(java.sql.Connection)
			 */
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"DELETE r, ra, rg, rt, rr, rc FROM resources r LEFT JOIN resource_attributes ra ON r.RESOURCE_ID=ra.RESOURCE_ID LEFT JOIN resource_tags rt ON r.RESOURCE_ID=rt.RESOURCE_ID LEFT JOIN resource_ratings rg ON r.RESOURCE_ID=rg.RESOURCE_ID LEFT JOIN resource_categories rc ON r.RESOURCE_ID=rc.RESOURCE_ID LEFT JOIN relationships rr ON r.RESOURCE_ID=rr.FROM_ID OR r.RESOURCE_ID=rr.TO_ID WHERE r.RESOURCE_ID=?");
				stmt.setLong(1, bean.getResourceId());
				return stmt;
			}
		});
	}	
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceDao#get(long)
	 */
	public Resource get(final long id, User user) {
		return (Resource) super.getJdbcTemplate().execute(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) 
					throws SQLException {
				PreparedStatement stmt = conn.prepareStatement("SELECT r.*, author.NAME AS AUTHOR_NAME FROM resources r LEFT JOIN resources author ON r.AUTHOR_ID=author.RESOURCE_ID WHERE r.RESOURCE_ID=?");
				stmt.setLong(1, id);
				return stmt;
			}
		}, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement stmt) 
					throws SQLException, DataAccessException {
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					return createResource(rs, stmt.getConnection());
				}
				throw new DataRetrievalFailureException(id + " not found");
			}
		});
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceDao#getList(long)
	 */
	@SuppressWarnings("unchecked")
	public List<Resource> getList(final long id, final User user) {
		return (List<Resource>) super.getJdbcTemplate().execute(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(final Connection conn) 
					throws SQLException {
				final PreparedStatement stmt = conn.prepareStatement("SELECT r.*, ra.*, author.NAME AS AUTHOR_NAME FROM resources r LEFT JOIN resources author ON r.AUTHOR_ID=author.RESOURCE_ID INNER JOIN resource_types rt ON r.TYPE_ID=rt.TYPE_ID LEFT JOIN attributes a ON rt.TYPE_ID=a.TYPE_ID AND a.LOAD_IN_LIST=1 LEFT JOIN resource_attributes ra ON r.RESOURCE_ID=ra.RESOURCE_ID AND a.ATTRIBUTE_ID=ra.ATTRIBUTE_ID WHERE r.PARENT_ID=? AND r.COMMUNITY_ID=? ORDER BY r.NAME, r.RESOURCE_ID, r.UPDATE_DATE");
				stmt.setLong(1, id);
				stmt.setLong(2, user.getCommunityId());
				return stmt;
			}
		}, new PreparedStatementCallback() {
			public Object doInPreparedStatement(final PreparedStatement stmt) 
					throws SQLException, DataAccessException {
				final ResultSet rs = stmt.executeQuery();
				List<Resource> list = new LinkedList<Resource>();
				Resource resource = null;
				while (rs.next()) {
					long resourceId = rs.getLong("RESOURCE_ID");
					if (resource == null || resource.getResourceId() != resourceId) {
						resource = createListResource(rs);
						list.add(resource);
					}
					resource.addResourceAttribute(createResourceAttribute(rs));
				} 
				return list;
			}
		});
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceDao#insert(ch.arpage.collaboweb.model.Resource)
	 */
	public void insert(final Resource bean) {
		super.getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(final Connection conn) 
					throws SQLException {
				final PreparedStatement stmt = conn.prepareStatement("INSERT INTO resources (RESOURCE_ID, PARENT_ID, COMMUNITY_ID, TYPE_ID, NAME, AUTHOR_ID, CREATE_DATE, UPDATE_DATE, ARCHIVED, STATUS, VISIBILITY) VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW(), ?, ?, ?)");
				stmt.setLong(1, bean.getResourceId());
				stmt.setLong(2, bean.getParentId());
				stmt.setLong(3, bean.getCommunityId());
				stmt.setInt(4, bean.getResourceType().getTypeId());
				stmt.setString(5, bean.getName());
				stmt.setLong(6, bean.getAuthorId());
				stmt.setBoolean(7, bean.isArchived());
				stmt.setInt(8, bean.getStatus());
				stmt.setInt(9, bean.getVisibility());
				return stmt;
			}
		});
		updateAttributeValues(bean);
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceDao#update(ch.arpage.collaboweb.model.Resource)
	 */
	public void update(final Resource bean) {
		super.getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(final Connection conn) 
					throws SQLException {
				final PreparedStatement stmt = conn.prepareStatement("UPDATE resources SET PARENT_ID=?, NAME=?, CREATE_DATE=CREATE_DATE, UPDATE_DATE=NOW(), ARCHIVED=?, STATUS=?, VISIBILITY=? WHERE RESOURCE_ID=?");
				stmt.setLong(1, bean.getParentId());
				stmt.setString(2, bean.getName());
				stmt.setBoolean(3, bean.isArchived());
				stmt.setInt(4, bean.getStatus());
				stmt.setInt(5, bean.getVisibility());
				stmt.setLong(6, bean.getResourceId());
				return stmt;
			}
		});
		updateAttributeValues(bean);
	}

	/**
	 * @param bean
	 * @param conn
	 */
	private void updateAttributeValues(Resource bean) {
		PreparedStatement rStmt = null;
		Connection conn = null;
		try {
			conn = getConnection();
			rStmt = conn.prepareStatement("REPLACE INTO resource_attributes (RESOURCE_ID, ATTRIBUTE_ID, VARCHAR_VALUE, BLOB_VALUE, CONTENT_TYPE) VALUES (?,?,?,?,?)");
			rStmt.setLong(1, bean.getResourceId());
			for (Iterator<Attribute> iter = 
				bean.getResourceType().getAttributes().iterator(); iter.hasNext(); ) {
				Attribute attribute = iter.next();
				ResourceAttribute resourceAttribute = 
					bean.getResourceAttribute(attribute.getAttributeId());
				if (resourceAttribute != null) {
					Object value = resourceAttribute.getValue();
					if (value != null) {
						rStmt.setInt(2, attribute.getAttributeId());
						if (attribute.getDataType() == Model.DATA_TYPE_BINARY && 
								value instanceof FormFile) {
							FormFile formFile = (FormFile) value;
							if (formFile.getFileSize() > 0) {
								rStmt.setString(3, formFile.getFileName());
								rStmt.setBinaryStream(4, formFile.getInputStream(), -1);
								rStmt.setString(5, formFile.getContentType());
								rStmt.executeUpdate();
							}
						} else {
							rStmt.setObject(3, value);
							rStmt.setBlob(4, null);
							rStmt.setString(5, null);
							rStmt.executeUpdate();
						}
					}
				}
			}
		} catch (Exception ioe) {
			throw new DaoException(ioe);
		} finally {
			JdbcUtils.closeStatement(rStmt);
			releaseConnection(conn);
		}
	}



	/**
	 * Creates a resource object (with all dependencies) based on the given 
	 * resultset.
	 * @param rs	The resultset
	 * @return		A resource object (with all dependencies)
	 * @throws SQLException
	 */
	private Resource createResource(ResultSet rs, Connection conn) throws SQLException {
		Resource resource = createListResource(rs);
		addResourceAttributes(resource, conn);
		resource.setRatings(getRatings(resource.getResourceId(), conn));
		resource.setTags(getTags(resource.getResourceId(), conn));
		resource.setCategories(getCategories(resource.getResourceId(), conn));
		resource.setRelationships(getRelationships(resource.getResourceId()));
		return resource;
	}

	/**
	 * Creates a resource object with attributes but without all dependencies
	 * based on the given resultset.
	 * @param rs	The resultset
	 * @return		A resource object with attributes but without all dependencies
	 * @throws SQLException
	 */
	private Resource createListResource(ResultSet rs) throws SQLException {
		long resourceId = rs.getLong("RESOURCE_ID"); 
		Resource resource = new Resource();
		resource.setResourceId(resourceId);
		resource.setArchived(rs.getBoolean("ARCHIVED"));
		resource.setAuthorId(rs.getLong("AUTHOR_ID"));
		resource.setAuthorName(rs.getString("AUTHOR_NAME"));
		resource.setCommunityId(rs.getLong("COMMUNITY_ID"));
		resource.setCreateDate(rs.getTimestamp("CREATE_DATE"));
		resource.setName(rs.getString("NAME"));
		resource.setParentId(rs.getLong("PARENT_ID"));
		resource.setStatus(rs.getInt("STATUS"));
		resource.setTypeId(rs.getInt("TYPE_ID"));
		resource.setUpdateDate(rs.getTimestamp("UPDATE_DATE"));
		resource.setValidFrom(rs.getTimestamp("VALID_FROM"));
		resource.setValidTo(rs.getTimestamp("VALID_TO"));
		resource.setVisibleFrom(rs.getTimestamp("VISIBLE_FROM"));
		resource.setVisibleTo(rs.getTimestamp("VISIBLE_TO"));
		resource.setVisibility(rs.getInt("VISIBILITY"));
		resource.setResourceType(resourceTypeDao.get(resource.getTypeId()));
		return resource;
	}



	/**
	 * Adds the attribute values to the given resource
	 * @param resource	The resource
	 * @param conn		The open connection
	 */
	private void addResourceAttributes(Resource resource, Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM resource_attributes WHERE RESOURCE_ID=?");
			stmt.setLong(1, resource.getResourceId());
			rs = stmt.executeQuery();
			while (rs.next()) {
				resource.addResourceAttribute(createResourceAttribute(rs));
			} 
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(stmt);
		}
	}

	/**
	 * Creates a Resource Attribute object
	 * @param rs	The resultset
	 * @return		The created Resource Attribute
	 */
	private ResourceAttribute createResourceAttribute(ResultSet rs) throws SQLException {
		ResourceAttribute bean = new ResourceAttribute();
		bean.setAttributeId(rs.getInt("ATTRIBUTE_ID"));
		bean.setValue(rs.getString("VARCHAR_VALUE"));
		bean.setValidFrom(rs.getTimestamp("VALID_FROM"));
		bean.setValidTo(rs.getTimestamp("VALID_TO"));
		bean.setContentType(rs.getString("CONTENT_TYPE"));
		return bean;
	}



	/**
	 * Returns the list of ratings for the give resource.
	 * 
	 * @param resourceId	The ID of the resource
	 * @param conn			The open connection
	 * @return				The list of ratings for the give resource.
	 */
	private List getRatings(long resourceId, Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM resource_ratings WHERE RESOURCE_ID=?");
			stmt.setLong(1, resourceId);
			rs = stmt.executeQuery();
			List<Rating> list = new LinkedList<Rating>();
			Rating bean = null;
			while (rs.next()) {
				bean = new Rating();
				bean.setAuthorId(rs.getLong("AUTHOR_ID"));
				bean.setValue(rs.getInt("VALUE"));
				bean.setTimestamp(rs.getTimestamp("TIMESTAMP"));
				list.add(bean);
			} 
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(stmt);
		}
	}


	/**
	 * Returns the set of tags for the given resource.
	 * 
	 * @param resourceId	The ID of the resource
	 * @param conn			The open connection
	 * @return				The set of tags for the given resource.
	 */
	private Set<TagStatistic> getTags(long resourceId, Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement("SELECT t.TAG, COUNT(*) AS COUNTER FROM resource_tags t INNER JOIN resources r ON r.RESOURCE_ID=t.RESOURCE_ID WHERE r.RESOURCE_ID=? GROUP BY t.TAG ORDER BY COUNTER DESC LIMIT 100");
			stmt.setLong(1, resourceId);
			rs = stmt.executeQuery();
			Set<TagStatistic> set = new TreeSet<TagStatistic>(new Comparator<TagStatistic>() {
				public int compare(TagStatistic o1, TagStatistic o2) {
					return o1.getTag().compareTo(o2.getTag());
				}
			});
			int max = -1;
			while (rs.next()) {
				if (max == -1) {
					max = rs.getInt(2);
				}
				set.add(new TagStatistic(rs.getString(1), rs.getInt(2), max));
			} 
			return set;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(stmt);
		}
	}



	/**
	 * Returns the list of categories for the given resource.
	 * 
	 * @param resourceId	The ID of the resource
	 * @param conn			The open connection
	 * @return				The list of categories for the given resource.
	 */
	private List<Category> getCategories(long resourceId, Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM resource_categories WHERE RESOURCE_ID=?");
			stmt.setLong(1, resourceId);
			rs = stmt.executeQuery();
			List<Category> list = new LinkedList<Category>();
			Category bean = null;
			while (rs.next()) {
				bean = new Category();
				bean.setCategoryId(rs.getInt("CATEGORY_ID"));
				bean.setName(rs.getString("NAME"));
				//bean.setParent(parent)
				list.add(bean);
			} 
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(stmt);
		}
	}



	/**
	 * Returns the list of relationships for the given resource.
	 * 
	 * @param resourceId	The ID of the resource
	 * @return				The list of relationships for the given resource.
	 */
	@SuppressWarnings("unchecked")
	private List<Relationship> getRelationships(final long resourceId) {
		return (List<Relationship>) super.getJdbcTemplate().execute(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(final Connection conn) 
					throws SQLException {
				final PreparedStatement stmt = conn.prepareStatement("SELECT 1 AS DIRECTION, rl.RELATIONSHIP_TYPE_ID, rl.TO_ID AS ID, r.*, ra.*, author.NAME AS AUTHOR_NAME FROM relationships rl INNER JOIN resources r ON r.RESOURCE_ID=rl.TO_ID LEFT JOIN resources author ON r.AUTHOR_ID=author.RESOURCE_ID INNER JOIN resource_types rt ON r.TYPE_ID=rt.TYPE_ID LEFT JOIN attributes a ON rt.TYPE_ID=a.TYPE_ID AND a.LOAD_IN_LIST=1 LEFT JOIN resource_attributes ra ON r.RESOURCE_ID=ra.RESOURCE_ID AND a.ATTRIBUTE_ID=ra.ATTRIBUTE_ID WHERE rl.FROM_ID=? UNION SELECT 0 AS DIRECTION, rl.RELATIONSHIP_TYPE_ID, rl.FROM_ID AS ID, r.*, ra.*, author.NAME AS AUTHOR_NAME FROM relationships rl INNER JOIN resources r ON r.RESOURCE_ID=rl.FROM_ID LEFT JOIN resources author ON r.AUTHOR_ID=author.RESOURCE_ID INNER JOIN resource_types rt ON r.TYPE_ID=rt.TYPE_ID LEFT JOIN attributes a ON rt.TYPE_ID=a.TYPE_ID AND a.LOAD_IN_LIST=1 LEFT JOIN resource_attributes ra ON r.RESOURCE_ID=ra.RESOURCE_ID AND a.ATTRIBUTE_ID=ra.ATTRIBUTE_ID WHERE rl.TO_ID=? ORDER BY DIRECTION, RELATIONSHIP_TYPE_ID, ID");
				stmt.setLong(1, resourceId);
				stmt.setLong(2, resourceId);
				return stmt;
			}
		}, new PreparedStatementCallback() {
			public Object doInPreparedStatement(final PreparedStatement stmt) 
					throws SQLException, DataAccessException {
				final ResultSet rs = stmt.executeQuery();
				List<Relationship> list = new LinkedList<Relationship>();
				Resource resource = null;
				Relationship relationship = null;
				while (rs.next()) {
					int relationshipTypeId = rs.getInt("RELATIONSHIP_TYPE_ID");
					long resourceId = rs.getLong("RESOURCE_ID");
					if (relationship == null || 
							relationship.getRelatedResource().getResourceId() != resourceId || 
							relationship.getRelationshipType().getRelationshipTypeId() != relationshipTypeId) {
						resource = createListResource(rs);
						relationship = new Relationship();
						relationship.setOutgoing(rs.getInt("DIRECTION") == 1);
						relationship.setRelatedResource(resource);
						relationship.setRelationshipType(
								resourceTypeDao.getRelationshipType(relationshipTypeId));
						list.add(relationship);
					}
					resource.addResourceAttribute(createResourceAttribute(rs));
				} 
				return list;
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceDao#getTagCloud(java.lang.String[], long)
	 */
	@SuppressWarnings("unchecked")
	public Set<TagStatistic> getTagCloud(final String[] tags, final User user) {
		return (Set<TagStatistic>) super.getJdbcTemplate().execute(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(final Connection conn) 
					throws SQLException {
				PreparedStatement stmt = null;
				if (tags == null || tags.length == 0) {
					stmt = conn.prepareStatement("SELECT t.TAG, COUNT(*) AS COUNTER FROM resource_tags t INNER JOIN resources r ON r.RESOURCE_ID=t.RESOURCE_ID WHERE COMMUNITY_ID=? GROUP BY t.TAG ORDER BY COUNTER DESC LIMIT 100");
					stmt.setLong(1, user.getCommunityId());
				} else {
					StringBuffer sql = new StringBuffer();
					sql.append("SELECT t.TAG, COUNT(DISTINCT t.RESOURCE_ID) AS COUNTER FROM resource_tags t INNER JOIN resources r ON r.RESOURCE_ID=t.RESOURCE_ID");
					for (int i = 0; i < tags.length; i++) {
						sql.append(" INNER JOIN resource_tags t")
							.append(i)
							.append(" ON r.RESOURCE_ID=t")
							.append(i)
							.append(".RESOURCE_ID AND t")
							.append(i)
							.append(".TAG=?");
					}
					sql.append(" WHERE COMMUNITY_ID=? AND t.TAG NOT IN (");
					for (int i = 0; i < tags.length; i++) {
						if (i != 0) {
							sql.append(',');
						}
						sql.append('?');
					}
					sql.append(") GROUP BY t.TAG ORDER BY COUNTER DESC LIMIT 100");
					stmt = conn.prepareStatement(sql.toString());
					for (int i = 0; i < tags.length; i++) {
						stmt.setString(i+1, tags[i]);
						stmt.setString(i+1+tags.length+1, tags[i]);
					}
					stmt.setLong(tags.length+1, user.getCommunityId());
				}
				return stmt;
			}
		}, new PreparedStatementCallback() {
			public Object doInPreparedStatement(final PreparedStatement stmt) 
					throws SQLException, DataAccessException {
				final ResultSet rs = stmt.executeQuery();
				Set<TagStatistic> list = new TreeSet<TagStatistic>(new Comparator<TagStatistic>() {
					/* (non-Javadoc)
					 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
					 */
					public int compare(TagStatistic o1, TagStatistic o2) {
						return o1.getTag().compareTo(o2.getTag());
					}
				});
				int max = -1;
				while (rs.next()) {
					if (max == -1) {
						max = rs.getInt(2);
					}
					list.add(new TagStatistic(rs.getString(1), rs.getInt(2), max));
				} 
				return list;
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceDao#getTagList(java.lang.String[], long)
	 */
	@SuppressWarnings("unchecked")
	public List<Resource> getTagList(final String[] tags, final User user) {
		return (List<Resource>) super.getJdbcTemplate().execute(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(final Connection conn) 
					throws SQLException {
				PreparedStatement stmt = null;
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT r.*, ra.*, author.NAME AS AUTHOR_NAME FROM resources r");
				for (int i = 0; i < tags.length; i++) {
					sql.append(" INNER JOIN resource_tags t")
						.append(i)
						.append(" ON r.RESOURCE_ID=t")
						.append(i)
						.append(".RESOURCE_ID AND t")
						.append(i)
						.append(".TAG=?");
				}
				sql.append(" INNER JOIN resource_types rt ON r.TYPE_ID=rt.TYPE_ID LEFT JOIN resources author ON r.AUTHOR_ID=author.RESOURCE_ID LEFT JOIN attributes a ON rt.TYPE_ID=a.TYPE_ID AND a.LOAD_IN_LIST=1 LEFT JOIN resource_attributes ra ON r.RESOURCE_ID=ra.RESOURCE_ID AND a.ATTRIBUTE_ID=ra.ATTRIBUTE_ID WHERE r.COMMUNITY_ID=? ORDER BY rt.FAMILY_ID, r.NAME, r.RESOURCE_ID, r.UPDATE_DATE");
				stmt = conn.prepareStatement(sql.toString());
				for (int i = 0; i < tags.length; i++) {
					stmt.setString(i+1, tags[i]);
				}
				stmt.setLong(tags.length+1, user.getCommunityId());
				return stmt;
			}
		}, new PreparedStatementCallback() {
			public Object doInPreparedStatement(final PreparedStatement stmt) 
					throws SQLException, DataAccessException {
				final ResultSet rs = stmt.executeQuery();
				List<Resource> list = new LinkedList<Resource>();
				Resource resource = null;
				while (rs.next()) {
					long resourceId = rs.getLong("RESOURCE_ID");
					if (resource == null || resource.getResourceId() != resourceId) {
						resource = createListResource(rs);
						list.add(resource);
					}
					resource.addResourceAttribute(createResourceAttribute(rs));
				} 
				return list;
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceDao#readAttribute(long, attributeIdentifier)
	 */
	public BinaryAttribute readAttribute(final long resourceId, final String attributeIdentifier) {
		return (BinaryAttribute) super.getJdbcTemplate().execute(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) 
					throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT r.* FROM resource_attributes r INNER JOIN attributes a ON a.ATTRIBUTE_ID=r.ATTRIBUTE_ID WHERE r.RESOURCE_ID=? AND a.IDENTIFIER=?");
				stmt.setLong(1, resourceId);
				stmt.setString(2, attributeIdentifier);
				return stmt;
			}
		}, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement stmt) 
					throws SQLException, DataAccessException {
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					return new BinaryAttribute(rs.getString("VARCHAR_VALUE"),
							rs.getBinaryStream("BLOB_VALUE"),
							rs.getString("CONTENT_TYPE"));
				} 
				throw new EmptyResultDataAccessException(1);
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceDao#addTag(long, java.lang.String, long)
	 */
	public void addTag(final long resourceId, final String tag, final long userId) {
		getJdbcTemplate().update(new PreparedStatementCreator() {
			/* (non-Javadoc)
			 * @see org.springframework.jdbc.core.PreparedStatementCreator#createPreparedStatement(java.sql.Connection)
			 */
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"REPLACE resource_tags VALUES(?,?,?,NOW())");
				stmt.setLong(1, resourceId);
				stmt.setLong(2, userId);
				stmt.setString(3, tag);
				return stmt;
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceDao#deleteTag(long, java.lang.String, long)
	 */
	public void deleteTag(final long resourceId, final String tag, final long userId) {
		getJdbcTemplate().update(new PreparedStatementCreator() {
			/* (non-Javadoc)
			 * @see org.springframework.jdbc.core.PreparedStatementCreator#createPreparedStatement(java.sql.Connection)
			 */
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"DELETE FROM resource_tags RESOURCE_ID=? AND AUTHOR_ID=? AND TAG=?");
				stmt.setLong(1, resourceId);
				stmt.setLong(2, userId);
				stmt.setString(3, tag);
				return stmt;
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceDao#addRelationships(long, int, java.util.Set)
	 */
	public void addRelationships(final long id, final int relationshipType, final Set<Long> references) {
		PreparedStatement stmt = null;
		Connection conn = null;
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(
					"REPLACE relationships VALUES (?,?,?)");
			stmt.setLong(1, relationshipType);
			stmt.setLong(2, id);
			for (long referencedId : references) {
				stmt.setLong(3, referencedId);
				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtils.closeStatement(stmt);
			releaseConnection(conn);
		}
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceDao#deleteRelationship(long, int, long)
	 */
	public void deleteRelationship(final long id, final int relationshipType, final long referencedId) {
		getJdbcTemplate().update(new PreparedStatementCreator() {
			/* (non-Javadoc)
			 * @see org.springframework.jdbc.core.PreparedStatementCreator#createPreparedStatement(java.sql.Connection)
			 */
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"DELETE FROM relationships WHERE RELATIONSHIP_TYPE_ID=? AND FROM_ID=? AND TO_ID=?");
				stmt.setInt(1, relationshipType);
				stmt.setLong(2, id);
				stmt.setLong(3, referencedId);
				return stmt;
			}
		});
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceDao#move(long, long)
	 */
	public void move(final long id, final long to) {
		getJdbcTemplate().update(new PreparedStatementCreator() {
			/* (non-Javadoc)
			 * @see org.springframework.jdbc.core.PreparedStatementCreator#createPreparedStatement(java.sql.Connection)
			 */
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"UPDATE resources SET UPDATE_DATE=UPDATE_DATE, PARENT_ID=? WHERE RESOURCE_ID=?");
				stmt.setLong(1, to);
				stmt.setLong(2, id);
				return stmt;
			}
		});
	}
}
