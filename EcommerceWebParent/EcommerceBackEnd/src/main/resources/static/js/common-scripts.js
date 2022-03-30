$(document).ready(() => {
    $("#logoutLink").on("click", (e) => {
        e.preventDefault();
        document.logoutForm.submit();
    });

    accountProfile();
    
    // $(".dropdown").mouseenter(() => {
	// 	$(".dropdown-menu").slideDown(600);
	// })
	// $(".dropdown").mouseleave(() => {
	// 	$(".dropdown-menu").slideUp(600);
	// })

});





const accountProfile = () => {

    $(".dropdown > a").click((e) => {
        location.href = e.target.href;
    });
}
