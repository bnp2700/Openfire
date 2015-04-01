package org.jivesoftware.openfire.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The Class UserEntity.
 */
@XmlRootElement(name = "presence")
@XmlType(propOrder = { "isConnected", "status" })
@XmlAccessorType(XmlAccessType.FIELD) // trisient 지시자, getter/setter 빼고 변환
public class UserPresence {
	@XmlElement(required=true)
	private boolean isConnected;
	@XmlElement(required=false)
	private String status;
	
	public UserPresence() {
	}
	
	public boolean isConnected() {
		return isConnected;
	}
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isConnected ? 1231 : 1237);
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserPresence other = (UserPresence) obj;
		if (isConnected != other.isConnected)
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "UserPresence [isConnected=" + isConnected + ", status="
				+ status + "]";
	}
	
}
