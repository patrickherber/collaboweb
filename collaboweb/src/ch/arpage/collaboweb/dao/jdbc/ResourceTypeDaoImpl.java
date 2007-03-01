/**
 * collaboweb5
 * Dec 10, 2006
 */
package ch.arpage.collaboweb.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcUtils;

import ch.arpage.collaboweb.dao.ResourceTypeDao;
import ch.arpage.collaboweb.exceptions.DaoException;
import ch.arpage.collaboweb.model.Action;
import ch.arpage.collaboweb.model.Aspect;
import ch.arpage.collaboweb.model.Attribute;
import ch.arpage.collaboweb.model.Category;
import ch.arpage.collaboweb.model.RelationshipType;
import ch.arpage.collaboweb.model.ResourceType;
import ch.arpage.collaboweb.model.ResourceValidation;
import ch.arpage.collaboweb.model.ResourceValidationType;
import ch.arpage.collaboweb.model.Validation;
import ch.arpage.collaboweb.model.ValidationType;
import ch.arpage.collaboweb.model.View;
import ch.arpage.collaboweb.model.ViewType;

/**
 * ResourceTypeDaoImpl
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ResourceTypeDaoImpl extends JdbcDaoSupport implements ResourceTypeDao {

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#delete(ch.arpage.collaboweb.model.ResourceType)
	 */
	public void delete(final ResourceType bean) {
		getJdbcTemplate().update(new PreparedStatementCreator() {
			/* (non-Javadoc)
			 * @see org.springframework.jdbc.core.PreparedStatementCreator#createPreparedStatement(java.sql.Connection)
			 */
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				//TODO delete (or historyze) all related objects!
				PreparedStatement stmt = conn.prepareStatement("DELETE r, rl, a, rs, ra, rta, rv, rr, v, va, vs, s FROM resource_types r LEFT JOIN resource_type_labels rl ON r.TYPE_ID=rl.TYPE_ID LEFT JOIN attributes a ON r.TYPE_ID=a.TYPE_ID LEFT JOIN validations v ON a.ATTRIBUTE_ID=v.ATTRIBUTE_ID LEFT JOIN resources rs ON r.TYPE_ID=rs.TYPE_ID LEFT JOIN resource_attributes ra ON rs.RESOURCE_ID=ra.RESOURCE_ID LEFT JOIN rights rr ON rs.RESOURCE_ID=rr.RESOURCE_ID OR rs.RESOURCE_ID=rr.RIGHTS_OWNER_ID LEFT JOIN resource_type_aspects rta ON r.TYPE_ID=rta.TYPE_ID LEFT JOIN resource_validations rv ON r.TYPE_ID=rv.TYPE_ID LEFT JOIN supported_children s ON r.TYPE_ID=s.PARENT_TYPE_ID OR r.TYPE_ID=s.CHILD_TYPE_ID LEFT JOIN view_actions va ON r.TYPE_ID=va.TYPE_ID LEFT JOIN views vs ON r.TYPE_ID=vs.TYPE_ID WHERE r.TYPE_ID=?");
				stmt.setInt(1, bean.getTypeId());
				return stmt;
			}
		});		
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#get(int)
	 */
	public ResourceType get(final int id) {
		return (ResourceType) super.getJdbcTemplate().execute(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) 
					throws SQLException {
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM resource_types WHERE TYPE_ID=?");
				stmt.setInt(1, id);
				return stmt;
			}
		}, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement stmt) 
					throws SQLException, DataAccessException {
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					return createResourceType(rs, stmt.getConnection());
				} else {
					throw new DataRetrievalFailureException(id + " not found");
				}
			}
		});
	}
	
	/**
	 * Creates a resource type object (with all its dependencies) on the basis 
	 * of the given resultset. 
	 * @param rs	The resultset
	 * @param conn	The open connection
	 * @return		The created resource type object
	 */
	private ResourceType createResourceType(ResultSet rs, Connection conn) throws SQLException {
		ResourceType bean = new ResourceType();
		bean.setCommunityId(rs.getLong("COMMUNITY_ID"));
		bean.setFamilyId(rs.getInt("FAMILY_ID"));
		bean.setTimeView(rs.getBoolean("TIME_VIEW"));
		bean.setTypeId(rs.getInt("TIPE_ID"));
		bean.setAttributes(getAttributes(bean.getTypeId(), conn));
		bean.setAspects(getAspects(bean.getTypeId(), conn));
		bean.setCategories(getCategories(bean.getTypeId(), conn));
		bean.setSupportedChildrenIds(getSupportedChildrenIds(bean.getTypeId(), conn));
		bean.setViews(getViews(bean.getTypeId(), conn));
		bean.setResourceValidations(getResourceValidations(bean.getTypeId(), conn));
		do {
			bean.setLabel(rs.getString("LANGUAGE"), rs.getString("LABEL"));
		} while (rs.next());
		return bean;
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getAttribute(int)
	 */
	public Attribute getAttribute(final int id) {
		return (Attribute) super.getJdbcTemplate().execute(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) 
					throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT a.*, l.LANGUAGE, l.LABEL FROM attributes a LEFT JOIN attribute_labels l ON a.ATTRIBUTE_ID=l.ATTRIBUTE_ID WHERE a.ATTRIBUTE_ID=?");
				stmt.setInt(1, id);
				return stmt;
			}
		}, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement stmt) 
					throws SQLException, DataAccessException {
				ResultSet rs = stmt.executeQuery();
				Attribute attribute = null;
				while (rs.next()) {
					if (attribute == null) {
						attribute = createAttribute(rs, stmt.getConnection());
					}
					attribute.setLabel(rs.getString("LANGUAGE"), rs.getString("LABEL"));
				} 
				return attribute;
			}
		});
	}

	/**
	 * Returns the list of attributes of the given resource type
	 * @param typeId	The ID of the resource type
	 * @param conn		The open connection
	 * @return			The list of attributes of the given resource type
	 */
	private List<Attribute> getAttributes(int typeId, Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement("SELECT a.*, l.LANGUAGE, l.LABEL FROM attributes a LEFT JOIN attribute_labels l ON a.ATTRIBUTE_ID=l.ATTRIBUTE_ID WHERE a.TYPE_ID=? ORDER BY a.FORM_ORDER, a.ATTRIBUTE_ID");
			stmt.setInt(1, typeId);
			rs = stmt.executeQuery();
			List<Attribute> list = new LinkedList<Attribute>();
			int lastId = 0;
			Attribute attribute = null;
			while (rs.next()) {
				int id = rs.getInt(1);
				if (id != lastId) {
					lastId = id;
					attribute = createAttribute(rs, conn);
					list.add(attribute);
				}
				attribute.setLabel(rs.getString("LANGUAGE"), rs.getString("LABEL"));
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
	 * Returns the list of categories of the given resource type
	 * @param typeId	The ID of the resource type
	 * @param conn		The open connection
	 * @return			The list of categories of the given resource type
	 */
	private List<Category> getCategories(int typeId, Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM categories WHERE TYPE_ID=?");
			stmt.setInt(1, typeId);
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
	 * Returns the list of the IDs of the supported children of the given 
	 * resource type
	 * @param typeId	The ID of the resource type
	 * @param conn		The open connection
	 * @return			The list of IDs of the supported children of the given 
	 * resource type
	 */
	private List<Integer> getSupportedChildrenIds(int typeId, Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(
					"SELECT CHILD_TYPE_ID FROM supported_children WHERE PARENT_TYPE_ID=?");
			stmt.setInt(1, typeId);
			rs = stmt.executeQuery();
			List<Integer> list = new LinkedList<Integer>();
			while (rs.next()) {
				list.add(rs.getInt(1));
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
	 * Returns the list of views of the given resource type
	 * @param typeId	The ID of the resource type
	 * @param conn		The open connection
	 * @return			The list of views of the given resource type
	 */
	private List<View> getViews(int typeId, Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement("SELECT TYPE_ID, VIEW_TYPE_ID, STYLESHEET FROM views WHERE TYPE_ID=?");
			stmt.setInt(1, typeId);
			rs = stmt.executeQuery();
			List<View> list = new LinkedList<View>();
			View bean = null;
			while (rs.next()) {
				int viewTypeId = rs.getInt("VIEW_TYPE_ID");
				bean = new View();
				bean.setViewType(getViewType(viewTypeId, conn));
				bean.setActions(getActions(typeId, viewTypeId, conn));
				bean.setStyleSheet(rs.getString("STYLESHEET"));
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
	 * Returns the view type identified by the given <code>viewTypeId</code>.
	 * @param viewTypeId	The ID of the view type
	 * @param conn			The open connection
	 * @return				The created view type
	 */
	private ViewType getViewType(int viewTypeId, Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement("SELECT v.VIEW_TYPE_ID, v.COMMUNITY_ID, v.CONTENT_TYPE, l.LANGUAGE, l.LABEL FROM view_types v LEFT JOIN view_type_labels l ON v.VIEW_TYPE_ID=l.VIEW_TYPE_ID WHERE v.VIEW_TYPE_ID=?");
			stmt.setInt(1, viewTypeId);
			rs = stmt.executeQuery();
			int lastId = 0;
			ViewType bean = null;
			while (rs.next()) {
				int id = rs.getInt(1);
				if (id != lastId) {
					lastId = id;
					bean = new ViewType();
					bean.setViewTypeId(id);
					bean.setCommunityId(rs.getLong("COMMUNITY_ID"));
					bean.setContentType(rs.getString("CONTENT_TYPE"));
				}
				bean.setLabel(rs.getString("LANGUAGE"), rs.getString("LABEL"));
			} 
			return bean;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(stmt);
		}
	}

	/**
	 * Returns the list of validations of the given attribute.
	 * @param attributeId	The ID of the attribute
	 * @param conn			The open connection
	 * @return				The list of validations of the given attribute
	 */
	private List<Validation> getValidations(int attributeId, Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM validations WHERE ATTRIBUTE_ID=?");
			stmt.setInt(1, attributeId);
			rs = stmt.executeQuery();
			List<Validation> list = new LinkedList<Validation>();
			Validation bean = null;
			while (rs.next()) {
				int validationTypeId = rs.getInt(2);
				bean = new Validation();
				bean.setAttributeId(attributeId);
				bean.setValidationType(getValidationType(validationTypeId, conn));
				bean.setParameter(rs.getString("PARAMETER"));
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
	 * Returns the validation type identified by the given <code>validationTypeId</code>.
	 * @param validationTypeId	The ID of the validation type
	 * @param conn				The open connection
	 * @return					The created validation type
	 */
	private ValidationType getValidationType(int validationTypeId, Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement("SELECT v.VALIDATION_TYPE_ID, v.COMMUNITY_ID, v.CLASS, l.LANGUAGE, l.LABEL, l.MESSAGE FROM validation_types v LEFT JOIN validation_type_labels l ON v.VALIDATION_TYPE_ID=l.VALIDATION_TYPE_ID WHERE v.VALIDATION_TYPE_ID=?");
			stmt.setInt(1, validationTypeId);
			rs = stmt.executeQuery();
			int lastId = 0;
			ValidationType bean = null;
			while (rs.next()) {
				int id = rs.getInt(1);
				if (id != lastId) {
					lastId = id;
					bean = new ValidationType();
					bean.setValidationTypeId(validationTypeId);
					bean.setCommunityId(rs.getLong("COMMUNITY_ID"));
					bean.setClassName(rs.getString("CLASS"));
				}
				bean.setLabel(rs.getString("LANGUAGE"), rs.getString("LABEL"));
				bean.setMessage(rs.getString("LANGUAGE"), rs.getString("MESSAGE"));
			} 
			return bean;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(stmt);
		}
	}

	/**
	 * Returns the list of resource validations of the given resource type.
	 * @param typeId	The ID of the resource type
	 * @param conn		The open connection
	 * @return			The list of resource validations of the given resource type
	 */
	private List<ResourceValidation> getResourceValidations(int typeId, Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM resource_validations WHERE TYPE_ID=?");
			stmt.setInt(1, typeId);
			rs = stmt.executeQuery();
			List<ResourceValidation> list = new LinkedList<ResourceValidation>();
			ResourceValidation bean = null;
			while (rs.next()) {
				int validationTypeId = rs.getInt(2);
				bean = new ResourceValidation();
				bean.setTypeId(typeId);
				bean.setValidationType(getResourceValidationType(validationTypeId, conn));
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
	 * Returns the resource validation type identified by the given 
	 * <code>validationTypeId</code>.
	 * @param validationTypeId	The ID of the resource validation type
	 * @param conn				The open connection
	 * @return					The created resource validation type
	 */
	private ResourceValidationType getResourceValidationType(int validationTypeId, 
			Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement("SELECT v.RESOURCE_VALIDATION_TYPE_ID, v.CLASS, l.LANGUAGE, l.LABEL, l.MESSAGE FROM resource_validation_types v LEFT JOIN resource_validation_type_labels l ON v.RESOURCE_VALIDATION_TYPE_ID=l.RESOURCE_VALIDATION_TYPE_ID WHERE v.RESOURCE_VALIDATION_TYPE_ID=?");
			stmt.setInt(1, validationTypeId);
			rs = stmt.executeQuery();
			int lastId = 0;
			ResourceValidationType bean = null;
			while (rs.next()) {
				int id = rs.getInt(1);
				if (id != lastId) {
					lastId = id;
					bean = new ResourceValidationType();
					bean.setValidationTypeId(validationTypeId);
					bean.setClassName(rs.getString("CLASS"));
				}
				bean.setLabel(rs.getString("LANGUAGE"), rs.getString("LABEL"));
				bean.setMessage(rs.getString("LANGUAGE"), rs.getString("MESSAGE"));
			} 
			return bean;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(stmt);
		}
	}

	/**
	 * Returns the list of action for the given resource type in the given 
	 * view type
	 * @param typeId		The ID of the resource type
	 * @param viewTypeId	The ID of the view type
	 * @param conn			The open connection
	 * @return				The list of action for the given resource type in 
	 * the given view type
	 */
	private List<Action> getActions(int typeId, int viewTypeId, Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement("SELECT a.*, l.LANGUAGE, l.LABEL FROM view_actions v INNER JOIN actions a ON v.ACTION_ID=a.ACTION_ID LEFT JOIN action_labels l ON l.ACTION_ID=a.ACTION_ID WHERE v.TYPE_ID=? AND v.VIEW_TYPE_ID=?");
			stmt.setInt(1, typeId);
			stmt.setInt(2, viewTypeId);
			rs = stmt.executeQuery();
			List<Action> list = new LinkedList<Action>();
			int lastId = 0;
			Action bean = null;
			while (rs.next()) {
				int id = rs.getInt(1);
				if (id != lastId) {
					lastId = id;
					bean = new Action();
					bean.setActionId(id);
					bean.setCommunityId(rs.getLong("COMMUNITY_ID"));
					bean.setClassName(rs.getString("CLASS"));
					bean.setParameter(rs.getString("PARAMETER"));
					list.add(bean);
				}
				bean.setLabel(rs.getString("LANGUAGE"), rs.getString("LABEL"));
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
	 * Returns the list of aspects for the given resource type
	 * @param typeId		The ID of the resource type
	 * @param conn			The open connection
	 * @return				The list of aspects for the given resource type
	 */
	private List<Aspect> getAspects(int typeId, Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement("SELECT a.ASPECT_ID, a.COMMUNITY_ID, a.CLASS, l.LANGUAGE, l.LABEL FROM resource_type_aspects r INNER JOIN aspects a ON a.ASPECT_ID=r.ASPECT_ID LEFT JOIN aspect_labels l ON l.ASPECT_ID=a.ASPECT_ID WHERE r.TYPE_ID=?");
			stmt.setInt(1, typeId);
			rs = stmt.executeQuery();
			List<Aspect> list = new LinkedList<Aspect>();
			int lastId = 0;
			Aspect bean = null;
			while (rs.next()) {
				int id = rs.getInt(1);
				if (id != lastId) {
					lastId = id;
					bean = new Aspect();
					bean.setAspectId(id);
					bean.setCommunityId(rs.getLong("COMMUNITY_ID"));
					bean.setClassName(rs.getString("CLASS"));
					list.add(bean);
				}
				bean.setLabel(rs.getString("LANGUAGE"), rs.getString("LABEL"));
			} 
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(stmt);
		}
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getList(long)
	 */
	@SuppressWarnings("unchecked")
	public List<ResourceType> getList(final long communityId) {
		return (List<ResourceType>) super.getJdbcTemplate().execute(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(final Connection conn) 
					throws SQLException {
				final PreparedStatement stmt = conn.prepareStatement(
						"SELECT r.TYPE_ID, r.FAMILY_ID, r.COMMUNITY_ID, r.TIME_VIEW, l.LANGUAGE, l.LABEL FROM resource_types r LEFT JOIN resource_type_labels l ON r.TYPE_ID=l.TYPE_ID WHERE r.COMMUNITY_ID IN (?,0)");
				stmt.setLong(1, communityId);
				return stmt;
			}
		}, new PreparedStatementCallback() {
			public Object doInPreparedStatement(final PreparedStatement stmt) 
					throws SQLException, DataAccessException {
				final ResultSet rs = stmt.executeQuery();
				final List<ResourceType> list = new LinkedList<ResourceType>();
				int lastId = 0;
				ResourceType bean = null;
				while (rs.next()) {
					final int id = rs.getInt(1);
					if (id != lastId) {
						lastId = id;
						bean = new ResourceType();
						bean.setTypeId(id);
						bean.setCommunityId(rs.getLong("COMMUNITY_ID"));
						bean.setFamilyId(rs.getInt("FAMILY_ID"));
						bean.setTimeView(rs.getBoolean("TIME_VIEW"));
						bean.setAttributes(getAttributes(bean.getTypeId(), stmt.getConnection()));
						bean.setAspects(getAspects(bean.getTypeId(), stmt.getConnection()));
						bean.setCategories(getCategories(bean.getTypeId(), stmt.getConnection()));
						bean.setSupportedChildrenIds(getSupportedChildrenIds(
								bean.getTypeId(), stmt.getConnection()));
						bean.setViews(getViews(bean.getTypeId(), stmt.getConnection()));
						bean.setResourceValidations(getResourceValidations(bean.getTypeId(), stmt.getConnection()));
						list.add(bean);
					}
					bean.setLabel(rs.getString("LANGUAGE"), rs.getString("LABEL"));
				} 
				return list;
			}
		});
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#insert(ch.arpage.collaboweb.model.ResourceType)
	 */
	public void insert(final ResourceType bean) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		super.getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(final Connection conn) 
					throws SQLException {
				final PreparedStatement stmt = conn.prepareStatement(
						"INSERT INTO resource_types (FAMILY_ID, COMMUNITY_ID, TIME_VIEW) VALUES (?,?,?)",
						Statement.RETURN_GENERATED_KEYS);
				stmt.setLong(1, bean.getFamilyId());
				stmt.setLong(2, bean.getCommunityId());
				stmt.setBoolean(3, bean.isTimeView());
				return stmt;
			}
		}, keyHolder);
		Number key = keyHolder.getKey();
		if (key != null) {
            bean.setTypeId(key.intValue());
			updateLabels(bean);
        } else {
        	throw new DataRetrievalFailureException("Cannot retrieve generated ID");
        }
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#update(ch.arpage.collaboweb.model.ResourceType)
	 */
	public void update(final ResourceType bean) {
		super.getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(final Connection conn) 
					throws SQLException {
				final PreparedStatement stmt = conn.prepareStatement(
						"UPDATE resource_types SET TIME_VIEW=? WHERE TYPE_ID=?");
				stmt.setBoolean(1, bean.isTimeView());
				stmt.setLong(2, bean.getTypeId());
				return stmt;
			}
		});
		updateLabels(bean);
	}
	
	/**
	 * Updates the labels of the given resource type
	 * @param bean	The resource type
	 */
	private void updateLabels(ResourceType bean) {
		updateLabels(bean.getTypeId(), bean.getLabels(), 
				"REPLACE resource_type_labels VALUES (?,?,?)");
	}

	/**
	 * Updates the labels
	 * @param id		The primary key of the table
	 * @param labels	The labels
	 * @param sql		The SQL Statement
	 */
	private void updateLabels(int id, Map<String, String> labels, String sql) {
		PreparedStatement stmt = null;
		Connection conn = null;
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			for (Iterator<String> iter = labels.keySet().iterator(); iter.hasNext(); ) {
				String language = iter.next();
				stmt.setString(2, language);
				stmt.setString(3, labels.get(language));
				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			throw new DaoException(e);
		} finally {
			JdbcUtils.closeStatement(stmt);
			releaseConnection(conn);
		}
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#setSupportedChildren(int, int[])
	 */
	public void setSupportedChildren(int parentId, Integer[] childrenIds) {
		PreparedStatement stmt = null;
		Connection conn = null;
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(
					"DELETE FROM supported_children WHERE PARENT_TYPE_ID=?");
			stmt.setInt(1, parentId);
			stmt.executeUpdate();
			if (childrenIds != null && childrenIds.length > 0) {
				stmt = conn.prepareStatement(
						"INSERT INTO supported_children VALUES (?,?)");
				stmt.setInt(1, parentId);
				for (int i = 0; i < childrenIds.length; i++) {
					stmt.setInt(2, childrenIds[i]);
					stmt.executeUpdate();
				}
			}
		} catch (SQLException e) {
			throw new DaoException(e);
		} finally {
			JdbcUtils.closeStatement(stmt);
			releaseConnection(conn);
		}
	}

	/**
	 * Updates the labels and the messages.
	 * @param id		The primary key of the table
	 * @param labels	The labels
	 * @param messages	The messages
	 * @param sql		The SQL Statements
	 */
	private void updateLabelsAndMessages(int id, Map<String, String> labels, 
			Map<String, String> messages, String sql) {
		PreparedStatement stmt = null;
		Connection conn = null;
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			for (Iterator<String> iter = labels.keySet().iterator(); iter.hasNext(); ) {
				String language = iter.next();
				stmt.setString(2, language);
				stmt.setString(3, labels.get(language));
				stmt.setString(4, messages.get(language));
				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			throw new DaoException(e);
		} finally {
			JdbcUtils.closeStatement(stmt);
			releaseConnection(conn);
		}
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#deleteAttribute(ch.arpage.collaboweb.model.Attribute)
	 */
	public void deleteAttribute(final Attribute attribute) {
		getJdbcTemplate().update(new PreparedStatementCreator() {
			/* (non-Javadoc)
			 * @see org.springframework.jdbc.core.PreparedStatementCreator#createPreparedStatement(java.sql.Connection)
			 */
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				// TODO "historize" the resource_attributes objects
				PreparedStatement stmt = conn.prepareStatement(
						"DELETE a, ra, al FROM attributes a LEFT JOIN attribute_labels al ON a.ATTRIBUTE_ID=al.ATTRIBUTE_ID LEFT JOIN resource_attributes ra ON a.ATTRIBUTE_ID=ra.ATTRIBUTE_ID WHERE a.ATTRIBUTE_ID=?");
				stmt.setInt(1, attribute.getAttributeId());
				return stmt;
			}
		});	
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#insertAttribute(ch.arpage.collaboweb.model.Attribute)
	 */
	public void insertAttribute(final Attribute attribute) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		super.getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(final Connection conn) 
					throws SQLException {
				final PreparedStatement stmt = conn.prepareStatement(
						"INSERT INTO attributes (TYPE_ID, IDENTIFIER, DATA_TYPE, CHOICES, DEFAULT_VALUE, CALCULATED, EDITOR, FORMATTER, FORM_ORDER, LOAD_IN_LIST, SEARCH_FIELD_TYPE_ID, NAME_PART) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)",
						Statement.RETURN_GENERATED_KEYS);
				stmt.setInt(1, attribute.getTypeId());
				stmt.setString(2, attribute.getIdentifier());
				stmt.setInt(3, attribute.getDataType());
				stmt.setString(4, attribute.getChoices());
				stmt.setObject(5, attribute.getDefaultValue());
				stmt.setBoolean(6, attribute.isCalculated());
				stmt.setInt(7, attribute.getEditor());
				stmt.setString(8, attribute.getFormatter());
				stmt.setInt(9, attribute.getFormOrder());
				stmt.setBoolean(10, attribute.isLoadInList());
				stmt.setInt(11, attribute.getSearchFieldType());
				stmt.setBoolean(12, attribute.isNamePart());
				return stmt;
			}
		}, keyHolder);
		Number key = keyHolder.getKey();
		if (key != null) {
			attribute.setAttributeId(key.intValue());
            updateLabels(attribute.getAttributeId(), 
            		attribute.getLabels(), 
    				"REPLACE attribute_labels VALUES (?,?,?)");
        } else {
        	throw new DataRetrievalFailureException("Cannot retrieve generated ID");
        }
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#updateAttribute(ch.arpage.collaboweb.model.Attribute)
	 */
	public void updateAttribute(final Attribute attribute) {
		super.getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(final Connection conn) 
					throws SQLException {
				final PreparedStatement stmt = conn.prepareStatement(
						"UPDATE attributes SET IDENTIFIER=?, DATA_TYPE=?, CHOICES=?, DEFAULT_VALUE=?, CALCULATED=?, EDITOR=?, FORMATTER=?, FORM_ORDER=?, LOAD_IN_LIST=?, SEARCH_FIELD_TYPE_ID=?, NAME_PART=? WHERE ATTRIBUTE_ID=?");
				stmt.setString(1, attribute.getIdentifier());
				stmt.setInt(2, attribute.getDataType());
				stmt.setString(3, attribute.getChoices());
				stmt.setObject(4, attribute.getDefaultValue());
				stmt.setBoolean(5, attribute.isCalculated());
				stmt.setInt(6, attribute.getEditor());
				stmt.setString(7, attribute.getFormatter());
				stmt.setInt(8, attribute.getFormOrder());
				stmt.setBoolean(9, attribute.isLoadInList());
				stmt.setInt(10, attribute.getSearchFieldType());
				stmt.setInt(11, attribute.getSearchFieldType());
				stmt.setInt(12, attribute.getAttributeId());
				return stmt;
			}
		});
		updateLabels(attribute.getAttributeId(), attribute.getLabels(), 
	    				"REPLACE attribute_labels VALUES (?,?,?)");
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getValidationType(int)
	 */
	public ValidationType getValidationType(final int id) {
		return (ValidationType) super.getJdbcTemplate().execute(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) 
					throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT v.VALIDATION_TYPE_ID, v.COMMUNITY_ID, v.CLASS, l.LANGUAGE, l.LABEL, l.MESSAGE FROM validation_types v LEFT JOIN validation_type_labels l ON v.VALIDATION_TYPE_ID=l.VALIDATION_TYPE_ID WHERE v.VALIDATION_TYPE_ID=?");
				stmt.setInt(1, id);
				return stmt;
			}
		}, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement stmt) 
					throws SQLException, DataAccessException {
				ResultSet rs = stmt.executeQuery();
				ValidationType bean = null;
				while (rs.next()) {
					if (bean == null) {
						bean = new ValidationType();
						bean.setValidationTypeId(rs.getInt(1));
						bean.setCommunityId(rs.getLong("COMMUNITY_ID"));
						bean.setClassName(rs.getString("CLASS"));
					}
					bean.setLabel(rs.getString("LANGUAGE"), rs.getString("LABEL"));
					bean.setMessage(rs.getString("LANGUAGE"), rs.getString("MESSAGE"));
				} 
				return bean;
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getValidationTypes(long)
	 */
	@SuppressWarnings("unchecked")
	public List<ValidationType> getValidationTypes(final long communityId) {
		return (List<ValidationType>) super.getJdbcTemplate().execute(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) 
					throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT v.VALIDATION_TYPE_ID, v.COMMUNITY_ID, v.CLASS, l.LANGUAGE, l.LABEL, l.MESSAGE FROM validation_types v LEFT JOIN validation_type_labels l ON v.VALIDATION_TYPE_ID=l.VALIDATION_TYPE_ID WHERE v.COMMUNITY_ID IN(?,0) ORDER BY VALIDATION_TYPE_ID");
				stmt.setLong(1, communityId);
				return stmt;
			}
		}, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement stmt) 
					throws SQLException, DataAccessException {
				ResultSet rs = stmt.executeQuery();
				List<ValidationType> list = new LinkedList<ValidationType>();
				ValidationType bean = null;
				int lastId = -1;
				while (rs.next()) {
					int id = rs.getInt(1);
					if (id != lastId) {
						lastId = id;
						bean = new ValidationType();
						bean.setValidationTypeId(id);
						bean.setCommunityId(rs.getLong("COMMUNITY_ID"));
						bean.setClassName(rs.getString("CLASS"));
						list.add(bean);
					}
					bean.setLabel(rs.getString("LANGUAGE"), rs.getString("LABEL"));
					bean.setMessage(rs.getString("LANGUAGE"), rs.getString("MESSAGE"));
				} 
				return list;
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#deleteValidationType(ch.arpage.collaboweb.model.ValidationType)
	 */
	public void deleteValidationType(final ValidationType validationType) {
		getJdbcTemplate().update(new PreparedStatementCreator() {
			/* (non-Javadoc)
			 * @see org.springframework.jdbc.core.PreparedStatementCreator#createPreparedStatement(java.sql.Connection)
			 */
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"DELETE vt, vl, v FROM validation_types vt LEFT JOIN validations v ON v.VALIDATION_TYPE_ID=vt.VALIDATION_TYPE_ID LEFT JOIN validation_type_labels vl ON vl.VALIDATION_TYPE_ID=vt.VALIDATION_TYPE_ID WHERE vt.VALIDATION_TYPE_ID=?");
				stmt.setInt(1, validationType.getValidationTypeId());
				return stmt;
			}
		});	
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#insertValidationType(ch.arpage.collaboweb.model.ValidationType)
	 */
	public void insertValidationType(final ValidationType validationType) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		super.getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(final Connection conn) 
					throws SQLException {
				final PreparedStatement stmt = conn.prepareStatement(
						"INSERT INTO validation_types (COMMUNITY_ID, CLASS) VALUES (?,?)",
						Statement.RETURN_GENERATED_KEYS);
				stmt.setLong(1, validationType.getCommunityId());
				stmt.setString(2, validationType.getClassName());
				return stmt;
			}
		}, keyHolder);
		Number key = keyHolder.getKey();
		if (key != null) {
			validationType.setValidationTypeId(key.intValue());
			updateLabelsAndMessages(
					validationType.getValidationTypeId(), 
					validationType.getLabels(),
					validationType.getMessages(),
					"REPLACE validation_type_labels VALUES (?,?,?,?)");
		} else {
			throw new DataRetrievalFailureException("Cannot retrieve generated ID");
		}
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#updateValidationType(ch.arpage.collaboweb.model.ValidationType)
	 */
	public void updateValidationType(final ValidationType validationType) {
		super.getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(final Connection conn) 
					throws SQLException {
				final PreparedStatement stmt = conn.prepareStatement(
						"UPDATE validation_types SET COMMUNITY_ID=?, CLASS=? WHERE VALIDATION_TYPE_ID=?");
				stmt.setLong(1, validationType.getCommunityId());
				stmt.setString(2, validationType.getClassName());
				stmt.setInt(3, validationType.getValidationTypeId());
				return stmt;
			}
		});
		updateLabelsAndMessages(validationType.getValidationTypeId(),
				validationType.getLabels(), validationType.getMessages(),
				"REPLACE validation_type_labels VALUES (?,?,?,?)");
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#deleteValidation(ch.arpage.collaboweb.model.Validation)
	 */
	public void deleteValidation(final Validation validation) {
		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"DELETE FROM validations WHERE ATTRIBUTE_ID=? AND VALIDATION_TYPE_ID=?");
				stmt.setInt(1, validation.getAttributeId());
				stmt.setInt(2, validation.getValidationTypeId());
				return stmt;
			}
		});	
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#saveValidation(ch.arpage.collaboweb.model.Validation)
	 */
	public void saveValidation(final Validation validation) {
		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"REPLACE INTO validations VALUES (?,?,?)");
				stmt.setInt(1, validation.getAttributeId());
				stmt.setInt(2, validation.getValidationTypeId());
				stmt.setString(3, validation.getParameter());
				return stmt;
			}
		});	
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getValidation(int, int)
	 */
	public Validation getValidation(final int attributeId, final int validationTypeId) {
		return (Validation) super.getJdbcTemplate().execute(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) 
					throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT v.ATTRIBUTE_ID, v.VALIDATION_TYPE_ID, v.PARAMETER, vt.CLASS, vt.COMMUNITY_ID, l.LANGUAGE, l.LABEL, l.MESSAGE FROM validations v INNER JOIN validation_types vt ON v.VALIDATION_TYPE_ID=vt.VALIDATION_TYPE_ID LEFT JOIN validation_type_labels l ON vt.VALIDATION_TYPE_ID=l.VALIDATION_TYPE_ID WHERE v.ATTRIBUTE_ID=? AND v.VALIDATION_TYPE_ID=?");
				stmt.setInt(1, attributeId);
				stmt.setInt(2, validationTypeId);
				return stmt;
			}
		}, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement stmt) 
					throws SQLException, DataAccessException {
				ResultSet rs = stmt.executeQuery();
				Validation bean = null;
				ValidationType vt = null;
				while (rs.next()) {
					if (bean == null) {
						bean = new Validation();
						vt = new ValidationType();
						vt.setValidationTypeId(validationTypeId);
						vt.setCommunityId(rs.getLong("COMMUNITY_ID"));
						vt.setClassName(rs.getString("CLASS"));
						bean.setValidationType(vt);
						bean.setAttributeId(attributeId);
						bean.setParameter(rs.getString("PARAMETER"));
					}
					vt.setLabel(rs.getString("LANGUAGE"), rs.getString("LABEL"));
					vt.setMessage(rs.getString("LANGUAGE"), rs.getString("MESSAGE"));
				} 
				return bean;
			}
		});
	}

	/**
	 * Creates an attribute based on the given resultset
	 * @param rs		The resultset
	 * @param conn		The open connection
	 * @return			The created attribute
	 * @throws SQLException
	 */
	private Attribute createAttribute(ResultSet rs, Connection conn) throws SQLException {
		Attribute attribute = new Attribute();
		attribute.setAttributeId(rs.getInt("ATTRIBUTE_ID"));
		attribute.setTypeId(rs.getInt("TYPE_ID"));
		attribute.setIdentifier(rs.getString("IDENTIFIER"));
		attribute.setChoices(rs.getString("CHOICES"));
		attribute.setDataType(rs.getInt("DATA_TYPE"));
		attribute.setDefaultValue(rs.getString("DEFAULT_VALUE"));
		attribute.setCalculated(rs.getBoolean("CALCULATED"));
		attribute.setEditor(rs.getInt("EDITOR"));
		attribute.setFormatter(rs.getString("FORMATTER"));
		attribute.setFormOrder(rs.getInt("FORM_ORDER"));
		attribute.setLoadInList(rs.getBoolean("LOAD_IN_LIST"));
		attribute.setNamePart(rs.getBoolean("NAME_PART"));
		attribute.setValidations(getValidations(attribute.getAttributeId(), conn));
		attribute.setSearchFieldType(rs.getInt("SEARCH_FIELD_TYPE_ID"));
		return attribute;
	}
	
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#deleteViewType(ch.arpage.collaboweb.model.ViewType)
	 */
	public void deleteViewType(final ViewType viewType) {
		getJdbcTemplate().update(new PreparedStatementCreator() {
			/* (non-Javadoc)
			 * @see org.springframework.jdbc.core.PreparedStatementCreator#createPreparedStatement(java.sql.Connection)
			 */
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"DELETE vt, vl, v, va FROM view_types vt LEFT JOIN views v ON v.VIEW_TYPE_ID=vt.VIEW_TYPE_ID LEFT JOIN view_type_labels vl ON vl.VIEW_TYPE_ID=vt.VIEW_TYPE_ID LEFT JOIN view_actions va ON va.VIEW_TYPE_ID=vt.VIEW_TYPE_ID WHERE vt.VIEW_TYPE_ID=?");
				stmt.setInt(1, viewType.getViewTypeId());
				return stmt;
			}
		});	
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getViewType(int)
	 */
	public ViewType getViewType(final int id) {
		return (ViewType) super.getJdbcTemplate().execute(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) 
					throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT v.VIEW_TYPE_ID, v.COMMUNITY_ID, v.CONTENT_TYPE, l.LANGUAGE, l.LABEL FROM view_types v LEFT JOIN view_type_labels l ON v.VIEW_TYPE_ID=l.VIEW_TYPE_ID WHERE v.VIEW_TYPE_ID=?");
				stmt.setInt(1, id);
				return stmt;
			}
		}, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement stmt) 
					throws SQLException, DataAccessException {
				ResultSet rs = stmt.executeQuery();
				ViewType bean = null;
				while (rs.next()) {
					if (bean == null) {
						bean = new ViewType();
						bean.setViewTypeId(rs.getInt(1));
						bean.setCommunityId(rs.getLong("COMMUNITY_ID"));
						bean.setContentType(rs.getString("CONTENT_TYPE"));
					}
					bean.setLabel(rs.getString("LANGUAGE"), rs.getString("LABEL"));
				} 
				return bean;
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getViewTypes(long)
	 */
	@SuppressWarnings("unchecked")
	public List<ViewType> getViewTypes(final long communityId) {
		return (List<ViewType>) super.getJdbcTemplate().execute(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) 
					throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT v.VIEW_TYPE_ID, v.COMMUNITY_ID, v.CONTENT_TYPE, l.LANGUAGE, l.LABEL FROM view_types v LEFT JOIN view_type_labels l ON v.VIEW_TYPE_ID=l.VIEW_TYPE_ID WHERE v.COMMUNITY_ID IN(?,0) ORDER BY VIEW_TYPE_ID");
				stmt.setLong(1, communityId);
				return stmt;
			}
		}, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement stmt) 
					throws SQLException, DataAccessException {
				ResultSet rs = stmt.executeQuery();
				List<ViewType> list = new LinkedList<ViewType>();
				ViewType bean = null;
				int lastId = -1;
				while (rs.next()) {
					int id = rs.getInt(1);
					if (id != lastId) {
						lastId = id;
						bean = new ViewType();
						bean.setViewTypeId(id);
						bean.setCommunityId(rs.getLong("COMMUNITY_ID"));
						bean.setContentType(rs.getString("CONTENT_TYPE"));
						list.add(bean);
					}
					bean.setLabel(rs.getString("LANGUAGE"), rs.getString("LABEL"));
				} 
				return list;
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#insertViewType(ch.arpage.collaboweb.model.ViewType)
	 */
	public void insertViewType(final ViewType viewType) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		super.getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(final Connection conn) 
					throws SQLException {
				final PreparedStatement stmt = conn.prepareStatement(
						"INSERT INTO view_types (COMMUNITY_ID, CONTENT_TYPE) VALUES (?,?)",
						Statement.RETURN_GENERATED_KEYS);
				stmt.setLong(1, viewType.getCommunityId());
				stmt.setString(2, viewType.getContentType());
				return stmt;
			}
		}, keyHolder);
		Number key = keyHolder.getKey();
		if (key != null) {
			viewType.setViewTypeId(key.intValue());
			updateLabels(viewType.getViewTypeId(), viewType.getLabels(),
				"REPLACE view_type_labels VALUES (?,?,?)");
        } else {
        	throw new DataRetrievalFailureException("Cannot retrieve generated ID");
        }
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#updateViewType(ch.arpage.collaboweb.model.ViewType)
	 */
	public void updateViewType(final ViewType viewType) {
		super.getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(final Connection conn) 
					throws SQLException {
				final PreparedStatement stmt = conn.prepareStatement(
						"UPDATE view_types SET COMMUNITY_ID=?, CONTENT_TYPE=? WHERE VIEW_TYPE_ID=?");
				stmt.setLong(1, viewType.getCommunityId());
				stmt.setString(2, viewType.getContentType());
				stmt.setInt(3, viewType.getViewTypeId());
				return stmt;
			}
		});
        updateLabels(viewType.getViewTypeId(), viewType.getLabels(),
        		"REPLACE view_type_labels VALUES (?,?,?)");
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#deleteView(ch.arpage.collaboweb.model.View)
	 */
	public void deleteView(final View view) {
		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"DELETE v, va FROM views v LEFT JOIN view_actions va ON v.VIEW_TYPE_ID=va.VIEW_TYPE_ID AND v.TYPE_ID=va.TYPE_ID WHERE v.TYPE_ID=? AND va.VIEW_TYPE_ID=?");
				stmt.setInt(1, view.getTypeId());
				stmt.setInt(2, view.getViewTypeId());
				return stmt;
			}
		});	
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getView(int, int)
	 */
	public View getView(final int typeId, final int viewTypeId) {
		return (View) super.getJdbcTemplate().execute(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) 
					throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT v.TYPE_ID, v.VIEW_TYPE_ID, v.STYLESHEET, vt.CONTENT_TYPE, vt.COMMUNITY_ID, l.LANGUAGE, l.LABEL FROM views v INNER JOIN view_types vt ON v.VIEW_TYPE_ID=vt.VIEW_TYPE_ID LEFT JOIN view_type_labels l ON vt.VIEW_TYPE_ID=l.VIEW_TYPE_ID WHERE v.TYPE_ID=? AND v.VIEW_TYPE_ID=?");
				stmt.setInt(1, typeId);
				stmt.setInt(2, viewTypeId);
				return stmt;
			}
		}, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement stmt) 
					throws SQLException, DataAccessException {
				ResultSet rs = stmt.executeQuery();
				View bean = null;
				ViewType vt = null;
				while (rs.next()) {
					if (bean == null) {
						vt = new ViewType();
						vt.setViewTypeId(viewTypeId);
						vt.setCommunityId(rs.getLong("COMMUNITY_ID"));
						vt.setContentType(rs.getString("CONTENT_TYPE"));
						bean = new View();
						bean.setViewType(vt);
						bean.setTypeId(typeId);
						bean.setStyleSheet(rs.getString("STYLESHEET"));
						bean.setActions(getActions(typeId, viewTypeId, 
								stmt.getConnection()));
					}
					vt.setLabel(rs.getString("LANGUAGE"), rs.getString("LABEL"));
				} 
				return bean;
			}
		});
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#saveView(ch.arpage.collaboweb.model.View)
	 */
	public void saveView(final View view) {
		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"REPLACE INTO views VALUES (?,?,?)");
				stmt.setInt(1, view.getTypeId());
				stmt.setInt(2, view.getViewTypeId());
				stmt.setString(3, view.getStyleSheet());
				return stmt;
			}
		});	
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#deleteAction(ch.arpage.collaboweb.model.Action)
	 */
	public void deleteAction(final Action action) {
		getJdbcTemplate().update(new PreparedStatementCreator() {
			/* (non-Javadoc)
			 * @see org.springframework.jdbc.core.PreparedStatementCreator#createPreparedStatement(java.sql.Connection)
			 */
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"DELETE a, va, al FROM actions a LEFT JOIN view_actions va ON va.ACTION_ID=a.ACTION_ID LEFT JOIN action_labels al ON al.ACTION_ID=a.ACTION_ID WHERE a.ACTION_ID=?");
				stmt.setInt(1, action.getActionId());
				return stmt;
			}
		});	
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getAction(int)
	 */
	public Action getAction(final int id) {
		return (Action) super.getJdbcTemplate().execute(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) 
					throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT a.ACTION_ID, a.COMMUNITY_ID, a.CLASS, a.PARAMETER, l.LANGUAGE, l.LABEL FROM actions a LEFT JOIN action_labels l ON a.ACTION_ID=l.ACTION_ID WHERE a.ACTION_ID=?");
				stmt.setInt(1, id);
				return stmt;
			}
		}, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement stmt) 
					throws SQLException, DataAccessException {
				ResultSet rs = stmt.executeQuery();
				Action bean = null;
				while (rs.next()) {
					if (bean == null) {
						bean = new Action();
						bean.setActionId(rs.getInt(1));
						bean.setCommunityId(rs.getLong("COMMUNITY_ID"));
						bean.setClassName(rs.getString("CLASS"));
						bean.setParameter(rs.getString("PARAMETER"));
					}
					bean.setLabel(rs.getString("LANGUAGE"), rs.getString("LABEL"));
				} 
				return bean;
			}
		});
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getActions(long)
	 */
	@SuppressWarnings("unchecked")
	public List<Action> getActions(final long communityId) {
		return (List<Action>) super.getJdbcTemplate().execute(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) 
					throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT a.ACTION_ID, a.COMMUNITY_ID, a.CLASS, a.PARAMETER, l.LANGUAGE, l.LABEL FROM actions a LEFT JOIN action_labels l ON a.ACTION_ID=l.ACTION_ID WHERE a.COMMUNITY_ID IN(?,0) ORDER BY a.ACTION_ID");
				stmt.setLong(1, communityId);
				return stmt;
			}
		}, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement stmt) 
					throws SQLException, DataAccessException {
				ResultSet rs = stmt.executeQuery();
				List<Action> list = new LinkedList<Action>();
				Action bean = null;
				int lastId = -1;
				while (rs.next()) {
					int id = rs.getInt(1);
					if (id != lastId) {
						lastId = id;
						bean = new Action();
						bean.setActionId(rs.getInt(1));
						bean.setCommunityId(rs.getLong("COMMUNITY_ID"));
						bean.setClassName(rs.getString("CLASS"));
						bean.setParameter(rs.getString("PARAMETER"));
						list.add(bean);
					}
					bean.setLabel(rs.getString("LANGUAGE"), rs.getString("LABEL"));
				} 
				return list;
			}
		});
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#insertAction(ch.arpage.collaboweb.model.Action)
	 */
	public void insertAction(final Action action) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		super.getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(final Connection conn) 
					throws SQLException {
				final PreparedStatement stmt = conn.prepareStatement(
						"INSERT INTO actions (COMMUNITY_ID, CLASS, PARAMETER) VALUES (?,?,?)",
						Statement.RETURN_GENERATED_KEYS);
				stmt.setLong(1, action.getCommunityId());
				stmt.setString(2, action.getClassName());
				stmt.setString(3, action.getParameter());
				return stmt;
			}
		}, keyHolder);
		Number key = keyHolder.getKey();
		if (key != null) {
			action.setActionId(key.intValue());
            updateLabels(action.getActionId(), action.getLabels(),
            		"REPLACE action_labels VALUES (?,?,?)");
        } else {
        	throw new DataRetrievalFailureException("Cannot retrieve generated ID");
        }
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#updateAction(ch.arpage.collaboweb.model.Action)
	 */
	public void updateAction(final Action action) {
		super.getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(final Connection conn) 
					throws SQLException {
				final PreparedStatement stmt = conn.prepareStatement(
						"UPDATE actions SET COMMUNITY_ID=?, CLASS=?, PARAMETER=? WHERE ACTION_ID=?");
				stmt.setLong(1, action.getCommunityId());
				stmt.setString(2, action.getClassName());
				stmt.setString(3, action.getParameter());
				stmt.setInt(4, action.getActionId());
				return stmt;
			}
		});
        updateLabels(action.getActionId(), action.getLabels(),
        		"REPLACE action_labels VALUES (?,?,?)");
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#deleteRelationshipType(ch.arpage.collaboweb.model.RelationshipType)
	 */
	public void deleteRelationshipType(final RelationshipType relationshipType) {
		getJdbcTemplate().update(new PreparedStatementCreator() {
			/* (non-Javadoc)
			 * @see org.springframework.jdbc.core.PreparedStatementCreator#createPreparedStatement(java.sql.Connection)
			 */
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"DELETE rt, rtl, r FROM relationship_types rt LEFT JOIN relationship_type_labels rtl ON rtl.RELATIONSHIP_TYPE_ID=rt.RELATIONSHIP_TYPE_ID LEFT JOIN relationships r ON r.RELATIONSHIP_TYPE_ID=rt.RELATIONSHIP_TYPE_ID WHERE rt.RELATIONSHIP_TYPE_ID=?");
				stmt.setInt(1, relationshipType.getRelationshipTypeId());
				return stmt;
			}
		});	
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getRelationshipType(int)
	 */
	public RelationshipType getRelationshipType(final int id) {
		return (RelationshipType) super.getJdbcTemplate().execute(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) 
					throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT rt.RELATIONSHIP_TYPE_ID, rt.COMMUNITY_ID, rtl.LANGUAGE, rtl.LABEL FROM relationship_types rt LEFT JOIN relationship_type_labels rtl ON rtl.RELATIONSHIP_TYPE_ID=rt.RELATIONSHIP_TYPE_ID WHERE rt.RELATIONSHIP_TYPE_ID=?");
				stmt.setInt(1, id);
				return stmt;
			}
		}, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement stmt) 
					throws SQLException, DataAccessException {
				ResultSet rs = stmt.executeQuery();
				RelationshipType bean = null;
				while (rs.next()) {
					if (bean == null) {
						bean = new RelationshipType();
						bean.setRelationshipTypeId(rs.getInt(1));
						bean.setCommunityId(rs.getLong("COMMUNITY_ID"));
					}
					bean.setLabel(rs.getString("LANGUAGE"), rs.getString("LABEL"));
				} 
				return bean;
			}
		});
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getRelationshipTypes(long)
	 */
	@SuppressWarnings("unchecked")
	public List<RelationshipType> getRelationshipTypes(final long communityId) {
		return (List<RelationshipType>) super.getJdbcTemplate().execute(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) 
					throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT rt.RELATIONSHIP_TYPE_ID, rt.COMMUNITY_ID, rtl.LANGUAGE, rtl.LABEL FROM relationship_types rt LEFT JOIN relationship_type_labels rtl ON rtl.RELATIONSHIP_TYPE_ID=rt.RELATIONSHIP_TYPE_ID WHERE rt.COMMUNITY_ID IN(?,0) ORDER BY rt.RELATIONSHIP_TYPE_ID");
				stmt.setLong(1, communityId);
				return stmt;
			}
		}, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement stmt) 
					throws SQLException, DataAccessException {
				ResultSet rs = stmt.executeQuery();
				List<RelationshipType> list = new LinkedList<RelationshipType>();
				RelationshipType bean = null;
				int lastId = -1;
				while (rs.next()) {
					int id = rs.getInt(1);
					if (id != lastId) {
						lastId = id;
						bean = new RelationshipType();
						bean.setRelationshipTypeId(rs.getInt(1));
						bean.setCommunityId(rs.getLong("COMMUNITY_ID"));
						list.add(bean);
					}
					bean.setLabel(rs.getString("LANGUAGE"), rs.getString("LABEL"));
				} 
				return list;
			}
		});
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#insertRelationshipType(ch.arpage.collaboweb.model.RelationshipType)
	 */
	public void insertRelationshipType(final RelationshipType relationshipType) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		super.getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(final Connection conn) 
					throws SQLException {
				final PreparedStatement stmt = conn.prepareStatement(
						"INSERT INTO relationship_types (COMMUNITY_ID) VALUES (?)",
						Statement.RETURN_GENERATED_KEYS);
				stmt.setLong(1, relationshipType.getCommunityId());
				return stmt;
			}
		}, keyHolder);
		Number key = keyHolder.getKey();
		if (key != null) {
			relationshipType.setRelationshipTypeId(key.intValue());
            updateLabels(relationshipType.getRelationshipTypeId(), 
            		relationshipType.getLabels(),
            		"REPLACE relationship_type_labels VALUES (?,?,?)");
        } else {
        	throw new DataRetrievalFailureException("Cannot retrieve generated ID");
        }
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#updateRelationshipType(ch.arpage.collaboweb.model.RelationshipType)
	 */
	public void updateRelationshipType(final RelationshipType relationshipType) {
		super.getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(final Connection conn) 
					throws SQLException {
				final PreparedStatement stmt = conn.prepareStatement(
						"UPDATE relationship_types SET COMMUNITY_ID=? WHERE RELATIONSHIP_TYPE_ID=?");
				stmt.setLong(1, relationshipType.getCommunityId());
				stmt.setInt(2, relationshipType.getRelationshipTypeId());
				return stmt;
			}
		});
        updateLabels(relationshipType.getRelationshipTypeId(), 
        		relationshipType.getLabels(),
        		"REPLACE relationship_type_labels VALUES (?,?,?)");
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#deleteResourceValidation(ch.arpage.collaboweb.model.ResourceValidation)
	 */
	public void deleteResourceValidation(final ResourceValidation resourceValidation) {
		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"DELETE FROM resource_validations WHERE TYPE_ID=? AND RESOURCE_VALIDATION_TYPE_ID=?");
				stmt.setInt(1, resourceValidation.getTypeId());
				stmt.setInt(2, resourceValidation.getValidationTypeId());
				return stmt;
			}
		});	
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#deleteResourceValidationType(ch.arpage.collaboweb.model.ResourceValidationType)
	 */
	public void deleteResourceValidationType(final ResourceValidationType resourceValidationType) {
		getJdbcTemplate().update(new PreparedStatementCreator() {
			/* (non-Javadoc)
			 * @see org.springframework.jdbc.core.PreparedStatementCreator#createPreparedStatement(java.sql.Connection)
			 */
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"DELETE vt, vl, v FROM resource_validation_types vt LEFT JOIN resource_validations v ON v.RESOURCE_VALIDATION_TYPE_ID=vt.RESOURCE_VALIDATION_TYPE_ID LEFT JOIN resource_validation_type_labels vl ON vl.RESOURCE_VALIDATION_TYPE_ID=vt.RESOURCE_VALIDATION_TYPE_ID WHERE vt.RESOURCE_VALIDATION_TYPE_ID=?");
				stmt.setInt(1, resourceValidationType.getValidationTypeId());
				return stmt;
			}
		});	
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getResourceValidation(int, int)
	 */
	public ResourceValidation getResourceValidation(final int typeId, final int validationTypeId) {
		return (ResourceValidation) super.getJdbcTemplate().execute(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) 
					throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT v.TYPE_ID, v.RESOURCE_VALIDATION_TYPE_ID, vt.CLASS, vt.COMMUNITY_ID, l.LANGUAGE, l.LABEL, l.MESSAGE FROM resource_validations v INNER JOIN resource_validation_types vt ON v.RESOURCE_VALIDATION_TYPE_ID=vt.RESOURCE_VALIDATION_TYPE_ID LEFT JOIN resource_validation_type_labels l ON vt.RESOURCE_VALIDATION_TYPE_ID=l.RESOURCE_VALIDATION_TYPE_ID WHERE v.TYPE_ID=? AND v.RESOURCE_VALIDATION_TYPE_ID=?");
				stmt.setInt(1, typeId);
				stmt.setInt(2, validationTypeId);
				return stmt;
			}
		}, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement stmt) 
					throws SQLException, DataAccessException {
				ResultSet rs = stmt.executeQuery();
				ResourceValidation bean = null;
				ResourceValidationType vt = null;
				while (rs.next()) {
					if (bean == null) {
						bean = new ResourceValidation();
						vt = new ResourceValidationType();
						vt.setValidationTypeId(validationTypeId);
						vt.setCommunityId(rs.getLong("COMMUNITY_ID"));
						vt.setClassName(rs.getString("CLASS"));
						bean.setValidationType(vt);
						bean.setTypeId(typeId);
					}
					vt.setLabel(rs.getString("LANGUAGE"), rs.getString("LABEL"));
					vt.setMessage(rs.getString("LANGUAGE"), rs.getString("MESSAGE"));
				} 
				return bean;
			}
		});
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getResourceValidationType(int)
	 */
	public ResourceValidationType getResourceValidationType(final int id) {
		return (ResourceValidationType) super.getJdbcTemplate().execute(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) 
					throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT v.RESOURCE_VALIDATION_TYPE_ID, v.COMMUNITY_ID, v.CLASS, l.LANGUAGE, l.LABEL, l.MESSAGE FROM resource_validation_types v LEFT JOIN resource_validation_type_labels l ON v.RESOURCE_VALIDATION_TYPE_ID=l.RESOURCE_VALIDATION_TYPE_ID WHERE v.RESOURCE_VALIDATION_TYPE_ID=?");
				stmt.setInt(1, id);
				return stmt;
			}
		}, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement stmt) 
					throws SQLException, DataAccessException {
				ResultSet rs = stmt.executeQuery();
				ResourceValidationType bean = null;
				while (rs.next()) {
					if (bean == null) {
						bean = new ResourceValidationType();
						bean.setValidationTypeId(rs.getInt(1));
						bean.setCommunityId(rs.getLong("COMMUNITY_ID"));
						bean.setClassName(rs.getString("CLASS"));
					}
					bean.setLabel(rs.getString("LANGUAGE"), rs.getString("LABEL"));
					bean.setMessage(rs.getString("LANGUAGE"), rs.getString("MESSAGE"));
				} 
				return bean;
			}
		});
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#getResourceValidationTypes(long)
	 */
	@SuppressWarnings("unchecked")
	public List<ResourceValidationType> getResourceValidationTypes(final long communityId) {
		return (List<ResourceValidationType>) super.getJdbcTemplate().execute(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) 
					throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT v.RESOURCE_VALIDATION_TYPE_ID, v.COMMUNITY_ID, v.CLASS, l.LANGUAGE, l.LABEL, l.MESSAGE FROM resource_validation_types v LEFT JOIN resource_validation_type_labels l ON v.RESOURCE_VALIDATION_TYPE_ID=l.RESOURCE_VALIDATION_TYPE_ID WHERE v.COMMUNITY_ID IN(?,0) ORDER BY RESOURCE_VALIDATION_TYPE_ID");
				stmt.setLong(1, communityId);
				return stmt;
			}
		}, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement stmt) 
					throws SQLException, DataAccessException {
				ResultSet rs = stmt.executeQuery();
				List<ResourceValidationType> list = new LinkedList<ResourceValidationType>();
				ResourceValidationType bean = null;
				int lastId = -1;
				while (rs.next()) {
					int id = rs.getInt(1);
					if (id != lastId) {
						lastId = id;
						bean = new ResourceValidationType();
						bean.setValidationTypeId(id);
						bean.setCommunityId(rs.getLong("COMMUNITY_ID"));
						bean.setClassName(rs.getString("CLASS"));
						list.add(bean);
					}
					bean.setLabel(rs.getString("LANGUAGE"), rs.getString("LABEL"));
					bean.setMessage(rs.getString("LANGUAGE"), rs.getString("MESSAGE"));
				} 
				return list;
			}
		});
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#insertResourceValidationType(ch.arpage.collaboweb.model.ResourceValidationType)
	 */
	public void insertResourceValidationType(final ResourceValidationType resourceValidationType) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		super.getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(final Connection conn) 
					throws SQLException {
				final PreparedStatement stmt = conn.prepareStatement(
						"INSERT INTO resource_validation_types (COMMUNITY_ID, CLASS) VALUES (?,?)",
						Statement.RETURN_GENERATED_KEYS);
				stmt.setLong(1, resourceValidationType.getCommunityId());
				stmt.setString(2, resourceValidationType.getClassName());
				return stmt;
			}
		}, keyHolder);
		Number key = keyHolder.getKey();
		if (key != null) {
			resourceValidationType.setValidationTypeId(key.intValue());
            updateLabelsAndMessages(
            		resourceValidationType.getValidationTypeId(), 
            		resourceValidationType.getLabels(),
            		resourceValidationType.getMessages(),
    				"REPLACE resource_validation_type_labels VALUES (?,?,?,?)");
        } else {
        	throw new DataRetrievalFailureException("Cannot retrieve generated ID");
        }
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#saveResourceValidation(ch.arpage.collaboweb.model.ResourceValidation)
	 */
	public void saveResourceValidation(final ResourceValidation resourceValidation) {
		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement stmt = conn.prepareStatement(
						"REPLACE INTO resource_validations VALUES (?,?)");
				stmt.setInt(1, resourceValidation.getTypeId());
				stmt.setInt(2, resourceValidation.getValidationTypeId());
				return stmt;
			}
		});	
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.ResourceTypeDao#updateResourceValidationType(ch.arpage.collaboweb.model.ResourceValidationType)
	 */
	public void updateResourceValidationType(final ResourceValidationType resourceValidationType) {
		super.getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(final Connection conn) 
					throws SQLException {
				final PreparedStatement stmt = conn.prepareStatement(
						"UPDATE resource_validation_types SET COMMUNITY_ID=?, CLASS=? WHERE RESOURCE_VALIDATION_TYPE_ID=?");
				stmt.setLong(1, resourceValidationType.getCommunityId());
				stmt.setString(2, resourceValidationType.getClassName());
				stmt.setInt(3, resourceValidationType.getValidationTypeId());
				return stmt;
			}
		});
		updateLabelsAndMessages(resourceValidationType.getValidationTypeId(),
				resourceValidationType.getLabels(), 
				resourceValidationType.getMessages(),
				"REPLACE resource_validation_type_labels VALUES (?,?,?,?)");
	}	
}
