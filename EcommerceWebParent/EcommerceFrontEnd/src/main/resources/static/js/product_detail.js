$(document).ready(function (){

	$(".product-descript-btntoggle").click(function(){
		
  		$(".product-fulldescpt").slideToggle();


  		if($(".angle-down").css("display")=="block"){
			$(".angle-down").hide();
			$(".angle-up").show();
		}
		else {
			$(".angle-up").hide();
			$(".angle-down").show();
		}


	});
	
	
	$(".product-detail-btntoggle").click(function(){
		
  		$(".product-detail").slideToggle();


  		if($(".angle-down").css("display")=="block"){
			$(".angle-down").hide();
			$(".angle-up").show();
		}
		else {
			$(".angle-up").hide();
			$(".angle-down").show();
		}


	});
	
})