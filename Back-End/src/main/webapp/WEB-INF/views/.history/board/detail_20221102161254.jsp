<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <c:set var="root" value="${pageContext.request.contextPath}"></c:set>


    <!DOCTYPE html>
    <html lang="en">

    <head>

      <meta charset="utf-8">
      <meta http-equiv="X-UA-Compatible" content="IE=edge">
      <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
      <meta name="description" content="">
      <meta name="author" content="">

      <title>SSAFY</title>

      <!-- Custom fonts for this template-->
      <link href="${root}/assets/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
      <link
        href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
        rel="stylesheet">

      <!-- Custom styles for this template-->
      <link href="${root}/assets/css/sb-admin-2.min.css" rel="stylesheet">

    </head>

    <body class="bg-gradient-primary">

      <div class="container">

        <!-- Outer Row -->
        <div class="row justify-content-center">

          <div class="col-xl-10 col-lg-12 col-md-9">

            <div class="card o-hidden border-0 shadow-lg my-5">
              <div class="card-body p-0">
                <!-- Nested Row within Card Body -->
                <div class="row">
                  <div class="col-lg-6 d-none d-lg-block bg-login-image"></div>
                  <div class="col-lg-6">
                    <div class="p-5">
                      <div class="text-center">
                        <h1 class="h4 text-gray-900 mb-4" id="articleSubject">
                        </h1>
                      </div>
                      <div class="mt-5">
                        <h4 id="articleContent">내용</h4>

                      </div>
                      <form class="mt-5" id="modityForm" method="post" action="${root}/board/modifyForm">
                        <!-- <input type="hidden" name="act" value="modifyForm"> -->
                        <input type="hidden" name="articleNo" value="${dto.articleNo}">
                        <input type="hidden" name="userId" value="${dto.userId}">
                        <input type="hidden" name="subject" value="${dto.subject}">
                        <input type="hidden" name="content" value="${dto.content}">
                        <input type="hidden" name="hit" value="${dto.hit}">
                        <input type="hidden" name="register_time" value="${dto.registerTime}">

                        <button type="button" class="btn btn-primary btn-user btn-block" id="modifyForm">수정</button>
                      </form>
                      <a class="mt-3 btn btn-facebook btn-user btn-block" href="${root}/board/articles">나가기</a>
                      <a class="btn btn-google btn-user btn-block" href="#" id="deleteArticle">삭제</a>
                      <hr>

                      <!-- 첨부파일 목록 -->
                      <div>
                        <table id="filedoc">
                          <tr>
                            <th>첨부파일</th>
                          </tr>

                          <!-- <c:if test="${!empty dto.fileInfos}">
                            <c:forEach var="file" items="${dto.fileInfos}">
                              <tr>
                                <th>${file.originalFile} <a href="#" class="filedown" sfolder="${file.saveFolder}"
                                    sfile="${file.saveFile}" ofile="${file.originalFile}">[다운로드]</a></th>
                              </tr>
                            </c:forEach>
                          </c:if> -->

                        </table>
                      </div>

                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <form id="downform" action="${root}/board/download">
        <input type="hidden" name="sfolder">
        <input type="hidden" name="ofile">
        <input type="hidden" name="sfile">
      </form>

      <!-- Bootstrap core JavaScript-->
      <script src="${root}/assets/vendor/jquery/jquery.min.js"></script>
      <script src="${root}/assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

      <!-- Core plugin JavaScript-->
      <script src="${root}/assets/vendor/jquery-easing/jquery.easing.min.js"></script>

      <!-- Custom scripts for all pages-->
      <script src="${root}/assets/js/sb-admin-2.min.js"></script>

      <script>
        let files = document.querySelectorAll(".filedown");
        files.forEach(function (file) {
          file.addEventListener("click", function () {
            // document.querySelector("[name='sfolder']").value = file.getAttribute("sfolder");
            // document.querySelector("[name='ofile']").value = file.getAttribute("ofile");
            // document.querySelector("[name='sfile']").value = file.getAttribute("sfile");
            // document.querySelector("#downform").submit();
            let formdata = new FormData(document.getElementById("downform"));
            fetch("${root}")


          });
        });


        window.onload = loadArticle(`${articleNo}`)

        function loadArticle(articleNo) {
          fetch(`${root}/board/detail/\${articleNo}`)
            .then(response => response.json())
            .then(data => {
              console.log(data);
              document.getElementById("articleSubject").innerText = data.subject;
              document.getElementById("articleContent").innerText = data.content;
              if (data.fileInfos != null) {
                data.fileInfos.forEach(e => {
                  document.getElementById("filedoc").innerHTML = `														<tr>
																<th>\${e.originalFile} <a href="#" class="filedown"
																		sfolder="\${e.saveFolder}"
																		sfile="\${e.saveFile}"
																		ofile="\${e.originalFile}">[다운로드]</a></th>
															</tr>`;
                })
              }
            });


        }

        document.getElementById("deleteArticle").addEventListener("click", function () {
          if (confirm("정말 삭제하시겠습니까?")) {
            let config = {
              method: "DELETE",
              headers: {
                "Content-Type": "application/json",
              },
            };
            fetch(`${root}/board/delete/${articleNo}`, config)
              .then(response => response.text())
              .then(data => {
                alert(data);
                location.href = `${root}/board/articles`;
              });
          }
        });

        document.getElementById("modifyForm").addEventListener("click", function () {
          location.href = `${root}/board/upload/${articleNo}`;
        })
      </script>

    </body>

    </html>