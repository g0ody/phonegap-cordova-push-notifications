// Cordova iOS Pushwoosh Push Notifications plugin
// (c) Pushwoosh, 2012

0. Visit http://www.pushwoosh.com and create an account there. Create the application and configure it (or you can configure it later).

1. Drag and drop the "PushNotification" folder from Finder to your Plugins folder in XCode (use "Create groups for any added folders")

2. Add the PushNotification.js file to your "www" folder on disk

3. Add the reference to the .js file using `<script>` tags in your html file(s):
	<script type="text/javascript" charset="utf-8" src="PushNotification.js"></script>

4. Add new entry with key `PushNotification` and value `PushNotification` to `Plugins` in "Cordova.plist/Cordova.plist"

5. Add cp.pushwoosh.com to <strong>hosts</strong> in Cordova.plist/ExternalHosts (or you can simply add wildcard *).

6. See the index.html sample for the Javascript integration.

In your <b>onDeviceReady</b> function add:
initPushwoosh();

Registering for push notifications:
Replace PUSHWOOSH_APP_CODE with your Application Id from Pushwoosh and APP_NAME with your Application name in the initPushwoosh function.

NOTE: Handling push notifications:
You can find the push notifications handling code in the initPushwoosh function. See:
document.addEventListener('push-notification', function(event) {
						  var notification = event.notification;
						  navigator.notification.alert(notification.aps.alert);
						  pushNotification.setApplicationIconBadgeNumber(0);
						  });


7. See PushNotification.js for more information on the interface

8. Wasn't it TOO EASY?

P.S. You might want to ask - "Do I have to change AppDelegate.m file as for all other PN plugins?".
No! You have to do only the steps mentioned above. That's where the magic comes true.