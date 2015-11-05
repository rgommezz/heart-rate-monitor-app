var nodemailer  = require('nodemailer');

var timerBuffer = 60; // in function of samples received (approx 1 sample/sec)
var counter = 0;
var backToNormality = false;

// Mail service initialization
var smtpTransport = nodemailer.createTransport('SMTP',{
   service: 'Gmail', // email provider to be chosen
   auth: {
       user: "", //introduce the email account you want to use to send notifications
       pass: "" // password
   }
});

module.exports = function(heartRateValue, location){
	if (backToNormality) counter++;
	if (counter == timerBuffer) counter = 0;

	if ((heartRateValue >= 100) && (counter == 0)) {
		backToNormality = true;
		smtpTransport.sendMail({
			from: credentials.userMail, // sender address
			to: '', // comma separated list of receivers
			subject: 'Heart Rate Monitor Notification System', // Subject line
			text: 'Heart Rate bigger than 100! Current location of user: http://maps.google.com/?q=' + location.latitude + ',' + location.longitude // plaintext body
		}, function(error, response){
			if(error){
			   console.log(error);
			}else{
			   console.log("Message sent: " + response.message);
			}
		});
		counter++;
	}

	if (backToNormality && heartRateValue < 100 && counter == 0){
		smtpTransport.sendMail({
			from: credentials.userMail, // sender address
			to: 'rauliyohmc@gmail.com', // comma separated list of receivers
			subject: 'Heart Rate Monitor Notification System', // Subject line
			text: 'Heart Rate Stabilized :)' // plaintext body
		}, function(error, response){
			if(error){
			   console.log(error);
			}else{
			   console.log("Message sent: " + response.message);
			}
		});
		backToNormality = false;
		counter = 0;
	}

};