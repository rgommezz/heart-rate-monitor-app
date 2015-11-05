define(function (){
	'use strict';

	var heartRateMonitorController = function ($scope, $rootScope, $window, socket){
		var dateObj		 		  = new Date(),
			month 		 		  = dateObj.getUTCMonth() + 1, //months from 1-12
			day 		 		  = dateObj.getUTCDate(),
			year 		 		  = dateObj.getUTCFullYear(),
			currentDay 	 		  = year + "/" + month + "/" + day,
			w 			 		  = angular.element($window);

		//initializing chart dimensions in function of viewport size
		var initialChartProperties = { // default for desktop
			width: $window.innerWidth - 8,
			height: $window.innerHeight - 200,
			margin: {
				top: 40,
				right: 100,
				bottom: 100,
				left: 100
			}
		};
		if ($window.innerHeight <= 480){
			initialChartProperties.height = $window.innerHeight - 70;
			initialChartProperties.margin.top = 15;
		} else if ($window.innerHeight <= 640){
			initialChartProperties.height = $window.innerHeight - 140;
			initialChartProperties.margin.top = 15;
		}
		if ($window.innerWidth < 640){
			initialChartProperties.margin.left = 50;
			initialChartProperties.margin.right = 50;
		}

		$scope.numSamples = 10;
		$scope.disconnected = true;
		var realTime;
		$scope.value = '';
		$scope.geographicCoordinates = '';
		$scope.currentDay = dateObj.toDateString();

		$scope.checkCurrentLocation = function() {
			$window.open('http://maps.google.com/?q=' + $scope.geographicCoordinates);
		};

		$scope.options = {
			title: {
				enable: false,
			},
		    chart: {
		        type: 'lineChart',
		        height: initialChartProperties.height,
		        width: initialChartProperties.width,
		        margin : {
		            top: initialChartProperties.margin.top,
		            right: initialChartProperties.margin.right,
		            bottom: initialChartProperties.margin.bottom,
		            left: initialChartProperties.margin.left
		        },
		        x: function(d){ return d.label; },
		        y: function(d){ return d.value; },
		        showValues: true,
		        valueFormat: function(d){
		            return d3.format(',.4f')(d);
		        },
		        useInteractiveGuideline: false,
		        showLegend: false,
		        transitionDuration: 500,
		        xAxis: {
		            axisLabel: 'Time',
		            tickFormat: (function(d) {return d3.time.format('%X')(new Date(d))})
		        },
		        xScale: d3.time.scale(),
		        yAxis: {
		            axisLabelDistance: 30
		        },
		        yDomain: [40,120]
		    }
		};
		$scope.config = {
		    visible: true,
		    extended: false,
		    disabled: false,
		    autorefresh: true,
		    refreshDataOnly: true
		};
		$scope.data = [
			{
			    key: 'HR',
			    values: [],
			    color: '#E91E63'
			},
			{
				key: 'Rango de reposo, límite superior',
				values: [],
				color: '#2ca02c'
			},
			{
				key: 'Rango de reposo, límite inferior',
				values: [],
				color: '#2ca02c'
			}
		];

		// Socket connection for heartRate value changes
		socket.on('heartRateValue', function(data){
			if (data.emitting == 'true'){
				$scope.disconnected = false;
				$scope.value = data.heartRate;
				realTime = new Date();
				$scope.data[0].values.push({'label': realTime, 'value': $scope.value});
				$scope.data[1].values.push({'label': realTime, 'value': '100'});
				$scope.data[2].values.push({'label': realTime, 'value': '50'});
				if ($scope.data[0].values.length > 10){
					$scope.data[0].values.shift();
					$scope.data[1].values.shift();
					$scope.data[2].values.shift();
				}
			} else {
				$scope.disconnected = true;
				$scope.value = '';
				$scope.geographicCoordinates = '';
				$scope.data[0].values = [];
				$scope.data[1].values = [];
				$scope.data[2].values = [];
				$scope.api.refresh();
			}
		});

		// Socket connection for location value changes
		socket.on('location', function(location){
			$scope.geographicCoordinates = location.latitude + ',' + location.longitude;
		});

		w.bind('resize', function () {
			if ($window.innerWidth < 640){
				$scope.numSamples = 5;
				$scope.options.chart.margin.left = 50;
				$scope.options.chart.margin.right = 50;
			} else {
				$scope.options.chart.margin.left = 100;
				$scope.options.chart.margin.right = 100;
			}
			if (this.innerHeight <= 480){
				$scope.numSamples = 2;
				$scope.options.chart.height = this.innerHeight - 70;
				$scope.options.chart.margin.top = 15;
			} else if (this.innerHeight <= 640){
				$scope.options.chart.height = this.innerHeight - 140;
				$scope.options.chart.margin.top = 15;
			} else {
				$scope.numSamples = 10;
				$scope.options.chart.height = this.innerHeight - 200;
				$scope.options.chart.margin.top = 40;
			}
			$scope.options.chart.width = $window.innerWidth - 8,
			$scope.$apply();
		});
	};

	return ['$scope', '$rootScope', '$window', 'heartRateMonitor.socket', heartRateMonitorController];

});