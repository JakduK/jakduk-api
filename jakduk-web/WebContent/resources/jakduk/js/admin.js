(function() {
	'use strict';

	angular.module('jakdukAdmin', ['ui.router'])
		.constant('MENU_ID_MAP', {
			HOME: {
				ID: 'admin',
				URL: '/admin',
				CONTROLLER: 'AdminController',
				TEMPLATE: 'resources/jakduk/template/admin.html'
			},
			INIT: {
				BOARD_CATEGORY: {
					ID: 'admin.initBoardCategory',
					URL: '/board/category/init',
					TEMPLATE: 'resources/jakduk/template/admin-board-category-init.html',
					CONTROLLER: 'AdminBoardCategoryInitController'
				},
				SEARCH_INDEX: {
					ID: 'admin.initSearchIndex',
					URL: '/search/index/init',
					TEMPLATE: 'resources/jakduk/template/admin-search-index-init.html',
					CONTROLLER: 'AdminSearchIndexInitController'
				},
				SEARCH_TYPE: {
					ID: 'admin.initSearchType',
					URL: '/search/type/init',
					TEMPLATE: 'resources/jakduk/template/admin-search-type-init.html',
					CONTROLLER: 'AdminSearchTypeInitController'
				},
				SEARCH_DATA: {
					ID: 'admin.initSearchData',
					URL: '/search/data/init',
					TEMPLATE: 'resources/jakduk/template/admin-write-encyclopedia.html',
					CONTROLLER: 'AdminWriteEncyclopediaController'
				}
			},
			WRITE: {
				ENCYCLOPEDIA: {
					ID: 'admin.writeEncyclopedia',
					URL: '/encyclopedia/write/:id',
					TEMPLATE: 'resources/jakduk/template/admin-write-encyclopedia.html',
					CONTROLLER: 'AdminWriteEncyclopediaController'
				},
				FC_ORIGIN: {
					ID: 'admin.writefcOrigin',
					URL: '/fcOrigin/write/:id',
					TEMPLATE: 'resources/jakduk/template/admin-write-football-club-origin.html',
					CONTROLLER: 'AdminWriteFootballClubOriginController'
				},
				FC: {
					ID: 'admin.writefc',
					URL: '/fc/write/:id',
					TEMPLATE: 'resources/jakduk/template/admin-write-football-club.html',
					CONTROLLER: 'AdminWriteFootballClubController'
				},
				BOARD_CATEGORY: {
					ID: 'admin.writeBoardCategory',
					URL: '/boardCategory/write/:id',
					TEMPLATE: 'resources/jakduk/template/admin-write-board-category.html',
					CONTROLLER: 'AdminWriteBoardCategoryController'
				},
				THUMBNAIL_SIZE: {
					ID: 'admin.writeThumbnailSize',
					URL: '/thumbnail/size/write/:id',
					TEMPLATE: 'resources/jakduk/template/admin-write-thumbnail-size.html',
					CONTROLLER: 'AdminWriteThumbnailSizeController'
				},
				HOME_DESCRIPTION: {
					ID: 'admin.writeHomeDescription',
					URL: '/homeDescription/write/:id',
					TEMPLATE: 'resources/jakduk/template/admin-write-home-description.html',
					CONTROLLER: 'AdminWriteHomeDescriptionController'
				},
				ATTENDANCE_LEAGUE: {
					ID: 'admin.writeAttendanceLeague',
					URL: '/attendanceLeague/write/:id',
					TEMPLATE: 'resources/jakduk/template/admin-write-attendance-league.html',
					CONTROLLER: 'AdminWriteAttendanceLeagueController'
				},
				ATTENDANCE_CLUB: {
					ID: 'admin.writeAttendanceClub',
					URL: '/attendanceClub/write/:id',
					TEMPLATE: 'resources/jakduk/template/admin-write-attendance-club.html',
					CONTROLLER: 'AdminWriteAttendanceClubController'
				},
				JAKDU_SCHEDULE: {
					ID: 'admin.writeJakduSchedule',
					URL: '/jakduSchedule/write/:id',
					TEMPLATE: 'resources/jakduk/template/admin-write-jakdu-schedule.html',
					CONTROLLER: 'AdminWriteJakduScheduleController'
				},
				JAKDU_SCHEDULE_GROUP: {
					ID: 'admin.writeJakduScheduleGroup',
					URL: '/jakduScheduleGroup/write/:id',
					TEMPLATE: 'resources/jakduk/template/admin-write-jakdu-schedule-group.html',
					CONTROLLER: 'AdminWriteJakduScheduleGroupController'
				},
				COMPETITION: {
					ID: 'admin.writeCompetition',
					URL: '/competition/write/:id',
					TEMPLATE: 'resources/jakduk/template/admin-write-competition.html',
					CONTROLLER: 'AdminWriteCompetitionController'
				}
			},
			GET: {
				ENCYCLOPEDIA: {
					ID: 'admin.getEncyclopedia',
					URL :'/encyclopedia',
					CONTROLLER: 'AdminGetController',
					TEMPLATE: 'resources/jakduk/template/admin-data-view.html'
				},
				FC_ORIGIN: {
					ID: 'admin.getFcOrigin',
					URL: '/fcOrigin',
					CONTROLLER: 'AdminGetController',
					TEMPLATE: 'resources/jakduk/template/admin-data-view.html'
				},
				FC: {
					ID: 'admin.getFc',
					URL: '/fc',
					CONTROLLER: 'AdminGetController',
					TEMPLATE: 'resources/jakduk/template/admin-data-view.html'
				},
				BOARD_CATEGORY: {
					ID: 'admin.getBoardCategory',
					URL: '/boardCategory',
					CONTROLLER: 'AdminGetController',
					TEMPLATE: 'resources/jakduk/template/admin-data-view.html'
				},
				HOME_DESCRIPTION: {
					ID: 'admin.getHomeDescription',
					URL: '/homeDescription',
					CONTROLLER: 'AdminGetController',
					TEMPLATE: 'resources/jakduk/template/admin-data-view.html'
				},
				ATTENDANCE_LEAGUE: {
					ID: 'admin.getAttendanceLeague',
					URL: '/attendanceLeague',
					CONTROLLER: 'AdminGetController',
					TEMPLATE: 'resources/jakduk/template/admin-data-view.html'
				},
				ATTENDANCE_CLUB: {
					ID: 'admin.getAttendanceClub',
					URL: '/attendanceClub',
					CONTROLLER: 'AdminGetController',
					TEMPLATE: 'resources/jakduk/template/admin-data-view.html'
				},
				JAKDU_SCHEDULE: {
					ID: 'admin.getJakduSchedule',
					URL: '/jakduSchedule',
					CONTROLLER: 'AdminGetController',
					TEMPLATE: 'resources/jakduk/template/admin-data-view.html'
				},
				JAKDU_SCHEDULE_GROUP: {
					ID: 'admin.getJakduScheduleGroup',
					URL: '/jakduScheduleGroup',
					CONTROLLER: 'AdminGetController',
					TEMPLATE: 'resources/jakduk/template/admin-data-view.html'
				},
				COMPETITION: {
					ID: 'admin.getCompetition',
					URL: '/competition',
					CONTROLLER: 'AdminGetController',
					TEMPLATE: 'resources/jakduk/template/admin-data-view.html'
				}
			}
		})
		.config(['$locationProvider', function($locationProvider) {
			$locationProvider.html5Mode(true);
		}])
		.config(['$urlMatcherFactoryProvider', '$urlRouterProvider', '$stateProvider', 'MENU_ID_MAP', function($urlMatcherFactoryProvider, $urlRouterProvider, $stateProvider, MENU_ID_MAP) {
			// https://github.com/angular-ui/ui-router/wiki/Frequently-Asked-Questions#how-to-make-a-trailing-slash-optional-for-all-routes
			$urlMatcherFactoryProvider.strictMode(false);
			$urlRouterProvider.rule(function ($injector, $location) {
				var path = $location.url();

				// check to see if the path already has a slash where it should be
				if (path[path.length - 1] === '/' || path.indexOf('/?') > -1) {
					return;
				}

				if (path.indexOf('?') > -1) {
					return path.replace('?', '/?');
				}

				return path + '/';
			});

			$stateProvider
				.state(MENU_ID_MAP.HOME.ID, {
					url: MENU_ID_MAP.HOME.URL,
					templateUrl: MENU_ID_MAP.HOME.TEMPLATE,
					controller: MENU_ID_MAP.HOME.CONTROLLER,
					controllerAs: 'ctrl',
					data: {
						category: ''
					}
				});

			angular.forEach(MENU_ID_MAP.INIT, function(value) {
				$stateProvider.state(value.ID, {
					url: value.URL,
					templateUrl: value.TEMPLATE,
					controller: value.CONTROLLER,
					controllerAs: 'ctrl',
					data: {
						category: 'init'
					}
				});
			});

			$stateProvider.state(MENU_ID_MAP.WRITE.ENCYCLOPEDIA.ID, {
				url: MENU_ID_MAP.WRITE.ENCYCLOPEDIA.URL,
				templateUrl: MENU_ID_MAP.WRITE.ENCYCLOPEDIA.TEMPLATE,
				controller: MENU_ID_MAP.WRITE.ENCYCLOPEDIA.CONTROLLER,
				controllerAs: 'ctrl',
				data: {
					category: 'write'
				}
			});

			// angular.forEach(MENU_ID_MAP.WRITE, function(value) {
			// 	$stateProvider.state(value.ID, {
			// 		url: value.URL,
			// 		templateUrl: value.TEMPLATE,
			// 		controller: value.CONTROLLER,
			// 		controllerAs: 'ctrl',
			// 		data: {
			// 			category: 'write'
			// 		}
			// 	});
			// });

			angular.forEach(MENU_ID_MAP.GET, function(value) {
				$stateProvider.state(value.ID, {
					url: value.URL,
					templateUrl: value.TEMPLATE,
					controller: value.CONTROLLER,
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

				switch (type) {
					case 'encyclopedia':
						bUrl = '/api/admin/encyclopedias';
						break;
					case 'fcOrigin':
						bUrl = '/admin/footballclub/origin.json';
						break;
					case 'fc':
						bUrl = '/admin/footballclub.json';
						break;
					case 'boardCategory':
						bUrl = '/admin/board/category.json';
						break;
					case 'attendanceLeague':
						bUrl = '/admin/data/attendance/league.json';
						break;
					case 'attendanceClub':
						bUrl = '/admin/data/attendance/club.json';
						break;
					case 'homeDescription':
						bUrl = '/api/admin/home/descriptions';
						break;
					case 'jakduSchedule':
						bUrl = '/admin/data/jakdu/schedule.json';
						break;
					case 'jakduScheduleGroup':
						bUrl = '/admin/data/jakdu/schedule/group.json';
						break;
					case 'competition':
						bUrl = '/admin/data/competition.json';
						break;
				}

				if (bUrl && self.dataConn === "none") {

					self.dataConn = "loading";

					$http.get(BASE_URL + bUrl).then(function(response) {
						clearData();

						var data = response.data;
						var name;

						switch (type) {
							case 'encyclopedia':
								name = 'encyclopedias';
								break;
							case 'fcOrigin':
								name = 'fcOrigins';
								break;
							case 'fc':
								name = 'fcs';
								break;
							case 'boardCategory':
								name = 'boardCategorys';
								break;
							case 'attendanceLeague':
								name = 'attendanceLeagues';
								break;
							case 'attendanceClub':
								name = 'attendanceClubs';
								break;
							case 'homeDescription':
								name = 'homeDescriptions';
								break;
							case 'jakduSchedule':
								name = 'jakduSchedules';
								break;
							case 'jakduScheduleGroup':
								name = 'jakduScheduleGroups';
								break;
							case 'competition':
								name = 'competitions';
								break;
						}

						self[name] = data[name] || data;
						self.dataConn = "none";
						self.message = (!self[name] || !self[name].length) ? '데이터 없음' : '';
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
		.controller('AdminWriteEncyclopediaController', ['$http', '$state', '$location', 'BASE_URL', function($http, $state, $location, BASE_URL) {
			var self = this;

			self.kind = 'player';

			if ($state.params.id) {
				$http.get(BASE_URL + '/api/admin/encyclopedia/' + $state.params.id).then(function(response) {
					var encyclopedia = response.data.encyclopedia;
					if (encyclopedia) {
						self.encyclopedia = encyclopedia;
						self.kind = encyclopedia.kind;
						self.subject = encyclopedia.subject;
						self.content = encyclopedia.content;
						self.language = encyclopedia.language;
					}
				});
			}

			self.submit = function() {
				var promise;
				var data = {};

				if (self.subject) {
					data.subject = self.subject;
				}

				if (self.kind) {
					data.kind = self.kind;
				}

				if (self.content) {
					data.content= self.content;
				}

				if (self.language) {
					data.language = self.language;
				}

				self.errorMessage = '';

				if (self.encyclopedia) {
					data.id = self.encyclopedia.id;
					promise = $http.put(BASE_URL + '/api/admin/encyclopedia/' + self.encyclopedia.seq, data);
				} else {
					promise = $http.post(BASE_URL + '/api/admin/encyclopedia', data);
				}
				promise.then(function() {
					$location.url('/admin/encyclopedia');
				}, function() {
					self.errorMessage = 'SUBJECT, CONTENT 필수 입력';
				});
			};
		}])
		.controller('AdminWriteFootballClubOriginController', ['$http', '$location', 'BASE_URL', function($http, $location, BASE_URL) {

		}])
		.controller('AdminWriteFootballClubController', ['$http', '$location', 'BASE_URL', function($http, $location, BASE_URL) {

		}])
		.controller('AdminWriteBoardCategoryController', ['$http', '$location', 'BASE_URL', function($http, $location, BASE_URL) {

		}])
		.controller('AdminWriteThumbnailSizeController', ['$http', '$location', 'BASE_URL', function($http, $location, BASE_URL) {

		}])
		.controller('AdminWriteHomeDescriptionController', ['$http', '$location', 'BASE_URL', function($http, $location, BASE_URL) {

		}])
		.controller('AdminWriteAttendanceLeagueController', ['$http', '$location', 'BASE_URL', function($http, $location, BASE_URL) {

		}])
		.controller('AdminWriteAttendanceClubController', ['$http', '$location', 'BASE_URL', function($http, $location, BASE_URL) {

		}])
		.controller('AdminWriteJakduScheduleController', ['$http', '$location', 'BASE_URL', function($http, $location, BASE_URL) {

		}])
		.controller('AdminWriteJakduScheduleGroupController', ['$http', '$location', 'BASE_URL', function($http, $location, BASE_URL) {

		}])
		.controller('AdminWriteCompetitionController', ['$http', '$location', 'BASE_URL', function($http, $location, BASE_URL) {

		}]);

})();