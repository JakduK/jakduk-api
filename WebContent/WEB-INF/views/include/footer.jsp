<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>    
    
<!--=== Footer v3 ===-->
<div id="footer-v3" class="footer-v3">
    <div class="footer">
        <div class="container">
            <div class="row">
                <!-- About -->
                <div class="col-sm-3 md-margin-bottom-20">
                    <a href="<c:url value="/"/>"><strong><spring:message code="common.jakduk"/></strong></a>
                    <p><spring:message code="common.jakduk.description"/></p>    
                </div><!--/col-md-3-->
                <!-- End About -->                    

                <!-- Simple List -->
                <div class="col-sm-3">
                    <div class="thumb-headline"><h2><spring:message code="about"/></h2></div>
                    <ul class="list-unstyled simple-list margin-bottom-20">
                        <li><a href="<c:url value="/about/intro/refresh"/>"><spring:message code="about.site"/></a></li>
                        <li><a href="<c:url value="/board/free/98"/>"><spring:message code="about.site.use.guide"/></a></li>
                    </ul>
                </div><!--/col-md-3--> 

                <div class="col-sm-3">
                    <div class="thumb-headline"><h2><spring:message code="board"/></h2></div>
                    <ul class="list-unstyled simple-list margin-bottom-20">
                        <li><a href="<c:url value="/board/free/refresh"/>"><spring:message code="board.name.free"/></a></li>
                    </ul>

                    <div class="thumb-headline"><h2><spring:message code="gallery"/></h2></div>
                    <ul class="list-unstyled simple-list">
                        <li><a href="<c:url value="/gallery/list/refresh"/>"><spring:message code="gallery.list"/></a></li>
                    </ul>
                </div><!--/col-md-3-->

                <div class="col-sm-3">
                    <div class="thumb-headline"><h2><spring:message code="stats"/></h2></div>
                    <ul class="list-unstyled simple-list">
                        <li><a href="<c:url value="/stats/supporters/refresh"/>"><spring:message code="stats.supporters"/></a></li>
                        <li><a href="<c:url value="/stats/attendance/refresh"/>"><spring:message code="stats.attendance"/></a></li>
                    </ul>
                </div><!--/col-md-3-->
                <!-- End Simple List -->                        
            </div>
        </div> 
    </div><!--/footer-->

    <div class="copyright">
        <div class="container">
            <div class="row">
                <!-- Terms Info-->
                <div class="col-md-8">
                    <p>
						<a href="https://github.com/Pyohwan"><spring:message code="common.msg.project.jakduk"/></a> @ 2014 | 
						<a href="mailto:phjang1983@daum.net"><spring:message code="common.msg.mail.to.administrator"/></a>
                    </p>
                </div>
                <!-- End Terms Info-->

                <!-- Social Links -->
                <div class="col-md-4">  
                    <ul class="social-icons pull-right">
                        <li><a href="<c:url value="/rss"/>" data-original-title="Rss" class="rounded-x social_rss"></a></li>
                    </ul>
                </div>
                <!-- End Social Links -->
            </div>
        </div> 
    </div><!--/copyright--> 
</div>
<!--=== End Footer v3 ===--> 