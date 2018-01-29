<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	静态下载：
	<br>
	<a href="${pageContext.request.contextPath }/case/abc.txt">download abc.txt</a>
	<a href="${pageContext.request.contextPath }/case/downloadtest.jsp">download test.jsp</a>
	<br>
	<br>
	<font color="red">${requestScope.message }</font>
	<c:if test="${!empty requestScope.lists}">
	动态下载：<br>
		<form action="${pageContext.request.contextPath }/download"
			method="post">
			<table border="1px" cellpadding="10px" cellspacing="0px">
					<tr>
						<th>文件名</th>
						<th>下载</th>
					<tr>
				<c:forEach items="${requestScope.lists }" var="file">
					<tr>
						<td>${file.fileName }</td>
						<td><input type="checkbox" name="downloadFile" value="${file.filePath}"/> </td>
					</tr>
				</c:forEach>
			<tr><td colspan="2"><input type="submit" value="下载" style="width: 100px;background-color: pink"/></td></tr>
			</table>
		</form>
	</c:if>
	<a href="${pageContext.request.contextPath }/case/index.jsp">返回首页</a>
</body>
</html>