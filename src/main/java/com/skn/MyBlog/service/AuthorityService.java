package com.skn.MyBlog.service;

import com.skn.MyBlog.domain.Authority;


public interface AuthorityService {
	 
	
	/**
	 * 根据id获取 Authority
	 * @param Authority
	 * @return
	 */
	Authority getAuthorityById(Long id);
}
