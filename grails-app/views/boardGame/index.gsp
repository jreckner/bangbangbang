<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Game Search</title>
    <meta name="layout" content="bootstrap"/>

</head>

<body>

<g:form action="searchBoardGames">

    <div class="row-fluid">

        <section id="main" class="span12">

            <div class="hero-unit">

                <div class="span4">
                    <h2 class="form-signin-heading">Search for Games</h2>
                    <input type="text" name="gameSearch" class="input-block-level"  placeholder="Game to look up.."/>
                    <button class="btn btn-primary" type="submit">Search</button>
                </div>
                <div class="span2">
                    <p>
                        &nbsp;
                    </p>
                </div>
                <div class="span6">
                    &nbsp;
                </div>
            </div>
        </section>
    </div>

</g:form>

</body>
</html>