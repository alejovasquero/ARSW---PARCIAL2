var app = (function () {
    var REPOSITORY = apiclient;

    var name = "";
    var fecha = "";
    var funciones = [];
    var cinema = [];

    var movieName = "";
    var currentDate = "";


    var connected = false;
    var currentRow = null;
    var currentCol = null;
    var stompClient = null;


    var seekerButton = "<td><button type='button' class='seeker'>"
        + "Open Seats</button></td>"
    var widht = 70;
    var height = 70;
    var space = 3;
    var callback = function (param) {
        console.log("2")
        if (param == undefined) {
            alert("Cinema no existe o datos invalidos");
            return;
        }
        cinema = param;
        searchByNameAndDate();
        checkTable();
        funciones.map(function (data) {
            var str = "<tr class='omg'>" +
                "<td name='movie'>" + data.movie + "</td>" +
                "<td name='genre'>" + data.genero + "</td>" +
                "<td name='fecha'>" + data.fecha + "</td>" +
                seekerButton +
                "</tr>";
            $('#mainTable').append(str)
        })
        //console.log(funciones);
    };

    var searchByName = function () {
        funciones = cinema.functions.map(function (data) {
            return {
                fecha: data.date,
                movie: data.movie.name,
                genero: data.movie.genre
            };
        })
    }

    var searchByNameAndDate = function () {
        funciones = cinema.map(function (data) {
            return {
                fecha: data.date,
                movie: data.movie.name,
                genero: data.movie.genre
            };
        })
    }

    var checkTable = function () {
        $('#mainTable').find('td[name="movie"]').each(function () {
            $(this).parents("tr").remove();
        });
    }

    var xStart = 3;
    var yStart = 3;
    var c, ctx;
    var seats;
    class Seat {
        constructor(row, col) {
            this.row = row;
            this.col = col;
        }
    }

    var getColl = function (x) {
        var start = xStart;
        var ans = undefined;
        for (i = 0; i < seats[0].length; i++) {
            if (start <= x && x <= start + widht) {
                ans = i;
                return ans;
            }
            start += widht;
        }
        return ans;
    }

    var getRow = function (y) {
        var start = yStart;
        var ans = undefined;
        for (i = 0; i < seats.length; i++) {
            if (start <= y && y <= start + height) {
                ans = i;
                return ans;
            }
            start += height;
        }
        return ans;
    }

    var drawFunction = function (seats) {
        var i = space, j = 0;
        console.log(widht);
        console.log(height);
        var totalH = seats.length * height;
        var totalW = seats[0].length * widht;
        var interlineado = (seats.length - 1) * space + space * 2;
        var intercolumnas = (seats[0].length - 1) * space + space * 2;
        var c = document.getElementById("myCanvas");
        var ctx = c.getContext("2d");
        console.log(totalW);
        console.log(totalH + interlineado);
        ctx.canvas.height = totalH + interlineado;
        ctx.canvas.width = totalW + intercolumnas;
        seats.forEach(function (row) {
            console.log(row);
            j = space;
            row.forEach(function (coll) {
                console.log(coll);
                // Create gradient
                var grd = ctx.createRadialGradient(55, 50, 5, 70, 30, 80);
                grd.addColorStop(0, coll === true ? "green" : "red");
                // Fill with gradient
                ctx.fillStyle = grd;
                ctx.fillRect(j, i, widht, height);
                j += widht + space;
            });
            i += height + space;
        });


        $("#divCheckbox").show();
        console.log("ok");
    }

    var fillSeat = function (row, col) {
        console.log("FILLING")
        console.log(row);
        console.log(col);
        c = document.getElementById("myCanvas");
        ctx = c.getContext("2d");
        ctx.fillStyle = "#EDA232";
        ctx.fillRect(73 * col + 8, 73 * row + 8, 60, 60);
    }

    var setFunction = function (functionsSource) {
        var movieSelected = functionsSource.filter(
            (p) => p.movie.name == movieName);
        if (movieSelected.length != 0) {
            console.log(movieSelected[0]['seats']);
            setListener();
            seats=movieSelected[0]['seats'];
            drawFunction(movieSelected[0]['seats']);
            connectAndSubscribe(name,currentDate,movieName);
            console.log(name,currentDate,movieName);
        }
    }

    var updateFunctionPut = function (nuevaFecha) {
        console.log("LOCURA")
        var jsonu = {
            "date": nuevaFecha
        }
        var put = $.ajax({
            url: "/cinemas/" + name + "/" + currentDate + "/" + movieName,
            type: 'PUT',
            data: JSON.stringify(jsonu),
            contentType: "application/json",
            success: function (data) {
                console.log(data)
                console.log("1")
            }
        });
        return put;
    }

    var createFunctionPost = function (newMovie, newDate, newGenre) {
        console.log("nueva")
        console.log(newMovie)
        console.log(newDate)
        console.log(newGenre)
        var jsonu = {
            "name": newMovie,
            "genre": newGenre
        }
        var post = $.ajax({
            url: "/cinemas/" + name + "/" + newDate,
            type: 'POST',
            data: JSON.stringify(jsonu),
            contentType: "application/json",
            success: function (data) {
                console.log(data)
                console.log("1")
            }
        });
        return post;
    }

    var getFunction = function () {
        app.setFuntionsByNameAndDate(name, fecha);
    }

    var deleteFunction = function () {
        var del = $.ajax({
            url: "/cinemas/" + name + "/" + currentDate + "/" + movieName,
            type: 'DELETE',
            success: function (data) {
                console.log(data)
                console.log("1")
            }
        });
        return del;
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
                //alert("Warn event");
                console.log("event topic");
                var theObject = JSON.parse(eventbody.body);
                console.log("AYYYYY");
                fillSeat(theObject.row, theObject.col);
                console.log(theObject);
            });
        });

    };

    //get the x, y positions of the mouse click relative to the canvas
    var getMousePosition = function (evt) {
        $('#myCanvas').click(function (e) {
            var rect = canvas.getBoundingClientRect();
            var x = e.clientX - rect.left;
            var y = e.clientY - rect.top;
            console.info("x: "+x+" y: "+y)
            var colu = getColl(x);
            var roww = getRow(y);
            console.info(roww + " - " + colu)
            if (colu != undefined && roww != undefined) {
                currentCol = colu;
                currentRow = roww;
                if (seats[currentRow][currentCol] === false) {
                    alert("Asiento no disponible");
                }else{
                    fillSeat(currentRow,currentCol);
                }
            }
        });


    };

    var setListener = function () {
        canvas = document.getElementById('myCanvas');
        canvas.addEventListener('click', getMousePosition(), false);
    };

    var verifyAvailability = function (row, col) {
        var st = new Seat(row, col);
        if (seats[row][col] === true) {
            seats[row][col] = false;
            console.info("purchased ticket");
            console.log(name,currentDate,movieName);
            stompClient.send('/app/buyticket.' + name + "." + currentDate + "." + movieName, {}, JSON.stringify(st));
            drawFunction(seats);
        } else {
            console.info("Ticket not available");
            alert("Ya se reservo");
        }
    };

    var disconnect = function () {
        if (stompClient !== null && connected === true) {
            stompClient.disconnect();
            connected = false;
        }
        console.log("Disconnected");
    };

    return {
        init: function () {
            try {
                disconnect();
            } catch (error) {
                //websocket connection
                this.connectAndSubscribe(name,currentDate,movieName);
            }

        },
        setNameCinema: function (newName) {
            name = newName;
        },
        setDateCinema: function (newFecha) {
            fecha = newFecha;
        },
        setFuntionsByNameAndDate: function (nombre, date) {
            app.setDateCinema(date);
            app.setNameCinema(nombre);
            REPOSITORY.getFunctionsByCinemaAndDate(nombre, date, callback);
        },
        setFuntionsByName: function (name) {
            REPOSITORY.getFunctionsByCinema(name, callback);
        },
        setFunctionByNameDateMovie: function (cinema, date, movie) {
            movieName = movie;
            currentDate = date;
            name = cinema;
            $('#function').text(movie);
            REPOSITORY.getFunctionsByCinemaAndDate(cinema, date, setFunction);
        },

        getCurrentCinema: function () {
            return name;
        },

        updateFunction: function (newDate) {
            console.log()
            $('#divCheckbox').hide();
            console.log(newDate);
            updateFunctionPut(newDate)
                .then(getFunction);
        },

        createFunction: function (newMovie, newDate, newGenre) {
            console.log()
            $('#divCheckbox').hide();
            createFunctionPost(newMovie, newDate, newGenre)
                .then(getFunction);
        },

        deleteCurrentFunction: function () {
            $('#divCheckbox').hide();
            deleteFunction().then(
                getFunction
            );
        },

        buyTicket: function () {
            console.info("buying ticket at row: " + currentRow + "col: " + currentCol);
            verifyAvailability(currentRow, currentCol);
        }
    }

})();