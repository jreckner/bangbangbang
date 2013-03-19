$( document ).ready(function() {
    $('#game-search-submit').click(function() {

        $('#search-results').html('');

        var searchTerm = $('#gameSearch').val();
        $.ajax({
          type: 'GET',
          url: appContext + "/rest/1.0/boardgame/search/"+searchTerm,
          dataType: 'json',
          success: function (data) {

              if(data.iTotalRecords == 0) {
                $('#search-results').append($('<div>', {
                    text: "No results found!"
                }));
              }

//              console.log(data.boardGames.length);
              for(var i=0;i<data.boardGames.length;i++) {
//                  console.log(data.boardGames[i].description);
//                  $('#search-results').append($('<div>', {
//                    text: data.boardGames[i].name + "----" + data.boardGames[i].description
//                  }));

                  var source   = $("#entry-template").html();
                  var template = Handlebars.compile(source);
                  var context = {name: data.boardGames[i].name,
                      description: data.boardGames[i].description,
                      minPlayers: data.boardGames[i].minPlayers,
                      maxPlayers: data.boardGames[i].maxPlayers,
                      thumbnail: data.boardGames[i].thumbnail,
                      yearPublished: data.boardGames[i].yearPublished,
                      playingTime: data.boardGames[i].playingTime
                  }
                  var html    = template(context);
                  $('#search-results').append(html);

              }



            }
        });
    });
});

