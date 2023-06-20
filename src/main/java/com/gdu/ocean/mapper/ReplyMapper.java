package com.gdu.ocean.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.ocean.domain.ReplyDTO;

@Mapper
public interface ReplyMapper {

	public int getCommentCount(int idolNo);
	public List<ReplyDTO> getCommentList(Map<String, Object> map);
	public int addComment(ReplyDTO replyDTO);
	public int addReply(ReplyDTO replyDTO);
	public int deleteComment(int replyNo);
}
