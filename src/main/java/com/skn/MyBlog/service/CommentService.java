package com.skn.MyBlog.service;

import com.skn.MyBlog.domain.Comment;


public interface CommentService {
	/**
	 * 根据id获取 Comment
	 * @param id
	 * @return
	 */
	Comment getCommentById(Long id);
	/**
	 * 删除评论
	 * @param id
	 * @return
	 */
	void removeComment(Long id);
	void removeAllComment(Long blogId);
}
