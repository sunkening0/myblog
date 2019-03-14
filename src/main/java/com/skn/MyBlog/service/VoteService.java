package com.skn.MyBlog.service;

import com.skn.MyBlog.domain.Vote;


public interface VoteService {
	/**
	 * 根据id获取 Vote
	 * @param id
	 * @return
	 */
	Vote getVoteById(Long id);
	/**
	 * 删除Vote
	 * @param id
	 * @return
	 */
	void removeVote(Long id);
	void removeAllVote(Long blogId);
}
