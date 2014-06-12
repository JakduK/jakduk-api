package com.jakduk.model.web;
/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 5. 24.
 * @desc     : 게시판 페이지네이션에 대한 정보를 담고 있음.
 */
public class BoardPageInfo {
		
	private long startPage = 1;
	private long endPage = 1;
	private long prevPage = 1;
	private long nextPage = 1;
	
	public long getStartPage() {
		return startPage;
	}

	public void setStartPage(long startPage) {
		this.startPage = startPage;
	}

	public long getEndPage() {
		return endPage;
	}

	public void setEndPage(long endPage) {
		this.endPage = endPage;
	}

	public long getPrevPage() {
		return prevPage;
	}

	public void setPrevPage(long prevPage) {
		this.prevPage = prevPage;
	}

	public long getNextPage() {
		return nextPage;
	}

	public void setNextPage(long nextPage) {
		this.nextPage = nextPage;
	}

	@Override
	public String toString() {
		return "BoardPageInfo [startPage=" + startPage + ", endPage=" + endPage
				+ ", prevPage=" + prevPage + ", nextPage=" + nextPage + "]";
	}

}
