const http = require("http")
const path = require("path")
const methods = require("methods")
const express = require("express")
const cors = require("cors")
const errorhandler = require("errorhandler")
const bodyParser = require('body-parser')
const jwt = require('express-jwt')
const passport = require('passport')
const FacebookTokenStrategy = require('passport-facebook-token')
const models = require('./models')

// Config
require("dotenv").config()
const config = require('./config')
const prod = process.env.NODE_ENV === "production"

// App setup
// DB setup is done by .sequelizerc and config/db.js
const app = express()
app.use(cors())
app.use(bodyParser.json())

// Set up passport
passport.use(new FacebookTokenStrategy({
  clientID: process.env.FACEBOOK_APP_ID,
  clientSecret: process.env.FACEBOOK_APP_SECRET
  }, (accessToken, refreshToken, profile, done) => {
    models.User.findOrCreate({facebookId: profile.id}, (err, user) => {
      return done(error, user)
    })
  }
))
app.use(passport.initialize())
app.use(passport.session())

// Set up routes
app.use(require('./routes'))


// Error handlers
if (!prod) {
  app.use(errorhandler())
  app.use((err, req, res, next) => {
    console.log(err.stack)

    res.status(err.status || 500)

    res.json({
      "errors": {
        message: err.message,
        error: err
      }
    })
  })
}

if (prod) {
  app.use((err, req, res, next) => {
    res.status(err.status || 500)
    res.json({
      "errors": {
        message: err.message,
        error: {}
      }
    })
  })
}

module.exports = app

// Start server
const server = app.listen(process.env.PORT || 3000, () => {
  console.log('PaddleMax API listening on port ' + server.address().port)
})
