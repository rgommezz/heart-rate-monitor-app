define([
	'angular',
	'heartRateMonitor/heartRateMonitor',
], function (angular){
	'use strict';

	var appName = 'app.heartRateMonitor',
		depends = [
					'heartRateMonitor'
				],
		app = angular.module(appName, depends);

	return app;
});