<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Activation Complete</title>
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

<div class="row-fluid">

    <section id="main" class="span12">

        <div class="hero-unit">
            <h3 class="form-signin-heading">Your activation is now complete. Please proceed to <a href="${createLink(uri: '/auth/signIn')}">login</a> now.</h3>
        </div>
    </section>
</div>

</body>
</html>