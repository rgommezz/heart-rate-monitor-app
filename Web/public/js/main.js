require.config({
	paths: {
		'angular': '../bower_components/angular/angular',
		'angularSocket': '../bower_components/angular-socket-io/socket',
		'ngAria': '../bower_components/angular-aria/angular-aria',
		'ngAnimate': '../bower_components/angular-animate/angular-animate',
		'ngMaterial': '../bower_components/angular-material/angular-material',
		'ngNvd3': '../bower_components/angular-nvd3/dist/angular-nvd3',
		'd3': '../bower_components/d3/d3',
		'nvd3': '../bower_components/nvd3/nv.d3',
		'socketio-client': '../node_modules/socket.io/node_modules/socket.io-client/socket.io',
		'app': 'app'
	},

	shim: {
		'angular': {
			exports: 'angular' // export angular as a global variable that can be used within other libraries
		},
		'angularSocket': {
			deps: ['angular']
		},
		'ngAria': {
			deps: ['angular']
		},
		'ngAnimate': {
			deps: ['angular']
		},
		'ngMaterial': {
			deps: ['angular', 'ngAria', 'ngAnimate']
		},
		'nvd3': {
			deps: ['d3']
		}, 
		'ngNvd3': {
			deps: ['angular', 'nvd3']
		}
	}
});

require(['angular','app'], function (angular, app){
	angular.bootstrap(document.getElementsByTagName('body')[0], [app.name]);
});