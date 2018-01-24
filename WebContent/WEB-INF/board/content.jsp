<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script>
function deleteArticle(idx){
	 $.ajax({
		   type:'post',
		   url:'/board/delete.json',
		   data: ({idx:idx}),
		   success:function(data){
			   console.log(data);
			  if(data.status == "success"){
				  alert("삭제되었습니다.");
				  location.reload();
			  }
		   }
	});
}
</script>	
	<table style="width: 580px;">							<!-- border은 테두리를 표시하는 속성입니다. -->
		<caption>게시글 조회</caption>						
		<tr>									
			<th>번호</th>						
			<td>${article.idx}</td>
			<th>작성자</th>
			<td>${article.writer}</td>
			<th>등록일</th>
			<td>${article.regdate}</td>
		</tr>
		<tr>
			<th>제목</th>			<!-- colspan은 행병합 속성입니다. -->
			<td colspan="3">${article.title}</td>
			<th>조회수</th>
			<td>${article.count}</td>
		</tr>
		<tr>
			<th height="300px;">내용</th>						
			<td  colspan="5"><pre>${article.content}</pre></td>
		</tr>
		<tr>
			<th>첨부파일</th>			
			<td  colspan="5">
				<c:if test="${empty article.filename}">없음</c:if>
				<c:if test="${not empty article.filename}"><a href="/board/download/${article.idx}">${article.filename}</a></c:if>
			</td>
		</tr>
	</table>
	<a href="#" onclick="deleteArticle('${article.idx}')">게시글삭제</a>
	<a href="#" onclick="closeContent()" style="float: right;">목록으로</a> 
