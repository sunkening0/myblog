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

import com.skn.MyBlog.domain.User;
import com.skn.MyBlog.repository.UserMapper;
import com.skn.MyBlog.service.UserService;

/**
 * User 服务.
 * 
 * @since 1.0.0 2017年3月18日
 * @author <a href="https://waylau.com">Way Lau</a>
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Autowired
	private UserMapper userMapper;
	
	@Transactional
	@Override
	public int saveUser(User user) {
		return userMapper.insert(user);
	}

	@Transactional
	@Override
	public int removeUser(Long id) {
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
	
	/*@Transactional
	@Override
	public User updateUser(User user) {
		return userMapper.save(user);
	}

	

	@Override
	public List<User> listUsers() {
		return userMapper.findAll();
	}

	@Override
	public Page<User> listUsersByNameLike(String name, Pageable pageable) {
		// 模糊查询
		name = "%" + name + "%";
		Page<User> users = userMapper.findByNameLike(name, pageable);
		return users;
	}

	
	
*/
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
