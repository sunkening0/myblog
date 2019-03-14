package com.skn.MyBlog.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skn.MyBlog.domain.Comment;
import com.skn.MyBlog.repository.CommentMapper;
import com.skn.MyBlog.service.CommentService;


@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentMapper commentMapper;
	/**
	 * id  blogId
	 */
	@Override
	@Transactional
	public void removeComment(Long id) {
		commentMapper.deleteBlogComment(id);
		commentMapper.deleteComment(id);
	}
	@Override
	public Comment getCommentById(Long id) {
		return commentMapper.findOne(id);
	}
	@Override
	@Transactional
	public void removeAllComment(Long blogId) {
		commentMapper.deleteBlogAllComment(blogId);
	}
	

}
