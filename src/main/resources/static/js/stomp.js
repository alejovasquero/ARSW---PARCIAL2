var stomp = (function () {

    var seats = [[true, true, true, true, true, true, true, true, true, true, true, true], [true, true, true, true, true, true, true, true, true, true, true, true], [true, true, true, true, true, true, true, true, true, true, true, true], [true, true, true, true, true, true, true, true, true, true, true, true], [true, true, true, true, true, true, true, true, true, true, true, true], [true, true, true, true, true, true, true, true, true, true, true, true], [true, true, true, true, true, true, true, true, true, true, true, true]];
    var c, ctx;

    class Seat {
        constructor(row, col) {
            this.row = row;
            this.col = col;
        }
    }
    var connected = false;
    var canvas = null;
    var stompClient = null;

    var xStart = 20;
    var yStart = 120;

    var ancho = 20;
    var alto = 20;
    
    var currentCinema = null;
    var currentDate = null;
    var currentMovie = null;


    var getColl = function (x) {
        var start = xStart;
        var ans = undefined;
        for (i = 0; i < seats[0].length; i++) {
            if (start <= x && x <= start + ancho) {
                ans = i;
                return ans;
            }
            start += ancho * 2;
        }
        return ans;
    }

    var getRow = function (y) {
        var start = yStart;
        var ans = undefined;
        for (i = 0; i < seats.length; i++) {
            if (start <= y && y <= start + alto) {
                ans = i;
                return ans;
            }
            start += alto * 2;
        }
        return ans;
    }

    //get the x, y positions of the mouse click relative to the canvas
    var getMousePosition = function (evt) {
        $('#myCanvas').click(function (e) {
            var rect = canvas.getBoundingClientRect();
            var x = e.clientX - rect.left;
            var y = e.clientY - rect.top;
            var colu = getColl(x);
            var roww = getRow(y);
            console.info(roww + " - " + colu)
            if (colu != undefined && roww != undefined) {
                stomp.buyTicket(roww, colu);
            }
        });


    };

    var drawSeats = function (cinemaFunction) {
        c = document.getElementById("myCanvas");
        ctx = c.getContext("2d");
        ctx.fillStyle = "#001933";
        ctx.fillRect(100, 20, 300, 80);
        ctx.fillStyle = "#FFFFFF";
        ctx.font = "40px Arial";
        ctx.fillText("Screen", 180, 70);
        var row = 5;
        var col = 0;
        for (var i = 0; i < seats.length; i++) {
            row++;
            col = 0;
            for (j = 0; j < seats[i].length; j++) {
                if (seats[i][j]) {
                    ctx.fillStyle = "#009900";
                } else {
                    ctx.fillStyle = "#FF0000";
                }
                col++;
                ctx.fillRect(20 * col, 20 * row, 20, 20);
                col++;
            }
            row++;
        }
    };

    var fillSeat = function (row, col) {
        console.log("FILLING")
        console.log(row);
        console.log(col);
        c = document.getElementById("myCanvas");
        ctx = c.getContext("2d");
        ctx.fillStyle = "#FF0000";
        ctx.fillRect(40 * col + 20, 40 * row + 120, 20, 20);
        seats[row][col] = false;
    }

    var connectAndSubscribe = function (cinema, date, movie) {
        console.info('Connecting to WS...');
        var socket = new SockJS('/stompendpoint');
        stompClient = Stomp.over(socket);
        connected = true;

        //subscribe to /topic/TOPICXX when connections succeed
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            console.log("AYYYYYYYYYYYYYYYYYYY 1");
            stompClient.subscribe('/topic/buyticket.' + cinema + "." + date + "." + movie, function (eventbody) {
                var theObject = JSON.parse(eventbody.body);
                console.log("AYYYYYYYYYYYYYYY")
                fillSeat(theObject.row, theObject.col);
                console.log(theObject);
            });
        });

    };

    var verifyAvailability = function (row, col) {
        var st = new Seat(row, col);
        if (seats[row][col] === true) {
            seats[row][col] = false;
            console.info("purchased ticket");
            stompClient.send('/topic/buyticket.' + currentMovie + "." + currentDate + "." + currentMovie, {}, JSON.stringify(st));
        } else {
            console.info("Ticket not available");
        }
    };



    return {

        init: function () {
            try {
                drawSeats();
            } catch (error) {

                //websocket connection
            }

        },

        buyTicket: function (row, col) {
            console.info("buying ticket at row: " + row + "col: " + col);
            verifyAvailability(row, col);

            //buy ticket
        },

        disconnect: function () {
            if (stompClient !== null && connected === true) {
                stompClient.disconnect();
                connected = false;
            }
            console.log("Disconnected");
        },

        setListener: function (callback) {
            canvas = document.getElementById('myCanvas');
            if (callback == undefined) {
                canvas.addEventListener('click', getMousePosition(), false);
            } else {
                canvas.addEventListener('click', callback, false);
            }

        },

        connectAndSus: function () {
            var cinema = $("#cinema").val();
            var date = $("#date").val();
            var movie = $("#movie").val();
            console.log(cinema, date, movie);
            if (cinema.length != 0 && date.length != 0 && movie.length != 0) {
                this.disconnect();
                currentCinema = cinema;
                currentDate = date;
                currentMovie = movie;
                connectAndSubscribe(cinema, date, movie);
            } else {
                console.info("DATOS INVÁLIDOS");
            }

        }
    };

})();