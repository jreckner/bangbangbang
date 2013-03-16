<%@ page import="org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes" %>
<!doctype html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<title><g:layoutTitle default="${meta(name: 'app.name')}"/></title>
		<meta name="description" content="">
		<meta name="author" content="">

        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href='http://fonts.googleapis.com/css?family=Marcellus+SC' rel='stylesheet' type='text/css'>
        <link href='http://fonts.googleapis.com/css?family=Open+Sans:400italic,600italic,700italic,400,600,700' rel='stylesheet' type='text/css'>
        %{--<link rel="stylesheet" href="${resource(dir: 'css', file: 'customBootstrap.css')}" type="text/css">--}%

		<!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
		<!--[if lt IE 9]>
			<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
		<![endif]-->

        <r:require modules="customBootstrap"/>
		%{--<r:require modules="scaffolding"/>--}%

		<!-- Le fav and touch icons -->
		<link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">

		<g:layoutHead/>
		<r:layoutResources/>
	</head>

	<body>

		<nav class="navbar navbar-fixed-top">
			<div class="navbar-inner">
				<div class="container-fluid">

					<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
					</a>

					<a class="brand" href="${createLink(uri: '/')}">Lexington Board Gamers</a>

					<div class="nav-collapse collapse">

                        <shiro:authenticated>
                            <p class="navbar-text pull-right" id="loginInfo">
                                <span style="padding: 10px 15px;">
                                    Logged in as
                                    <a class="navbar-link" href="#"><shiro:principal/></a>
                                </span>
                                <a class="navbar-link" href="${createLink(uri: '/auth/signOut')}">Sign out</a>
                            </p>
                        </shiro:authenticated>
                        <shiro:notAuthenticated>
                            <p class="navbar-text pull-right" id="registerLink">
                                <a class="navbar-link" href="${createLink(uri: '/registration')}">Register</a>
                            </p>
                            <form action="${createLink(uri: '/auth/signIn')}" class="form-signin navbar-form pull-right">
                                <input class="span2 top-login" type="text" name="username" placeholder="Email">
                                <input class="span2 top-login" type="password" name="password" placeholder="Password">
                                <button class="btn btn-primary" type="submit">Sign in</button>
                            </form>
                        </shiro:notAuthenticated>

						<ul class="nav">
							<li<%= request.forwardURI == "${createLink(uri: '/')}" ? ' class="active"' : '' %>><a href="${createLink(uri: '/')}">Home</a></li>
                            <li<%= request.forwardURI == "${createLink(uri: '/games')}" ? ' class="active"' : '' %>><a href="${createLink(uri: '/games')}">Game Search</a></li>
                            <li<%= request.forwardURI == "${createLink(uri: '/forums')}" ? ' class="active"' : '' %>><a href="${createLink(uri: '/')}">Forums</a></li>
                            <li<%= request.forwardURI == "${createLink(uri: '/about')}" ? ' class="active"' : '' %>><a href="${createLink(uri: '/about')}">About</a></li>
						</ul>
					</div>
				</div>
			</div>
		</nav>

		<div class="container-fluid container-full">
			<g:layoutBody/>

			<hr>

			<footer>
				<p>&copy; Wireblend 2013</p>
			</footer>
		</div>

		<r:layoutResources/>

	</body>
</html>