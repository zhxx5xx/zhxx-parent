var TTCart = {
	load : function() { // 加载购物车数据

	},
	itemNumChange : function() {
		$(".increment").click(
				function() {// ＋
					var _thisInput = $(this).siblings("input");
					_thisInput.val(eval(_thisInput.val()) + 1);
					$.post("/cart/update/num/" + _thisInput.attr("itemId")
							+ "/" + _thisInput.val() + ".action",
							function(data) {
								TTCart.refreshTotalPrice();
							});
				});
		$(".decrement").click(
				function() {// -
					var _thisInput = $(this).siblings("input");
					if (eval(_thisInput.val()) == 1) {
						return;
					}
					_thisInput.val(eval(_thisInput.val()) - 1);
					$.post("/cart/update/num/" + _thisInput.attr("itemId")
							+ "/" + _thisInput.val() + ".action",
							function(data) {
								TTCart.refreshTotalPrice();
							});
				});
		$(".quantity-form .quantity-text").rnumber(1);// 限制只能输入数字
		$(".quantity-form .quantity-text").change(
				function() {
					var _thisInput = $(this);
					$.post("/service/cart/update/num/"
							+ _thisInput.attr("itemId") + "/"
							+ _thisInput.val(), function(data) {
						TTCart.refreshTotalPrice();
					});
				});
	},
	refreshTotalPrice : function() { // 重新计算总价
		var total = 0;
		$(".quantity-form .quantity-text")
				.each(
						function(i, e) {
							var _this = $(e);
							total += (eval(_this.attr("itemPrice")) * 10000 * eval(_this
									.val())) / 10000;
						});
		$(".totalSkuPrice").html(new Number(total / 100).toFixed(2))
				.priceFormat({ // 价格格式化插件
					prefix : '￥',
					thousandsSeparator : ',',
					centsLimit : 2
				});
	}
};

$(function() {
	TTCart.load();
	TTCart.itemNumChange();

	// 对删除超链接添加点击事情
	$(".mycart_remove").click(function() {
		var $a = $(this);
		var href = $(this).attr("href");
		$.post(href, function(data) {
			if (data.status == 200) {
				// parent()当前标签的父标签
				$a.parent().parent().parent().remove();
				TTCart.refreshTotalPrice();
			}
		})
		return false;
	})

	$(".checkbox")
			.click(
					function() {
						var total = 0;
						$(".quantity-form .quantity-text")
								.each(
										function(i, e) {
											var _this = $(e);
											if (_this.parent().parent()
													.siblings(".p-checkbox")
													.children().eq(0)[0].checked) {
												total += (eval(_this
														.attr("itemPrice")) * 10000 * eval(_this
														.val())) / 10000;
											}
										});
						$(".totalSkuPrice").html(
								new Number(total / 100).toFixed(2))
								.priceFormat({ // 价格格式化插件
									prefix : '￥',
									thousandsSeparator : ',',
									centsLimit : 2
								});
					})

	$("#toSettlement").click(function() {
		// i脚标 n当前循环时对象,对象是一个dom对象
		var param = "";
		$.each($(".checkbox:checked"), function(i, n) {
			param += "id=" + $(n).val();
			if (i < $(".checkbox:checked").length - 1) {
				param += "&";
			}
		})
		location.href = $(this).attr("href") + "?" + param;
		return false;
	});

});