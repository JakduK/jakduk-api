var Jakduk = {
	BoardCommentSize : 30,
	BoardCommentContentLengthMin : 3, // 게시판 댓글 입력 가능한 최소한의 문자열 수
	BoardCommentContentLengthMax : 800, // 게시판 댓글 입력 가능한 최대 문자열 수
	SummernoteContentsMinSize : 5,
	FormEmailLengthMin : 6,
	FormEmailLengthMax : 30,
	FormPasswordLengthMin : 4,
	FormPasswordLengthMax : 20,
	FormUsernameLengthMin : 2,
	FormUsernameLengthMax : 20,
	ItemsPerPageOnSearch : 10, // 찾기에서 페이지 당 아이템 수
	ItemsPerPageOnSearchGallery : 12 // 찾기에서 사진첩의 페이지 당 아이템 수
};

function isEmpty(str) {
	obj = String(str);

	if(obj == null || obj == undefined || obj == 'null' || obj == 'undefined' || obj == '' ) return true;

	else return false;
}

// 이것을 summernote.js의 insertImage에 넣을것.
/*
$image.addClass("img-responsive");
$image.css({
  display: ''
});
*/