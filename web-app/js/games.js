$( document ).ajaxStart(function() {
    $( "#search-loading" ).show();
});
$( document ).ajaxStop(function() {
    $( "#search-loading" ).hide();
});

$( document ).ready(function() {
    $('#game-search-submit').click(function() {

        $('#search-results').html('');

        var searchTerm = $('#gameSearch').val();
        if(document.getElementById('exactGameSearch').checked) {
            var urlPath = "/rest/1.0/boardgame/search/"+searchTerm+"?exact=1";
        }
        else {
            var urlPath = "/rest/1.0/boardgame/search/"+searchTerm;
        }

        $.ajax({
          type: 'GET',
          url: appContext + urlPath,
          dataType: 'json',
          success: function (data) {

              if(data.iTotalRecords == 0) {
                $('#search-results').append($('<div>', {
                    text: "No results found!"
                }));
              }

              for(var i=0;i<data.boardGames.length;i++) {
                  var source   = $("#entry-template").html();
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
                  $('#search-results').append(html);
              }
            }
        });
    });
});

function onIOwnThisClick(objectId, username) {
    var urlPath = "/rest/1.0/boardgame/user/" + username + "/" + objectId;
    $.ajax({
        type: 'POST',
        global: false, // Disable our global ajaxStart and ajaxStop functions.
        url: appContext + urlPath,
        dataType: 'json',
        success: function (data) {
        }
    });
}

function onToggleDescription(id) {
    $("#game-description-hide-"+id).slideToggle('fast');
    $("#game-description-show-"+id).slideToggle('fast');
}


