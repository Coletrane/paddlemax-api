const express = require('express')
const cors = require('cors')
const errorhandler = require('errorhandler')
const bodyParser = require('body-parser')
const models = require('./models')

// Config
require('dotenv').config()
// const config = require('./config')
const prod = process.env.NODE_ENV === 'production'

// App setup
const app = express()
app.use(cors())
app.use(bodyParser.json())

// Set up routes
app.use(require('./routes'))

// Error handlers
if (!prod) {
  app.use(errorhandler())
  app.use((err, req, res, next) => {
    console.log(err.stack)

    res.status(err.status || 500)

    res.json({
      'errors': {
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
      'errors': {
        message: err.message,
        error: {}
      }
    })
  })
}

// DB setup
models.sequelize.sync().then(() => {
  // Start server
  const server = app.listen(process.env.PORT || 3000, () => {
    console.log('PaddleMax API listening on port ' + server.address().port)
  })
})

// For testing
module.exports = app
