<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="layout" content="main"/>
    <title>Login</title>
    <style>
    /* خلفية تغطي الصفحة كلها */
    body {
        margin: 0;
        height: 100vh;
        font-family: Arial, sans-serif;
        background: url('https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=1470&q=80') no-repeat center center fixed;
        background-size: cover;
        display: flex;
        justify-content: center;
        align-items: center;
        color: #fff;
    }
    /* صندوق تسجيل الدخول */
    .login-container {
        background-color: rgba(0, 0, 0, 0.6);
        padding: 2rem 3rem;
        border-radius: 10px;
        box-shadow: 0 0 15px rgba(0,0,0,0.7);
        width: 320px;
        box-sizing: border-box;
    }
    h2 {
        text-align: center;
        margin-bottom: 1.5rem;
    }
    label {
        display: block;
        margin-bottom: 0.5rem;
        font-weight: bold;
    }
    input[type="text"], input[type="password"] {
        width: 100%;
        padding: 0.5rem;
        border: none;
        border-radius: 5px;
        margin-bottom: 1rem;
        box-sizing: border-box;
    }
    .alert {
        background-color: #ff4d4d;
        padding: 0.75rem;
        border-radius: 5px;
        margin-bottom: 1rem;
        text-align: center;
        font-weight: bold;
    }
    input[type="submit"] {
        width: 100%;
        background-color: #0066cc;
        color: white;
        font-weight: bold;
        border: none;
        padding: 0.75rem;
        border-radius: 5px;
        cursor: pointer;
        transition: background-color 0.3s ease;
    }
    input[type="submit"]:hover {
        background-color: #004080;
    }
    p {
        text-align: center;
        margin-top: 1rem;
    }
    p a {
        color: #66b3ff;
        text-decoration: none;
        font-weight: bold;
    }
    p a:hover {
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

    <g:form controller="login" action="auth" method="POST">
        <div>
            <label for="username">Username</label>
            <g:textField name="username" id="username" required="true" autofocus="autofocus"/>
        </div>
        <div>
            <label for="password">Password</label>
            <g:passwordField name="password" id="password" required="true"/>
        </div>
        <div>
            <input type="submit" value="Login" />
        </div>
    </g:form>

    <p>Don't have an account? <g:link controller="auth" action="register">Register here</g:link>.</p>
</div>
</body>
</html>
