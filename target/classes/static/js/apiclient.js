apiclient = (function () {



    return {
        getFunctionsByCinema: function (cinema_name, callback) {
            $.getJSON('/cinemas/' + cinema_name, function (response) {
                callback(response);
            }).fail(function () {
                callback(undefined);
            })
        },
        getFunctionsByCinemaAndDate: function (cinema_name, fdate, callback) {
            if (fdate === "") {
                $.getJSON('/cinemas/' + cinema_name, function (response) {
                    callback(response['functions']);
                }).fail(function () {
                    callback(undefined);
                })
            } else {
                $.getJSON('/cinemas/' + cinema_name + '/' + fdate, function (response) {
                    console.log("HPT");
                    callback(response);
                }).fail(function () {
                    callback(undefined);
                })
            }

        },

        buyTicket: function (row, col) {
            //buy
        }
    }
})();