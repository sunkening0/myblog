/**
 * 
 */
package com.skn.MyBlog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skn.MyBlog.domain.Authority;
import com.skn.MyBlog.repository.AuthorityMapper;
import com.skn.MyBlog.service.AuthorityService;


@Service
public class AuthorityServiceImpl  implements AuthorityService {
	
	@Autowired
	private AuthorityMapper authorityMapper;
	
	@Override
	public Authority getAuthorityById(Long id) {
		return authorityMapper.findOne(id);
	}

}
