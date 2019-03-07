package com.skn.MyBlog.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skn.MyBlog.domain.Comment;
import com.skn.MyBlog.repository.CommentMapper;
import com.skn.MyBlog.service.CommentService;

/**
 * Comment 服务.
 * 
 * @since 1.0.0 2017年4月9日
 * @author <a href="https://waylau.com">Way Lau</a>
 */
@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentMapper commentMapper;
	/* (non-Javadoc)
	 * @see com.waylau.spring.boot.blog.service.CommentService#removeComment(java.lang.Long)
	 */
	@Override
	@Transactional
	public void removeComment(Long id) {
		commentMapper.delete(id);
	}
	@Override
	public Comment getCommentById(Long id) {
		return commentMapper.findOne(id);
	}

}
