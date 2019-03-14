package com.skn.MyBlog.service.impl;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.skn.MyBlog.domain.User;
import com.skn.MyBlog.repository.UserMapper;
import com.skn.MyBlog.service.UserService;


@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Autowired
	private UserMapper userMapper;
	
	@Transactional
	@Override
	public void saveUser(User user) {
		if(user.getId() == null){
			userMapper.insert(user);
			userMapper.insertUserAuthority(user.getId(),user.getAuthority().getId());
		}else{
			userMapper.updateByPrimaryKey(user);
		}
		
	}

	@Transactional
	@Override
	public int removeUser(Long id) {
		userMapper.deleteUserAuthority(id);
		return userMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Transactional
	@Override
	public void removeUsersInBatch(List<User> users) {
		for(User user:users){
			userMapper.deleteByPrimaryKey(user.getId());
		}
	}
	
	@Transactional
	@Override
	public int updateUser(User user) {
		return userMapper.updateByPrimaryKey(user);
	}

	

	@Override
	public List<User> listUsers() {
		return userMapper.findAll();
	}

	@Override
	public List<User> listUsersByNameLike(String name,int pageIndex,int pageSize) {
		// 模糊查询
		name = "%" + name + "%";
		PageHelper.startPage(pageIndex+1, pageSize);
		List<User> users = userMapper.findByNameLike(name);
		return users;
	}

	
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = new User();
		user.setUsername(username);	
		logger.info("------------------"+username);
		User user2 = userMapper.selectByName(user);
		logger.info("---345363654635-----"+user2.getAuthorities().size());
		return user2;
		
	}
	
	@Override
	public List<User> listUsersByUsernames(List<String> usernames) {
		return userMapper.findByUsernames(usernames);
	}
	
	@Override
	public User getUserById(Long id) {
		User user = new User();
		user.setId(id);
		return userMapper.selectById(user);
	}

}
