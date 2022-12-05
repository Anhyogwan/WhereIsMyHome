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
                        <h1 class="h4 text-gray-900 mb-4">공지 등록</h1>
                      </div>
                      <form class="user" id="registForm" method="post" enctype="multipart/form-data"
                        action=${root}/board/regist>
                        <!-- <input type="hidden" name="act" value="regist"> -->
                        <div class="form-group">
                          <input type="text" class="form-control form-control-user" id="exampleInput1"
                            aria-describedby="emailHelp" name="subject" placeholder="제목">
                        </div>
                        <div class="form-group">
                          <input type="text" class="form-control form-control-user" name="userId" id="userId"
                            value="${userinfo.userId}" readonly>
                        </div>

                        <div class="form-group">
                          <input type="textarea" class="h-100 form-control form-control-user" id="exampleInput2"
                            aria-describedby="emailHelp" name="content" placeholder="내용">
                        </div>

                        <div class="form-group" align="left">
                          <label for="upfile">첨부파일:</label>
                          <input type="file" class="form-control border" name="upfile" id="upfile" multiple="multiple">
                        </div>

                        <input type="button" class="btn btn-primary btn-user btn-block" value="등록" id="registboard">
                        <a class="btn btn-google btn-user btn-block" href="${root}/board/articles">나가기</a>
                      </form>
                      <hr>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <script>
        // document.getElementById("registButton").addEventListener("click", function () {

        //   let userId = document.getElementById("userId").value;
        //   let subject = document.getElementById("exampleInput1").value;
        //   let content = document.getElementById("exampleInput2").value;
        //   console.log(userId);
        //   console.log(subject);
        //   console.log(content);
        //   let write = {
        //     userId: userId,
        //     subject: subject,
        //     content: content
        //   };
        //   let config = {
        //     method: "POST",
        //     headers: {
        //       "Content-Type": "application/json"
        //     },
        //     body: JSON.stringify(write)
        //   };
        //   fetch(`${root}/board/upload`, config)
        //     .then(response => response.text())
        //     .then(data => {
        //       location.href = `${root}/board/articles`;
        //     })
        // })

        document.getElementById("registboard").addEventListener("click", function () {
          fetch(`${root}/board/regist`, {
            method: "POST",
            body: new FormData(document.getElementById("registForm"))
          })
            .then(response => response.json())
            .then(data => {
              location.href=""
              console.log(data);
            })
        });
      </script>
      <!-- Bootstrap core JavaScript-->
      <script src="${root}/assets/vendor/jquery/jquery.min.js"></script>
      <script src="${root}/assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

      <!-- Core plugin JavaScript-->
      <script src="${root}/assets/vendor/jquery-easing/jquery.easing.min.js"></script>

      <!-- Custom scripts for all pages-->
      <script src="${root}/assets/js/sb-admin-2.min.js"></script>

    </body>

    </html>