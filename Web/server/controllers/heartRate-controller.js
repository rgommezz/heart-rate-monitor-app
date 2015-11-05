var HeartRateModel 	= require('../model/heartRateModel'),
	notifications  	= require('./heartRate-notifications');

var buffer = [];
var bufferLength = 60; // number of heartRate samples per db document/entry
var startTime; // h:m format, first element for lookup
var formattedDate; // d/m/y format second element for lookup
var location = {
	latitude: '',
	longitude: ''
};

var saveToDatabase = {

	fixedBuffer: function (req, res){
		var currentDate = new Date();
		if (!buffer.length){
			startTime = getFormattedTime(currentDate, 0);
			formattedDate = getFormattedDate(currentDate);
			console.log('key created ' + startTime);
			console.log('key created ' + formattedDate);
		}
		buffer.push({
			value: req.body.heartRate,
			emitting: req.body.emitting
		});
		if (buffer.length === bufferLength){
			// we save the 60 samples in the db as new document/entry
			saveBuffer(formattedDate, startTime, buffer, res);
			buffer = [];
		} else if ((req.body.emitting === 'false') || (req.body.heartRate == 0)){
			//we save the data in another document before disconnecting
			startTime = getFormattedTime(currentDate, 0);
			formattedDate = getFormattedDate(currentDate);
			saveBuffer(formattedDate, startTime, buffer, res);
			buffer = [];
		} else {
			res.send(buffer);
		}
	},

	variableBuffer: function (req, res){
		var currentDate = new Date();
		buffer.push({
			value: req.body.heartRate,
			emitting: req.body.emitting
		});
		if (currentDate.getSeconds() === 0){
			startTime = getFormattedTime(currentDate, 1);
			formattedDate = getFormattedDate(currentDate);
			saveBuffer(formattedDate, startTime, buffer, res);
			buffer = [];
		}

	}
};

var getFormattedTime = function (currentDate, offset){
	// building the key lookup for the document, with string format h:m
	var hour = currentDate.getHours().toString();
	var minutes = currentDate.getMinutes();
	if (offset == 1){
		if (minutes == 0){
			minutes = 59;
		} else {
			minutes = minutes - 1;
		}
	}
	if (minutes < 10){
		minutes = '0' + minutes.toString();
	} else {
		minutes = minutes.toString();
	}
	var time = hour + ':' + minutes;
	
	return time;
};

var getFormattedDate = function (currentDate){
	var day = currentDate.getDate().toString();
	var month = (currentDate.getMonth() + 1).toString();
	var year = currentDate.getFullYear();
	var currentDate = day + '/' + month + '/' + year;
	return currentDate;
};

var saveBuffer = function (formattedDate, startTime, buffer, res){
	var heartRateModel = new HeartRateModel({
		day: formattedDate,
		time: startTime,
		values: buffer
	});
	heartRateModel.save(function(err){
		if(!err) console.log('Buffer stored');
		else console.log('ERROR: ' + err);
	});
	res.send(heartRateModel);
};

module.exports = {
	postHeartRateValue: function (req, res){
		// Sending value to web socket client
		var socketio = req.app.get('socketio'); // take out socket instance from the app container
		socketio.sockets.emit('heartRateValue', req.body); // emit an event for all connected clients
		saveToDatabase.fixedBuffer(req, res); // db method
		notifications(parseInt(req.body.heartRate), location); // email notification system for dangerous values
		console.log(req.body);
	},
	postLocation: function (req, res){
		var socketio = req.app.get('socketio'); // take out socket instance from the app container
		socketio.sockets.emit('location', req.body); // emit an event for all connected clients
		console.log(req.body);
		location.latitude = req.body.latitude;
		location.longitude = req.body.longitude;
		res.send('200 OK');
	}
};
