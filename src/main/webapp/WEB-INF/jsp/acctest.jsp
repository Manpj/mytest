<%@page pageEncoding="utf-8" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link type="text/css" href="../styles/style.css" rel="stylesheet"
	media="screen" />
<title>公积金-city</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
	<div class="bg2">
		<header> <img src="../images/logo.png" alt></header>
		
	</div>
	<section id="content"> <!--查询-->
	<form action="searchById.do" method="post">
		<div class="search_add">
			<table>
				<tr>
					<td width="200px">ID：<input type="text" placeholder="不验证"
						name="district" />
						地区代号<select id="pProvince" name="pProvince">
				        <option value="660">华东地区</option>
				        <option value="661">华南地区</option>
				        <option value="662">华中地区</option>
				        <option value="663">华北地区</option>
				        <option value="664">西北地区</option>
				        <option value="665">西南地区</option>
				        <option value="666">东北地区</option>
				    </select>
					</td>
					<td><input type="submit" value="搜索" /></td>
				</tr>
			</table>
		</div>
		<div class="report_box">
			<!--数据区域：用表格展示数据-->
			<div id="report_data">
				<c:choose>
					<c:when test="${crawlerInterface!=null}">
						<table id="datalist">
							<tr>
								<th width="100px">ID</th>
								<th width="200px">城市</th>
								<th>状态</th>
								<th>测试</th>
							</tr>
							<tr>
								<td>${district}</td>
								<td>${crawlerInterface.city}</td>
								<td><c:choose>
										<c:when test="${crawlerInterface.status=='NORMAL'}">
									服务正常
									<a href="changeStatus.do?district=${district}&status=false">[下线]</a>
										</c:when>
										<c:when test="${crawlerInterface.status=='DOWN'}">
									服务终止
									<a href="changeStatus.do?district=${district}&status=true">[上线]</a>
										</c:when>
										<c:when test="${crawlerInterface.status=='PAUSE'}">
									服务暂停
									</c:when>
									</c:choose></td>
								<td><c:choose>
										<c:when test="${crawlerInterface.fetchInterface!=null}">
											<a href="accfetchtest.do?district=${district}">[填写验证码测试]</a>
										</c:when>
									</c:choose> <a href="accadictest.do?district=${district}">[自动化测试]</a></td>
							</tr>
						</table>
					</c:when>
					<c:when test="${district!=null}">
								${district}——未查询到结果
					</c:when>
				</c:choose>
			</div>
		</div>
	</form>
	</section>
</body>
</html>