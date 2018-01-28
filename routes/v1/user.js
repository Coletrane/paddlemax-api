const router = require('express').Router()
const jwtAuthz = require('express-jwt-authz')
const jwt = require('../../config/jwt')

// TODO: might not need this
const readAndUpdate = jwtAuthz([
  'read:user',
  'update:user'
])

router.post('/user/login', (req, res, next) => {
  if (!req.body.user.email
  && (!req.body.user.facebookAuthToken) || (!req.body.user.googleAuthToken)) {
    return res.status(400)
  }
})

router.get('/user/me', jwt, (req, res, next) => {
  return 1
})

module.exports = router
