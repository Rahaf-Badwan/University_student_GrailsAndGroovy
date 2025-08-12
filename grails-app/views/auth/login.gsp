<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Login</title>
    <style>
    body {
        margin: 0;
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        background-color: #003366; /* كحلي */
        color: white;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
    }
    .login-container {
        background-color: rgba(255, 255, 255, 0.1);
        padding: 2.5rem 3rem;
        border-radius: 10px;
        box-shadow: 0 8px 16px rgba(0,0,0,0.4);
        width: 320px;
        text-align: center;
    }
    h2 {
        margin-bottom: 1.5rem;
        font-weight: 700;
        letter-spacing: 1px;
    }
    label {
        display: block;
        text-align: left;
        margin-bottom: 0.3rem;
        font-weight: 600;
    }
    input[type="text"], input[type="password"] {
        width: 100%;
        padding: 0.5rem 0.75rem;
        margin-bottom: 1.2rem;
        border: none;
        border-radius: 5px;
        font-size: 1rem;
        box-sizing: border-box;
    }
    input[type="text"]:focus, input[type="password"]:focus {
        outline: none;
        box-shadow: 0 0 5px 2px #4caf50;
    }
    .btn-login {
        background-color: #4caf50; /* أخضر */
        border: none;
        padding: 0.7rem;
        width: 100%;
        border-radius: 5px;
        font-size: 1.1rem;
        font-weight: 600;
        color: white;
        cursor: pointer;
        transition: background-color 0.3s ease;
    }
    .btn-login:hover {
        background-color: #388e3c;
    }
    .alert {
        background-color: #f44336; /* أحمر */
        color: white;
        padding: 0.7rem;
        margin-bottom: 1rem;
        border-radius: 5px;
    }
    .register-link {
        margin-top: 1rem;
        font-size: 0.9rem;
    }
    .register-link a {
        color: #4caf50;
        text-decoration: none;
        font-weight: 600;
    }
    .register-link a:hover {
        text-decoration: underline;
    }
    </style>
</head>
<body>
<div class="login-container">
    <h2>Login</h2>

    <g:if test="${flash.message}">
        <div class="alert">${flash.message}</div>
    </g:if>

    <g:form controller="login" action="auth">
        <label for="username">Username</label>
        <g:textField name="username" required="true" autofocus="true" />

        <label for="password">Password</label>
        <g:passwordField name="password" required="true" />

        <g:submitButton name="login" value="Login" class="btn-login" />
    </g:form>

    <p class="register-link">Don't have an account? <g:link controller="auth" action="register">Register here</g:link>.</p>
</div>
</body>
</html>
