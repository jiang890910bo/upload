<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page isELIgnored="false"%>
<%-- <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> --%>
<%-- <c:set var="ctx" value="${pageContext.request.contextPath}" /> --%>
<% 
String path = request.getContextPath(); 
// 获得本项目的地址(例如: http://localhost:8080/MyApp/)赋值给basePath变量 
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"; 
%>
<html>
<body>
	<form action="<%=basePath %>upload" enctype="multipart/form-data"
		method="post">
		<input type="file" name="file1" /> <input type="file" name="file2" />
		<input type="text" name="text" /> <input type="submit" value="Submit" />
	</form>
</body>
</html>
