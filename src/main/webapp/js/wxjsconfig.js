$(function(){
	var param={};
	param.url=window.location.href;
	var url = getContextPath()+ "/wechat/getWechatSignature.json";
	var result=generalAjaxSyncPost(url,param);
	var nonceStr=result.nonceStr;
	var signature=result.signature;
	var timestamp=result.timestamp;
	var appId=result.appId;
	wx.config({
		debug:false,
		appId:appId,
		timestamp:timestamp,
		nonceStr:nonceStr,
		signature:signature,
		jsApiList: [
			'checkJsApi',
			'chooseImage',
			'previewImage',
			'uploadImage',
			'downloadImage',
			'getNetworkType',
			'openLocation',
			'getLocation',
			'hideOptionMenu',
			'showOptionMenu',
			'closeWindow',
			'scanQRCode',
			'chooseWXPay',
			'openProductSpecificView',
			'addCard',
			'chooseCard',
			'openCard'
		  ]
	  });
});