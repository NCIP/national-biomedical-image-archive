/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.internaldomain;


import java.io.Serializable;
	/**
	* 	**/
public class LoginHistory  implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	
		/**
	* 	**/
	protected Long id;
	/**
	* Retreives the value of id attribute
	* @return id
	**/

	public Long getId(){
		return id;
	}

	/**
	* Sets the value of id attribue
	**/

	public void setId(Long id){
		this.id = id;
	}
	
		/**
	* 	**/
	protected String IPAddress;
	/**
	* Retreives the value of IPAddress attribute
	* @return IPAddress
	**/

	public String getIPAddress(){
		return IPAddress;
	}

	/**
	* Sets the value of IPAddress attribue
	**/

	public void setIPAddress(String IPAddress){
		this.IPAddress = IPAddress;
	}
	
		/**
	* 	**/
	protected java.util.Date loginTimestamp;
	/**
	* Retreives the value of loginTimestamp attribute
	* @return loginTimestamp
	**/

	public java.util.Date getLoginTimestamp(){
		return loginTimestamp;
	}

	/**
	* Sets the value of loginTimestamp attribue
	**/

	public void setLoginTimestamp(java.util.Date loginTimestamp){
		this.loginTimestamp = loginTimestamp;
	}
	
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof LoginHistory) 
		{
			LoginHistory c =(LoginHistory)obj; 			 
			if(getId() != null && getId().equals(c.getId())) {
				return true;
			}
		}
		return false;
	}
		
	/**
	* Returns hash code for the primary key of the object
	**/
	public int hashCode()
	{
		if(getId() != null) {
			return getId().hashCode();
		}
		return 0;
	}
	
}