	var request = null;
	createRequest();
	
	function createRequest() {
		try {
			request = new XMLHttpRequest();
		} catch (trymicrosoft) {
			try {
				request = new ActiveXObject("Msxm12.XMLHTTP");
			} catch( othermicrosoft) {
				try {
					request = new ActiveXObject("Microsoft.XMLHTTP");
				} catch(failed) {
					request = null;
				}
			}
		}
		if ( request == null )
			alert("Error creating request object!");
	}
	
	
	function getDynamicXHR(){
		var dRequest = null;
		try {
			dRequest = new XMLHttpRequest();
		} catch (trymicrosoft) {
			try {
				dRequest = new ActiveXObject("Msxm12.XMLHTTP");
			} catch( othermicrosoft) {
				try {
					dRequest = new ActiveXObject("Microsoft.XMLHTTP");
				} catch(failed) {
					dRequest = null;
				}
			}
		}
		if ( dRequest == null )
			alert("Error creating request object!");
		else
			return dRequest;
	}
	
