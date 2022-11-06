'use strict';

setTimeout(function() {

	// スクロールを最下部に固定
	let target = document.getElementById('scroll-inner');
	target.scrollIntoView(false);

/*	// コメント投稿
	$(() => {
		$('#sendBtn').on('click', sendMsg);
	});

	let sendMsg = (event) => {
		let jsonString = {
			'comment': $('input[name=comment]').val(),
			'roomId': $('input[name=roomId]').val()
		};
		$.ajax({
			type: 'POST',
			url: '/talk/createMessage',
			data: JSON.stringify(jsonString),
			contentType: 'application/json',
			datatype: 'json',
			scriptCharset: 'utf-8'
		})
			.then((result) => {

			}, () => {
			}
			);
	};*/
}, 1);