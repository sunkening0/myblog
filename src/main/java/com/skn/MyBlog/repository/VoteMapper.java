package com.skn.MyBlog.repository;

import com.skn.MyBlog.domain.Vote;

public interface VoteMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Vote record);

    int insertSelective(Vote record);

    Vote selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Vote record);

    int updateByPrimaryKey(Vote record);
}