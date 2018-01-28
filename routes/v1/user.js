const router = require('express').Router()
const jwtAuthz = require('express-jwt-authz')
const checkJwt = require('../../config/jwt')

const readAndUpdate = jwtAuthz([
  'read:user',
  'update:user'
])

router.post('/users/login', readAndUpdate, (req, res, next) => {
  if (!req.body.user.email) {
    return res.status(401)
  }


})

module.exports = router
