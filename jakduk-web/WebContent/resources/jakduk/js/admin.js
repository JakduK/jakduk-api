(function() {
	'use strict';

	angular.module('jakdukAdmin', ['ui.router'])
		.constant('MENU_ID_MAP', {
			HOME: 'admin.home',
			BOARD_CATEGORY_INIT: 'admin.boardInit',
			SEARCH_INDEX_INIT: 'admin.searchIndexInit',
			SEARCH_TYPE_INIT: 'admin.searchTypeInit',
			SEARCH_DATA_INIT: 'admin.searchDataInit'
		})
		.config(['$locationProvider', function($locationProvider) {
			$locationProvider.html5Mode(true);
		}])
		.config(['$stateProvider', 'MENU_ID_MAP', function($stateProvider, MENU_ID_MAP) {
			$stateProvider
				.state('admin', {
					abstract: true,
					url: '/admin',
					templateUrl: 'resources/jakduk/template/admin.html',
					controller: 'AdminController',
					controllerAs: 'ctrl'
				})
				.state(MENU_ID_MAP.HOME, {
					url: '/settings?{open:string}',
					templateUrl: 'resources/jakduk/template/admin-settings.html',
					controller: 'AdminHomeController',
					controllerAs: 'ctrl'
				})
				.state(MENU_ID_MAP.BOARD_CATEGORY_INIT, {
					url: '/board/category/init',
					templateUrl: 'resources/jakduk/template/admin-board-category-init.html',
					controller: 'AdminBoardCategoryInitController',
					controllerAs: 'ctrl'
				})
				.state(MENU_ID_MAP.SEARCH_INDEX_INIT, {
					url: '/search/index/init',
					templateUrl: 'resources/jakduk/template/admin-search-index-init.html',
					controller: 'AdminSearchIndexInitController',
					controllerAs: 'ctrl'
				})
				.state(MENU_ID_MAP.SEARCH_TYPE_INIT, {
					url: '/search/type/init',
					templateUrl: 'resources/jakduk/template/admin-search-type-init.html',
					controller: 'AdminSearchTypeInitController',
					controllerAs: 'ctrl'
				})
				.state(MENU_ID_MAP.SEARCH_DATA_INIT, {
					url: '/search/data/init',
					templateUrl: 'resources/jakduk/template/admin-search-data-init.html',
					controller: 'AdminSearchDataInitController',
					controllerAs: 'ctrl'
				});
		}])
		.controller("AdminController", ['$scope', '$state', 'MENU_ID_MAP', function($scope, $state, MENU_ID_MAP) {
			var self = this;

			self.currentMenu = $state.current.name;

			$scope.MENU_ID_MAP = MENU_ID_MAP;
			$scope.$on('$stateChangeSuccess', function (event, toState) {
				self.currentMenu = toState.name;
			});

		}])
		.controller("AdminHomeController", ['$http', '$state', 'BASE_URL', function($http, $state, BASE_URL) {
			var self = this;

			self.getData = getData;
			self.getDataLeague = getDataLeague;
			self.clearData = clearData;

			self.message = '대시보드';
			self.dataConn = "none";
			self.dataLeagueConn = "none";
			self.encyclopedias = [];
			self.fcOrigins = [];
			self.fcs = [];
			self.boardCategorys = [];
			self.attendanceLeagues = [];
			self.attendanceClubs = [];
			self.homeDescriptions = [];
			self.jakduSchedules = [];
			self.jakduScheduleGroups = [];
			self.competitions = [];

			if ($state.params.open) {
				self.message = '불러오는 중...';
				getData($state.params.open);
			}

			function getData(type) {
				var bUrl;

				if (type === "encyclopedia") {
					bUrl = '/admin/encyclopedia.json';
				} else if (type === "fcOrigin") {
					bUrl = '/admin/footballclub/origin.json';
				} else if (type === "fc") {
					bUrl = '/admin/footballclub.json';
				} else if (type === "boardCategory") {
					bUrl = '/admin/board/category.json';
				} else if (type === "attendanceLeague") {
					bUrl = '/admin/data/attendance/league.json';
				} else if (type === "attendanceClub") {
					bUrl = '/admin/data/attendance/club.json';
				} else if (type === "homeDescription") {
					bUrl = '/api/admin/home/descriptions';
				} else if (type === "jakduSchedule") {
					bUrl = '/admin/data/jakdu/schedule.json';
				} else if (type === "jakduScheduleGroup") {
					bUrl = '/admin/data/jakdu/schedule/group.json';
				} else if (type === "competition") {
					bUrl = '/admin/data/competition.json';
				}

				if (bUrl && self.dataConn === "none") {

					self.dataConn = "loading";

					$http.get(BASE_URL + bUrl).then(function(response) {
						clearData();

						var data = response.data;

						if (type == "encyclopedia") {
							self.encyclopedias = data.encyclopedias;
						} else if (type == "fcOrigin") {
							self.fcOrigins = data.fcOrigins;
						} else if (type == "fc") {
							self.fcs = data.fcs;
						} else if (type == "boardCategory") {
							self.boardCategorys = data.boardCategorys;
						} else if (type == "attendanceLeague") {
							self.attendanceLeagues = data.attendanceLeagues;
						} else if (type == "attendanceClub") {
							self.attendanceClubs = data.attendanceClubs;
						} else if (type == "homeDescription") {
							self.homeDescriptions = data.homeDescriptions;
						} else if (type == "jakduSchedule") {
							self.jakduSchedules = data.jakduSchedules;
						} else if (type == "jakduScheduleGroup") {
							self.jakduScheduleGroups = data.jakduScheduleGroups;
						} else if (type == "competition") {
							self.competitions = data.competitions;
						}

						self.dataConn = "none";
						self.message = '완료';
					}, function() {
						self.dataConn = "none";
						self.message = '오류 발생';
						alert("get data error");
					});
				}
			}


			function getDataLeague(league) {
				if (self.dataLeagueConn == "none") {

					self.dataLeagueConn = "loading";

					$http.get(BASE_URL + '/admin/attendance/league.json?league=' + league).then(function(response) {
						clearData();

						var data = response.data;

						self.attendanceLeagues = data.attendanceLeagues;

						self.dataLeagueConn = "none";
					}, function() {
						self.dataLeagueConn = "none";
						alert("get data league error");
					});
				}
			}

			function clearData() {
				self.encyclopedias = [];
				self.fcOrigins = [];
				self.fcs = [];
				self.boardCategorys = [];
				self.attendanceLeagues = [];
				self.attendanceClubs = [];
				self.homeDescriptions = [];
				self.jakduSchedules = [];
				self.jakduScheduleGroups = [];
				self.competitions = [];
			}

		}])
		.controller('AdminBoardCategoryInitController', ['$http', 'BASE_URL', function($http, BASE_URL) {
			var self = this;
			self.message = '처리중...';
			$http.post(BASE_URL + '/api/admin/board/category/init')
				.then(function (response) {
					self.message = response.data.result ? '기본 카테고리 생성완료' : '이미 생성되어 있음';
				}, function(response) {
					self.message = '오류 발생';
				});
		}])
		.controller('AdminSearchIndexInitController', ['$http', 'BASE_URL', function($http, BASE_URL) {
			var self = this;
			self.message = '처리중...';
			$http.post(BASE_URL + '/api/admin/search/index/init')
				.then(function (response) {
					self.message = '검색 색인 완료';
				}, function(response) {
					self.message = '오류 발생';
				});
		}])
		.controller('AdminSearchTypeInitController', ['$http', 'BASE_URL', function($http, BASE_URL) {
			var self = this;
			self.message = '처리중...';
			$http.post(BASE_URL + '/api/admin/search/type/init')
				.then(function (response) {
					self.message = '검색 색인 완료';
				}, function(response) {
					self.message = '오류 발생';
				});
		}])
		.controller('AdminSearchDataInitController', ['$http', 'BASE_URL', function($http, BASE_URL) {
			var self = this;
			self.message = '처리중...';
			$http.post(BASE_URL + '/api/admin/search/data/init')
				.then(function (response) {
					self.message = '검색 색인 완료';
				}, function(response) {
					self.message = '오류 발생';
				});
		}]);

})();