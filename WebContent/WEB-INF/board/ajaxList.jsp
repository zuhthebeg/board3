<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:forEach items="${articleList}" var="article">
	<tr>
		<td>${article.idx}</td>
		<td><a href="#layer" onclick="openContent('${article.idx}')">${article.title} </a></td>
		<td>${article.writer}</td>
		<td>${article.regdate}</td>
		<td>${article.count}</td> 
		<td>
			<c:if test="${empty article.filename}">없음</c:if>
			<c:if test="${not empty article.filename}"><a href="/board/download/${article.idx}">다운로드</a></c:if>
		</td>  
	</tr>
</c:forEach>
