<!doctype html>
<html>
	<head>
		<meta name="layout" content="bootstrap"/>
		<title>Lexington Board Gamers</title>
	</head>

	<body>
		<div class="row-fluid">
			%{--<aside id="application-status" class="span3">--}%
				%{--<div class="well sidebar-nav">--}%
					%{--<h5>Application Status</h5>--}%
					%{--<ul>--}%
						%{--<li>App version: <g:meta name="app.version"/></li>--}%
						%{--<li>Grails version: <g:meta name="app.grails.version"/></li>--}%
						%{--<li>Groovy version: ${org.codehaus.groovy.runtime.InvokerHelper.getVersion()}</li>--}%
						%{--<li>JVM version: ${System.getProperty('java.version')}</li>--}%
						%{--<li>Controllers: ${grailsApplication.controllerClasses.size()}</li>--}%
						%{--<li>Domains: ${grailsApplication.domainClasses.size()}</li>--}%
						%{--<li>Services: ${grailsApplication.serviceClasses.size()}</li>--}%
						%{--<li>Tag Libraries: ${grailsApplication.tagLibClasses.size()}</li>--}%
					%{--</ul>--}%
					%{--<h5>Installed Plugins</h5>--}%
					%{--<ul>--}%
						%{--<g:each var="plugin" in="${applicationContext.getBean('pluginManager').allPlugins}">--}%
							%{--<li>${plugin.name} - ${plugin.version}</li>--}%
						%{--</g:each>--}%
					%{--</ul>--}%
				%{--</div>--}%
			%{--</aside>--}%

			<section id="main" class="span12">

				<div class="hero-unit">
                    <div class="span8" style="background: url(images/shield.png) no-repeat;padding-left: 150px">
                        %{--<img src="images/shield.png">--}%
                        <h1>Game On.</h1>
                        <h3>Roll it, shuffle it, deal it.</h3>
                    </div>
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

					<div class="span4" style="padding-left: 20px;">
						<h2>Try It</h2>
						<p>This demo app includes a couple of controllers to show off its features.</p>
						<ul class="nav nav-list">
							<g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
								<li><g:link controller="${c.logicalPropertyName}">${c.naturalName}</g:link></li>
							</g:each>
						</ul>
					</div>

					<div class="span4">
						<h3>Currently Being Played</h3>
						<p>
                            <ul>
                                <li>Eclipse</li>
                                <li>Pandemic</li>
                                <li>Dominion</li>
                            </ul>
						</p>
					</div>

					<div class="span4">
						<h2>Project Location</h2>
						<p>You can find this project on <a href="https://github.com/jreckner/bangbangbang">GitHub</a>.</p>
					</div>

				</div>

			</section>
		</div>

	</body>
</html>
