try {
	$.session.get('login');
} catch (err) {
	window.location.href = "http://218.94.159.98:10001/login";
}