<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Register</title>
    <meta name="layout" content="bootstrap"/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'registration.css')}" type="text/css">
</head>

<body>


<g:if test="${flash.message}">
    <div class="alert alert-info">${flash.message}</div>
</g:if>

<g:hasErrors bean="${user}">
    <div class="alert alert-error">
        <g:renderErrors bean="${user}" as="list"/>
    </div>
</g:hasErrors>

<g:form action="register">

    <div class="row-fluid">

        <section id="main" class="span12">

            <div class="hero-unit">

                <div class="span4">
                    <h2 class="form-signin-heading">Signup</h2>
                    <input type="text" name="username" value="${username}" class="input-block-level"  placeholder="Email"/>
                    <input type="password" name="password" value="" class="input-block-level" placeholder="Password" />
                    <input type="password" name="password2" value="" class="input-block-level" placeholder="Confirm Password" />
                    <button class="btn btn-primary" type="submit">Register</button>
                </div>
                <div class="span2">
                    <p>
                        &nbsp;
                    </p>
                </div>
                <div class="span6">
                    <p>
                        Welcome!<br/>
                        You will be emailed a link to activate your account.
                    </p>
                </div>
            </div>
        </section>
    </div>

</g:form>

</body>
</html>