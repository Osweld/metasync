<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Activate Your Account</title>
</head>
<body style="font-family: Arial, sans-serif; background-color: #f5f5f5; padding: 20px;">
<table width="100%" cellpadding="0" cellspacing="0" style="max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px;">
    <tr>
        <td style="padding: 24px;">
            <h2 style="color: #111827; margin-top: 0;">Hello ${name}!</h2>

            <p style="color: #374151;">
                Thank you for signing up on <strong>${appName}</strong>.
            </p>

            <p style="color: #374151;">
                To activate your account, click the button below:
            </p>

            <p style="text-align: center; margin: 24px 0;">
                <a href="${activationLink}"
                   style="background-color: #2563eb; color: #ffffff; padding: 12px 24px;
                          text-decoration: none; border-radius: 6px; display: inline-block;">
                    Activate Account
                </a>
            </p>

            <p style="color: #6b7280; font-size: 14px;">
                If the button doesn't work, copy and paste this link into your browser:
                <br/>
                <span style="word-break: break-all;">${activationLink}</span>
            </p>

            <p style="color: #9ca3af; font-size: 12px; margin-top: 32px;">
                If you didn't create an account, you can ignore this email.
            </p>
        </td>
    </tr>
</table>
</body>
</html>
