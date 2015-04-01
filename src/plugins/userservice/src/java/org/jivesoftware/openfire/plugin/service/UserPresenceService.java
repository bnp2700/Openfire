package org.jivesoftware.openfire.plugin.service;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jivesoftware.openfire.entity.UserPresence;
import org.jivesoftware.openfire.exceptions.ServiceException;
import org.jivesoftware.openfire.plugin.UserServiceManager;
import org.jivesoftware.openfire.plugin.UserServicePluginNGProxy;

@Path("userService/users/{username}/presence")
public class UserPresenceService {

	private UserServiceManager plugin;

	@PostConstruct
	public void init() {
		plugin = UserServicePluginNGProxy.getInstance();
	}

	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public UserPresence getUserPresence(@PathParam("username") String username) throws ServiceException {
		
		UserPresence presence = plugin.getUserPresence(username);

		return presence;
	}
}
