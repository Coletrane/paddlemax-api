const router = require('express').Router()

router.post('/users/login', (req, res, next) => {
  if (!req.body.user.email) {
    return res.status(401)
  }


})

module.exports = router
