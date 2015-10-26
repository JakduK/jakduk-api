angular.module('AngularOwl', []).directive('angularOwl', ['$timeout', function ($timeout) {

  /*
  Convers a string into a boolean
  Returns boolean or undefined
  */
  var convertStringToBoolean = function (string) {
    if (string != undefined) {
      return (string == 'true') ? true : false
    } else {
      return string
    }
  }

  var directive = { }

  // Template vars
  directive.restrict = 'AE'
  directive.replace = true
  directive.templateUrl = 'angular-owl.html'

  // Scope vars
  directive.scope = {
    elements:               '=elements',
    singleItem:             '@singleItem',
    items:                  '@items',
    itemsDesktop:           '=itemsDesktop',
    itemsTablet:            '=itemsTablet',
    itemsTabletSmall:       '=itemsTabletSmall',
    itemsMobile:            '=itemsMobile',
    itemsCustom:            '=itemsCustom',
    itemsScaleUp:           '@itemsScaleUp',
    slideSpeed:             '@slideSpeed',
    paginationSpeed:        '@paginationSpeed',
    rewindSpeed:            '@rewindSpeed',
    autoPlay:               '@autoPlay',
    stopOnHover:            '@stopOnHover',
    navigation:             '@navigation',
    navigationText:         '=navigationText',
    rewindNav:              '@rewindNav',
    scrollPerPage:          '@scrollPerPage',
    pagination:             '@pagination',
    paginationNumbers:      '@paginationNumbers',
    responsive:             '@responsive',
    responsiveRefreshRate:  '@responsiveRefreshRate',
    responsiveBaseWidth:    '=responsiveBaseWidth',
    baseClass:              '@baseClass',
    theme:                  '@theme',
    lazyLoad:               '@lazyLoad',
    lazyFollow:             '@lazyFollow',
    lazyEffect:             '@lazyEffect',
    autoHeight:             '@autoHeight',
    jsonPath:               '@jsonPath',
    dragBeforeAnimFinish:   '@dragBeforeAnimFinish',
    mouseDrag:              '@mouseDrag',
    touchDrag:              '@touchDrag',
    addClassActive:         '@addClassActive',
    transitionStyle:        '@transitionStyle',
    beforeUpdate:           '=beforeUpdate',
    afterUpdate:            '=afterUpdate',
    beforeInit:             '=beforeInit',
    afterInit:              '=afterInit',
    beforeMove:             '=beforeMove',
    afterMove:              '=afterMove',
    afterAction:            '=afterAction',
    startDragging:          '=startDragging',
    afterLazyLoad:          '=afterLazyLoad'
  }

  // On link function
  directive.link = function (scope, element, attrs) {
    $timeout(function () {
      var options = { }

      // Convert strings to booleans
      scope.singleItem = convertStringToBoolean(scope.singleItem)
      scope.itemsScaleUp = convertStringToBoolean(scope.itemsScaleUp)
      if (scope.autoPlay != undefined && (scope.autoPlay == 'true' || scope.autoPlay == 'false')) {
        scope.autoPlay = (scope.autoPlay == 'true') ? true : false
      }
      scope.stopOnHover = convertStringToBoolean(scope.stopOnHover)
      scope.navigation = convertStringToBoolean(scope.navigation)
      scope.rewindNav = convertStringToBoolean(scope.rewindNav)
      scope.scrollPerPage = convertStringToBoolean(scope.scrollPerPage)
      scope.pagination = convertStringToBoolean(scope.pagination)
      scope.paginationNumbers = convertStringToBoolean(scope.paginationNumbers)
      scope.lazyLoad = convertStringToBoolean(scope.lazyLoad)
      scope.lazyFollow = convertStringToBoolean(scope.lazyFollow)
      scope.dragBeforeAnimFinish = convertStringToBoolean(scope.dragBeforeAnimFinish)
      scope.mouseDrag = convertStringToBoolean(scope.mouseDrag)
      scope.touchDrag = convertStringToBoolean(scope.touchDrag)
      scope.addClassActive = convertStringToBoolean(scope.addClassActive)

      // Start plugin
      $(element).owlCarousel(scope)
    }, 0)
  }

  return directive
}])
