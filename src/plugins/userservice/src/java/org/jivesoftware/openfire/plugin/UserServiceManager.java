package org.jivesoftware.openfire.plugin;

import java.util.List;

import org.jivesoftware.openfire.SharedGroupException;
import org.jivesoftware.openfire.entity.RosterEntities;
import org.jivesoftware.openfire.entity.RosterItemEntity;
import org.jivesoftware.openfire.entity.UserEntities;
import org.jivesoftware.openfire.entity.UserEntity;
import org.jivesoftware.openfire.entity.UserGroupsEntity;
import org.jivesoftware.openfire.entity.UserPresence;
import org.jivesoftware.openfire.exceptions.ServiceException;
import org.jivesoftware.openfire.group.GroupAlreadyExistsException;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserAlreadyExistsException;
import org.jivesoftware.openfire.user.UserNotFoundException;

public interface UserServiceManager {
	/**
	 * Creates the user.
	 *
	 * @param userEntity
	 *            the user entity
	 * @return 
	 * @throws ServiceException
	 *             the service exception
	 */
	public void createUser(UserEntity userEntity) throws ServiceException;

	/**
	 * Update user.
	 *
	 * @param username
	 *            the username
	 * @param userEntity
	 *            the user entity
	 * @throws ServiceException
	 *             the service exception
	 */
	public void updateUser(String username, UserEntity userEntity)
			throws ServiceException;

	/**
	 * Delete user.
	 *
	 * @param username
	 *            the username
	 * @throws ServiceException
	 *             the service exception
	 */
	public void deleteUser(String username) throws ServiceException;

	/**
	 * Gets the user entities.
	 *
	 * @param userSearch
	 *            the user search
	 * @return the user entities
	 */
	public UserEntities getUserEntities(String userSearch);

	/**
	 * Gets the user entity.
	 *
	 * @param username
	 *            the username
	 * @return the user entity
	 * @throws ServiceException
	 *             the service exception
	 */
	public UserEntity getUserEntity(String username) throws ServiceException;

	/**
	 * Enable user.
	 *
	 * @param username
	 *            the username
	 * @throws ServiceException
	 *             the service exception
	 */
	public void enableUser(String username) throws ServiceException;

	/**
	 * Disable user.
	 *
	 * @param username
	 *            the username
	 * @throws ServiceException
	 *             the service exception
	 */
	public void disableUser(String username) throws ServiceException;

	/**
	 * Gets the roster entities.
	 *
	 * @param username
	 *            the username
	 * @return the roster entities
	 * @throws ServiceException
	 *             the service exception
	 */
	public RosterEntities getRosterEntities(String username)
			throws ServiceException;

	/**
	 * Adds the roster item.
	 *
	 * @param username
	 *            the username
	 * @param rosterItemEntity
	 *            the roster item entity
	 * @throws ServiceException
	 *             the service exception
	 * @throws UserAlreadyExistsException
	 *             the user already exists exception
	 * @throws SharedGroupException
	 *             the shared group exception
	 * @throws UserNotFoundException
	 *             the user not found exception
	 */
	public void addRosterItem(String username, RosterItemEntity rosterItemEntity)
			throws ServiceException, UserAlreadyExistsException,
			SharedGroupException, UserNotFoundException;

	/**
	 * Update roster item.
	 *
	 * @param username
	 *            the username
	 * @param rosterJid
	 *            the roster jid
	 * @param rosterItemEntity
	 *            the roster item entity
	 * @throws ServiceException
	 *             the service exception
	 * @throws UserNotFoundException
	 *             the user not found exception
	 * @throws UserAlreadyExistsException
	 *             the user already exists exception
	 * @throws SharedGroupException
	 *             the shared group exception
	 */
	public void updateRosterItem(String username, String rosterJid,
			RosterItemEntity rosterItemEntity) throws ServiceException,
			UserNotFoundException, UserAlreadyExistsException,
			SharedGroupException;

	/**
	 * Delete roster item.
	 *
	 * @param username
	 *            the username
	 * @param rosterJid
	 *            the roster jid
	 * @throws SharedGroupException
	 *             the shared group exception
	 * @throws ServiceException
	 *             the service exception
	 */
	public void deleteRosterItem(String username, String rosterJid)
			throws SharedGroupException, ServiceException;

	/**
	 * Gets the user groups.
	 *
	 * @param username
	 *            the username
	 * @return the user groups
	 * @throws ServiceException
	 *             the service exception
	 */
	public List<String> getUserGroups(String username) throws ServiceException;

	/**
	 * Adds the user to group.
	 *
	 * @param username
	 *            the username
	 * @param userGroupsEntity
	 *            the user groups entity
	 * @throws ServiceException
	 * @throws GroupAlreadyExistsException
	 *             the group already exists exception
	 */
	public void addUserToGroups(String username,
			UserGroupsEntity userGroupsEntity) throws ServiceException;

	/**
	 * Delete user from groups.
	 *
	 * @param username
	 *            the username
	 * @param userGroupsEntity
	 *            the user groups entity
	 * @throws ServiceException
	 *             the service exception
	 */
	public void deleteUserFromGroups(String username,
			UserGroupsEntity userGroupsEntity) throws ServiceException;

	/**
	 * Gets the user entities by property key and or value.
	 *
	 * @param propertyKey
	 *            the property key
	 * @param propertyValue
	 *            the property value (can be null)
	 * @return the user entities by property
	 * @throws ServiceException
	 *             the service exception
	 */
	public UserEntities getUserEntitiesByProperty(String propertyKey,
			String propertyValue) throws ServiceException;

	public UserPresence getUserPresence(String username) throws ServiceException;

}