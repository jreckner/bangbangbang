<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Registration Complete</title>
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
            <h3 class="form-signin-heading">You have been sent an activation email. You must activate your account before you may continue to login.</h3>
        </div>
    </section>
</div>

</body>
</html>