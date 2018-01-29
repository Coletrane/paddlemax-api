const router = require('express').Router()
const jwtAuthz = require('express-jwt-authz')
const jwt = require('../../config/jwt')
const passport = require('passport')
const User = require('../../models').User

// TODO: might not need this
const readAndUpdate = jwtAuthz([
  'read:user',
  'update:user'
])

router.post('/user/login', (req, res, next) => {
  console.log(req.body)
  if (!req.body.email
  && (!req.body.facebookAuthToken) || (!req.body.googleAuthToken)) {
    res.status(400).send("Email and access token are required")
  }

  if (req.body.facebookAuthToken) {
    passport.authenticate('facebook-token', (err, user, info) => {
      console.log(err, user, info)
      res.ok()
    })
  }

})

router.get('/user/me', jwt, (req, res, next) => {
  if (!req.email) {
    res.status(400).send("Email is required")
  }

  const user = User.findAll({
    where: {
      email: req.email
    }
  })

  res.status(200).body(user).send()
})

module.exports = router
