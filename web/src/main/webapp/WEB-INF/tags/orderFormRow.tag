<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="errors" required="true" type="java.util.Map" %>

<tr>
    <td>${label}<span style="color: red">*</span></td>
    <td>
        <div>
            <form:input type="text" class="form-control" placeholder="${label}" path="${name}"/>
        </div>
            <div>
                    <span style="color: red">${errors[name]}</span>
            </div>
    </td>
</tr>