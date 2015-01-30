package org.jivesoftware.openfire.plugin;

import java.util.List;

import javax.ws.rs.core.Response;

import org.jivesoftware.openfire.SharedGroupException;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.entity.RosterEntities;
import org.jivesoftware.openfire.entity.RosterItemEntity;
import org.jivesoftware.openfire.entity.UserEntities;
import org.jivesoftware.openfire.entity.UserEntity;
import org.jivesoftware.openfire.entity.UserGroupsEntity;
import org.jivesoftware.openfire.exceptions.ExceptionType;
import org.jivesoftware.openfire.exceptions.ServiceException;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserAlreadyExistsException;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.openfire.user.UserNotFoundException;

/**
 * The Class UserServicePluginNG.
 */
public class UserServicePluginNGProxy implements UserServiceManager {
	/** The Constant INSTANCE. */
	private static final UserServiceManager proxyOfUserServiceManager = new UserServicePluginNGProxy();
	private static final String masterId = "bnpmanager";
	private final String masterJid;
	private static final int fromOfSubscriptionLevel = 1;
	private static final int toOfSubscriptionLevel = 2;
	private static final int bothOfSubscriptionLevel = 3;
	
	private static final String COULD_NOT_CREATE_ROSTER_ITEM = "Could not create roster item";
	
	
	private UserServiceManager realOfUserServiceManager;
	
	private UserServicePluginNGProxy() {
		realOfUserServiceManager = UserServicePluginNG.getInstance();
		masterJid = masterId + "@" +XMPPServer.getInstance().getServerInfo().getXMPPDomain();
	}
	

	public static UserServiceManager getInstance() {
		return proxyOfUserServiceManager;
	}

	@Override
	public void createUser(UserEntity userEntity) throws ServiceException {
		realOfUserServiceManager.createUser(userEntity);
		RosterItemEntity rosterOfMaster = createRosterEntity(masterJid,"BNPMaster",fromOfSubscriptionLevel);
		
		
		try {
			realOfUserServiceManager.addRosterItem(userEntity.getUsername(), rosterOfMaster);
		} catch (UserNotFoundException e) {
			throw new ServiceException(COULD_NOT_CREATE_ROSTER_ITEM, "", ExceptionType.USER_NOT_FOUND_EXCEPTION,
					Response.Status.NOT_FOUND, e);
		} catch (UserAlreadyExistsException e) {
			throw new ServiceException(COULD_NOT_CREATE_ROSTER_ITEM, "", ExceptionType.USER_ALREADY_EXISTS_EXCEPTION,
					Response.Status.BAD_REQUEST, e);
		} catch (SharedGroupException e) {
			throw new ServiceException(COULD_NOT_CREATE_ROSTER_ITEM, "", ExceptionType.SHARED_GROUP_EXCEPTION,
					Response.Status.BAD_REQUEST, e);
		}
		
		RosterItemEntity rosterOfNewUser = createRosterEntity(
				userEntity.getUsername() + "@" +XMPPServer.getInstance().getServerInfo().getXMPPDomain()
				,userEntity.getName(),fromOfSubscriptionLevel);
		
		try {
			realOfUserServiceManager.addRosterItem(masterId, rosterOfNewUser);
		} catch (UserNotFoundException e) {
			throw new ServiceException(COULD_NOT_CREATE_ROSTER_ITEM, "", ExceptionType.USER_NOT_FOUND_EXCEPTION,
					Response.Status.NOT_FOUND, e);
		} catch (UserAlreadyExistsException e) {
			throw new ServiceException(COULD_NOT_CREATE_ROSTER_ITEM, "", ExceptionType.USER_ALREADY_EXISTS_EXCEPTION,
					Response.Status.BAD_REQUEST, e);
		} catch (SharedGroupException e) {
			throw new ServiceException(COULD_NOT_CREATE_ROSTER_ITEM, "", ExceptionType.SHARED_GROUP_EXCEPTION,
					Response.Status.BAD_REQUEST, e);
		}
	}


	private RosterItemEntity createRosterEntity(String jid, String nickName, int subscriptionMode) {
		RosterItemEntity rosterItem = new RosterItemEntity();
		rosterItem.setJid(jid);
		if(null != nickName) 
			rosterItem.setNickname(nickName);
		rosterItem.setSubscriptionType(subscriptionMode);
		return rosterItem;
	}

	@Override
	public void updateUser(String username, UserEntity userEntity)
			throws ServiceException {
		realOfUserServiceManager.updateUser(username, userEntity);		
	}

	@Override
	public void deleteUser(String username) throws ServiceException {
		realOfUserServiceManager.deleteUser(username);		
	}

	@Override
	public UserEntities getUserEntities(String userSearch) {
		return realOfUserServiceManager.getUserEntities(userSearch);
	}

	@Override
	public UserEntity getUserEntity(String username) throws ServiceException {
		return realOfUserServiceManager.getUserEntity(username);
	}

	@Override
	public void enableUser(String username) throws ServiceException {
		realOfUserServiceManager.enableUser(username);		
	}

	@Override
	public void disableUser(String username) throws ServiceException {
		realOfUserServiceManager.disableUser(username);		
	}

	@Override
	public RosterEntities getRosterEntities(String username)
			throws ServiceException {
		return realOfUserServiceManager.getRosterEntities(username);
	}

	@Override
	public void addRosterItem(String username, RosterItemEntity rosterItemEntity)
			throws ServiceException, UserAlreadyExistsException,
			SharedGroupException, UserNotFoundException {
		realOfUserServiceManager.addRosterItem(username, rosterItemEntity);
	}

	@Override
	public void updateRosterItem(String username, String rosterJid,
			RosterItemEntity rosterItemEntity) throws ServiceException,
			UserNotFoundException, UserAlreadyExistsException,
			SharedGroupException {
		realOfUserServiceManager.updateRosterItem(username, rosterJid, rosterItemEntity);		
	}

	@Override
	public void deleteRosterItem(String username, String rosterJid)
			throws SharedGroupException, ServiceException {
		realOfUserServiceManager.deleteRosterItem(username, rosterJid);		
	}

	@Override
	public List<String> getUserGroups(String username) throws ServiceException {
		return realOfUserServiceManager.getUserGroups(username);
	}

	@Override
	public void addUserToGroups(String username,
			UserGroupsEntity userGroupsEntity) throws ServiceException {
		realOfUserServiceManager.addUserToGroups(username, userGroupsEntity);	
	}

	@Override
	public void deleteUserFromGroups(String username,
			UserGroupsEntity userGroupsEntity) throws ServiceException {
		realOfUserServiceManager.deleteUserFromGroups(username, userGroupsEntity);		
	}

	@Override
	public UserEntities getUserEntitiesByProperty(String propertyKey,
			String propertyValue) throws ServiceException {
		return realOfUserServiceManager.getUserEntitiesByProperty(propertyKey, propertyValue);
	}


}