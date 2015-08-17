package com.fmc.v1.data;

public class WallData {
	String name, time_elapsed,textPost,email,commentText,img,fb_id;
	int commentCount,likeCount,bookmarkCount,postID;
	boolean postLiked,postBookmarked;
	
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

	public int getBookmarkCount() {
		return bookmarkCount;
	}

	public void setBookmarkCount(int bookmarkCount) {
		this.bookmarkCount = bookmarkCount;
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

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getFb_id() {
		return fb_id;
	}

	public void setFb_id(String fb_id) {
		this.fb_id = fb_id;
	}

	public boolean isPostLiked() {
		return postLiked;
	}

	public void setPostLiked(boolean postLiked) {
		this.postLiked = postLiked;
	}

	public boolean isPostBookmarked() {
		return postBookmarked;
	}

	public void setPostBookmarked(boolean postBookmarked) {
		this.postBookmarked = postBookmarked;
	}
}
