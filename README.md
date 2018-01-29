To run: get the .env file from me, or create your own: 

    UTH0_AUDIENCE=
    AUTH0_DOMAIN=
    FACEBOOK_APP_ID=
    FACEBOOK_APP_SECRET=

To test: create `test/fixtures-private.js` and put Auth0 test users in it

    module.exports = {
        clientAuthorized: {
                client_id:,
                client_secret:
            },
        clientUnauthorized: {
                client_id: ,
                client_secret:
            }
    }

To set up a File Watcher in IntelliJ/WebStorm: https://stackoverflow.com/questions/38883634/how-can-i-run-eslint-fix-on-my-javascript-in-intellij-idea-webstorm-other-je
    
