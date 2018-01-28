const router = require('express').Router()
const jwtAuthz = require('express-jwt-authz')
const checkJwt = require('../../config/jwt')

const readAndUpdate = jwtAuthz([
  'read:user',
  'update:user'
])

router.post('/user/login', (req, res, next) => {
  if (!req.body.user.email) {
    return res.status(401)
  }




})

router.get('/user/me', readAndUpdate, (req, res, next) => {
  return 1
})

module.exports = router
