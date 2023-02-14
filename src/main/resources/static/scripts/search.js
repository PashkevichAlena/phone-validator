var modal = document.getElementById("response");

var span = document.getElementsByClassName("close")[0];

span.onclick = function () {
    modal.style.display = "none";
}

window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

document.getElementById('searchForm').addEventListener('submit', function (e) {

    e.preventDefault();

    var $form = $(this),
        term = $form.find("input[name='code']").val(),
        url = `http://localhost:8080/countries/by/${term}`;

    $.getJSON(url, function (data) {
        let result = '';

        $.each(data, function (key, val) {
            $.each(val, function (index, value) {
                if (index === 0) result = value
                else result = result + ", " + value
            })
        });

        $('#content').html(result)
        modal.style.display = "block";
    })
        .fail(function (data, textStatus, xhr) {
            $('#content').html(data.responseText)
            modal.style.display = "block";
        });

    return false;
});




