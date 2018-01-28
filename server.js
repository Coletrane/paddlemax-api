const http = require("http")
const path = require("path")
const methods = require("methods")
const express = require("express")
const cors = require("cors")
const errorhandler = require("errorhandler")
const bodyParser = require('body-parser')
const jwt = require('express-jwt')


// Config
require("dotenv").config()
const config = require('./config')
const prod = process.env.NODE_ENV === "production"

// App setup
// DB setup is done by .sequelizerc and config/db.js
const app = express()
app.use(cors())
app.use(bodyParser.json())

// Set up auth
if ( !process.env.AUTH0_DOMAIN
  || !process.env.AUTH0_AUDIENCE) {
  throw "Set AUTH0_DOMAIN, and AUTH0_AUDIENCE in .env!"
}
app.use(config.jwt)

// Models and routes
require('./models/User')
require('./routes')


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

// Start server
const server = app.listen(process.env.PORT || 3000, () => {
  console.log('PaddleMax API listening on port ' + server.address().port)
})
