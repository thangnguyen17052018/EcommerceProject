$(document).ready(() => {
    $("#logoutLink").on("click", (e) => {
        e.preventDefault();
        document.logoutForm.submit();
    });

    accountProfile();

});

const accountProfile = () => {

    $(".dropdown > a").click((e) => {
        location.href = e.target.href;
    });
}
