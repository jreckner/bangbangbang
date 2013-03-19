<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Game Search</title>
    <meta name="layout" content="bootstrap"/>
    <script type="text/javascript" src="${resource(dir: 'js', file: 'games.js')}"></script>

    <script id="entry-template" type="text/x-handlebars-template">
        <div class="entry">
          <h4>{{name}}</h4>
            <div>
                <img src="{{thumbnail}}" alt="{{name}}" title="{{name}}" >
            </div>
          <div class="game-description">
            {{{description}}}
          </div>
          <div class="game-players">
              Players: {{minPlayers}} - {{maxPlayers}}
          </div>
        </div>
    </script>
</head>

<body>

%{--<g:form action="searchBoardGames">--}%

    <div class="row-fluid">

        <section id="main" class="span12">

            <div class="hero-unit">

                <div class="span4">
                    <h2 class="form-signin-heading">Search for Games</h2>
                    <input type="text" id="gameSearch" name="gameSearch" class="input-block-level"  placeholder="Game to look up.."/>
                    <button id="game-search-submit" class="btn btn-primary" type="submit">Search</button>
                </div>
                <div class="span2">
                    <p>
                        &nbsp;
                    </p>
                </div>
                <div class="span6">
                    <div id="search-results"></div>
                </div>
            </div>
        </section>
    </div>

%{--</g:form>--}%

</body>
</html>