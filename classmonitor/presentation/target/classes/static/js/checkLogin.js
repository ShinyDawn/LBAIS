try {
	$.session.get('login');
} catch (err) {
	window.location.href = "http://localhost:10001/login";
}