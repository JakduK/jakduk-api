package com.jakduk.common;
/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 6.
 * @desc     : ROLE_USER_01 = 10, ROLE_USER_02 = 11, ROLE_USER_03 = 12, ROLE_ADMIN = 30, ROLE_ROOT = 90
 */
public class CommonRole {
	
	public static String getRoleName(Integer roleNumber) {
		String roleName = "";
		
		if (roleNumber != null) {
			switch (roleNumber) {
			case 10:
				roleName = "ROLE_USER_01";
				break;
			case 11:
				roleName = "ROLE_USER_02";
				break;
			case 12:
				roleName = "ROLE_USER_03";
				break;
			case 30:
				roleName = "ROLE_ADMIN";
				break;
			case 90:
				roleName = "ROLE_ROOT";
				break;
			default:
				break;
			}
		}
		
		return roleName;
	}
	
	public static Integer getRoleNumber(String roleName) {
		Integer roleNumber = 0;
		
		if (roleName != null) {
			switch(roleName) {
			case "ROLE_USER_01":
				roleNumber = 10;
				break;
			case "ROLE_USER_02":
				roleNumber = 11;
				break;
			case "ROLE_USER_03":
				roleNumber = 12;
				break;
			case "ROLE_ADMIN":
				roleNumber = 30;
				break;
			case "ROLE_ROOT":
				roleNumber = 90;
				break;
			}
		}
		
		return roleNumber;
	}

}
