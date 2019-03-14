package com.skn.MyBlog.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skn.MyBlog.domain.Vote;
import com.skn.MyBlog.repository.VoteMapper;
import com.skn.MyBlog.service.VoteService;

@Service
public class VoteServiceImpl implements VoteService {

	@Autowired
	private VoteMapper voteMapper;
	/* (non-Javadoc)
	 * @see com.waylau.spring.boot.blog.service.VoteService#removeVote(java.lang.Long)
	 */
	@Override
	@Transactional
	public void removeVote(Long id) {
		//先删除主表  在删除关联表
		int i = voteMapper.deleteBlogVote(id);
		int j = voteMapper.deleteVote(id);
	}
	@Override
	public Vote getVoteById(Long id) {
		return voteMapper.findOne(id);
	}
	
	@Override
	@Transactional
	public void removeAllVote(Long blogId) {

		int i = voteMapper.deleteBlogAllVote(blogId);
	}

}
