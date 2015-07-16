package com.fmc.v1.data;

public class WallData {
	String name, time_elapsed,textPost,email,commentText;
	int commentCount,likeCount,postID;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime_elapsed() {
		return time_elapsed;
	}
	public void setTime_elapsed(String time_elapsed) {
		this.time_elapsed = time_elapsed;
	}
	public String getTextPost() {
		return textPost;
	}
	public void setTextPost(String textPost) {
		this.textPost = textPost;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getPostID() {
		return postID;
	}

	public void setPostID(int postID) {
		this.postID = postID;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}
}
