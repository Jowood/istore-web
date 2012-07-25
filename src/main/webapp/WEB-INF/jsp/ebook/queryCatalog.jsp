<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
{"retcode":0,"result":[

<c:forEach var="item" items="${requestScope.list}" varStatus="status"> 
<c:if test="${status.index >= 1}">
,
</c:if>
<c:if test="${item.key == null}">
{
"name":"${item.name}"
,"url":"#" 
,"icon":"data\/icon\/left.jpg" 
}
</c:if>
<c:if test="${item.key != null}">
{
"name":"${item.name}"
,"url":"queryFile.ebook?catalogKey=${item.key}&name=${item.name }"
<c:if test="${status.index % 3 == 0 }">
,"icon":"data\/icon\/book1.jpg"
</c:if>
<c:if test="${status.index % 3 == 1 }">
,"icon":"data\/icon\/book2.jpg"
</c:if>
<c:if test="${status.index % 3 == 2 }">
,"icon":"data\/icon\/book3.jpg"
</c:if>
} 
</c:if>
</c:forEach>
,{
"name":" "
,"url":"#" 
,"icon":"data\/icon\/left.jpg" 
}
,{
"name":"人员去向"
,"url":"search.emp?lock=true" 
,"icon":"data\/icon\/book1.jpg" 
}

]
}