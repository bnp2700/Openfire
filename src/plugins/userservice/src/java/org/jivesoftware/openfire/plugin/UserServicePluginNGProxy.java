package org.jivesoftware.openfire.plugin;

import java.util.List;

import javax.ws.rs.core.Response;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jivesoftware.openfire.SharedGroupException;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.entity.RosterEntities;
import org.jivesoftware.openfire.entity.RosterItemEntity;
import org.jivesoftware.openfire.entity.UserEntities;
import org.jivesoftware.openfire.entity.UserEntity;
import org.jivesoftware.openfire.entity.UserGroupsEntity;
import org.jivesoftware.openfire.entity.UserPresence;
import org.jivesoftware.openfire.entity.UserProperty;
import org.jivesoftware.openfire.exceptions.ServiceException;
import org.jivesoftware.openfire.user.UserAlreadyExistsException;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.jivesoftware.openfire.vcard.VCardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class UserServicePluginNG.
 */
public class UserServicePluginNGProxy implements UserServiceManager {
	private static final Logger Log = LoggerFactory
			.getLogger(UserServicePluginNGProxy.class);

	/** The Constant INSTANCE. */
	private static final UserServiceManager proxyOfUserServiceManager = new UserServicePluginNGProxy();
	private final String domainNameForOpenfire;
	
	private static final String COULD_NOT_CREATE_VCARD = "Could not create VCARD";

	private UserServiceManager realOfUserServiceManager;

	private UserServicePluginNGProxy() {
		realOfUserServiceManager = UserServicePluginNG.getInstance();
		domainNameForOpenfire = XMPPServer.getInstance().getServerInfo()
				.getXMPPDomain();
	}

	public static UserServiceManager getInstance() {
		return proxyOfUserServiceManager;
	}

	@Override
	public void createUser(UserEntity userEntity) throws ServiceException {
		// user create
		realOfUserServiceManager.createUser(userEntity);

		setVCard(userEntity);
	}

	private void setVCard(UserEntity userEntity) throws ServiceException {
		// vcard set
		String sip = null;
		String phone = "";
		List<UserProperty> properties = userEntity.getProperties();
		for (UserProperty property : properties) {
			if ("sip".equalsIgnoreCase(property.getKey())) {
				sip = property.getValue();
			}
			if ("phone".equalsIgnoreCase(property.getKey())) {
				phone = property.getValue();
			}
		}
		Log.info(userEntity.getUsername() + "/" + userEntity.getName()
				+ ", sip : " + sip + ", phone : " + phone);

		
		String jid = userEntity.getUsername() + "@"	+ domainNameForOpenfire;
		try {
			VCardManager.getInstance().setVCard(
					userEntity.getUsername(),
					getDefaultVCard(jid, userEntity.getUsername(), userEntity.getName(), sip,	phone));
		} catch (Exception e) {
			throw new ServiceException(COULD_NOT_CREATE_VCARD, "",
					COULD_NOT_CREATE_VCARD, Response.Status.BAD_REQUEST, e);
		}

	}

	private Element getDefaultVCard(String jid, String id, String nickname, String sip,
			String phone) {
		Element vCard;

		try {
			String xml = "<vCard xmlns=\"vcard-temp\"><N>"
					+ "<FAMILY>"
					+ id
					+ "</FAMILY><GIVEN></GIVEN><MIDDLE></MIDDLE></N><ORG><ORGNAME></ORGNAME><ORGUNIT/></ORG>"
					+ "<FN>"
					+ id
					+"</FN>"
					+ "<ROLE/><DESC/>"
					+ "<JABBERID>"
					+ jid
					+ "</JABBERID><URL/><NICKNAME>"
					+ nickname
					+ "</NICKNAME><TITLE/><PHOTO><TYPE>image/jpeg</TYPE><BINVAL>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</BINVAL></PHOTO><EMAIL><HOME/><INTERNET/><PREF/><USERID/></EMAIL><TEL><PAGER/><WORK/><NUMBER/></TEL><TEL><CELL/><WORK/><NUMBER/></TEL><TEL><VOICE/><WORK/><NUMBER/></TEL><TEL><FAX/><WORK/><NUMBER/></TEL><TEL><PAGER/><HOME/><NUMBER/></TEL><TEL><CELL/><HOME/><NUMBER>"
					+ phone
					+ "</NUMBER></TEL><TEL><VOICE/><HOME/><NUMBER>"
					+ sip
					+ "</NUMBER></TEL><TEL><FAX/><HOME/><NUMBER/></TEL><ADR><WORK/><EXTADD/><PCODE/><REGION/><STREET/><CTRY/><LOCALITY/></ADR><ADR><HOME/><EXTADD/><PCODE/><REGION/><STREET/><CTRY/><LOCALITY/></ADR></vCard>";
			vCard = DocumentHelper.parseText(xml).getRootElement();
			return vCard;
		} catch (DocumentException e) {
			return null;
		}
	}

	@Override
	public void updateUser(String username, UserEntity userEntity)
			throws ServiceException {
		realOfUserServiceManager.updateUser(username, userEntity);
		setVCard(userEntity);
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
		realOfUserServiceManager.updateRosterItem(username, rosterJid,
				rosterItemEntity);
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
		realOfUserServiceManager.deleteUserFromGroups(username,
				userGroupsEntity);
	}

	@Override
	public UserEntities getUserEntitiesByProperty(String propertyKey,
			String propertyValue) throws ServiceException {
		return realOfUserServiceManager.getUserEntitiesByProperty(propertyKey,
				propertyValue);
	}

	@Override
	public UserPresence getUserPresence(String username) throws ServiceException {
		return realOfUserServiceManager.getUserPresence(username);
	}

}