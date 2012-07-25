<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
{"retcode":0,"result":[

<c:forEach var="item" items="${requestScope.list}" varStatus="status">
<c:if test="${status.index > 0 }">
,
</c:if>
{
"name":"${item.name}"
,"url":"downloadFile.ebook?catalogKey=${requestScope.catalogKey}&key=${item.key}&name=${item.name}"
,"icon":"data\/icon\/tangshi.png"
} 
</c:forEach>


]
}