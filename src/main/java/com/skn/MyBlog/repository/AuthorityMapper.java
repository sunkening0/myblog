package com.skn.MyBlog.repository;



import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.skn.MyBlog.domain.Authority;

public interface AuthorityMapper {

	Authority findOne(Long id);
	
    int deleteByPrimaryKey(Long id);

    int insert(Authority record);

    int insertSelective(Authority record);

    int updateByPrimaryKeySelective(Authority record);

    int updateByPrimaryKey(Authority record);
}