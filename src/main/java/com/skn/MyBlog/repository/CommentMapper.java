package com.skn.MyBlog.repository;

import org.apache.ibatis.annotations.Param;

import com.skn.MyBlog.domain.Comment;

public interface CommentMapper {
    int deleteBlogComment(Long commentId);
	
	int deleteComment(Long commentId);
	
	int deleteBlogAllComment(Long blogId);
	
	int deleteAllComment(Long blogId);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment findOne(Long id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);
    
    int insertBlogComment(@Param("blogId") Long blogId,@Param("commentId") Long commentId);

}