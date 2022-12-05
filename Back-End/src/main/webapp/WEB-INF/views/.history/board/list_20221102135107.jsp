<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <c:set var="root" value="${pageContext.request.contextPath}"></c:set>
    <!DOCTYPE html>
    <html lang="ko">

    <head>
      <meta charset="utf-8">
      <meta http-equiv="X-UA-Compatible" content="IE=edge">
      <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
      <meta name="description" content="">
      <meta name="author" content="">
      <!-- Custom fonts for this template-->
      <link href="${root}/assets/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
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

        <%@ include file="/WEB-INF/views/include/sidebar.jsp" %>

          <!-- Content Wrapper -->
          <div id="content-wrapper" class="d-flex flex-column">

            <!-- Main Content -->
            <div id="content">
              <%@ include file="/WEB-INF/views/include/topbar.jsp" %>

                <!-- Start of List -->
                <div class="card shadow mb-4">
                  <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">공지사항</h6>
                  </div>

                  <form class="d-none d-sm-inline-block form-inline mr-3 ml-md-3 my-2 my-md-0 mw-100 navbar-search"
                    method="post" action="${root}/board/search">
                    <!-- <input type="hidden" name="act" value="search"> -->
                    <select id="searchScope" name="searchScope" class="custom-select custom-select-sm form-control form-control-sm">
                      <option value="subject">제목</option>
                      <option value="userId">작성자</option>
                    </select>
                    <div class="input-group">
                      <input type="text" class="form-control bg-light border-0 small" placeholder="Search for..."
                        aria-label="Search" aria-describedby="basic-addon2" name="searchData">
                      <div class="input-group-append">
                        <button class="btn btn-primary" type="button" id="searchButton">
                          <i class="fas fa-search fa-sm"></i>
                        </button>
                      </div>
                    </div>
                  </form>

                  <div class="card-body">
                    <div class="table-responsive">
                      <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                        <!-- 제목행 -->
                        <!-- <tr>
									<th>번호</th>
									<th>제목</th>
									<th>작성자</th>
									<th>조회수</th>
									<th>작성시간</th>
								</tr> -->
                        <!-- 상품목록 반복행 -->
                        <!-- <c:forEach var="dto" items="${list}">
									<tr>
										<td>${dto.articleNo}</td>
										<td><a
											href="${root}/board/detail?articleNo=${dto.articleNo}&userId=${dto.userId}">${dto.subject}</a></td>
										<td>${dto.userId}</td>
										<td>${dto.hit}</td>
										<td>${dto.registerTime}</td>
									</tr>
								</c:forEach> -->
                      </table>
                      <nav aria-label="Page navigation example" style="display: flex; justify-content: center;">
                        <ul class="pagination" id="buttonmenu">

                        </ul>
                      </nav>
                    </div>
                  </div>
                </div>

                <div class="container row" style="float: none; margin: 0 auto;">
                  <div class="col-md-3" style="float: none; margin: 0 auto;">
                    <a class="btn btn-primary btn-user btn-block" href="${root}/board/write">글쓰기</a>
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
      <a class="scroll-to-top rounded" href="#page-top"> <i class="fas fa-angle-up"></i>
      </a>

      <!-- Logout Modal-->
      <div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
        aria-hidden="true">
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="exampleModalLabel">Ready to Leave?</h5>
              <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">×</span>
              </button>
            </div>
            <div class="modal-body">Select "Logout" below if you are ready
              to end your current session.</div>
            <div class="modal-footer">
              <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
              <a class="btn btn-primary" href="${root}/user/logout">Logout</a>
            </div>
          </div>
        </div>
      </div>
      <script>
        window.onload = makeList(1);
        let listpage = document.getElementById("dataTable");

        function makeList(pagenation) {
          pagenation = parseInt(pagenation);
          fetch(`${root}/board/articles/` + pagenation)
            .then(response => response.json())
            .then(data => {
              listpage.innerHTML = `												<tr>
													<th>번호</th>
													<th>제목</th>
													<th>작성자</th>
													<th>조회수</th>
													<th>작성시간</th>
												</tr>`;
              let list = data["list"];
              data["list"].forEach(e => {
                listpage.innerHTML += `
													<tr>
														<td>\${e.articleNo}</td>
														<td><a
																href="${root}/board/article/\${e.articleNo}">\${e.subject}</a>
														</td>
														<td>\${e.userId}</td>
														<td>\${e.hit}</td>
														<td>\${e.registerTime}</td>
													</tr>
													`
              });
              let buttonmenu = document.getElementById("buttonmenu");
              buttonmenu.innerHTML = ``;
              let page = data["page"];
              if (page.prevCheck) {
                buttonmenu.innerHTML += `														
														<li class="page-item"><a class="page-link" href="#"> << </a>
														</li>`;
              }

              for (var i = 0; i <= page.endButton - page.startButton; i++) {
                if (page.startButton + i == page.nowPage) {
                  buttonmenu.innerHTML += `<li class="page-item active" aria-current="page"><a
																	class="page-link"
																	href="#">\${page.startButton + i}</a>
															</li>
															</li>`;
                } else {
                  buttonmenu.innerHTML += `
															<li class="page-item"><a class="page-link"
																	href="#">\${page.startButton + i}</a>
															</li>
															</li>`;
                }
              }

              if (page.nextCheck) {
                buttonmenu.innerHTML += `	<li class="page-item"><a class="page-link" href="#">>></a>
														</li>`;
              }

              document.querySelectorAll(".page-item").forEach(target => target.addEventListener("click", function () {
                if (this.outerText == ">>") makeList(page.nextClickPage);
                else if (this.outerText == "<<") makeList(page.prevClickPage);
                else makeList(this.outerText);
              }));


            })
        }

        console.dir(document.getElementById("searchScope"));
        document.getElementById("searchButton").addEventListener("click", function () {
          let searchScope = document.getElementById("searchScope").value;
          let searchData = document.getElementById("searchData").value;
          let searchparam = {
            searchScope: searchScope,
            searchData: searchData
          };
          fetch(`${root}/board/search/\${searchScope}/\${searchData}`)
            .then(response => response.json())
            .then(data => {
              listpage.innerHTML = `												<tr>
													<th>번호</th>
													<th>제목</th>
													<th>작성자</th>
													<th>조회수</th>
													<th>작성시간</th>
												</tr>`;

              let list = data["list"];

              data.forEach(e => {
                listpage.innerHTML += `
													<tr>
														<td>\${e.articleNo}</td>
														<td><a
																href="${root}/board/article/\${e.articleNo}">\${e.subject}</a>
														</td>
														<td>\${e.userId}</td>
														<td>\${e.hit}</td>
														<td>\${e.registerTime}</td>
													</tr>
													`;
              });
              let buttonmenu = document.getElementById("buttonmenu");
              buttonmenu.innerHTML = ``;
            });

        })

      </script>
      <!-- Bootstrap core JavaScript-->
      <script src="${root}/assets/vendor/jquery/jquery.min.js"></script>
      <script src="${root}/assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

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