var app = (function () {

    var info = null;

    var paint = function () {
        console.log(info.coord);
        $("#data").empty();

        var content = "";
        content += "<div class='row'>";
        content += "<div class='col-sm-4'>"
            + "<i>Longitud " + info.coord.lon + "</i>"
            + "</div>"
            + "<div class='col-sm-4'>"
            + "<i>Latitud " + info.coord.lat + "</i>"
            + "</div>"
            + "<div class='col-sm-4'>"
            + "<i>Velocidad viento " + info.wind.speed + "</i>"
            + "</div>"
        content += "</div>";


        content += "<div class='row'>";
        content += "<div class='col-sm-4'>"
            + "<i>Descripción " + info.weather[0].description + "</i>"
            + "</div>"
            + "<div class='col-sm-4'>"
            + "<i>Temperatura " + info.main.temp + "</i>"
            + "</div>"
            + "<div class='col-sm-4'>"
            + "<i>Presión" + info.main.pressure + "</i>"
            + "</div>"
        content += "</div>";


        content += "<div class='row'>";
        content += "<div class='col-sm-4'>"
            + "<i>Humedad " + info.main.humidity + "</i>"
            + "</div>"
            + "<div class='col-sm-4'>"
            + "<i>Tempreratura máxima " + info.main.temp_min + "</i>"
            + "</div>";
        content += "</div>";
        $("#data").append(content).hide().show(200);
    }

    var getdata = function (city) {
        var req = $.ajax({
            url: '/weather',
            type: "GET",
            data: "city=" + city,
            contentType: 'application/x-www-form-urlencoded; charset=utf-8',
            success: function (data, status, xhr) {
                console.log(xhr.status);
                info = JSON.parse(data);
                console.log(info);
                paint();
            },
            error: function (jqXhr, textStatus, errorMessage) {
                console.log('Error' + errorMessage);
            }
        });
        return req;
    }


    return {
        seek: function (callback) {
            var name = $("#city").val();
            console.log(name);
            if (name !== "") {
                return getdata(name);
            }

        },

        getInfo: function () {
            return info;
        }

    }

})();