<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
    
<!DOCTYPE html>
<html ng-app="jakdukApp">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<jsp:include page="../include/html-header.jsp"></jsp:include>

	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/unify/assets/plugins/layer-slider/layerslider/css/layerslider.css">
</head>

<body>
<div class="wrapper" ng-controller="sampleCtrl">
	<jsp:include page="../include/navigation-header.jsp"/>
	
    <!--=== Slider ===-->
    <div id="layerslider" style="width: 100%; height: 500px; margin: 0px auto;">
        <!-- First slide -->
        <div class="ls-slide" data-ls="slidedelay:4500;transition2d:25;">
            <img src="<%=request.getContextPath()%>/resources/unify/assets/img/sliders/layer/bg1.jpg" class="ls-bg" alt="Slide background"/>

            <img class="ls-l" src="<%=request.getContextPath()%>/resources/unify/assets/img/mockup/iphone1.png" style="top: 85%; left: 44%;" 
            data-ls="offsetxin:left; durationin:1500; delayin:900; fadein:false; offsetxout:left; durationout:1000; fadeout:false;" />
            
            <img src="<%=request.getContextPath()%>/resources/unify/assets/img/mockup/iphone.png" alt="Slider image" class="ls-s-1" style=" top:62px; left: 29%;" 
            data-ls="offsetxin:left; durationin:1500; delayin:1500; fadein:false; offsetxout:left; durationout:1000; fadeout:false;">
        
            <span class="ls-s-1" style=" text-transform: uppercase; line-height: 45px; font-size:35px; color:#fff; top:200px; left: 590px; slidedirection : top; slideoutdirection : bottom; durationin : 3500; durationout : 3500; delayin : 1000;">
                Fully Responsive <br> Bootstrap 3 Template
            </span>

            <a class="btn-u btn-u-orange ls-s-1" href="#" style=" padding: 9px 20px; font-size:25px; top:340px; left: 590px; slidedirection : bottom; slideoutdirection : top; durationin : 3500; durationout : 2500; delayin : 1000; ">
                Download Now
            </a>
        </div>	
        
      <!--Second Slide-->
        <div class="ls-slide" data-ls="transition2d:93;">
            <img src="<%=request.getContextPath()%>/resources/unify/assets/img/bg/5.jpg" class="ls-bg" alt="Slide background">

            <i class="fa fa-chevron-circle-right ls-s-1" style=" color: #fff; font-size: 24px; top:70px; left: 40px; slidedirection : left; slideoutdirection : top; durationin : 1500; durationout : 500; "></i> 

            <span class="ls-s-2" style=" color: #fff; font-weight: 200; font-size: 22px; top:70px; left: 70px; slidedirection : top; slideoutdirection : bottom; durationin : 1500; durationout : 500; ">
                Fully Responsive and Easy to Customize
            </span>

            <i class="fa fa-chevron-circle-right ls-s-1" style=" color: #fff; font-size: 24px; top:120px; left: 40px; slidedirection : left; slideoutdirection : top; durationin : 2500; durationout : 1500; "></i> 

            <span class="ls-s-2" style=" color: #fff; font-weight: 200; font-size: 22px; top:120px; left: 70px; slidedirection : top; slideoutdirection : bottom; durationin : 2500; durationout : 1500; ">
                Revolution and Layer Slider Included 
            </span>

            <i class="fa fa-chevron-circle-right ls-s-1" style=" color: #fff; font-size: 24px; top:170px; left: 40px; slidedirection : left; slideoutdirection : top; durationin : 3500; durationout : 3500; "></i> 

            <span class="ls-s-2" style=" color: #fff; font-weight: 200; font-size: 22px; top:170px; left: 70px; slidedirection : top; slideoutdirection : bottom; durationin : 3500; durationout : 2500; ">
                1000+ Glyphicons Pro and Font Awesome Icons 
            </span>

            <i class="fa fa-chevron-circle-right ls-s-1" style=" color: #fff; font-size: 24px; top:220px; left: 40px; slidedirection : left; slideoutdirection : top; durationin : 4500; durationout : 3500; "></i> 

            <span class="ls-s-2" style=" color: #fff; font-weight: 200; font-size: 22px; top:220px; left: 70px; slidedirection : top; slideoutdirection : bottom; durationin : 4500; durationout : 3500; ">
                Revolution and Layer Slider Included 
            </span>

            <i class="fa fa-chevron-circle-right ls-s-1" style=" color: #fff; font-size: 24px; top:270px; left: 40px; slidedirection : left; slideoutdirection : top; durationin : 5500; durationout : 4500; "></i> 

            <span class="ls-s-2" style=" color: #fff; font-weight: 200; font-size: 22px; top:270px; left: 70px; slidedirection : top; slideoutdirection : bottom; durationin : 5500; durationout : 4500; ">
                60+ Template Pages and 20+ Plugins Included
            </span>

            <a class="btn-u btn-u-blue ls-s1" href="#" style=" padding: 9px 20px; font-size:25px; top:340px; left: 40px; slidedirection : bottom; slideoutdirection : bottom; durationin : 6500; durationout : 3500; ">
                Twitter Bootstrap 3
            </a>

            <img src="<%=request.getContextPath()%>/resources/unify/assets/img/mockup/iphone1.png" alt="Slider Image" class="ls-s-1" style=" top:30px; left: 650px; slidedirection : right; slideoutdirection : bottom; durationin : 1500; durationout : 1500; ">
        </div>                

        <!--Third Slide-->
        <div class="ls-slide" style="slidedirection: right; transition2d: 92,93,105; ">
            <img src="<%=request.getContextPath()%>/resources/unify/assets/img/sliders/layer/bg2.jpg" class="ls-bg" alt="Slide background">

            <span class="ls-s-1" style=" color: #777; line-height:45px; font-weight: 200; font-size: 35px; top:100px; left: 50px; slidedirection : top; slideoutdirection : bottom; durationin : 1000; durationout : 1000; ">
                Unify is Fully Responsive <br> Twitter Bootstrap 3 Template
            </span>

            <a class="btn-u btn-u-green ls-s-1" href="#" style=" padding: 9px 20px; font-size:25px; top:220px; left: 50px; slidedirection : bottom; slideoutdirection : bottom; durationin : 2000; durationout : 2000; ">
                Find Out More
            </a>

            <img src="<%=request.getContextPath()%>/resources/unify/assets/img/mockup/iphone.png" alt="Slider Image" class="ls-s-1" style=" top:30px; left: 670px; slidedirection : right; slideoutdirection : bottom; durationin : 3000; durationout : 3000; ">
        </div>
        <!--End Third Slide-->        
	
	</div>
	
		<!--=== Content Part ===-->
	<div class="container content" >
	
	</div>
	
	<jsp:include page="../include/footer.jsp"/>

</div><!-- /.container -->

<!-- Bootstrap core JavaScript
  ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<%=request.getContextPath()%>/resources/jquery/dist/jquery.min.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/plugins/layer-slider/layerslider/js/greensock.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/plugins/layer-slider/layerslider/js/layerslider.transitions.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/plugins/layer-slider/layerslider/js/layerslider.kreaturamedia.jquery.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/resources/unify/assets/js/plugins/layer-slider.js"></script>

<script type="text/javascript">
var jakdukApp = angular.module("jakdukApp", []);

jakdukApp.controller("sampleCtrl", function($scope, $http) {
	angular.element(document).ready(function() {
      	App.init();
        LayerSlider.initLayerSlider();
	});		
	
});
</script>

<jsp:include page="../include/body-footer.jsp"/>

</body>
</html>