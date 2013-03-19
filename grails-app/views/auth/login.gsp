<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="bootstrap"/>
  %{--<meta name="layout" content="main" />--}%
  <title>Login</title>
  <link rel="stylesheet" href="${resource(dir: 'css', file: 'login.css')}" type="text/css">
</head>
<body>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
<g:form action="signIn" class="form-signin">

    <div class="row-fluid">

        <section id="main" class="span12">

            <div class="hero-unit">
                <div class="span4">
                    <h2 class="form-signin-heading">Please sign in</h2>
                    <input type="hidden" name="targetUri" value="${targetUri}" />
                    <input type="text" name="username" value="${username}" class="input-block-level"  placeholder="Username"/>
                    <input type="password" name="password" value="" class="input-block-level" placeholder="Password" />
                    <label class="checkbox">
                        <g:checkBox name="rememberMe" value="${rememberMe}" /> Remember me
                    </label>

                    <button class="btn btn-primary" type="submit">Sign in</button>
                </div>
            </div>
        </section>
    </div>

</g:form>
</body>
</html>
