function fnAddComment(){
	$('#btnAddComment').on('click', function(){
		if($('.comment-line').val() == ''){
			Swal.fire({
				text : '댓글 내용을 입력하세요.'	,
				icon : 'error',
				confirmButtonText: '돌아가기'
			});
			return;
		}
		$.ajax({
			type : 'post',
			url : '/comment/addComment.do',
			data : $('fnAddComment').serialize(),
			dataType : 'json',
			success : function(resData) {
				if(resData.isAdd){
					Swal.fire({
						text : '댓글이 등록되었습니다',
						icon : 'success',
						confirmButtonText: '확인'
					});
					$('#content').val('');
					fnCommentList();
				}
			}
		})
	})
}

	var page = 1;
	var idolNo = 1;
function fnCommentList(){
	$.ajax({
		type : 'get',
		url : '/comment/list.do',
		data : 'idolNo=' + idolNo + '&page=' + page,
		dataType : 'json',
		success : function(resData){
			$('#commentList').empty();
			$.each(resData.commentList, function(i, comment){
				var str = '';
					if(comment.depth == 0) {
						str += '<span>';
					} else {
						str += '<span style="margin-left: 30px;">';
					}
				str += comment.usersDTO.userNo;
				str += '<br>' + comment.content;
				if('${session.loginEmail}' != '') {
					if('${session.loginUsersNo}' == comment.usersDTO.userNo) {
						str += '<input type="button" value="삭제" class="btnDeleteReply" data-comment_no="' + comment.replyNo + '">';
					} else {
						str += '<input type="button" value="답글" class="btnOpenReply">';
					}
				}
				str += '<div class="replyArea blind">';
				  /******************* 답글달 때 전송할 데이터는 4개(content, blogNo, groupNo, memberNo) *******************/
				  str += '  <form class="frmReply">';
				  str += '    <input type="text"   name="content"  class="replyContent" placeholder="답글을 작성해 주세요">';
				  str += '    <input type="hidden" name="idolNo"   value="' + comment.idolNo + '">';
				  str += '    <input type="hidden" name="groupNo"  value="' + comment.groupNo + '">';
				  str += '    <input type="hidden" name="userNo" value="${session.loginUsersNo}">';  // 수업 때 잘못 구현한 부분
				  str += '    <input type="button" value="답글작성완료" class="btnAddReply">';
				  str += '  </form>';
				  /*********************************************************************************************************/
				  str += '</div>';
			$('#commentList').append(str);
			})
		}
	})
}
function fnAddReply(){
	$('.btnAddReply').on('click', function(){
		if($(this).prevAll('.replyContent').val() == ''){
			alert('답글 내용을 입력하세요!');
			return;
		}
		$.ajax({
			type : 'post',
			url : '/comment/addReply.do',
			data : $(this).parent('.fnReply').serialize(),
			dataType : 'json',
			success : function(resData){
				if(resData.isAdd){
					Swal.fire({
						text : '답글이 등록되었습니다.',
						icon : 'success',
						confirmButtonText: '확인'
					});
					fnCommentList();
				}
			}
		})
	})
}
function fnDeleteReply(){
	$('.btnDeleteReply').on('click', function(){
		$.ajax({
			type : 'post',
			url : '/comment/remove.do',
			data : 'replyNo=' + replyNo,
			dataType : 'json',
			success : function(resData){
				if(resData.isRemove){
					Swal.fire({
						text : '삭제 완료되었습니다.'	,
						icon : 'success',
						confirmButtonText: '확인'
					});
					fnCommentList();
				} else {
					Swal.fire({
						text : '삭제에 실패하였습니다.',
						icon : 'error',
						confirmButtonText: '확인'
					});
				}
			}
		})
	})
}

$(function(){
	var currentPosition = parseInt($(".quick-menu").css("top"));
		$(window).scroll(function() {
	    	var position = $(window).scrollTop(); 
	    	$(".quick-menu").stop().animate({"top":position+currentPosition+"px"},1000);
  });
});

$(function(){
	fnCommentList();
	fnAddComment();
	fnAddReply();
})