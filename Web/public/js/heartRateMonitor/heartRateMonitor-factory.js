define([
	'socketio-client'
],
function (io){
	var factory = function (socketFactory){
		return socketFactory({
	        prefix: '',
	        ioSocket: io.connect('https://localhost:3000') //your web server url
		});
	};

	return ['socketFactory', factory];

});