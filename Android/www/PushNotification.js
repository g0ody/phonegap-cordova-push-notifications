//
//  PushNotification.js
//
// Based on the Push Notifications Cordova Plugin by Olivier Louvignes on 06/05/12.
// Modified by Max Konev on 18/05/12.
//
// Pushwoosh Push Notifications Plugin for Cordova iOS
// www.pushwoosh.com
//
// MIT Licensed

(function(cordova) {

	function PushNotification() {}

	// Call this to register for push notifications and retreive a deviceToken
	PushNotification.prototype.registerDevice = function(config, success, fail) {
		cordova.exec(success, fail, "PushNotifications", "registerDevice", config ? [config] : []);
	};

	// Event spawned when a notification is received while the application is active
	PushNotification.prototype.notificationCallback = function(notification) {
		var ev = document.createEvent('HTMLEvents');
		ev.notification = notification;
		ev.initEvent('push-notification', true, true, arguments);
		document.dispatchEvent(ev);
	};

	cordova.addConstructor(function() {
		if(!window.plugins) window.plugins = {};
		window.plugins.pushNotification = new PushNotification();
	});

})(window.cordova || window.Cordova || window.PhoneGap);

function initPushwoosh()
{
	var pushNotification = window.plugins.pushNotification;
	// CHANGE projectid & appid
	pushNotification.registerDevice({ projectid: "GCM_PROJECT_ID", appid : "PUSHWOOSH_APP_ID" },
									function(status) {
										console.warn('registerDevice:%o', status);
										console.warn(JSON.stringify(['registerDevice', status]));
									},
									function(status) {
									    console.warn('failed to register :%o', status);
									    console.warn(JSON.stringify(['failed to register ', status]));
									});

	document.addEventListener('push-notification', function(event) {
	                            var title = event.notification.title;
	                            var userData = event.notification.userdata;

								console.warn('user data: ' + JSON.stringify(userData));
								navigator.notification.alert(title);
							  });

 }