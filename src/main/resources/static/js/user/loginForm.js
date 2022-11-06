'use strict';

setTimeout(function() {

	const email = document.getElementById("loginEmail");
	const password = document.getElementById("loginPassword");
	const registerBtn = document.getElementById("loginBtn");
	registerBtn.disabled = true; // btn非活性

	// 各要素の要件を確認　→　ネーム、メール、パスワード、パスワード確認
	let checkFlagList = [false, false];
	const emailFlagNum = 0;
	const passFlagNum = 1;

	// email正規表現
	const emailPatarn = /.+@.+\..+/;
	const passPatarn = /^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9])/;

	// emailのチェック
	email.oninput = () => {

		const elem = email.value;

		if (emailPatarn.test(elem)) {
			checkFlagList[emailFlagNum] = true;
		} else {
			checkFlagList[emailFlagNum] = false;
		}

		allJudge();
	}

	// パスワードのチェック
	password.oninput = () => {

		const elem = password.value;
		const elemLen = elem.length;
		const min = 8; // 最大値
		const max = 30; // 最小値

		if (passPatarn.test(elem) && elemLen >= min && elemLen <= max) {
			checkFlagList[passFlagNum] = true;
		} else {
			checkFlagList[passFlagNum] = false;
		}
		
		allJudge();
	}


	// すべての項目がtrueであるか確認
	const allJudge = () => {

		if (checkFlagList[emailFlagNum] &&
			checkFlagList[passFlagNum]) {
			registerBtn.disabled = false;
		} else {
			registerBtn.disabled = true;
		}
	}

}, 1);