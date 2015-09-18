<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="modal-body" ng-controller="LightboxCtrl"
    ng-swipe-left="Lightbox.nextImage()"
    ng-swipe-right="Lightbox.prevImage()">
  <!-- navigation -->
  <div class="lightbox-nav">
    	<button class="btn-u btn-brd btn-u" type="button" ng-click="Lightbox.prevImage()"><i class="fa fa-chevron-left"></i></button>
		<button class="btn-u btn-brd btn-u" type="button" ng-click="Lightbox.nextImage()"><i class="fa fa-chevron-right"></i></button>
		<button class="btn-u btn-brd btn-u" aria-hidden="true" ng-click="openNewTab()"><i class="fa fa-clone"></i></button>
		<!-- close button -->
		<button class="btn-u btn-brd btn-u" aria-hidden="true" ng-click="$dismiss()"><i class="fa fa-times"></i></button>
  </div>
  
	<div>
		<strong>{{Lightbox.imageCaption}}</strong>
</div>
<div>		
		<span aria-hidden="true" class="icon-user"></span> {{Lightbox.images[Lightbox.index].writer.username}}
		&nbsp;
		{{dateFromObjectId(Lightbox.images[Lightbox.index].id) | date:dateTimeFormat.dateTime}}
	</div>

  <div class="lightbox-image-container">
    <!-- image -->
    <img ng-if="!Lightbox.isVideo(Lightbox.image)" lightbox-src="{{Lightbox.imageUrl}}">

    <!-- video -->
    <div ng-if="Lightbox.isVideo(Lightbox.image)" class="embed-responsive embed-responsive-16by9">
      <!-- video file embedded directly -->
      <video ng-if="!Lightbox.isSharedVideo(Lightbox.image)"
          lightbox-src="{{Lightbox.imageUrl}}"
          controls
          autoplay="true">
      </video>

      <!-- video embedded with an external service using
           `ng-videosharing-embed` -->
      <embed-video ng-if="Lightbox.isSharedVideo(Lightbox.image)"
          lightbox-src="{{Lightbox.imageUrl}}"
          ng-href="{{Lightbox.imageUrl}}"
          iframe-id="lightbox-video"
          class="embed-responsive-item">
        <a ng-href="{{Lightbox.imageUrl}}">Watch video</a>
      </embed-video>
    </div>
  </div>
</div>