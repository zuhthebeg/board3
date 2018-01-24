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
				  alert("�����Ǿ����ϴ�.");
				  location.reload();
			  }
		   }
	});
}
</script>	
	<table style="width: 580px;">							<!-- border�� �׵θ��� ǥ���ϴ� �Ӽ��Դϴ�. -->
		<caption>�Խñ� ��ȸ</caption>						
		<tr>									
			<th>��ȣ</th>						
			<td>${article.idx}</td>
			<th>�ۼ���</th>
			<td>${article.writer}</td>
			<th>�����</th>
			<td>${article.regdate}</td>
		</tr>
		<tr>
			<th>����</th>			<!-- colspan�� �ິ�� �Ӽ��Դϴ�. -->
			<td colspan="3">${article.title}</td>
			<th>��ȸ��</th>
			<td>${article.count}</td>
		</tr>
		<tr>
			<th height="300px;">����</th>						
			<td  colspan="5"><pre>${article.content}</pre></td>
		</tr>
		<tr>
			<th>÷������</th>			
			<td  colspan="5">
				<c:if test="${empty article.filename}">����</c:if>
				<c:if test="${not empty article.filename}"><a href="/board/download/${article.idx}">${article.filename}</a></c:if>
			</td>
		</tr>
	</table>
	<a href="#" onclick="deleteArticle('${article.idx}')">�Խñۻ���</a>
	<a href="#" onclick="closeContent()" style="float: right;">�������</a> 
