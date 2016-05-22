(function() {
	'use strict';

	angular.module('jakdukAdmin', ['ui.router'])
		.constant('MENU_ID_MAP', {
			HOME: 'admin',
			INIT: {
				BOARD_CATEGORY: 'admin.initBoardCategory',
				SEARCH_INDEX: 'admin.initSearchIndex',
				SEARCH_TYPE: 'admin.initSearchType',
				SEARCH_DATA: 'admin.initSearchData'
			},
			WRITE: {
				ENCYCLOPEDIA: 'admin.writeEncyclopedia'
			},
			GET: {
				ENCYCLOPEDIA: 'admin.getEncyclopedia',
				FC_ORIGIN: 'admin.getFcOrigin',
				FC: 'admin.getFc',
				BOARD_CATEGORY: 'admin.getBoardCategory',
				HOME_DESCRIPTION: 'admin.getHomeDescription',
				ATTENDANCE_LEAGUE: 'admin.getAttendanceLeague',
				ATTENDANCE_CLUB: 'admin.getAttendanceClub',
				JAKDU_SCHEDULE: 'admin.getJakduSchedule',
				JAKDU_SCHEDULE_GROUP: 'admin.getJakduScheduleGroup',
				COMPETITION: 'admin.getCompetition'
			},
			URL: {
				HOME: '/admin',
				INIT: {
					BOARD_CATEGORY: '/board/category/init',
					SEARCH_INDEX: '/search/index/init',
					SEARCH_TYPE: '/search/type/init',
					SEARCH_DATA: '/search/data/init'
				},
				WRITE: {
					ENCYCLOPEDIA: '/encyclopedia/write'
				},
				GET: {
					ENCYCLOPEDIA: '/encyclopedia',
					FC_ORIGIN: '/fcOrigin',
					FC: '/fc',
					BOARD_CATEGORY: '/boardCategory',
					HOME_DESCRIPTION: '/homeDescription',
					ATTENDANCE_LEAGUE: '/attendanceLeague',
					ATTENDANCE_CLUB: '/attendanceClub',
					JAKDU_SCHEDULE: '/jakduSchedule',
					JAKDU_SCHEDULE_GROUP: '/jakduScheduleGroup',
					COMPETITION: '/competition'
				}
			}
		})
		.config(['$locationProvider', function($locationProvider) {
			$locationProvider.html5Mode(true);
		}])
		.config(['$stateProvider', 'MENU_ID_MAP', function($stateProvider, MENU_ID_MAP) {
			$stateProvider
				.state(MENU_ID_MAP.HOME, {
					url: MENU_ID_MAP.URL.HOME,
					templateUrl: 'resources/jakduk/template/admin.html',
					controller: 'AdminController',
					controllerAs: 'ctrl',
					data: {
						category: ''
					}
				})
				.state(MENU_ID_MAP.INIT.BOARD_CATEGORY, {
					url: MENU_ID_MAP.URL.INIT.BOARD_CATEGORY,
					templateUrl: 'resources/jakduk/template/admin-board-category-init.html',
					controller: 'AdminBoardCategoryInitController',
					controllerAs: 'ctrl',
					data: {
						category: 'init'
					}
				})
				.state(MENU_ID_MAP.INIT.SEARCH_INDEX, {
					url: MENU_ID_MAP.URL.INIT.SEARCH_INDEX,
					templateUrl: 'resources/jakduk/template/admin-search-index-init.html',
					controller: 'AdminSearchIndexInitController',
					controllerAs: 'ctrl',
					data: {
						category: 'init'
					}
				})
				.state(MENU_ID_MAP.INIT.SEARCH_TYPE, {
					url: MENU_ID_MAP.URL.INIT.SEARCH_TYPE,
					templateUrl: 'resources/jakduk/template/admin-search-type-init.html',
					controller: 'AdminSearchTypeInitController',
					controllerAs: 'ctrl',
					data: {
						category: 'init'
					}
				})
				.state(MENU_ID_MAP.INIT.SEARCH_DATA, {
					url: MENU_ID_MAP.URL.INIT.SEARCH_DATA,
					templateUrl: 'resources/jakduk/template/admin-search-data-init.html',
					controller: 'AdminSearchDataInitController',
					controllerAs: 'ctrl',
					data: {
						category: 'init'
					}
				})
				.state(MENU_ID_MAP.WRITE.ENCYCLOPEDIA, {
					url: MENU_ID_MAP.URL.WRITE.ENCYCLOPEDIA,
					templateUrl: 'resources/jakduk/template/admin-write-encyclopedia.html',
					controller: 'AdminWriteEncyclopediaController',
					controllerAs: 'ctrl',
					data: {
						category: 'write'
					}
				});

			angular.forEach(MENU_ID_MAP.GET, function(value, key) {
				$stateProvider.state(value, {
					url: MENU_ID_MAP.URL.GET[key],
					templateUrl: 'resources/jakduk/template/admin-settings.html',
					controller: 'AdminGetController',
					controllerAs: 'ctrl',
					data: {
						category: 'get'
					}
				});
			});

		}])
		.controller("AdminController", ['$scope', '$state', 'MENU_ID_MAP', function($scope, $state, MENU_ID_MAP) {
			var self = this;

			pickMenuInfo($state.current);

			$scope.MENU_ID_MAP = MENU_ID_MAP;
			$scope.$on('$stateChangeSuccess', function (event, toState) {
				pickMenuInfo(toState);
			});

			function pickMenuInfo(state) {
				self.isOpened = {};
				self.isOpened[state.data.category] = true;
				self.currentMenu = state.name;
			}

		}])
		.controller("AdminGetController", ['$http', '$state', 'BASE_URL', function($http, $state, BASE_URL) {
			var self = this;

			self.getData = getData;
			self.getDataLeague = getDataLeague;
			self.clearData = clearData;

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

			self.message = '불러오는 중...';
			getData($state.current.url.replace('/', ''));

			function getData(type) {
				var bUrl;

				if (type === "encyclopedia") {
					bUrl = '/api/admin/encyclopedias';
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

						if (type === "encyclopedia") {
							self.encyclopedias = data;
						} else if (type === "fcOrigin") {
							self.fcOrigins = data;
						} else if (type === "fc") {
							self.fcs = data;
						} else if (type === "boardCategory") {
							self.boardCategorys = data;
						} else if (type === "attendanceLeague") {
							self.attendanceLeagues = data;
						} else if (type === "attendanceClub") {
							self.attendanceClubs = data;
						} else if (type === "homeDescription") {
							self.homeDescriptions = data;
						} else if (type === "jakduSchedule") {
							self.jakduSchedules = data;
						} else if (type === "jakduScheduleGroup") {
							self.jakduScheduleGroups = data;
						} else if (type === "competition") {
							self.competitions = data;
						}

						self.dataConn = "none";
						self.message = (!data || !data.length) ? '완료' : '';
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
		}])
		.controller('AdminWriteEncyclopediaController', ['$http', '$location', 'BASE_URL', function($http, $location, BASE_URL) {
			var self = this;
			self.kind = 'player';
			self.submit = function() {
				var data = [];

				if (self.subject) {
					data.push('subject=' + self.subject);
				}

				if (self.kind) {
					data.push('kind=' + self.kind);
				}

				if (self.content) {
					data.push('content=' + self.content);
				}

				if (self.language) {
					data.push('language=' + self.language);
				}

				self.errorMessage = '';

				$http.post(BASE_URL + '/api/admin/encyclopedia/write', data.join('&'), {
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded'
					}
				}).then(function() {
					$location.url('/admin/encyclopedia');
				}, function() {
					self.errorMessage = 'SUBJECT, CONTENT 필수 입력';
				});
			};
		}]);

})();