<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="root" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">
<!-- Custom fonts for this template-->
<link href="${root}/assets/vendor/fontawesome-free/css/all.min.css"
	rel="stylesheet" type="text/css">
<link
	href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
	rel="stylesheet">

<!-- Custom styles for this template-->
<link href="${root}/assets/css/sb-admin-2.min.css" rel="stylesheet">
<title>SSAFY</title>
</head>

<body id="page-top">

	<!-- Page Wrapper -->
	<div id="wrapper">

		<%@ include file="/WEB-INF/views/include/sidebar.jsp"%>

		<!-- Content Wrapper -->
		<div id="content-wrapper" class="d-flex flex-column">

			<!-- Main Content -->
			<div id="content">
				<%@ include file="/WEB-INF/views/include/topbar.jsp"%>

				<!-- Start of List -->
				<div class="card shadow mb-4">
					<div class="card-header py-3">
						<h6 class="m-0 font-weight-bold text-primary">회원정보</h6>
					</div>
					
					<div class="card-body">
						<div class="table-responsive">
							<table class="table table-bordered" id="dataTable" width="100%"
								cellspacing="0">
								<!-- 제목행 -->
								<tr>
									<th>아이디</th>
									<th>이름</th>
									<th>이메일</th>
									<th>가입날짜</th>
								</tr>
								<!-- 상품목록 반복행 -->
								<c:forEach var="member" items="${userlist}">
									<tr>
										<td>${member.userId}</td>
										<td>${member.userName}</td>
										<td>${member.emailId}@${member.emailDomain}</td>
										<td>${member.joinDate}</td>
									</tr>
								</c:forEach>
							</table>
						</div>
					</div>
				</div>

				<!-- End of List -->
			</div>
			<!-- End of Main Content -->

			<!-- Footer -->
			<footer class="sticky-footer bg-white">
				<div class="container my-auto">
					<div class="copyright text-center my-auto">
						<span>Copyright &copy; Where is my home team 7</span>
					</div>
				</div>
			</footer>
			<!-- End of Footer -->

		</div>
		<!-- End of Content Wrapper -->

	</div>
	<!-- End of Page Wrapper -->

	<!-- Scroll to Top Button-->
	<a class="scroll-to-top rounded" href="#page-top"> <i
		class="fas fa-angle-up"></i>
	</a>

	<!-- Logout Modal-->
	<div class="modal fade" id="logoutModal" tabindex="-1" role="dialog"
		aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLabel">Ready to Leave?</h5>
					<button class="close" type="button" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">×</span>
					</button>
				</div>
				<div class="modal-body">Select "Logout" below if you are ready
					to end your current session.</div>
				<div class="modal-footer">
					<button class="btn btn-secondary" type="button"
						data-dismiss="modal">Cancel</button>
					<a class="btn btn-primary" href="${root}/user/logout">Logout</a>
				</div>
			</div>
		</div>
	</div>
  <script>
    window.onload=makeList();

    function makeList(){
      fetch(`${root}/user/users`)
      .then(response => response.json())
      .then(data => {
        
      })
    }
  </script>
	<!-- Bootstrap core JavaScript-->
	<script src="${root}/assets/vendor/jquery/jquery.min.js"></script>
	<script
		src="${root}/assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

	<!-- Core plugin JavaScript-->
	<script src="${root}/assets/vendor/jquery-easing/jquery.easing.min.js"></script>

	<!-- Custom scripts for all pages-->
	<script src="${root}/assets/js/sb-admin-2.min.js"></script>

	<!-- Page level plugins -->
	<script src="${root}/assets/vendor/chart.js/Chart.min.js"></script>

	<!-- Page level custom scripts -->
	<script src="${root}/assets/js/demo/chart-area-demo.js"></script>
	<script src="${root}/assets/js/demo/chart-pie-demo.js"></script>
</html>
