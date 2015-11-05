define(['angular',
		'angularSocket',
		'ngMaterial',
		'ngNvd3',
		'heartRateMonitor/heartRateMonitor-factory',
		'heartRateMonitor/heartRateMonitor-controller',
		'heartRateMonitor/heartRateMonitor-service'
], function (angular, angularSocket, ngMaterial, ngNvd3, hrmFactory, hrmController, hrmService){

	var heartRateMonitorModule = angular.module('heartRateMonitor', ['btford.socket-io','ngMaterial','nvd3']);

	heartRateMonitorModule.factory('heartRateMonitor.socket', hrmFactory);

	heartRateMonitorModule.controller('heartRateMonitor.controller', hrmController);

	return heartRateMonitorModule;

});