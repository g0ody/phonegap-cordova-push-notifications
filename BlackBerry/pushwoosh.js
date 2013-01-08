var PushWoosh = {
	getToken : function() {
		return blackberry.identity.PIN;
	},
	
	register : function(lambda, lambdaerror) {
		var method = 'POST';
		var token = PushWoosh.getToken();
		var url = PushWoosh.baseurl + 'registerDevice';
		
		var language = window.navigator.language;
		var lang = 'en';
        if(language) {
             lang = language.substring(0,2); 
        }

		var params = {
				request : {
					language : lang,
					application : 'YOUR_PUSHWOOSH_APP_ID',
					push_token : token,
					device_type : 2,
					hwid : blackberry.identity.IMEI
					}
			};

		payload = (params) ? JSON.stringify(params) : '';
		PushWoosh.helper(url, method, payload, function(data, status) {
			var status_code = data['status_code'];
			var message = data['status_message'];
			
			if(status_code == 200 || status_code == 103) {
				//alert('success registration: ' + message);
				lambda({
					action : "subscribed",
					success : true
				});
			} else {
				lambdaerror(message);
				//alert('error registration: ' + message);
			}
		}, function(xhr, error) {
			//alert('xhr error registration: ' + JSON.stringify(error));
			lambdaerror(jqXHR, err);
		});
	},
	
	unregister : function(lambda) {
		var method = 'POST';
		var url = PushWoosh.baseurl + 'unregisterDevice';
		
		var params = {
				request : {
					application : 'YOUR_PUSHWOOSH_APP_ID',
					hwid : blackberry.identity.IMEI
				}
			};

		payload = (params) ? JSON.stringify(params) : '';
		PushWoosh.helper(url, method, payload, function(data, status) {
			var status_code = data['status_code'];
			var message = data['status_message'];
			
			if(status_code == 200) {
				lambda({
					status : status_code
				});
			} else {
				lambda({
					status : status_code
				});
			}
		}, function(xhr, error) {
			lambda({
				success : false,
				xhr : xhr.status,
				error : error
			});
		});
	},
	
	helper : function(url, method, params, lambda, lambdaerror) {
		$.ajax({
			type: "POST",
			async: true,
			url: url,
			dataType: "json",
			data: params,
			contentType: "application/json; charset=utf-8",
			success: function (msg, sts, jqXHR) {
				lambda(msg, sts);
			},
			error: function (jqXHR, sts, err) {
				lambdaerror(jqXHR, err);
			}
		});
	}
};

PushWoosh.baseurl = 'https://cp.pushwoosh.com/json/1.3/';
