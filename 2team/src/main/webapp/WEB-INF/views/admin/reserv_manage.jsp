<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="../resources/css/adminMain.css" />
<title>Insert title here</title>
</head>
<body>
	<div id="header">
		<jsp:include page="header.jsp" /> 
	</div>
	<div id="main-wrapper">
		<div id="content-wrap">
		<div id="search"> 
			<form name="form1" method="get" action="/admin/reserv_manage">
				<select id="searchSelectBox" name="searchOption">
					<option value="">검색 옵션</option>
					<option value="name" <c:out value="${map.searchOption == 'res_name'?'selected':''}"/>>매장명</option>
					<option value="tel" <c:out value="${map.searchOption == 'tel'?'selected':''}"/>>연락처</option>
				</select>
				<input type="text" name="keyword" id="searchBar" placeholder="매장 검색" value="${map.keyword}">
			<input type="submit" id="searchBtn" value="검색">
			</form>
		</div>
		<table border="1">
			<tr>
				<td>예약번호</td>
				<td>예약자명</td>
				<td>매장명</td>
				<td>결제일</td>
				<td>예약일</td>
				<td>취소여부</td>
			</tr>
			<c:forEach var="bdto" items="${bdto}" varStatus="i">
			<tr>
				<td>${bdto.NO}</td>
				<td>${bdto.NAME}</td>
				<td>${bdto.RES_NAME}</td>
				<td>${bdto.DATE1}</td>
				<td>${bdto.DATE2}</td>
				<c:choose>
					<c:when test="${bdto.STATUS=='결제완료'}">
						<td id="status_${bdto.NO}" onclick="booking_cancel(${bdto.NO})"><span>취소 가능</span></td>
					</c:when>
					<c:when test="${bdto.STATUS!='결제완료'}">
						<td id="status_${bdto.NO}" onclick="booking_cancel(${bdto.NO})">${bdto.STATUS}</td>
					</c:when>
				</c:choose>
			</tr>
			</c:forEach>
		</table>
		</div>
	</div>
	
</body>
<script type="text/javascript" src="../resources/js/adminIndex.js"></script>
</html>