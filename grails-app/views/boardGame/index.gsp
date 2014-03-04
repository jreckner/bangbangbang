<%@ page import="org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Game Search</title>
    <meta name="layout" content="bootstrap"/>
    <script type="text/javascript" src="${resource(dir: 'js', file: 'games.js')}"></script>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'games.css')}" type="text/css">

    <script id="entry-template" type="text/x-handlebars-template" src="${resource(dir: 'js', file: 'games.js')}">
        <div class="entry">
          <h4>{{name}} ({{yearPublished}})</h4>
          <div style="width: 100%; overflow: hidden;">
              <div style="width: 200px; float: left;">
                  <img src="{{thumbnail}}" alt="{{name}}" title="{{name}}" >
              </div>
              <div style="margin-left: 220px;">
                  <div>
                      <br/><button class="btn btn-primary" type="submit" onclick="onIOwnThisClick('{{objectId}}', '<shiro:principal/>')">I Own This</button>
                  </div>
                  <div class="game-players">
                      Players: {{minPlayers}} - {{maxPlayers}}, Play Time: {{playingTime}} minutes
                  </div>
                  <div class="game-description" onclick="onToggleDescription('{{objectId}}')">
                      Description:
                      <div id="game-description-hide-{{objectId}}">
                          <img src="${g.resource( dir:'images', file:'down.png' ) }" width="32" height="32">
                      </div>
                      <div id="game-description-show-{{objectId}}" style="display:none;">
                          <img src="${g.resource( dir:'images', file:'up.png' ) }" width="32" height="32">
                          <br/>
                          {{{description}}}
                      </div>
                  </div>
              </div>
          </div>
          <div><p>&nbsp;</p></div>
        </div>
    </script>
</head>

<body>

%{--<g:form action="searchBoardGames">--}%

    <div class="row-fluid">

        <section id="main" class="span12">

            <div class="hero-unit">
                <div class="span4" style="background: url(images/shield.png) no-repeat;padding-left: 150px">
                    <h2 class="form-signin-heading">Search for Games</h2>
                    <input type="text" id="gameSearch" name="gameSearch" class="input-block-level"  placeholder="Game to look up..."/>
                    <button id="game-search-submit" class="btn btn-primary" type="submit">Search</button>
                    <input type="checkbox" id="exactGameSearch" name="exactGameSearch" value="1" />Exact Search
                </div>
                <div class="span4"></div>
                <div class="span4">
                    <ul>
                        <li style="list-style-type:none !important;"><i class="icon-th-list icon-white"></i>Track your games.</li>
                        <li style="list-style-type:none !important;"><i class="icon-user icon-white"></i><strike>See what your buddies are playing.</strike></li>
                        <li style="list-style-type:none !important;"><i class="icon-thumbs-up icon-white"></i><strike>Write and share reviews.</strike></li>
                        <li style="list-style-type:none !important;"><i class="icon-search icon-white"></i><strike>Find a game in your area.</strike></li>
                        <li style="list-style-type:none !important;"><i class="icon-comment icon-white"></i><strike>Join a game discussion.</strike></li>
                    </ul>
                </div>
            </div>

            <div class="row-fluid">
                <div class="span1">
                    <p>&nbsp;</p>
                </div>
                <div class="span11">
                    <div id="search-loading"></div>
                    <div id="search-results"></div>
                </div>
            </div>
        </section>
    </div>

%{--</g:form>--}%

</body>
</html>
