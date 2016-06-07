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
					TEMPLATE: 'resources/jakduk/template/admin-write-fc-origin.html',
					CONTROLLER: 'AdminWriteFootballClubOriginController'
				},
				FC: {
					ID: 'admin.writefc',
					URL: '/fc/write/:id',
					TEMPLATE: 'resources/jakduk/template/admin-write-fc.html',
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
					URL: '/attendanceLeague?competitionCode',
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

			angular.forEach(MENU_ID_MAP.WRITE, function(value) {
				$stateProvider.state(value.ID, {
					url: value.URL,
					templateUrl: value.TEMPLATE,
					controller: value.CONTROLLER,
					controllerAs: 'ctrl',
					data: {
						category: 'write'
					}
				});
			});

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
		.controller("AdminController", ['$scope', '$state', '$transitions', 'MENU_ID_MAP', function($scope, $state, $transitions, MENU_ID_MAP) {
			var self = this;

			pickMenuInfo($state);

			$scope.MENU_ID_MAP = MENU_ID_MAP;
			$transitions.onSuccess({}, pickMenuInfo.bind(self, $state));

			function pickMenuInfo($state) {
				var state = $state.current;
				self.isOpened = {};
				self.isOpened[state.data.category] = true;
				self.currentMenu = state.name;
			}

		}])
		.controller("AdminGetController", ['$http', '$state', '$location', 'MENU_ID_MAP', 'BASE_URL', function($http, $state, $location, MENU_ID_MAP, BASE_URL) {
			var self = this;

			self.getData = getData;
			self.clearData = clearData;

			self.dataConn = "none";
			self.dataLeagueConn = "none";
			self.encyclopedias = [];
			self.fcOrigins = [];
			self.fcs = [];
			self.boardCategories = [];
			self.leagueAttendances = [];
			self.attendanceClubs = [];
			self.homeDescriptions = [];
			self.jakduSchedules = [];
			self.jakduScheduleGroups = [];
			self.competitions = [];

			self.message = '불러오는 중...';
			getData($state.current.name);

			function getData(id) {
				var apiUrl, apiParams = '';

				switch (id) {
					case MENU_ID_MAP.GET.ENCYCLOPEDIA.ID:
						apiUrl = '/api/admin/encyclopedias';
						break;
					case MENU_ID_MAP.GET.FC_ORIGIN.ID:
						apiUrl = '/api/admin/origin/football/clubs';
						break;
					case MENU_ID_MAP.GET.FC.ID:
						apiUrl = '/api/admin/football/clubs';
						break;
					case MENU_ID_MAP.GET.BOARD_CATEGORY.ID:
						apiUrl = '/api/admin/board/categories';
						break;
					case MENU_ID_MAP.GET.ATTENDANCE_LEAGUE.ID:
						self.activeLeague = ($state.params.competitionCode || 'KL');
						self.activeLeagueName = getActiveLeage(self.activeLeague);
						apiUrl = '/api/admin/league/attendances';
						apiParams = '?competitionCode=' + self.activeLeague;
						break;
					case MENU_ID_MAP.GET.ATTENDANCE_CLUB.ID:
						apiUrl = '/admin/data/attendance/club.json';
						break;
					case MENU_ID_MAP.GET.HOME_DESCRIPTION.ID:
						apiUrl = '/api/admin/home/descriptions';
						break;
					case MENU_ID_MAP.GET.JAKDU_SCHEDULE.ID:
						apiUrl = '/admin/data/jakdu/schedule.json';
						break;
					case MENU_ID_MAP.GET.JAKDU_SCHEDULE_GROUP.ID:
						apiUrl = '/admin/data/jakdu/schedule/group.json';
						break;
					case MENU_ID_MAP.GET.COMPETITION.ID:
						apiUrl = '/admin/data/competition.json';
						break;
				}

				if (apiUrl && self.dataConn === "none") {

					self.dataConn = "loading";

					$http.get(BASE_URL + apiUrl + apiParams).then(function(response) {
						clearData();

						var data = response.data;
						var name;

						switch (id) {
							case MENU_ID_MAP.GET.ENCYCLOPEDIA.ID:
								name = 'encyclopedias';
								break;
							case MENU_ID_MAP.GET.FC_ORIGIN.ID:
								name = 'originFCs';
								break;
							case MENU_ID_MAP.GET.FC.ID:
								name = 'fcs';
								break;
							case MENU_ID_MAP.GET.BOARD_CATEGORY.ID:
								name = 'boardCategories';
								break;
							case MENU_ID_MAP.GET.ATTENDANCE_LEAGUE.ID:
								name = 'leagueAttendances';
								break;
							case MENU_ID_MAP.GET.ATTENDANCE_CLUB.ID:
								name = 'attendanceClubs';
								break;
							case MENU_ID_MAP.GET.HOME_DESCRIPTION.ID:
								name = 'homeDescriptions';
								break;
							case MENU_ID_MAP.GET.JAKDU_SCHEDULE.ID:
								name = 'jakduSchedules';
								break;
							case MENU_ID_MAP.GET.JAKDU_SCHEDULE_GROUP.ID:
								name = 'jakduScheduleGroups';
								break;
							case MENU_ID_MAP.GET.COMPETITION.ID:
								name = 'competitions';
								break;
						}

						self[name] = data[name] || data;
						self.dataConn = "none";
						self.message = (!self[name] || !self[name].length) ? '데이터 없음' : '';
					}, function() {
						self.dataConn = "none";
						self.message = '오류 발생';
					});
				}
			}

			function clearData() {
				self.encyclopedias = [];
				self.fcOrigins = [];
				self.fcs = [];
				self.boardCategories = [];
				self.leagueAttendances = [];
				self.attendanceClubs = [];
				self.homeDescriptions = [];
				self.jakduSchedules = [];
				self.jakduScheduleGroups = [];
				self.competitions = [];
			}

			function getActiveLeage(id) {
				switch (id) {
					case 'KLCL':
						return 'K League Classic';
					case 'KLCH':
						return 'K League Challenge';
					default:
						return 'K League';
				}
			}

		}])
		.controller('AdminBoardCategoryInitController', ['$http', 'BASE_URL', function($http, BASE_URL) {
			var self = this;
			self.message = '처리중...';
			$http.post(BASE_URL + '/api/admin/board/category/init')
				.then(function (response) {
					self.message = response.data.result ? '기본 카테고리 생성완료' : '이미 생성되어 있음';
				}, function() {
					self.message = '오류 발생';
				});
		}])
		.controller('AdminSearchIndexInitController', ['$http', 'BASE_URL', function($http, BASE_URL) {
			var self = this;
			self.message = '처리중...';
			$http.post(BASE_URL + '/api/admin/search/index/init')
				.then(function () {
					self.message = '검색 색인 완료';
				}, function() {
					self.message = '오류 발생';
				});
		}])
		.controller('AdminSearchTypeInitController', ['$http', 'BASE_URL', function($http, BASE_URL) {
			var self = this;
			self.message = '처리중...';
			$http.post(BASE_URL + '/api/admin/search/type/init')
				.then(function () {
					self.message = '검색 색인 완료';
				}, function() {
					self.message = '오류 발생';
				});
		}])
		.controller('AdminSearchDataInitController', ['$http', 'BASE_URL', function($http, BASE_URL) {
			var self = this;
			self.message = '처리중...';
			$http.post(BASE_URL + '/api/admin/search/data/init')
				.then(function () {
					self.message = '검색 색인 완료';
				}, function() {
					self.message = '오류 발생';
				});
		}])
		.controller('AdminWriteEncyclopediaController', ['$scope', '$http', '$state', '$location', 'BASE_URL', function($scope, $http, $state, $location, BASE_URL) {
			var self = this;

			self.submit = submit;

			self.language = 'ko';
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

			function submit() {
				self.errorMessage = '';
				if ($scope.encyclopediaForm.$invalid) {
					self.errorMessage = 'SUBJECT, CONTENT 필수 입력입니다';
					return;
				}

				var data = {
					subject: self.subject,
					kind: self.kind,
					content: self.content,
					language: self.language
				};
				var promise;

				if (self.encyclopedia) {
					promise = $http.put(BASE_URL + '/api/admin/encyclopedia/' + self.encyclopedia.id, data);
				} else {
					promise = $http.post(BASE_URL + '/api/admin/encyclopedia', data);
				}

				promise.then(function() {
					$location.url('/admin/encyclopedia');
				}, function() {
					self.errorMessage = 'SUBJECT, CONTENT 필수 입력입니다';
				});
			}
		}])
		.controller('AdminWriteFootballClubOriginController', ['$scope', '$http', '$state', '$location', 'BASE_URL', function($scope, $http, $state, $location, BASE_URL) {
			var self = this;

			self.submit = submit;

			if ($state.params.id) {
				$http.get(BASE_URL + '/api/admin/origin/football/club/' + $state.params.id).then(function(response) {
					var originFC = response.data.originFC;
					if (originFC) {
						self.originFC = originFC;
						self.codeName = originFC.name;
						self.clubType = originFC.clubType;
						self.age = originFC.age;
						self.sex = originFC.sex;
					}
				});
			} else {
				self.clubType = 'FOOTBALL_CLUB';
				self.age = 'UNDER_14';
				self.sex = 'MEN';
			}

			function submit() {
				self.errorMessage = '';
				if ($scope.footballClubOriginForm.$invalid) {
					self.errorMessage = 'CODE NAME 필수 입력입니다';
					return;
				}

				var data = {
					name: self.codeName,
					clubType: self.clubType,
					age: self.age,
					sex: self.sex
				};
				var promise;

				if ($state.params.id) {
					promise = $http.put(BASE_URL + '/api/admin/origin/football/club/' + $state.params.id, data);
				} else {
					promise = $http.post(BASE_URL + '/api/admin/origin/football/club', data);
				}

				promise.then(function() {
					$location.url('/admin/fcOrigin');
				});
			}
		}])
		.controller('AdminWriteFootballClubController', ['$scope', '$http', '$state', '$location', 'BASE_URL', function($scope, $http, $state, $location, BASE_URL) {
			var self = this;

			self.submit = submit;

			if ($state.params.id) {
				$http.get(BASE_URL + '/api/admin/football/club/' + $state.params.id).then(function(response) {
					var fc = response.data.fcRequest;
					if (fc) {
						self.active = fc.active;
						self.shortNameKr = fc.shortNameKr;
						self.fullNameKr = fc.fullNameKr;
						self.shortNameEn = fc.shortNameEn;
						self.fullNameEn = fc.fullNameEn;
						self.origins = response.data.originFCs;
						self.origin = self.origins.filter(function(each) {
							return each.id === fc.origin;
						})[0];
					}
				});
			} else {
				self.active = 'active';
				$http.get(BASE_URL + '/api/admin/origin/football/clubs').then(function(response) {
					self.origins = response.data.originFCs;
					self.origin = self.origins[0];
				});
			}

			function submit() {
				self.errorMessage = '';
				if ($scope.footballClubForm.$invalid) {
					self.errorMessage = '빠짐없이 입력해 주세요';
					return;
				}

				var data = {
					origin: self.origin.id,
					active: self.active,
					shortNameKr: self.shortNameKr,
					fullNameKr: self.fullNameKr,
					shortNameEn: self.shortNameEn,
					fullNameEn: self.fullNameEn
				};
				var promise;

				if ($state.params.id) {
					promise = $http.put(BASE_URL + '/api/admin/football/club/' + $state.params.id, data);
				} else {
					promise = $http.post(BASE_URL + '/api/admin/football/club', data);
				}

				promise.then(function() {
					$location.url('/admin/fc');
				}, function() {
					self.errorMessage = '빠짐없이 입력해 주세요';
				});
			}
		}])
		.controller('AdminWriteBoardCategoryController', ['$scope', '$http', '$state', '$location', 'BASE_URL', function($scope, $http, $state, $location, BASE_URL) {
			var self = this;

			self.submit = submit;

			if ($state.params.id) {
				$http.get(BASE_URL + '/api/admin/board/category/' + $state.params.id).then(function(response) {
					var boardCategory = response.data.boardCategory;
					if (boardCategory) {
						self.boardCategory = boardCategory;
						self.name = boardCategory.name;
						self.resName = boardCategory.resName;
						self.usingBoard = boardCategory.usingBoard.map(function(each) {
							return {
								name: each,
								value: true
							}
						});
					}
				});
			} else {
				self.usingBoard = [{name: 'freeBoard', value: true}];
			}

			function submit() {
				self.errorMessage = '';
				if ($scope.boardCategoryWrite.$invalid) {
					self.errorMessage = '빠짐없이 입력해 주세요.';
					return;
				}

				var data = {
					name: self.name,
					resName: self.resName,
					usingBoard: self.usingBoard.filter(function(each) {return each.value;}).map(function(each) {return each.name;})
				};
				var promise;

				if ($state.params.id) {
					promise = $http.put(BASE_URL + '/api/admin/board/category/write/' + $state.params.id, data)
				} else {
					promise = $http.post(BASE_URL + '/api/admin/board/category/write', data)
				}

				promise.then(function() {
					$location.url('/admin/boardCategory');
				});
			}

		}])
		.controller('AdminWriteThumbnailSizeController', ['$http', '$location', 'BASE_URL', function($http, $location, BASE_URL) {

		}])
		.controller('AdminWriteHomeDescriptionController', ['$scope', '$http', '$state', '$location', 'BASE_URL', function($scope, $http, $state, $location, BASE_URL) {
			var self = this;

			self.submit = submit;

			if ($state.params.id) {
				$http.get(BASE_URL + '/api/admin/home/description/' + $state.params.id).then(function(response) {
					var homeDescription = response.data.homeDescription;
					if (homeDescription) {
						self.homeDescription = homeDescription;
						self.priority = homeDescription.priority;
						self.desc = homeDescription.desc;
					}
				});
			}

			function submit() {
				self.errorMessage = '';
				if ($scope.homeDescriptionForm.$invalid) {
					self.errorMessage = '빠짐없이 입력해 주세요.';
					return;
				}

				var data = {
					priority: self.priority,
					desc: self.desc
				};
				var promise;

				if ($state.params.id) {
					promise = $http.put(BASE_URL + '/api/admin/home/description/' + $state.params.id, data);
				} else {
					promise = $http.post(BASE_URL + '/api/admin/home/description', data);
				}

				promise.then(function() {
					$location.url('/admin/homeDescription');
				}, function() {
					self.errorMessage = '빠짐없이 입력해 주세요.';
				});
			}
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