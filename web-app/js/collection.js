$( document ).ready(function() {
    $('#collection-results').html('');

    getCollection();
});

function getCollection(){
    var username = $('#currentLoggedInUser').text();
    var urlPath = "/rest/1.0/boardgame/user/"+username;
    $.ajax({
        type: 'GET',
        url: appContext + urlPath,
        dataType: 'json',
        success: function (data) {
            generateCollection(data);
        }
    });
}

function generateCollection(data) {
    if(data.iTotalRecords == 0) {
        $('#collection-results').append($('<div>', {
            text: "No collections found!"
        }));
    }

    for(var i=0;i<data.boardGames.length;i++) {
        var source   = $("#collection-template").html();
        var template = Handlebars.compile(source);

        var thumbnail = "http://localhost:8080/bangbangbang/static/images/shield.png";
        if(data.boardGames[i].thumbnail) {
            var thumbnail = data.boardGames[i].thumbnail;
        }
        var context = {name: data.boardGames[i].name,
            description: data.boardGames[i].description,
            minPlayers: data.boardGames[i].minPlayers,
            maxPlayers: data.boardGames[i].maxPlayers,
            thumbnail: thumbnail,
            yearPublished: data.boardGames[i].yearPublished,
            playingTime: data.boardGames[i].playingTime,
            objectId: data.boardGames[i].objectId
        }
        var html    = template(context);
        $('#collection-results').append(html);
    }
}

function onRemoveThisClick(objectId, username) {
    var username = $('#currentLoggedInUser').text();
    var urlPath = "/rest/1.0/boardgame/user/" + username + "/" + objectId;
    $.ajax({
        type: 'DELETE',
        url: appContext + urlPath,
        dataType: 'json',
        success: function (data) {
            $('#collection-results').html('');
            getCollection();
        }
    });
}
