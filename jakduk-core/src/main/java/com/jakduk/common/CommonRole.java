package com.jakduk.common;
/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 6.
 * @desc     : 
 */
public class CommonRole {
	
	public final static String ROLE_NAME_USER_01 = "ROLE_USER_01"; // 미인증 회원
	public final static String ROLE_NAME_USER_02 = "ROLE_USER_02"; // 인증 회원
	public final static String ROLE_NAME_USER_03 = "ROLE_USER_03";
	public final static String ROLE_NAME_ADMIN = "ROLE_ADMIN";
	public final static String ROLE_NAME_ROOT = "ROLE_ROOT";
	
	public final static int ROLE_NUMBER_USER_01 = 10;
	public final static int ROLE_NUMBER_USER_02 = 11;
	public final static int ROLE_NUMBER_USER_03 = 12;
	public final static int ROLE_NUMBER_ADMIN = 30;
	public final static int ROLE_NUMBER_ROOT = 90;
	
	public static String getRoleName(Integer roleNumber) {
		String roleName = "";
		
		if (roleNumber != null) {
			switch (roleNumber) {
			case ROLE_NUMBER_USER_01:
				roleName = ROLE_NAME_USER_01;
				break;
			case ROLE_NUMBER_USER_02:
				roleName = ROLE_NAME_USER_02;
				break;
			case ROLE_NUMBER_USER_03:
				roleName = ROLE_NAME_USER_03;
				break;
			case ROLE_NUMBER_ADMIN:
				roleName = ROLE_NAME_ADMIN;
				break;
			case ROLE_NUMBER_ROOT:
				roleName = ROLE_NAME_ROOT;
				break;
			default:
				break;
			}
		}
		
		return roleName;
	}
	
	public static int getRoleNumber(String roleName) {
		Integer roleNumber = 0;
		
		if (roleName != null) {
			switch(roleName) {
			case ROLE_NAME_USER_01:
				roleNumber = ROLE_NUMBER_USER_01;
				break;
			case ROLE_NAME_USER_02:
				roleNumber = ROLE_NUMBER_USER_02;
				break;
			case ROLE_NAME_USER_03:
				roleNumber = ROLE_NUMBER_USER_03;
				break;
			case ROLE_NAME_ADMIN:
				roleNumber = ROLE_NUMBER_ADMIN;
				break;
			case ROLE_NAME_ROOT:
				roleNumber = ROLE_NUMBER_ROOT;
				break;
			}
		}
		
		return roleNumber;
	}

}
