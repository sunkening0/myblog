package com.skn.MyBlog.repository;

import org.apache.ibatis.annotations.Param;

import com.skn.MyBlog.domain.Vote;

public interface VoteMapper {
    int delete(Long id);

    int insert(Vote record);

    int insertSelective(Vote record);

    Vote findOne(Long id);

    int updateByPrimaryKeySelective(Vote record);

    int updateByPrimaryKey(Vote record);
    
    int deleteBlogVote(Long voteId);
    
    int deleteVote(Long voteId);
    
    int insertBlogVote(@Param("blogId") Long blogId,@Param("voteId") Long voteId);
    
    int deleteBlogAllVote(Long blogId);
    
    int deleteAllVote(Long blogId);

}