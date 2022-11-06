'use strict'

setTimeout(function() {

	const userName = document.getElementById("userName");
	const email = document.getElementById("email");
	const password = document.getElementById("password");
	const passwordConfirm = document.getElementById("passwordConfirm");
	const registerBtn = document.getElementById("registerBtn");
	registerBtn.disabled = true; // btn非活性

	// 各要素の要件を確認　→　ネーム、メール、パスワード、パスワード確認
	let checkFlagList = [false, false, false, false];
	const nameFlagNum = 0;
	const emailFlagNum = 1;
	const passFlagNum = 2;
	const passConfFlagNum = 3

	// email正規表現
	const emailPatarn = /.+@.+\..+/;
	const passPatarn = /^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9])/;

	// 名前のチェック
	userName.oninput = () => {

		const elem = userName.value;
		const elemLen = elem.length;
		const min = 1; // 最大値
		const max = 20; // 最小値

		if (elemLen >= min && elemLen <= max) {
			checkFlagList[nameFlagNum] = true;
		} else {
			checkFlagList[nameFlagNum] = false;
		}

		allJudge();
	}

	// メールのチェック
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

		passwordConfirmCheck();
		allJudge();
	}

	// パスワード再確認のチェック
	passwordConfirm.oninput = () => {

		passwordConfirmCheck();
		allJudge();
	}

	// パスワードとパスワード再確認が一致しているか
	const passwordConfirmCheck = () => {

		if (password.value === passwordConfirm.value) {
			checkFlagList[passConfFlagNum] = true;
		} else {
			checkFlagList[passConfFlagNum] = false;
		}
	}


	// すべての項目がtrueであるか確認
	const allJudge = () => {

		if (checkFlagList[nameFlagNum] && checkFlagList[emailFlagNum] &&
			checkFlagList[passFlagNum] && checkFlagList[passConfFlagNum]) {
			registerBtn.disabled = false;
		} else {
			registerBtn.disabled = true;
		}
	}

}, 1);