<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>My Collection</title>
    <meta name="layout" content="bootstrap"/>
    <script type="text/javascript" src="${resource(dir: 'js', file: 'collection.js')}"></script>

    <script id="collection-template" type="text/x-handlebars-template" src="${resource(dir: 'js', file: 'collection.js')}">
        <div class="entry">
          <h4>{{name}} ({{yearPublished}})</h4>
          <div style="width: 100%; overflow: hidden;">
              <div style="width: 200px; float: left;">
                  <img src="{{thumbnail}}" alt="{{name}}" title="{{name}}" >
              </div>
              <div style="margin-left: 220px;">
                  <div>
                      <br/><button class="btn btn-danger" type="submit" onclick="onRemoveThisClick('{{objectId}}', '<shiro:principal/>')">Remove This</button>
                  </div>
                  <div class="game-players">
                      Players: {{minPlayers}} - {{maxPlayers}}, Play Time: {{playingTime}} minutes
                  </div>
              </div>
          </div>
          <div><p>&nbsp;</p></div>
        </div>
    </script>
</head>

<body>
%{--<g:form action="getAllBoardGamesForUser">--}%


<div class="row-fluid">
    <section id="main" class="span12">
        <div class="hero-unit">
            <div class="span4" style="background: url(images/shield.png) no-repeat;padding-left: 150px">
                <h2 class="form-signin-heading">Import Game</h2>
                <input type="text" id="gameImport" name="gameImport" class="input-block-level"  placeholder="BoardGameGeeks Url to Import..."/>
                <form action="${createLink(uri: '/games')}">
                    <span class="help-inline">
                        <button id="game-import-submit" class="btn btn-primary" type="submit" disabled="true">Import</button>
                        <input class="btn btn-primary" type="submit" value="Search">
                    </span>
                </form>
            </div>
        </div>

        <div class="row-fluid">
            <div class="span1">
                <p>&nbsp;</p>
            </div>
            <div class="span11">
                <div id="collection-results"></div>
            </div>
        </div>
    </section>
</div>

%{--</g:form>--}%

</body>
</html>