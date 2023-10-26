<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
    pageContext.setAttribute("LF", "\n");
    pageContext.setAttribute("BR", "<br/>");
%>

<!DOCTYPE html>
<html lang="kr">
<head>
    <%@ include file="/include/header.jspf"%>
</head>
<body>
<%@ include file="/include/navigation.jspf"%>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-12">
        <div class="panel panel-default">
            <header class="qna-header">
                <h2 class="qna-title"><c:out value="${question.title}"/></h2>
            </header>
            <div class="content-main">
                <article class="article">
                    <div class="article-header">
                        <div class="article-header-thumb">
                            <img src="https://graph.facebook.com/v2.3/100000059371774/picture" class="article-author-thumb" alt="">
                        </div>
                        <div class="article-header-text">
                            <a href="#" class="article-author-name"><c:out value="${question.writer}"/></a>
                            <a href="#" class="article-header-time" title="퍼머링크">
                                <fmt:formatDate value="${question.createdDate}" pattern="YYYY-MM-dd HH:mm:ss"/>
                                <i class="icon-link"></i>
                            </a>
                        </div>
                    </div>
                    <div class="article-doc">
                        <c:out value="${fn:replace(question.contents, LF, BR)}" escapeXml="false"/>
                    </div>
                    <div class="article-util">
                        <ul class="article-util-list">
                            <li>
                                <a class="link-modify-article" href="/questions/423/form">수정</a>
                            </li>
                            <li>
                                <form class="form-delete" action="/questions/423" method="POST">
                                    <input type="hidden" name="_method" value="DELETE">
                                    <button class="link-delete-article" type="submit">삭제</button>
                                </form>
                            </li>
                            <li>
                                <a class="link-modify-article" href="/index.html">목록</a>
                            </li>
                        </ul>
                    </div>
                </article>

                <div class="qna-comment">
                    <div class="qna-comment-slipp">
                        <p class="qna-comment-count"><strong><c:out value="${fn:length(answerList)}"/></strong>개의 의견</p>
                        <div class="qna-comment-slipp-articles">
                            <c:forEach items="${answerList}" var="answer">
                            <article class="article" id="answer-${answer.answerId}">
                                <div class="article-header">
                                    <div class="article-header-thumb">
                                        <img src="https://graph.facebook.com/v2.3/1324855987/picture" class="article-author-thumb" alt="">
                                    </div>
                                    <div class="article-header-text">
                                        <a href="#" class="article-author-name">${answer.writer}</a>
                                        <a href="#" class="article-header-time">
                                            <fmt:formatDate value="${answer.createdDate}" pattern="YYYY-MM-dd HH:mm:ss"/>
                                        </a>
                                    </div>
                                </div>
                                <div class="article-doc comment-doc">
                                    <c:out value="${fn:replace(answer.contents, LF, BR)}" escapeXml="false"/>
                                </div>
                                <div class="article-util">
                                    <ul class="article-util-list">
                                        <li>
                                            <a class="link-modify-article" href="/api/qna/updateAnswer?answerId=${answer.answerId}">수정</a>
                                        </li>
                                        <li>
                                            <form class="form-delete">
                                                <input type="hidden" name="answerId" value="<c:out value="${answer.answerId}"/>">
                                                <button type="submit" class="link-delete-article">삭제</button>
                                            </form>
                                        </li>
                                    </ul>
                                </div>
                            </article>
                            </c:forEach>

                            <div class="answerWrite">
                                <form name="answer" method="post">
                                    <input type="hidden" name="questionId" value="${question.questionId}">
                                    <div class="form-group" style="padding:14px;">
                                        <input class="form-control" id="writer" name="writer" placeholder="이름">
                                        <textarea class="form-control" placeholder="" name="contents" id="contents"></textarea>
                                    </div>
                                    <input type="submit" class="btn btn-success pull-right" value="답변하기"/>
                                    <div class="clearfix" />
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<script type="text/template" id="answerTemplate">
    <article class="article">
        <div class="article-header">
            <div class="article-header-thumb">
                <img src="https://graph.facebook.com/v2.3/1324855987/picture" class="article-author-thumb" alt="">
            </div>
            <div class="article-header-text">
                {0}
                <div class="article-header-time">{1}</div>
            </div>
        </div>
        <div class="article-doc comment-doc">
            {2}
        </div>
        <div class="article-util">
            <ul class="article-util-list">
                <li>
                    <a class="link-modify-article" href="/api/qna/updateAnswer/{3}">수정</a>
                </li>
                <li>
                    <form class="form-delete">
                        <input type="hidden" name="answerId" value="{4}" />
                        <button type="submit" class="link-delete-article">삭제</button>
                    </form>
                </li>
            </ul>
        </div>
    </article>
</script>

<%@ include file="/include/footer.jspf" %>
</body>
</html>
