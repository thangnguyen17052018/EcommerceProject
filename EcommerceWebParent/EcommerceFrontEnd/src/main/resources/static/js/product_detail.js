$(document).ready(function (){

	$(".product-descript-btntoggle").click(function(){
		
  		$(".product-fulldescpt").slideToggle();


  		if($(".descript-angle-down").css("display")=="block"){
			$(".descript-angle-down").hide();
			$(".descript-angle-up").show();
		}
		else {
			$(".descript-angle-up").hide();
			$(".descript-angle-down").show();
		}


	});
	
	
	$(".product-detail-btntoggle").click(function(){
		
  		$(".product-detail").slideToggle();


  		if($(".detail-angle-down").css("display")=="block"){
			$(".detail-angle-down").hide();
			$(".detail-angle-up").show();
		}
		else {
			$(".detail-angle-up").hide();
			$(".detail-angle-down").show();
		}


	});
	
})