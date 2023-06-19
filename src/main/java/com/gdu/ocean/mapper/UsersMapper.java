package com.gdu.ocean.mapper;


import org.apache.ibatis.annotations.Mapper;

import com.gdu.ocean.domain.OutUsersDTO;
import com.gdu.ocean.domain.SleepUsersDTO;
import com.gdu.ocean.domain.UsersDTO;

@Mapper
public interface UsersMapper {
	
	public UsersDTO selectUsersByEmail(String email);
	public SleepUsersDTO selectSleepUsersByEmail(String email);
	public OutUsersDTO selectOutUsersByEmail(String email);
	public UsersDTO selectUsersByPhoneNo(String phoneNo);
	public UsersDTO selectSleepUsersByPhoneNo(String phoneNo);
	public UsersDTO selectOutUsersByPhoneNo(String phoneNo);
	public int insertUsers(UsersDTO usersDTO);
	public UsersDTO selectUsersByUsersDTO(UsersDTO usersDTO);
	public UsersDTO insertUsersAccess(String email);
	public int updateUsersAccess(String email);
	public int insertAutologin(UsersDTO usersDTO);
	public int deleteAutologin(String email);
	public UsersDTO selectAutologin(String autologinEmail);
	public int insertOutUsers(OutUsersDTO outUsersDTO);
	public int deleteUsers(String email);
    public int insertSleepUsers();
    public int deleteUsersForSleep();
    public int insertRestoreUsers(String email);
    public int deleteSleepUsers(String email);
    public int updateUsersPassword(UsersDTO usersDTO);
    public int updateUsersInfo(UsersDTO usersDTO);
}
