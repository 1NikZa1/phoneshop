<%@ tag pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="currentPage" type="java.lang.Integer" required="true" %>
<%@ attribute name="pagesTotal" type="java.lang.Integer" required="true" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<html>
<body>
<nav class="float-end" aria-label="navigation">
    <ul class="pagination">


        <li class="page-item ${currentPage eq 1 ? "disabled": ""}">
            <a class="page-link" href="<tags:pageHref page="${currentPage-1}"/>" aria-label="Previous">Previous</a>
        </li>


        <c:forEach begin="${currentPage - 4 > 0 ? currentPage - 4 : 1}"
                   end="${currentPage + 4 < pagesTotal ? currentPage + 4 : pagesTotal}" var="page">
            <li class="page-item ${currentPage eq page ? "active" :""}">
                <a class="page-link" href="<tags:pageHref page="${page}"/>">${page}</a>
            </li>
        </c:forEach>


        <li class="page-item ${currentPage eq pagesTotal ? "disabled": ""}">
            <a class="page-link" href="<tags:pageHref page="${currentPage+1}"/>" aria-label="Next">Next</a>
        </li>

    </ul>
</nav>
</body>
</html>
