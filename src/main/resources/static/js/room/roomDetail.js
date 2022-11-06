'use strict'

/*<![CDATA[*/

// 先にJavaScriptが読み込まれると何故か動かないから仕方なしに
setTimeout(function() {

	// 評価の数字　0 = なし, 1 = 高評価, 2 = 低評価
	let elem = /*[[${evaluation}]]*/
		console.log(elem);

	let good = document.getElementById("good");
	let bad = document.getElementById("bad");

	//高評価、低評価の場合で、所定の要素にクラスを追加する。 
	if (elem === 1) {
		good.classList.add("good");
	} else if (elem === 2) {
		bad.classList.add("bad");
	}
}, 100);

/*]]>*/